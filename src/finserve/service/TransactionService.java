package finserve.service;

import finserve.ds.*;
import finserve.model.*;

import java.util.HashMap;

/**
 * Core transaction engine of the system.
 *
 * DS Used:
 *  - TransactionLinkedList : Per-account transaction history
 *  - UndoStack             : Admin rollback (undo last transaction)
 *  - FraudAlertQueue       : Priority queue for fraud/large-value alerts
 *  - ServiceRequestQueue   : FIFO queue for service request processing
 *  - TellerTokenQueue      : Circular queue for teller token system
 */
public class TransactionService {

    private static final double FRAUD_THRESHOLD = 100000.0; // Rs. 1 Lakh triggers alert

    // Per-account transaction history stored in Linked Lists
    // accountNumber → TransactionLinkedList
    private HashMap<String, TransactionLinkedList> historyMap;

    private UndoStack undoStack;
    private FraudAlertQueue fraudQueue;
    private ServiceRequestQueue serviceQueue;
    private TellerTokenQueue tellerQueue;

    private AccountService accountService;
    private CustomerService customerService;

    private int txnCounter = 1;
    private int alertCounter = 1;
    private int srCounter = 1;

    public TransactionService(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
        historyMap = new HashMap<>();
        undoStack = new UndoStack();
        fraudQueue = new FraudAlertQueue();
        serviceQueue = new ServiceRequestQueue();
        tellerQueue = new TellerTokenQueue(10); // 10-slot circular token queue
    }

    // ─────────────────────────── DEPOSIT ───────────────────────────

    public boolean deposit(String accountNumber, double amount) {
        Account acc = accountService.findByNumber(accountNumber);
        if (acc == null) {
            System.out.println("  Account not found: " + accountNumber);
            return false;
        }

        acc.deposit(amount);
        Transaction txn = new Transaction(
                "TXN" + String.format("%04d", txnCounter++),
                Transaction.TransactionType.DEPOSIT,
                amount, accountNumber, null
        );

        recordTransaction(accountNumber, txn);
        System.out.printf("  Deposited Rs.%,.2f to %s. New Balance: Rs.%,.2f%n",
                amount, accountNumber, acc.getBalance());
        checkFraud(txn, acc);
        return true;
    }

    // ─────────────────────────── WITHDRAWAL ───────────────────────────

    public boolean withdraw(String accountNumber, double amount) {
        Account acc = accountService.findByNumber(accountNumber);
        if (acc == null) {
            System.out.println("  Account not found: " + accountNumber);
            return false;
        }
        if (acc.getBalance() < amount) {
            System.out.println("  Insufficient balance! Available: Rs." + acc.getBalance());
            return false;
        }

        acc.withdraw(amount);
        Transaction txn = new Transaction(
                "TXN" + String.format("%04d", txnCounter++),
                Transaction.TransactionType.WITHDRAWAL,
                amount, accountNumber, null
        );

        recordTransaction(accountNumber, txn);
        System.out.printf("  Withdrew Rs.%,.2f from %s. New Balance: Rs.%,.2f%n",
                amount, accountNumber, acc.getBalance());
        checkFraud(txn, acc);
        return true;
    }

    // ─────────────────────────── TRANSFER ───────────────────────────

    public boolean transfer(String fromAcc, String toAcc, double amount) {
        Account from = accountService.findByNumber(fromAcc);
        Account to = accountService.findByNumber(toAcc);

        if (from == null || to == null) {
            System.out.println("  One or both accounts not found.");
            return false;
        }
        if (from.getBalance() < amount) {
            System.out.println("  Insufficient balance in source account.");
            return false;
        }

        from.withdraw(amount);
        to.deposit(amount);

        Transaction txn = new Transaction(
                "TXN" + String.format("%04d", txnCounter++),
                Transaction.TransactionType.TRANSFER,
                amount, fromAcc, toAcc
        );

        recordTransaction(fromAcc, txn);
        recordTransaction(toAcc, txn);
        System.out.printf("  Transferred Rs.%,.2f from %s to %s%n", amount, fromAcc, toAcc);
        checkFraud(txn, from);
        return true;
    }

    // ─────────────────────────── UNDO (ADMIN) ───────────────────────────

    /**
     * Admin Rollback: Undo the last transaction using the Stack.
     * Pops from UndoStack and reverses the balance operation.
     */
    public void undoLastTransaction() {
        Transaction last = undoStack.pop();
        if (last == null) {
            System.out.println("  Nothing to undo.");
            return;
        }
        if (last.isReversed()) {
            System.out.println("  Transaction already reversed.");
            return;
        }

        last.setReversed(true);
        Account from = accountService.findByNumber(last.getFromAccount());

        if (last.getType() == Transaction.TransactionType.DEPOSIT && from != null) {
            from.withdraw(last.getAmount());
            System.out.printf("  [UNDO] Reversed DEPOSIT of Rs.%,.2f on %s%n",
                    last.getAmount(), last.getFromAccount());

        } else if (last.getType() == Transaction.TransactionType.WITHDRAWAL && from != null) {
            from.deposit(last.getAmount());
            System.out.printf("  [UNDO] Reversed WITHDRAWAL of Rs.%,.2f on %s%n",
                    last.getAmount(), last.getFromAccount());

        } else if (last.getType() == Transaction.TransactionType.TRANSFER) {
            Account toAcc = accountService.findByNumber(last.getToAccount());
            if (from != null && toAcc != null) {
                toAcc.withdraw(last.getAmount());
                from.deposit(last.getAmount());
                System.out.printf("  [UNDO] Reversed TRANSFER of Rs.%,.2f from %s to %s%n",
                        last.getAmount(), last.getFromAccount(), last.getToAccount());
            }
        }
    }

    // ─────────────────────────── HISTORY ───────────────────────────

    public void printHistory(String accountNumber) {
        TransactionLinkedList list = historyMap.get(accountNumber);
        if (list == null) {
            System.out.println("  No transaction history for: " + accountNumber);
            return;
        }
        System.out.println("  Transaction History for " + accountNumber + ":");
        list.printAll();
    }

    public void printAuditTrail() {
        undoStack.printAuditTrail();
    }

    // ─────────────────────────── FRAUD ───────────────────────────

    private void checkFraud(Transaction txn, Account acc) {
        if (txn.getAmount() >= FRAUD_THRESHOLD) {
            double riskScore = Math.min(10.0, txn.getAmount() / 100000.0);
            FraudAlert alert = new FraudAlert(
                    "ALRT" + String.format("%03d", alertCounter++),
                    txn.getTransactionId(),
                    acc.getAccountNumber(),
                    txn.getAmount(),
                    riskScore,
                    "Large transaction detected"
            );
            fraudQueue.addAlert(alert);

            // Update customer risk score
            Customer owner = customerService.findById(acc.getOwnerId());
            if (owner != null) {
                owner.setRiskScore(Math.max(owner.getRiskScore(), riskScore));
            }
        }
    }

    public void processFraudAlert() { fraudQueue.processNext(); }
    public void viewFraudAlerts()   { fraudQueue.printAllAlerts(); }

    // ─────────────────────────── SERVICE REQUESTS ───────────────────────────

    public void raiseServiceRequest(String customerId,
                                    ServiceRequest.RequestType type, String description) {
        String id = "SR" + String.format("%03d", srCounter++);
        ServiceRequest sr = new ServiceRequest(id, customerId, type, description);
        serviceQueue.enqueue(sr);
    }

    public void processNextServiceRequest() { serviceQueue.dequeue(); }
    public void viewServiceRequests()       { serviceQueue.printAll(); }

    // ─────────────────────────── TELLER TOKENS ───────────────────────────

    public void issueToken()      { tellerQueue.issueToken(); }
    public void callNextToken()   { tellerQueue.callNext(); }
    public void viewTokenStatus() { tellerQueue.printStatus(); }

    // ─────────────────────────── HELPER ───────────────────────────

    // Store transaction in per-account linked list AND push to undo stack
    private void recordTransaction(String accountNumber, Transaction txn) {
        historyMap.computeIfAbsent(accountNumber, k -> new TransactionLinkedList())
                  .addFirst(txn);
        undoStack.push(txn);
    }
}

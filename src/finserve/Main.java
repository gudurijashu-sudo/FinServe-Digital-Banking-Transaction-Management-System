package finserve;

import finserve.model.*;
import finserve.service.*;
import finserve.util.SearchSortUtil;

import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║   FinServe – Digital Banking & Transaction Management    ║
 * ║              Console-Based DSA Project                   ║
 * ║         2nd Year B.E. Computer Science Project           ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * Entry point: menu-driven console application.
 */
public class Main {

    static CustomerService customerService = new CustomerService();
    static AccountService accountService = new AccountService();
    static TransactionService txnService = new TransactionService(accountService, customerService);
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║   Welcome to FinServe Banking System v1.0    ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");

        loadSampleData(); // Pre-load demo data

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("  Enter choice: ");
            System.out.println();

            switch (choice) {
                case 1  -> customerMenu();
                case 2  -> accountMenu();
                case 3  -> transactionMenu();
                case 4  -> serviceRequestMenu();
                case 5  -> fraudMenu();
                case 6  -> tellerMenu();
                case 7  -> adminMenu();
                case 8  -> SearchSortUtil.printComplexityTable();
                case 0  -> { running = false; System.out.println("\n  Goodbye! Thank you for using FinServe.\n"); }
                default -> System.out.println("  Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    // ─────────────── MAIN MENU ───────────────

    static void printMainMenu() {
        System.out.println("\n  ══════════════════════════════════");
        System.out.println("        FINSERVE MAIN MENU          ");
        System.out.println("  ══════════════════════════════════");
        System.out.println("  1. Customer Management");
        System.out.println("  2. Account Management");
        System.out.println("  3. Transactions");
        System.out.println("  4. Service Requests");
        System.out.println("  5. Fraud Alerts");
        System.out.println("  6. Teller Token System");
        System.out.println("  7. Admin Panel (Undo/Audit)");
        System.out.println("  8. Algorithm Complexity Reference");
        System.out.println("  0. Exit");
        System.out.println("  ══════════════════════════════════");
    }

    // ─────────────── CUSTOMER MENU ───────────────

    static void customerMenu() {
        System.out.println("\n  --- Customer Management ---");
        System.out.println("  1. Add Customer");
        System.out.println("  2. View All Customers");
        System.out.println("  3. Find Customer by ID (HashMap O(1))");
        System.out.println("  4. Search Customer by Name (Naive O(n))");
        System.out.println("  5. Sort Customers by Balance");
        System.out.println("  6. Sort Customers by Risk Score");
        int c = readInt("  Enter choice: ");

        switch (c) {
            case 1 -> {
                String name  = readString("  Name: ");
                String phone = readString("  Phone: ");
                boolean kyc  = readString("  KYC Verified? (y/n): ").equalsIgnoreCase("y");
                customerService.addCustomer(name, phone, kyc);
            }
            case 2 -> { System.out.println("\n  All Customers:"); customerService.listAll(); }
            case 3 -> {
                String id = readString("  Enter Customer ID: ");
                Customer c2 = customerService.findById(id);
                System.out.println(c2 != null ? "  Found: " + c2 : "  Not found.");
            }
            case 4 -> {
                String name = readString("  Enter Name: ");
                Customer c2 = customerService.naiveSearchByName(name);
                System.out.println(c2 != null ? "  Found: " + c2 : "  Not found.");
            }
            case 5 -> customerService.sortByBalance();
            case 6 -> customerService.sortByRiskScore();
            default -> System.out.println("  Invalid choice.");
        }
    }

    // ─────────────── ACCOUNT MENU ───────────────

    static void accountMenu() {
        System.out.println("\n  --- Account Management ---");
        System.out.println("  1. Open New Account");
        System.out.println("  2. View All Accounts");
        System.out.println("  3. View Account by Number (HashMap O(1))");
        System.out.println("  4. Search Comparison (Naive vs HashMap)");
        System.out.println("  5. Setup EMI Schedule (Loan Account)");
        System.out.println("  6. View EMI Schedule");
        int c = readInt("  Enter choice: ");

        switch (c) {
            case 1 -> {
                String custId = readString("  Customer ID: ");
                Customer cust = customerService.findById(custId);
                if (cust == null) { System.out.println("  Customer not found."); break; }
                System.out.println("  Account Type: 1=SAVINGS  2=CURRENT  3=LOAN");
                int t = readInt("  Type: ");
                Account.AccountType type = (t == 2) ? Account.AccountType.CURRENT
                                         : (t == 3) ? Account.AccountType.LOAN
                                         : Account.AccountType.SAVINGS;
                double bal = readDouble("  Initial Deposit (Rs.): ");
                accountService.createAccount(cust, type, bal);
            }
            case 2 -> { System.out.println("\n  All Accounts:"); accountService.listAllAccounts(); }
            case 3 -> {
                String num = readString("  Account Number: ");
                Account acc = accountService.findByNumber(num);
                System.out.println(acc != null ? "  Found: " + acc : "  Not found.");
            }
            case 4 -> {
                String num = readString("  Account Number to search: ");
                accountService.searchComparison(num);
            }
            case 5 -> {
                String num = readString("  Loan Account Number: ");
                Account acc = accountService.findByNumber(num);
                if (acc == null) { System.out.println("  Account not found."); break; }
                double loan   = readDouble("  Loan Amount (Rs.): ");
                int months    = readInt("  Tenure (months): ");
                double rate   = readDouble("  Annual Interest Rate (%): ");
                acc.setupEmiSchedule(loan, months, rate);
                System.out.println("  EMI schedule created.");
            }
            case 6 -> {
                String num = readString("  Account Number: ");
                Account acc = accountService.findByNumber(num);
                if (acc == null) { System.out.println("  Account not found."); break; }
                acc.printEmiSchedule();
            }
            default -> System.out.println("  Invalid choice.");
        }
    }

    // ─────────────── TRANSACTION MENU ───────────────

    static void transactionMenu() {
        System.out.println("\n  --- Transactions ---");
        System.out.println("  1. Deposit");
        System.out.println("  2. Withdraw");
        System.out.println("  3. Transfer");
        System.out.println("  4. Transaction History (Linked List)");
        int c = readInt("  Enter choice: ");

        switch (c) {
            case 1 -> {
                String acc = readString("  Account Number: ");
                double amt = readDouble("  Amount (Rs.): ");
                txnService.deposit(acc, amt);
            }
            case 2 -> {
                String acc = readString("  Account Number: ");
                double amt = readDouble("  Amount (Rs.): ");
                txnService.withdraw(acc, amt);
            }
            case 3 -> {
                String from = readString("  From Account: ");
                String to   = readString("  To Account:   ");
                double amt  = readDouble("  Amount (Rs.): ");
                txnService.transfer(from, to, amt);
            }
            case 4 -> {
                String acc = readString("  Account Number: ");
                txnService.printHistory(acc);
            }
            default -> System.out.println("  Invalid choice.");
        }
    }

    // ─────────────── SERVICE REQUEST MENU ───────────────

    static void serviceRequestMenu() {
        System.out.println("\n  --- Service Requests (Queue) ---");
        System.out.println("  1. Raise New Request");
        System.out.println("  2. View Pending Requests");
        System.out.println("  3. Process Next Request (Dequeue)");
        int c = readInt("  Enter choice: ");

        switch (c) {
            case 1 -> {
                String custId = readString("  Customer ID: ");
                System.out.println("  Type: 1=BLOCK_CARD  2=UPDATE_KYC  3=CHEQUE_BOOK  4=LOAN_INQUIRY  5=OTHER");
                int t = readInt("  Request Type: ");
                ServiceRequest.RequestType[] types = ServiceRequest.RequestType.values();
                ServiceRequest.RequestType type = (t >= 1 && t <= 5) ? types[t - 1] : ServiceRequest.RequestType.OTHER;
                String desc = readString("  Description: ");
                txnService.raiseServiceRequest(custId, type, desc);
            }
            case 2 -> txnService.viewServiceRequests();
            case 3 -> txnService.processNextServiceRequest();
            default -> System.out.println("  Invalid choice.");
        }
    }

    // ─────────────── FRAUD MENU ───────────────

    static void fraudMenu() {
        System.out.println("\n  --- Fraud Alerts (Priority Queue) ---");
        System.out.println("  1. View All Fraud Alerts");
        System.out.println("  2. Process Highest-Risk Alert");
        int c = readInt("  Enter choice: ");
        switch (c) {
            case 1 -> txnService.viewFraudAlerts();
            case 2 -> txnService.processFraudAlert();
            default -> System.out.println("  Invalid choice.");
        }
    }

    // ─────────────── TELLER TOKEN MENU ───────────────

    static void tellerMenu() {
        System.out.println("\n  --- Teller Token System (Circular Queue) ---");
        System.out.println("  1. Issue Token to Customer");
        System.out.println("  2. Call Next Token");
        System.out.println("  3. View Token Queue Status");
        int c = readInt("  Enter choice: ");
        switch (c) {
            case 1 -> txnService.issueToken();
            case 2 -> txnService.callNextToken();
            case 3 -> txnService.viewTokenStatus();
            default -> System.out.println("  Invalid choice.");
        }
    }

    // ─────────────── ADMIN MENU ───────────────

    static void adminMenu() {
        System.out.println("\n  --- Admin Panel ---");
        System.out.println("  1. Undo Last Transaction (Stack pop)");
        System.out.println("  2. View Audit Trail (Stack)");
        int c = readInt("  Enter choice: ");
        switch (c) {
            case 1 -> txnService.undoLastTransaction();
            case 2 -> txnService.printAuditTrail();
            default -> System.out.println("  Invalid choice.");
        }
    }

    // ─────────────── SAMPLE DATA ───────────────

    /**
     * Pre-loads sample customers, accounts and transactions
     * so the viva demo has data ready without manual input.
     */
    static void loadSampleData() {
        System.out.println("\n  [System] Loading sample data...");

        Customer c1 = customerService.addCustomer("Arjun Sharma",   "9876543210", true);
        Customer c2 = customerService.addCustomer("Priya Nair",     "9123456789", true);
        Customer c3 = customerService.addCustomer("Rahul Gupta",    "9988776655", false);

        Account a1 = accountService.createAccount(c1, Account.AccountType.SAVINGS,  50000);
        Account a2 = accountService.createAccount(c1, Account.AccountType.LOAN,     0);
        Account a3 = accountService.createAccount(c2, Account.AccountType.CURRENT,  150000);
        Account a4 = accountService.createAccount(c3, Account.AccountType.SAVINGS,  20000);

        // Set up EMI schedule for Arjun's loan account
        a2.setupEmiSchedule(500000, 12, 10.5);

        // Sample transactions
        txnService.deposit(a1.getAccountNumber(), 25000);
        txnService.withdraw(a1.getAccountNumber(), 5000);
        txnService.deposit(a3.getAccountNumber(), 200000); // Will trigger fraud alert
        txnService.transfer(a3.getAccountNumber(), a1.getAccountNumber(), 30000);

        // Sample service requests
        txnService.raiseServiceRequest(c1.getCustomerId(),
                ServiceRequest.RequestType.CHEQUE_BOOK, "Please issue a new cheque book");
        txnService.raiseServiceRequest(c3.getCustomerId(),
                ServiceRequest.RequestType.UPDATE_KYC, "Want to submit Aadhaar");

        // Issue a couple of teller tokens
        txnService.issueToken();
        txnService.issueToken();
        txnService.issueToken();

        System.out.println("  [System] Sample data loaded successfully!\n");
        System.out.println("  Sample Account Numbers:");
        System.out.println("    Arjun Savings : " + a1.getAccountNumber());
        System.out.println("    Arjun Loan    : " + a2.getAccountNumber());
        System.out.println("    Priya Current : " + a3.getAccountNumber());
        System.out.println("    Rahul Savings : " + a4.getAccountNumber());
        System.out.println("  Sample Customer IDs:");
        System.out.println("    Arjun: " + c1.getCustomerId()
                + "  |  Priya: " + c2.getCustomerId()
                + "  |  Rahul: " + c3.getCustomerId());
    }

    // ─────────────── INPUT HELPERS ───────────────

    static int readInt(String prompt) {
        System.out.print(prompt);
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    static double readDouble(String prompt) {
        System.out.print(prompt);
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
}

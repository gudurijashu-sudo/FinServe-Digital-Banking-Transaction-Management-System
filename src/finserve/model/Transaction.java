package finserve.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single transaction (Deposit / Withdraw / Transfer).
 * These are stored inside a Linked List (TransactionHistory).
 */
public class Transaction {

    public enum TransactionType { DEPOSIT, WITHDRAWAL, TRANSFER }

    private String transactionId;
    private TransactionType type;
    private double amount;
    private String fromAccount;
    private String toAccount;   // null for Deposit/Withdrawal
    private String timestamp;
    private boolean reversed;   // True if admin rolled back this transaction

    public Transaction(String transactionId, TransactionType type,
                       double amount, String fromAccount, String toAccount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.reversed = false;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    // --- Getters ---
    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public double getAmount()        { return amount; }
    public String getFromAccount()   { return fromAccount; }
    public String getToAccount()     { return toAccount; }
    public String getTimestamp()     { return timestamp; }
    public boolean isReversed()      { return reversed; }

    public void setReversed(boolean reversed) { this.reversed = reversed; }

    @Override
    public String toString() {
        String to = (toAccount != null) ? " -> " + toAccount : "";
        String status = reversed ? " [REVERSED]" : "";
        return String.format("[%s] %s | Rs.%,.2f | %s%s | %s%s",
                transactionId, type, amount, fromAccount, to, timestamp, status);
    }
}

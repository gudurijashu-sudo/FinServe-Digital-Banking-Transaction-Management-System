package finserve.model;

/**
 * Represents a fraud alert triggered by a large or suspicious transaction.
 * Stored in a Priority Queue — higher risk score = processed first.
 *
 * Implements Comparable so Java's PriorityQueue can order by risk score.
 */
public class FraudAlert implements Comparable<FraudAlert> {

    private String alertId;
    private String transactionId;
    private String accountNumber;
    private double amount;
    private double riskScore;    // Higher = more dangerous, processed first
    private String description;

    public FraudAlert(String alertId, String transactionId,
                      String accountNumber, double amount, double riskScore, String description) {
        this.alertId = alertId;
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.riskScore = riskScore;
        this.description = description;
    }

    // PriorityQueue in Java is a Min-Heap by default.
    // We want MAX priority (higher risk first), so we negate the comparison.
    @Override
    public int compareTo(FraudAlert other) {
        return Double.compare(other.riskScore, this.riskScore); // descending
    }

    public String getAlertId()       { return alertId; }
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public double getAmount()        { return amount; }
    public double getRiskScore()     { return riskScore; }

    @Override
    public String toString() {
        return String.format("[ALERT %s] TxnID: %s | Acc: %s | Rs.%,.2f | Risk: %.1f | %s",
                alertId, transactionId, accountNumber, amount, riskScore, description);
    }
}

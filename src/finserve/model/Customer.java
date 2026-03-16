package finserve.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank customer.
 * Stores basic customer details and their linked accounts.
 */
public class Customer {

    private String customerId;
    private String name;
    private String phone;
    private boolean kycVerified;
    private List<Account> accounts;  // One customer can have multiple accounts
    private double riskScore;        // Used for fraud priority scoring

    public Customer(String customerId, String name, String phone, boolean kycVerified) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.kycVerified = kycVerified;
        this.accounts = new ArrayList<>();
        this.riskScore = 0.0;
    }

    // --- Getters ---
    public String getCustomerId()  { return customerId; }
    public String getName()        { return name; }
    public String getPhone()       { return phone; }
    public boolean isKycVerified() { return kycVerified; }
    public List<Account> getAccounts() { return accounts; }
    public double getRiskScore()   { return riskScore; }

    // --- Setters ---
    public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
    public void setKycVerified(boolean kycVerified) { this.kycVerified = kycVerified; }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    // Returns the total balance across all accounts (used for sorting)
    public double getTotalBalance() {
        double total = 0;
        for (Account acc : accounts) {
            total += acc.getBalance();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Phone: %s | KYC: %s | Accounts: %d | Risk Score: %.1f",
                customerId, name, phone,
                kycVerified ? "Verified" : "Pending",
                accounts.size(), riskScore);
    }
}

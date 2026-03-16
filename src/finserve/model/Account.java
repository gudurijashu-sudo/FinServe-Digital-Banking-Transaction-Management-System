package finserve.model;

/**
 * Represents a bank account.
 * Can be Savings, Current, or Loan type.
 *
 * DS Used: The EMI schedule for Loan accounts is stored in a plain Array.
 */
public class Account {

    public enum AccountType { SAVINGS, CURRENT, LOAN }

    private String accountNumber;
    private AccountType type;
    private double balance;
    private String ownerId;       // Links back to Customer ID
    private double[] emiSchedule; // Array to store monthly EMI amounts (Loan accounts only)
    private int emiMonths;

    public Account(String accountNumber, AccountType type, double initialBalance, String ownerId) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = initialBalance;
        this.ownerId = ownerId;
    }

    // Set up EMI schedule for a loan account
    // DS: Array — fixed-size, indexed monthly EMI storage
    public void setupEmiSchedule(double loanAmount, int months, double interestRate) {
        this.emiMonths = months;
        this.emiSchedule = new double[months];
        double monthlyRate = interestRate / 12 / 100;
        double emi;
        if (monthlyRate == 0) {
            emi = loanAmount / months;
        } else {
            emi = (loanAmount * monthlyRate * Math.pow(1 + monthlyRate, months))
                    / (Math.pow(1 + monthlyRate, months) - 1);
        }
        for (int i = 0; i < months; i++) {
            emiSchedule[i] = Math.round(emi * 100.0) / 100.0;
        }
    }

    public void printEmiSchedule() {
        if (emiSchedule == null) {
            System.out.println("  No EMI schedule set up for this account.");
            return;
        }
        System.out.println("  EMI Schedule (" + emiMonths + " months):");
        for (int i = 0; i < emiSchedule.length; i++) {
            System.out.printf("    Month %2d: Rs. %.2f%n", (i + 1), emiSchedule[i]);
        }
    }

    // --- Getters ---
    public String getAccountNumber() { return accountNumber; }
    public AccountType getType()     { return type; }
    public double getBalance()       { return balance; }
    public String getOwnerId()       { return ownerId; }

    // --- Balance operations ---
    public void deposit(double amount)  { balance += amount; }
    public void withdraw(double amount) { balance -= amount; }

    @Override
    public String toString() {
        return String.format("Acc# %s | Type: %-8s | Balance: Rs. %,.2f | Owner: %s",
                accountNumber, type, balance, ownerId);
    }
}

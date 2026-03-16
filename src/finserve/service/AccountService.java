package finserve.service;

import finserve.model.Account;
import finserve.model.Customer;

import java.util.HashMap;

/**
 * Manages bank accounts.
 *
 * DS Used:
 *  - HashMap<String, Account>: Maps AccountNumber → Account for O(1) lookup
 *
 * This is the "optimized search" — instead of looping through all accounts
 * to find one by number, we directly get it from the HashMap in O(1).
 */
public class AccountService {

    // HashMap: accountNumber → Account object
    private HashMap<String, Account> accountMap;

    private int accCounter = 1000;

    public AccountService() {
        accountMap = new HashMap<>();
    }

    // Create a new account and link it to a customer
    public Account createAccount(Customer customer, Account.AccountType type, double initialBalance) {
        String accNumber = "ACC" + (++accCounter);
        Account acc = new Account(accNumber, type, initialBalance, customer.getCustomerId());
        accountMap.put(accNumber, acc);
        customer.addAccount(acc);
        System.out.println("  Account created: " + acc);
        return acc;
    }

    // Optimized lookup — O(1) HashMap get
    public Account findByNumber(String accountNumber) {
        return accountMap.get(accountNumber);
    }

    // Demonstrate naive vs optimized search complexity
    public void searchComparison(String accountNumber) {
        System.out.println("\n  === Search Comparison for Account: " + accountNumber + " ===");

        // Naive: iterate all values in the map (simulating an array scan)
        long start1 = System.nanoTime();
        Account naiveResult = null;
        for (Account acc : accountMap.values()) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                naiveResult = acc;
                break;
            }
        }
        long end1 = System.nanoTime();

        // Optimized: direct HashMap get
        long start2 = System.nanoTime();
        Account optimizedResult = accountMap.get(accountNumber);
        long end2 = System.nanoTime();

        System.out.println("  Naive Search   (O(n)): " + (end1 - start1) + " ns | Found: "
                + (naiveResult != null ? naiveResult.getAccountNumber() : "Not Found"));
        System.out.println("  HashMap Lookup (O(1)): " + (end2 - start2) + " ns | Found: "
                + (optimizedResult != null ? optimizedResult.getAccountNumber() : "Not Found"));
        System.out.println("  HashMap lookup is significantly faster at scale.");
    }

    public void listAllAccounts() {
        if (accountMap.isEmpty()) {
            System.out.println("  No accounts found.");
            return;
        }
        int i = 1;
        for (Account acc : accountMap.values()) {
            System.out.println("  " + i++ + ". " + acc);
        }
    }

    public HashMap<String, Account> getAccountMap() { return accountMap; }
}

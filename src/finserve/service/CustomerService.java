package finserve.service;

import finserve.model.Customer;
import finserve.model.Account;
import finserve.util.SearchSortUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages all customers in the system.
 *
 * DS Used:
 *  - HashMap<String, Customer> : Cache of active customers by ID (O(1) lookup)
 *  - ArrayList<Customer>       : Full list for iteration and sorting
 */
public class CustomerService {

    // HashMap: customerId → Customer object
    // WHY? Fast O(1) lookup when we know the customer ID
    private HashMap<String, Customer> customerCache;

    // List of all customers (for iteration, sorting, display)
    private ArrayList<Customer> allCustomers;

    private int idCounter = 1;

    public CustomerService() {
        customerCache = new HashMap<>();
        allCustomers = new ArrayList<>();
    }

    // Add a new customer
    public Customer addCustomer(String name, String phone, boolean kycVerified) {
        String id = "CUST" + String.format("%03d", idCounter++);
        Customer c = new Customer(id, name, phone, kycVerified);
        customerCache.put(id, c);
        allCustomers.add(c);
        System.out.println("  Customer added: " + c);
        return c;
    }

    // Lookup by ID — O(1) HashMap lookup
    public Customer findById(String customerId) {
        return customerCache.get(customerId);
    }

    // Naive linear search by name — O(n)
    public Customer naiveSearchByName(String name) {
        System.out.println("  [Naive Search] Scanning all " + allCustomers.size() + " records...");
        for (Customer c : allCustomers) {
            if (c.getName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }

    // Displays all customers
    public void listAll() {
        if (allCustomers.isEmpty()) {
            System.out.println("  No customers registered.");
            return;
        }
        for (int i = 0; i < allCustomers.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + allCustomers.get(i));
        }
    }

    // Sort by total balance descending (uses Bubble Sort in SearchSortUtil)
    public void sortByBalance() {
        List<Customer> sorted = SearchSortUtil.bubbleSortByBalance(new ArrayList<>(allCustomers));
        System.out.println("  Customers sorted by Total Balance (Highest First):");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.printf("  %d. %s | Total Balance: Rs.%,.2f%n",
                    i + 1, sorted.get(i).getName(), sorted.get(i).getTotalBalance());
        }
    }

    // Sort by risk score descending
    public void sortByRiskScore() {
        List<Customer> sorted = SearchSortUtil.bubbleSortByRisk(new ArrayList<>(allCustomers));
        System.out.println("  Customers sorted by Risk Score (Highest First):");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.printf("  %d. %s | Risk Score: %.1f%n",
                    i + 1, sorted.get(i).getName(), sorted.get(i).getRiskScore());
        }
    }

    public List<Customer> getAllCustomers() { return allCustomers; }
    public int getCount() { return allCustomers.size(); }
}

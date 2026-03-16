package finserve.util;

import finserve.model.Customer;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for search and sort demonstrations.
 *
 * Algorithms covered:
 *  1. Naive linear search      — O(n)
 *  2. HashMap-based lookup     — O(1)  (shown in AccountService)
 *  3. Bubble Sort by balance   — O(n²)
 *  4. Bubble Sort by risk score— O(n²)
 *
 * For a 2nd-year viva, you can explain:
 *  - Why O(1) HashMap is better than O(n) scan
 *  - Why sorting helps reporting (e.g., show highest-risk customers first)
 */
public class SearchSortUtil {

    /**
     * Bubble Sort customers by total balance (descending).
     * Time Complexity: O(n²) — acceptable for small datasets (college banking simulation)
     */
    public static List<Customer> bubbleSortByBalance(List<Customer> customers) {
        int n = customers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (customers.get(j).getTotalBalance() < customers.get(j + 1).getTotalBalance()) {
                    // Swap
                    Customer temp = customers.get(j);
                    customers.set(j, customers.get(j + 1));
                    customers.set(j + 1, temp);
                }
            }
        }
        return customers;
    }

    /**
     * Bubble Sort customers by risk score (descending).
     * Higher risk score = more suspicious = shown first.
     */
    public static List<Customer> bubbleSortByRisk(List<Customer> customers) {
        int n = customers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (customers.get(j).getRiskScore() < customers.get(j + 1).getRiskScore()) {
                    Customer temp = customers.get(j);
                    customers.set(j, customers.get(j + 1));
                    customers.set(j + 1, temp);
                }
            }
        }
        return customers;
    }

    /**
     * Demo: Compare naive O(n) search vs HashMap O(1)
     * (Actual HashMap demo is in AccountService.searchComparison())
     */
    public static void printComplexityTable() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║          Algorithm Complexity Reference              ║");
        System.out.println("  ╠══════════════════════════════════════════════════════╣");
        System.out.println("  ║ Operation              │ DS Used        │ Complexity  ║");
        System.out.println("  ╠══════════════════════════════════════════════════════╣");
        System.out.println("  ║ Customer lookup by ID  │ HashMap        │ O(1)        ║");
        System.out.println("  ║ Account lookup         │ HashMap        │ O(1)        ║");
        System.out.println("  ║ Search by name (naive) │ ArrayList      │ O(n)        ║");
        System.out.println("  ║ Sort by balance        │ Bubble Sort    │ O(n²)       ║");
        System.out.println("  ║ Add transaction        │ Linked List    │ O(1)        ║");
        System.out.println("  ║ Undo transaction       │ Stack pop      │ O(1)        ║");
        System.out.println("  ║ Process service req    │ Queue dequeue  │ O(1)        ║");
        System.out.println("  ║ Process fraud alert    │ PQ poll (Heap) │ O(log n)    ║");
        System.out.println("  ║ Issue teller token     │ Circular Queue │ O(1)        ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
    }
}

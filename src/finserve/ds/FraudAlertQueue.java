package finserve.ds;

import finserve.model.FraudAlert;
import java.util.PriorityQueue;

/**
 * Priority Queue to handle fraud alerts and high-value transactions.
 *
 * WHY PRIORITY QUEUE (HEAP)?
 * - Not all alerts are equal. A Rs. 50 lakh transaction is more urgent than Rs. 1 lakh.
 * - PriorityQueue automatically puts the highest-risk alert at the top.
 * - Uses a Max-Heap (achieved by reversing compareTo in FraudAlert).
 *
 * Backed by Java's built-in PriorityQueue (internally a binary heap).
 *
 * Operations:
 *   addAlert()    — O(log n)
 *   processNext() — O(log n)
 *   peek()        — O(1)
 */
public class FraudAlertQueue {

    private PriorityQueue<FraudAlert> pq;

    public FraudAlertQueue() {
        // FraudAlert.compareTo() handles max-priority ordering
        pq = new PriorityQueue<>();
    }

    // Add a new fraud alert
    public void addAlert(FraudAlert alert) {
        pq.add(alert);
        System.out.println("  ⚠  FRAUD ALERT raised: " + alert.getAlertId()
                + " | Risk Score: " + alert.getRiskScore());
    }

    // Process (remove) the highest-risk alert first
    public FraudAlert processNext() {
        if (pq.isEmpty()) {
            System.out.println("  No pending fraud alerts.");
            return null;
        }
        FraudAlert alert = pq.poll();
        System.out.println("  Processing highest-risk alert: " + alert);
        return alert;
    }

    public FraudAlert peekNext() { return pq.peek(); }
    public boolean isEmpty()     { return pq.isEmpty(); }
    public int size()            { return pq.size(); }

    public void printAllAlerts() {
        if (pq.isEmpty()) {
            System.out.println("  No fraud alerts pending.");
            return;
        }
        // Copy to a temp list and sort for display
        System.out.println("  Pending Fraud Alerts (Highest Risk First):");
        PriorityQueue<FraudAlert> temp = new PriorityQueue<>(pq);
        int i = 1;
        while (!temp.isEmpty()) {
            System.out.println("  " + i++ + ". " + temp.poll());
        }
    }
}

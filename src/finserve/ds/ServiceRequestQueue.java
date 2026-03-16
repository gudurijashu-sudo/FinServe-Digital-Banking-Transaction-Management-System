package finserve.ds;

import finserve.model.ServiceRequest;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Standard Queue (FIFO) to process service requests.
 *
 * WHY QUEUE?
 * - Customers are served in the order they raise requests — First Come, First Served.
 * - Enqueue at rear, dequeue from front.
 *
 * Uses Java's built-in LinkedList as the underlying queue.
 *
 * Operations:
 *   enqueue() — O(1)
 *   dequeue() — O(1)
 *   peek()    — O(1)
 */
public class ServiceRequestQueue {

    private Queue<ServiceRequest> queue;

    public ServiceRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add new service request to the back of the queue
    public void enqueue(ServiceRequest request) {
        queue.add(request);
        System.out.println("  Request queued: " + request.getRequestId());
    }

    // Process (remove) the front request
    public ServiceRequest dequeue() {
        if (isEmpty()) {
            System.out.println("  No pending service requests.");
            return null;
        }
        ServiceRequest sr = queue.poll();
        sr.setResolved(true);
        System.out.println("  Processing request: " + sr);
        return sr;
    }

    // Look at the front without removing
    public ServiceRequest peek() {
        return queue.peek();
    }

    public boolean isEmpty() { return queue.isEmpty(); }
    public int size() { return queue.size(); }

    public void printAll() {
        if (isEmpty()) {
            System.out.println("  No pending service requests.");
            return;
        }
        System.out.println("  Pending Service Requests:");
        int i = 1;
        for (ServiceRequest sr : queue) {
            System.out.println("  " + i++ + ". " + sr);
        }
    }
}

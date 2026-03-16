package finserve.ds;

import finserve.model.Transaction;
import java.util.ArrayList;

/**
 * Custom Stack to support:
 *  1. Undo last transaction (admin rollback)
 *  2. Audit trail (view history in reverse)
 *
 * WHY STACK?
 * - Undo = LIFO: last-in, first-out.
 * - The most recent transaction is always on top and undone first.
 *
 * Implemented using an ArrayList internally (dynamic size).
 *
 * Operations:
 *   push()  — O(1)
 *   pop()   — O(1)
 *   peek()  — O(1)
 */
public class UndoStack {

    private ArrayList<Transaction> stack;

    public UndoStack() {
        stack = new ArrayList<>();
    }

    // Push a transaction onto the stack
    public void push(Transaction txn) {
        stack.add(txn);
    }

    // Remove and return the top transaction (most recent)
    public Transaction pop() {
        if (isEmpty()) return null;
        return stack.remove(stack.size() - 1);
    }

    // View the top without removing
    public Transaction peek() {
        if (isEmpty()) return null;
        return stack.get(stack.size() - 1);
    }

    public boolean isEmpty() { return stack.isEmpty(); }
    public int size() { return stack.size(); }

    // Print entire audit trail (top to bottom = newest to oldest)
    public void printAuditTrail() {
        if (isEmpty()) {
            System.out.println("  Audit trail is empty.");
            return;
        }
        System.out.println("  === AUDIT TRAIL (Newest First) ===");
        for (int i = stack.size() - 1; i >= 0; i--) {
            System.out.println("  " + (stack.size() - i) + ". " + stack.get(i));
        }
    }
}

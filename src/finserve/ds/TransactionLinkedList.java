package finserve.ds;

import finserve.model.Transaction;

/**
 * Custom Singly Linked List to store transaction history.
 *
 * WHY LINKED LIST?
 * - Transactions are always added at the front (newest first).
 * - We don't need random access by index; we scroll through them in order.
 * - Insertion at head is O(1) — perfect for logging.
 *
 * Operations:
 *   addFirst()  — O(1)
 *   printAll()  — O(n)
 *   getHead()   — O(1)
 */
public class TransactionLinkedList {

    private TransactionNode head;
    private int size;

    public TransactionLinkedList() {
        head = null;
        size = 0;
    }

    // Add new transaction at the beginning (most recent first)
    public void addFirst(Transaction txn) {
        TransactionNode newNode = new TransactionNode(txn);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // Print all transactions from newest to oldest
    public void printAll() {
        if (head == null) {
            System.out.println("  No transactions found.");
            return;
        }
        TransactionNode current = head;
        int count = 1;
        while (current != null) {
            System.out.println("  " + count + ". " + current.data);
            current = current.next;
            count++;
        }
    }

    // Returns the most recent transaction (head of list)
    public Transaction getLatest() {
        if (head == null) return null;
        return head.data;
    }

    public TransactionNode getHead() { return head; }
    public int getSize() { return size; }
    public boolean isEmpty() { return head == null; }
}

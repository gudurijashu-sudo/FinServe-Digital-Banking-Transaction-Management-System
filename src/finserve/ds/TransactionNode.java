package finserve.ds;

import finserve.model.Transaction;

/**
 * A single node in our custom Singly Linked List.
 * Each node holds one Transaction and a pointer to the next node.
 *
 * DS: Linked List Node
 */
public class TransactionNode {
    public Transaction data;
    public TransactionNode next;

    public TransactionNode(Transaction data) {
        this.data = data;
        this.next = null;
    }
}

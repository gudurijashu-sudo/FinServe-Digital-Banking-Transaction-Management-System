package finserve.ds;

/**
 * Circular Queue to simulate the token system at a bank teller counter.
 *
 * WHY CIRCULAR QUEUE?
 * - After token MAX_SIZE is reached, tokens wrap around and reuse slots.
 * - Avoids wasted space at the front that a normal array queue would have.
 * - Think of it like a number token machine: 001, 002, ..., 010, then back to 001.
 *
 * Fixed capacity array-based implementation.
 *
 * Operations:
 *   issueToken()  — O(1)  (enqueue)
 *   callNext()    — O(1)  (dequeue)
 */
public class TellerTokenQueue {

    private int[] tokens;       // Stores token numbers
    private int front;
    private int rear;
    private int size;
    private int capacity;
    private int tokenCounter;   // Increments for each new token issued

    public TellerTokenQueue(int capacity) {
        this.capacity = capacity;
        tokens = new int[capacity];
        front = 0;
        rear = -1;
        size = 0;
        tokenCounter = 0;
    }

    // Issue a new token to a customer arriving at the bank
    public int issueToken() {
        if (isFull()) {
            System.out.println("  Token queue is full! Please wait.");
            return -1;
        }
        tokenCounter++;
        rear = (rear + 1) % capacity;  // Circular increment
        tokens[rear] = tokenCounter;
        size++;
        System.out.println("  Token issued: #" + tokenCounter);
        return tokenCounter;
    }

    // Call the next token for service
    public int callNext() {
        if (isEmpty()) {
            System.out.println("  No customers waiting.");
            return -1;
        }
        int token = tokens[front];
        front = (front + 1) % capacity;  // Circular increment
        size--;
        System.out.println("  Now serving token: #" + token);
        return token;
    }

    public boolean isEmpty() { return size == 0; }
    public boolean isFull()  { return size == capacity; }
    public int getSize()     { return size; }

    public void printStatus() {
        System.out.println("  Teller Queue: " + size + "/" + capacity + " slots used.");
        if (!isEmpty()) {
            System.out.println("  Next token to be called: #" + tokens[front]);
        }
    }
}

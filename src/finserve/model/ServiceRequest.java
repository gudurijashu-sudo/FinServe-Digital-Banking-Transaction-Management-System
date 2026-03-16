package finserve.model;

/**
 * Represents a customer service request (e.g., "Block card", "Update KYC").
 * These are stored in a Queue (ServiceRequestQueue) for FIFO processing.
 */
public class ServiceRequest {

    public enum RequestType { BLOCK_CARD, UPDATE_KYC, CHEQUE_BOOK, LOAN_INQUIRY, OTHER }

    private String requestId;
    private String customerId;
    private RequestType requestType;
    private String description;
    private boolean resolved;

    public ServiceRequest(String requestId, String customerId,
                          RequestType requestType, String description) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.requestType = requestType;
        this.description = description;
        this.resolved = false;
    }

    public String getRequestId()     { return requestId; }
    public String getCustomerId()    { return customerId; }
    public RequestType getRequestType() { return requestType; }
    public String getDescription()   { return description; }
    public boolean isResolved()      { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }

    @Override
    public String toString() {
        return String.format("[%s] Customer: %s | Type: %s | Desc: %s | Status: %s",
                requestId, customerId, requestType, description,
                resolved ? "Resolved" : "Pending");
    }
}

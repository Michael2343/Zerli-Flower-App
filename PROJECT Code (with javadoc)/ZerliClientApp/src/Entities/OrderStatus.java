package Entities;

// TODO: Auto-generated Javadoc
/**
 * The Enum OrderStatus.
 */
public enum OrderStatus {
	
	/** wait for manager approval. */
	pending_confirm,
	
	/** The pending cancel. */
	pending_cancel,
	
	/** waiting for delivery. */
	confirmed,
	
	/** canceled order. */
	canceled,
	
	/** completed order. */
	completed,
}

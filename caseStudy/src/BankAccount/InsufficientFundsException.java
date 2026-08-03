package BankAccount;

/**
 * Custom checked exception used to enforce account-specific withdrawal rules.
 * Demonstrates exception handling.
 */
public class InsufficientFundsException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InsufficientFundsException(String message) {
        super(message);
    }
}

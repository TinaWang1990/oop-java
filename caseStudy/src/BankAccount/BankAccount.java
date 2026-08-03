package BankAccount;

import java.io.Serializable;

public abstract class BankAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String accountNumber;
	private String accountHolder;
	private double balance;
	
	public BankAccount(String accountNumber, String accountHolder) {
		this(accountNumber, accountHolder, 0.0);
	}

	public BankAccount(String accountNumber, String accountHolder, double balance) {
		this.accountNumber = accountNumber;
		this.accountHolder = accountHolder;
		this.balance = balance;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public String getAccountHolder() {
		return accountHolder;
	}

	public double getBalance() {
		return balance;
	}
	
	protected void setBalance(double balance) {
		this.balance = balance;
	}
	
	// Abstract methods -> every subclass MUST provide its own implementation.
    // Calling these through a BankAccount reference is where polymorphism happens.
	public abstract String getAccountType();
	public abstract double calculateInterest();
	
	// ----- Overriding Object's toString() -----
    @Override
    public String toString() {
        return String.format("[%s] %s | Holder: %s | Balance: $%.2f",
                getAccountType(), accountNumber, accountHolder, balance);
    }
    
    // ----- Method Overloading: deposit() with and without a note -----
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
    }
    
    public void deposit(double amount, String note) {
    	deposit(amount);
        System.out.println("Deposit note for " + accountNumber + ": " + note);
    }
    
    /**
     * Default withdraw behaviour. Subclasses override this to apply their
     * own business rules (minimum balance, overdraft limit, etc.), which is
     * the basis for runtime polymorphism when accounts are processed through
     * a BankAccount reference.
     */
    public void withdraw(double amount) throws InsufficientFundsException {
    	if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
    	if (amount > balance) {
            throw new InsufficientFundsException(
                    "Insufficient funds in account " + accountNumber
                            + ". Balance: " + balance + ", Requested: " + amount);
        }
    	balance -= amount;
    }
}

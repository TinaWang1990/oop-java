package BankAccount;

public class CheckingAccount extends BankAccount{
	private static final long serialVersionUID = 1L;
	
	private double overdraftLimit;

	public CheckingAccount(String accountNumber, String accountHolder, double balance) {
        super(accountNumber, accountHolder, balance);
        this.overdraftLimit = 200.0; // default overdraft limit
    }
	
	public CheckingAccount(String accountNumber, String accountHolder, double balance, double overdraftLimit) {
		super(accountNumber, accountHolder, balance);
		this.overdraftLimit = overdraftLimit;
	}

	public double getOverdraftLimit() {
        return overdraftLimit;
    }
	
	@Override
    public String getAccountType() {
        return "Checking";
    }
	
	@Override
	public double calculateInterest() {
		return 0.0;
	}
	
	@Override
	public void withdraw(double amount) throws InsufficientFundsException {
		if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
		if (getBalance() - amount < -overdraftLimit) {
            throw new InsufficientFundsException(
                    "Checking account " + getAccountNumber()
                            + " would exceed the overdraft limit of $" + overdraftLimit);
        }
		setBalance(getBalance() - amount);
	}
}

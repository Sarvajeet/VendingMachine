package vendingmachine.user;

public class UserAccount {

    private String userID;
    private String pin;
    private int balance; // in cents

    /**
     * Constructor for UserAccount.
     * @param userID The user's identifier.
     * @param pin The user's personal identification number.
     * @param initialBalance The initial balance for the account in cents.
     *                       If negative, balance is set to 0.
     */
    public UserAccount(String userID, String pin, int initialBalance) {
        this.userID = userID;
        this.pin = pin;
        if (initialBalance < 0) {
            this.balance = 0;
        } else {
            this.balance = initialBalance;
        }
    }

    /**
     * Returns the user's ID.
     * @return The userID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Returns the current balance of the account.
     * @return The balance in cents.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Checks if the provided PIN matches the user's PIN.
     * @param inputPin The PIN to check.
     * @return True if the inputPin matches the user's PIN, false otherwise.
     *         Returns false if inputPin is null.
     */
    public boolean checkPin(String inputPin) {
        if (inputPin == null) {
            return false;
        }
        return this.pin.equals(inputPin);
    }

    /**
     * Debits a specified amount from the account balance.
     * @param amount The amount to debit in cents.
     * @return True if the debit was successful (amount is positive and balance was sufficient),
     *         false otherwise.
     */
    public boolean debit(int amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Credits a specified amount to the account balance.
     * @param amount The amount to credit in cents.
     * @return True if the credit was successful (amount is positive), false otherwise.
     */
    public boolean credit(int amount) {
        if (amount > 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }
}

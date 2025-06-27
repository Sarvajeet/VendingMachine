package vendingmachine.user;

import java.util.HashMap;
import java.util.Map;

public class UserManagement {

    private Map<String, UserAccount> users;

    /**
     * Constructor for UserManagement.
     * Initializes the collection of users.
     */
    public UserManagement() {
        this.users = new HashMap<>();
    }

    /**
     * Creates a new user account.
     * @param userID The user's identifier. Must not be null or empty.
     * @param pin The user's personal identification number. Must not be null or empty.
     * @param initialBalance The initial balance for the account in cents.
     * @return True if the user was created successfully, false if the userID already exists
     *         or if userID or pin is null/empty.
     */
    public boolean createUser(String userID, String pin, int initialBalance) {
        if (userID == null || userID.isEmpty() || pin == null || pin.isEmpty() || users.containsKey(userID)) {
            return false;
        }
        UserAccount newUser = new UserAccount(userID, pin, initialBalance);
        users.put(userID, newUser);
        return true;
    }

    /**
     * Retrieves a user account by userID.
     * @param userID The identifier of the user to retrieve.
     * @return The UserAccount object if found, otherwise null.
     */
    public UserAccount getUser(String userID) {
        return users.get(userID);
    }

    /**
     * Authenticates a user based on userID and PIN.
     * @param userID The user's identifier.
     * @param pin The user's PIN.
     * @return The UserAccount object if authentication is successful, otherwise null.
     */
    public UserAccount authenticateUser(String userID, String pin) {
        UserAccount user = getUser(userID);
        if (user != null && user.checkPin(pin)) {
            return user;
        }
        return null;
    }

    /**
     * Loads (credits) a specified amount to a user's account after authentication.
     * @param userID The user's identifier.
     * @param pin The user's PIN.
     * @param amount The amount to load in cents. Must be positive.
     * @return True if the balance was loaded successfully, false otherwise
     *         (authentication failed or amount was not positive).
     */
    public boolean loadBalance(String userID, String pin, int amount) {
        UserAccount user = authenticateUser(userID, pin);
        if (user != null) {
            // The credit method itself checks for positive amount.
            // However, the requirement explicitly states "amount is positive" for this method's success.
            if (amount > 0) {
                return user.credit(amount);
            }
        }
        return false;
    }
}

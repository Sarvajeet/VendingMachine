package vendingmachine.ui;

import vendingmachine.user.UserAccount;

/**
 * Represents the result of processing a user's menu choice.
 * This class is used to return multiple values from menu processing methods,
 * specifically the updated current user status and whether the application should exit.
 */
public class ProcessChoiceResult {
    /**
     * The current user after processing the choice.
     * This might be the same user, a new user (after login), or null (after logout or if no user was logged in).
     */
    public UserAccount currentUser;

    /**
     * Flag indicating whether the application should terminate based on the user's choice (e.g., selecting "Exit").
     */
    public boolean exitApplication;

    /**
     * Constructs a new ProcessChoiceResult.
     *
     * @param currentUser The updated UserAccount status. This can be null if the user logged out
     *                    or if no user was relevant to the choice.
     * @param exitApplication True if the user's choice leads to application termination, false otherwise.
     */
    public ProcessChoiceResult(UserAccount currentUser, boolean exitApplication) {
        this.currentUser = currentUser;
        this.exitApplication = exitApplication;
    }
}

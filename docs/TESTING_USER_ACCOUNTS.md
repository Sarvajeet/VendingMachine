# Test Cases for Vending Machine User Account Functionality

This document outlines test scenarios for the user account features of the vending machine, including creation, login/logout, account operations, and purchases using account balance.

## I. Account Creation

1.  **Test Successful Account Creation:**
    *   **Objective:** Verify a new user account can be created with valid inputs.
    *   **Steps:**
        1.  Run `VendingMachine.java`.
        2.  Select menu option for "Create User Account".
        3.  Enter a unique UserID (e.g., "newUser1").
        4.  Enter a PIN (e.g., "1234").
        5.  Enter a non-negative initial balance (e.g., 500 cents).
    *   **Expected Outcome:** A confirmation message like "Account created successfully for newUser1." should be displayed. The user should then be able to log in with these credentials in a subsequent step.

2.  **Test Duplicate UserID Creation:**
    *   **Objective:** Verify the system prevents creation of an account with an existing UserID.
    *   **Steps:**
        1.  Run `VendingMachine.java`.
        2.  Create a user account (e.g., UserID: "userExists", PIN: "1111", Balance: 100).
        3.  Attempt to create another account with the same UserID "userExists" (PIN and balance can be different).
    *   **Expected Outcome:** An error message like "Account creation failed. UserID might be taken or inputs invalid." should be displayed.

3.  **Test Account Creation with Invalid Inputs:**
    *   **Objective:** Verify the system handles empty UserID, empty PIN, or negative initial balance during account creation.
    *   **Steps:**
        1.  Run `VendingMachine.java`.
        2.  Attempt 1: Select "Create User Account". Enter an empty UserID, a valid PIN (e.g., "0000"), and a valid balance (e.g., 0).
        3.  Attempt 2: Select "Create User Account". Enter a valid UserID (e.g., "userValid"), an empty PIN, and a valid balance.
        4.  Attempt 3: Select "Create User Account". Enter a valid UserID (e.g., "userValid2"), a valid PIN (e.g., "0000"), and a negative initial balance (e.g., -100).
    *   **Expected Outcome:** For each attempt, an appropriate error message (e.g., "Account creation failed...") should be displayed, and no account should be created.

## II. User Login & Logout

4.  **Test Successful Login:**
    *   **Objective:** Verify a user can log in with correct credentials.
    *   **Steps:**
        1.  Ensure an account exists (e.g., use the pre-created "testUser" with PIN "1234" or create a new one like "loginUser1", "pass1").
        2.  Run `VendingMachine.java`.
        3.  Select menu option for "Login to User Account".
        4.  Enter the correct UserID for the existing account.
        5.  Enter the corresponding correct PIN.
    *   **Expected Outcome:** A message "Login successful. Welcome [userID]!" should be displayed. The main menu should change to the logged-in state, showing options like "View Account Balance", "Logout", etc.

5.  **Test Login with Incorrect UserID:**
    *   **Objective:** Verify login fails when a non-existent UserID is entered.
    *   **Steps:**
        1.  Run `VendingMachine.java`.
        2.  Select "Login to User Account".
        3.  Enter a UserID that does not exist (e.g., "nonExistentUser").
        4.  Enter any PIN (e.g., "1234").
    *   **Expected Outcome:** An error message like "Login failed. Invalid UserID or PIN." should be displayed. The user should remain in the logged-out state.

6.  **Test Login with Incorrect PIN:**
    *   **Objective:** Verify login fails when a correct UserID is provided but with an incorrect PIN.
    *   **Steps:**
        1.  Ensure an account exists (e.g., "testUser" with PIN "1234").
        2.  Run `VendingMachine.java`.
        3.  Select "Login to User Account".
        4.  Enter the correct UserID ("testUser").
        5.  Enter an incorrect PIN (e.g., "0000").
    *   **Expected Outcome:** An error message like "Login failed. Invalid UserID or PIN." should be displayed. The user should remain in the logged-out state.

7.  **Test Logout:**
    *   **Objective:** Verify a logged-in user can successfully log out.
    *   **Steps:**
        1.  Run `VendingMachine.java` and log in with a valid account.
        2.  Verify the menu is in the logged-in state.
        3.  Select the menu option for "Logout".
    *   **Expected Outcome:** A message "Logged out successfully." should be displayed. The main menu should revert to the logged-out state (showing "Login" and "Create User Account" options). `currentUser` should be null internally.

## III. Account Operations (when logged in)

8.  **Test View Balance:**
    *   **Objective:** Verify a logged-in user can view their current account balance.
    *   **Steps:**
        1.  Log in with an account that has a known balance (e.g., "testUser" starts with 1000 cents, or create "balanceUser" with 250 cents).
        2.  Select the menu option for "View Account Balance".
    *   **Expected Outcome:** A message displaying the correct balance, e.g., "Your current balance is: 1000 cents." (or 250 cents for "balanceUser").

9.  **Test Load Balance (Successful):**
    *   **Objective:** Verify a logged-in user can successfully add funds to their account.
    *   **Steps:**
        1.  Log in with an account (e.g., "testUser"). Note its initial balance (e.g., 1000 cents).
        2.  Select "Load Account Balance".
        3.  When prompted, enter a positive integer amount (e.g., 500).
    *   **Expected Outcome:** A confirmation message like "Balance updated. New balance: 1500 cents." should be displayed. Selecting "View Account Balance" again should confirm the new total.

10. **Test Load Balance (Invalid Amount):**
    *   **Objective:** Verify the system handles attempts to load zero, negative, or non-numeric amounts.
    *   **Steps:**
        1.  Log in with an account.
        2.  Select "Load Account Balance".
        3.  Attempt 1: Enter 0 when prompted for the amount.
        4.  Attempt 2 (if re-prompted): Enter -100.
        5.  Attempt 3 (if re-prompted): Enter "abc" or other non-numeric text.
    *   **Expected Outcome:** For each invalid attempt, an error message (e.g., "Amount must be positive." or "Invalid input. Please enter a number.") should be displayed. The account balance should remain unchanged from before these attempts.

## IV. Purchasing with Account Balance

11. **Test Purchase with Sufficient Funds:**
    *   **Objective:** Verify a logged-in user can successfully purchase a product using their account balance.
    *   **Steps:**
        1.  Log in with an account having sufficient funds (e.g., "testUser" with 1000 cents).
        2.  Note the price of a product (e.g., Gum costs 50 cents, PotatoChips 75 cents).
        3.  Select the product to purchase from the menu.
        4.  When prompted "Pay with account balance (Y/N)?", enter "Y" (or "y").
    *   **Expected Outcome:**
        *   A message "Purchase successful with account! Collect [ProductName]."
        *   A message "Remaining balance: [initial_balance - product_cost] cents."
        *   The quantity of the purchased product displayed in the main menu should decrease by 1.
        *   The user's account balance, if viewed again, should be correctly reduced.

12. **Test Purchase with Insufficient Funds:**
    *   **Objective:** Verify that a purchase attempt fails if the user's account balance is less than the product's cost.
    *   **Steps:**
        1.  Log in with an account. Ensure its balance is known and less than the cost of a chosen product (e.g., create user "lowFunds" with 20 cents).
        2.  Attempt to purchase a product that costs more than the balance (e.g., Gum at 50 cents).
        3.  When prompted "Pay with account balance (Y/N)?", enter "Y".
    *   **Expected Outcome:**
        *   A message like "Purchase failed. Insufficient account balance. Your balance is 20 cents. Product cost is 50 cents."
        *   The product quantity should not change.
        *   The user's account balance should not change.

13. **Test Purchase with Coins When Logged In:**
    *   **Objective:** Verify a logged-in user can choose to pay with coins instead of their account balance.
    *   **Steps:**
        1.  Log in with an account.
        2.  Select a product to purchase.
        3.  When prompted "Pay with account balance (Y/N)?", enter "N" (or "n").
    *   **Expected Outcome:** The system should proceed to the standard coin insertion process ("Please enter Coin..."). The user's account balance should remain unchanged after the transaction (if it completes or is cancelled).

## V. Interaction with Other Features

14. **Test Restocking when Logged In/Out:**
    *   **Objective:** Ensure the restocking functionality operates independently of the user's login state.
    *   **Steps:**
        1.  Run `VendingMachine.java`. While logged out, select "Restock Products". Choose a product and add a quantity. Verify the quantity updates on the main menu.
        2.  Log in with any user account.
        3.  Select "Restock Products". Choose a different product (or the same one) and add a quantity. Verify the quantity updates on the main menu.
    *   **Expected Outcome:** The restocking process (selecting product, entering quantity, seeing updated stock) should work identically whether a user is logged in or logged out. The login state should not affect this administrative function.

package vendingmachine.ui;

import vendingmachine.product.bean.Products;
import vendingmachine.products.CocaCola; // Specific product import
import vendingmachine.products.Gum;       // Specific product import
import vendingmachine.products.PotatoChips; // Specific product import
import vendingmachine.user.UserAccount;
import vendingmachine.user.UserManagement;
import vendingmachine.utilities.VendingMachineUtilities;
import java.util.Scanner;

/**
 * Handles the display of console-based menus and processes user choices for the Vending Machine.
 * This class centralizes menu logic, separating UI concerns from the main application flow
 * and utility functions. It provides static methods for displaying different menu states
 * (logged in, logged out) and for processing the user's selections in those states.
 */
public class MenuHandler {

    // No static Scanner is defined in this class.
    // Scanner instances are passed as parameters to methods that require input,
    // promoting flexibility and avoiding issues with multiple System.in scanners.

    /**
     * Displays the list of available products with their details.
     * Assumes a fixed set of 3 products are passed in order for consistent display.
     * @param gum The Gum product.
     * @param cocacola The CocaCola product.
     * @param potatochips The PotatoChips product.
     */
    public static void displayProductListing(Products gum, Products cocacola, Products potatochips) {
        System.out.println( "Index \t ProductName \t\t Cost \t\t Quantity Available " );
        // Displaying with hardcoded indices 1, 2, 3 as per original VendingMachine display
        System.out.println( "1    \t " + gum.getProductName() + "\t\t\t " + gum.getProductCost() + " cent\t\t" + gum.getProductQuantity() );
        System.out.println( "2    \t " + cocacola.getProductName() + "\t\t " + cocacola.getProductCost() + " cent\t\t"
                + cocacola.getProductQuantity() );
        System.out.println( "3    \t " + potatochips.getProductName() + "\t\t " + potatochips.getProductCost() + " cent\t\t"
                + potatochips.getProductQuantity() );
    }

    /**
     * Displays the menu for users who are not logged in.
     * @param gum The Gum product.
     * @param cocacola The CocaCola product.
     * @param potatochips The PotatoChips product.
     */
    public static void displayLoggedOutMenu(Products gum, Products cocacola, Products potatochips) {
        System.out.println( "\n--- Vending Machine Menu ---" );
        displayProductListing(gum, cocacola, potatochips);
        System.out.println( "4    \t Restock Products" );
        System.out.println( "5    \t Login to User Account" );
        System.out.println( "6    \t Create User Account" );
        System.out.println( "7    \t Exit" );
        System.out.print( "Please enter your choice: " );
    }

    /**
     * Displays the menu for users who are logged in.
     * @param currentUser The currently logged-in user.
     * @param gum The Gum product.
     * @param cocacola The CocaCola product.
     * @param potatochips The PotatoChips product.
     */
    public static void displayLoggedInMenu(UserAccount currentUser, Products gum, Products cocacola, Products potatochips) {
        System.out.println( "\n--- Vending Machine Menu ---" );
        System.out.println( "Logged in as: " + currentUser.getUserID() );
        displayProductListing(gum, cocacola, potatochips);
        System.out.println( "4    \t Restock Products" );
        System.out.println( "5    \t View Account Balance" );
        System.out.println( "6    \t Load Account Balance" );
        System.out.println( "7    \t Logout" );
        System.out.println( "8    \t Exit" );
        System.out.print( "Please enter your choice: " );
    }

    /**
     * Processes the user's menu choice when no user is logged in.
     * Handles actions like purchasing products (delegating to utilities), logging in, creating an account, or exiting.
     *
     * @param choice The integer choice made by the user from the logged-out menu.
     * @param scanner The Scanner instance for reading further user input if needed (e.g., for login credentials).
     * @param userManager The UserManagement instance for handling login and account creation.
     * @param gum The Gum product instance.
     * @param cocacola The CocaCola product instance.
     * @param potatochips The PotatoChips product instance.
     * @return A {@link ProcessChoiceResult} object containing the new current user (null if login failed or not attempted)
     *         and a boolean indicating if the application should exit.
     */
    public static ProcessChoiceResult processLoggedOutChoice(int choice, Scanner scanner, UserManagement userManager, Products gum, Products cocacola, Products potatochips) {
        UserAccount newCurrentUser = null; // Represents the user state after this choice; null if no login.
        boolean exitApplication = false;   // Flag to signal if the application should terminate.

        switch (choice) {
            case 1: // Buy Gum
                VendingMachineUtilities.purchase(gum); // Delegates to utility method for purchase
                break;
            case 2: // Buy CocaCola
                VendingMachineUtilities.purchase(cocacola);
                break;
            case 3: // Buy PotatoChips
                VendingMachineUtilities.purchase(potatochips);
                break;
            case 4: // Restock Products
                VendingMachineUtilities.restockProducts(gum, cocacola, potatochips);
                break;
            case 5: // Login to User Account
                newCurrentUser = VendingMachineUtilities.handleLogin(userManager); // Attempt login
                break;
            case 6: // Create User Account
                VendingMachineUtilities.handleCreateAccount(userManager); // Attempt account creation
                break;
            case 7: // Exit
                System.out.println("Exiting application. Thank you!");
                exitApplication = true; // Signal application termination
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        // Return the outcome: potentially a newly logged-in user and the exit status
        return new ProcessChoiceResult(newCurrentUser, exitApplication);
    }

    /**
     * Processes the user's menu choice when a user is logged in.
     * Handles actions like purchasing (with option for account payment), viewing balance, loading balance, logging out, or exiting.
     *
     * @param choice The integer choice made by the user from the logged-in menu.
     * @param scanner The Scanner instance for reading further user input (e.g., "Pay with account Y/N?").
     * @param currentUser The currently logged-in UserAccount. This will not be null.
     * @param userManager The UserManagement instance (though not directly used in all logged-in choices, passed for consistency or future use).
     * @param gum The Gum product instance.
     * @param cocacola The CocaCola product instance.
     * @param potatochips The PotatoChips product instance.
     * @return A {@link ProcessChoiceResult} object containing the updated current user status (e.g., null after logout)
     *         and a boolean indicating if the application should exit.
     */
    public static ProcessChoiceResult processLoggedInChoice(int choice, Scanner scanner, UserAccount currentUser, UserManagement userManager, Products gum, Products cocacola, Products potatochips) {
        boolean exitApplication = false;    // Flag to signal if the application should terminate.
        UserAccount nextUserStatus = currentUser; // By default, user remains logged in unless they choose to logout.

        switch (choice) {
            case 1: // Buy Gum
            case 2: // Buy CocaCola
            case 3: // Buy PotatoChips
                Products selectedProduct = null;
                if (choice == 1) selectedProduct = gum;
                else if (choice == 2) selectedProduct = cocacola;
                else selectedProduct = potatochips; // choice == 3

                System.out.print("Pay with account balance (Y/N)? ");
                String payWithAccountChoice = scanner.nextLine().trim();
                if (payWithAccountChoice.equalsIgnoreCase("Y")) {
                    VendingMachineUtilities.purchaseWithAccount(selectedProduct, currentUser);
                } else {
                    VendingMachineUtilities.purchase(selectedProduct); // Standard coin purchase
                }
                break;
            case 4: // Restock Products
                VendingMachineUtilities.restockProducts(gum, cocacola, potatochips);
                break;
            case 5: // View Account Balance
                VendingMachineUtilities.handleViewBalance(currentUser);
                break;
            case 6: // Load Account Balance
                VendingMachineUtilities.handleLoadBalance(currentUser);
                break;
            case 7: // Logout
                System.out.println("Logged out successfully.");
                nextUserStatus = null; // Set current user to null to signify logout
                break;
            case 8: // Exit
                System.out.println("Exiting application. Thank you!");
                exitApplication = true; // Signal application termination
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        // Return the outcome: the user status (possibly changed by logout) and the exit status
        return new ProcessChoiceResult(nextUserStatus, exitApplication);
    }
}

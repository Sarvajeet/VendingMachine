package vendingmachine.ui;

import vendingmachine.product.bean.Products;
// Specific product imports (Gum, CocaCola, PotatoChips) are no longer strictly needed here
// if methods operate on List<Products> and Products instances.
import vendingmachine.user.UserAccount;
import vendingmachine.user.UserManagement;
import vendingmachine.utilities.VendingMachineUtilities;
import java.util.List; // Added import
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
     * Displays the list of available products with their details from a list.
     * Products are indexed starting from 1 for display purposes.
     * @param allProducts A list of {@link Products} to be displayed.
     */
    public static void displayProductListing(List<Products> allProducts) {
        System.out.println("Index \t ProductName \t\t Cost \t\t Quantity Available ");
        if (allProducts == null || allProducts.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        for (int i = 0; i < allProducts.size(); i++) {
            Products product = allProducts.get(i);
            // Basic formatting, might need adjustment for longer product names
            System.out.printf("%-5d\t %-20s \t %-7d cent\t\t%-10d%n",
                              (i + 1),
                              product.getProductName(),
                              product.getProductCost(),
                              product.getProductQuantity());
        }
    }

    /**
     * Displays the menu for users who are not logged in.
     * Product listing is followed by general machine options.
     * @param allProducts A list of {@link Products} to be displayed.
     */
    public static void displayLoggedOutMenu(List<Products> allProducts) {
        System.out.println("\n--- Vending Machine Menu ---");
        displayProductListing(allProducts);
        int nextOptionIndex = (allProducts != null ? allProducts.size() : 0) + 1;
        System.out.println(nextOptionIndex++ + "    \t Restock Products");
        System.out.println(nextOptionIndex++ + "    \t Login to User Account");
        System.out.println(nextOptionIndex++ + "    \t Create User Account");
        System.out.println(nextOptionIndex + "    \t Exit");
        System.out.print("Please enter your choice: ");
    }

    /**
     * Displays the menu for users who are logged in.
     * Shows logged-in user ID, product listing, and user-specific + general machine options.
     * @param currentUser The currently logged-in {@link UserAccount}.
     * @param allProducts A list of {@link Products} to be displayed.
     */
    public static void displayLoggedInMenu(UserAccount currentUser, List<Products> allProducts) {
        System.out.println("\n--- Vending Machine Menu ---");
        System.out.println("Logged in as: " + currentUser.getUserID());
        displayProductListing(allProducts);
        int nextOptionIndex = (allProducts != null ? allProducts.size() : 0) + 1;
        System.out.println(nextOptionIndex++ + "    \t Restock Products");
        System.out.println(nextOptionIndex++ + "    \t View Account Balance");
        System.out.println(nextOptionIndex++ + "    \t Load Account Balance");
        System.out.println(nextOptionIndex++ + "    \t Logout");
        System.out.println(nextOptionIndex + "    \t Exit");
        System.out.print( "Please enter your choice: " );
    }

    /**
     * Processes the user's menu choice when no user is logged in.
     * Handles actions like purchasing products (delegating to utilities), logging in, creating an account, or exiting.
     *
     * @param choice The integer choice made by the user from the logged-out menu.
     * @param scanner The Scanner instance for reading further user input if needed.
     * @param userManager The UserManagement instance for handling login and account creation.
     * @param allProducts A list of all {@link Products} available in the machine.
     * @return A {@link ProcessChoiceResult} object containing the new current user (null if login failed or not attempted)
     *         and a boolean indicating if the application should exit.
     */
    public static ProcessChoiceResult processLoggedOutChoice(int choice, Scanner scanner, UserManagement userManager, List<Products> allProducts) {
        UserAccount newCurrentUser = null;
        boolean exitApplication = false;
        int productListSize = (allProducts != null ? allProducts.size() : 0);

        // Product choices are 1 to productListSize
        if (choice >= 1 && choice <= productListSize) {
            Products selectedProduct = allProducts.get(choice - 1);
            VendingMachineUtilities.purchase(selectedProduct);
        } else {
            // Other menu options are indexed after product listings
            int optionIndex = choice - productListSize;
            switch (optionIndex) {
                case 1: // Restock Products
                    // TODO: Update VendingMachineUtilities.restockProducts to accept List<Products> and Scanner
                    // For now, this call will likely cause a compile error or runtime issue if not adapted.
                    // VendingMachineUtilities.restockProducts(allProducts.get(0), allProducts.get(1), allProducts.get(2)); // Placeholder
                    System.out.println("Restock functionality to be fully adapted for List<Products> in VendingMachineUtilities.");
                    VendingMachineUtilities.restockProducts(allProducts, scanner); // Ideal future call
                    break;
                case 2: // Login to User Account
                    newCurrentUser = VendingMachineUtilities.handleLogin(userManager);
                    break;
                case 3: // Create User Account
                    VendingMachineUtilities.handleCreateAccount(userManager);
                    break;
                case 4: // Exit
                    System.out.println("Exiting application. Thank you!");
                    exitApplication = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        return new ProcessChoiceResult(newCurrentUser, exitApplication);
    }

    /**
     * Processes the user's menu choice when a user is logged in.
     * Handles actions like purchasing, viewing balance, loading balance, logging out, or exiting.
     * Product choices are dynamic based on the size of the `allProducts` list.
     * Other menu options are indexed sequentially after the product list.
     *
     * @param choice The integer choice made by the user from the logged-in menu.
     * @param scanner The Scanner instance for reading further user input.
     * @param currentUser The currently logged-in {@link UserAccount}.
     * @param userManager The UserManagement instance.
     * @param allProducts A list of all {@link Products} available in the machine.
     * @return A {@link ProcessChoiceResult} object containing the updated current user status
     *         and a boolean indicating if the application should exit.
     */
    public static ProcessChoiceResult processLoggedInChoice(int choice, Scanner scanner, UserAccount currentUser, UserManagement userManager, List<Products> allProducts) {
        boolean exitApplication = false;
        UserAccount nextUserStatus = currentUser;
        int productListSize = (allProducts != null ? allProducts.size() : 0);

        // Product choices
        if (choice >= 1 && choice <= productListSize) {
            Products selectedProduct = allProducts.get(choice - 1);
            System.out.print("Pay with account balance (Y/N)? ");
            String payWithAccountChoice = scanner.nextLine().trim();
            if (payWithAccountChoice.equalsIgnoreCase("Y")) {
                VendingMachineUtilities.purchaseWithAccount(selectedProduct, currentUser);
            } else {
                VendingMachineUtilities.purchase(selectedProduct);
            }
        } else {
            // Other menu options
            int optionIndex = choice - productListSize;
            switch (optionIndex) {
                case 1: // Restock Products
                     // TODO: Update VendingMachineUtilities.restockProducts to accept List<Products> and Scanner
                    System.out.println("Restock functionality to be fully adapted for List<Products> in VendingMachineUtilities.");
                    VendingMachineUtilities.restockProducts(allProducts, scanner); // Ideal future call
                    break;
                case 2: // View Account Balance
                    VendingMachineUtilities.handleViewBalance(currentUser);
                    break;
                case 3: // Load Account Balance
                    VendingMachineUtilities.handleLoadBalance(currentUser);
                    break;
                case 4: // Logout
                    System.out.println("Logged out successfully.");
                    nextUserStatus = null;
                    break;
                case 5: // Exit
                    System.out.println("Exiting application. Thank you!");
                    exitApplication = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        return new ProcessChoiceResult(nextUserStatus, exitApplication);
    }
}

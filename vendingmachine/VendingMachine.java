//Code by Sarvajeet Gada :)
package vendingmachine;

import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.ArrayList; // Added import
import java.util.List;    // Added import
import vendingmachine.product.bean.Products; // Required for List<Products>
import vendingmachine.products.CandyBar; // Added import
import vendingmachine.products.CocaCola;
import vendingmachine.products.Gum;
import vendingmachine.products.PotatoChips;
import vendingmachine.user.UserAccount;
import vendingmachine.user.UserManagement;
import vendingmachine.utilities.VendingMachineUtilities;
import vendingmachine.ui.MenuHandler;
import vendingmachine.ui.ProcessChoiceResult;

/**
 * Main class for the Vending Machine application.
 * This class orchestrates the overall application flow. It initializes product inventory,
 * manages user sessions (login/logout), and processes user interactions through a console menu.
 * Menu display and choice processing are delegated to the {@link MenuHandler} class.
 * User account management is handled by {@link UserManagement}, and various utilities
 * (like product restocking, account operations) are provided by {@link VendingMachineUtilities}.
 * Payment processing for coin-based transactions is handled by {@link vendingmachine.payment.PaymentProcessor}.
 */
public class VendingMachine {

	private static UserManagement userManager = new UserManagement(); // Manages user accounts
	private static UserAccount currentUser = null; // Holds the currently logged-in user, if any
	private static Scanner mainScanner = new Scanner(System.in); // Scanner for reading main menu input
	private static List<Products> allProducts; // List to hold all product instances

	// displayProductListing, displayLoggedOutMenu, and displayLoggedInMenu methods were moved to MenuHandler.

	/**
	 * Initializes the list of products available in the vending machine.
	 * Products are added to the `allProducts` list. This method should be called
	 * once at the beginning of the application lifecycle.
	 */
	private static void initializeProducts() {
		allProducts = new ArrayList<>();
		// Assuming Gum, CocaCola, PotatoChips classes exist and have default constructors
		// or constructors that set their specific properties.
		allProducts.add(new Gum());        // Index 0
		allProducts.add(new CocaCola());   // Index 1
		allProducts.add(new PotatoChips());// Index 2
		allProducts.add(new CandyBar());   // Index 3 - Added new product
		// To add a new product later, one would add, for example:
		// allProducts.add(new SomeOtherProduct());
		// And then ensure MenuHandler and other relevant classes can handle variable product list sizes.
	}

	/**
	 * The main entry point for the Vending Machine application.
	 * It sets up the initial state (products, user manager), and then enters a loop
	 * to display menus, get user input, and process choices via {@link MenuHandler}.
	 * The loop continues until the user chooses to exit.
	 * It also handles user session state (logged in/out) and updates it based on
	 * results from menu choice processing.
	 *
	 * @param args Command line arguments (not used).
	 * @throws InterruptedException If any thread operations are interrupted (currently not explicitly used but part of original signature).
	 * @throws MalformedURLException If any URL operations encounter issues (currently not explicitly used but part of original signature).
	 */
	public static void main( String[] args ) throws InterruptedException, MalformedURLException {

		initializeProducts(); // Initialize the list of all products

		// Pre-populate with a test user for easier testing
		userManager.createUser("testUser", "1234", 1000);


		int indexValue;
		boolean exitApp = false;

		do {
			if (currentUser == null) {
				MenuHandler.displayLoggedOutMenu(allProducts);
			} else {
				MenuHandler.displayLoggedInMenu(currentUser, allProducts);
			}

			if (mainScanner.hasNextInt()) {
				indexValue = mainScanner.nextInt();
				mainScanner.nextLine(); // consume newline
			} else {
				System.out.println("Invalid input. Please enter a number.");
				mainScanner.nextLine(); // consume invalid input
				indexValue = -1; // Invalid choice to force loop continuation or error handling
			}

			// Process the user's choice using MenuHandler
			ProcessChoiceResult result;
			if (currentUser == null) { // User is currently logged out
				result = MenuHandler.processLoggedOutChoice(indexValue, mainScanner, userManager, allProducts);
			} else { // User is currently logged in
				result = MenuHandler.processLoggedInChoice(indexValue, mainScanner, currentUser, userManager, allProducts);
			}

			// Update current user status (e.g., after login/logout) and exit flag based on processing result
			currentUser = result.currentUser;
			exitApp = result.exitApplication;

			// After each action (if not exiting), check and update the vending machine's empty status.
			// This flag might be used by other parts of the system or for display purposes.
			if (!exitApp) {
				VendingMachineUtilities.vendingMachineEmpty(allProducts);
			}

		} while( !exitApp );

		mainScanner.close();
		System.out.println("Application closed.");
	}
}

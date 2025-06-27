package vendingmachine.utilities;

import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.InputMismatchException; // Added for robust input

import vendingmachine.currency.Currency;
import vendingmachine.flags.Flag;
import vendingmachine.product.bean.Products;
import vendingmachine.product.utilities.ProductUtilities;
import vendingmachine.user.UserAccount; // Added import
import vendingmachine.user.UserManagement; // Added import
import vendingmachine.payment.PaymentProcessor; // Added import

/**
 * Provides utility methods for various operations of the Vending Machine.
 * This class is responsible for handling user input for product selection,
 * managing product stock (restocking, checking if empty), coordinating purchase processes
 * (delegating to PaymentProcessor for coin payments or handling account payments directly),
 * and managing user account interactions like login, creation, and balance operations.
 * It extends the {@link Currency} class, primarily to inherit coin value definitions,
 * though direct coin processing has largely moved to {@link PaymentProcessor}.
 */
public class VendingMachineUtilities extends Currency {

	// private static int amountPaid; // Removed - coin payment state now managed in PaymentProcessor
	private static int vendingMachineEmptyFlag; // Flag indicating if all products are out of stock.
	private static final Scanner utilScanner = new Scanner(System.in); // Centralized Scanner for utility input.

	/**
	 * Gets the flag indicating whether the vending machine is completely out of stock.
	 *
	 * @return 0 if all products are empty, 1 otherwise.
	 */
	public static int getVendingMachineEmptyFlag() {
		return vendingMachineEmptyFlag;
	}

	/**
	 * Sets the flag indicating whether the vending machine is completely out of stock.
	 * This flag is typically updated after purchases or restocking.
	 *
	 * @param flagValue 0 if all products are empty, 1 otherwise.
	 */
	public static void setVendingMachineEmptyFlag( int flagValue ) {
		VendingMachineUtilities.vendingMachineEmptyFlag = flagValue;
	}

	/**
	 * Prompts the user to enter the index of the product they wish to interact with (e.g., for purchase).
	 * Input is validated to ensure it's a number within the valid range of available product indices.
	 * This method is specifically for selecting a product from the displayed list, not for main menu choices.
	 *
	 * @return The valid product index (1-based) entered by the user.
	 */
	public static int getProductIndex() {
		while (true) {
			System.out.println( "Please enter the index of product you want to buy (1-" + Products.getProductsCount() + ")." );
			int indexValue = -1;
			try {
				indexValue = utilScanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println( "Invalid input. Please enter a number." );
			}
			utilScanner.nextLine(); // Consume newline or invalid input

			if (indexValue >= 1 && indexValue <= Products.getProductsCount()) {
				return indexValue;
			} else {
				// Error message printed if not an InputMismatchException but still out of range
				if (indexValue != -1) { // Avoid double error message if already caught by InputMismatch
					System.out.println( "User entered Incorrect index value." );
				}
				System.out.println( "Index value should range from 1 to " + (Products.getProductsCount()) );
			}
		}
	}

	/**
	 * Prompts the user to enter a coin (NIKEL, DIMES, or QUARTER).
	 * Validates the input to ensure it's a valid coin type.
	 * @return The valid coin string entered by the user.
	 */
	// Coin processing methods (getCoin, calculateInsertedCoinsValue, getTotalAmountFromUser, calculateChange, buy)
	// have been moved to the vendingmachine.payment.PaymentProcessor class.

	/**
	 * Initiates the purchase of a product using coin payment.
	 * This method checks for product availability. If available, it creates a
	 * {@link PaymentProcessor} instance to handle the coin insertion and change calculation.
	 * Product quantity is decremented by the {@code PaymentProcessor} upon successful payment.
	 *
	 * @param product The {@link Products} object to be purchased.
	 * @throws MalformedURLException If a URL related to product details (if any) is malformed. (Currently not used directly here but part of original signature)
	 * @throws InterruptedException If the thread is interrupted. (Currently not used directly here but part of original signature)
	 */
	public static void purchase( Products product ) throws MalformedURLException, InterruptedException {
		System.out.println( "Process initiated for buying " + product.getProductName() + " with coins..." );
		if (product.getProductQuantity() == 0) {
			System.out.println(product.getProductName() + " is OUT OF STOCK. Please try another item or restock.");
			return; // Exit if out of stock.
		}

		// Optional: Display more product details if necessary, though ProductUtilities.setProductDetails was not used previously.
		// ProductUtilities.setProductDetails(product.getProductName());

		PaymentProcessor paymentProcessor = new PaymentProcessor(utilScanner); // Use the static scanner for the payment processor.
		boolean purchaseSuccessful = paymentProcessor.processCoinPurchase(product);

		if (purchaseSuccessful) {
			// Messages like "Collect dispensed product" and quantity updates are now handled by PaymentProcessor.
			// This utility method now acts as a high-level coordinator for coin purchases.
			// Additional logic after a successful purchase could be added here if needed.
		} else {
			// Payment failed. Messages are handled by PaymentProcessor.
			// Additional logic for a failed purchase could be added here.
		}
		// The final "Thanks for shopping." message is also part of PaymentProcessor.processCoinPurchase.
	}

	/**
	 * Checks if the vending machine is completely out of stock for all provided products.
	 * Sets the internal {@code vendingMachineEmptyFlag} based on the stock status.
	 * This flag can be used by the main application loop to determine if operations should continue.
	 *
	 * @param p1 The first product to check.
	 * @param p2 The second product to check.
	 * @param p3 The third product.
	 */
	public static void vendingMachineEmpty( Products p1, Products p2, Products p3 ) {

		if( p1.getProductQuantity() == 0 && p2.getProductQuantity() == 0 && p3.getProductQuantity() == 0 ) {
			setVendingMachineEmptyFlag( 0 );
			System.out.println( "Vending Machine is OUT OF STOCK !!" );
		} else {
			setVendingMachineEmptyFlag( 1 );
		}

	}

	/**
	 * Prompts the user to enter the index of the product they want to restock.
	 * Validates the input to ensure it's within the valid range (1-3).
	 * @return The valid product index (1, 2, or 3) entered by the user.
	 */
	private static int getProductIndexToRestock() {
		while (true) {
			System.out.println( "Enter the index of the product you want to restock:" );
			System.out.println( "1: Gum, 2: CocaCola, 3: PotatoChips" );
			int indexValue = -1;
			try {
				indexValue = utilScanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println( "Invalid input. Please enter a number (1, 2, or 3)." );
			}
			utilScanner.nextLine(); // Consume newline or invalid input

			if (indexValue >= 1 && indexValue <= 3) {
				return indexValue;
			} else {
				if (indexValue != -1) { // Avoid double error message
					System.out.println( "Invalid index. Please enter 1, 2, or 3." );
				}
			}
		}
	}

	/**
	 * Prompts the user to enter the quantity to add for restocking.
	 * Validates the input to ensure it's a positive integer.
	 * @return The valid positive quantity entered by the user.
	 */
	private static int getQuantityToRestock() {
		while (true) {
			System.out.println( "Enter the quantity to add for the selected product:" );
			int quantity = -1;
			try {
				quantity = utilScanner.nextInt();
				if (quantity > 0) {
					utilScanner.nextLine(); // Consume newline only after a successful int read
					return quantity;
				} else {
					System.out.println( "Invalid quantity. Please enter a positive number." );
				}
			} catch (InputMismatchException e) {
				System.out.println( "Invalid input. Please enter a whole number." );
			}
			// If we reach here, it's either an error or quantity <= 0
			// For InputMismatchException, nextLine() might have already been called in some cases,
			// but it's crucial to consume the rest of the line if not.
			// However, if nextInt() fails, it doesn't consume the token.
			// So, if an exception occurred, or if quantity was not > 0, we must ensure the line is cleared.
			// The `finally` block in handleCreateAccount / handleLoadBalance is a good pattern.
			// For here, if quantity was read but not >0, nextLine() wasn't called.
			// If nextInt() threw exception, nextLine() wasn't called.
			if (utilScanner.hasNextLine()) { // Check if there's something to consume
                 utilScanner.nextLine(); // Consume the rest of the invalid input line
            }
		}
	}

	/**
	 * Allows restocking of products in the vending machine.
	 * It displays the current stock of all products, then prompts the user to select a product
	 * and enter a quantity to add. The selected product's quantity is then updated.
	 * This operation also triggers an update of the {@code vendingMachineEmptyFlag}.
	 *
	 * @param gum The {@link Products} instance representing Gum.
	 * @param cocacola The {@link Products} instance representing CocaCola.
	 * @param potatochips The {@link Products} instance representing PotatoChips.
	 */
	public static void restockProducts( Products gum, Products cocacola, Products potatochips ) {
		System.out.println( "\nCurrent Stock:" );
		System.out.println( "1: " + gum.getProductName() + " - Quantity: " + gum.getProductQuantity() );
		System.out.println( "2: " + cocacola.getProductName() + " - Quantity: " + cocacola.getProductQuantity() );
		System.out.println( "3: " + potatochips.getProductName() + " - Quantity: " + potatochips.getProductQuantity() );
		System.out.println();

		int productIndex = getProductIndexToRestock();
		int quantityToAdd = getQuantityToRestock();

		Products selectedProduct = null;
		String productName = "";

		switch ( productIndex ) {
			case 1:
				selectedProduct = gum;
				productName = gum.getProductName();
				break;
			case 2:
				selectedProduct = cocacola;
				productName = cocacola.getProductName();
				break;
			case 3:
				selectedProduct = potatochips;
				productName = potatochips.getProductName();
				break;
			default:
				// Should not happen due to validation in getProductIndexToRestock
				System.out.println( "Error: Invalid product index." );
				return;
		}

		selectedProduct.setProductQuantity( selectedProduct.getProductQuantity() + quantityToAdd );
		System.out.println( "Successfully restocked " + productName + "." );
		System.out.println( "Added: " + quantityToAdd + ". New total: " + selectedProduct.getProductQuantity() );
		System.out.println();
		vendingMachineEmpty(gum, cocacola, potatochips); // Update empty flag
	}

	// --- New User Account Management Methods ---

	/**
	 * Handles the user login process.
	 * Prompts for UserID and PIN, then attempts to authenticate the user
	 * using the provided {@link UserManagement} instance.
	 *
	 * @param userManager The {@link UserManagement} instance responsible for user data and authentication.
	 * @return The {@link UserAccount} object if login is successful; {@code null} otherwise.
	 */
	public static UserAccount handleLogin(UserManagement userManager) {
		System.out.print( "Enter UserID: " );
		String userID = utilScanner.nextLine();
		System.out.print( "Enter PIN: " );
		String pin = utilScanner.nextLine();

		UserAccount user = userManager.authenticateUser(userID, pin);
		if (user != null) {
			System.out.println( "Login successful. Welcome " + userID + "!" );
			return user;
		} else {
			System.out.println( "Login failed. Invalid UserID or PIN." );
			return null;
		}
	}

	/**
	 * Handles the user account creation process.
	 * Prompts for a new UserID, PIN, and an initial deposit amount.
	 * Validates inputs and then attempts to create the user account via the {@link UserManagement} instance.
	 *
	 * @param userManager The {@link UserManagement} instance responsible for creating new user accounts.
	 */
	public static void handleCreateAccount(UserManagement userManager) {
		System.out.print( "Enter new UserID: " );
		String userID = utilScanner.nextLine();
		System.out.print( "Enter new PIN: " );
		String pin = utilScanner.nextLine();
		int initialDeposit = 0;
		boolean validInput = false;
		while(!validInput) {
			System.out.print( "Enter initial deposit amount (in cents, non-negative): " );
			try {
				initialDeposit = utilScanner.nextInt();
				if (initialDeposit >= 0) {
					validInput = true;
				} else {
					System.out.println("Deposit cannot be negative. Please try again.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
			} finally {
				utilScanner.nextLine(); // Consume newline or invalid input
			}
		}

		if (userID.isEmpty() || pin.isEmpty()) {
			System.out.println("Account creation failed. UserID and PIN cannot be empty.");
			return;
		}

		if (userManager.createUser(userID, pin, initialDeposit)) {
			System.out.println( "Account created successfully for " + userID + "." );
		} else {
			System.out.println( "Account creation failed. UserID might be taken or inputs invalid." );
		}
	}

	/**
	 * Displays the current balance of the provided {@link UserAccount}.
	 * If the provided user account is null (e.g., no user is logged in), an error message is printed.
	 *
	 * @param currentUser The {@link UserAccount} whose balance is to be displayed.
	 */
	public static void handleViewBalance(UserAccount currentUser) {
		if (currentUser != null) {
			System.out.println( "Your current balance is: " + currentUser.getBalance() + " cents." );
		} else {
			// This case should ideally be prevented by UI flow (e.g., option not shown if not logged in).
			System.out.println( "Error: No user logged in to view balance." );
		}
	}

	/**
	 * Handles loading (crediting) funds to the current user's account.
	 * Prompts the user for the amount to load and validates that it is a positive integer.
	 *
	 * @param currentUser The {@link UserAccount} to which funds will be loaded. Must not be null.
	 */
	public static void handleLoadBalance(UserAccount currentUser) {
		if (currentUser != null) {
			int amountToLoad = 0;
			boolean validInput = false;
			while(!validInput) {
				System.out.print( "Enter amount to load (in cents, positive number): " );
				try {
					amountToLoad = utilScanner.nextInt();
					if (amountToLoad > 0) {
						validInput = true;
					} else {
						System.out.println("Amount must be positive. Please try again.");
					}
				} catch (InputMismatchException e) {
					System.out.println("Invalid input. Please enter a number.");
				} finally {
					utilScanner.nextLine(); // Consume newline or invalid input
				}
			}

			if (currentUser.credit(amountToLoad)) {
				System.out.println( "Balance updated. New balance: " + currentUser.getBalance() + " cents." );
			} else {
				// This case should ideally not be reached if amountToLoad is validated as positive
				System.out.println( "Invalid amount. Balance not loaded." );
			}
		} else {
			System.out.println( "Error: No user logged in." ); // Should not happen
		}
	}

	/**
	 * Processes a product purchase using a user's account balance.
	 * Checks for product availability and sufficient funds in the user's account.
	 * If successful, it debits the account and decrements the product quantity.
	 *
	 * @param product The {@link Products} object to be purchased.
	 * @param currentUser The {@link UserAccount} of the logged-in user making the purchase. Must not be null.
	 */
	public static void purchaseWithAccount(Products product, UserAccount currentUser) {
		if (currentUser == null) {
			// This check is a safeguard; UI flow should prevent this method from being called without a logged-in user.
			System.out.println( "Error: No user logged in for account purchase." );
			return;
		}

		if (product.getProductQuantity() == 0) {
			System.out.println( product.getProductName() + " is OUT OF STOCK." );
			System.out.println( "Thanks for shopping." ); // Consistent message
			System.out.println();
			return;
		}

		System.out.println( "Attempting purchase of " + product.getProductName() + " for " + product.getProductCost() + " cents using account balance." );
		if (currentUser.debit(product.getProductCost())) {
			product.setProductQuantity(product.getProductQuantity() - 1);
			System.out.println( "Purchase successful with account! Collect " + product.getProductName() + "." );
			System.out.println( "Remaining balance: " + currentUser.getBalance() + " cents." );
			// ProductUtilities.setProductDetails(product.getProductName()); // Optional: if it shows more details
		} else {
			System.out.println( "Purchase failed. Insufficient account balance." );
			System.out.println( "Your balance is " + currentUser.getBalance() + " cents. Product cost is " + product.getProductCost() + " cents." );
		}
		System.out.println( "Thanks for shopping." );
		System.out.println();
	}
}

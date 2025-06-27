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

/**
 * Provides utility methods for the vending machine operations.
 * This class handles user input, coin calculations, purchase processing,
 * checking the vending machine's stock status, and user account management tasks.
 * It extends the {@link Currency} class to access coin values.
 */
public class VendingMachineUtilities extends Currency {

	private static int amountPaid;
	private static int vendingMachineEmptyFlag;
	private static final Scanner utilScanner = new Scanner(System.in); // Centralized Scanner

	/**
	 * Gets the total amount paid by the user.
	 * @return The amount paid in cents.
	 */
	public static int getAmountPaid() {

		return amountPaid;
	}

	/**
	 * Sets the total amount paid by the user.
	 * @param amountPaid The amount paid in cents.
	 */
	public static void setAmountPaid( int amountPaid ) {

		VendingMachineUtilities.amountPaid = amountPaid;
	}

	/**
	 * Gets the flag indicating whether the vending machine is empty.
	 * @return 0 if empty, 1 otherwise.
	 */
	public static int getVendingMachineEmptyFlag() {

		return vendingMachineEmptyFlag;
	}

	/**
	 * Sets the flag indicating whether the vending machine is empty.
	 * @param vendingMachineEmptyFlag 0 if empty, 1 otherwise.
	 */
	public static void setVendingMachineEmptyFlag( int vendingMachineEmptyFlag ) {

		VendingMachineUtilities.vendingMachineEmptyFlag = vendingMachineEmptyFlag;
	}

	/**
	 * Prompts the user to enter the index of the product they want to buy.
	 * Validates the input to ensure it's within the valid range of product indices.
	 * @return The valid product index entered by the user.
	 */
	public static int getProductIndex() {
		// This method is for product selection during purchase,
		// distinct from main menu choice.
		System.out.println( "Please enter the index of product you want to buy (1-" + Products.getProductsCount() + ")." );
		int indexValue = -1;
		try {
			indexValue = utilScanner.nextInt();
		} catch (InputMismatchException e) {
			// Handled below by default value and re-prompt
		}
		utilScanner.nextLine(); // Consume newline

		if( indexValue < 1 || indexValue > (Products.getProductsCount()) ) {
			System.out.println( "User entered Incorrect index value." );
			System.out.println( "Index value should range from 1 to " + (Products.getProductsCount()) );
			return getProductIndex();
		} else {
			return indexValue;
		}
	}

	/**
	 * Prompts the user to enter a coin (NIKEL, DIMES, or QUARTER).
	 * Validates the input to ensure it's a valid coin type.
	 * @return The valid coin string entered by the user.
	 */
	public static String getCoin() {
		System.out.println( "Please enter Coin. (Valid coin values are NIKEL, DIMES and QUARTER.)" );
		String coin = utilScanner.next().toUpperCase(); // Read and convert to upper for easier comparison
		utilScanner.nextLine(); // Consume newline

		if( coin.equals( "NIKEL" ) || coin.equals( "DIMES" ) || coin.equals( "QUARTER" ) ) {
			System.out.println( "You entered a " + coin );
			return coin;
		} else {
			System.out.println( "You entered an incorrect coin value." );
			System.out.println( "Valid coin values are NIKEL, DIMES and QUARTER." );
			// Recursive call or loop might be better, but for now, this matches original style
			return getCoin();
		}
	}


	/**
	 * Calculates the value of the inserted coin and adds it to the total amount paid.
	 * @param insertedCoin The string representation of the coin inserted (e.g., "NIKEL", "DIMES", "QUARTER").
	 */
	public static void calculateInsertedCoinsValue( String insertedCoin ) {

		if( insertedCoin.equals( "NIKEL" ) ) {
			setAmountPaid( getAmountPaid() + getNikel() );
		} else if( insertedCoin.equals( "DIMES" ) ) {
			setAmountPaid( getAmountPaid() + getDimes() );
		} else if( insertedCoin.equals( "QUARTER" ) ) {
			setAmountPaid( getAmountPaid() + getQuarter() );
		}
	}

	/**
	 * Prompts the user to insert coins until they indicate they are done.
	 * Calculates the total value of the inserted coins.
	 */
	public static void getTotalAmountFromUser() {

		String userEntersMoreCoin;
		do {
			String coin = getCoin();
			calculateInsertedCoinsValue( coin );
			System.out.println( "Total amount paid is: " + getAmountPaid() + " cent." );
			System.out.println( "Enter 'Y' to enter more coins or any other key to proceed:" );
			userEntersMoreCoin = utilScanner.nextLine();
		} while( userEntersMoreCoin.equalsIgnoreCase( "y" ) );
	}


	/**
	 * Calculates the change to be returned to the user after a purchase.
	 * Handles cases of insufficient payment, exact payment, and overpayment.
	 * Resets the amount paid after calculation.
	 * @param cost The cost of the product being purchased.
	 * @return A flag indicating the sufficiency of the amount paid ({@link Flag#INSUFFICIENTAMOUNT} or {@link Flag#SUFFICIENTAMOUNT}).
	 */
	public static int calculateChange( int cost ) {

		if( getAmountPaid() < cost ) {
			System.out.println( "Insufficient cash supplied." );
			System.out.println( "Please collect the cash coins." );
			setAmountPaid( 0 );
			return Flag.INSUFFICIENTAMOUNT.getFlag();
		} else if( getAmountPaid() == cost ) {
			System.out.println( "You paid the exact amount" );
			setAmountPaid( 0 );;
			return Flag.SUFFICIENTAMOUNT.getFlag();
		} else {
			System.out.println( "You have paid " + amountPaid + " cent" );
			System.out.println( "Please collect the remaining cash " + (amountPaid - cost) + " cent" );
			setAmountPaid( 0 );
			return Flag.SUFFICIENTAMOUNT.getFlag();
		}
	}

	/**
	 * Processes the purchase of a product if sufficient payment is made.
	 * Dispenses the product, updates its quantity, and provides feedback to the user.
	 * @param product The product to be purchased.
	 */
	public static void buy( Products product ) {

		int returnValue = calculateChange( product.getProductCost() );
		if( returnValue != Flag.INSUFFICIENTAMOUNT.getFlag() ) {
			product.setProductQuantity( product.getProductQuantity() - 1 );
			System.out.println( "Collect the dispensed product." );
			System.out.println( "You bought " + product.getProductName() + " at cost of " + product.getProductCost() + "cent." );
			System.out.println( "Thanks for shopping." );
			System.out.println();
		}
	}

	/**
	 * Initiates the purchase process for a selected product.
	 * Checks product availability, gets payment from the user, and completes the purchase.
	 * @param product The product to be purchased.
	 * @throws MalformedURLException If a malformed URL has occurred (not directly used here but propagated).
	 * @throws InterruptedException If the thread is interrupted while sleeping (not directly used here but propagated).
	 */
	public static void purchase( Products product ) throws MalformedURLException, InterruptedException {

		System.out.println( "Process initiated for buying " + product.getProductName() + " ..." );
		if( product.getProductQuantity() == 0 ) {
			System.out.println( product.getProductName() + " is OUT OF STOCK. Please enter any other index value" );
		} else {
			ProductUtilities.setProductDetails( product.getProductName() );

			VendingMachineUtilities.getTotalAmountFromUser();
			VendingMachineUtilities.buy( product );
		}

	}

	/**
	 * Checks if the vending machine is out of stock for all products.
	 * Sets the {@link #vendingMachineEmptyFlag} accordingly.
	 * @param p1 The first product.
	 * @param p2 The second product.
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
		System.out.println( "Enter the index of the product you want to restock:" );
		System.out.println( "1: Gum, 2: CocaCola, 3: PotatoChips" );
		int indexValue = -1;
		try {
			indexValue = utilScanner.nextInt();
		} catch (InputMismatchException e) {
			// Handled below
		}
		utilScanner.nextLine(); // Consume newline

		if ( indexValue < 1 || indexValue > 3 ) {
			System.out.println( "Invalid index. Please enter 1, 2, or 3." );
			return getProductIndexToRestock(); // Recursive call for valid input
		}
		return indexValue;
	}

	/**
	 * Prompts the user to enter the quantity to add for restocking.
	 * Validates the input to ensure it's a positive integer.
	 * @return The valid positive quantity entered by the user.
	 */
	private static int getQuantityToRestock() {
		System.out.println( "Enter the quantity to add for the selected product:" );
		int quantity = -1;
		try {
			quantity = utilScanner.nextInt();
		} catch (InputMismatchException e) {
			// Handled below
		}
		utilScanner.nextLine(); // Consume newline

		if ( quantity <= 0 ) {
			System.out.println( "Invalid quantity. Please enter a positive number." );
			return getQuantityToRestock(); // Recursive call for valid input
		}
		return quantity;
	}

	/**
	 * Allows the user to restock products in the vending machine.
	 * Displays current stock, prompts for product and quantity to add,
	 * and updates the product quantity.
	 * @param gum The Gum product object.
	 * @param cocacola The CocaCola product object.
	 * @param potatochips The PotatoChips product object.
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
	 * @param userManager The UserManagement instance.
	 * @return UserAccount object if login is successful, null otherwise.
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
	 * @param userManager The UserManagement instance.
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
	 * Displays the current user's account balance.
	 * @param currentUser The currently logged-in UserAccount.
	 */
	public static void handleViewBalance(UserAccount currentUser) {
		if (currentUser != null) {
			System.out.println( "Your current balance is: " + currentUser.getBalance() + " cents." );
		} else {
			System.out.println( "Error: No user logged in." ); // Should not happen if called correctly
		}
	}

	/**
	 * Handles loading balance to the current user's account.
	 * @param currentUser The currently logged-in UserAccount.
	 */
	public static void handleLoadBalance(UserAccount currentUser) { // userManager removed as currentUser.credit is direct
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
	 * Processes a product purchase using the user's account balance.
	 * @param product The product to be purchased.
	 * @param currentUser The currently logged-in UserAccount.
	 */
	public static void purchaseWithAccount(Products product, UserAccount currentUser) {
		if (currentUser == null) {
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

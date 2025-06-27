//Code by Sarvajeet Gada :)
package vendingmachine;

import java.net.MalformedURLException;
import java.util.Scanner; // Added import
import vendingmachine.products.CocaCola;
import vendingmachine.products.Gum;
import vendingmachine.products.PotatoChips;
import vendingmachine.user.UserAccount; // Added import
import vendingmachine.user.UserManagement; // Added import
import vendingmachine.utilities.VendingMachineUtilities;

/**
 * Represents a vending machine that allows users to purchase products.
 * This class initializes products, displays them to the user,
 * and processes purchase requests, including user account interactions.
 */
public class VendingMachine {

	private static UserManagement userManager = new UserManagement();
	private static UserAccount currentUser = null;
	private static Scanner mainScanner = new Scanner(System.in);

	private static void displayProductListing(Products gum, Products cocacola, Products potatochips) {
		System.out.println( "Index \t ProductName \t\t Cost \t\t Quantity Available " );
		System.out.println( "1    \t " + gum.getProductName() + "\t\t\t " + gum.getProductCost() + " cent\t\t" + gum.getProductQuantity() );
		System.out.println( "2    \t " + cocacola.getProductName() + "\t\t " + cocacola.getProductCost() + " cent\t\t"
				+ cocacola.getProductQuantity() );
		System.out.println( "3    \t " + potatochips.getProductName() + "\t\t " + potatochips.getProductCost() + " cent\t\t"
				+ potatochips.getProductQuantity() );
	}

	private static void displayLoggedOutMenu(Products gum, Products cocacola, Products potatochips) {
		System.out.println( "\n--- Vending Machine Menu ---" );
		displayProductListing(gum, cocacola, potatochips);
		System.out.println( "4    \t Restock Products" );
		System.out.println( "5    \t Login to User Account" );
		System.out.println( "6    \t Create User Account" );
		System.out.println( "7    \t Exit" );
		System.out.print( "Please enter your choice: " );
	}

	private static void displayLoggedInMenu(Products gum, Products cocacola, Products potatochips) {
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
	 * The main method for the vending machine application.
	 * Initializes products, displays them, and handles user interaction for purchasing items.
	 *
	 * @param args Command line arguments (not used).
	 * @throws InterruptedException If the thread is interrupted while sleeping.
	 * @throws MalformedURLException If a malformed URL has occurred.
	 */
	public static void main( String[] args ) throws InterruptedException, MalformedURLException {

		Gum gum = new Gum();
		CocaCola cocacola = new CocaCola();
		PotatoChips potatochips = new PotatoChips();

		// Pre-populate with a test user for easier testing
		userManager.createUser("testUser", "1234", 1000);


		int indexValue;
		boolean exitApp = false;

		do {
			if (currentUser == null) {
				displayLoggedOutMenu(gum, cocacola, potatochips);
			} else {
				displayLoggedInMenu(gum, cocacola, potatochips);
			}

			if (mainScanner.hasNextInt()) {
				indexValue = mainScanner.nextInt();
				mainScanner.nextLine(); // consume newline
			} else {
				System.out.println("Invalid input. Please enter a number.");
				mainScanner.nextLine(); // consume invalid input
				indexValue = -1; // Invalid choice to force loop continuation or error handling
			}


			if (currentUser == null) { // Logged Out State
				switch( indexValue ) {
					case 1: // Buy Gum
						VendingMachineUtilities.purchase( gum );
						break;
					case 2: // Buy CocaCola
						VendingMachineUtilities.purchase( cocacola );
						break;
					case 3: // Buy PotatoChips
						VendingMachineUtilities.purchase( potatochips );
						break;
					case 4: // Restock
						VendingMachineUtilities.restockProducts( gum, cocacola, potatochips );
						break;
					case 5: // Login
						currentUser = VendingMachineUtilities.handleLogin(userManager);
						break;
					case 6: // Create Account
						VendingMachineUtilities.handleCreateAccount(userManager);
						break;
					case 7: // Exit
						System.out.println( "Exiting application. Thank you!" );
						exitApp = true;
						break;
					default:
						System.out.println( "Invalid choice. Please try again." );
						break;
				}
			} else { // Logged In State
				switch( indexValue ) {
					case 1: // Buy Gum
					case 2: // Buy CocaCola
					case 3: // Buy PotatoChips
						Products selectedProduct = null;
						if (indexValue == 1) selectedProduct = gum;
						else if (indexValue == 2) selectedProduct = cocacola;
						else selectedProduct = potatochips;

						System.out.print( "Pay with account balance (Y/N)? " );
						String payWithAccountChoice = mainScanner.nextLine().trim();
						if (payWithAccountChoice.equalsIgnoreCase("Y")) {
							VendingMachineUtilities.purchaseWithAccount(selectedProduct, currentUser);
						} else {
							VendingMachineUtilities.purchase( selectedProduct );
						}
						break;
					case 4: // Restock
						VendingMachineUtilities.restockProducts( gum, cocacola, potatochips );
						break;
					case 5: // View Balance
						VendingMachineUtilities.handleViewBalance(currentUser);
						break;
					case 6: // Load Balance
						VendingMachineUtilities.handleLoadBalance(currentUser);
						break;
					case 7: // Logout
						currentUser = null;
						System.out.println( "Logged out successfully." );
						break;
					case 8: // Exit
						System.out.println( "Exiting application. Thank you!" );
						exitApp = true;
						break;
					default:
						System.out.println( "Invalid choice. Please try again." );
						break;
				}
			}
			// Update vending machine empty status if needed, but loop is controlled by exitApp
			if (!exitApp) {
				VendingMachineUtilities.vendingMachineEmpty( gum, potatochips, cocacola );
			}

		} while( !exitApp );

		mainScanner.close();
		System.out.println("Application closed.");
	}
}

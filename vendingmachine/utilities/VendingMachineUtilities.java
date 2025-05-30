package vendingmachine.utilities;

import java.net.MalformedURLException;
import java.util.List; // Added for the new method
import java.util.Scanner;

import vendingmachine.currency.Currency;
import vendingmachine.flags.Flag;
import vendingmachine.product.bean.Products;
import vendingmachine.product.utilities.ProductUtilities;

/**
 * Provides utility methods for the vending machine operations.
 * This class handles user input, coin calculations, purchase processing,
 * and checking the vending machine's stock status.
 * It extends the {@link Currency} class to access coin values.
 */
public class VendingMachineUtilities extends Currency {

	private static int amountPaid;
	private static boolean vendingMachineEmptyFlag = true; // Default to true (not empty)
	private static final Scanner inputScanner = new Scanner(System.in);

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
	 * @return `false` if empty, `true` otherwise (i.e., if it has stock).
	 */
	public static boolean getVendingMachineEmptyFlag() {

		return vendingMachineEmptyFlag;
	}

	/**
	 * Sets the flag indicating whether the vending machine is empty.
	 * @param vendingMachineEmptyFlag `false` if empty, `true` otherwise.
	 */
	public static void setVendingMachineEmptyFlag( boolean vendingMachineEmptyFlag ) {

		VendingMachineUtilities.vendingMachineEmptyFlag = vendingMachineEmptyFlag;
	}

	/**
	 * Prompts the user to enter the index of the product they want to buy.
	 * Validates the input to ensure it's within the valid range of product indices.
	 * @return The valid product index entered by the user.
	 */
	public static int getProductIndex() {
		// Scanner inputFromKeyboard = new Scanner(System.in); // Will be replaced by shared scanner later
		int indexValue;
		while (true) {
			System.out.println( "Please enter the index of product you want to buy." );
			// Basic input validation for integer type
			if (inputScanner.hasNextInt()) {
				indexValue = inputScanner.nextInt();
				inputScanner.nextLine(); // Consume newline
				if (indexValue >= 1 && indexValue <= Products.getProductsCount()) {
					return indexValue;
				} else {
					System.out.println( "User entered Incorrect index value." );
					System.out.println( "Index value should range from 1 to " + Products.getProductsCount() );
				}
			} else {
				System.out.println("Invalid input. Please enter a number.");
				inputScanner.nextLine(); // Consume invalid input
			}
		}
	}

	/**
	 * Prompts the user to enter a coin (NIKEL, DIMES, or QUARTER).
	 * Validates the input to ensure it's a valid coin type.
	 * @return The valid coin string entered by the user.
	 */
	public static String getCoin() {
		// Scanner inputFromKeyboard = new Scanner(System.in); // Will be replaced by shared scanner later
		String coin;
		while (true) {
			System.out.println( "Please enter Coin. (Valid coin values are NIKEL, DIMES and QUARTER.)" );
			coin = inputScanner.next();
			inputScanner.nextLine(); // Consume newline

			if (coin.equals("NIKEL") || coin.equals("DIMES") || coin.equals("QUARTER")) {
				System.out.println("You entered a " + coin);
				return coin;
			} else {
				System.out.println("You entered an incorrect coin value.");
				// The message "Valid coin values are NIKEL, DIMES and QUARTER." will be printed again at the start of the loop.
			}
		}
	}

	/**
	 * Calculates the value of the inserted coin and adds it to the total amount paid.
	 * @param insertedCoin The string representation of the coin inserted (e.g., "NIKEL", "DIMES", "QUARTER").
	 */
	public static void calculateInsertedCoinsValue( String insertedCoin ) {
		switch (insertedCoin) {
			case "NIKEL":
				setAmountPaid( getAmountPaid() + getNikel() );
				break;
			case "DIMES":
				setAmountPaid( getAmountPaid() + getDimes() );
				break;
			case "QUARTER":
				setAmountPaid( getAmountPaid() + getQuarter() );
				break;
			default:
				// This case should ideally not be reached if getCoin() validates properly.
				// However, adding a default for robustness or logging.
				System.out.println("Warning: calculateInsertedCoinsValue received an unhandled coin type: " + insertedCoin);
				break;
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
			System.out.println( "Total amount paid is: " + getAmountPaid() + "cent" );
			System.out.println( "Enter y to enter more coins and any other key to exit" );
			// Scanner inputFromKeyboard = new Scanner( System.in ); // Replaced by shared scanner
			userEntersMoreCoin = inputScanner.next();
			inputScanner.nextLine(); // Consume newline
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
			setVendingMachineEmptyFlag( false ); // Machine is empty
			System.out.println( "Vending Machine is OUT OF STOCK !!" );
		} else {
			setVendingMachineEmptyFlag( true ); // Machine is not empty
		}

	}

	/**
	 * Checks if the vending machine is out of stock for all products in the provided list.
	 * Sets the {@link #vendingMachineEmptyFlag} accordingly.
	 * Prints a message if the machine is out of stock.
	 * @param products A list of products to check.
	 */
	public static void vendingMachineEmpty( List<Products> products ) {
		boolean allEmpty = true;
		if (products == null || products.isEmpty()) {
			allEmpty = true; // Or handle as an error, for now, consider empty list as "empty machine"
		} else {
			for (Products product : products) {
				if (product.getProductQuantity() > 0) {
					allEmpty = false;
					break;
				}
			}
		}

		if (allEmpty) {
			setVendingMachineEmptyFlag( false ); // Machine is empty
			System.out.println( "Vending Machine is OUT OF STOCK !!" );
		} else {
			setVendingMachineEmptyFlag( true ); // Machine is not empty
		}
	}

}

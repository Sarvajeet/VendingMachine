package vendingmachine.utilities;

import java.net.MalformedURLException;
import java.util.Scanner;

import vendingmachine.currency.Currency;
import vendingmachine.flags.Flag;
import vendingmachine.product.bean.Products;
import vendingmachine.product.utilities.ProductUtilities;


public class VendingMachineUtilities extends Currency {

	private static int amountPaid;
	private static int vendingMachineEmptyFlag;

	public static int getAmountPaid() {

		return amountPaid;
	}

	public static void setAmountPaid( int amountPaid ) {

		VendingMachineUtilities.amountPaid = amountPaid;
	}

	public static int getVendingMachineEmptyFlag() {

		return vendingMachineEmptyFlag;
	}

	public static void setVendingMachineEmptyFlag( int vendingMachineEmptyFlag ) {

		VendingMachineUtilities.vendingMachineEmptyFlag = vendingMachineEmptyFlag;
	}

	public static int getProductIndex() {

		System.out.println( "Please enter the index of product you want to buy." );
		Scanner inputFromKeyboard = new Scanner( System.in );
		int indexValue = inputFromKeyboard.nextInt();
		inputFromKeyboard.nextLine();
		if( indexValue < 1 || indexValue > (Products.getProductsCount()) ) {

			System.out.println( "User entered Incorrect index value." );
			System.out.println( "Index value should range form 1 to " + (Products.getProductsCount()) );
			return getProductIndex();
		} else {
			return indexValue;
		}
	}

	public static String getCoin() {

		System.out.println( "Please enter Coin. (Valid coin values are NIKEL, DIMES and QUARTER.)" );
		Scanner inputFromKeyboard = new Scanner( System.in );
		String coin = inputFromKeyboard.next();
		inputFromKeyboard.nextLine();
		if( coin.equals( "NIKEL" ) || coin.equals( "DIMES" ) || coin.equals( "QUARTER" ) ) {
			System.out.println( "You entered a " + coin );
			return coin;

		} else {
			System.out.println( "You entered a incorrect coin value" );
			System.out.println( "Valid coin values are NIKEL, DIMES and QUARTER." );
			return coin;
		}
	}

	public static void calculateInsertedCoinsValue( String insertedCoin ) {

		if( insertedCoin.equals( "NIKEL" ) ) {
			setAmountPaid( getAmountPaid() + getNikel() );
		} else if( insertedCoin.equals( "DIMES" ) ) {
			setAmountPaid( getAmountPaid() + getDimes() );
		} else if( insertedCoin.equals( "QUARTER" ) ) {
			setAmountPaid( getAmountPaid() + getQuarter() );
		}
	}

	public static void getTotalAmountFromUser() {

		String userEntersMoreCoin;
		do {
			String coin = getCoin();
			calculateInsertedCoinsValue( coin );
			System.out.println( "Total amount paid is: " + getAmountPaid() + "cent" );
			System.out.println( "Enter y to enter more coins and any other key to exit" );
			Scanner inputFromKeyboard = new Scanner( System.in );
			userEntersMoreCoin = inputFromKeyboard.next();
			inputFromKeyboard.nextLine();
		} while( userEntersMoreCoin.equalsIgnoreCase( "y" ) );
	}

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

	public static void vendingMachineEmpty( Products p1, Products p2, Products p3 ) {

		if( p1.getProductQuantity() == 0 && p2.getProductQuantity() == 0 && p3.getProductQuantity() == 0 ) {
			setVendingMachineEmptyFlag( 0 );
			System.out.println( "Vending Machine is OUT OF STOCK !!" );
		} else {
			setVendingMachineEmptyFlag( 1 );
		}

	}

}

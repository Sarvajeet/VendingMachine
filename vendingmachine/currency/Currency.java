package vendingmachine.currency;

/**
 * Represents currency denominations used in the vending machine.
 * This class provides constant values for different coins and methods to access them.
 */
public class Currency {

	/**
	 * The value of a Nikel coin in cents.
	 */
	final static int NIKEL = 5;
	/**
	 * The value of a Dimes coin in cents.
	 */
	final static int DIMES = 10;
	/**
	 * The value of a Quarter coin in cents.
	 */
	final static int QUARTER = 25;

	/**
	 * Gets the value of a Nikel coin.
	 * @return The value of a Nikel in cents.
	 */
	public static int getNikel() {

		return NIKEL;
	}

	/**
	 * Gets the value of a Dimes coin.
	 * @return The value of a Dimes in cents.
	 */
	public static int getDimes() {

		return DIMES;
	}

	/**
	 * Gets the value of a Quarter coin.
	 * @return The value of a Quarter in cents.
	 */
	public static int getQuarter() {

		return QUARTER;
	}

}

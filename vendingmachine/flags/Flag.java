package vendingmachine.flags;

/**
 * Represents flags used in the vending machine to indicate various states or outcomes.
 * For example, it can indicate if a sufficient or insufficient amount was paid.
 */
public enum Flag {
	/**
	 * Flag indicating that an insufficient amount was paid.
	 * The associated value is -1.
	 */
	INSUFFICIENTAMOUNT( -1 ),
	/**
	 * Flag indicating that a sufficient amount was paid.
	 * The associated value is 1.
	 */
	SUFFICIENTAMOUNT( 1 );

	private int flag;

	/**
	 * Constructor for the Flag enum.
	 * @param value The integer value associated with the flag.
	 */
	Flag( int value ) {

		flag = value;
	}

	/**
	 * Gets the integer value associated with the flag.
	 * @return The integer value of the flag.
	 */
	public int getFlag() {

		return flag;
	}

}

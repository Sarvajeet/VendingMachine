package vendingmachine.flags;

public enum Flag {
	INSUFFICIENTAMOUNT( -1 ), SUFFICIENTAMOUNT( 1 );

	private int flag;

	Flag( int value ) {

		flag = value;
	}

	public int getFlag() {

		return flag;
	}

}

package vendingmachine.products;

import vendingmachine.product.bean.Products;

/**
 * Represents a CocaCola product in the vending machine.
 * This class extends the {@link Products} class and sets specific properties for CocaCola.
 */
public class CocaCola extends Products {

	/**
	 * Constructs a new CocaCola product.
	 * Initializes CocaCola with a name "CocaCola", cost of 100 cents, and initial quantity of 2.
	 */
	public CocaCola() {

		super( "CocaCola", 100, 2 );
	}

}

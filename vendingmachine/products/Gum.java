package vendingmachine.products;

import vendingmachine.product.bean.Products;

/**
 * Represents a Gum product in the vending machine.
 * This class extends the {@link Products} class and sets specific properties for Gum.
 */
public class Gum extends Products {

	/**
	 * Constructs a new Gum product.
	 * Initializes Gum with a name "Gum", cost of 35 cents, and initial quantity of 2.
	 */
	public Gum() {

		super( "Gum", 35, 2 );
	}

}

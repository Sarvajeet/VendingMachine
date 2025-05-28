package vendingmachine.products;

import vendingmachine.product.bean.Products;

/**
 * Represents a PotatoChips product in the vending machine.
 * This class extends the {@link Products} class and sets specific properties for PotatoChips.
 */
public class PotatoChips extends Products {

	/**
	 * Constructs a new PotatoChips product.
	 * Initializes PotatoChips with a name "PotatoChips", cost of 50 cents, and initial quantity of 2.
	 */
	public PotatoChips() {

		super( "PotatoChips", 50, 2 );
	}

}

//Code by Sarvajeet Gada :)
package vendingmachine;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import vendingmachine.product.bean.Products; // Added for the list
import vendingmachine.products.CocaCola;
import vendingmachine.products.Gum;
import vendingmachine.products.PotatoChips;
import vendingmachine.utilities.VendingMachineUtilities;

/**
 * Represents a vending machine that allows users to purchase products.
 * This class initializes products, displays them to the user,
 * and processes purchase requests.
 */
public class VendingMachine {

	/**
	 * The main method for the vending machine application.
	 * Initializes products, displays them, and handles user interaction for purchasing items.
	 *
	 * @param args Command line arguments (not used).
	 * @throws InterruptedException If the thread is interrupted while sleeping.
	 * @throws MalformedURLException If a malformed URL has occurred.
	 */
	public static void main( String[] args ) throws InterruptedException, MalformedURLException {

		List<Products> products = new ArrayList<>();
		products.add(new Gum());
		products.add(new CocaCola());
		products.add(new PotatoChips());

		int indexValue;
		do {
			System.out.println( "Index \t ProductName \t\t Cost \t\t Quantity Available " );
			for (int i = 0; i < products.size(); i++) {
				Products product = products.get(i);
				// Adjust spacing for alignment, this might need fine-tuning
				String productName = product.getProductName();
				if (productName.length() < 8) { // Assuming tab is 8 spaces, and "CocaCola" is longer
					productName += "\t";
				}
				System.out.println( (i + 1) + "    \t " + productName + "\t\t " + product.getProductCost() + " cent\t\t" + product.getProductQuantity() );
			}
			
			// Assuming getProductIndex() is designed to work with the number of products available.
			// If VendingMachineUtilities.getProductIndex() has hardcoded max index, it might need adjustment.
			// For now, we assume it uses Products.getProductsCount() which should be 3.
			indexValue = VendingMachineUtilities.getProductIndex(); 

			if (indexValue >= 1 && indexValue <= products.size()) {
				VendingMachineUtilities.purchase(products.get(indexValue - 1));
			} else {
				// VendingMachineUtilities.getProductIndex() should ideally handle invalid index messages.
				// Adding a generic message here if it doesn't or for further robustness.
				System.out.println("Invalid product selection. Please try again.");
			}

			// Call the new method in VendingMachineUtilities
			VendingMachineUtilities.vendingMachineEmpty(products);
		} while( VendingMachineUtilities.getVendingMachineEmptyFlag() ); // Condition updated for boolean return type

	}
}

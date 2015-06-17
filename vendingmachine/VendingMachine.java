package vendingmachine;

import java.net.MalformedURLException;
import vendingmachine.products.CocaCola;
import vendingmachine.products.Gum;
import vendingmachine.products.PotatoChips;
import vendingmachine.utilities.VendingMachineUtilities;

public class VendingMachine {

	public static void main( String[] args ) throws InterruptedException, MalformedURLException {

		Gum gum = new Gum();
		CocaCola cocacola = new CocaCola();
		PotatoChips potatochips = new PotatoChips();

		int indexValue;
		do {
			System.out.println( "Index \t ProductName \t\t Cost \t\t Quantity Available " );
			System.out.println( "1    \t " + gum.getProductName() + "\t\t\t " + gum.getProductCost() + " cent\t\t" + gum.getProductQuantity() );
			System.out.println( "2    \t " + cocacola.getProductName() + "\t\t " + cocacola.getProductCost() + " cent\t\t"
					+ cocacola.getProductQuantity() );
			System.out.println( "3    \t " + potatochips.getProductName() + "\t\t " + potatochips.getProductCost() + " cent\t\t"
					+ potatochips.getProductQuantity() );
			indexValue = VendingMachineUtilities.getProductIndex();

			switch( indexValue ) {
				case 1:
					VendingMachineUtilities.purchase( gum );
					break;
				case 2:
					VendingMachineUtilities.purchase( cocacola );
					break;
				case 3:
					VendingMachineUtilities.purchase( potatochips );
					break;
				default:
					break;
			}

			VendingMachineUtilities.vendingMachineEmpty( gum, potatochips, cocacola );
		} while( VendingMachineUtilities.getVendingMachineEmptyFlag() == 1 );

	}
}

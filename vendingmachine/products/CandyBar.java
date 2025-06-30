package vendingmachine.products;

import vendingmachine.product.bean.Products;

public class CandyBar extends Products {
    public CandyBar() {
        super("CandyBar", 75, 15); // Name, Cost (cents), Initial Quantity
    }
}

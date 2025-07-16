package vendingmachine.inventory;

import vendingmachine.product.bean.Products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManager {

    private Map<String, Integer> inventory;

    public InventoryManager(List<Products> products) {
        inventory = new HashMap<>();
        for (Products product : products) {
            inventory.put(product.getProductName(), product.getProductQuantity());
        }
    }

    public int getStock(String productName) {
        return inventory.getOrDefault(productName, 0);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void decreaseStock(String productName) {
        if (inventory.containsKey(productName)) {
            int currentStock = inventory.get(productName);
            if (currentStock > 0) {
                inventory.put(productName, currentStock - 1);
            }
        }
    }
}

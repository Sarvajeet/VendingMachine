package vendingmachine.inventory;

import java.util.Map;

public class RemoteInventoryManagement {

    private InventoryManager inventoryManager;

    public RemoteInventoryManagement(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public Map<String, Integer> getInventory() {
        return inventoryManager.getInventory();
    }

    public int getInventory(String productName) {
        return inventoryManager.getStock(productName);
    }
}

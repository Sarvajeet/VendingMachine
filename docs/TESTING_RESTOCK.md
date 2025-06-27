# Test Cases for Vending Machine Restocking Functionality

This document outlines test scenarios for the restocking feature of the vending machine.

## 1. Test Restock Option in Menu

*   **Objective:** Verify the "Restock Products" option is displayed in the main menu.
*   **Steps:** Run `VendingMachine.java`. Observe the menu.
*   **Expected Outcome:** Menu should include an option like "4. Restock Products".

## 2. Test Product Selection for Restock

*   **Objective:** Verify the user can select a product to restock.
*   **Steps:**
    1.  Run `VendingMachine.java`.
    2.  Select the restock option (e.g., enter '4').
    3.  When prompted to select a product, enter a valid product index (e.g., 1 for Gum, 2 for CocaCola, 3 for PotatoChips).
*   **Expected Outcome:** The system should then prompt for the quantity to add for the selected product.

## 3. Test Invalid Product Selection for Restock

*   **Objective:** Verify the system handles invalid product index input during restocking.
*   **Steps:**
    1.  Run `VendingMachine.java`.
    2.  Select the restock option.
    3.  Enter an invalid index (e.g., 0, 5, or a non-numeric value like "abc") when prompted for the product.
*   **Expected Outcome:** The system should display an error message (e.g., "Invalid index. Please enter 1, 2, or 3.") and re-prompt for a valid product index.

## 4. Test Quantity Input for Restock

*   **Objective:** Verify the user can input a valid quantity to restock.
*   **Steps:**
    1.  Run `VendingMachine.java`.
    2.  Select the restock option.
    3.  Select a valid product (e.g., enter '1' for Gum).
    4.  Enter a positive integer quantity (e.g., 5) when prompted for quantity.
*   **Expected Outcome:** The product's quantity should be updated. A confirmation message (e.g., "Successfully restocked Gum. Added: 5. New total: [New Quantity]") should be displayed. The main menu, when redisplayed, should show the updated quantity for Gum.

## 5. Test Invalid Quantity Input for Restock

*   **Objective:** Verify the system handles invalid quantity input (e.g., zero, negative, non-numeric).
*   **Steps:**
    1.  Run `VendingMachine.java`.
    2.  Select the restock option.
    3.  Select a valid product.
    4.  Enter an invalid quantity (e.g., -1, 0, "abc") when prompted.
*   **Expected Outcome:** The system should display an error message (e.g., "Invalid quantity. Please enter a positive number.") and re-prompt for a valid quantity.

## 6. Test Quantity Update Accuracy

*   **Objective:** Verify the product quantity is correctly updated after restocking.
*   **Steps:**
    1.  Run `VendingMachine.java`. Note the initial quantity of a product (e.g., Gum).
    2.  Select the restock option.
    3.  Select that product (Gum).
    4.  Enter a quantity to add (e.g., 10).
    5.  Observe the confirmation message.
    6.  After returning to the main menu, observe the displayed quantity for Gum.
*   **Expected Outcome:** The confirmation message should show the correct new quantity (initial quantity + 10). The main menu should display the product with its quantity accurately reflecting this new total.

## 7. Test Vending Machine Status After Restock (from empty)

*   **Objective:** Verify that if the machine was empty, its operational status (`vendingMachineEmptyFlag`) is updated correctly after restocking, allowing further operations.
*   **Steps:**
    1.  Purchase all items from the vending machine until it is empty. The program might typically exit or display an "OUT OF STOCK" message and loop.
    2.  If the program exits, for this test, temporarily modify `VendingMachine.java` so the main loop continues even if the machine is empty (e.g., by changing `while( VendingMachineUtilities.getVendingMachineEmptyFlag() == 1 )` to `while(true)` or by ensuring the restock option is available regardless of the flag).
    3.  Select the restock option.
    4.  Choose a product and restock it with a positive quantity (e.g., Gum, quantity 5).
    5.  The `vendingMachineEmpty` method should be called internally by `restockProducts`.
    6.  After restocking, select the option to purchase a product.
    7.  Attempt to purchase the product that was just restocked (Gum).
*   **Expected Outcome:** After restocking, the `vendingMachineEmptyFlag` should be updated to indicate the machine is no longer empty (if it was). The user should be able to successfully initiate a purchase for the restocked product. (Remember to revert any temporary code changes made to `VendingMachine.java` for this test).

## 8. Test Restocking Multiple Products Sequentially

*   **Objective:** Verify that multiple products can be restocked one after another, and their quantities are updated correctly.
*   **Steps:**
    1.  Run `VendingMachine.java`.
    2.  Select the restock option. Choose Gum and add a quantity (e.g., 5). Verify its quantity on the main menu.
    3.  Select the restock option again. Choose CocaCola and add a quantity (e.g., 3). Verify its quantity on the main menu.
    4.  Select the restock option again. Choose PotatoChips and add a quantity (e.g., 7). Verify its quantity on the main menu.
*   **Expected Outcome:** The quantities for Gum, CocaCola, and PotatoChips should all be correctly updated and displayed on the main menu according to the amounts restocked. Each restock operation should independently update the respective product.

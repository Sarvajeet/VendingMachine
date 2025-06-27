package vendingmachine.payment;

import java.util.Scanner;
import java.util.InputMismatchException; // For getCoin if adapting its try-catch, though original getCoin didn't use it
import vendingmachine.currency.Currency;
import vendingmachine.flags.Flag;
import vendingmachine.product.bean.Products;

/**
 * Handles the processing of coin-based payments for products in the vending machine.
 * This class manages the collection of coins, calculation of the total amount paid,
 * and determination of change. It extends the {@link Currency} class to access
 * coin denomination values and their names.
 */
public class PaymentProcessor extends Currency {

    private int amountPaid; // Stores the total amount paid by the user during a single transaction.
    private Scanner paymentScanner; // Scanner instance for handling user input related to payments.

    /**
     * Constructs a PaymentProcessor instance.
     *
     * @param scanner The Scanner instance to be used for reading user input (e.g., coin types, continue prompts).
     */
    public PaymentProcessor(Scanner scanner) {
        this.amountPaid = 0; // Initialize amount paid to zero for a new payment session.
        this.paymentScanner = scanner;
    }

    /**
     * Gets the total amount currently paid by the user in this transaction.
     *
     * @return The total amount paid in cents.
     */
    public int getAmountPaid() {
        return amountPaid;
    }

    /**
     * Sets the total amount paid by the user. Used internally, for example, to reset after a transaction.
     *
     * @param amount The amount to set in cents.
     */
    private void setAmountPaid(int amount) {
        this.amountPaid = amount;
    }

    // --- Methods moved and adapted from VendingMachineUtilities ---

    /**
     * Prompts the user to enter a coin (NIKEL, DIMES, or QUARTER) and validates the input.
     * This method will loop until a valid coin name is entered.
     *
     * @return The valid coin string (e.g., "NIKEL", "DIMES", "QUARTER") entered by the user, converted to uppercase.
     */
    public String getCoin() {
        while (true) {
            System.out.println("Please enter Coin. (Valid coin values are NIKEL, DIMES and QUARTER.)");
            String coin = paymentScanner.next().toUpperCase();
            paymentScanner.nextLine(); // Consume newline after reading the coin token.

            if (coin.equals(NIKEL_STR) || coin.equals(DIMES_STR) || coin.equals(QUARTER_STR)) {
                System.out.println("You entered a " + coin);
                return coin;
            } else {
                System.out.println("You entered an incorrect coin value.");
                System.out.println("Valid coin values are " + NIKEL_STR + ", " + DIMES_STR + " and " + QUARTER_STR + ".");
            }
        }
    }

    /**
     * Calculates the value of the inserted coin and adds it to the running total for the current transaction.
     * Uses coin name constants from the {@link Currency} class.
     *
     * @param insertedCoin The string representation of the coin inserted (e.g., "NIKEL").
     */
    public void calculateInsertedCoinsValue(String insertedCoin) {
        if (insertedCoin.equals(NIKEL_STR)) {
            setAmountPaid(getAmountPaid() + getNikel()); // getNikel() is from the parent Currency class
        } else if (insertedCoin.equals(DIMES_STR)) {
            setAmountPaid(getAmountPaid() + getDimes()); // getDimes() is from the parent Currency class
        } else if (insertedCoin.equals(QUARTER_STR)) {
            setAmountPaid(getAmountPaid() + getQuarter()); // getQuarter() is from the parent Currency class
        }
    }

    /**
     * Prompts the user to insert coins repeatedly until they indicate they are done.
     * It accumulates the total value of the inserted coins for the current transaction.
     */
    public void getTotalAmountFromUser() {
        String userEntersMoreCoin;
        do {
            String coin = getCoin(); // Calls this class's getCoin method to get validated coin input.
            calculateInsertedCoinsValue(coin); // Updates the internal amountPaid.
            System.out.println("Total amount paid is: " + getAmountPaid() + " cent.");
            System.out.println("Enter 'Y' to enter more coins or any other key to proceed:");
            userEntersMoreCoin = paymentScanner.nextLine();
        } while (userEntersMoreCoin.equalsIgnoreCase("y"));
    }

    /**
     * Calculates the change to be returned to the user after a purchase attempt.
     * It handles cases of insufficient payment, exact payment, and overpayment.
     * The internal `amountPaid` is reset to 0 after this calculation, regardless of outcome.
     *
     * @param cost The cost of the product being purchased in cents.
     * @return A flag from {@link Flag} (e.g., {@code Flag.SUFFICIENTAMOUNT} or {@code Flag.INSUFFICIENTAMOUNT})
     *         indicating the outcome of the payment.
     */
    public int calculateChange(int cost) {
        if (getAmountPaid() < cost) {
            System.out.println("Insufficient cash supplied.");
            System.out.println("Please collect the cash coins."); // Implies returning inserted coins
            setAmountPaid(0); // Reset amount paid as the transaction failed here.
            return Flag.INSUFFICIENTAMOUNT.getFlag();
        } else if (getAmountPaid() == cost) {
            System.out.println("You paid the exact amount. No change due.");
            setAmountPaid(0); // Reset amount paid for the next transaction.
            return Flag.SUFFICIENTAMOUNT.getFlag();
        } else {
            System.out.println("You have paid " + getAmountPaid() + " cent.");
            System.out.println("Please collect the remaining cash " + (getAmountPaid() - cost) + " cent.");
            setAmountPaid(0); // Reset amount paid for the next transaction.
            return Flag.SUFFICIENTAMOUNT.getFlag();
        }
    }

    /**
     * Processes a coin-based purchase for a given product.
     * This method orchestrates the collection of coins from the user, calculates change,
     * and updates the product quantity if the purchase is successful.
     * Note: Product stock check (if quantity > 0) is assumed to be done by the caller before invoking this.
     *
     * @param product The product being purchased.
     * @return {@code true} if the purchase was successful (sufficient funds, product dispensed),
     *         {@code false} otherwise (e.g., insufficient funds).
     */
    public boolean processCoinPurchase(Products product) {
        System.out.println("Processing coin payment for " + product.getProductName() + "...");

        setAmountPaid(0); // Ensure amountPaid is reset for this new transaction.
        getTotalAmountFromUser(); // Collect coins from the user.

        int changeResult = calculateChange(product.getProductCost()); // Determine if payment was sufficient.

        if (changeResult != Flag.INSUFFICIENTAMOUNT.getFlag()) {
            // Payment was sufficient or exact.
            product.setProductQuantity(product.getProductQuantity() - 1); // Dispense product by reducing quantity.
            System.out.println("Collect the dispensed product.");
            System.out.println("You bought " + product.getProductName() + " at cost of " + product.getProductCost() + " cent.");
            System.out.println("Thanks for shopping.");
            System.out.println();
            return true; // Purchase successful.
        } else {
            // Insufficient amount paid. Message already printed by calculateChange.
            System.out.println("Thanks for shopping."); // Consistent final message.
            System.out.println();
            return false; // Purchase failed.
        }
    }
}

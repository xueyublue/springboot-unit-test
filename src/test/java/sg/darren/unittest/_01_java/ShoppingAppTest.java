package sg.darren.unittest._01_java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingAppTest {

    private Product firstProduct;
    private Product secondProduct;
    private Checkout checkout;

    @BeforeEach
    @DisplayName("Given 2 products in the shopping cart")
    public void setup() {
        firstProduct = new Product(1, "USB Drive", "128 GB USB Drive", 19.9F);
        secondProduct = new Product(2, "External Hard Drive", "1 TB External Drive", 79.9F);
        checkout = new Checkout();
        checkout.addToCart(firstProduct);
        checkout.addToCart(secondProduct);
    }

    @Test
    @DisplayName("Test if products added to shopping cart successfully")
    public void testItemsAddedToShoppingCart() {
        // Given 2 products in the shopping cart

        // When
        Product newProduct = new Product(2, "2nd External Hard Drive", "1 TB External Drive", 79.9F);
        checkout.addToCart(newProduct);

        // Then
        assertEquals(3, checkout.getItems().size());
    }

    @Test
    @DisplayName("Test if shopping cart total amount is calculated correctly")
    public void testShoppingCartAmount() {
        // Given 2 products in the shopping cart

        // Then
        assertEquals(2, checkout.getItems().size());
        assertEquals(99.8F, checkout.getTotalAmount());
    }

    @Test
    @DisplayName("Test if shopping cart total amount is calculated correctly")
    public void testDueAmountCalculation() {
        // Given 2 products in the shopping cart

        // When
        checkout.pay(90.8F);

        // Then
        assertEquals(9.0F, checkout.getPaymentDue());
    }

    @Test
    @DisplayName("Test if product removed from shopping cart correctly")
    public void testProductRemovalFromCart() {
        // Given 2 products in the shopping cart

        // When
        checkout.removeFromCart(firstProduct);

        // Then
        assertEquals(1, checkout.getItems().size());
        assertEquals(79.9F, checkout.getTotalAmount());
    }

    @Test
    @DisplayName("Test if payment status is correct")
    public void testIfPaymentStatusIsCorrect() {
        // Given 2 products in the shopping cart

        // When
        checkout.pay(99.8F);
        checkout.complete();

        // Then
        assertEquals(Checkout.PaymentStatus.DONE, checkout.getPaymentStatus());
    }
}
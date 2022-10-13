package sg.darren.unittest.repository._01_java;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Cart {

    private float totalAmount = 0F;
    private List<Product> items = new ArrayList<>();

    public void addToCart(Product p) {
        items.add(p);
        totalAmount = totalAmount + p.getPrice();
    }

    public void removeFromCart(Product p) {
        items.remove(p);
        totalAmount = totalAmount - p.getPrice();
    }

    public void emptyCart() {
        items.clear();
    }

    public List<Product> getItems() {
        return items;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void printCartDetails() {
        log.info("Here are the items in your shopping cart:");
        for (Product p : items) {
            log.info("============================");
            log.info("Product ID:" + p.getId());
            log.info("Product Name:" + p.getName());
            log.info("Product Description" + p.getDescription());
            log.info("Product Price:$" + p.getPrice());
            log.info("============================");
        }
        log.info("Shopping Cart Total:$" + totalAmount);
    }
}

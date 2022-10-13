package sg.darren.unittest._01_java;

public class ShoppingApp {

    public static void main(String[] args) {
        Product firstProduct = Product.builder()
                .id(1)
                .name("USB Drive")
                .description("128GB USB Drive")
                .price(19.9F).build();
        Product secondProduct = Product.builder()
                .id(2)
                .name("External Hard Drive")
                .description("1 TB External Drive")
                .price(99.9F).build();

        Checkout checkout = new Checkout();
        checkout.addToCart(firstProduct);
        checkout.addToCart(secondProduct);
        checkout.pay(119.8F);
        checkout.complete();
    }
}

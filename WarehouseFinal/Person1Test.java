import java.io.*;

public class Person1Test {
    private static int checks;
    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
    }

    private static void rejected(Runnable action) {
        try { action.run(); }
        catch (IllegalArgumentException expected) { checks++; return; }
        throw new AssertionError("Invalid value was accepted");
    }

    public static void main(String[] args) throws Exception {
        Warehouse warehouse = Warehouse.instance();
        Client client = warehouse.addClient("Test client", "Address", "123");
        Product product = warehouse.addProduct("Product", 10, 3.0);
        Order order = new Order(client);
        OrderItem item = new OrderItem(product, 5, product.getSalePrice());
        check(order.addItem(item), "add item");
        check(order.getTotal() == 15.0, "order total");
        check(item.getPrice() == 3.0 && item.getCost() == 15.0, "price snapshot");
        // Simulate a future Product price change without adding a teammate's method.
        java.lang.reflect.Field price = Product.class.getDeclaredField("salePrice");
        price.setAccessible(true);
        price.setDouble(product, 9.0);
        check(item.getPrice() == 3.0 && order.getTotal() == 15.0, "historic price unchanged");
        check(client.addOrder(order), "add order");
        check(!client.addOrder(order), "duplicate order rejected");
        check(client.getOrders().next() == order, "order history");
        Client other = warehouse.addClient("Other", "Address", "456");
        check(!other.addOrder(order), "wrong client rejected");
        client.debit(order.getTotal());
        check(client.getBalance() == 15.0, "charge");
        check(warehouse.receivePayment(client.getId(), 5) == client, "payment return");
        check(client.getBalance() == 10.0, "partial payment");
        warehouse.receivePayment(client.getId(), 10);
        check(client.getBalance() == 0.0, "exact payment");
        warehouse.receivePayment(client.getId(), 100);
        check(client.getBalance() == -100.0, "overpayment credit");
        for (double invalid : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY}) {
            check(warehouse.receivePayment(client.getId(), invalid) == null, "invalid payment rejected");
            check(client.getBalance() == -100.0, "invalid payment unchanged");
        }
        check(warehouse.receivePayment("missing", 1) == null, "unknown client");
        check(warehouse.receivePayment(null, 1) == null, "null client");
        check(client.addToWishlist(product, 4), "wishlist add");
        check(client.searchWishlistItem(product.getId()).getQuantity() == 4, "wishlist search");
        check(client.searchWishlistItem(null) == null, "null search");
        check(client.removeFromWishlist(product.getId()), "wishlist remove");
        check(!client.getWishlist().hasNext(), "wishlist empty");
        check(!client.removeFromWishlist("missing"), "missing remove");
        check(!order.addItem(null), "null line rejected");
        rejected(() -> client.debit(-1));
        rejected(() -> client.applyPayment(Double.NaN));
        rejected(() -> new OrderItem(product, 0, 3));
        rejected(() -> new OrderItem(product, 1, Double.POSITIVE_INFINITY));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new ObjectOutputStream(bytes).writeObject(client);
        Client restored = (Client) new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())).readObject();
        check(restored.getBalance() == -100.0, "serialized balance");
        check(restored.getOrders().next().getTotal() == 15.0, "serialized orders");
        System.out.println("PASS: " + checks + " Person 1 checks");
    }
}

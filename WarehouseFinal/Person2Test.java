import java.util.Iterator;

/**
 * Individual test for Person 2: Product stock/waitlist and the receive-shipment
 * algorithm, run directly against Product and plain Client objects.
 */
public class Person2Test {
    private static int checks;

    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
        System.out.println("PASS  " + message);
    }

    // What Feature 1 (Process Order) will do with each wishlist line:
    // fill from stock, charge only the filled part, waitlist the rest.
    private static void order(Client client, Product product, int requested) {
        int filled = product.removeFromStock(requested);
        if (filled > 0) client.debit(filled * product.getSalePrice());
        if (requested > filled) product.addToWaitlist(client, requested - filled);
    }

    // Same algorithm as receiveShipment_P2.java.txt, minus the inventory lookup.
    // Keep the two in step.
    private static ShipmentResult receiveShipment(Product product, int quantityReceived) {
        if (quantityReceived <= 0 || product == null) return null;
        ShipmentResult result = new ShipmentResult(product);
        int remaining = quantityReceived;
        while (remaining > 0 && product.hasWaitlist()) {
            WaitlistItem item = product.getFirstWaitlistItem();
            Client client = item.getClient();
            int filled = Math.min(remaining, item.getQuantity());

            Order order = new Order(client);
            order.addItem(new OrderItem(product, filled, product.getSalePrice()));
            client.addOrder(order);
            client.debit(order.getTotal());
            result.addOrder(order);

            if (filled == item.getQuantity()) {
                product.removeWaitlistItem(item);
            } else {
                item.setQuantity(item.getQuantity() - filled);
            }
            remaining -= filled;
        }
        product.addToStock(remaining);
        result.setQuantityAddedToStock(remaining);
        return result;
    }

    private static String waitlistString(Product product) {
        StringBuilder text = new StringBuilder();
        for (Iterator iterator = product.getWaitlist(); iterator.hasNext(); ) {
            WaitlistItem item = (WaitlistItem) iterator.next();
            if (text.length() > 0) text.append(", ");
            text.append(item.getClient().getName()).append(" x ").append(item.getQuantity());
        }
        return text.toString();
    }

    public static void main(String[] args) {
        Client c1 = new Client("C1", "Address 1", "111");
        Client c2 = new Client("C2", "Address 2", "222");
        Client c3 = new Client("C3", "Address 3", "333");
        Product p1 = new Product("P1", 10, 1.0);

        System.out.println("--- Required test sequence ---");
        order(c2, p1, 7);
        System.out.println("After C2's order: " + p1 + " | waitlist [" + waitlistString(p1) + "]");
        check(p1.getQuantityInStock() == 3 && !p1.hasWaitlist(), "after C2: stock 3, no waitlist");
        check(c2.getBalance() == 7.0, "C2 charged $7 at order time");

        order(c3, p1, 6);
        System.out.println("After C3's order: " + p1 + " | waitlist [" + waitlistString(p1) + "]");
        check(p1.getQuantityInStock() == 0, "after C3: stock 0");
        check(waitlistString(p1).equals("C3 x 3"), "after C3: waitlist = C3 x 3");
        check(c3.getBalance() == 3.0, "C3 charged only for the 3 filled");

        order(c1, p1, 5);
        System.out.println("After C1's order: " + p1 + " | waitlist [" + waitlistString(p1) + "]");
        check(p1.getQuantityInStock() == 0, "after C1: stock 0, never negative");
        check(waitlistString(p1).equals("C3 x 3, C1 x 5"), "after C1: waitlist C3 x 3 then C1 x 5 (FIFO)");
        check(c1.getBalance() == 0.0, "C1 not charged while waitlisted");

        ShipmentResult result = receiveShipment(p1, 100);
        System.out.println("After shipment of 100: " + p1 + " | waitlist [" + waitlistString(p1) + "]");
        for (Iterator orders = result.getOrders(); orders.hasNext(); ) {
            System.out.println("  created order " + orders.next());
        }
        check(c3.getBalance() == 6.0, "C3 charged $3 at shipment (balance 3 -> 6)");
        check(c1.getBalance() == 5.0, "C1 charged $5 at shipment");
        check(!p1.hasWaitlist(), "waitlist empty");
        check(p1.getQuantityInStock() == 92, "P1 stock 92");
        check(result.getQuantityAddedToStock() == 92, "result reports 92 to stock");
        check(c3.getOrders().hasNext() && c1.getOrders().hasNext(), "orders recorded on clients");

        System.out.println("--- Partial fill ---");
        Product p2 = new Product("P2", 0, 2.0);
        Client a = new Client("A", "Address", "1");
        Client b = new Client("B", "Address", "2");
        order(a, p2, 5);
        order(b, p2, 4);
        result = receiveShipment(p2, 3);
        System.out.println("After shipment of 3: " + p2 + " | waitlist [" + waitlistString(p2) + "]");
        check(waitlistString(p2).equals("A x 2, B x 4"), "partly filled entry stays at front, reduced");
        check(a.getBalance() == 6.0 && b.getBalance() == 0.0, "only A charged for 3 units");
        check(p2.getQuantityInStock() == 0 && result.getQuantityAddedToStock() == 0, "nothing left for stock");
        receiveShipment(p2, 10);
        check(!p2.hasWaitlist() && p2.getQuantityInStock() == 4, "next shipment finishes A then B, 4 to stock");
        check(a.getBalance() == 10.0 && b.getBalance() == 8.0, "A and B charged at fulfillment");

        System.out.println("--- Edge cases ---");
        check(p2.removeFromStock(10) == 4 && p2.getQuantityInStock() == 0, "removeFromStock caps at stock");
        check(p2.removeFromStock(3) == 0, "removeFromStock returns 0 when out of stock");
        check(p2.getFirstWaitlistItem() == null, "peek on empty waitlist returns null");
        check(receiveShipment(p2, 0) == null && receiveShipment(p2, -5) == null, "nonpositive shipment rejected");
        order(a, p2, 1);
        WaitlistItem first = p2.getFirstWaitlistItem();
        check(p2.getFirstWaitlistItem() == first && p2.hasWaitlist(), "peek does not remove");
        check(p2.removeWaitlistItem(first) && !p2.removeWaitlistItem(first), "remove specific entry once");

        System.out.println("All " + checks + " Person 2 checks passed.");
    }
}

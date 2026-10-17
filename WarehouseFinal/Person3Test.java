import java.util.Iterator;

//person3 test java

public class Person3Test {
    private static int checks;

    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
        System.out.println("PASS  " + message);
    }

    public static void main(String[] args) {
        Warehouse warehouse = Warehouse.instance();
        Client c1 = warehouse.addClient("C1", "Address", "111");
        Product p1 = warehouse.addProduct("P1", 10, 2.0);
        Product p2 = warehouse.addProduct("P2", 1, 10.0);
        Product p3 = warehouse.addProduct("P3", 4, 5.0);
        warehouse.addToWishlist(c1.getId(), p1.getId(), 5);
        warehouse.addToWishlist(c1.getId(), p2.getId(), 3);
        warehouse.addToWishlist(c1.getId(), p3.getId(), 2);

        // clerk changes P1 to 4, keeps P2, removes P3
        check(warehouse.changeWishlistQuantity(c1.getId(), p1.getId(), 4), "change P1 to 4");
        check(warehouse.removeFromWishlist(c1.getId(), p3.getId()), "remove P3");
        Order order = warehouse.processOrder(c1.getId());
        System.out.println("Order: " + order);

        check(p1.getQuantityInStock() == 6, "P1 stock 6");
        check(p2.getQuantityInStock() == 0, "P2 stock 0");
        check(p3.getQuantityInStock() == 4, "P3 stock 4, not ordered");
        WaitlistItem waiting = (WaitlistItem) warehouse.getWaitlist(p2.getId()).next();
        check(waiting.getClient() == c1 && waiting.getQuantity() == 2, "P2 waitlist C1 x 2");
        check(order.getTotal() == 18.0 && c1.getBalance() == 18.0, "order total and balance $18");
        check(warehouse.getOrders(c1.getId()).next() == order, "order in C1's orders");
        check(!warehouse.getWishlist(c1.getId()).hasNext(), "C1 wishlist empty");

        Iterator<Client> owing = warehouse.getClientsWithOutstandingBalance();
        check(owing.next() == c1, "C1 has outstanding balance");
        warehouse.receivePayment(c1.getId(), 20);
        check(!warehouse.getClientsWithOutstandingBalance().hasNext(), "credit is not outstanding");

        check(warehouse.processOrder("missing") == null, "unknown client");
        check(warehouse.processOrder(c1.getId()) == null, "empty wishlist");
        check(!warehouse.changeWishlistQuantity(c1.getId(), p1.getId(), 0), "quantity 0 rejected");
        check(!warehouse.removeFromWishlist(c1.getId(), "missing"), "missing item not removed");

        System.out.println("All " + checks + " Person 3 checks passed.");
    }
}

import java.util.Iterator;
import java.util.List;

public class Person4Test {

    public static void main(String[] args) {

        Warehouse warehouse = Warehouse.instance();

        System.out.println("=== Adding Clients ===");
        Client c1 = warehouse.addClient("Connor", "123 Main St", "555-1111");
        Client c2 = warehouse.addClient("Alice", "456 Oak St", "555-2222");
        System.out.println(c1);
        System.out.println(c2);

        System.out.println("\n=== Adding Products ===");
        Product p1 = warehouse.addProduct("Widget", 10, 5.00);
        Product p2 = warehouse.addProduct("Gadget", 3, 12.50);
        System.out.println(p1);
        System.out.println(p2);

        System.out.println("\n=== Adding Wishlist Items for Connor ===");
        warehouse.addToWishlist(c1.getId(), p1.getId(), 6);
        warehouse.addToWishlist(c1.getId(), p2.getId(), 5);

        System.out.println("Connor's Wishlist:");
        Iterator<WishlistItem> w = warehouse.getWishlist(c1.getId());
        while (w.hasNext()) {
            System.out.println(w.next());
        }

        System.out.println("\n=== Processing Connor's Order ===");
        Order order = warehouse.processOrder(c1.getId());
        if (order != null) {
            System.out.println("Order created:");
            Iterator<OrderItem> oi = order.getItems();
            while (oi.hasNext()) {
                System.out.println(oi.next());
            }
            System.out.println("Order Total: $" + order.getTotal());
            System.out.println("Client Balance: $" + c1.getBalance());
        } else {
            System.out.println("Order could not be processed.");
        }

        System.out.println("\n=== Receiving Shipment for Gadget ===");
        ShipmentResult result = warehouse.receiveShipment(p2.getId(), 10);
        if (result != null) {
            System.out.println("Orders filled from waitlist:");
            Iterator<Order> filledOrders = (Iterator<Order>) result.getOrders();
            while (filledOrders.hasNext()) {
                Order o = filledOrders.next();
                System.out.println(o);
                Iterator<OrderItem> items = o.getItems();
                while (items.hasNext()) {
                    System.out.println(items.next());
                }
            }
            System.out.println("Remaining quantity added to stock: " + result.getQuantityAddedToStock());
        } else {
            System.out.println("Shipment failed.");
        }

        System.out.println("\n=== Receiving Payment from Connor ===");
        Client updated = warehouse.receivePayment(c1.getId(), 20.00);
        if (updated != null) {
            System.out.println("Payment applied.");
            System.out.println("New Balance: $" + updated.getBalance());
        } else {
            System.out.println("Payment failed.");
        }

        System.out.println("\n=== Final Client State ===");
        System.out.println(c1);

        System.out.println("\n=== Final Product States ===");
        System.out.println(p1);
        System.out.println(p2);

        System.out.println("\n=== QUERY: View One Product ===");
        Product qp = warehouse.getProduct(p1.getId());
        if (qp != null) {
            System.out.println(qp.toString());
        } else {
            System.out.println("Product not found.");
        }

        System.out.println("\n=== QUERY: View Product Waitlist ===");
        Iterator<WaitlistItem> wli = warehouse.getWaitlist(p2.getId());
        if (wli != null && wli.hasNext()) {
            while (wli.hasNext()) {
                System.out.println(wli.next().toString());
            }
        } else {
            System.out.println("No waitlist for product " + p2.getId());
        }

        System.out.println("\n=== QUERY: View Client Profile ===");
        Client qc = warehouse.getClient(c1.getId());
        if (qc != null) {
            System.out.println(qc.toString());
        } else {
            System.out.println("Client not found.");
        }

        System.out.println("\n=== QUERY: View Client Orders ===");
        Iterator<Order> qOrders = warehouse.getOrders(c1.getId());
        if (qOrders == null || !qOrders.hasNext()) {
            System.out.println("Client has no orders.");
        } else {
            while (qOrders.hasNext()) {
                Order qo = qOrders.next();
                System.out.println(qo.toString());

                Iterator<OrderItem> qoi = qo.getItems();
                while (qoi.hasNext()) {
                    System.out.println("  " + qoi.next().toString());
                }
            }
        }

        System.out.println("\n=== QUERY: List Clients With Outstanding Balance ===");
        Iterator<Client> allClients = warehouse.getClientsWithOutstandingBalance();
        if (allClients == null || !allClients.hasNext()) {
            System.out.println("No clients have outstanding balances.");
        } else {
            while (allClients.hasNext()) {
                System.out.println(allClients.next().toString());
            }
        }
    }
}
/** This is my Warehouse class. This is the system/library. I was assigned this as one
of my classes. My other was ClientList. This one is essentially a gateway to the entire
inside of the code. The user interacts with UserInterface and this directs the rest
of all operations. */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Warehouse {
    private static Warehouse warehouse;
    private ClientList clients = new ClientList();
    private Inventory inventory = Inventory.instance();

    private Warehouse() { }

    public static Warehouse instance() {
        if (warehouse == null) {
            warehouse = new Warehouse();
        }
        return warehouse;
    }

    public Client addClient(String name, String address, String phone) {
        return clients.createClient(name, address, phone);
    }

    public Product addProduct(String name, int quantity, double salePrice) {
        Product p = new Product(name, quantity, salePrice);
        boolean inserted = inventory.insertProduct(p);
        return inserted ? p : null;
    }

    public boolean addToWishlist(String clientID, String productID, int quantity) {
    Client c = clients.getClient(clientID);
    Product p = inventory.searchProduct(productID);
    if (c == null || p == null) return false;
    return c.addToWishlist(p, quantity);
}

    public Iterator<Product> getProducts() {
        return inventory.getProducts();
    }

    public Client receivePayment(String clientID, double amount) {
        if (!Double.isFinite(amount) || amount <= 0) return null;
        Client client = clients.getClient(clientID);
        if (client == null || !Double.isFinite(client.getBalance() - amount)) return null;
        client.applyPayment(amount);
        return client;
    }

    public Iterator<Client> getClients() {
        return clients.getClients();
    }

    public Iterator<WishlistItem> getWishlist(String clientID) {
        Client c = clients.getClient(clientID);
        if (c == null) return null;
        return c.getWishlist();
    }

    // Process Order
    public Order processOrder(String clientID) {
        Client client = clients.getClient(clientID);
        if (client == null) return null;

        List<WishlistItem> items = new ArrayList<WishlistItem>(); // copy the wishlist
        Iterator<WishlistItem> wishlist = client.getWishlist();
        while (wishlist.hasNext()) {
            items.add(wishlist.next());
        }
        if (items.isEmpty()) return null;

        Order order = new Order(client);
        for (WishlistItem item : items) {
            Product product = item.getProduct();
            int requested = item.getQuantity();
            int filled = product.removeFromStock(requested);
            if (filled > 0) {
                order.addItem(new OrderItem(product, filled, product.getSalePrice()));
            }
            if (requested > filled) {
                product.addToWaitlist(client, requested - filled);
            }

            client.removeFromWishlist(product.getId());
        }
        client.addOrder(order);
        client.debit(order.getTotal());
        return order;
    }

    public boolean changeWishlistQuantity(String clientID, String productID, int quantity) {
        if (quantity <= 0) return false;
        Client c = clients.getClient(clientID);
        if (c == null) return false;
        WishlistItem item = c.searchWishlistItem(productID);
        if (item == null) return false;
        item.setQuantity(quantity);
        return true;
    }

    public boolean removeFromWishlist(String clientID, String productID) {
        Client c = clients.getClient(clientID);
        if (c == null) return false;
        return c.removeFromWishlist(productID);
    }


    public ShipmentResult receiveShipment(String productID, int quantityReceived) {
        if (quantityReceived <= 0) return null;
        Product product = inventory.searchProduct(productID);
        if (product == null) return null;

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

    // queries
    public Iterator getWaitlist(String productID) {
        Product p = inventory.searchProduct(productID);
        if (p == null) return null;
        return p.getWaitlist();
    }

    public Iterator<Order> getOrders(String clientID) {
        Client c = clients.getClient(clientID);
        if (c == null) return null;
        return c.getOrders();
    }

    public Product getProduct(String productID) {
        return inventory.searchProduct(productID);
    }

    public Client getClient(String clientID) {
        return clients.getClient(clientID);
    }

    public Iterator<Client> getClientsWithOutstandingBalance() {
        return clients.getClientsWithOutstandingBalance();
    }
}

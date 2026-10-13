/** This is my Warehouse class. This is the system/library. I was assigned this as one
of my classes. My other was ClientList. This one is essentially a gateway to the entire
inside of the code. The user interacts with UserInterface and this directs the rest
of all operations. */

import java.util.Iterator;

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

    public Iterator<Client> getClients() {
        return clients.getClients();
    }

    public Iterator<WishlistItem> getWishlist(String clientID) {
        Client c = clients.getClient(clientID);
        if (c == null) return null;
        return c.getWishlist();
    }
}
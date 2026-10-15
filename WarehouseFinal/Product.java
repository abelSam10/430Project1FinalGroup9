import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents one product sold by the warehouse.
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private String id;
    private String name;
    private int quantityInStock;
    private double salePrice;
    private List waitlist = new LinkedList();

    /**
     * Creates a product and generates its unique ID.
     *
     * @param name product name
     * @param quantityInStock starting quantity in stock
     * @param salePrice price per unit
     */
    public Product(String name, int quantityInStock, double salePrice) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (quantityInStock < 0) {
            throw new IllegalArgumentException("Quantity in stock cannot be negative");
        }
        if (salePrice < 0.0) {
            throw new IllegalArgumentException("Sale price cannot be negative");
        }

        this.id = "P" + nextId++;
        this.name = name.trim();
        this.quantityInStock = quantityInStock;
        this.salePrice = salePrice;
    }

    /**
     * Adds newly received units to this product's stock.
     *
     * @param quantity number of units to add
     * @return true when the stock was updated; false for a nonpositive quantity
     */
    public boolean addToStock(int quantity) {
        if (quantity <= 0) {
            return false;
        }
        quantityInStock += quantity;
        return true;
    }

    /**
     * Takes as many units as possible from stock, never going below zero.
     *
     * @param requested number of units wanted
     * @return number of units actually taken; 0 when out of stock
     */
    public int removeFromStock(int requested) {
        if (requested <= 0) {
            return 0;
        }
        int filled = Math.min(requested, quantityInStock);
        quantityInStock -= filled;
        return filled;
    }

    /**
     * Adds a client to the end of this product's waitlist (FIFO).
     *
     * @param client the waiting client
     * @param quantity the quantity the client is waiting for
     */
    public void addToWaitlist(Client client, int quantity) {
        waitlist.add(new WaitlistItem(client, quantity));
    }

    public Iterator getWaitlist() {
        return waitlist.iterator();
    }

    /**
     * Returns the front waitlist entry without removing it. A partly filled
     * entry must stay at the front, so unlike Book.getNextHold this does not remove.
     *
     * @return the first entry, or null if the waitlist is empty
     */
    public WaitlistItem getFirstWaitlistItem() {
        ListIterator iterator = waitlist.listIterator();
        if (iterator.hasNext()) {
            return (WaitlistItem) iterator.next();
        }
        return null;
    }

    public boolean removeWaitlistItem(WaitlistItem item) {
        for (ListIterator iterator = waitlist.listIterator(); iterator.hasNext(); ) {
            WaitlistItem current = (WaitlistItem) iterator.next();
            if (current == item) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean hasWaitlist() {
        ListIterator iterator = waitlist.listIterator();
        if (iterator.hasNext()) {
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public double getSalePrice() {
        return salePrice;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4s | product: %-15s | quantity: %3d | price: $%.2f",
                id, name, quantityInStock, salePrice);
    }
}

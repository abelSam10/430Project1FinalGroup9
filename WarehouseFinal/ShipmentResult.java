import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Summary of one received shipment: the updated product, the orders created
 * for waitlisted clients, and how many units went into normal stock.
 */
public class ShipmentResult {
    private Product product;
    private List orders = new LinkedList();
    private int quantityAddedToStock;

    public ShipmentResult(Product product) {
        this.product = product;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void setQuantityAddedToStock(int quantity) {
        quantityAddedToStock = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Iterator getOrders() {
        return orders.iterator();
    }

    public int getQuantityAddedToStock() {
        return quantityAddedToStock;
    }
}

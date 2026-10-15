import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Client client;
    private final List<OrderItem> items = new ArrayList<OrderItem>();
    private double total;

    public Order(Client client) {
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        this.client = client;
    }

    public boolean addItem(OrderItem item) {
        if (item == null || !Double.isFinite(total + item.getCost())) return false;
        items.add(item);
        total += item.getCost();
        return true;
    }

    public Iterator<OrderItem> getItems() { return items.iterator(); }
    public double getTotal() { return total; }
    public Client getClient() { return client; }

    public String toString() {
        return String.format("Client: %s | Items: %d | Total: $%.2f",
                client.getId(), items.size(), total);
    }
}

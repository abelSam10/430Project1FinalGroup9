import java.io.Serializable;

/**
 * One client's waiting quantity for a product. Lives inside the Product that
 * owns it, so only the client end of the association is stored.
 */
public class WaitlistItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Client client;
    private int quantity;

    public WaitlistItem(Client client, int quantity) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Waitlist quantity must be positive");
        }
        this.client = client;
        this.quantity = quantity;
    }

    public Client getClient() {
        return client;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the waiting quantity after a partial fill.
     *
     * @param quantity the quantity still waiting; must be positive
     */
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Waitlist quantity must be positive");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Client: %s | Quantity: %d", client.getId(), quantity);
    }
}

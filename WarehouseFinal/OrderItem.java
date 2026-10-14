import java.io.Serializable;

public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Product product;
    private final int quantity;
    private final double price;

    public OrderItem(Product product, int quantity, double price) {
        if (product == null || quantity <= 0 || !Double.isFinite(price) || price < 0
                || !Double.isFinite(quantity * price)) {
            throw new IllegalArgumentException("Invalid order item");
        }
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getCost() { return quantity * price; }

    public String toString() {
        return String.format("Product: %s | Quantity: %d | Unit price: $%.2f | Cost: $%.2f",
                product.getId(), quantity, price, getCost());
    }
}

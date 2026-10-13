import java.io.Serializable;

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

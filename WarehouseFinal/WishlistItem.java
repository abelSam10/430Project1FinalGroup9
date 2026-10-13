import java.io.Serializable;


public class WishlistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product product;
    private int quantity;

    // create a new wishlist item
    public WishlistItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product is null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be larger than 0");
        }

        this.product = product;
        this.quantity = quantity;
    }

    // replace current quantity
    public void setQuantity(int quantity) {
        if (quantity >= 0) { // make sure new quntity is not negative
            this.quantity = quantity;
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String toString() {
    return String.format("Product ID: %s | Name: %s | Quantity: %d",
            product.getId(), product.getName(), quantity);
    }
}

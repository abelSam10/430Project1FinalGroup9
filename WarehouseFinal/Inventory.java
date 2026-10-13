import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** Stores and searches all Product objects in the warehouse. */
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Product> products = new LinkedList<Product>();
    private static Inventory inventory;

    private Inventory() { }

    public static Inventory instance() {
        if (inventory == null) inventory = new Inventory();
        return inventory;
    }

    public boolean insertProduct(Product product) {
        if (product == null || searchProduct(product.getId()) != null) return false;
        return products.add(product);
    }

    public Product searchProduct(String productId) {
        if (productId == null) return null;
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId.trim())) return product;
        }
        return null;
    }

    public Product searchProduct(String name, double salePrice) {
        if (name == null) return null;
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name.trim())
                    && Double.compare(product.getSalePrice(), salePrice) == 0) return product;
        }
        return null;
    }

    public Iterator<Product> getProducts() { return products.iterator(); }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(inventory);
        } catch (IOException ioe) { System.out.println(ioe); }
    }

    private void readObject(java.io.ObjectInputStream input) {
        try {
            if (inventory != null) return;
            input.defaultReadObject();
            if (inventory == null) inventory = (Inventory) input.readObject();
            else input.readObject();
        } catch (IOException ioe) {
            System.out.println("in Inventory readObject\n" + ioe);
        } catch (ClassNotFoundException cnfe) { cnfe.printStackTrace(); }
    }

    @Override
    public String toString() { return products.toString(); }
}

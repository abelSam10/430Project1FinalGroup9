import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private String id;
    private String name;
    private String address;
    private String phone;
    private double balance;
    private List<WishlistItem> wishlist = new LinkedList<WishlistItem>();

    public Client(String name, String address, String phone) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Client must have a name");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Client must have a address");
        }
        if (phone ==null || phone.isEmpty()) {
            throw new IllegalArgumentException("Client must have a phone number");
        }

        this.id = "C" + nextId++;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.balance = 0.00;
    }

    public boolean addToWishlist(Product product, int quantity) {
        if (product == null || quantity <= 0) { //check if product exists and if quantity is negative
            return false; 
        }
        for (WishlistItem item : wishlist) {
            if (item.getProduct().getId().equals(product.getId())) { // check if the item already exists in the wishlist
                item.setQuantity(quantity);                          // if so just update the quantity 
                return true;
            }
        }
        return wishlist.add(new WishlistItem(product, quantity));
    }

    public Iterator<WishlistItem> getWishlist() {
        return wishlist.iterator();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public  String getPhone() {
        return phone;
    }

    public double getBalance() {
        return balance;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    public void setAddress(String address) {
        if (address != null && !address.isEmpty()) {
            this.address = address;
        }
    }

    public void setPhone(String phone) {
        if (phone != null && !phone.isEmpty()) {
            this.phone = phone;
        }
    }

    public String toString() {
    return String.format("ID: %s | Name: %s | Address: %s | Phone: %s | Balance: $%.2f",
            id, name, address, phone, balance);
    }
}

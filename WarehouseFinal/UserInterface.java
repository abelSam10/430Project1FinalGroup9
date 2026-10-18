import java.util.*;
// import java.text.*;
import java.io.*;
public class UserInterface {
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCTS = 2;
  private static final int ADD_TO_WISHLIST = 3;
  private static final int DISPLAY_CLIENTS = 4;
  private static final int DISPLAY_PRODUCTS = 5;
  private static final int DISPLAY_WISHLIST = 6;
  private static final int HELP = 7;
  // 3 aditional functions for final code.
  private static final int PROCESS_ORDER = 8;
  private static final int RECEIVE_SHIPMENT = 9;
  private static final int RECEIVE_PAYMENT = 10;
  // 5 New Queries:
private static final int VIEW_PRODUCT = 11;
private static final int VIEW_PRODUCT_WAITLIST = 12;
private static final int VIEW_CLIENT_PROFILE = 13;
private static final int VIEW_CLIENT_ORDERS = 14;
private static final int LIST_CLIENTS_WITH_BALANCE = 15;


  private UserInterface() {
     warehouse = Warehouse.instance();
    
  }
  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
    }
  }
  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        if (line == null) {
          System.exit(0);
        }
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  public double getDouble(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        return Double.parseDouble(item);
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  // public Calendar getDate(String prompt) {
  //   do {
  //     try {
  //       Calendar date = new GregorianCalendar();
  //       String item = getToken(prompt);
  //       DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
  //       date.setTime(df.parse(item));
  //       return date;
  //     } catch (Exception fe) {
  //       System.out.println("Please input a date as mm/dd/yy");
  //     }
  //   } while (true);
  // }
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:"));
        if (value >= EXIT && value <= LIST_CLIENTS_WITH_BALANCE) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 15 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCTS + " to  add products");
    System.out.println(ADD_TO_WISHLIST + " to  add to client's wishlist");
    System.out.println(DISPLAY_CLIENTS + " to  display clients ");
    System.out.println(DISPLAY_PRODUCTS + " to  display products ");
    System.out.println(DISPLAY_WISHLIST + " to  display wishlist");
    System.out.println(HELP + " for help");
    // Adding the 3 new functions to UI.
    System.out.println(PROCESS_ORDER + " to process a client's order");
    System.out.println(RECEIVE_SHIPMENT + " to receive a shipment");
    System.out.println(RECEIVE_PAYMENT + " to receive a payment");
    // The 5 new Queries
    System.out.println(VIEW_PRODUCT + " to view a single product");
    System.out.println(VIEW_PRODUCT_WAITLIST + " to view a product's waitlist");
    System.out.println(VIEW_CLIENT_PROFILE + " to view a client's profile");
    System.out.println(VIEW_CLIENT_ORDERS + " to view a client's orders");
    System.out.println(LIST_CLIENTS_WITH_BALANCE + " to list clients with outstanding balances");
  }

  public void addClient() {
    String name = getToken("Enter Client name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
    Client result;
    result = warehouse.addClient(name, address, phone);
    if (result == null) {
      System.out.println("Could not add client");
    } else {
      System.out.println(result);
    }
  }

  public void addProducts() {
    Product result;
    do {
      String name = getToken("Enter name of product");
      int quantity = getNumber("Enter quantity");
      double salePrice = getDouble("Enter SalePrice");
      result = warehouse.addProduct(name, quantity, salePrice);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added");
      }
      if (!yesOrNo("Add more products?")) {
        break;
      }
    } while (true);
  }

  public void addToWishlist() {
    String ClientID = getToken("Enter Client ID");
    String ProductID = getToken("Enter Product ID");
    int productQuantity = getNumber("Enter product quantity");
    boolean result = warehouse.addToWishlist(ClientID, ProductID, productQuantity);
    if (result) {
      System.out.println("Product added to wishlist");
    } else {
      System.out.println("Could not add product to wishlist. Check if you entered the right Client ID and Product ID");
    }
  }

  public void displayProducts() {
      Iterator<Product> allProducts = warehouse.getProducts();
      while (allProducts.hasNext()){
	  Product product = allProducts.next();
          System.out.println(product.toString());
      }
  }

  public void displayClients() {
      Iterator<Client> allClients = warehouse.getClients();
      while (allClients.hasNext()){
	  Client client = allClients.next();
          System.out.println(client.toString());
      }
  }

  public void displayWishlist() {
      String ClientID = getToken("Enter Client ID");
      Iterator<WishlistItem> wishlist = warehouse.getWishlist(ClientID);
      if (wishlist == null) {
        System.out.println("No such client");
        return;
      }
      System.out.println("Wishlist for " + ClientID);
      while (wishlist.hasNext()){
          WishlistItem item = wishlist.next();
          System.out.println(item.toString());
      }
  }
  
  // private void save() {
  //   if (warehouse.save()) {
  //     System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
  //   } else {
  //     System.out.println(" There has been an error in saving \n" );
  //   }
  // }
  // private void retrieve() {
  //   try {
  //     Warehouse tempWarehouse = Warehouse.retrieve();
  //     if (tempWarehouse != null) {
  //       System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
  //       warehouse = tempWarehouse;
  //     } else {
  //       System.out.println("File doesnt exist; creating new warehouse" );
  //       warehouse = Warehouse.instance();
  //     }
  //   } catch(Exception cnfe) {
  //     cnfe.printStackTrace();
  //   }
  // }

  // Process Client Order function
public void processClientOrder() {
    String clientID = getToken("Enter Client ID");
    Client client = warehouse.getClient(clientID);
    if (client == null) {
        System.out.println("No such client");
        return;
    }

    Iterator<WishlistItem> it = client.getWishlist();
    List<WishlistItem> items = new ArrayList<>();
    while (it.hasNext()) items.add(it.next());

    if (items.isEmpty()) {
        System.out.println("Wishlist is empty");
        return;
    }

    for (WishlistItem item : items) {
        System.out.println(item.toString());
        String choice = getToken("Keep (K), Change quantity (C), or Remove (R)");
        if (choice.equalsIgnoreCase("C")) {
            int q = getNumber("Enter new quantity");
            warehouse.changeWishlistQuantity(clientID, item.getProduct().getId(), q);
        } else if (choice.equalsIgnoreCase("R")) {
            warehouse.removeFromWishlist(clientID, item.getProduct().getId());
        }
    }

    Order order = warehouse.processOrder(clientID);
    if (order == null) {
        System.out.println("Order could not be processed");
        return;
    }

    System.out.println("Order Summary:");
    Iterator<OrderItem> oi = order.getItems();
    while (oi.hasNext()) System.out.println(oi.next().toString());
    System.out.println("Order Total: $" + order.getTotal());
    System.out.println("Updated Balance: $" + client.getBalance());
  }

  // Recieve Shipment Function
  public void receiveShipment() {
    String productID = getToken("Enter Product ID");
    int quantity = getNumber("Enter quantity received");

    ShipmentResult result = warehouse.receiveShipment(productID, quantity);
    if (result == null) {
        System.out.println("Invalid product or quantity");
        return;
    }

    System.out.println("Shipment processed");

    Iterator<Order> orders = (Iterator<Order>) result.getOrders();

    while (orders.hasNext()) {
        Order o = orders.next();
        System.out.println(o.toString());
        Iterator<OrderItem> items = o.getItems();
        while (items.hasNext()) System.out.println(items.next().toString());
    }

    System.out.println("Remaining quantity added to stock: " + result.getQuantityAddedToStock());
}

  // Recieve Payment Function
  public void receivePayment() {
    String clientID = getToken("Enter Client ID");
    double amount = getDouble("Enter payment amount");

    Client client = warehouse.receivePayment(clientID, amount);
    if (client == null) {
        System.out.println("Invalid client or amount");
        return;
    }

    System.out.println("Payment applied");
    System.out.println("Updated Balance: $" + client.getBalance());
  }

  // New View Product Query Function
  public void viewProduct() {
    String productID = getToken("Enter Product ID");
    Product product = warehouse.getProduct(productID);

    if (product == null) {
        System.out.println("No such product.");
        return;
    }

    System.out.println(product.toString());
  }

  // New View Product Waitlist Query
  public void viewProductWaitlist() {
    String productID = getToken("Enter Product ID");
    Iterator<WaitlistItem> waitlist = warehouse.getWaitlist(productID);

    if (waitlist == null) {
        System.out.println("No such product.");
        return;
    }

    if (!waitlist.hasNext()) {
        System.out.println("No waitlist for this product.");
        return;
    }

    System.out.println("Waitlist for product " + productID + ":");
    while (waitlist.hasNext()) {
        System.out.println(waitlist.next().toString());
    }
  }

  // New View Client Profile Query
  public void viewClientProfile() {
    String clientID = getToken("Enter Client ID");
    Client client = warehouse.getClient(clientID);

    if (client == null) {
        System.out.println("No such client.");
        return;
    }

    System.out.println(client.toString());
}

  // New View Client Orders Query
  public void viewClientOrders() {
    String clientID = getToken("Enter Client ID");
    Iterator<Order> orders = warehouse.getOrders(clientID);

    if (orders == null) {
        System.out.println("No such client.");
        return;
    }

    if (!orders.hasNext()) {
        System.out.println("Client has no orders.");
        return;
    }

    System.out.println("Orders for client " + clientID + ":");
    while (orders.hasNext()) {
        Order order = orders.next();
        System.out.println(order.toString());

        Iterator<OrderItem> items = order.getItems();
        while (items.hasNext()) {
            System.out.println("  " + items.next().toString());
        }
    }
  }

  // New Outstanding balance Query
  public void listClientsWithBalance() {
    Iterator<Client> clients = warehouse.getClientsWithOutstandingBalance();

    if (clients == null || !clients.hasNext()) {
        System.out.println("No clients have outstanding balances.");
        return;
    }

    System.out.println("Clients with outstanding balances:");
    while (clients.hasNext()) {
        System.out.println(clients.next().toString());
    }
}

  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:        addClient();
                                break;
        case ADD_PRODUCTS:         addProducts();
                                break;
        case ADD_TO_WISHLIST:       addToWishlist();
                                break;
        case DISPLAY_CLIENTS:      displayClients();
                                break;
        case DISPLAY_PRODUCTS:      displayProducts();
                                break;
        case DISPLAY_WISHLIST:       displayWishlist();
                                break;
        case HELP:              help();
                                break;
        case PROCESS_ORDER:     processClientOrder();
                                break;
        case RECEIVE_SHIPMENT:  receiveShipment();
                                break;
        case RECEIVE_PAYMENT:   receivePayment();
                                break;
        case VIEW_PRODUCT:
                                viewProduct();
                                break;

        case VIEW_PRODUCT_WAITLIST:
                                viewProductWaitlist();
                                break;

        case VIEW_CLIENT_PROFILE:
                                viewClientProfile();
                                break;

        case VIEW_CLIENT_ORDERS:
                                viewClientOrders();
                                break;

        case LIST_CLIENTS_WITH_BALANCE:
                                listClientsWithBalance();
                                break;
      }
    }
  }

  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}
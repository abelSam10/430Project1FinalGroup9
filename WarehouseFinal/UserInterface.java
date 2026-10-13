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
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 7 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCTS + " to  add products");
    System.out.println(ADD_TO_WISHLIST + " to  add to client's wishlist");
    System.out.println(DISPLAY_CLIENTS + " to  display clients ");
    System.out.println(DISPLAY_PRODUCTS + " to  display products ");
    System.out.println(DISPLAY_WISHLIST + " to  display wishlist");
    System.out.println(HELP + " for help");
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
      }
    }
  }
  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}

# Person 1 handoff

Implemented Order.java, OrderItem.java, Client additions, and Warehouse.receivePayment.
Positive balance means money owed; negative balance means credit. A payment subtracts
from the balance and may take it below zero. Invalid payments return null without updates.

Person 3 owns Warehouse.java. Merge only receivePayment into their final version,
preserving their processOrder and query methods. Persons 2 and 3 can construct
new Order(client), add new OrderItem(product, filledQuantity, product.getSalePrice()),
then call client.addOrder(order) and client.debit(order.getTotal()). Order itself
does not change the client's balance, so callers charge exactly once.

OrderItem stores the sale price when it is constructed. Later product price changes
do not alter old lines or totals. addItem returns false for a null line or overflow.
Finish building an order before storing and charging it. getItems returns an Iterator.

Client methods: addOrder(Order), getOrders(), debit(double), applyPayment(double),
searchWishlistItem(String productID), removeFromWishlist(String productID).
Removing a wishlist entry invalidates an existing traversal; callers must collect
items first or perform removal after their traversal, as agreed in the group design.

The final handout calls fulfilled records invoices. The shared design calls them
Orders. The UI owner must display these stored Order and OrderItem records through
the required invoice menu, or the group must agree on a mapping before integration.

## Run the focused checks

From WarehouseFinal: javac *.java then java Person1Test.
These are component tests, not the professor's complete menu demonstration.
After integrating all four contributions, run every handout operation through
UserInterface in the specified order and capture the required script session.

## What Irfan still needs to do

1. Review the PR with the group, especially the receivePayment merge with Person 3.
2. Explain the payment and order classes in the recorded demonstration.
3. Collect Person 2's test table containing real actual outputs, all four members'
   contribution percentages, the private repository URL, and Person 3's video link.
4. Assemble and submit the group report in D2L using the usual black Times New Roman
   format and all four names. Do not claim the full tests passed before they run.

The report cannot be finalized until the integrated testing and recording are complete.

package final_pharmacy.pharmacy.sales;
import java.sql.*;
import java.util.*;

public class SalesProcessor {
    Scanner sc=new Scanner(System.in);
    Connection conn;
    CircularQueue salesQueue = new CircularQueue(20); // Last 20 sales

    class SaleItem {
        int productId;
        int quantity;
        double unitPrice;

        SaleItem(int productId, int quantity, double unitPrice) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
    }

    public SalesProcessor(Connection conn) throws Exception{
        this.conn = conn;
    }
    public void processSales() throws Exception {
        boolean b3 = true;

        while (b3) {
            System.out.println("\n--- Sales Processing ---");
            System.out.println("1. New Sale");
            System.out.println("2. View Sales History");
            System.out.println("3. View Recent Sales Queue");
            System.out.println("4. Dequeue Oldest Sale (Queue)");
            System.out.println("5. Peek Oldest Sale (Queue)");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    processNewSale();
                    break;
                case 2:
                    viewSalesHistory(sc);
                    break;
                case 3:
                    salesQueue.display();
                    break;
                case 4:
                    try {
                        String removed = (String) salesQueue.dequeue();
                        System.out.println("Removed the oldest sale from the queue: " + removed);
                    } catch (IllegalStateException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 5:
                    try {
                        String next = (String) salesQueue.peek();
                        System.out.println("Oldest sale waiting in the queue: " + next);
                    } catch (IllegalStateException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 6:
                    b3 = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    void processNewSale()  throws Exception{
        System.out.println("\nProcess New Sale");

        // Customer lookup or new customer
        System.out.print("Enter customer ID (or 0 for walk-in customer): ");
        int customerId = sc.nextInt();
        sc.nextLine();
        if (customerId < 0) {
            System.out.println("Invalid customer ID. Treating as walk-in customer.");
            customerId = 0;
        }
        if (customerId > 0) {
            // Verify customer exists
            String sql = "SELECT name FROM Customers WHERE customer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Customer not found. Creating as walk-in customer.");
                customerId = 0;
            } else {
                System.out.println("Customer: " + rs.getString("name"));
            }

        }

        // Collect sale items using simple SaleItem class instead of Map
        ArrayList<SaleItem> saleItems = new ArrayList<>();
        boolean addItems = true;
        double totalAmount = 0.0;

        // Simple tracking for current product being processed
        int currentProductId = 0;
        int currentProductReserved = 0;

        while (addItems) {
            System.out.print("\nEnter product ID (or 0 to finish): ");
            int productId = sc.nextInt();
            sc.nextLine();

            if (productId == 0) {
                addItems = false;
                continue;
            }

            // Reset tracking if it's a different product
            if (productId != currentProductId) {
                currentProductId = productId;
                currentProductReserved = 0;
            }

            // Get product details
            String productSql = "SELECT product_id, name, price, quantity_in_stock FROM Products WHERE product_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(productSql);
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Product not found with ID: " + productId);
                continue;
            }

            String productName = rs.getString("name");
            double price = rs.getDouble("price");
            int stock = rs.getInt("quantity_in_stock");

            // Calculate available stock for current product
            int availableStock = stock - currentProductReserved;

                System.out.println("Product: " + productName);
                System.out.println("Price: ₹" + price);
                System.out.println("Current Stock: " + stock);
                System.out.println("Already Reserved: " + currentProductReserved);
                System.out.println("Available Stock: " + availableStock);

            System.out.print("Enter quantity: ");
            int quantity;
            try {
                quantity = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number only.");
                sc.nextLine(); // Clear the invalid input
                continue;
            }

            if (quantity < 0) {
                System.out.println("Quantity must be positive.");
                continue;
            }

            if (quantity > availableStock) {
                System.out.println("Not enough stock. Available: " + availableStock);
                continue;
            }

            // Validate price is positive
            if (price <= 0) {
                System.out.println("Price must be greater than zero.");
                continue;
            }

            // Calculate item total
            double itemTotal = quantity * price;
            totalAmount += itemTotal;

                // Add to sale items using simple SaleItem class
                SaleItem item = new SaleItem(productId, quantity, price);
                saleItems.add(item);
            // Update reserved stock for current product
            currentProductReserved += quantity;

            System.out.println("Added: " + productName + " = ₹" + itemTotal);

        }


        if (saleItems.isEmpty()) {
            System.out.println("No items added. Sale cancelled.");
            return;
        }

        // Payment method
        System.out.println("\nTotal Amount: ₹" + totalAmount);
        String paymentMethod;
        while (true) {
            System.out.print("Enter payment method (Cash/Card): ");
            paymentMethod = sc.nextLine().trim();

            if (paymentMethod.equalsIgnoreCase("Cash") || paymentMethod.equalsIgnoreCase("Card")) {
                break;
            } else {
                System.out.println("Invalid payment method. Please enter 'Cash' or 'Card'.");
            }
        }

        paymentMethod = paymentMethod.equalsIgnoreCase("Card") ? "Card" : "Cash";

// Handle card details if card is selected
        String cardNumber = null;
        String cardType = null;

        if (paymentMethod.equals("Card")) {
            // Card Type: Credit or Debit
            while (true) {
                System.out.print("Enter Card Type (Credit/Debit): ");
                cardType = sc.nextLine().trim();

                if (cardType.equalsIgnoreCase("Credit") || cardType.equalsIgnoreCase("Debit")) {
                    cardType = cardType.substring(0, 1).toUpperCase() + cardType.substring(1).toLowerCase(); // Format
                    break;
                } else {
                    System.out.println("Invalid card type. Please enter 'Credit' or 'Debit'.");
                }
            }

            // 12-digit card number
            while (true) {
                System.out.print("Enter 12-digit Card Number: ");
                cardNumber = sc.nextLine().trim();

                if (cardNumber.matches("\\d{12}")) {
                    break;
                } else {
                    System.out.println("Invalid card number. Please enter exactly 12 digits.");
                }
            }

            // (Optional) Collect more details like expiry or holder name if needed
        }

        // Process sale in a transaction
        try {

            conn.setAutoCommit(false); // Start transaction

            // 1. Create sale record
            String saleSql = "INSERT INTO Sales (customer_id, sale_date, total_amount, payment_method) " +
                    "VALUES (?, CURRENT_DATE(), ?, ?)";

            PreparedStatement saleStmt = conn.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS);
            if (customerId > 0) {
                saleStmt.setInt(1, customerId);
            } else {
                saleStmt.setNull(1, Types.INTEGER);
            }
            saleStmt.setDouble(2, totalAmount);
            saleStmt.setString(3, paymentMethod);

            int saleRows = saleStmt.executeUpdate();
            if (saleRows == 0) {
                throw new SQLException("Failed to create sale record.");
            }

            // Get generated sale ID
            int saleId;
            try (ResultSet generatedKeys = saleStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get sale ID.");
                }
            }

            // 2. Add sale items using simple SaleItem class
            String itemSql = "INSERT INTO SaleItems (sale_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(itemSql);

            String updateStockSql = "UPDATE Products SET quantity_in_stock = quantity_in_stock - ? WHERE product_id = ?";
            PreparedStatement stockStmt = conn.prepareStatement(updateStockSql);

            for (SaleItem item : saleItems) {
                // Add sale item
                itemStmt.setInt(1, saleId);
                itemStmt.setInt(2, item.productId);
                itemStmt.setInt(3, item.quantity);
                itemStmt.setDouble(4, item.unitPrice);
                itemStmt.executeUpdate();

                // Update stock
                stockStmt.setInt(1, item.quantity);
                stockStmt.setInt(2, item.productId);
                stockStmt.executeUpdate();
            }

            conn.commit();
            System.out.println(" Sale completed successfully. Sale ID: " + saleId);

            //  Add to circular queue
            String masked = (cardNumber != null && cardNumber.length() >= 4)
                    ? (" ****" + cardNumber.substring(cardNumber.length() - 4))
                    : "";
            String saleSummary = "Sale ID: " + saleId +
                    ", Amount: ₹" + totalAmount +
                    ", Payment: " + paymentMethod +
                    (cardType != null ? (" (" + cardType + masked + ")") : "");

            salesQueue.enqueue(saleSummary);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Rollback failed: " + ex.getMessage());
                }
            }
            System.out.println("Error processing sale. Transaction rolled back. " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Failed to reset autocommit: " + e.getMessage());
                }
            }
        }
    }
    void viewSalesHistory(Scanner sc) throws SQLException {
        System.out.println("\nSales History");
        java.sql.Date startDate = null, endDate = null;

        // Get and validate start date
        while (true) {
            System.out.print("Enter start date (yyyy-mm-dd) or leave empty: ");
            String startDateStr = sc.nextLine();
            if (startDateStr.isEmpty()) break;
            try {
                startDate = java.sql.Date.valueOf(startDateStr);
                break; // Valid input
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid start date format. Please use yyyy-mm-dd.");
            }
        }

        // Get and validate end date
        while (true) {
            System.out.print("Enter end date (yyyy-mm-dd) or leave empty: ");
            String endDateStr = sc.nextLine();
            if (endDateStr.isEmpty()) break;
            try {
                endDate = java.sql.Date.valueOf(endDateStr);
                break; // Valid input
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid end date format. Please use yyyy-mm-dd.");
            }
        }

        String sql = "SELECT s.sale_id, s.sale_date, COALESCE(c.name, 'Walk-in') AS customer_name, " +
                "s.total_amount, s.payment_method FROM Sales s " +
                "LEFT JOIN Customers c ON s.customer_id = c.customer_id";

        ArrayList<Object> params = new ArrayList<>();

        if (startDate != null || endDate != null) {
            sql += " WHERE ";
            boolean hasCondition = false;

            if (startDate != null) {
                sql += "s.sale_date >= ?";
                params.add(startDate);
                hasCondition = true;
            }

            if (endDate != null) {
                if (hasCondition) sql += " AND ";
                sql += "s.sale_date <= ?";
                params.add(endDate);
            }
        }

        sql += " ORDER BY s.sale_date DESC"; // latest first

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set query parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean any = false;
                System.out.printf("%-10s %-15s %-30s %-15s %-15s\n",
                        "Sale ID", "Date", "Customer", "Total", "Payment");
                System.out.println("-----------------------------------------------------------");

                while (rs.next()) {
                    any = true;
                    System.out.printf("%-10d %-15s %-30s Rs%-14.2f %-15s\n",
                            rs.getInt("sale_id"),
                            rs.getDate("sale_date"),
                            rs.getString("customer_name"),
                            rs.getDouble("total_amount"),
                            rs.getString("payment_method"));
                }

                if (!any) {
                    System.out.println("No sales found for the given date range.");
                }
            }
        }
    }
}


class Stack{
    Node top;

    class Node {
        Object data;
        Node next;

        Node(Object data) {
            this.data = data;
        }
    }

    public void push(Object item) {
        Node node = new Node(item);
        node.next = top;
        top = node;
    }

    public Object pop() {
        if (top == null) return null;
        Object item = top.data;
        top = top.next;
        return item;
    }

    public boolean isEmpty() {
        return top == null;
    }
}
class CircularQueue {
    Object[] queue;
    int front, rear, size, capacity;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(Object data) {
        if (isFull()) {
            front = (front + 1) % capacity; // overwrite oldest
        } else {
            size++;
        }

        rear = (rear + 1) % capacity;
        queue[rear] = data;
    }

    public Object dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        Object data = queue[front];
        queue[front] = null;
        front = (front + 1) % capacity;
        size--;
        return data;
    }

    public Object peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return queue[front];
    }

    public void display() {
        System.out.println("\n Recent Sales Queue:");
        for (int i = 0; i < size; i++) {
            int index = (front + i) % capacity;
            System.out.println("  - " + queue[index]);
        }
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
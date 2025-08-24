package final_pharmacy.pharmacy.report;
import java.sql.*;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ReportGenerator {
    Scanner sc=new Scanner(System.in);
    Connection con;

    public ReportGenerator(Connection con) {
        this.con = con;
    }

    public void generateReports() throws Exception {
        boolean b1 = true;

        while (b1) {
            System.out.println("\n----------- Reports -------------");
            System.out.println("1. View Trigger Logs");
            System.out.println("2. Sales Summary");
            System.out.println("3. Product Sales Analysis");
            System.out.println("4. Low Stock Report");
            System.out.println("5.validate phone number");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewTriggerLogs();
                    break;
                case 2:
                    salesSummaryReport();
                    break;
                case 3:
                    productSalesAnalysis();
                    break;
                case 4:
                    lowStockReport();
                    break;
                case 5:
                    validatePhoneNumber(sc);
                    break;
                case 6:
                    b1= false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
/*
    ITS USE TO SEE THE TRIGGERLOG TABLE ALL INFORMATION ABOUT CUSTOMER AND PRODUCT BY USING STACK
*/
    void viewTriggerLogs() throws Exception {
        String sql = "SELECT * FROM TriggerLog ORDER BY log_time DESC";

        Stack s = new Stack();

        try (
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // Push all logs into stack
            while (rs.next()) {
                TriggerLog log = new TriggerLog(
                        rs.getInt("log_id"),
                        rs.getString("table_name"),
                        rs.getString("operation"),
                        rs.getInt("record_id"),
                        rs.getString("details"),
                        rs.getTimestamp("log_time")
                );
                s.push(log);
            }
        }

        System.out.println("\nTrigger Logs (oldest first):");
        System.out.printf("%-5s %-12s %-10s %-9s %-50s %-20s\n",
                "ID", "Table", "Action", "RecordID", "Details", "Time");

        // Pop and print logs (oldest first)
        while (!s.isEmpty()) {
            TriggerLog log = (TriggerLog) s.pop();
            System.out.printf("%-5d %-12s %-10s %-9d %-50s %-20s\n",
                    log.getLogId(),
                    log.getTableName(),
                    log.getOperation(),
                    log.getRecordId(),
                    truncate(log.getDetails(), 50),
                    log.getLogTime());
        }
    }

    static String truncate(String input, int length) {
        if (input == null) return "";
        return input.length() > length ? input.substring(0, length - 3) + "..." : input;
    }
    /*
    ITS USE TO SEE THE SALES SUMMARY,PAYMENTMETHODS,DAILYSALES
    */
    void salesSummaryReport() throws Exception {
        System.out.println("\nSales Summary Report");
        Date startDate = null, endDate = null;


        while (true) {
            System.out.print("Enter start date (yyyy-mm-dd) or leave empty: ");
            String startDateStr = sc.nextLine();
            if (startDateStr.isEmpty()) break;
            try {
                startDate = Date.valueOf(startDateStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Invalid start date format. Please try again.");
            }
        }

        while (true) {
            System.out.print("Enter end date (yyyy-mm-dd) or leave empty: ");
            String endDateStr = sc.nextLine();
            if (endDateStr.isEmpty()) break;
            try {
                endDate = Date.valueOf(endDateStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Invalid end date format. Please try again.");
            }
        }

        //SALES SUMMARY
        String sql = "SELECT COUNT(*) as total_sales, SUM(total_amount) as total_revenue, AVG(total_amount) as avg_sale FROM Sales";
        boolean hasStart = startDate != null;
        boolean hasEnd = endDate != null;

        if (hasStart || hasEnd) {
            sql += " WHERE ";
            if (hasStart) sql += "sale_date >= ?";
            if (hasStart && hasEnd) sql += " AND ";
            if (hasEnd) sql += "sale_date <= ?";
        }

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            if (hasStart) pstmt.setDate(1, startDate);
            if (hasEnd) pstmt.setDate(hasStart ? 2 : 1, endDate);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n-- Sales Summary --");
                System.out.println("Total Sales: " + rs.getInt("total_sales"));
                System.out.printf("Total Revenue: $%.2f\n", rs.getDouble("total_revenue"));
                System.out.printf("Average Sale Amount: $%.2f\n", rs.getDouble("avg_sale"));

                //PAYMENT METHOD GROUP BY PAYMENT METHOD
                String paymentSql = "SELECT payment_method, COUNT(*) as count, SUM(total_amount) as amount FROM Sales";
                if (hasStart || hasEnd) {
                    paymentSql += " WHERE ";
                    if (hasStart) paymentSql += "sale_date >= ?";
                    if (hasStart && hasEnd) paymentSql += " AND ";
                    if (hasEnd) paymentSql += "sale_date <= ?";
                }
                paymentSql += " GROUP BY payment_method";

                try (PreparedStatement paymentStmt = con.prepareStatement(paymentSql)) {
                    if (hasStart) paymentStmt.setDate(1, startDate);
                    if (hasEnd) paymentStmt.setDate(hasStart ? 2 : 1, endDate);

                    ResultSet paymentRs = paymentStmt.executeQuery();

                    System.out.println("\n-- Payment Methods --");
                    while (paymentRs.next()) {
                        System.out.printf("%-10s: %d sales, $%.2f\n",
                                paymentRs.getString("payment_method"),
                                paymentRs.getInt("count"),
                                paymentRs.getDouble("amount"));
                    }
                }

                //DAILY SALES ORDER BY SALEDATE
                String dailySql = "SELECT sale_date, COUNT(*) as count, SUM(total_amount) as amount FROM Sales";
                if (hasStart || hasEnd) {
                    dailySql += " WHERE ";
                    if (hasStart) dailySql += "sale_date >= ?";
                    if (hasStart && hasEnd) dailySql += " AND ";
                    if (hasEnd) dailySql += "sale_date <= ?";
                }
                dailySql += " GROUP BY sale_date ORDER BY sale_date";

                try (PreparedStatement dailyStmt = con.prepareStatement(dailySql)) {
                    if (hasStart) dailyStmt.setDate(1, startDate);
                    if (hasEnd) dailyStmt.setDate(hasStart ? 2 : 1, endDate);

                    ResultSet dailyRs = dailyStmt.executeQuery();

                    System.out.println("\n-- Daily Sales --");
                    System.out.printf("%-15s %-10s %-15s\n", "Date", "Sales", "Revenue");

                    while (dailyRs.next()) {
                        System.out.printf("%-15s %-10d $%-14.2f\n",
                                dailyRs.getDate("sale_date"),
                                dailyRs.getInt("count"),
                                dailyRs.getDouble("amount"));
                    }
                }
            } else {
                System.out.println("No sales data found for the given period.");
            }
        }
    }

    /*
    ITS USE TO TOP SELLING PRODUCTS BY REVENUE
    */
    void productSalesAnalysis() throws SQLException {
        System.out.println("\nProduct Sales Analysis");
        Date startDate = null, endDate = null;

        while (true) {
            System.out.print("Enter start date (yyyy-mm-dd) or leave empty: ");
            String startDateStr = sc.nextLine();
            if (startDateStr.isEmpty()) break;
            try {
                startDate = Date.valueOf(startDateStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Invalid start date format. Please try again.");
            }
        }

        while (true) {
            System.out.print("Enter end date (yyyy-mm-dd) or leave empty: ");
            String endDateStr = sc.nextLine();
            if (endDateStr.isEmpty()) break;
            try {
                endDate = Date.valueOf(endDateStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Invalid end date format. Please try again.");
            }
        }

        String sql = "SELECT p.product_id, p.name, COUNT(si.sale_item_id) as sales_count, " +
                "SUM(si.quantity) as total_quantity, SUM(si.quantity * si.unit_price) as total_revenue " +
                "FROM Products p " +
                "JOIN SaleItems si ON p.product_id = si.product_id " +
                "JOIN Sales s ON si.sale_id = s.sale_id";

        boolean hasStart = startDate != null;
        boolean hasEnd = endDate != null;

        if (hasStart || hasEnd) {
            sql += " WHERE ";
            if (hasStart) sql += "s.sale_date >= ?";
            if (hasStart && hasEnd) sql += " AND ";
            if (hasEnd) sql += "s.sale_date <= ?";
        }

        sql += " GROUP BY p.product_id, p.name ORDER BY total_revenue DESC";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            if (hasStart) pstmt.setDate(1, startDate);
            if (hasEnd) pstmt.setDate(hasStart ? 2 : 1, endDate);

            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n-- Top Selling Products by Revenue --");
            System.out.printf("%-10s %-30s %-15s %-15s %-15s\n",
                    "ID", "Product", "Sales Count", "Total Units", "Total Revenue");
            System.out.println("---------------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-10d %-30s %-15d %-15d $%-14.2f\n",
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getInt("sales_count"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_revenue"));
            }

            if (!found) {
                System.out.println("No sales data found for the selected period.");
            }
        }
    }
/*
    ITS USE TO CHECK THE STOCK BELOW REORDER LEVEL FROM PRODUCTMANAGER CLASS
*/
    void lowStockReport() throws Exception {
        System.out.println("\nLow Stock Report");
        String sql = "SELECT p.product_id, p.name, p.quantity_in_stock, p.reorder_level, " +
                "COALESCE(s.name, 'No Supplier') as supplier_name, s.phone as supplier_phone " +
                "FROM Products p LEFT JOIN Suppliers s ON p.supplier_id = s.supplier_id WHERE " +
                "p.quantity_in_stock <= p.reorder_level";
        PriorityQueue<Product> lowStockQueue = new PriorityQueue<>();

        try (
                Statement st = this.con.createStatement();
                ResultSet rs = st.executeQuery(sql);
        ) {
            while(rs.next()) {
                Product product = new Product(rs.getInt("product" +
                        "_id"), rs.getString("name"), rs.getInt("quantity_" +
                        "in_stock"), rs.getInt("reorder_level"), rs.getString("suppli" +
                        "er_name"), this.validatePhoneNumber(rs.getString("supplier_phone")));
                lowStockQueue.add(product);
            }
        }

        System.out.println("\n-- PRIORITY Low Stock Report --");
        System.out.printf("%-10s %-30s %-15s %-15s %-25s %-20s\n", "ID", "Product", "Current " +
                "Stock", "Reorder Level", "Supplier", "Supplier Phone");
        System.out.println("------------------------------------------------------------------------------------------");
        if (lowStockQueue.isEmpty()) {
            System.out.println(" No products are currently below the reorder level.");
        } else {
            while(!lowStockQueue.isEmpty()) {
                Product p = lowStockQueue.poll();
                System.out.printf("%-10d %-30s %-15d %-15d %-25s %-20s\n", p.id, p.name,
                        p.quantityInStock, p.reorderLevel, p.supplierName, p.supplierPhone);
            }
        }
    }
private void validatePhoneNumber(Scanner sc) {
    System.out.println("\nPhone Number Validation");
    System.out.print("Enter phone number: ");
    String phone = sc.nextLine().trim();

    while(!phone.matches("\\d{10}")) {
        System.out.println("Invalid phone number. It must be at least 10 digits and numeric only.");
        System.out.print("Enter phone: ");
        phone = sc.nextLine().trim();
    }

    System.out.println("Valid phone number: " + phone);
}

private String validatePhoneNumber(String phone) {
    if (phone == null) {
        return "N/A";
    } else {
        String digits = phone.replaceAll("\\D", "");
        return digits.length() == 10 ? digits : "Invalid";
    }
}
}

    /*
    ITS USE IN TRIGGER LOG METHOD
    */
class Stack {
    private Node top;

    private static class Node {
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

class TriggerLog {
    int logId;
    String tableName;
    String operation;
    int recordId;
    String details;
    Timestamp logTime;

    public TriggerLog(int logId, String tableName, String operation, int recordId, String details, Timestamp logTime) {
        this.logId = logId;
        this.tableName = tableName;
        this.operation = operation;
        this.recordId = recordId;
        this.details = details;
        this.logTime = logTime;
    }

    public int getLogId()
    { return logId; }
    public String getTableName()
    { return tableName; }
    public String getOperation()
    { return operation; }
    public int getRecordId()
    { return recordId; }
    public String getDetails()
    { return details; }
    public Timestamp getLogTime()
    { return logTime; }
}
class Product implements Comparable<Product> {
    int id;
    String name;
    int quantityInStock;
    int reorderLevel;
    String supplierName;
    String supplierPhone;

    public Product(int id, String name, int quantityInStock, int reorderLevel, String supplierName, String supplierPhone) {
        this.id = id;
        this.name = name;
        this.quantityInStock = quantityInStock;
        this.reorderLevel = reorderLevel;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
    }

    @Override
    public int compareTo(Product other) {
        // Sort by stock level (lowest first)
        return Integer.compare(this.quantityInStock, other.quantityInStock);
    }
}


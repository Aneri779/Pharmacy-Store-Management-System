package final_pharmacy.pharmacy.manager;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ProductManager {
    Scanner sc=new Scanner(System.in);
    Connection con;

    public ProductManager(Connection con) {
        this.con = con;
    }
    public void manageProducts() throws Exception {
        boolean b1 = true;
        while (b1) {
            System.out.println("\n------Product Management--------");
            System.out.println("1. Add Product");
            System.out.println("2. View ALL Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Check Stock Levels");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewAllProducts();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    checkStockLevels();
                    break;
                case 6:
                    b1 = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    /*
         USE TO ADD PRODUCT IN PRODUCT TABLE USING PreparedStatement
    */
    void addProduct()
    {
        System.out.println("\nAdd New Product");
        System.out.print("Enter product name: ");
        String name = sc.nextLine();
        System.out.print("Enter category:");
        String category = sc.nextLine();
        System.out.print("Enter description: ");
        String description = sc.nextLine();
        System.out.print("Enter price: ");
        double price;
        while (true) {
            try {
                price = sc.nextDouble();
                sc.nextLine();
                if (price > 0) {
                    break; // Valid price, exit loop
                } else {
                    System.out.println("Price must be greater than zero.");
                    System.out.print("Enter proper price: ");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number only.");
                sc.nextLine(); // Clear the invalid input
                System.out.print("Enter proper price: ");
            }
        }
        System.out.print("Enter quantity in stock: ");
        int quantity = sc.nextInt();
        System.out.print("Enter reorder level: ");
        int reorderLevel = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter expiry date (yyyy-mm-dd) or leave empty: ");
        String expiryDateStr = sc.nextLine();
        Date expiryDate = null;

        if (!expiryDateStr.isEmpty()) {
            try {
                LocalDate localDate = LocalDate.parse(expiryDateStr);
                expiryDate = Date.valueOf(localDate);
            } catch (Exception e) {
                System.out.println("Invalid date format. Date not set.");
            }
        }

        System.out.print("Enter supplier ID or 0 for none: ");
        int supplierId = sc.nextInt();
        sc.nextLine();

        String sql = "INSERT INTO Products (name, category, description, price, quantity_in_stock, reorder_level, expiry_date, supplier_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement  st = con.prepareStatement(sql);

             st.setString(1, name);
             st.setString(2, category);
             st.setString(3, description);
             st.setDouble(4, price);
             st.setInt(5, quantity);
             st.setInt(6, reorderLevel);
             st.setDate(7, expiryDate);
            st.setInt(8, supplierId);

            if (supplierId > 0) {
                 st.setInt(8, supplierId);
            } else {
                 st.setNull(8, Types.INTEGER);
            }

            int rowsAffected =  st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

/*
   USE TO SEE ALL PRODUCTS IN PRODUCT TABLE USING STATEMENT
*/
    void viewAllProducts() {
        System.out.println("\nAll Products");
        String sql = "SELECT * FROM Products";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.printf("%-10s %-30s %-15s %-10s %-10s %-15s %-15s\n",
                    "ID", "Name", "Category", "Price", "Stock", "Reorder Level", "Expiry Date");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-30s %-15s %-10.2f %-10d %-15d %-15s\n",
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock"),
                        rs.getInt("reorder_level"),
                        (rs.getDate("expiry_date") != null)
                                ? rs.getDate("expiry_date").toString() : "N/A");
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
        USED TO UPDATE THE PRODUCT TABLE ALL THINGS USING ROUTINES
    */
    void updateProduct()  throws Exception{
        System.out.println("\nUpdate Product");
        System.out.print("Enter product ID to update: ");
        int productId = sc.nextInt();
        sc.nextLine();

        // Check if product exists
        String checkSql = "SELECT * FROM Products WHERE product_id = ?";

            PreparedStatement pstmt = con.prepareStatement(checkSql);

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Product not found with ID: " + productId);
                return;
            }

            System.out.println("Current Product Details:");
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Category: " + rs.getString("category"));
            System.out.println("Description: " + rs.getString("description"));
            System.out.println("Price: " + rs.getDouble("price"));
            System.out.println("Quantity in Stock: " + rs.getInt("quantity_in_stock"));
            System.out.println("Reorder Level: " + rs.getInt("reorder_level"));
            System.out.println("Expiry Date: " + (rs.getDate("expiry_date") != null ?
                    rs.getDate("expiry_date").toString() : "N/A"));

            System.out.println("\nEnter new values");
            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Category: ");
            String category = sc.nextLine();

            System.out.print("Description: ");
            String description = sc.nextLine();

            System.out.print("Price: ");
            double price = sc.nextDouble();

            System.out.print("Quantity in Stock: ");
            int quantity = sc.nextInt();

            System.out.print("Reorder Level: ");
            int reorder = sc.nextInt();
            sc.nextLine();

            System.out.print("Expiry Date (yyyy-mm-dd): ");
            String expiryDateStr = sc.nextLine();

        // Check if date is valid
        boolean isValidDate = false;
        try {
            LocalDate.parse(expiryDateStr);
            isValidDate = true;
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-mm-dd");
        }

        if (!isValidDate) {
            return; // Exit if date is invalid
        }

            CallableStatement cs = con.prepareCall("{CALL UPDATEPRODUCT(?,?,?,?,?,?,?,?)}");
            cs.setInt(1, productId);
            cs.setString(2, name);
            cs.setString(3, category);
            cs.setString(4, description);
            cs.setDouble(5, price);
            cs.setInt(6, quantity);
            cs.setInt(7, reorder);
            try{
                cs.setDate(8, Date.valueOf(expiryDateStr));
            } catch (Exception e) {
                cs.setNull(8,Types.DATE);
            }
        boolean r=cs.execute();
        if (r){
            System.out.println("Product Updated");
        }
        else {
            System.out.println("Product Not Updated");
        }
    }

    /*
        DELETING THE PRODUCT USING ID IN PRODUCT TABLE BY USING PREPAREDSTATEMENT
    */
    void deleteProduct()  throws Exception{
        System.out.println("\nDelete Product");
        System.out.print("Enter product ID to delete: ");
        int productId = sc.nextInt();
        sc.nextLine();

        System.out.print("Are you sure you want to delete this product? (yes/no): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            String sql = "DELETE FROM Products WHERE product_id = ?";

            PreparedStatement  st = con.prepareStatement(sql);
                 st.setInt(1, productId);
                int rowsAffected =  st.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Product deleted successfully.");
                } else {
                    System.out.println("Product not found or deletion failed.");
                }

        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    /*
        USE TO CHECK THE STOCK LEVEL WHICH LESS QUANTITY IN STOCK THAN REORDERLEVEL USING STATEMENT
    */
    public void checkStockLevels() throws Exception {
        System.out.println("\nProducts Below Reorder Level");

        String sql = "SELECT p.product_id, p.name, p.quantity_in_stock, p.reorder_level, s.name as supplier_name,s.phone as supplier_phone  " +
                "FROM Products p LEFT JOIN Suppliers s ON p.supplier_id = s.supplier_id " +
                "WHERE p.quantity_in_stock <= p.reorder_level";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
            boolean hasResults = false;

            System.out.printf("%-10s %-30s %-15s %-15s %-25s %-20s\n",
                    "ID", "Product", "Current Stock", "Reorder Level", "Supplier", "Supplier Phone");
            System.out.println("--------------------------------------------------------------------------------------");

            while (rs.next()) {
                hasResults = true;
                System.out.printf("%-10d %-30s %-15d %-15d %-25s %-20s\n",
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getInt("quantity_in_stock"),
                        rs.getInt("reorder_level"),
                        rs.getString("supplier_name") != null ? rs.getString("supplier_name") : "No Supplier",
                        rs.getString("supplier_phone") != null ? rs.getString("supplier_phone") : "N/A");
            }

            if (!hasResults) {
                System.out.println(" No products are currently below the reorder level.");
            }
    }
}
package final_pharmacy.pharmacy.manager;
import java.io.*;
import java.sql.*;
import java.util.*;

public class CustomerManager {
    Scanner sc=new Scanner(System.in);
    Connection con;
    public CustomerManager(Connection con) {
        this.con = con;
    }
    public void manageCustomers() throws Exception {
        boolean b2 = true;

        while (b2) {
            System.out.println("\n-------------- Customer Management --------------");
            System.out.println("1. Add New Customer\n" +
                    "2. View All Customers\n" +
                    "3. Update Customer\n" +
                    "4. Delete Customer\n" +
                    "5. View Customer Purchases\n" +
                    "6. Export Customer Data to File\n" +
                    "7. Back to Main Menu");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewAllCustomers();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 5:
                    viewCustomerPurchases();
                    break;
                case 6:
                    exportCustomerDataToFile();
                    break;
                case 7:
                    b2 = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

/*
    ADDING THE CUSTOMER IN CUSTOMERS TABLE USING ROUTINES
*/
     void addCustomer() throws Exception {
        System.out.println("Adding New Customer");

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        // Validate and format email
        System.out.print("Enter email (optional): ");
        String email = sc.nextLine().trim();
        if (email!=null) {
            if (!email.contains("@")) {
                System.out.println("you entered wrong syntax of email it must contain @");
                return;
            }
            String[] p = email.split("@", 2);
            String first = p[0].toUpperCase();
            String last = p[1].toLowerCase();
            email=first+"@"+last;
        }

        // Validate phone
         System.out.print("Enter phone: ");
         String phone = sc.nextLine().trim();
         while(!phone.matches("\\d{10,}")) {
             System.out.println("Invalid phone number. It must be at least 10 digits and numeric only.");
             System.out.print("Enter phone: ");
             phone = sc.nextLine().trim();

         }

        System.out.print("Enter address (optional): ");
        String address = sc.nextLine();

        CallableStatement c = con.prepareCall("{CALL ADDCUSTOMER(?, ?, ?, ?)}");
        c.setString(1, name);
        c.setString(2, email);   // pass "" for optional
        c.setString(3, phone);
        c.setString(4, address); // pass "" or null for optional
       int r= c.executeUpdate();
         if (r > 0) {
             System.out.println("Customer: " + name + " Successfully Added");
         } else {
             System.out.println("Failed to add customer");
         }
    }

/*
    USE TO SEE ALL CUSTOMERDETAILS USING STATEMENT
*/
     void viewAllCustomers() throws Exception {
        System.out.println("\nAll Customers");
        String sql = "SELECT * FROM Customers ORDER BY name";


                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

            System.out.printf("%-10s %-30s %-30s %-20s %-50s %-10s\n",
                    "ID", "Name", "Email", "Phone", "Address", "Loyalty");
            System.out.println("---------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-30s %-30s %-20s %-50s %-10d\n",
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email") != null ? rs.getString("email") : "N/A",
                        rs.getString("phone") != null ? rs.getString("phone") : "N/A",
                        rs.getString("address") != null ? rs.getString("address") : "N/A",
                        rs.getInt("loyalty_points"));
            }

    }

    /*
    UPDATING ALL DETAILS OF THE CUSTOMER BY CUSTOMER_ID USING ROUTINES IN CUSTOMERS TABLE
    */
    private void updateCustomer() throws Exception{
        System.out.println("\nUpdate Customer");
        System.out.print("Enter customer ID to update: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        // Check if customer exists
        String Sql = "SELECT * FROM Customers WHERE customer_id = ?";
        PreparedStatement  st = con.prepareStatement(Sql);

             st.setInt(1, customerId);
            ResultSet rs =  st.executeQuery();

            if (!rs.next()) {
                System.out.println("Customer not found with ID: " + customerId);
                return;
            }

            System.out.println("Current Customer Details:");
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Email: " + (rs.getString("email") != null ? rs.getString("email") : "N/A"));
            System.out.println("Phone: " + (rs.getString("phone") != null ? rs.getString("phone") : "N/A"));
            System.out.println("Address: " + (rs.getString("address") != null ? rs.getString("address") : "N/A"));

            System.out.println("\nEnter new values (leave blank to keep current):");

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine().trim();
            if (email!=null) {
                if (!email.contains("@")) {
                    System.out.println("you entered wrong syntax of email it must contain @");
                    return;
                }
                String[] p = email.split("@", 2);
                String first = p[0].toUpperCase();
                String last = p[1].toLowerCase();
                email=first+"@"+last;
            }

            System.out.print("Phone: ");
            String phone = sc.nextLine().trim();
            while(!phone.matches("\\d{10}")) {
                System.out.println("Invalid phone number. It must be at least 10 digits and numeric only.");
                System.out.print("Enter phone: ");
                phone = sc.nextLine().trim();
            }

            System.out.print("Address: ");
            String address = sc.nextLine();

            CallableStatement cst=con.prepareCall("{call UPDATECUSTOMER(?,?,?,?,?)}");
            cst.setInt(1,customerId);
            cst.setString(2,name);
            cst.setString(3,email);
            cst.setString(4,phone);
            cst.setString(5,address);
            int r=cst.executeUpdate();
            if(r>0)
                System.out.println("updated customer details successfully");
            else
            {
                System.out.println("updation is cancelled");
            }

    }
        /*
        DELETING THE CUSTOMER IN CUSTOMERS TABLE USING THE ROUTINES BY CUSTOMER_ID
        */
    private void deleteCustomer()  throws Exception{
        System.out.println("\nDelete Customer");
        System.out.print("Enter customer ID to delete: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        System.out.print("Are you sure you want to delete this customer? (yes/no): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            String sql = "DELETE FROM Customers WHERE customer_id = ?";
            CallableStatement cst=con.prepareCall("{call DELETECUSTOMER(?)}");
            cst.setInt(1,customerId);
            int r=cst.executeUpdate();
            if (r > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Customer not found or deletion failed.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewCustomerPurchases()  throws Exception{
        System.out.println("\nCustomer Purchase History");
        System.out.print("Enter customer ID: ");
        int customerId = sc.nextInt();
        sc.nextLine();

        // Get customer details
        String customerSql = "SELECT name FROM Customers WHERE customer_id = ?";
        String customerName = null;

        PreparedStatement  st = con.prepareStatement(customerSql);
        st.setInt(1, customerId);
        ResultSet rs =  st.executeQuery();

            if (!rs.next()) {
                System.out.println("Customer not found with ID: " + customerId);
                return;
            }

            customerName = rs.getString("name");


        System.out.println("\nPurchase History for: " + customerName);

        String salesSql = "SELECT s.sale_id, s.sale_date, s.total_amount, s.payment_method " +
                "FROM Sales s WHERE s.customer_id = ? ORDER BY s.sale_date DESC";

                PreparedStatement  st1 = con.prepareStatement(salesSql);

             st1.setInt(1, customerId);
            ResultSet rs1 =  st.executeQuery();

            if (!rs1.next()) {
                System.out.println("No purchase history found for this customer.");
                return;
            }

            do {
                System.out.println("\nSale ID: " + rs1.getInt("sale_id"));
                System.out.println("Date: " + rs1.getDate("sale_date"));
                System.out.println("Total: $" + rs1.getDouble("total_amount"));
                System.out.println("Payment Method: " + rs1.getString("payment_method"));

                // Get items for this sale
                String itemsSql = "SELECT p.name, si.quantity, si.unit_price " +
                        "FROM SaleItems si " +
                        "JOIN Products p ON si.product_id = p.product_id " +
                        "WHERE si.sale_id = ?";

                PreparedStatement itemsStmt = con.prepareStatement(itemsSql);
                    itemsStmt.setInt(1, rs.getInt("sale_id"));
                    ResultSet itemsRs = itemsStmt.executeQuery();

                    System.out.println("\nPurchased Items:");
                    System.out.printf("%-30s %-10s %-10s %-10s\n", "Product", "Quantity", "Unit Price", "Total");

                    while (itemsRs.next()) {
                        String productName = itemsRs.getString("name");
                        int quantity = itemsRs.getInt("quantity");
                        double unitPrice = itemsRs.getDouble("unit_price");
                        double itemTotal = quantity * unitPrice;

                        System.out.printf("%-30s %-10d $%-9.2f $%-9.2f\n",
                                productName, quantity, unitPrice, itemTotal);
                    }

            } while (rs1.next());
    }

    // store the exported customer data as a CLOB
    private void exportCustomerDataToFile() throws Exception {
        String fileName = "customers_export.csv";
        String sql = "SELECT * FROM Customers ORDER BY name";

        try (
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                PrintWriter writer = new PrintWriter(new FileWriter(fileName)) // write formatted text to a file (or other output stream).
        ) {
            String border = "+----+------------------------------+------------------------------+-------------+--------";
            String header = "| ID | Name                         | Email                        | Phone       | Address";

            writer.println(border);
            writer.println(header);
            writer.println(border);

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String name = rs.getString("name");
                String email = rs.getString("email") != null ? rs.getString("email") : "";
                String phone = rs.getString("phone") != null ? rs.getString("phone") : "";
                String address = rs.getString("address") != null ? rs.getString("address") : "";

                String row = String.format("| %-2d | %-28s | %-28s | %-11s | %-29s",
                        id, name, email, phone, address);


                writer.println(row);
            }

            writer.println(border);
            writer.flush();

            System.out.println("Customer data exported to file: " + fileName);

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return;
        }

        //  Now store the full file content as CLOB
        try (
                FileReader fr = new FileReader(fileName);
                PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO CustomerFiles (file_name, file_data, created_at) VALUES (?, ?, NOW())")
        ) {
            pstmt.setString(1, fileName);
            pstmt.setCharacterStream(2, fr); // Store full file content as CLOB

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Customer file content stored in database as CLOB.");
            } else {
                System.out.println("Failed to store customer file.");
            }
        } catch (Exception e) {
            System.out.println("Error storing file to database: " + e.getMessage());
        }
    }
}
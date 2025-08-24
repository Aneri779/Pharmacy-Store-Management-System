package final_pharmacy.pharmacy.main;
import final_pharmacy.pharmacy.manager.*;
import final_pharmacy.pharmacy.report.ReportGenerator;
import final_pharmacy.pharmacy.sales.SalesProcessor;
import final_pharmacy.pharmacy.util.Authentication;

import java.sql.*;
import java.util.*;

public class PharmacyStoreManagement {
    static String JDBC_URL = "jdbc:mysql://localhost:3306/pharmacy_management";
    static String USERNAME = "root";
    static String PASSWORD = "";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        if (Authentication.login(sc)) {
            try (Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);) {
                ProductManager pm = new ProductManager(con);
                CustomerManager cm = new CustomerManager(con);
                SalesProcessor sp = new SalesProcessor(con);
                ReportGenerator rg = new ReportGenerator(con);
                System.out.println("\n-----------------------" +
                        "-Pharmacy Management System---------------------------");
                boolean b = true;
                while (b) {
                    /*
                    IT'S CONTAINS METHODS LIKE ADD PRODUCT,VIEW ALL PRODUCTS,UPDATE PRODUCT,DELETE PRODUCT,
                    CHECK STOCK LEVEL BELOW REORDER LEVEL
                    */
                    System.out.println("1. Product Management");
                    /*
                    ITS CONTAINS METHODS LIKE ADD CUSTOMER ,VIEW ALL CUSTOMERS,UPDATE CUSTOMERS,DELETE CUSTOMERS,
                    VIEW CUSTOMER PURCHASE,EXPORTING CUSTOMER DATA TO FILE
                    */
                    System.out.println("2. Customer Management");
                    System.out.println("3. Sales Processing");
                    /*
                    ITS CONTAINS METHODS LIKE VIEW TRIGGERLOG TABLE,SALES SUMMARY REPORT,PRODUCT SALES REPORT,LOW STOCK REPORT
                    */
                    System.out.println("4. Reports");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = 0;
                    try {

                        choice=sc.nextInt();

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a digit between 1 and 5.");
                        continue;
                    }

                    switch (choice) {
                        case 1:
                            pm.manageProducts();
                            break;
                        case 2:
                            cm.manageCustomers();
                            break;
                        case 3:
                            sp.processSales();
                            break;
                        case 4:
                            rg.generateReports();
                            break;
                        case 5:
                            b = false;
                            System.out.println("Exiting system...");
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

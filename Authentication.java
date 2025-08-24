package final_pharmacy.pharmacy.util;
import java.util.*;

public class Authentication {
    public static boolean login(Scanner sc) {
        Map<String, String> userCredentials = new HashMap<>();

        // Using Java's default String.hashCode() as the hashing method
        userCredentials.put("admin", hashPassword("admin123"));        // admin123
        userCredentials.put("pharmacist", hashPassword("pharma456"));  // pharma456
        userCredentials.put("manager", hashPassword("manage789"));     // manage789

        System.out.println("\n================= Pharmacy Management System Login =================");

        int attempts = 3;
        while(attempts > 0) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            String hashedInput = hashPassword(password);
            if (userCredentials.containsKey(username)) {
                if (((String)userCredentials.get(username)).equals(hashedInput)) {
                    System.out.println(" Login successful. Welcome, " + username + "!");
                    return true;
                }

                System.out.println(" Incorrect password.");
            } else {
                System.out.println(" Username not found.");
            }

            --attempts;
            if (attempts > 0) {
                System.out.println("Attempts remaining: " + attempts);
            } else {
                System.out.println("Too many failed attempts. Exiting...");
            }
        }

        return false;
    }

    public static String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }
}

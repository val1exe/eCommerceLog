import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

enum UserRole {
    BUYER,
    SELLER,
    LOGISTICS,
    ADMIN
}

/**
 * Represents a user in the E-Commerce system.
 * Contains user profile information and selected role.
 */
class User {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private UserRole role;

    public User(String username, String password, String email, String fullName, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + email + "," + fullName + "," + address + "," + (role != null ? role.name() : "");
    }
}

/**
 * Handles file operations for user data.
 * Manages saving and retrieving user information from text files.
 */
class UserRepository {
    private static final String USERS_FILE = "USERS.txt";
    private static final String BUYERS_FILE = "BUYERS.txt";
    private static final String SELLERS_FILE = "SELLERS.txt";
    private static final String LOGISTICS_FILE = "LOGISTICS.txt";
    private static final String ADMINS_FILE = "ADMINS.txt";

    public void saveUser(User user) {
        try (FileWriter fw = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user.toString());
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    public void saveUserToRoleFile(User user) {
        String roleFile;
        switch (user.getRole()) {
            case BUYER:
                roleFile = BUYERS_FILE;
                break;
            case SELLER:
                roleFile = SELLERS_FILE;
                break;
            case LOGISTICS:
                roleFile = LOGISTICS_FILE;
                break;
            case ADMIN:
                roleFile = ADMINS_FILE;
                break;
            default:
                System.out.println("Invalid role!");
                return;
        }

        try (FileWriter fw = new FileWriter(roleFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user.toString());
            System.out.println("User added to " + user.getRole() + " list successfully!");
        } catch (IOException e) {
            System.out.println("Error saving user to role file: " + e.getMessage());
        }
    }

    public User findUser(String username, String password) {
        try (FileReader fr = new FileReader(USERS_FILE);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(username) && parts[1].equals(password)) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    if (parts.length >= 6 && !parts[5].isEmpty()) {
                        user.setRole(UserRole.valueOf(parts[5]));
                    }
                    return user;
                }
            }
        } catch (IOException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    public boolean usernameExists(String username) {
        try (FileReader fr = new FileReader(USERS_FILE);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // If file doesn't exist yet, username doesn't exist
            return false;
        }
        return false;
    }

    public void updateUserRole(User user) {
        List<String> fileContent = new ArrayList<>();
        try (FileReader fr = new FileReader(USERS_FILE);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(user.getUsername()) && parts[1].equals(user.getPassword())) {
                    fileContent.add(user.toString());
                } else {
                    fileContent.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
            return;
        }

        try (FileWriter fw = new FileWriter(USERS_FILE);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (String line : fileContent) {
                out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error updating user role: " + e.getMessage());
        }
    }
}

//Utility class for validating user input.

class ValidationUtils {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}

/**
 * Main interface class for the E-Commerce system.
 * This class handles user registration and login.
 * After successful authentication, it redirects users to their role-specific dashboards.
 */
public class ECommerceSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static UserRepository userRepository = new UserRepository();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== E-COMMERCE SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Thank you for using our system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.println("\n===== USER REGISTRATION =====");

        String username;
        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (userRepository.usernameExists(username)) {
                System.out.println("Username already exists. Please choose another.");
            }
        } while (userRepository.usernameExists(username));

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String email;
        do {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (!ValidationUtils.isValidEmail(email)) {
                System.out.println("Invalid email format. Please include '@' character.");
            }
        } while (!ValidationUtils.isValidEmail(email));

        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        User user = new User(username, password, email, fullName, address);
        userRepository.saveUser(user);
    }

    private static void loginUser() {
        System.out.println("\n===== USER LOGIN =====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userRepository.findUser(username, password);
        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getFullName() + "!");
            if (user.getRole() != null) {
                // User already has a role, go directly to dashboard
                showRoleDashboard(user);
            } else {
                // New user, select role first
                selectRole(user);
            }
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void selectRole(User user) {
        System.out.println("\n===== ROLE SELECTION =====");
        System.out.println("Select a role:");
        System.out.println("1. Buyer");
        System.out.println("2. Seller");
        System.out.println("3. Logistics");
        System.out.println("4. Admin");
        System.out.print("Choose an option: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return;
        }

        UserRole selectedRole;
        switch (choice) {
            case 1:
                selectedRole = UserRole.BUYER;
                break;
            case 2:
                selectedRole = UserRole.SELLER;
                break;
            case 3:
                selectedRole = UserRole.LOGISTICS;
                break;
            case 4:
                selectedRole = UserRole.ADMIN;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                return;
        }

        user.setRole(selectedRole);
        userRepository.updateUserRole(user);
        userRepository.saveUserToRoleFile(user);

        System.out.println("You have successfully logged in as a " + selectedRole.name() + "!");
        showRoleDashboard(user);
    }

    private static void showRoleDashboard(User user) {
        switch (user.getRole()) {
            case SELLER:
                // Redirect to your existing Seller Dashboard
                redirectToSellerDashboard(user);
                break;
            case BUYER:
                redirectToBuyerDashboard(user);
                break;
            case LOGISTICS:
                redirectToLogisticsDashboard(user);
                break;
            case ADMIN:
                redirectToAdminDashboard(user);
                break;
            default:
                System.out.println("Role dashboard not available.");
        }
    }

    private static void redirectToSellerDashboard(User user) {
        System.out.println("\n===== REDIRECTING TO SELLER DASHBOARD =====");
        System.out.println("Connecting to your existing Seller Dashboard...");

        // Set the current user in the SellerSession before launching dashboard
        SellerSession.setCurrentUser(user);

        System.out.println("User details being passed to Seller Dashboard:");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Address: " + user.getAddress());

        // Start the dashboard controller
        DashboardController controller = new DashboardController();
        controller.start();
    }

    private static void redirectToBuyerDashboard(User user) {
        System.out.println("\n===== BUYER DASHBOARD =====");
        System.out.println("Welcome to the Buyer Dashboard, " + user.getFullName() + "!");
        System.out.println("This is a placeholder for Buyer Dashboard implementation.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void redirectToLogisticsDashboard(User user) {
        System.out.println("\n===== LOGISTICS DASHBOARD =====");
        System.out.println("Welcome to the Logistics Dashboard, " + user.getFullName() + "!");
        System.out.println("This is a placeholder for Logistics Dashboard implementation.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void redirectToAdminDashboard(User user) {
        System.out.println("\n===== ADMIN DASHBOARD =====");
        System.out.println("Welcome to the Admin Dashboard, " + user.getFullName() + "!");
        System.out.println("This is a placeholder for Admin Dashboard implementation.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}


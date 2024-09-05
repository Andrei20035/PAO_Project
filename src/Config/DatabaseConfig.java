package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "Andrei";
        String password = "Andrei2003.";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            String createDatabase = "CREATE DATABASE pao";
            statement.executeUpdate(createDatabase);

            statement.executeUpdate("USE ecommerce");

            String createUserTable = "CREATE TABLE User (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "address VARCHAR(255), " +
                    "DiscountRate DECIMAL(5, 2) CHECK (DiscountRate BETWEEN 1.0 AND 99.0))";
            statement.executeUpdate(createUserTable);

            String createPremiumUserTable = "CREATE TABLE PremiumUser (" +
                    "id INT PRIMARY KEY, " +
                    "discountRate DOUBLE, " +
                    "FOREIGN KEY (id) REFERENCES User(id))";
            statement.executeUpdate(createPremiumUserTable);

            String createProductTable = "CREATE TABLE Product (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "description TEXT, " +
                    "price DOUBLE NOT NULL, " +
                    "stockQuantity INT NOT NULL)";
            statement.executeUpdate(createProductTable);

            String createCartItemTable = "CREATE TABLE CartItem (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "shoppingCartId INT NOT NULL, " +
                    "productId INT, " +
                    "quantity INT NOT NULL, " +
                    "FOREIGN KEY (productId) REFERENCES Product(id), " +
                    "FOREIGN KEY (shoppingCartId) REFERENCES ShoppingCart(id))";
            statement.executeUpdate(createCartItemTable);


            String createShoppingCartTable = "CREATE TABLE ShoppingCart (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userId INT, " +
                    "totalAmount DOUBLE, " +
                    "FOREIGN KEY (userId) REFERENCES User(id))";
            statement.executeUpdate(createShoppingCartTable);

            String createOrderItemTable = "CREATE TABLE OrderItem (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "productId INT, " +
                    "quantity INT NOT NULL, " +
                    "price DOUBLE NOT NULL, " +
                    "FOREIGN KEY (productId) REFERENCES Product(id))";
            statement.executeUpdate(createOrderItemTable);

            String createOrderTable = "CREATE TABLE `Order` (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userId INT, " +
                    "totalAmount DOUBLE, " +
                    "status VARCHAR(255), " +
                    "FOREIGN KEY (userId) REFERENCES User(id))";
            statement.executeUpdate(createOrderTable);

            String createReviewTable = "CREATE TABLE Review (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userId INT, " +
                    "productId INT, " +
                    "rating INT NOT NULL, " +
                    "comment TEXT, " +
                    "FOREIGN KEY (userId) REFERENCES User(id), " +
                    "FOREIGN KEY (productId) REFERENCES Product(id))";
            statement.executeUpdate(createReviewTable);

            System.out.println("Baza de date a fost creata cu succes!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

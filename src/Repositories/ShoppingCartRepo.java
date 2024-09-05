package Repositories;

import Models.CartItem;
import Models.Product;
import Models.ShoppingCart;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartRepo {
    private Connection connection;

    public ShoppingCartRepo(Connection connection) {
        this.connection = connection;
    }

    public void createShoppingCart(ShoppingCart shoppingCart) {
        try {
            String query = "INSERT INTO ShoppingCart (userId, totalAmount) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, shoppingCart.getUser().getId());
            statement.setDouble(2, shoppingCart.getTotalAmount());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                shoppingCart.setId(generatedKeys.getInt(1));
            }

            for (CartItem item : shoppingCart.getItems()) {
                addCartItemToDatabase(shoppingCart.getId(), item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ShoppingCart getShoppingCartById(int id) {
        try {
            String query = "SELECT * FROM ShoppingCart WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                double totalAmount = resultSet.getDouble("totalAmount");
                User user = new UserRepo(connection).getUserById(userId);
                ShoppingCart shoppingCart = new ShoppingCart(id, user, totalAmount);
                shoppingCart.setItems(getCartItemsByShoppingCartId(id));
                return shoppingCart;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateShoppingCart(ShoppingCart shoppingCart) {
        try {
            String query = "UPDATE ShoppingCart SET userId = ?, totalAmount = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, shoppingCart.getUser().getId());
            statement.setDouble(2, shoppingCart.getTotalAmount());
            statement.setInt(3, shoppingCart.getId());
            statement.executeUpdate();

            for (CartItem item : shoppingCart.getItems()) {
                addCartItemToDatabase(shoppingCart.getId(), item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteShoppingCart(int id) {
        try {
            String query = "DELETE FROM ShoppingCart WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCartItemToDatabase(int shoppingCartId, CartItem cartItem) {
        try {
            String query = "INSERT INTO CartItem (shoppingCartId, productId, quantity) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, shoppingCartId);
            statement.setInt(2, cartItem.getProduct().getId());
            statement.setInt(3, cartItem.getQuantity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<CartItem> getCartItemsByShoppingCartId(int shoppingCartId) {
        List<CartItem> cartItems = new ArrayList<>();
        try {
            String query = "SELECT * FROM CartItem WHERE shoppingCartId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, shoppingCartId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("productId");
                int quantity = resultSet.getInt("quantity");
                Product product = new ProductRepo(connection).getProductById(productId);
                CartItem cartItem = new CartItem(id, product, quantity);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public List<ShoppingCart> getAllShoppingCarts() {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        try {
            String query = "SELECT * FROM ShoppingCart";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("userId");
                double totalAmount = resultSet.getDouble("totalAmount");
                User user = new UserRepo(connection).getUserById(userId);
                ShoppingCart shoppingCart = new ShoppingCart(id, user, totalAmount);
                shoppingCart.setItems(getCartItemsByShoppingCartId(id));
                shoppingCarts.add(shoppingCart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shoppingCarts;
    }
}

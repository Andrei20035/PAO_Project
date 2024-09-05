package Repositories;

import Models.CartItem;
import Models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemRepo {
    private final Connection connection;

    public CartItemRepo(Connection connection) {
        this.connection = connection;
    }

    public void createCartItem(CartItem cartItem) {
        String sql = "INSERT INTO CartItem (productId, quantity) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getProduct().getId());
            stmt.setInt(2, cartItem.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CartItem getCartItemById(int id) {
        String sql = "SELECT * FROM CartItem WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new ProductRepo(connection).getProductById(rs.getInt("productId"));
                return new CartItem(rs.getInt("id"), product, rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM CartItem";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Product product = new ProductRepo(connection).getProductById(rs.getInt("productId"));
                cartItems.add(new CartItem(rs.getInt("id"), product, rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public void updateCartItem(CartItem cartItem) {
        String sql = "UPDATE CartItem SET productId = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getProduct().getId());
            stmt.setInt(2, cartItem.getQuantity());
            stmt.setInt(3, cartItem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCartItem(int id) {
        String sql = "DELETE FROM CartItem WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

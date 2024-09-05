package Repositories;

import Models.OrderItem;
import Models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepo {
    private final Connection connection;

    public OrderItemRepo(Connection connection) {
        this.connection = connection;
    }

    public void createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO orderitem (productId, quantity, price) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getProduct().getId());
            stmt.setInt(2, orderItem.getQuantity());
            stmt.setDouble(3, orderItem.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrderItem getOrderItemById(int id) {
        String sql = "SELECT * FROM orderitem WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new ProductRepo(connection).getProductById(rs.getInt("productId"));
                return new OrderItem(rs.getInt("id"), product, rs.getInt("quantity"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM orderitem";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Product product = new ProductRepo(connection).getProductById(rs.getInt("productId"));
                orderItems.add(new OrderItem(rs.getInt("id"), product, rs.getInt("quantity"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public void updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE orderitem SET productId = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getProduct().getId());
            stmt.setInt(2, orderItem.getQuantity());
            stmt.setDouble(3, orderItem.getPrice());
            stmt.setInt(4, orderItem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrderItem(int id) {
        String sql = "DELETE FROM orderitem WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

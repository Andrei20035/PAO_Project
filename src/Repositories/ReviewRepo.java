package Repositories;

import Models.Product;
import Models.Review;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepo {
    private final Connection connection;

    public ReviewRepo(Connection connection) {
        this.connection = connection;
    }

    public void createReview(Review review) {
        String sql = "INSERT INTO review (userId, productId, rating, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, review.getUser().getId());
            stmt.setInt(2, review.getProduct().getId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Review getReviewById(int id) {
        String sql = "SELECT * FROM review WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new UserRepo(connection).getUserById(rs.getInt("userId"));
                Product product = new ProductRepo(connection).getProductById(rs.getInt("productId"));
                return new Review(rs.getInt("id"), user, product, rs.getInt("rating"), rs.getString("comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM review";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new UserRepo(connection).getUserById(rs.getInt("userId"));
                Product product = new ProductRepo(connection).getProductById(rs.getInt("productId"));
                reviews.add(new Review(rs.getInt("id"), user, product, rs.getInt("rating"), rs.getString("comment")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public void updateReview(Review review) {
        String sql = "UPDATE review SET userId = ?, productId = ?, rating = ?, comment = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, review.getUser().getId());
            stmt.setInt(2, review.getProduct().getId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setInt(5, review.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReview(int id) {
        String sql = "DELETE FROM review WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

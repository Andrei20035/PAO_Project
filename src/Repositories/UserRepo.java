package Repositories;

import Models.PremiumUser;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {
    private Connection connection;

    public UserRepo(Connection connection) {
        this.connection = connection;
    }

    public void createUser(User user) {
        try {
            String query = "INSERT INTO User (name, email, password, address) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getAddress());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPremiumUser(PremiumUser premiumUser) {
        try {
            String query = "INSERT INTO User (name, email, password, address, discountRate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, premiumUser.getName());
            statement.setString(2, premiumUser.getEmail());
            statement.setString(3, premiumUser.getPassword());
            statement.setString(4, premiumUser.getAddress());
            statement.setDouble(5, premiumUser.getDiscountRate());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                premiumUser.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        try {
            String query = "SELECT * FROM User WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String address = resultSet.getString("address");
                double discountRate = resultSet.getDouble("discountRate");
                if (discountRate > 0) {
                    return new PremiumUser(id, name, email, password, address, discountRate);
                } else {
                    return new User(id, name, email, password, address);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM User";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String address = resultSet.getString("address");
                double discountRate = resultSet.getDouble("discountRate");
                if (discountRate > 0) {
                    users.add(new PremiumUser(id, name, email, password, address, discountRate));
                } else {
                    users.add(new User(id, name, email, password, address));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateUser(User user) {
        try {
            String query = "UPDATE User SET name = ?, email = ?, password = ?, address = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getAddress());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePremiumUser(PremiumUser premiumUser) {
        try {
            String query = "UPDATE User SET name = ?, email = ?, password = ?, address = ?, discountRate = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, premiumUser.getName());
            statement.setString(2, premiumUser.getEmail());
            statement.setString(3, premiumUser.getPassword());
            statement.setString(4, premiumUser.getAddress());
            statement.setDouble(5, premiumUser.getDiscountRate());
            statement.setInt(6, premiumUser.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        try {
            String query = "DELETE FROM User WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package Repositories;

import Models.PremiumUser;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PremiumUserRepo {
    private final Connection connection;

    public PremiumUserRepo(Connection connection) {
        this.connection = connection;
    }

    public List<PremiumUser> getAllPremiumUsers() {
        List<PremiumUser> premiumUsers = new ArrayList<>();
        String sql = "SELECT * FROM PremiumUser";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User user = new UserRepo(connection).getUserById(rs.getInt("id"));
                premiumUsers.add(new PremiumUser(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getAddress(),
                        rs.getDouble("discountRate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return premiumUsers;
    }
}

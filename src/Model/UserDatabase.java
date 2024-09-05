package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDatabase {
    private Connection connection;

    public UserDatabase(String dbPath) throws SQLException {
        // Connect to the SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }
    
    public boolean checkIfUserExists(String userId) throws SQLException {
        String query = "SELECT 1 FROM Users WHERE UserId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            throw e;
        }
    }
    
    public User getUserDetails(String userId) throws SQLException {
        User user = null;
        String query = "SELECT UserId, Name, Color, Password FROM Users WHERE UserId = ?"; // Changed query to use correct column and additional parameter

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String uId = resultSet.getString("UserId");
                String retrievedUserName = resultSet.getString("Name");
                String color = resultSet.getString("Color");
                String passwordFromDB = resultSet.getString("Password");
                user = new User(uId, retrievedUserName, color, passwordFromDB);
            }
        }

        return user;
    }

    public List<User> fetchAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT UserId, Name, Color, Password FROM Users";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String userId = resultSet.getString("UserId");
                String userName = resultSet.getString("Name");
                String color = resultSet.getString("Color");
                String passwordHash = resultSet.getString("Password");
                users.add(new User(userId, userName, color,passwordHash));
            }
        }

        return users;
    }
    

    public void registerUser(String userName, String userId, String password) throws SQLException {
        String insertSQL = """
            INSERT INTO Users (Name, UserId, Color, Password) 
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userId);
            preparedStatement.setString(3,"GRAY");
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        }
    }
    
}

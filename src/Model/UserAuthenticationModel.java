package Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthenticationModel {
    private Connection connection;

    public UserAuthenticationModel(Connection connection) {
        this.connection = connection;
    }

    // Register a new user
    public boolean registerUser(String name, String userId, String password, String color) {
        String hashedPassword = hashPassword(password); // Securely hash the password
        String query = "INSERT INTO users (Name, UserId, Password, Color) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, userId);
            statement.setString(3, hashedPassword);
            statement.setString(4, color);
            return statement.executeUpdate() > 0; // Return true if successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate user login
    public boolean validateUser(String userId, String password) {
        String query = "SELECT Password FROM users WHERE UserId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("Password");
                return validatePassword(password, storedHashedPassword); // Check the password
            }
            return false; // User not found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String hashPassword(String password) {
    	// TO BE IMPLEMENTED
        return password;
    }

    private boolean validatePassword(String password, String hashedPassword) {
        return password.equals(hashedPassword);
    }
}

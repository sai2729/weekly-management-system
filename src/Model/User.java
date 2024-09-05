package Model;

public class User {
    private String userId;
    private String userName;
    private String color;
    private String passwordHash; // Store the password hash

    public User(String userId, String userName, String color, String passwordHash) {
        this.userId = userId;
        this.userName = userName;
        this.color = color;
        this.passwordHash = passwordHash;
    }

    public String getUserId() {
        return userId;
    }

    public String getColor() {
        return color;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return userName;
    }
}

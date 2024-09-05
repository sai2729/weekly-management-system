package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SignUpView extends JFrame {
    private JTextField nameField;
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JComboBox<String> colorComboBox;
    private JButton signupButton;
    private JButton backButton;


    public SignUpView() {
        setTitle("Signup");
        setLayout(new BorderLayout());

        // Signup form
        JPanel signupPanel = new JPanel(new GridLayout(6, 2));

        signupPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        signupPanel.add(nameField);

        signupPanel.add(new JLabel("UserId:"));
        userIdField = new JTextField();
        signupPanel.add(userIdField);

        signupPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        signupPanel.add(passwordField);

        signupPanel.add(new JLabel("Color:"));
        colorComboBox = new JComboBox<>(new String[]{"GREEN", "ORANGE", "LIGHT_GRAY", "CYAN"});
        signupPanel.add(colorComboBox);

        signupButton = new JButton("Signup");
        signupPanel.add(new JLabel()); // Filler to align the button
        signupPanel.add(signupButton);
        
        // Button to go back to login
        backButton = new JButton("Already have an account? Login");
        signupPanel.add(backButton);

        add(signupPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allows for closing without terminating the app
        setSize(300, 200);
        setLocationRelativeTo(null); // Center the window on the screen
    }

    public void setSignupListener(ActionListener listener) {
        signupButton.addActionListener(listener); // Connect to the controller
    }
    
    public void setBackNavigationListener(ActionListener listener) {
        backButton.addActionListener(listener); // Listener for navigation back to login
    }

    public String getName() {
        return nameField.getText();
    }

    public String getUserId() {
        return userIdField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword(); // Passwords are stored as char arrays for security
    }

    public String getColor() {
        return (String) colorComboBox.getSelectedItem();
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

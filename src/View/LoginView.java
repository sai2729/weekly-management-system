package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton; // Button to navigate to the signup view

    public LoginView() {
        setTitle("Login");
        setLayout(new BorderLayout());

        // Login form
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("UserId:"));
        userIdField = new JTextField();
        loginPanel.add(userIdField);

        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        
        // Button to navigate to signup
        signupButton = new JButton("Don't have an account? Sign Up");
        loginPanel.add(signupButton);

        add(loginPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
    }
    

    public String getUserIdField() {
		return userIdField.getText();
	}

	public char[] getPasswordField() {
		return passwordField.getPassword();
	}

	public void setLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
	
    public void setSignupNavigationListener(ActionListener listener) {
        signupButton.addActionListener(listener);
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

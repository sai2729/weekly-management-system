package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.ScheduleModel;
import Model.UserAuthenticationModel;
import View.LoginView;
import View.ScheduleView;
import View.SignUpView;

public class UserController {
    private UserAuthenticationModel userModel;
    private LoginView loginView;
    private SignUpView signupView;
    private ScheduleView scheduleView;
    private ScheduleController scheduleController;


    public UserController(UserAuthenticationModel userModel, LoginView loginView, SignUpView signupView) {
        this.userModel = userModel;
        this.loginView = loginView;
        this.signupView = signupView;

        // Attach listeners to the views for both login and signup
        loginView.setLoginListener(e -> handleLogin());
        loginView.setSignupNavigationListener(e -> navigateToSignup()); // Navigate to signup

        signupView.setSignupListener(e -> handleSignup());
        signupView.setBackNavigationListener(e -> navigateToLogin()); // Navigate to login

    }

    // Handle the login process
    private void handleLogin() {
        String userId = loginView.getUserIdField();
        char[] passwordChars = loginView.getPasswordField(); // Password is stored as char[]
        String password = new String(passwordChars); // Convert char[] to String

        if (userModel.validateUser(userId, password)) {
            loginView.showSuccessMessage("Login successful!"); // Show success message
            // Initialize Schedule components when login is successful
            
            ScheduleModel model = new ScheduleModel();
            List<String> userNames = new ArrayList<>();
            scheduleView = new ScheduleView(model, userNames,userId);
            scheduleController = new ScheduleController(model, scheduleView, "my_database.db",userId);
            scheduleView.setController(scheduleController);
            // Hide login/signup views and show schedule view
            loginView.setVisible(false);
            signupView.setVisible(false);
            scheduleView.setVisible(true); // Display the schedule view

            
        } else {
            loginView.showErrorMessage("Invalid credentials. Please try again."); // Show error message
        }
    }

    // Handle the signup process
    private void handleSignup() {
        String name = signupView.getName();
        String userId = signupView.getUserId();
        char[] passwordChars = signupView.getPassword();
        String password = new String(passwordChars); // Convert char[] to String
        String color = signupView.getColor();

        if (userModel.registerUser(name, userId, password, color)) {
            signupView.showSuccessMessage("Signup successful!"); // Show success message
            navigateToLogin();
        } else {
            signupView.showErrorMessage("Signup failed. User might already exist."); // Show error message
        }
    }
    
    // Navigate to the signup view
    private void navigateToSignup() {
        loginView.setVisible(false); // Hide the login view
        signupView.setVisible(true); // Show the signup view
    }

    // Navigate back to the login view
    private void navigateToLogin() {
        signupView.setVisible(false); // Hide the signup view
        loginView.setVisible(true); // Show the login view
    }
}


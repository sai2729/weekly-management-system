import Model.ScheduleModel;
import Model.UserAuthenticationModel;
import View.LoginView;
import View.SignUpView;
import View.ScheduleView;
import Controller.ScheduleController;
import Controller.UserController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static String dbPath = "my_database.db";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:my_database.db");

            // Create the model
            UserAuthenticationModel userModel = new UserAuthenticationModel(connection);

            // Create the views
            LoginView loginView = new LoginView();
            SignUpView signupView = new SignUpView();

            // Create the controller and connect views
            UserController userController = new UserController(userModel, loginView, signupView);

            // Show the login view
            loginView.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
}

package Controller;
import Model.EventDatabase;
import Model.ScheduleModel;
import Model.User;
import Model.UserAuthenticationModel;
import Model.UserDatabase;
import View.EventDialog;
import View.LoginView;
import View.ScheduleView;
import View.SignUpView;
import Model.Event;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.JSpinner;

import java.util.Calendar;
import java.util.Date;

public class ScheduleController {
    private ScheduleModel model;
    private ScheduleView view;
    private EventDatabase database;
    private UserDatabase userDatabase;
    private String userLoggedIn;
    EventDialog eventDialog;
    private EventDatabase eventDatabase;

    public ScheduleController(ScheduleModel model, ScheduleView view, String dbPath, String userLoggedIn) {
        this.model = model;
        this.view = view;
        this.userLoggedIn=userLoggedIn;

        try {
            database = new EventDatabase(dbPath); // Initialize the database
            userDatabase = new UserDatabase(dbPath);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            return;
        }
        
        setEventListeners(); // Set up event listeners for the view
        updateModelFromDatabase();
        if(this.userLoggedIn.equals("Admin")) {
        	getUserNames();
        }
        printDataFromDB();
    }


    private void getUserNames() {
    	try {
            // Fetch all user names for the dropdown
    		List<User> users = userDatabase.fetchAllUsers();
    			view.updateUserDropdown(users,model);

        } catch (SQLException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
        }
		
	}


	private void setEventListeners() {
        view.setAddEventListener(e -> openEventDialog());
    }
    
    private void openEventDialog() {
        eventDialog = new EventDialog(view, event -> {
            try {
                String eventName = eventDialog.getEventName();
                Date eventStartDate = eventDialog.getEventStartDate();
                Date eventEndDate = eventDialog.getEventEndDate();
                String eventStartTime = eventDialog.getEventStartTime();
                String eventEndTime = eventDialog.getEventEndTime();
                String host = eventDialog.getHost();
                String invitees = eventDialog.getInvitees();

                // Add event to the database
                database.addEvent(eventName, eventStartDate, eventEndDate, eventStartTime,eventEndTime, host, invitees); // Insert into the database
                // Refresh the view with the updated data
                updateModelFromDatabase(); 
            } catch (SQLException ex) {
                System.err.println("Error adding event: " + ex.getMessage());
            }

            eventDialog.dispose(); // Close the dialog
        });

        eventDialog.setVisible(true); // Show the dialog
    }
    
    public void updateModelFromDatabase() {
        try {
        	ResultSet resultSet;
        	if(this.userLoggedIn.equals("Admin")) {
        		resultSet = database.getAllEvents();
        	}
        	else {
        		resultSet = database.getUserEvents(this.userLoggedIn);
        	}
//            ResultSet resultSet = database.getAllEvents(); // Retrieve all events from the database
            List<Event> newEvents = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("EventName");
                Date eventStartDate = resultSet.getDate("EventStartDate");
                Date eventEndDate = resultSet.getDate("EventEndDate");
                String eventStartTime = resultSet.getString("EventStartTime");
                String eventEndTime = resultSet.getString("EventEndTime");
                String host = resultSet.getString("Host");
                String invitees = resultSet.getString("Invitees");
                newEvents.add(new Event(id,name,eventStartDate,eventEndDate,eventStartTime,eventEndTime,host,invitees));
            }

            // Update the Model's events list
            model.getEvents().clear(); // Clear existing events
            model.getEvents().addAll(newEvents); // Add new events
            view.updateViewFromModel(model); // Update the view with the loaded data
        } catch (SQLException ex) {
            System.err.println("Error retrieving events from database: " + ex.getMessage());
        }
    }

    
    private void printDataFromDB() {
            List<Event> events = model.getEvents(); // Get data from the Model
            // Print all events to the console
            System.out.println("PRINTING TABLE DETAILS");
            for (Event event : events) {
                System.out.println("Event Name: " + event.getEventName() +
                                   ", Event Start Date: " + event.getEventStartTime() +
                                   ", Event End Time: " + event.getEventEndTime());
            }
    
    }
    
    public void userSelectionChanged(String selectedUser) {
        updateScheduleForUser(selectedUser); // Fetch and update schedule for the selected user
    }
    
    private void updateScheduleForUser(String userName) {
        try {
            // Fetch events for the specific user
            List<Event> userEvents = eventDatabase.fetchEventsForUser(userName);

            // Update the model with the new list of events
            model.setEvents(userEvents);

            // Update the view with the new list of events
            view.updateWeeklySchedule(userEvents);

        } catch (SQLException ex) {
            System.err.println("Error fetching events: " + ex.getMessage());
        }
    }
    
    public void refreshSchedule() {
        if (view != null) {
            updateModelFromDatabase(); // Refresh the existing view
        }
    }


	public void logout() {
		view.setVisible(false);
		try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:my_database.db");

            // Create the model
            UserAuthenticationModel userModel = new UserAuthenticationModel(connection);

            // Create the views
            LoginView loginView = new LoginView();
            SignUpView signupView = new SignUpView();

            // Create the controller and connect views
            UserController userController = new UserController(userModel, loginView, signupView);

            // Show the login view (you could also show the signup view)
            loginView.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
    // Method to generate indentation
    private static String indent(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    // Method to generate a line of characters
    private static String line(int length, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ch);
        }
        sb.append("\n");
        return sb.toString();
    }
	
    public static void exportEventsToFile(List<Event> eventsList, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            
            int i=1;
            // Write each event to the file
            for (Event event : eventsList) {
                writer.write("Event No: " + i + "\n");
                writer.write("Event Name: " + event.getEventName() + "\n");
                writer.write("Start Date: " + dateFormat.format(event.getEventStartDate()) + "\n");
                writer.write("End Date: " + dateFormat.format(event.getEventEndDate()) + "\n");
                writer.write("Start Time: " + event.getEventStartTime() + "\n");
                writer.write("End Time: " + event.getEventEndTime() + "\n");
                writer.write("Host: " + event.getHost() + "\n");
                writer.write("Invitees: " + event.getInvitees() + "\n\n");
                i++;
            }
            
            writer.write("SCHEDULE IN TABLE FORMAT +\n\n");
            // Write header row
            writer.write("Event ID,Event Name,Start Date,End Date,Start Time,End Time,Host,Invitees\n");
            
            i=1;
            // Write each event to the file
            for (Event event : eventsList) {
                writer.write(indent(2));
                writer.write(i + ",");
                writer.write(event.getEventName() + ",");
                writer.write(dateFormat.format(event.getEventStartDate()) + ",");
                writer.write(dateFormat.format(event.getEventEndDate()) + ",");
                writer.write(event.getEventStartTime() + ",");
                writer.write(event.getEventEndTime() + ",");
                writer.write(event.getHost() + ",");
                writer.write(event.getInvitees() + "\n");
                writer.write(line(80, '-')); // Horizontal line separator
                i++;
            }

            System.out.println("Events exported to " + filename);
        } catch (IOException e) {
            System.err.println("Error exporting events to file: " + e.getMessage());
        }
    }


	public void printSchedule() {
		List<Event> eventsList = model.getEvents();
		exportEventsToFile(eventsList, "events.txt");
	}

}


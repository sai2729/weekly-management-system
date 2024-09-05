package Controller;

import java.sql.SQLException;
import java.util.Date;

import javax.swing.JSpinner;

import Model.EventDatabase;
import Model.UserDatabase;

public class EventController {
    private EventDatabase database;
    private UserDatabase userDatabase;
    
	public EventController() {
        try {
            database = new EventDatabase("my_database.db"); // Initialize the database
            userDatabase = new UserDatabase("my_database.db");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            return;
        }
	}
    
	
	public void updateEventDetails(int id, String name, Date startDate, Date endDate,
			String startTime, String endTime, String invitees) {
		try {
			database.updateEvent(id, name, startDate, endDate, startTime, endTime, invitees);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void deleteEvent(int id) {
		try {
			database.deleteEvent(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
    
}

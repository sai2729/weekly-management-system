package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDatabase {
    private Connection connection;

    public EventDatabase(String dbPath) throws SQLException {
        // Connect to the SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        createTableIfNotExists(); // Ensure the table exists
    }

    private void createTableIfNotExists() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS scheduled_events (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                EventName TEXT,
                EventDate DATE,
                EventTime DATE,
                Host TEXT,
                Invitees TEXT,
                FOREIGN KEY(Host) REFERENCES Users(UserId)
            )
        """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL); // Ensure table exists
        }
    }

    public void addEvent(String eventName, Date eventStartDate, Date eventEndDate, String eventStartTime,String eventEndTime, String host, String invitees) throws SQLException {
        String insertSQL = """
            INSERT INTO scheduled_events (EventName, EventStartDate, EventEndDate, EventStartTime, EventEndTime, Host, Invitees) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, eventName);
            preparedStatement.setDate(2, new java.sql.Date(eventStartDate.getTime()));
            preparedStatement.setDate(3, new java.sql.Date(eventEndDate.getTime()));
            preparedStatement.setString(4, eventStartTime);
            preparedStatement.setString(5, eventEndTime);
            preparedStatement.setString(6, host);
            preparedStatement.setString(7, invitees);
            preparedStatement.executeUpdate();
        }
    }
    

    public void updateEvent(int eventId, String eventName, Date eventStartDate, Date eventEndDate, String eventStartTime, String eventEndTime, String invitees) throws SQLException {
        String updateSQL = """
            UPDATE scheduled_events
            SET EventName = ?, EventStartDate = ?, EventEndDate = ?, EventStartTime = ?, EventEndTime = ?, Invitees = ?
            WHERE Id = ?
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            // Set new values for the record
            preparedStatement.setString(1, eventName);
            preparedStatement.setDate(2, new java.sql.Date(eventStartDate.getTime()));
            preparedStatement.setDate(3, new java.sql.Date(eventEndDate.getTime()));
            preparedStatement.setString(4, eventStartTime);
            preparedStatement.setString(5, eventEndTime);
            preparedStatement.setString(6, invitees);
            preparedStatement.setInt(7, eventId);

            preparedStatement.executeUpdate(); // Execute the update
        }
    }

    public ResultSet getAllEvents() throws SQLException {
        // Retrieve all events from the "events" table
        String query = "SELECT * FROM scheduled_events";
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
    
    
    public List<Event> fetchEventsForUser(String userName) throws SQLException {
        List<Event> userEvents = new ArrayList<>();
        String query = "SELECT * FROM scheduled_events WHERE Host = ?"; // Query to fetch events for a specific user

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName); // Set the user name in the query
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Fetch event details from the database
                    String eventName = resultSet.getString("EventName");
                    Date eventStartDate = resultSet.getDate("EventStartDate");
                    Date eventEndDate = resultSet.getDate("EventEndDate");
                    String eventStartTime = resultSet.getString("EventTime");
                    String eventEndTime = resultSet.getString("EventEndTime");
                    String host = resultSet.getString("Host");
                    String invitees = resultSet.getString("Invitees");

                    // Create and add an Event object to the list
                    userEvents.add(new Event(eventName, eventStartDate, eventEndDate , eventStartTime, eventEndTime, host, invitees));
                }
            }
        }

        return userEvents; // Return the list of events for the specific user
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

	public void deleteEvent(int id) throws SQLException{
		String deleteSQL = """
	            DELETE FROM scheduled_events WHERE Id = ?
	        """;

	        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
	            // Set new values for the record
	            preparedStatement.setInt(1, id);
	            preparedStatement.executeUpdate(); // Execute the update
	        }
	}

	public ResultSet getUserEvents(String userLoggedIn) throws SQLException {
		String query = "SELECT * FROM scheduled_events WHERE Host = ? OR Invitees LIKE ?";

		// Set the parameters for the prepared statement
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, userLoggedIn); // Check if the user is the host
		statement.setString(2, "%" + userLoggedIn + "%"); // Check if the user is in the invitees list
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}
}

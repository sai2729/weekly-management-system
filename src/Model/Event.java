package Model;

import java.util.Date;

public class Event {
    private final int id;
    private final String eventName;
    private final Date eventStartDate;
    private final Date eventEndDate;
    private final String eventStartTime;
    private final String eventEndTime;
    private final String host;
    private final String invitees;


    public Event(int id, String eventName, Date eventStartDate, Date eventEndDate, String eventStartTime, String eventEndTime, String host, String invitees) {
        this.id = id;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.host = host;
        this.invitees = invitees;
    }


    public Event(String eventName, Date eventStartDate,Date eventEndDate, String eventStartTime, String eventEndTime, String host, String invitees) {
        this(-1, eventName, eventStartDate,eventEndDate, eventStartTime, eventEndTime, host, invitees); // Default ID of -1 for new events
    }

    // Getters for the fields
    public int getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }
    
    public Date getEventEndDate() {
        return eventEndDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }
    
    public String getEventEndTime() {
        return eventEndTime;
    }

    public String getHost() {
        return host;
    }

    public String getInvitees() {
        return invitees;
    }
}

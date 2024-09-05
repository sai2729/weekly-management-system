package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleModel {
    private List<Event> events;
    private Calendar calendar;

    public ScheduleModel() {
        events = new ArrayList<>();
        calendar = new GregorianCalendar();
        setStartOfWeek();
    }

    private void setStartOfWeek() {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
    }


    public void removeEvent(String name) {
        events.removeIf(event -> event.getEventName().equals(name));
    }

    public List<Event> getEvents() {
        return events;
    }
    
    public void setEvents(List<Event> events) {
        this.events=events;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
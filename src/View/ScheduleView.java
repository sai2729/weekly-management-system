package View;

import javax.swing.*;
import javax.swing.text.View;

import Controller.ScheduleController;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import Model.Event;
import Model.ScheduleModel;
import Model.User;
import Model.UserDatabase;

public class ScheduleView extends JFrame {
    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_WEEK = 7;
    private JPanel weeklyPanel;
    private JLabel[] dayLabels;
    private JButton addEventButton, removeEventButton, editEventButton;
    private Calendar calendar;
    List<Event> events;
    List<User> usersDetails=null;
    String userLoggedIn = null;
    String presentUser = null;
    private JComboBox<String> userDropdown; // Dropdown list for users
    private ScheduleController controller; // Reference to controller
    
    public void setController(ScheduleController controller) {
        this.controller = controller; // Assign the controller
    }
    
    public ScheduleView(ScheduleModel model, List<String> userNames, String userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    	setTitle("Weekly Schedule");
        calendar = new GregorianCalendar();
        setStartOfWeek(calendar);
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE); // Set background color to white

        createNavigationPanel();
        createButtonPanel(); // Create the panel for buttons
        createWeeklyPanel(); // Create the schedule panel
        
        if(userLoggedIn.equals("Admin")) {
        	 createUserDropdown(userNames); // Create the user dropdown
        }
        
        pack();
        setLocationRelativeTo(null); // Center the frame
        setVisible(true); // Show the frame
    }
    
    public void createUserDropdown(List<String> userNames) {
        JPanel userPanel = new JPanel(new BorderLayout()); // Panel to hold the dropdown
        userDropdown = new JComboBox<>(userNames.toArray(new String[0])); // Convert to array
        userPanel.add(new JLabel("Select User:"), BorderLayout.WEST); // Label for dropdown
        userPanel.add(userDropdown, BorderLayout.CENTER); // Add the dropdown to the panel
        
        userDropdown.addActionListener(e -> onUserSelectionChanged()); // Listener for dropdown changes
        
        add(userPanel, BorderLayout.SOUTH); // Add to the bottom of the frame
    }
    
    private void onUserSelectionChanged() {
        // Get the selected user from the dropdown
        String selectedUser = (String) userDropdown.getSelectedItem();
        presentUser = selectedUser;
        filterEventsUserWise(selectedUser);
    }
    
    // Method to update the user dropdown with new user names
    public void updateUserDropdown(List<User> users,ScheduleModel model) {
    	usersDetails=users;
        userDropdown.removeAllItems(); // Clear existing items

        // Sort users by user names
        Collections.sort(users, Comparator.comparing(User::getUserName));

        // Add each user object to the dropdown
        for (User user : users) {
            userDropdown.addItem(user.getUserName()); // Add User object to the dropdown
        }
    }
    
    public void filterEventsUserWise(String userName) {
        List<Event> userEvents = new ArrayList<>();
        String userId = null;
        String[] invitees = null;
        for (User user : usersDetails) {
			if(user.getUserName()==userName) {
				userId=user.getUserId();
			}
		}
        for (Event event : events) {
        	String inviteesString = event.getInvitees();
        	if(event.getHost().equals(userId) || inviteesString.contains(userId) || userName.equals("All")) {
        		userEvents.add(event);
        	}
			
		}
        
        if(userEvents!=null)
        	updateWeeklySchedule(userEvents);
    	
    }
    
    private void createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new BorderLayout());
        JLabel monthYearLabel = new JLabel("", JLabel.CENTER);
        updateMonthYearLabel(monthYearLabel);

        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> {
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            updateWeekView();
            updateWeeklySchedule(events);
            updateMonthYearLabel(monthYearLabel);
        });

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            updateWeekView();
            updateWeeklySchedule(events);
            updateMonthYearLabel(monthYearLabel);
        });

        navigationPanel.add(prevButton, BorderLayout.WEST);
        navigationPanel.add(monthYearLabel, BorderLayout.CENTER);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        add(navigationPanel, BorderLayout.NORTH); // Navigation at the top
    }
    
    private void updateMonthYearLabel(JLabel label) {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        label.setText("<html><div style='text-align:center;'>" + 
                      "<span style='font-size:14px;'><b>" + month + " " + year + "</b></span>" +
                      "</div></html>");
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));

        // Add space above the Add Event button
        buttonPanel.add(Box.createVerticalGlue());

        addEventButton = new JButton("Add Event");
        buttonPanel.add(addEventButton);
        addEventButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the Add Event button

        // Add space between the Add Event button and the Logout button
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Add Print Schedule button
        JButton printScheduleButton = new JButton("Print Schedule");
        printScheduleButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the Print Schedule button
        printScheduleButton.addActionListener(e -> {
        	controller.printSchedule();
        });
        buttonPanel.add(printScheduleButton);
        
     // Add space between the Print Schedule button and the Logout button
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the Logout button
        logoutButton.addActionListener(e -> {
        	controller.logout();
        });
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.WEST); // Add button panel to the left
    }

    
    public void setAddEventListener(ActionListener listener) {
        addEventButton.addActionListener(listener);
    }

    public JButton getAddEventButton() {
        return addEventButton;
    }
    
    public void setDayLabels(List<String> labels) {
        for (int i = 0; i < dayLabels.length; i++) {
            dayLabels[i].setText(labels.get(i));
        }
    }


    private void createWeeklyPanel() {
        weeklyPanel = new JPanel(new GridLayout(HOURS_IN_DAY + 1, DAYS_IN_WEEK + 1));
        dayLabels = new JLabel[DAYS_IN_WEEK];
        weeklyPanel.setBackground(Color.WHITE);

        // Add the top row for dates
        weeklyPanel.add(new JLabel("")); // top-left corner empty label
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            dayLabels[i] = new JLabel("", JLabel.CENTER);
            weeklyPanel.add(dayLabels[i]);
        }
        updateWeekView();

        // Add rows for each hour
        for (int hour = 0; hour < HOURS_IN_DAY; hour++) {
            for (int day = 0; day <= DAYS_IN_WEEK; day++) {
                if (day == 0) {
                    String amPm = hour < 12 ? "AM" : "PM";
                    int displayHour = hour % 12;
                    if (displayHour == 0) displayHour = 12;
                    JLabel hourLabel = new JLabel(String.format("%2d %s", displayHour, amPm), JLabel.RIGHT);
                    hourLabel.setBorder(BorderFactory.createEmptyBorder(-25, 0, 0, 20));
                    weeklyPanel.add(hourLabel);
                } else {
                    // Add empty labels for schedule cells
                    JLabel label = new JLabel();
                    label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                    weeklyPanel.add(label);
                }
            }
        }

        weeklyPanel.setPreferredSize(new Dimension(1200, 1000));
        weeklyPanel.setBackground(Color.WHITE);
        add(weeklyPanel, BorderLayout.CENTER); // Weekly schedule in the center
    }
    
    public void updateViewFromModel(ScheduleModel model) {
        events = model.getEvents();
        updateWeeklySchedule(events);
    }
    
    public void updateWeeklySchedule(List<Event> events) {
    	String userColor = "GRAY";
        for (int hour = 1; hour <= HOURS_IN_DAY; hour++) {
            for (int day = 1; day <= DAYS_IN_WEEK; day++) {
                int index = (hour * (DAYS_IN_WEEK + 1)) + day;
                JLabel label = (JLabel) weeklyPanel.getComponent(index);
                Event event = findEvent(events, day, hour - 1);
                for (MouseListener ml : label.getMouseListeners()) {
                    label.removeMouseListener(ml);
                }
                if (event != null) {
                    label.setText(event.getEventName());
                    try {
						setLabelBackground(label, event, userColor);
					} catch (SQLException e) {
						e.printStackTrace();
					}
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (!label.getText().isEmpty()) {
                                EventDetailsPopup popup = new EventDetailsPopup(controller);
                                popup.showEventDetails(event);
                            }
                        }
                    });
                } else {
                    label.setText("");
                    label.setBackground(Color.WHITE);
                    label.setOpaque(true);
                }
            }
        }

        weeklyPanel.revalidate();
        weeklyPanel.repaint();
    }
    
    private void setLabelBackground(JLabel label, Event event, String defaultColor) throws SQLException {
        String userColor = defaultColor;
        if (usersDetails != null) {
            for (User user : usersDetails) {
                if (user.getUserId().equals(event.getHost())) {
                    userColor = user.getColor();
                    break;
                }
            }
        }
        else {
        	UserDatabase userDatabase = new UserDatabase("my_database.db");
        	User u = userDatabase.getUserDetails(this.userLoggedIn);
        	userColor = u.getColor();
        }

        switch (userColor) {
            case "GREEN":
                label.setBackground(Color.GREEN);
                break;
            case "ORANGE":
                label.setBackground(Color.ORANGE);
                break;
            case "CYAN":
                label.setBackground(Color.CYAN);
                break;
            default:
                label.setBackground(Color.LIGHT_GRAY);
                break;
        }

        label.setOpaque(true);
    }
    
    private Date getStartOfWeek() {
        Calendar startOfWeek = (Calendar) calendar.clone(); // Clone the current calendar
        startOfWeek.set(Calendar.DAY_OF_WEEK, startOfWeek.getFirstDayOfWeek()); // Set to the start of the week
        return startOfWeek.getTime(); // Convert Calendar to Date
    }

    private Date getEndOfWeek() {
        Calendar endOfWeek = (Calendar) calendar.clone(); // Clone the current calendar
        endOfWeek.set(Calendar.DAY_OF_WEEK, endOfWeek.getFirstDayOfWeek()); // Set to the start of the week
        endOfWeek.add(Calendar.DATE, DAYS_IN_WEEK - 1); // Add to get to the end of the week
        return endOfWeek.getTime(); // Convert Calendar to Date
    }



    
    private boolean isDateInCurrentWeek(Date eventDate) {
    	Date startOfWeek = getStartOfWeek();
    	Date endOfWeek = getEndOfWeek();
    	Calendar calendar = Calendar.getInstance();
        
        // Get day, month, and year for start of the week
        calendar.setTime(startOfWeek);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int startMonth = calendar.get(Calendar.MONTH);
        int startYear = calendar.get(Calendar.YEAR);

        // Get day, month, and year for end of the week
        calendar.setTime(endOfWeek);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endMonth = calendar.get(Calendar.MONTH);
        int endYear = calendar.get(Calendar.YEAR);

        // Get day, month, and year for the event date
        calendar.setTime(eventDate);
        int eventDay = calendar.get(Calendar.DAY_OF_MONTH);
        int eventMonth = calendar.get(Calendar.MONTH);
        int eventYear = calendar.get(Calendar.YEAR);

        // Check if event is within the week, considering the year, month, and day
        boolean afterOrEqualStart = (eventYear > startYear)
            || (eventYear == startYear && eventMonth > startMonth)
            || (eventYear == startYear && eventMonth == startMonth && eventDay >= startDay);

        boolean beforeOrEqualEnd = (eventYear < endYear)
            || (eventYear == endYear && eventMonth < endMonth)
            || (eventYear == endYear && eventMonth == endMonth && eventDay <= endDay);

        return afterOrEqualStart && beforeOrEqualEnd;
    	
    }



    private Event findEvent(List<Event> events, int targetDay, int targetHour) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("u"); // Day of week as a number (1=Monday, 7=Sunday)
        SimpleDateFormat hourFormat = new SimpleDateFormat("H"); // 24-hour format hour

        if (events.size() > 0) {
            for (Event event : events) {
                if (isDateInCurrentWeek(event.getEventStartDate())) {
                    // Convert to your expected day format
                    int eventStartDay = Integer.parseInt(dayFormat.format(event.getEventStartDate()));
                    eventStartDay = (eventStartDay % 7) + 1; // Adjusting to align with 1-based index

                    int eventEndDay = Integer.parseInt(dayFormat.format(event.getEventEndDate()));
                    eventEndDay = (eventEndDay % 7) + 1; // Adjusting to align with 1-based index

                    // Get the event's starting hour
                    int eventStartingHour = Integer.parseInt(event.getEventStartTime().split(":")[0]);
                    int eventEndingHour = Integer.parseInt(event.getEventEndTime().split(":")[0]);

                    // Handle the case where the event spans from the end of a week to the beginning of the next week
                    if ((eventStartDay <= eventEndDay && targetDay >= eventStartDay && targetDay <= eventEndDay) ||
                        (eventStartDay > eventEndDay && (targetDay >= eventStartDay || targetDay <= eventEndDay))) {

                        // EVENT START AND END IN SAME DAY
                        if (eventStartDay == eventEndDay && targetHour >= eventStartingHour && targetHour < eventEndingHour) {
                            return event;
                        }
                        // EVENTS ACROSS MULTIPLE DAYS
                        else if (eventStartDay != eventEndDay) {
                            // Starting Day
                            if (targetDay == eventStartDay && targetHour >= eventStartingHour) {
                                return event;
                            }
                            // Ending Day
                            else if (targetDay == eventEndDay && targetHour < eventEndingHour) {
                                return event;
                            }
                            // Middle Days
                            else if (targetDay > eventStartDay && targetDay < eventEndDay) {
                                return event;
                            }
                            // Event spanning across midnight
                            else if (eventStartDay > eventEndDay && (targetDay == eventStartDay || targetDay == eventEndDay)) {
                                return event;
                            }
                        }
                    }
                }
            }
        }

        return null; // No matching event found
    }
    
    
    private void updateWeekView() {
        Calendar currentDay = (Calendar) calendar.clone();
        // Ensure that the calendar starts at the beginning of the week
        setStartOfWeek(currentDay);

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            String dayName = getDayOfWeek(currentDay.get(Calendar.DAY_OF_WEEK));
            String dayOfMonth = Integer.toString(currentDay.get(Calendar.DAY_OF_MONTH));
            dayLabels[i].setText(dayName + " " + dayOfMonth); // Set the label text to "Day Date"
            currentDay.add(Calendar.DATE, 1);
        }
    }
    
    private void setStartOfWeek(Calendar calendar) {
        // Set the calendar to the first day of the current week (Sunday)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
    }

    private String getDayOfWeek(int day) {
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        return days[day - Calendar.SUNDAY];
    }
}

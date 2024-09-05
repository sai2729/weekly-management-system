package View;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import Controller.EventController;
import Controller.ScheduleController;
import Model.Event;
import Model.EventDatabase;
import Model.ScheduleModel;

public class EventDetailsPopup {
	private EventDatabase database;
	private EventController controller = new EventController();
	private ScheduleController schedController;
	
    public EventDetailsPopup(ScheduleController controller) {
        this.schedController = controller; // Store the reference to the controller
    }
	
    // Function to create a JDialog that shows event details
    void showEventDetails(Event event) {
        JDialog eventDetailsDialog = new JDialog(); // Create a new dialog
        eventDetailsDialog.setTitle("Event Details");
        eventDetailsDialog.setSize(250, 400);
        eventDetailsDialog.setLayout(new BorderLayout());
        eventDetailsDialog.setModal(true); 

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); 

        // Event Name
        JPanel eventNamePanel = new JPanel();
        eventNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel eventNameLabel = new JLabel("Event Name:");
        JTextField eventNameField = new JTextField(event.getEventName(), 20);
        eventNamePanel.add(eventNameLabel);
        eventNamePanel.add(eventNameField);
        
        // Event Start Date
        JPanel startDatePanel = new JPanel();
        startDatePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel eventStartDateLabel = new JLabel("Event Start Date:");
        JSpinner eventStartDateSpinner = new JSpinner(
        		new SpinnerDateModel(
        				event.getEventStartDate(), // default Value
        				event.getEventStartDate(), // Minimum Value - cannot move event to past
        				null, // Maximum Value
        				Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(eventStartDateSpinner, "yyyy-MM-dd");
        eventStartDateSpinner.setEditor(startDateEditor);
        startDatePanel.add(eventStartDateLabel);
        startDatePanel.add(eventStartDateSpinner);
        
        // Event End Date
        JPanel endDatePanel = new JPanel();
        endDatePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel eventEndDateLabel = new JLabel("Event End Date:");
        JSpinner eventEndDateSpinner = new JSpinner(
        		new SpinnerDateModel(
        				event.getEventEndDate(),
        				event.getEventEndDate(),
        				null,
        				Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(eventEndDateSpinner, "yyyy-MM-dd");
        eventEndDateSpinner.setEditor(endDateEditor);
        endDatePanel.add(eventEndDateLabel);
        endDatePanel.add(eventEndDateSpinner);

        // Event Start Time
        JPanel eventStartTimePanel = new JPanel();
        eventStartTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel eventStartTimeLabel = new JLabel("Event StartTime:");
        JTextField eventStartTimeField = new JTextField(event.getEventStartTime(), 20);
        eventStartTimePanel.add(eventStartTimeLabel);
        eventStartTimePanel.add(eventStartTimeField);

        // Event End Time
        JPanel eventEndTimePanel = new JPanel();
        eventEndTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel eventEndTimeLabel = new JLabel("Event EndTime:");
        JTextField eventEndTimeField = new JTextField(event.getEventEndTime(), 20);
        eventEndTimePanel.add(eventEndTimeLabel);
        eventEndTimePanel.add(eventEndTimeField);

        // Invitees
        JPanel inviteesPanel = new JPanel();
        inviteesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel inviteesLabel = new JLabel("Invitees:");
        JTextArea inviteesTextArea = new JTextArea(event.getInvitees(), 3, 20);
        JScrollPane scrollPane = new JScrollPane(inviteesTextArea);
        inviteesPanel.add(inviteesLabel);
        inviteesPanel.add(scrollPane);

        // Add all panels to the content panel
        contentPanel.add(eventNamePanel);
        contentPanel.add(startDatePanel);
        contentPanel.add(endDatePanel);
        contentPanel.add(eventStartTimePanel);
        contentPanel.add(eventEndTimePanel);
        contentPanel.add(inviteesPanel);

        // Buttons for Edit and Delete
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete Event");

        editButton.addActionListener(e -> {
            // Action for Edit button (e.g., make fields editable)
        	Date startDate = null;
        	Date endDate = null;
            if (eventStartDateSpinner.getModel() instanceof SpinnerDateModel &&
                    eventEndDateSpinner.getModel() instanceof SpinnerDateModel) {

                    startDate = (Date) eventStartDateSpinner.getValue(); // Cast to Date
                    endDate = (Date) eventEndDateSpinner.getValue(); // Cast to Date
            }
        	
        	controller.updateEventDetails(event.getId(),eventNameField.getText(), startDate, endDate , eventStartTimeField.getText(), eventEndTimeField.getText(), inviteesTextArea.getText());
            JOptionPane.showMessageDialog(eventDetailsDialog, "Edited Successfully.");
            eventDetailsDialog.dispose();
            if (controller != null) {
                schedController.refreshSchedule(); // Refresh the existing schedule
            }
        });

        deleteButton.addActionListener(e -> {
            // Action for Delete button (e.g., remove the event)
        	controller.deleteEvent(event.getId());
            JOptionPane.showMessageDialog(eventDetailsDialog, "Event deleted.");
            eventDetailsDialog.dispose();
            if (controller != null) {
                schedController.refreshSchedule(); // Refresh the existing schedule
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add panels to the dialog
        eventDetailsDialog.add(contentPanel, BorderLayout.CENTER);
        eventDetailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        eventDetailsDialog.setVisible(true); // Show the dialog
    }
}

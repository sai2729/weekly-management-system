package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventDialog extends JDialog {
    private JTextField eventNameField;
    private JTextField hostField;
    private JTextArea inviteesField;
    private JSpinner eventStartDateSpinner;
    private JSpinner eventEndDateSpinner;
    private JSpinner eventStartTimeSpinner;
    private JSpinner eventEndTimeSpinner;

    public EventDialog(JFrame parent, ActionListener submitListener) {
        super(parent, "Add Event", true); // Modal dialog
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2)); // 5 rows and 2 columns

        // Event Name
        formPanel.add(new JLabel("Event Name:"));
        eventNameField = new JTextField(20);
        formPanel.add(eventNameField);

        // Event Start Date
        formPanel.add(new JLabel("Event Start Date:"));
        eventStartDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(eventStartDateSpinner, "yyyy-MM-dd");
        eventStartDateSpinner.setEditor(startDateEditor);
        formPanel.add(eventStartDateSpinner);
        
        // Event End Date
        formPanel.add(new JLabel("Event Date:"));
        eventEndDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(eventEndDateSpinner, "yyyy-MM-dd");
        eventEndDateSpinner.setEditor(endDateEditor);
        formPanel.add(eventEndDateSpinner);

        // Event Start Time
        formPanel.add(new JLabel("Event Start Time:"));
        eventStartTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(eventStartTimeSpinner, "HH:mm");
        eventStartTimeSpinner.setEditor(startTimeEditor);
        formPanel.add(eventStartTimeSpinner);
        
        // Event End Time
        formPanel.add(new JLabel("Event End Time:"));
        eventEndTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(eventEndTimeSpinner, "HH:mm");
        eventEndTimeSpinner.setEditor(endTimeEditor);
        formPanel.add(eventEndTimeSpinner);

        // Host
        formPanel.add(new JLabel("Host:"));
        hostField = new JTextField(20);
        formPanel.add(hostField);

        // Invitees
        formPanel.add(new JLabel("Invitees:"));
        inviteesField = new JTextArea(3, 20); // Multi-line input for invitees
        formPanel.add(new JScrollPane(inviteesField)); // Scroll pane to allow scrolling

        add(formPanel, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(submitListener);
        add(submitButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent); // Center the dialog relative to the parent frame
    }

    // Getters for field values
    public String getEventName() {
        return eventNameField.getText();
    }

    public Date getEventStartDate() {
        return ((SpinnerDateModel) eventStartDateSpinner.getModel()).getDate();
    }
    
    public Date getEventEndDate() {
        return ((SpinnerDateModel) eventEndDateSpinner.getModel()).getDate();
    }

    public String getEventStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(eventStartTimeSpinner.getValue());
    }
    
    public String getEventEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(eventEndTimeSpinner.getValue());
    }

    public String getHost() {
        return hostField.getText();
    }

    public String getInvitees() {
        return inviteesField.getText();
    }
}


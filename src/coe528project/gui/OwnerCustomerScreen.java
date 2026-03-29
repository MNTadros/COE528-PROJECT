//package coe528project;

import coe528project.model.BookStore;
import coe528project.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class OwnerCustomerScreen extends JPanel implements ActionListener {
    private JTable customersTable;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    private BookStoreApp app;
    private BookStore store;

    public OwnerCustomerScreen(BookStoreApp app, BookStore store) {
        this.app = app;
        this.store = store;

        // Split the screen into top (North), middle (Center), and bottom (South) sections
        setLayout(new BorderLayout(12, 12));
        
        // Add a 12-pixel invisible border around the entire screen so things don't touch the edges
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Define the titles for the three columns in our table
        String[] columns = {"Username", "Password", "Points"};
        
        // Create the rules for how the table works
        DefaultTableModel initialModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Make the entire table "Read-Only". 
                // This stops people from double-clicking and changing the text directly on the screen.
                return false;
            }
        };
        
        // Create the actual table using the Read-Only rules we just set up
        customersTable = new JTable(initialModel);
        
        // Make sure the table background fills the whole area, even if there are only a few customers
        customersTable.setFillsViewportHeight(true);
        
        // Put the table inside a scrolling box so we can see the column titles and scroll down
        JScrollPane tableScroll = new JScrollPane(customersTable);
        
        // Set the height of the scrolling box to exactly 160 pixels
        tableScroll.setPreferredSize(new Dimension(0, 160));

        // Create a container for the top of the screen and arrange items left-to-right with some spacing
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        
        // Add a little bit of invisible padding (8 pixels) to the bottom of this top container
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        // Create a text box for the username that is 12 characters wide
        usernameField = new JTextField(12);
        
        // Create a text box for the password that is also 12 characters wide
        passwordField = new JTextField(12);
        
        // Create the button that says "Add"
        addButton = new JButton("Add");
        
        // Connect the "Add" button so the app listens for when it gets clicked
        addButton.addActionListener(this);

        // Add the text label "Username:" to the top container
        topPanel.add(new JLabel("Username:"));
        
        // Put the empty username text box right next to it
        topPanel.add(usernameField);
        
        // Add the text label "Password:" next in line
        topPanel.add(new JLabel("Password:"));
        
        // Put the empty password text box right next to that
        topPanel.add(passwordField);
        
        // Finally, stick the "Add" button at the end of the row
        topPanel.add(addButton);
        
        // Put this entire top row of inputs to the top (NORTH) edge of the main screen
        add(topPanel, BorderLayout.NORTH);

        // Put the scrolling table in the middle (CENTER) of the main screen so it takes up the rest of the space
        add(tableScroll, BorderLayout.CENTER);

        // Create a container for the bottom of the screen, arranging items left-to-right
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        
        // Add some invisible padding (8 pixels) to the top of this bottom container so it doesn't hug the table
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        // Create the "Delete" and "Back" buttons
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");
        
        // Connect both buttons so the app listens for their clicks
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);

        // Add the buttons to the bottom container
        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        
        // Lock this entire bottom row of buttons to the bottom (SOUTH) edge of the main screen
        add(bottomPanel, BorderLayout.SOUTH);

        // Load any existing customers into the table right when the screen opens
        refreshTable();
    }

    public void addCustomer() {
        // Get the text the user typed into the username box and remove accidental spaces at the ends
        String username = usernameField.getText().trim();
        
        // Get the text the user typed into the password box and remove accidental spaces
        String password = passwordField.getText().trim();
        
        // Check to make sure the boxes aren't completely empty before trying to save
        if (!username.isEmpty() && !password.isEmpty()) {
            
            // Create a new Customer object (starting them with 0 points) and save it to the main storage list
            store.addCustomer(new Customer(username, password, 0));
            
            // Empty out the username and password text boxes so they are clean for the next entry
            usernameField.setText("");
            passwordField.setText("");
            
            // Redraw the visual table so the new customer immediately shows up on the screen
            refreshTable();
        }
    }

    public void deleteCustomer() {
        // Find exactly which row number the user clicked on in the table
        int selectedRow = customersTable.getSelectedRow();
        
        // If they actually clicked a row (0 or higher), do the delete logic.
        if (selectedRow >= 0) {
            
            // Get the specific Customer object from the backend storage using that row number
            Customer cToRemove = store.getCustomers().get(selectedRow);
            
            // Tell the main storage to permanently remove that specific customer
            store.removeCustomer(cToRemove);
            
            // Redraw the visual table so the deleted customer disappears from the screen
            refreshTable();
        }
    }

    // A helper method to wipe the table clean and redraw it perfectly using the newest list of customers
    public void refreshTable() {
        // Get the rules/model that control the visual table
        DefaultTableModel model = (DefaultTableModel) customersTable.getModel();
        
        // Erase every single row currently visible on the screen
        model.setRowCount(0);
        
        // Loop through the actual backend list of customers one by one
        for (Customer c : store.getCustomers()) {
            // Add a fresh row to the screen for each customer, showing their username, password, and points
            model.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()});
        }
    }

    // The "Brain" of the screen that decides what to do when ANY connected button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the click came from the "Add" button
        if (e.getSource() == addButton) {
            // Run the addCustomer() code we wrote above
            addCustomer();
            
        // Check if the click came from the "Delete" button
        } else if (e.getSource() == deleteButton) {
            // Run the deleteCustomer() code we wrote above
            deleteCustomer();
            
        // Check if the click came from the "Back" button
        } else if (e.getSource() == backButton) {
            // Leave this screen and swap back to the Owner Start Screen
            app.showPanel(new OwnerStartScreen(app, store));
        }
    }
}
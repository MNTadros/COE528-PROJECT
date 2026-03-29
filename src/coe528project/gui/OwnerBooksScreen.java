//package coe528project;

import coe528project.model.Book;
import coe528project.model.BookStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class OwnerBooksScreen extends JPanel implements ActionListener {
    
    private JTable booksTable;
    private JTextField nameField;
    private JTextField priceField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    private BookStoreApp app;
    private BookStore store;

    public OwnerBooksScreen(BookStoreApp app, BookStore store) {
        this.app = app;
        this.store = store;

        // Split the screen into top, middle, and bottom sections
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] columns = {"Book Name", "Book Price"};
        
        // Make rules for how the table works
        DefaultTableModel initialModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
               // Make the entire table "Read-Only". 
                // This stops users from double-clicking a cell to type over the text, 
                // forcing them to use our proper Add and Delete buttons instead.
                return false;
            }
        };
    // Create the actual table using the Read-Only rules we set up earlier
    booksTable = new JTable(initialModel);
        
    // Make the table background fill the whole empty space below it
    booksTable.setFillsViewportHeight(true);
    
    // Put the table inside a scrolling box so we can see the column titles and scroll down
    JScrollPane tableScroll = new JScrollPane(booksTable);
    
    // Set the height of the scrolling box to exactly 160 pixels
    tableScroll.setPreferredSize(new Dimension(0, 160));

    // Create a container for the top of the screen and arrange items left-to-right with some spacing
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
    
    // Add a little bit of invisible padding (8 pixels) to the bottom of this top container
    topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
    
    // Create a text box for the book name that is 12 characters wide
    nameField = new JTextField(12);
    
    // Create a smaller text box for the price that is 6 characters wide
    priceField = new JTextField(6);
    
    // Create the button that says "Add"
    addButton = new JButton("Add");
    
    // Connect the "Add" button so the app listens for when it gets clicked
    addButton.addActionListener(this);

    // Add the text label "Name:" to the top container
    topPanel.add(new JLabel("Name:"));
    
    // Put the empty name text box right next to it
    topPanel.add(nameField);
    
    // Add the text label "Price:" next in line
    topPanel.add(new JLabel("Price:"));
    
    // Put the empty price text box right next to that
    topPanel.add(priceField);
    
    // Finally, stick the "Add" button at the end of the row
    topPanel.add(addButton);
    
    // Lock this entire top row of inputs to the top (NORTH) edge of the main screen
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

    // Load any existing books into the table right when the screen opens
    refreshTable();
}

public void addBook() {
    // Get the text the user typed into the Name box and remove any accidental spaces at the ends
    String name = nameField.getText().trim();
    
    try {
        // Try to convert the text they typed into the Price box into a real number with decimals
        double price = Double.parseDouble(priceField.getText().trim());
        
        // If it is a valid number, create a new Book and save it to the main storage list
        store.addBook(new Book(name, price));
        
        // Empty out the Name and Price text boxes so they are clean for the next book
        nameField.setText("");
        priceField.setText("");
        
        // Redraw the visual table so the new book immediately shows up on the screen
        refreshTable();
        
    } catch (NumberFormatException ex) {
        // If they typed letters instead of numbers for the price, catch the error so the app doesn't crash.
        // Pop up a warning message telling them to fix it.
        JOptionPane.showMessageDialog(this, "Invalid price.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public void deleteBook() {
    // Find exactly which row number the user clicked on in the table
    int selectedRow = booksTable.getSelectedRow();
    
    // If they actually clicked a row (0 or higher), do the delete logic.
    // If they didn't click anything, it returns -1 and this code just skips safely.
    if (selectedRow >= 0) { 
        // Get the specific Book object from the backend storage using that row number
        Book bookToRemove = store.getBooks().get(selectedRow);
        
        // Tell the main storage to permanently remove that specific book
        store.removeBook(bookToRemove);
        
        // Redraw the visual table so the deleted book disappears from the screen
        refreshTable();
    }
}

// A helper method to wipe the table clean and redraw it perfectly using the newest list of books
public void refreshTable() {
    // Get the rules/model that control the visual table
    DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
    
    // Erase every single row currently visible on the screen
    model.setRowCount(0); 
    
    // Loop through the actual backend list of books one by one
    for (Book b : store.getBooks()) {
        // Add a fresh row to the screen for each book, showing its name and price
        model.addRow(new Object[]{b.getName(), b.getPrice()});
    }
}

// The "Brain" of the screen that decides what to do when ANY connected button is clicked
@Override
public void actionPerformed(ActionEvent e) {
    // Check if the click came from the "Add" button
    if (e.getSource() == addButton) {
        // Run the addBook() code we wrote above
        addBook();
        
    // Check if the click came from the "Delete" button
    } else if (e.getSource() == deleteButton) {
        // Run the deleteBook() code we wrote above
        deleteBook();
        
    // Check if the click came from the "Back" button
    } else if (e.getSource() == backButton) {
        // Leave this screen and swap back to the Owner Start Screen
        app.showPanel(new OwnerStartScreen(app, store));
    }
}
}
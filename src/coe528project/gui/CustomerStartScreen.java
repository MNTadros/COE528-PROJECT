//package coe528project;

import coe528project.model.Book;
import coe528project.model.BookStore;
import coe528project.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CustomerStartScreen extends JPanel implements ActionListener {
    
    private JTable booksTable;
    private JButton buyButton;
    private JButton redeemBuyButton;
    private JButton logoutButton;
    private BookStoreApp app;
    private BookStore store;
    private Customer customer;

    public CustomerStartScreen(BookStoreApp app, BookStore store, Customer customer) {
        this.app = app;
        this.store = store;
        this.customer = customer;

        // Split the screen into areas (North, Center, South) and add some spacing between them
        setLayout(new BorderLayout(8, 8));
        
        // Add a 10-pixel invisible border around the edges of the whole screen
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Requirement: Show a welcome message with Name, Points, and Status
        // Build the text message using the customer's actual data
        JLabel welcomeLabel = new JLabel(
                "Welcome " + customer.getUsername()
                        + ".  You have " + customer.getPoints()
                        + " points.  Your status is " + customer.getStatus(),
                SwingConstants.CENTER); // Center the text on the screen
                
        // Make the welcome text bold so it stands out
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD));
        
        // Stick the welcome message to the very top (NORTH) of the screen
        add(welcomeLabel, BorderLayout.NORTH);

        // Define the titles for the three columns in the shopping table
        String[] columns = {"Book Name", "Book Price", "Select"};
        
        // Create the rules for the table
        DefaultTableModel initialModel = new DefaultTableModel(columns, 0) {

            // special rule for the third column (the "Select" column).
            // Tell Java that the third column contains True or False (Boolean) values instead of text.
            // Because it's True/False, Java will automatically draw a clickable checkbox!
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }

            // This rule decides which boxes the user is allowed to click on.
            @Override
            public boolean isCellEditable(int row, int col) {
                // If it's the 3rd column (index 2), let them click it so they can check the box.
                if (col == 2) {
                    return true;
                } else {
                    // Lock the Name and Price columns so they can't change the book details.
                    return false;
                }
            }
        };
        
        // Create the actual table using those rules
        booksTable = new JTable(initialModel);
        
        // Make the checkbox column skinny (maximum 60 pixels wide) so it looks neat
        booksTable.getColumnModel().getColumn(2).setMaxWidth(60);
        
        // Put the table inside a scrolling box and stick it in the middle (CENTER) of the screen
        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        // Create a container for the buttons at the bottom of the screen
        JPanel bottomPanel = new JPanel(new FlowLayout());
        
        // Create the three buttons the customer needs
        buyButton = new JButton("Buy");
        redeemBuyButton = new JButton("Redeem points and Buy");
        logoutButton = new JButton("Logout");
        
        // Connect all three buttons so the app listens for when they get clicked
        buyButton.addActionListener(this);
        redeemBuyButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Add the buttons to the bottom container side-by-side
        bottomPanel.add(buyButton);
        bottomPanel.add(redeemBuyButton);
        bottomPanel.add(logoutButton);
        
        // Stick the button container to the very bottom (SOUTH) of the screen
        add(bottomPanel, BorderLayout.SOUTH);

        // Get the rules/model that control the visual table
        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        
        // Erase any rows that might be there
        model.setRowCount(0);
        
        // Loop through the main list of books in the store
        for (Book b : store.getBooks()) {
            // Add a row for each book showing its name, its price, and a blank checkbox (Boolean.FALSE)
            model.addRow(new Object[]{b.getName(), String.format("%.2f", b.getPrice()), Boolean.FALSE});
        }
    }

    // A helper method that checks the whole table to figure out which books the user wants to buy
    public ArrayList<Book> getSelectedBooks(boolean redeem) {
        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        
        // Create an empty shopping cart
        ArrayList<Book> selected = new ArrayList<>();
        
        // Get the master list of books from the store
        ArrayList<Book> storeBooks = store.getBooks();

        // Loop through every single row in the table, one by one
        for (int i = 0; i < model.getRowCount(); i++) {
            
            // Look at the checkbox in the 3rd column (index 2) of the current row
            Boolean checked = (Boolean) model.getValueAt(i, 2);
            
            // If the box is checked (True), grab that book from the master list and put it in the shopping cart
            if (Boolean.TRUE.equals(checked)) {
                selected.add(storeBooks.get(i));
            }
        }
        
        // Return the filled shopping cart
        return selected;
    }

    public void handleBuy(boolean redeem) {
        // Run the method above to get the shopping cart full of checked books
        ArrayList<Book> selectedBooks = getSelectedBooks(redeem);
        
        // If the cart is empty, pop up an error message and stop the buying process
        if (selectedBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one book.");
            return;
        }

        // A variable to hold the total cost of the purchase
        double finalTC;
        
        // Check which Buy button they clicked
        if (redeem) {
            // If they clicked "Redeem and Buy", run the discount math in the backend
            finalTC = customer.redeemAndBuy(selectedBooks);
        } else {
            // If they clicked regular "Buy", run the normal math in the backend
            finalTC = customer.buyBooks(selectedBooks);
        }

        // Leave this screen and go to the final Receipt/Cost screen, passing the final price forward
        app.showPanel(new CustomerCostScreen(app, store, customer, finalTC));
    }

    // The "Brain" of the screen that decides what to do when ANY connected button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        // If they clicked the normal "Buy" button, trigger the buying process with redeem set to false
        if (e.getSource() == buyButton) {
            handleBuy(false);
            
        // If they clicked "Redeem points and Buy", trigger the buying process with redeem set to true
        } else if (e.getSource() == redeemBuyButton) {
            handleBuy(true);
            
        // If they clicked "Logout", send them all the way back to the main Login Screen
        } else if (e.getSource() == logoutButton) {
            app.showPanel(new LoginScreen(app, store));
        }
    }
}
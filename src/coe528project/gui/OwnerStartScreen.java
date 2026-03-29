//package coe528project;

import coe528project.model.BookStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OwnerStartScreen extends JPanel implements ActionListener {
    private JButton booksButton;
    private JButton customerButton;
    private JButton logoutButton;
    private BookStoreApp app;
    private BookStore store;

    public OwnerStartScreen(BookStoreApp app, BookStore store) {
        this.app = app;
        this.store = store;

        // Put the buttons in a straight line going down the middle
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Requirement: The owner-start-screen has three buttons: [Books], [Customers], [Logout]
        booksButton = new JButton("Books");
        customerButton = new JButton("Customers");
        logoutButton = new JButton("Logout");

        // Make the buttons do smth when u click them
        booksButton.addActionListener(this);
        customerButton.addActionListener(this);
        logoutButton.addActionListener(this);

        gbc.gridx = 0; gbc.gridy = 0; add(booksButton, gbc);
        gbc.gridy = 1; add(customerButton, gbc);
        gbc.gridy = 2; add(logoutButton, gbc);
    }

    // Figure out which button was clicked and go to the right screen
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == booksButton) {
            app.showPanel(new OwnerBooksScreen(app, store));
        } else if (e.getSource() == customerButton) {
            app.showPanel(new OwnerCustomerScreen(app, store));
        } else if (e.getSource() == logoutButton) {
            // Requirement: When the owner clicks [Logout],  should be taken back to the login-screen
            app.showPanel(new LoginScreen(app, store));
        }
    }
}
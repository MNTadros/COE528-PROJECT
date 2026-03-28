package coe528project.gui;

import coe528project.model.BookStore;
import coe528project.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerCostScreen extends JPanel implements ActionListener {
    private JLabel totalCostLabel;
    private JLabel pointsLabel;
    private JLabel statusLabel;
    private JButton logoutButton;
    private BookStoreApp app;
    private BookStore store;

    // customer and finalTC passed in to fulfill the required logic display
    public CustomerCostScreen(BookStoreApp app, BookStore store, Customer customer, double finalTC) {
        this.app = app;
        this.store = store;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Requirement: The top item is the message Total Cost: TC
        totalCostLabel = new JLabel(String.format("Total Cost: $%.2f", finalTC));
        totalCostLabel.setFont(totalCostLabel.getFont().deriveFont(Font.BOLD, 16f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(totalCostLabel, gbc);

        // Requirement: middle item is the message Points: P, Status: S
        pointsLabel = new JLabel("Points: " + customer.getPoints() + ",  Status: " + customer.getStatus());
        pointsLabel.setFont(pointsLabel.getFont().deriveFont(14f));
        gbc.gridy = 1;
        add(pointsLabel, gbc);

        statusLabel = new JLabel("Thank you for your purchase!");
        gbc.gridy = 2;
        add(statusLabel, gbc);

        // Requirement: The bottom item is a [Logout] button
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        gbc.gridy = 3;
        add(logoutButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            app.showPanel(new LoginScreen(app, store));
        }
    }
}

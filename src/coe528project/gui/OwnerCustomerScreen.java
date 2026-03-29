package coe528project.gui;

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

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Create the model locally just to initialize the table
        String[] columns = {"Username", "Password", "Points"};
        DefaultTableModel initialModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        customersTable = new JTable(initialModel);
        customersTable.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(customersTable);
        tableScroll.setPreferredSize(new Dimension(0, 160));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        usernameField = new JTextField(12);
        passwordField = new JTextField(12);
        addButton = new JButton("Add");
        addButton.addActionListener(this);

        topPanel.add(new JLabel("Username:"));
        topPanel.add(usernameField);
        topPanel.add(new JLabel("Password:"));
        topPanel.add(passwordField);
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        add(tableScroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);

        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    public void addCustomer() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (!username.isEmpty() && !password.isEmpty()) {
            store.addCustomer(new Customer(username, password, 0));
            usernameField.setText("");
            passwordField.setText("");
            refreshTable();
        }
    }

    public void deleteCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow >= 0) {
            Customer cToRemove = store.getCustomers().get(selectedRow);
            store.removeCustomer(cToRemove);
            refreshTable();
        }
    }

    public void refreshTable() {
        // Fetch the model dynamically here
        DefaultTableModel model = (DefaultTableModel) customersTable.getModel();
        model.setRowCount(0);
        for (Customer c : store.getCustomers()) {
            model.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addCustomer();
        } else if (e.getSource() == deleteButton) {
            deleteCustomer();
        } else if (e.getSource() == backButton) {
            app.showPanel(new OwnerStartScreen(app, store));
        }
    }
}

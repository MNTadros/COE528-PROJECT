package coe528project.gui;

import coe528project.model.Book;
import coe528project.model.BookStore;
import coe528project.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CustomerStartScreen extends JPanel implements ActionListener {
    // Attributes exactly match the UML
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

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel(
                "Welcome " + customer.getUsername()
                        + ".  You have " + customer.getPoints()
                        + " points.  Your status is " + customer.getStatus(),
                SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD));
        add(welcomeLabel, BorderLayout.NORTH);

        String[] columns = {"Book Name", "Book Price", "Select"};
        DefaultTableModel initialModel = new DefaultTableModel(columns, 0) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 2) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        booksTable = new JTable(initialModel);
        booksTable.getColumnModel().getColumn(2).setMaxWidth(60);
        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        buyButton = new JButton("Buy");
        buyButton.addActionListener(this);
        redeemBuyButton = new JButton("Redeem points and Buy");
        redeemBuyButton.addActionListener(this);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        bottomPanel.add(buyButton);
        bottomPanel.add(redeemBuyButton);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        model.setRowCount(0);
        for (Book b : store.getBooks()) {
            model.addRow(new Object[]{b.getName(), String.format("%.2f", b.getPrice()), Boolean.FALSE});
        }
    }

    public ArrayList<Book> getSelectedBooks(boolean redeem) {
        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        ArrayList<Book> selected = new ArrayList<>();
        ArrayList<Book> storeBooks = store.getBooks();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean checked = (Boolean) model.getValueAt(i, 2);
            if (Boolean.TRUE.equals(checked)) {
                selected.add(storeBooks.get(i));
            }
        }
        return selected;
    }

    public void handleBuy(boolean redeem) {
        ArrayList<Book> selectedBooks = getSelectedBooks(redeem);
        if (selectedBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one book.");
            return;
        }

        double finalTC;
        if (redeem) {
            finalTC = customer.redeemAndBuy(selectedBooks);
        } else {
            finalTC = customer.buyBooks(selectedBooks);
        }

        app.showPanel(new CustomerCostScreen(app, store, customer, finalTC));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buyButton) {
            handleBuy(false);
        } else if (e.getSource() == redeemBuyButton) {
            handleBuy(true);
        } else if (e.getSource() == logoutButton) {
            app.showPanel(new LoginScreen(app, store));
        }
    }
}

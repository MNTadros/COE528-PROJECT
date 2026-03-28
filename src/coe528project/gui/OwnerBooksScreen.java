package coe528project.gui;

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

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Book Name", "Book Price"};
        DefaultTableModel initialModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        booksTable = new JTable(initialModel);
        add(new JScrollPane(booksTable), BorderLayout.NORTH);

        JPanel middlePanel = new JPanel(new FlowLayout());
        nameField = new JTextField(10);
        priceField = new JTextField(5);
        addButton = new JButton("Add");
        addButton.addActionListener(this);

        middlePanel.add(new JLabel("Name:"));
        middlePanel.add(nameField);
        middlePanel.add(new JLabel("Price:"));
        middlePanel.add(priceField);
        middlePanel.add(addButton);
        add(middlePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);

        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    public void addBook() {
        String name = nameField.getText().trim();
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            store.addBook(new Book(name, price));
            nameField.setText("");
            priceField.setText("");
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow >= 0) {
            Book bookToRemove = store.getBooks().get(selectedRow);
            store.removeBook(bookToRemove);
            refreshTable();
        }
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        model.setRowCount(0);
        for (Book b : store.getBooks()) {
            model.addRow(new Object[]{b.getName(), b.getPrice()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addBook();
        } else if (e.getSource() == deleteButton) {
            deleteBook();
        } else if (e.getSource() == backButton) {
            app.showPanel(new OwnerStartScreen(app, store));
        }
    }
}

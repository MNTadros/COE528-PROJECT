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

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] columns = {"Book Name", "Book Price"};
        DefaultTableModel initialModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        booksTable = new JTable(initialModel);
        booksTable.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(booksTable);
        tableScroll.setPreferredSize(new Dimension(0, 160));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        nameField = new JTextField(12);
        priceField = new JTextField(6);
        addButton = new JButton("Add");
        addButton.addActionListener(this);

        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(new JLabel("Price:"));
        topPanel.add(priceField);
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

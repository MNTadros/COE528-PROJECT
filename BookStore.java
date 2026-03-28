import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

package coe528project;

public class BookStore {

    // List to store all books
    private ArrayList<Book> books;

    // List to store all customers
    private ArrayList<Customer> customers;

    // Constructor initializes lists and loads data from files
    public BookStore() {
        books = new ArrayList<>();
        customers = new ArrayList<>();
        loadData(); // load existing data from files
    }

    // ---------------- BOOK METHODS ----------------

    // Add a new book to the store
    public void addBook(Book b) {
        books.add(b);
    }

    // Remove a book from the store
    public void removeBook(Book b) {
        books.remove(b);
    }

    // Return list of all books
    public ArrayList<Book> getBooks() {
        return books;
    }

    // ---------------- CUSTOMER METHODS ----------------

    // Add a new customer
    public void addCustomer(Customer c) {
        customers.add(c);
    }

    // Remove a customer
    public void removeCustomer(Customer c) {
        customers.remove(c);
    }

    // Return list of all customers
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    // Find a customer by username and password
    public Customer findCustomer(String username, String password) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username) &&
                c.getPassword().equals(password)) {
                return c; // match found
            }
        }
        return null; // no match
    }

    // ---------------- LOGIN LOGIC ----------------

    // Determines user type during login
    public String login(String username, String password) {

        // Check if owner login
        if (username.equals("admin") && password.equals("admin")) {
            return "OWNER";
        }

        // Check if valid customer
        Customer c = findCustomer(username, password);
        if (c != null) {
            return "CUSTOMER";
        }

        // Invalid login
        return "INVALID";
    }

    // ---------------- FILE HANDLING ----------------

    // Load all data from files
    public void loadData() {
        loadBooks();
        loadCustomers();
    }

    // Load books from books.txt
    private void loadBooks() {
        try {
            File file = new File("books.txt");

            // If file doesn't exist, skip loading
            if (!file.exists()) return;

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Extract book data
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);

                books.add(new Book(name, price));
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load customers from customers.txt
    private void loadCustomers() {
        try {
            File file = new File("customers.txt");

            // If file doesn't exist, skip loading
            if (!file.exists()) return;

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Extract customer data
                String username = parts[0];
                String password = parts[1];
                int points = Integer.parseInt(parts[2]);

                customers.add(new Customer(username, password, points));
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save all data to files
    public void saveData() {
        saveBooks();
        saveCustomers();
    }

    // Save books to books.txt
    private void saveBooks() {
        try {
            PrintWriter writer = new PrintWriter(new File("books.txt"));

            for (Book b : books) {
                writer.println(b.getName() + "," + b.getPrice());
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save customers to customers.txt
    private void saveCustomers() {
        try {
            PrintWriter writer = new PrintWriter(new File("customers.txt"));

            for (Customer c : customers) {
                writer.println(c.getUsername() + "," +
                               c.getPassword() + "," +
                               c.getPoints());
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

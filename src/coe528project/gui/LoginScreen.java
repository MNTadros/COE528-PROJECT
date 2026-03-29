//package coe528project;

import coe528project.model.BookStore;
import coe528project.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// This lets the screen know when a button is clicked
public class LoginScreen extends JPanel implements ActionListener {
    
    private JTextField usernameField;
    // Hides the password with dots
    private JPasswordField passwordField;
    private JButton loginButton;
    private BookStoreApp app;
    private BookStore store;

    public LoginScreen(BookStoreApp app, BookStore store) {
        this.app = app;
        this.store = store;

        // Put things in a neat grid and add some space around them
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Make the title stretch across two columns
        JLabel welcomeLabel = new JLabel("Welcome to the BookStore App", SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 14f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:")); // Requirement: login-screen has a username field
        usernameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:")); // Requirement: login-screen has a password field
        passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        add(passwordField, gbc);

        loginButton = new JButton("Login"); // Requirement: login-screen has button [login]
        // Make the button actually listen for clicks
        loginButton.addActionListener(this); 
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);
    }

    public void handleLogin() {
        String username = usernameField.getText().trim();
        // Turn the hidden password back into normal text so we can check it
        String password = new String(passwordField.getPassword());

        // Requirement: The owner's username is admin and password is admin
        if ("admin".equals(username) && "admin".equals(password)) {
            // Go to the Owner screen
            app.showPanel(new OwnerStartScreen(app, store));
            return;
        }

        // Check if the user is a normal customer
        Customer customer = store.findCustomer(username, password);
        if (customer != null) {
            // Go to the Customer screen
            app.showPanel(new CustomerStartScreen(app, store, customer));
            return;
        }

        // If wrong, show an error message and clear the password box
        JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        passwordField.setText("");
    }

    // What to do when the Login button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        }
    }
}
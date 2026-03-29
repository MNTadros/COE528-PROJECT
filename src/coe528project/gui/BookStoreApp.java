//package coe528project;

import coe528project.model.BookStore;

import javax.swing.*;
import java.awt.event.*;

public class BookStoreApp {
    // The main data storage
    private BookStore bookStore;
    // The main window on the screen
    private JFrame frame;
    // Remembers which screen the user is looking at right now
    private JPanel currentPanel;

    public BookStoreApp() {
        bookStore = new BookStore();

        frame = new JFrame("Bookstore App");
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        
        // --- SAVING DATA WHEN CLOSING ---
        // Requirement: Whenever a user clicks the [x] button, relevant data is written
        // Stop the app from closing right away when you click [x]. We need to save first.
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Wait for the user to click the [x] button.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // When they click [x], run our custom save code.
                onWindowsClose(); 
            }
        });

        // Requirement: The app starts with a login-screen
        showPanel(new LoginScreen(this, bookStore));
        frame.setVisible(true);
    }

    // Swaps out the screens without opening a bunch of new windows.
    public void showPanel(JPanel panel) {
        // Requirement: The app should be a single-window GUI... last screen replaced by the new screen
        // Remove the old screen and put the new one in
        frame.setContentPane(panel);
        // Tell the window to update and show the new screen
        frame.revalidate();
        frame.repaint();
        currentPanel = panel;
    }

    // What happens when the [x] is clicked
    public void onWindowsClose() {
        // Tell the main storage to save everything to the text files
        bookStore.saveData();
        // Close the app completely
        System.exit(0);
    }

    public static void main(String[] args) {
        // Start the app safely so it doesn't freeze or crash
        SwingUtilities.invokeLater(() -> new BookStoreApp());
    }
}
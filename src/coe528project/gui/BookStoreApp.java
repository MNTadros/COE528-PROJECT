package coe528project.gui;

import coe528project.model.BookStore;

import javax.swing.*;
import java.awt.event.*;

public class BookStoreApp {
    private BookStore bookStore;
    private JFrame frame;
    private JPanel currentPanel;

    public BookStoreApp() {
        bookStore = new BookStore();

        frame = new JFrame("Bookstore App");
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        // Requirement: Whenever a user clicks the [x] button, relevant data is written
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowsClose(); // Corrected to match UML spelling
            }
        });

        // Requirement: The app starts with a login-screen
        showPanel(new LoginScreen(this, bookStore));
        frame.setVisible(true);
    }

    public void showPanel(JPanel panel) {
        // Requirement: The app should be a single-window GUI... last screen replaced by the new screen
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
        currentPanel = panel;
    }

    public void onWindowsClose() {
        bookStore.saveData();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookStoreApp());
    }
}

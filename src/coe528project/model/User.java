package coe528project.model;

public abstract class User {

    protected String username;
    protected String password;

    // Constructor to initialize the user
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Login method checks if the credentials match
    public boolean login(String username, String password) {
        return this.username.equals(username) &&
               this.password.equals(password);
    }
}

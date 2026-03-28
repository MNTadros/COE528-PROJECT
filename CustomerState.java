package coe528project;

public interface CustomerState {

    // Returns the status as a string 
    String getStatus();

    // Checks and updates the customer's state if needed
    void checkState(Customer customer);
}


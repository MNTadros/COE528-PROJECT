package coe528project.model;

public interface CustomerState {

    // Returns the status as a string
    String getStatus();

    // Checks and updates the customer's state if needed
    void checkState(Customer customer);
}

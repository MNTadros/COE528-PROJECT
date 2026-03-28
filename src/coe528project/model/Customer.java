package coe528project.model;

import java.util.ArrayList;

public class Customer extends User {

    private int points;
    private CustomerState state;

    // Constructor
    public Customer(String username, String password, int points) {
        super(username, password);
        this.points = points;

        // Set initial state
        if (points >= 1000) {
            state = new GoldState();
        } else {
            state = new SilverState();
        }
    }

    // Getter for points
    public int getPoints() {
        return points;
    }

    // Setter for points
    public void setPoints(int points) {
        this.points = points;
        updateState(); // update status when points change
    }

    // Getter for state
    public CustomerState getState() {
        return state;
    }

    // Allows state objects to change the customer's state
    public void setState(CustomerState state) {
        this.state = state;
    }

    // Update state based on points
    public void updateState() {
        state.checkState(this);
    }

    // Get status as a string
    public String getStatus() {
        return state.getStatus();
    }

    // ---------------- BUYING ----------------

    // Buy books normally
    public double buyBooks(ArrayList<Book> selectedBooks) {
        double totalCost = 0;

        // Calculate total cost
        for (Book b : selectedBooks) {
            totalCost += b.getPrice();
        }

        // Earn 10 points per $1
        int earnedPoints = (int) (totalCost * 10);
        points += earnedPoints;

        updateState();

        return totalCost;
    }

    // Redeem points and buy
    public double redeemAndBuy(ArrayList<Book> selectedBooks) {
        double totalCost = 0;

        // Calculate total cost
        for (Book b : selectedBooks) {
            totalCost += b.getPrice();
        }

        // Calculate discount from points
        double discount = points / 100.0; // 100 points = $1

        // Apply discount
        double finalCost = totalCost - discount;

        if (finalCost < 0) {
            finalCost = 0;
        }

        // Deduct used points
        int usedPoints = (int) (discount * 100);
        points -= usedPoints;

        // Earn new points from the final cost
        int earnedPoints = (int) (finalCost * 10);
        points += earnedPoints;

        updateState();

        return finalCost;
    }
}

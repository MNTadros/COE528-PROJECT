package coe528project;

import java.util.ArrayList;

public class Customer extends User {

    private int points;
    private CustomerState state;

    // Constructor
    public Customer(String username, String password, int points) {
        super(username, password);
        this.points = points;
        updateState(); // set initial state
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

    // Update state based on points
    public void updateState() {
        if (points >= 1000) {
            state = new GoldState();
        } else {
            state = new SilverState();
        }
    }

    // Get status as string (for GUI)
    public String getStatus() {
        return state.getStatus();
    }

    // ---------------- BUY LOGIC ----------------

    // Buy books normally
    public double buyBooks(ArrayList<Book> selectedBooks) {
        double totalCost = 0;

        // Calculate total cost
        for (Book b : selectedBooks) {
            totalCost += b.getPrice();
        }

        // Earn points (10 points per $1)
        int earnedPoints = (int)(totalCost * 10);
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
        int usedPoints = (int)(discount * 100);
        points -= usedPoints;

        // Earn new points from final cost
        int earnedPoints = (int)(finalCost * 10);
        points += earnedPoints;

        updateState();

        return finalCost;
    }
}


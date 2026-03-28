package coe528project;

public class Book {

    // Instance variables
    private String name;
    private double price;

    // Constructor to initialize book object
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getter for book name
    public String getName() {
        return name;
    }

    // Setter for book name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for book price
    public double getPrice() {
        return price;
    }

    // Setter for book price
    public void setPrice(double price) {
        this.price = price;
    }

    // Returns book details
    @Override
    public String toString() {
        return name + "," + price;
    }
}

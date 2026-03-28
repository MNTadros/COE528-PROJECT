package coe528project;

public class SilverState implements CustomerState {

    // Return status name
    @Override
    public String getStatus() {
        return "Silver";
    }

    // Check if customer should move to Gold
    @Override
    public void checkState(Customer customer) {

        // If points reach 1000 or more then switch to Gold state
        if (customer.getPoints() >= 1000) {
            customer.setState(new GoldState());
        }
    }
}

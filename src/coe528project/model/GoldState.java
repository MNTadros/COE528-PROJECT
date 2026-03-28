package coe528project.model;

public class GoldState implements CustomerState {

    // Return status name
    @Override
    public String getStatus() {
        return "Gold";
    }

    // Check if customer should move back to Silver state
    @Override
    public void checkState(Customer customer) {

        // If points drop below 1000 then switch to Silver state
        if (customer.getPoints() < 1000) {
            customer.setState(new SilverState());
        }
    }
}

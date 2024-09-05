package Models;

public class PremiumUser extends User {
    private double discountRate;

    public PremiumUser(int id, String name, String email, String password, String address, double discountRate) {
        super(id, name, email, password, address);
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public String toString() {
        return "PremiumUser{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", discountRate=" + discountRate +
                '}';
    }
}

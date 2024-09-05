package Models;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private int id;
    private User user;
    private List<CartItem> items;
    private double totalAmount;

    public ShoppingCart(int id, User user, double totalAmount) {
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>();
    }

    public ShoppingCart(User user, double totalAmount) {
        this.user = user;
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addCartItem(CartItem cartItem) {
        this.items.add(cartItem);
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShoppingCart{")
                .append("id=").append(id)
                .append(", user=").append(user)
                .append(", totalAmount=").append(totalAmount)
                .append(", items=[");

        for (CartItem item : items) {
            sb.append("\n  ").append(item);
        }

        sb.append("\n]}");
        return sb.toString();
    }

}


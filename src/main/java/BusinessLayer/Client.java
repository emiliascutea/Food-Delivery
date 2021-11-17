package BusinessLayer;

import java.io.Serializable;

public class Client implements Serializable {
    private static final long serialVersionUID = 1923080036514182598L;
    private int ID;
    private String username;
    private String password;
    private int numberOfOrders;
    private float priceOfOrder;

    public Client(int ID, String username, String password) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.numberOfOrders = 0;
        this.priceOfOrder = 0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public float getPriceOfOrder() {
        return priceOfOrder;
    }

    public void setPriceOfOrder(float priceOfOrder) {
        this.priceOfOrder = priceOfOrder;
    }
}

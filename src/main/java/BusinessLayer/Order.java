package BusinessLayer;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {
    private static final long serialVersionUID = -5982268915097648063L;
    private int orderID;
    private int clientID;
    private float price;
    private Date orderDate;

    public Order(int orderID, int clientID, float price, Date orderDate) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.price = price;
        this.orderDate = orderDate;
    }

    public Order(int orderID, int clientID) {
        this.orderID = orderID;
        this.clientID = clientID;
    }

    public Order(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderID == order.orderID && clientID == order.clientID && Objects.equals(orderDate, order.orderDate);
    }

    @Override
    public int hashCode() {
        final int i1 = 5;
        final int i2 = 3;
        int hashValue = orderID + clientID;
        hashValue += i1 * hashValue;
        hashValue += i2 * hashValue;
        return hashValue;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isAfter(int startHour){
        if(orderDate.getHours() >= startHour){
            return true;
        }
        return false;
    }

    public boolean isBefore(int endHour){
        if(orderDate.getHours() <= endHour){
            return true;
        }
        return false;
    }

}

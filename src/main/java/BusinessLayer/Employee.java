package BusinessLayer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class Employee implements PropertyChangeListener {
    private Order order = new Order();

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        System.out.println("OBSERVER: New order: ID of order: " + order.getOrderID() + "; price of order:" + order.getPrice());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.setOrder((Order) evt.getNewValue());
    }
}

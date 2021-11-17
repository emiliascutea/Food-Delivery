package BusinessLayer;

import java.io.Serializable;

public class BaseProduct extends MenuItem {
    private static final long serialVersionUID = -7760611245051447989L;
    private String name;
    private float price;

    public BaseProduct(String name, double rating, int calories, int protein, int fat, int sodium, float price) {
        super(name, rating, calories, protein, fat, sodium, price);
        this.name = name;
        this.price = price;
    }

    @Override
    public float computePrice() {
        return price;
    }
}





package BusinessLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompositeProduct extends MenuItem {
    private static final long serialVersionUID = 2027508147031171150L;
    private String name;
    private float price;
    private List<MenuItem> products = new ArrayList<MenuItem>();

    public CompositeProduct() {
        super();
    }

    public void addProduct(MenuItem menuItem) {
        products.add(menuItem);
    }

    public List<MenuItem> getProducts() {
        return products;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public void setPrice(float price) {
        this.price = price;
    }

    public void setProducts(List<MenuItem> products) {
        this.products = products;
    }

    @Override
    public float computePrice() {
        float price = 0;
        for (MenuItem product : products) {
            price += product.computePrice();
        }
        return price;
    }

    public int computeFat(){
        int fat = 0;
        for (MenuItem product : products) {
            fat += product.getFat();
        }
        return fat;
    }
    public int computeProtein(){
        int protein = 0;
        for (MenuItem product : products) {
            protein += product.getProtein();
        }
        return protein;
    }
    public int computeSodium(){
        int sodium = 0;
        for (MenuItem product : products) {
            sodium += product.getSodium();
        }
        return sodium;
    }
    public int computeCalories(){
        int calories = 0;
        for (MenuItem product : products) {
            calories += product.getCalories();
        }
        return calories;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeProduct that = (CompositeProduct) o;
        return Float.compare(that.price, price) == 0 && Objects.equals(name, that.name) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        final int prime1 = 7;
        final int prime2 = 5;
        int hash = (int) computePrice() * prime1;
        hash = hash * prime2;
        return hash;
    }
}

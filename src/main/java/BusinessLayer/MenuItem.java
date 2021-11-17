package BusinessLayer;

import java.io.Serializable;
import java.util.Objects;

public class MenuItem implements Serializable {
    private static final long serialVersionUID = -4807503029114919819L;
    private String name;
    private double rating;
    private int calories;
    private int protein;
    private int fat;
    private int sodium;
    private float price;
    private int numberOfOrders;

    public MenuItem(String name, double rating, int calories, int protein, int fat, int sodium, float price) {
        this.name = name;
        this.rating = rating;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sodium = sodium;
        this.price = price;
        this.numberOfOrders = 0;
    }

    public MenuItem(String name, float price) {
        this.name = name;
        this.price = price;
        this.numberOfOrders = 0;
    }

    public MenuItem(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float computePrice(){
        return price;
    }

    public float getPrice(){
        return price;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Double.compare(menuItem.rating, rating) == 0 && calories == menuItem.calories && protein == menuItem.protein && fat == menuItem.fat && sodium == menuItem.sodium && Float.compare(menuItem.price, price) == 0 && Objects.equals(name, menuItem.name);
    }

    @Override
    public int hashCode() {
        final int prime1 = 7;
        final int prime2 = 5;
        int hash = (int) (protein + sodium + fat + rating + calories);
        hash = hash * prime1 + (int) price;
        hash += hash * prime2;
        return hash;
    }
}

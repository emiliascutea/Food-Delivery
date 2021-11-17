package BusinessLayer;

import BusinessLayer.MenuItem;

import java.util.ArrayList;
import java.util.HashSet;

public interface IDeliveryServiceProcessing {

    void importProducts();

    void modifyProduct(MenuItem menuItem1, MenuItem menuItem2);

    boolean addProduct(MenuItem menuItem);

    void deleteProduct(MenuItem menuItem);

    boolean wellFormedMenu();

    boolean generateReportTimeInterval(int startHour, int endHour);

    boolean generateReportClientAndAmount(int numberOfOrders, int amount);

    boolean generateReportFrequentlyOrderedProducts(int numberOfOrders);

    boolean generateReportsProductsToday(int day);

    void createOrder(Order order, ArrayList<MenuItem> products);

    float computeOrderPrice(Order order);

    void generateBill(Order order);

    HashSet<MenuItem> filterByName(HashSet<MenuItem> menu, String name);

    HashSet<MenuItem> filterByPrice(HashSet<MenuItem> menu, float price);

    HashSet<MenuItem> filterBySodium(HashSet<MenuItem> menu, int sodium);

    HashSet<MenuItem> filterByRating(HashSet<MenuItem> menu, double rating);

    HashSet<MenuItem> filterByProtein(HashSet<MenuItem> menu, int protein);

    HashSet<MenuItem> filterByFat(HashSet<MenuItem> menu, int fat);

    HashSet<MenuItem> filterByCalories(HashSet<MenuItem> menu, int calories);

}

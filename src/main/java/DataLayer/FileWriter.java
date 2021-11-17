package DataLayer;

import BusinessLayer.BaseProduct;
import BusinessLayer.MenuItem;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileWriter implements Serializable {
    public File file = new File("./products.csv");

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public HashSet<MenuItem> readFromCSV() {

        HashSet<MenuItem> items = new HashSet<>();
        HashSet<MenuItem> menu = new HashSet<>();
        String item;
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("./products.csv"));
            item = csvReader.readLine();
            while ((item = csvReader.readLine()) != null) {
                String[] data = item.split(",");
                MenuItem menuItem = new BaseProduct(data[0], Double.parseDouble(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Float.parseFloat(data[6]));
                items.add(menuItem);
            }
            menu = items.stream().filter(distinctByKey(MenuItem::getName))
                    .collect(Collectors.toCollection(HashSet::new));
            System.out.println(menu.size());
            for(MenuItem menuItem : menu){
                    System.out.println(menuItem.getName());

            }
            csvReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return menu;
    }
}

package BusinessLayer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import DataLayer.FileWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * class deliveryService
 */
public class DeliveryService implements IDeliveryServiceProcessing, Serializable {
    private static final long serialVersionUID = -3906923963781705306L;
    public HashMap<Order, ArrayList<MenuItem>> orders;
    public PropertyChangeSupport support;
    public HashSet<MenuItem> menu;
    public ArrayList<Client> clients;
    public Administrator administrator;

    /**
     * constructor of class DeliveryService
     */
    public DeliveryService() {
        this.orders = new HashMap<Order, ArrayList<MenuItem>>();
        this.menu = new HashSet<>();
        this.clients = new ArrayList<>();
        this.administrator = new Administrator();
        administrator.setUsername("admin");
        administrator.setPassword("admin");
    }

    /**
     * method to add a PropertyChangeListener to the PropertyChangeSupport object
     * @param pcl represents the property change listener which is added
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * method to add a product to the menu
     * @param menuItem the product which is added to the menu
     * @return true if the product was successfully added, false otherwise
     */
    @Override
    public boolean addProduct(MenuItem menuItem) {
        return menu.add(menuItem);
    }

    /**
     * method to modify a product from the menu
     * @param currentItem the current value of the product
     * @param modifiedItem the new value of the product
     */
    @Override
    public void modifyProduct(MenuItem currentItem, MenuItem modifiedItem) {
        menu.remove(currentItem);
        menu.add(modifiedItem);
        assert wellFormedMenu();
    }

    /**
     * method to delete a product from the menu
     * @param menuItem the product to be deleted
     */
    @Override
    public void deleteProduct(MenuItem menuItem) {
        menu.remove(menuItem);
    }

    /**
     * method used to check if there are no duplicate products in the menu
     * @return false if there are duplicates in the menu, true otherwise
     */
    @Override
    public boolean wellFormedMenu() {
        for (MenuItem item1 : menu) {
            for (MenuItem item2 : menu) {
                if (item1.equals(item2) || item1.getName().equals(item2.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * method used to import products from the provided .csv file
     */
    @Override
    public void importProducts() {
        FileWriter fileWriter = new FileWriter();
        menu = fileWriter.readFromCSV();
    }

    /**
     * method which generates a report with the orders performed
     * between a given start hour and a given end hour regardless the date.
     * @param startHour represents the start hour
     * @param endHour represents the end hour
     * @return true if the report was successfully generated, false otherwise
     */
    @Override
    public boolean generateReportTimeInterval(int startHour, int endHour) {
        StringBuilder string = new StringBuilder();
        string.append("Report Time Interval");
        string.append(".pdf");
        Document document = new Document();
        ArrayList<Order> reportOrders = new ArrayList<>();
        orders.forEach((order, items) -> {
            reportOrders.add(order);
        });
        List<Order> filteredOrders = reportOrders.stream().filter(o -> o.isAfter(startHour) && o.isBefore(endHour)).collect(Collectors.toList());
        if (filteredOrders.size() > 0) {
            try {
                PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
                document.open();
                document.add(new Paragraph("             Report time interval\n\n"));
                for (Order order : filteredOrders) {
                    document.add(new Paragraph("ID of the order: " + order.getOrderID()));
                    document.add(new Paragraph("ID of the client: " + order.getClientID()));
                    document.add(new Paragraph("Date of the order: " + order.getOrderDate()));
                    document.add(new Paragraph("Price of the order: " + computeOrderPrice(order) + "\n"));
                    ArrayList<MenuItem> items = orders.get(order);
                    for (MenuItem item : items) {
                        System.out.print(item.getName() + " ");
                        document.add(new Paragraph("Name of the product: " + item.getName()));
                        document.add(new Paragraph("Price of the product: " + item.getPrice()));
                    }
                    document.add(new Paragraph("\n\n"));
                }
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
            document.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * method which searches a client by ID in the list of clients
     * @param id represents the id of the client
     * @return the found client
     */
    public Client findByID(int id) {
        for (Client client : clients) {
            if (client.getID() == id) {
                return client;
            }
        }
        return null;
    }

    /**
     * method to increment the number of orders of a client
     * @param reportClients represents an arrayList of the clients
     * @param client represents the client for which the number of orders must be incremented
     */
    public void incrementNumbersOfOrdersForClient(ArrayList<Client> reportClients, Client client) {
        for (Client clients : reportClients) {
            if (clients.getID() == client.getID()) {
                int numberOfOrders = client.getNumberOfOrders() + 1;
                client.setNumberOfOrders(numberOfOrders);
            }
        }
    }

    /**
     * method to generate a report with the clients that have ordered more than a specified number of times and the value
     * of the order was higher than a specified amount.
     * @param numberOfOrders represents the number of times the client placed an order
     * @param amount represents the value of the order
     * @return true if the report was successfully generated, false otherwise
     */
    @Override
    public boolean generateReportClientAndAmount(int numberOfOrders, int amount) {
        StringBuilder string = new StringBuilder();
        string.append("Report Clients And Amount");
        string.append(".pdf");
        Document document = new Document();
        ArrayList<Client> reportClients = new ArrayList<>();
        orders.forEach((order, items) -> {
            if (order.getPrice() >= amount) {
                Client client = findByID(order.getClientID());
                client.setPriceOfOrder(order.getPrice());
                if (!reportClients.contains(client)) {
                    client.setNumberOfOrders(1);
                    reportClients.add(client);
                } else {
                    incrementNumbersOfOrdersForClient(reportClients, client);
                }
            }
        });
        List<Client> filteredClients = reportClients.stream().filter(o -> o.getNumberOfOrders() >= numberOfOrders).collect(Collectors.toList());
        if (filteredClients.size() > 0) {
            try {
                PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
                document.open();
                document.add(new Paragraph("   Report clients that have ordered more than " + numberOfOrders + " times with price of order greater than " + amount + "\n\n"));

                for (Client client : filteredClients) {
                    document.add(new Paragraph("ID of the client: " + client.getID()));
                    document.add(new Paragraph("Name of the client: " + client.getUsername()));
                    document.add(new Paragraph("Number of orders with price greater than " + amount + " lei is: " + client.getNumberOfOrders() + "\n\n"));
                }

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
            document.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * method to increment the number of orders of
     * @param reportItems represents a list with the menu items
     * @param menuItem represents the item for which the number of orders must be incremented
     */
    public void incrementNumbersOfOrders(List<MenuItem> reportItems, MenuItem menuItem) {
        for (MenuItem item : reportItems) {
            if (item.getName().equals(menuItem.getName())) {
                int numberOfOrders = item.getNumberOfOrders() + 1;
                item.setNumberOfOrders(numberOfOrders);
            }
        }
    }

    /**
     * method to generate a report with the products ordered more than a specified number of times so far
     * @param numberOfOrders represents the number of times the product was ordered
     * @return true if the report was successfully generated, false otherwise
     */
    @Override
    public boolean generateReportFrequentlyOrderedProducts(int numberOfOrders) {
        StringBuilder string = new StringBuilder();
        string.append("Report Frequently Ordered Products");
        string.append(".pdf");
        Document document = new Document();
        List<MenuItem> reportItems = new ArrayList<>();
        orders.forEach((order, items) -> {
            for (MenuItem item : items) {
                MenuItem menuItem = new MenuItem();
                menuItem = item;
                if (!reportItems.contains(menuItem)) {
                    menuItem.setNumberOfOrders(1);
                    reportItems.add(menuItem);

                } else {
                    incrementNumbersOfOrders(reportItems, menuItem);
                }
            }
        });
        List<MenuItem> filteredItems = reportItems.stream().filter(o -> o.getNumberOfOrders() >= numberOfOrders).collect(Collectors.toList());
        if (filteredItems.size() > 0) {
            try {
                PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
                document.open();
                document.add(new Paragraph("   Report products that have been ordered more than " + numberOfOrders + " times \n\n"));

                for (MenuItem item : filteredItems) {
                    document.add(new Paragraph("Name of the product: " + item.getName()));
                    document.add(new Paragraph("Number of orders: " + item.getNumberOfOrders() + "\n\n"));
                }
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
            document.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * method to generate a report with the products ordered within a specified day with the number of times they have been ordered
     * @param day represents the day in which the products were ordered
     * @return true if the report was successfully generated, false otherwise
     */
    @Override
    public boolean generateReportsProductsToday(int day) {
        StringBuilder string = new StringBuilder();
        string.append("Report Products In A Day");
        string.append(".pdf");
        Document document = new Document();
        List<Order> orderList = new ArrayList<>();
        orderList.addAll(orders.keySet());
        List<Order> filteredOrders = orderList.stream().filter(o -> o.getOrderDate().getDate() == day).collect(Collectors.toList());
        ArrayList<MenuItem> reportItems = new ArrayList<>();
        for (Order order : filteredOrders) {
            ArrayList<MenuItem> currentItems = orders.get(order);
            for (MenuItem item : currentItems) {
                MenuItem menuItem = new MenuItem();
                menuItem = item;
                if (!reportItems.contains(menuItem)) {
                    menuItem.setNumberOfOrders(1);
                    reportItems.add(menuItem);

                } else {
                    incrementNumbersOfOrders(reportItems, menuItem);
                }
            }
        }
        if (reportItems.size() > 0) {
            try {
                PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
                document.open();
                document.add(new Paragraph("   Report products that have been ordered in day " + day + "\n\n"));
                for (MenuItem item : reportItems) {
                    document.add(new Paragraph("Name of the product: " + item.getName()));
                    document.add(new Paragraph("Number times product was ordered: " + item.getNumberOfOrders() + "\n\n"));
                }
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
            document.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * method to get the list of clients
     * @return the list of clients
     */
    public ArrayList<Client> getClients() {
        return clients;
    }

    /**
     * method to add a client to the list
     * @param client represents the client which is added to the list
     */
    public void addClient(Client client) {
        clients.add(client);
    }

    /**
     * method to add an administrator
     * @param administrator the administrator which is added
     */
    public void addAdmin(Administrator administrator) {
        this.administrator = administrator;

    }

    /**
     * method to create a new order
     * @param order represents the order
     * @param products represents the arraylist of the products which have been ordered
     */
    @Override
    public void createOrder(Order order, ArrayList<MenuItem> products) {
        orders.putIfAbsent(order, products);
        support.firePropertyChange("New order",null, order);
    }

    /**
     * method to compute the price of an order
     * @param order represents the order for which the price is computed
     * @return the price of the order
     */
    @Override
    public float computeOrderPrice(Order order) {
        float price = 0;
        ArrayList<MenuItem> products = orders.get(order);
        for (MenuItem product : products) {
            price += product.computePrice();
        }
        return price;
    }

    /**
     * method to generate a bill after performing an order
     * @param order represents the order for which the bill was generated
     */
    @Override
    public void generateBill(Order order) {
        StringBuilder string = new StringBuilder();
        string.append("Bill");
        string.append(".pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
            document.open();
            document.add(new Paragraph("                 Bill \n\n"));
            document.add(new Paragraph("ID of the client: " + order.getClientID()));
            document.add(new Paragraph("Date of the order: " + order.getOrderDate()));
            document.add(new Paragraph("Price of the order: " + computeOrderPrice(order)));
            document.add(new Paragraph("\n"));
            for (MenuItem item : orders.get(order)) {
                document.add(new Paragraph("Name of the product: " + item.getName()));
                document.add(new Paragraph("Price of the product: " + item.getPrice() + "\n\n"));
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        document.close();
    }

    /**
     * method to filter the menu by name
     * @param oldMenu represent the current menu
     * @param name represents the name which is filtered by
     * @return the filtered menu
     */
    @Override
    public HashSet<MenuItem> filterByName(HashSet<MenuItem> oldMenu, String name) {
        HashSet<MenuItem> filteredMenu = new HashSet<>();
        HashSet<MenuItem> items = oldMenu;

        filteredMenu = items.stream().filter(o -> o.getName().contains(name))
                .collect(Collectors.toCollection(HashSet::new));
        return filteredMenu;
    }

    /**
     * method to filter the menu by price
     * @param oldMenu represent the current menu
     * @param price represents the price which is filtered by decreasing
     * @return the filtered menu
     */
    @Override
    public HashSet<MenuItem> filterByPrice(HashSet<MenuItem> oldMenu, float price) {
        HashSet<MenuItem> filteredMenu = new HashSet<>();
        HashSet<MenuItem> items = oldMenu;
        filteredMenu = items.stream().filter(o -> o.getPrice() <= price)
                .collect(Collectors.toCollection(HashSet::new));
        return filteredMenu;
    }

    /**
     * method to filter the menu by sodium
     * @param oldMenu represent the current menu
     * @param sodium represents the value of the sodium which is filtered by decreasing
     * @return the filtered menu
     */
    @Override
    public HashSet<MenuItem> filterBySodium(HashSet<MenuItem> oldMenu, int sodium) {
        HashSet<MenuItem> filteredMenu = new HashSet<>();
        HashSet<MenuItem> items = oldMenu;

        filteredMenu = items.stream().filter(o -> o.getSodium() <= sodium)
                .collect(Collectors.toCollection(HashSet::new));
        return filteredMenu;
    }

    /**
     * method to filter the menu by rating
     * @param oldMenu represent the current menu
     * @param rating represents the value of the rating which is filtered by increasing
     * @return the filtered menu
     */
    @Override
    public HashSet<MenuItem> filterByRating(HashSet<MenuItem> oldMenu, double rating) {
        HashSet<MenuItem> filteredMenu = new HashSet<>();
        HashSet<MenuItem> items = oldMenu;

        filteredMenu = items.stream().filter(o -> o.getRating() >= rating)
                .collect(Collectors.toCollection(HashSet::new));
        return filteredMenu;
    }

    /**
     * method to filter the menu by protein
     * @param oldMenu represent the current menu
     * @param protein represents the value of the protein which is filtered by increasing
     * @return the filtered menu
     */
    @Override
    public HashSet<MenuItem> filterByProtein(HashSet<MenuItem> oldMenu, int protein) {
        HashSet<MenuItem> filteredMenu = new HashSet<>();
        HashSet<MenuItem> items = oldMenu;

        filteredMenu = items.stream().filter(o -> o.getProtein() >= protein)
                .collect(Collectors.toCollection(HashSet::new));
        return filteredMenu;
    }

    /**
     * method to filter the menu by fat
     * @param oldMenu represent the current menu
     * @param fat represents the value of the fat which is filtered by decreasing
     * @return the filtered menu
     */
    @Override
    public HashSet<MenuItem> filterByFat(HashSet<MenuItem> oldMenu, int fat) {
        HashSet<MenuItem> filteredMenu = new HashSet<>();
        HashSet<MenuItem> items = oldMenu;

        filteredMenu = items.stream().filter(o -> o.getFat() <= fat)
                .collect(Collectors.toCollection(HashSet::new));
        return filteredMenu;
    }

    /**
     * method to filter the menu by calories
     * @param oldMenu represent the current menu
     * @param calories represents the value of the calories which is filtered by decreasing
     * @return the filtered menu
     */
    @Override
    public HashSet<MenuItem> filterByCalories(HashSet<MenuItem> oldMenu, int calories){
        HashSet<MenuItem> filteredMenu = new HashSet<>();
        HashSet<MenuItem> items = oldMenu;

        filteredMenu = items.stream().filter(o -> o.getCalories() <= calories)
                .collect(Collectors.toCollection(HashSet::new));
        return filteredMenu;
    }

}



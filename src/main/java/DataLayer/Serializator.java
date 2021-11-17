package DataLayer;

import BusinessLayer.Client;

import BusinessLayer.DeliveryService;
import BusinessLayer.MenuItem;

import java.io.*;

public class Serializator {

    public void serialize(DeliveryService deliveryService) {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream("BusinessLayer.DeliveryService.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(deliveryService);
            out.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("Serialize: IOException is caught");
            ex.printStackTrace();
        }
    }

    /**
     * method used for deserialization of objects
     */
    public DeliveryService deserialize() {
        File simulatorSer = new File("./BusinessLayer.DeliveryService.ser");
        boolean exists = simulatorSer.exists();
        DeliveryService deliveryService = new DeliveryService();

        if (exists) {
            try {
                FileInputStream file = new FileInputStream("./BusinessLayer.DeliveryService.ser");
                ObjectInputStream in = new ObjectInputStream(file);

                // Method for deserialization of object
                System.out.println("DESERIALIZE");
                deliveryService = (DeliveryService) in.readObject();

                for(Client client : deliveryService.clients){
                    System.out.println("Client " + client.getUsername() + " with id " + client.getID());
                }

                deliveryService.orders.forEach((order, items) -> {
                    System.out.print("Order " + order.getOrderID() + " from client  " + order.getClientID() + " : ");
                    for (MenuItem item : items) {
                        System.out.print(item.getName() + " ");
                    }
                    System.out.println(" price " + order.getPrice());
                });

                in.close();
                file.close();
            } catch (IOException e) {
                System.out.println("Deserialize: IOException is caught");
                System.out.println(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        return deliveryService;
    }
}

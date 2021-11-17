package PresentationLayer;

import BusinessLayer.Administrator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import BusinessLayer.Client;
import BusinessLayer.DeliveryService;
import  DataLayer.Serializator;

public class Main extends  Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Controller controller = new Controller();

//        Serializator serializator = new DataLayer.Serializator();
//        DeliveryService deliveryService = new BusinessLayer.DeliveryService();
//        deliveryService.importProducts();
//        Client client = new Client(1,"emi","emi");
//        Administrator administrator = new BusinessLayer.Administrator("admin","admin");
//        deliveryService.clients.add(client);
//        deliveryService.addAdmin(administrator);
//        serializator.serialize(deliveryService);

        controller.initialDeserialize();
        Parent root = FXMLLoader.load(getClass().getResource("/FirstPageUserInterface.fxml"));
        primaryStage.setTitle("Interface");
        primaryStage.setScene(new Scene(root, 600, 600));

        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
                    controller.finalSerialize();
                }
        );
    }

    public static void main(String[] args) {
       launch();
    }
}
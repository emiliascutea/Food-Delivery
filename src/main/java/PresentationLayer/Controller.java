package PresentationLayer;

import BusinessLayer.*;
import BusinessLayer.MenuItem;
import DataLayer.Serializator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Controller implements Serializable {

    private Alert alert = new Alert();
    private Serializator serializator = new Serializator();
    private static DeliveryService deliveryService = new DeliveryService();
    private static Employee employee = new Employee();
    private ArrayList<MenuItem> selectedProducts;
    private CompositeProduct compositeProduct = new CompositeProduct();
    private HashSet<MenuItem> filteredMenu;
    private float price;
    private Client client;
    private Order order;
    private int orderID = 0;
    private static StringBuilder employeeInfo = new StringBuilder();

    public void initialDeserialize() {
        deliveryService = serializator.deserialize();
    }

    public void finalSerialize() {
        serializator.serialize(deliveryService);
    }

    public void setClientInterface(ActionEvent event) {
        setScene(event, "/ClientUserInterface.fxml");
    }

    public void setAdminInterface(ActionEvent event) {
        setScene(event, "/AdminUserInterface.fxml");
    }

    public void setEmployeeInterface(ActionEvent event) {
        setScene(event, "/EmployeeUserInterface.fxml");
    }

    public void closeMain(ActionEvent event) {
        setScene(event, "/FirstPageUserInterface.fxml");
    }

    public void setScene(ActionEvent event, String FXMLFile) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(FXMLFile));

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private Button signUpButton, logInButton;
    @FXML
    private TextField logInUsername, logInPassword, signUpUsername, signUpPassword;

    ObservableList<MenuItem> observableList = FXCollections.observableArrayList();
    @FXML
    private TableView<MenuItem> tableWithProducts;
    @FXML
    private TableColumn<Object, Object> product_name, product_rating, product_calories, product_protein, product_fat, product_sodium, product_price;
    @FXML
    private TextArea cart;
    @FXML
    private TextField totalPrice, fieldName, fieldRating, fieldCalories, fieldProteins, fieldFats, fieldSodium, fieldPrice;
    @FXML
    private Button priceButton, orderButton, closeButton, clearCart, filter;
    @FXML
    private Label label11, label21, label31, label41, label51, label61, label71;
    @FXML
    private Button checkOrders;
    @FXML
    private TextArea employeeOrders;

    public void close() {
        signUpButton.setVisible(true);
        logInButton.setVisible(true);
        logInPassword.setVisible(true);
        logInUsername.setVisible(true);
        signUpUsername.setVisible(true);
        signUpPassword.setVisible(true);
        closeButton.setVisible(false);
        tableWithProducts.setVisible(false);
        priceButton.setVisible(false);
        orderButton.setVisible(false);
        totalPrice.setVisible(false);
        cart.setVisible(false);
        clearCart.setVisible(false);
    }

    public boolean validateLogIn() {
        if (!logInUsername.getText().equals("") && !logInPassword.getText().equals("")) {
            for (Client iterator : deliveryService.getClients()) {
                if (iterator.getUsername().equals(logInUsername.getText())) {
                    if (iterator.getPassword().equals(logInPassword.getText())) {
                        client = new Client(iterator.getID(), iterator.getUsername(), iterator.getPassword());
                        return true;
                    } else {
                        alert.alert("Wrong Password", "The introduced password is incorrect", "Please introduce a valid password.");
                        return false;
                    }
                }
            }
            alert.alert("Invalid Username", "The introduced username does not exist", "Please introduce a valid username.");
            return false;
        } else
            alert.alert("Empty Input", "You have not introduced the required data", "Please introduce valid data in all fields.");
        return false;
    }

    public void logIn() {
        if (validateLogIn()) {
            logInUsername.setText("");
            logInPassword.setText("");
            hideClientElements();
            setTableView();
        }
    }

    public boolean validateSignUp() {
        if (!signUpUsername.getText().equals("") && !signUpPassword.getText().equals("")) {
            if (deliveryService.getClients() != null) {
                for (Client client : deliveryService.getClients()) {
                    if (client.getUsername().equals(signUpUsername.getText())) {
                        alert.alert("Wrong Username", "The introduced username already exists", "Please choose another username.");
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public void signUp() {
        if (validateSignUp()) {
            client = new Client(deliveryService.clients.size() + 1, signUpUsername.getText(), signUpPassword.getText());
            deliveryService.addClient(client);
            serializator.serialize(deliveryService);
            signUpUsername.setText("");
            signUpPassword.setText("");
            hideClientElements();
            setTableView();
        }

    }

    public void hideClientElements() {
        signUpButton.setVisible(false);
        logInButton.setVisible(false);
        logInPassword.setVisible(false);
        logInUsername.setVisible(false);
        signUpUsername.setVisible(false);
        signUpPassword.setVisible(false);
        closeButton.setVisible(true);
    }

    public void setTableView() {
        try {
            filteredMenu = new HashSet<>();
            observableList = FXCollections.observableArrayList();
            ;
            for (MenuItem menuItem : deliveryService.menu) {
                observableList.add(new MenuItem(menuItem.getName(), menuItem.getRating(), menuItem.getCalories(), menuItem.getProtein(), menuItem.getFat(), menuItem.getSodium(), menuItem.getPrice()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        product_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        product_calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
        product_protein.setCellValueFactory(new PropertyValueFactory<>("protein"));
        product_fat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        product_sodium.setCellValueFactory(new PropertyValueFactory<>("sodium"));
        product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableWithProducts.setItems(observableList);
        tableWithProducts.setVisible(true);
        cart.setText("");
        cart.setVisible(true);
        label11.setVisible(true);
        label21.setVisible(true);
        label31.setVisible(true);
        label41.setVisible(true);
        label51.setVisible(true);
        label61.setVisible(true);
        label71.setVisible(true);
        fieldName.setText("");
        fieldCalories.setText("");
        fieldFats.setText("");
        fieldPrice.setText("");
        fieldProteins.setText("");
        fieldRating.setText("");
        fieldSodium.setText("");
        fieldName.setVisible(true);
        fieldCalories.setVisible(true);
        fieldFats.setVisible(true);
        fieldPrice.setVisible(true);
        fieldProteins.setVisible(true);
        fieldRating.setVisible(true);
        fieldSodium.setVisible(true);
        filter.setVisible(true);
        priceButton.setVisible(true);
        totalPrice.setText("");
        totalPrice.setVisible(false);
        orderButton.setVisible(true);
        clearCart.setVisible(true);
    }

    public void showPrice() {
        totalPrice.setText(String.valueOf(price));
        totalPrice.setVisible(true);
    }

    public void getSelectedProducts() {
        if (orderID == 0) {
            selectedProducts = new ArrayList<>();
        }
        MenuItem menuItem = tableWithProducts.getSelectionModel().getSelectedItem();
        selectedProducts.add(menuItem);
        cart.appendText(menuItem.getName() + "\t" + menuItem.getPrice() + "\n");
        if (orderID == 0) {
            orderID = deliveryService.orders.size() + 1;
            order = new Order(orderID, client.getID());
        }
        price += menuItem.getPrice();
        totalPrice.setText(String.valueOf(price));
    }

    public void setFilter() {
        if (filteredMenu.size() == 0) {
            filteredMenu = deliveryService.menu;
        }
        if (!fieldName.getText().equals("")) {
            filteredMenu = deliveryService.filterByName(filteredMenu, fieldName.getText());
        }
        if (!fieldPrice.getText().equals("")) {
            filteredMenu = deliveryService.filterByPrice(filteredMenu, Float.parseFloat(fieldPrice.getText()));
        }
        if (!fieldSodium.getText().equals("")) {
            filteredMenu = deliveryService.filterBySodium(filteredMenu, Integer.parseInt(fieldSodium.getText()));
        }
        if (!fieldRating.getText().equals("")) {
            filteredMenu = deliveryService.filterByRating(filteredMenu, Double.parseDouble(fieldRating.getText()));
        }
        if (!fieldProteins.getText().equals("")) {
            filteredMenu = deliveryService.filterByProtein(filteredMenu, Integer.parseInt(fieldProteins.getText()));
        }
        if (!fieldFats.getText().equals("")) {
            filteredMenu = deliveryService.filterByFat(filteredMenu, Integer.parseInt(fieldFats.getText()));
        }
        if (!fieldCalories.getText().equals("")) {
            filteredMenu = deliveryService.filterByCalories(filteredMenu, Integer.parseInt(fieldCalories.getText()));
        }
        try {
            observableList = FXCollections.observableArrayList();
            ;
            for (MenuItem menuItem : filteredMenu) {
                observableList.add(new MenuItem(menuItem.getName(), menuItem.getRating(), menuItem.getCalories(), menuItem.getProtein(), menuItem.getFat(), menuItem.getSodium(), menuItem.getPrice()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        product_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        product_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        product_calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
        product_protein.setCellValueFactory(new PropertyValueFactory<>("protein"));
        product_fat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        product_sodium.setCellValueFactory(new PropertyValueFactory<>("sodium"));
        product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableWithProducts.setItems(observableList);
        tableWithProducts.setVisible(true);
    }

    public void clearOrder() {
        orderID = 0;
        price = 0;
        setTableView();
    }

    public void placeOrder() {
        Date orderDate = new Date();
        if (order != null) {
            order.setOrderDate(orderDate);
            order.setPrice(price);
            client.setPriceOfOrder(price);
            deliveryService.support = new PropertyChangeSupport(deliveryService);
            deliveryService.addPropertyChangeListener(employee);
            deliveryService.createOrder(order, selectedProducts);
            employeeInfo.append("Order : ID ").append(employee.getOrder().getOrderID()).append(" price ").append(employee.getOrder().getPrice()).append(" date ").append(employee.getOrder().getOrderDate()).append("\n");
            serializator.serialize(deliveryService);
            deliveryService.generateBill(order);
            alert.alert("Successful order", "The order was placed successfully", "Thank you for your order! :)");
            clearOrder();
        } else {
            alert.alert("ERROR", "Empty order.", "No products were added to the order");
        }
    }

    public void clearProducts() {
        deliveryService.orders.remove(order);
        selectedProducts.clear();
        price = 0;
        orderID = 0;
        setTableView();
        totalPrice.setVisible(false);
        clearCart.setVisible(false);
        orderButton.setVisible(false);
        priceButton.setVisible(false);
        cart.setVisible(false);
        setTableView();
    }

    @FXML
    private Button logInAdminButton;
    @FXML
    private TextField usernameAdmin, passwordAdmin;

    @FXML
    private Button importProductsButton, addProductButton, insertButton;
    @FXML
    private Label addNameLabel, addRatingLabel, addCaloriesLabel, addProteinLabel, addFatLabel, addSodiumLabel, addPriceLabel;
    @FXML
    private TextField addNameField, addRatingField, addCaloriesField, addProteinField, addFatField, addSodiumField, addPriceField;

    @FXML
    private Button deleteProductButton, deleteButton;
    @FXML
    private Label deleteNameLabel;
    @FXML
    private TextField deleteNameField;

    @FXML
    private Button modifyProductButton, modifyButton;
    @FXML
    private Label modifyNameLabel, modifyRatingLabel, modifyCaloriesLabel, modifyProteinLabel, modifyFatLabel, modifySodiumLabel, modifyPriceLabel;
    @FXML
    private TextField modifyNameField, modifyRatingField, modifyCaloriesField, modifyProteinField, modifyFatField, modifySodiumField, modifyPriceField;

    public void logInAdmin() {
        if (!usernameAdmin.getText().equals("") && !passwordAdmin.getText().equals("")) {
            if (deliveryService.administrator.getUsername().equals(usernameAdmin.getText())) {
                if (deliveryService.administrator.getPassword().equals(passwordAdmin.getText())) {
                    hideAdminElements();
                } else {
                    alert.alert("Wrong Password", "The introduced password is incorrect", "Please introduce a valid password.");
                }
            } else {
                alert.alert("Invalid Username", "The introduced username does not exist", "Please introduce a valid username.");
            }
        } else {
            alert.alert("Empty Input", "You have not introduced the required data", "Please introduce valid data in all fields.");
        }
    }

    public void hideAdminElements() {
        usernameAdmin.setText("");
        passwordAdmin.setText("");
        logInAdminButton.setVisible(false);
        usernameAdmin.setVisible(false);
        passwordAdmin.setVisible(false);
        importProductsButton.setVisible(true);
        addProductButton.setVisible(true);
        deleteProductButton.setVisible(true);
        modifyProductButton.setVisible(true);
        createCompositeButton.setVisible(true);
        generateReport1.setVisible(true);
        generateReport2.setVisible(true);
        generateReport3.setVisible(true);
        generateReport4.setVisible(true);
    }

    public void setImportProductsButton() {
        hideAddProductButton();
        hideDeleteProductButton();
        hideModifyProductButton();
        deliveryService.importProducts();
        serializator.serialize(deliveryService);
        alert.alert("Import update", "Successful import", "The products were successfully imported into the menu");
    }

    public void setAddProductButton() {
        hideDeleteProductButton();
        hideModifyProductButton();
        addNameLabel.setVisible(true);
        addRatingLabel.setVisible(true);
        addCaloriesLabel.setVisible(true);
        addProteinLabel.setVisible(true);
        addFatLabel.setVisible(true);
        addSodiumLabel.setVisible(true);
        addPriceLabel.setVisible(true);
        addNameField.setVisible(true);
        addRatingField.setVisible(true);
        addCaloriesField.setVisible(true);
        addProteinField.setVisible(true);
        addFatField.setVisible(true);
        addSodiumField.setVisible(true);
        addPriceField.setVisible(true);
        insertButton.setVisible(true);
    }

    public boolean validateAddInput() {
        if (!addNameField.getText().equals("") && !addRatingField.getText().equals("") && !addCaloriesField.getText().equals("") && !addProteinField.getText().equals("") && !addFatField.getText().equals("") && !addSodiumField.getText().equals("") && !addPriceField.getText().equals("")) {
            return true;
        } else
            alert.alert("Empty Input", "You have not introduced the required data", "Please introduce valid data in all fields.");
        return false;
    }

    public void hideAddProductButton() {
        addNameLabel.setVisible(false);
        addRatingLabel.setVisible(false);
        addCaloriesLabel.setVisible(false);
        addProteinLabel.setVisible(false);
        addFatLabel.setVisible(false);
        addSodiumLabel.setVisible(false);
        addPriceLabel.setVisible(false);
        addNameField.setVisible(false);
        addRatingField.setVisible(false);
        addCaloriesField.setVisible(false);
        addProteinField.setVisible(false);
        addFatField.setVisible(false);
        addSodiumField.setVisible(false);
        addPriceField.setVisible(false);
        insertButton.setVisible(false);

    }

    public void addProduct() {
        if (validateAddInput()) {
            MenuItem menuItem = new BaseProduct(addNameField.getText(), Double.parseDouble(addRatingField.getText()), Integer.parseInt(addCaloriesField.getText()), Integer.parseInt(addProteinField.getText()), Integer.parseInt(addFatField.getText()), Integer.parseInt(addSodiumField.getText()), Float.parseFloat(addPriceField.getText()));
            if (!deliveryService.addProduct(menuItem)) {
                alert.alert("ERROR", "The introduced product already exists in the menu.", "");
            } else {
                serializator.serialize(deliveryService);
                alert.alert("Successful insert", "The product was successfully created and inserted into the menu.", "");
                hideAddProductButton();
            }
        }
    }

    public void hideDeleteProductButton() {
        deleteNameLabel.setVisible(false);
        deleteNameField.setText("");
        deleteNameField.setVisible(false);
        deleteButton.setVisible(false);
    }

    public void setDeleteProductButton() {
        hideAddProductButton();
        hideModifyProductButton();
        deleteProductButton.setVisible(true);
        deleteNameLabel.setVisible(true);
        deleteNameField.setVisible(true);
        deleteButton.setVisible(true);
    }

    public MenuItem searchProductInMenu(TextField textField) {
        for (MenuItem menuItem : deliveryService.menu) {
            if (menuItem.getName().equals(textField.getText())) {
                return menuItem;
            }
        }
        return null;
    }

    public void deleteProduct() {
        MenuItem deleteItem = new MenuItem();
        if (!deleteNameField.getText().equals("")) {
            System.out.println("delete name field " + deleteNameField.getText());
            deleteItem = searchProductInMenu(deleteNameField);
            System.out.println("Delete item is " + deleteItem.getName());
            if (deleteItem != null) {
                deliveryService.deleteProduct(deleteItem);
                serializator.serialize(deliveryService);
                hideDeleteProductButton();
                alert.alert("Successful delete", "The product was successfully deleted from the menu.", "");
            } else {
                alert.alert("Invalid input", "The introduced product does not exist.", "Please introduce a valid name.");
                deleteNameField.setText("");
            }

        } else
            alert.alert("Empty Input", "You have not introduced the required data", "Please introduce valid data in all fields.");
    }

    public void setModifyProductButton() {
        hideAddProductButton();
        hideDeleteProductButton();
        modifyNameLabel.setVisible(true);
        modifyRatingLabel.setVisible(true);
        modifyCaloriesLabel.setVisible(true);
        modifyProteinLabel.setVisible(true);
        modifyFatLabel.setVisible(true);
        modifySodiumLabel.setVisible(true);
        modifyPriceLabel.setVisible(true);
        modifyNameField.setVisible(true);
        modifyRatingField.setVisible(true);
        modifyCaloriesField.setVisible(true);
        modifyProteinField.setVisible(true);
        modifyFatField.setVisible(true);
        modifySodiumField.setVisible(true);
        modifyPriceField.setVisible(true);
        modifyButton.setVisible(true);
    }

    public void hideModifyProductButton() {
        modifyNameLabel.setVisible(false);
        modifyRatingLabel.setVisible(false);
        modifyCaloriesLabel.setVisible(false);
        modifyProteinLabel.setVisible(false);
        modifyFatLabel.setVisible(false);
        modifySodiumLabel.setVisible(false);
        modifyPriceLabel.setVisible(false);
        modifyNameField.setText("");
        modifyNameField.setVisible(false);
        modifyRatingField.setText("");
        modifyRatingField.setVisible(false);
        modifyCaloriesField.setText("");
        modifyCaloriesField.setVisible(false);
        modifyProteinField.setText("");
        modifyProteinField.setVisible(false);
        modifyFatField.setText("");
        modifyFatField.setVisible(false);
        modifySodiumField.setText("");
        modifySodiumField.setVisible(false);
        modifyPriceField.setText("");
        modifyPriceField.setVisible(false);
        modifyButton.setVisible(false);
    }

    public boolean validateModifyInput() {
        if (!modifyNameField.getText().equals("")) {
            if (!modifyCaloriesField.getText().equals("") || !modifyRatingField.getText().equals("") || !modifyProteinField.getText().equals("") || !modifyFatField.getText().equals("") || !modifySodiumField.getText().equals("") || !modifyPriceField.getText().equals("")) {
                return true;
            }
        }
        return false;
    }

    public void modifyProduct() {
        if (validateModifyInput()) {
            MenuItem oldProduct = searchProductInMenu(modifyNameField);

            if (oldProduct == null) {
                alert.alert("ERROR", "Introduced product does not exist in the menu.", "");
            } else {
                MenuItem newProduct = new MenuItem();
                newProduct.setName(oldProduct.getName());
                System.out.println("Old product " + oldProduct.getName() + " rating " + oldProduct.getRating() + " price " + oldProduct.getPrice());
                if (!modifyRatingField.getText().equals("")) {
                    newProduct.setRating(Double.parseDouble(String.valueOf(modifyRatingField.getText())));
                } else {
                    newProduct.setRating(oldProduct.getRating());
                }
                if (!modifyCaloriesField.getText().equals("")) {
                    newProduct.setCalories(Integer.parseInt(String.valueOf(modifyCaloriesField.getText())));
                } else {
                    newProduct.setCalories(oldProduct.getCalories());
                }
                if (!modifyProteinField.getText().equals("")) {
                    newProduct.setProtein(Integer.parseInt(String.valueOf(modifyProteinField.getText())));
                } else {
                    newProduct.setProtein(oldProduct.getProtein());
                }
                if (!modifyFatField.getText().equals("")) {
                    newProduct.setFat(Integer.parseInt(String.valueOf(modifyFatField.getText())));
                } else {
                    newProduct.setFat(oldProduct.getFat());
                }
                if (!modifySodiumField.getText().equals("")) {
                    newProduct.setSodium(Integer.parseInt(String.valueOf(modifySodiumField.getText())));
                } else {
                    newProduct.setSodium(oldProduct.getSodium());
                }
                if (!modifyPriceField.getText().equals("")) {
                    newProduct.setPrice(Float.parseFloat(String.valueOf(modifyPriceField.getText())));
                } else {
                    newProduct.setPrice(oldProduct.getPrice());

                }

                if (!deliveryService.menu.contains(newProduct)) {
                    System.out.println("Old product " + oldProduct.getName() + oldProduct.getPrice() + oldProduct.getRating());
                    alert.alert("Successful modify", "New product : " + newProduct.getName() + ", price " + newProduct.getPrice() + ", rating " + newProduct.getRating() + ", calories " + newProduct.getCalories() + ", protein " + newProduct.getProtein() + ", fat " + newProduct.getFat() + ", sodium " + newProduct.getSodium(), "Old product: " + oldProduct.getName() + ", price " + oldProduct.getPrice() + ", rating " + oldProduct.getRating() + ", calories " + oldProduct.getCalories() + ", protein " + oldProduct.getProtein() + ", fat " + oldProduct.getFat() + ", sodium " + oldProduct.getSodium());
                    deliveryService.modifyProduct(oldProduct, newProduct);
                    serializator.serialize(deliveryService);
                    hideModifyProductButton();
                } else {
                    alert.alert("ERROR", "The modified product already exists in the menu.", "");
                }
            }
        }
    }

    @FXML
    private Button createCompositeButton;
    ObservableList<MenuItem> observableListBase = FXCollections.observableArrayList();
    @FXML
    private TableView<MenuItem> productsTable;
    @FXML
    private TableColumn<Object, Object> nameBase, ratingBase, caloriesBase, proteinBase, fatBase, sodiumBase, priceBase;
    @FXML
    private TextArea compositeElements;
    @FXML
    private Label label1, label2, label3, label4, label5, label6;
    @FXML
    private TextField compositeName, compositeSodium, compositeFat, compositeProtein, compositeCalories, compositePrice;
    @FXML
    private Button createCompositeProduct, deleteAllBaseProducts, addBaseProduct;

    public void setCreateCompositeButton(ActionEvent event) {
        setScene(event, "/CompositeProductUserInterface.fxml");
        compositeProduct = new CompositeProduct();
    }

    public void setCompositeElements() {
        setTableBaseProducts();
        compositeElements.setVisible(true);
        label1.setVisible(true);
        label2.setVisible(true);
        label3.setVisible(true);
        label4.setVisible(true);
        label5.setVisible(true);
        label6.setVisible(true);
        compositeName.setVisible(true);
        compositeSodium.setVisible(true);
        compositeFat.setVisible(true);
        compositeProtein.setVisible(true);
        compositeCalories.setVisible(true);
        compositePrice.setVisible(true);
        addBaseProduct.setVisible(true);
        createCompositeProduct.setVisible(true);
        deleteAllBaseProducts.setVisible(true);
    }

    public void hideCompositeElements() {
        productsTable.setVisible(false);
        compositeElements.setVisible(false);
        label1.setVisible(false);
        label2.setVisible(false);
        label3.setVisible(false);
        label4.setVisible(false);
        label5.setVisible(false);
        label6.setVisible(false);
        compositeName.setText("");
        compositeName.setVisible(false);
        compositeSodium.setText("");
        compositeSodium.setVisible(false);
        compositeFat.setText("");
        compositeFat.setVisible(false);
        compositeProtein.setText("");
        compositeProtein.setVisible(false);
        compositeCalories.setText("");
        compositeCalories.setVisible(false);
        compositePrice.setText("");
        compositePrice.setVisible(false);
        addBaseProduct.setVisible(false);
        createCompositeProduct.setVisible(false);
        deleteAllBaseProducts.setVisible(false);
    }

    public void setTableBaseProducts() {
        try {
            for (MenuItem menuItem : deliveryService.menu) {
                observableListBase.add(new MenuItem(menuItem.getName(), menuItem.getRating(), menuItem.getCalories(), menuItem.getProtein(), menuItem.getFat(), menuItem.getSodium(), menuItem.getPrice()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        nameBase.setCellValueFactory(new PropertyValueFactory<>("name"));
        ratingBase.setCellValueFactory(new PropertyValueFactory<>("rating"));
        caloriesBase.setCellValueFactory(new PropertyValueFactory<>("calories"));
        proteinBase.setCellValueFactory(new PropertyValueFactory<>("protein"));
        fatBase.setCellValueFactory(new PropertyValueFactory<>("fat"));
        sodiumBase.setCellValueFactory(new PropertyValueFactory<>("sodium"));
        priceBase.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setItems(observableListBase);
        productsTable.setVisible(true);
        compositeElements.setText("");
    }

    public void getBaseProducts() {
        MenuItem menuItem = productsTable.getSelectionModel().getSelectedItem();
        compositeProduct.addProduct(menuItem);
        compositeElements.appendText(menuItem.getName() + ",\t price " + menuItem.getPrice() + "\n");
        int fat = 0, sodium = 0, protein = 0, calories = 0;
        float price = 0;
        for (MenuItem item : compositeProduct.getProducts()) {
            fat += item.getFat();
            sodium += item.getSodium();
            protein += item.getProtein();
            calories += item.getCalories();
            price = compositeProduct.computePrice();
        }
        compositeFat.setText(String.valueOf(fat));
        compositeSodium.setText(String.valueOf(sodium));
        compositeProtein.setText(String.valueOf(protein));
        compositeCalories.setText(String.valueOf(calories));
        compositePrice.setText(String.valueOf(price));
        compositeProduct.setPrice(price);
        compositeProduct.setProtein(protein);
        compositeProduct.setFat(fat);
        compositeProduct.setSodium(sodium);
        compositeProduct.setCalories(calories);
    }

    public void createCompositeProduct() {
        if (compositeProduct.getProducts().size() == 0) {
            alert.alert("Empty input", "No products were selected.", "Please select products to be added to the composite product.");
        } else if (compositeName.getText().equals("")) {
            alert.alert("ERROR", "No name introduced for the composite product", "Please introduce a name for the composite product");
        } else {
            compositeProduct.setName(compositeName.getText());
            deliveryService.addProduct(compositeProduct);
            serializator.serialize(deliveryService);
            compositeProduct = new CompositeProduct();
            alert.alert("Successful insert", "The product was successfully created and inserted into the menu.", "");
            hideCompositeElements();
        }
    }

    public void deleteSelectedBaseProducts() {
        compositeName.setText("");
        compositeSodium.setText("");
        compositeFat.setText("");
        compositeProtein.setText("");
        compositeCalories.setText("");
        compositePrice.setText("");
        compositeElements.setText("");
    }

    @FXML
    private Button generateReport1, generate1, generateReport2, generate2, generateReport3, generate3, generateReport4, generate4;
    @FXML
    private TextField field11, field12, field21, field31, field32, field41;

    public void generateReport1() {
        field11.setVisible(true);
        field12.setVisible(true);
        generate1.setVisible(true);
    }

    public void generate1() {
        if (!field11.getText().equals("") && !field12.getText().equals("")) {
            int startHour = Integer.parseInt(field11.getText());
            int endHour = Integer.parseInt(field12.getText());
            if (deliveryService.generateReportTimeInterval(startHour, endHour)) {
                alert.alert("Successful Report", "Report created successfully.", "");
                field11.setVisible(false);
                field12.setVisible(false);
                generate1.setVisible(false);
            } else {
                alert.alert("Unsuccessful Report", "There were no orders added to the report.", "");
            }
        } else alert.alert("ERROR", "Empty input", "Please introduce data in all fields.");
    }

    public void generateReport2() {
        field21.setVisible(true);
        generate2.setVisible(true);
    }

    public void generate2() {
        if (!field21.getText().equals("")) {
            int numberOfOrders = Integer.parseInt(field21.getText());
            if (deliveryService.generateReportFrequentlyOrderedProducts(numberOfOrders)) {
                alert.alert("Successful Report", "Report created successfully.", "");
                field21.setVisible(false);
                generate2.setVisible(false);
            } else {
                alert.alert("Unsuccessful Report", "There were no products added to the report.", "");
            }
        } else alert.alert("ERROR", "Empty input", "Please introduce data in all fields.");

    }

    public void generateReport3() {
        field31.setVisible(true);
        field32.setVisible(true);
        generate3.setVisible(true);
    }

    public void generate3() {
        if (!field31.getText().equals("") && !field32.getText().equals("")) {
            int numberOfOrders = Integer.parseInt(field31.getText());
            int amount = Integer.parseInt(field32.getText());
            if (deliveryService.generateReportClientAndAmount(numberOfOrders, amount)) {
                alert.alert("Successful Report", "Report created successfully.", "");
                field31.setVisible(false);
                field32.setVisible(false);
                generate1.setVisible(false);
            } else {
                alert.alert("Unsuccessful Report", "There were no clients added to the report.", "");
            }
        } else alert.alert("ERROR", "Empty input", "Please introduce data in all fields.");
    }

    public void generateReport4() {
        field41.setVisible(true);
        generate4.setVisible(true);
    }

    public void generate4() {
        if (!field41.getText().equals("")) {
            int day = Integer.parseInt(field41.getText());
            if (deliveryService.generateReportsProductsToday(day)) {
                alert.alert("Successful Report", "Report created successfully.", "");
                field41.setVisible(false);
                generate4.setVisible(false);
            } else {
                alert.alert("Unsuccessful Report", "There were no products added to the report.", "");
            }
        } else alert.alert("ERROR", "Empty input", "Please introduce data in all fields.");


    }

    public void checkOrders() {
        employeeOrders.appendText(String.valueOf(employeeInfo));
        checkOrders.setVisible(false);
        employeeOrders.setVisible(true);
    }
}

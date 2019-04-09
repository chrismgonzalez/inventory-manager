/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import model.inventory;
import model.part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class AddProductController implements Initializable {

   inventory inv;

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField count;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField search;
    @FXML
    private TableView<part> partSearchTable;
    @FXML
    private TableView<part> assocPartsTable;

    private ObservableList<part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<part> assocPartList = FXCollections.observableArrayList();
    ArrayList<Integer> partIDList;

    public AddProductController(inventory inv) {
        this.inv = inv;
        partIDList = inv.retrievePartsIDList();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateProductID();
        populateSearchTable();
    }

    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

    @FXML
    private void searchForPart(MouseEvent event) {
        if (search.getText() != null && search.getText().trim().length() != 0) {
            partsInventorySearch.clear();
            for (int i = 0; i < inv.partListSize(); i++) {
                if (inv.lookUpPart(partIDList.get(i)).getName().contains(search.getText().trim())) {
                    partsInventorySearch.add(inv.lookUpPart(partIDList.get(i)));
                }
            }
            partSearchTable.setItems(partsInventorySearch);
        }
    }

    @FXML
    private void addPart(MouseEvent event) {
        part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart != null) {
            int id = addPart.getPartID();
            for (int i = 0; i < assocPartList.size(); i++) {
                if (assocPartList.get(i).getPartID() == id) {
                    alertMessage.errorProduct(2, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);

            }
            assocPartsTable.setItems(assocPartList);
        }
    }

    @FXML
    private void deletePart(MouseEvent event
    ) {
        part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = alertMessage.confirmationWindow(removePart.getName());
            if (remove) {
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            alertMessage.infoWindow(1, removePart.getName());
        } else {
            alertMessage.infoWindow(2, "");
        }

    }


    @FXML
    private void cancelAddProduct(MouseEvent event
    ) {
        boolean cancel = alertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }

    @FXML
    private void saveAddProduct(MouseEvent event
    ) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        double minCost = 0;
        for (int i = 0; i < assocPartList.size(); i++) {
            minCost += assocPartList.get(i).getPrice();
        }
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
            alertMessage.errorProduct(4, name);
            return;
        }
        for (TextField fieldCount1 : fieldCount) {
            boolean valueError = checkValue(fieldCount1);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount1);
            if (typeError) {
                end = true;
                break;
            }
        }
        if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            alertMessage.errorProduct(10, min);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            alertMessage.errorProduct(8, count);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            alertMessage.errorProduct(9, count);
            return;
        }
        if (Double.parseDouble(price.getText().trim()) < minCost) {
            alertMessage.errorProduct(6, price);
            return;
        }
        if (assocPartList.isEmpty()) {
            alertMessage.errorProduct(7, null);
            return;
        }

        saveProduct();
        mainScreen(event);

    }

    private void fieldError(TextField field) {
        if (field != null) {
            field.setStyle("-fx-border-color: red");
        }
    }

    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(count.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (int i = 0; i < assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }

        inv.addProduct(product);

    }

    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");

    }

    private void generateProductID() {
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);
        id.setText(num.toString());

    }

    private void populateSearchTable() {
        if (partIDList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < partIDList.size(); i++) {
                partsInventory.add(inv.lookUpPart(partIDList.get(i)));
            }
        }

        partSearchTable.setItems(partsInventory);
    }

    @FXML
    void clearField(MouseEvent event) {
        search.setText("");
        if (!partsInventory.isEmpty()) {
            partSearchTable.setItems(partsInventory);
        }

    }

    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScreen.fxml"));
            mainScreenController controller = new mainScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                alertMessage.errorProduct(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                alertMessage.errorProduct(5, field);
                error = true;
            }
        } catch (NumberFormatException e) {
            error = true;
            alertMessage.errorProduct(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            alertMessage.errorProduct(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            alertMessage.errorProduct(3, field);
            return true;
        }
        return false;

    }   
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import model.inHouse;
import model.inventory;
import model.outSourced;
import model.part;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chris
 */
public class AddPartController implements Initializable {

    inventory inv;
    
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField count;
    @FXML
    private TextField price;
    @FXML
    private TextField max;
    @FXML
    private TextField company;
    @FXML
    private Label companyLabel;
    @FXML
    private TextField min;

    public AddPartController(inventory inv) {
        this.inv = inv;
    }
    
    
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartID();
        resetFields();
    }   
    
    private void resetFields() {
        name.setText("Part Name");
        count.setText("Inv Count");
        price.setText("Part Price");
        min.setText("Min");
        max.setText("Max");
        company.setText("Machine ID");
        companyLabel.setText("Machine ID");
        inHouseRadio.setSelected(true);
        
    }
       private void generatePartID() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);

        if (inv.partListSize() == 0) {
            id.setText(num.toString());

        }
        if (inv.partListSize() == 1000) {
            alertMessage.errorPart(3, null);
        } else {
            match = generateNum(num);

            if (match == false) {
                id.setText(num.toString());
            } else {
                generatePartID();
            }
        }
    }

    private boolean generateNum(Integer num) {
        part match = inv.lookUpPart(num);
        return match != null;
    }

    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

    @FXML
    private void selectInHouse(MouseEvent event) {
        companyLabel.setText("Machine ID");
        company.setText("Machine ID");
    }

    @FXML
    private void selectOutSourced(MouseEvent event) {
        companyLabel.setText("Company Name");
        company.setText("Company Name");
    }

    @FXML
    private void idDisabled(MouseEvent event) {
    }

    @FXML
    private void cancelAddPart(MouseEvent event) {
        boolean cancel = alertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }

    @FXML
    private void saveAddPart(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
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
            if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
                alertMessage.errorPart(4, name);
                return;
            }
            if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                alertMessage.errorPart(8, min);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                alertMessage.errorPart(6, count);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                alertMessage.errorPart(7, count);
                return;
            }
            if (end) {
                return;
            } else if (company.getText().trim().isEmpty() || company.getText().trim().toLowerCase().equals("company name")) {
                alertMessage.errorPart(3, company);
                return;

            } else if (inHouseRadio.isSelected() && !company.getText().trim().matches("[0-9]*")) {
                alertMessage.errorPart(3, company);
                return;
            } else if (inHouseRadio.isSelected()) {
                addInHouse();

            } else if (outSourcedRadio.isSelected()) {
                addOutSourced();

            }

        } else {
            alertMessage.errorPart(2, null);
            return;

        }
        mainScreen(event);
    }

    private void addInHouse() {
        inv.addPart(new inHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), (Integer.parseInt(company.getText().trim()))));

    }

    private void addOutSourced() {
        inv.addPart(new outSourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim()));

    }

    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");
        company.setStyle("-fx-border-color: lightgray");
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
            if (field.getText().trim().isEmpty() | field.getText().trim() == null) {
                alertMessage.errorPart(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                alertMessage.errorPart(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            alertMessage.errorPart(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            alertMessage.errorPart(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            alertMessage.errorPart(3, field);
            return true;
        }
        return false;
    } 
}

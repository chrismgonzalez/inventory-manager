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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

    public class ModifyPartController implements Initializable {

    inventory inv;
    part part;

    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;
    @FXML
    private Label companyLabel;
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
    private TextField company;
    @FXML
    private Button modifyPartSaveButton;

    public ModifyPartController(inventory inv, part part) {
        this.inv = inv;
        this.part = part;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setData();
    }

    private void setData() {

        if (part instanceof inHouse) {

            inHouse part1 = (inHouse) part;
            inHouseRadio.setSelected(true);
            companyLabel.setText("Machine ID");
            this.name.setText(part1.getName());
            this.id.setText((Integer.toString(part1.getPartID())));
            this.count.setText((Integer.toString(part1.getInStock())));
            this.price.setText((Double.toString(part1.getPrice())));
            this.min.setText((Integer.toString(part1.getMin())));
            this.max.setText((Integer.toString(part1.getMax())));
            this.company.setText((Integer.toString(part1.getMachineID())));

        }

        if (part instanceof outSourced) {

            outSourced part2 = (outSourced) part;
            outSourcedRadio.setSelected(true);
            companyLabel.setText("Company Name");
            this.name.setText(part2.getName());
            this.id.setText((Integer.toString(part2.getPartID())));
            this.count.setText((Integer.toString(part2.getInStock())));
            this.price.setText((Double.toString(part2.getPrice())));
            this.min.setText((Integer.toString(part2.getMin())));
            this.max.setText((Integer.toString(part2.getMax())));
            this.company.setText(part2.getCompanyName());
        }
    }

    @FXML
    private void clearTextField(MouseEvent event
    ) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

    @FXML
    private void selectInHouse(MouseEvent event
    ) {
        companyLabel.setText("Machine ID");

    }

    @FXML
    private void selectOutSourced(MouseEvent event
    ) {
        companyLabel.setText("Company Name");

    }

    @FXML
    private void idDisabled(MouseEvent event
    ) {
    }

    @FXML
    private void cancelModifyPart(MouseEvent event
    ) {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(event);
        } else {
            return;
        }
    }

    @FXML
    private void saveModifyPart(MouseEvent event
    ) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
            for (int i = 0; i < fieldCount.length; i++) {
                boolean valueError = checkValue(fieldCount[i]);
                if (valueError) {
                    end = true;
                    break;
                }
                boolean typeError = checkType(fieldCount[i]);
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
            } else if (outSourcedRadio.isSelected() && company.getText().trim().isEmpty()) {
                alertMessage.errorPart(1, company);
                return;
            } else if (inHouseRadio.isSelected() && !company.getText().matches("[0-9]*")) {
                alertMessage.errorPart(1, company);
                return;

            } else if (inHouseRadio.isSelected() & part instanceof inHouse) {
                updateItemInHouse();

            } else if (inHouseRadio.isSelected() & part instanceof outSourced) {
                updateItemInHouse();
            } else if (outSourcedRadio.isSelected() & part instanceof outSourced) {
                updateItemOutSourced();
            } else if (outSourcedRadio.isSelected() & part instanceof inHouse) {
                updateItemOutSourced();
            }

        } else {
            alertMessage.errorPart(2, null);
            return;

        }
        mainScreen(event);
    }

    private void updateItemInHouse() {
        inv.updatePart(new inHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), Integer.parseInt(company.getText().trim())));
    }

    private void updateItemOutSourced() {
        inv.updatePart(new outSourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
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
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
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

    private boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }  
    
}

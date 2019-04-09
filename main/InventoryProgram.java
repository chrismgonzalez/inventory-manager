/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import model.inHouse;
import model.inventory;
import model.outSourced;
import model.part;
import model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author chris
 */
public class InventoryProgram extends Application {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
	public void start(Stage stage) throws Exception {
		inventory inv = new inventory();
                addTestData(inv);
                
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/mainScreen.fxml"));
		view_controller.mainScreenController controller = new view_controller.mainScreenController(inv);
		loader.setController(controller);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

    void addTestData(inventory inv){
            //Add InHouse Parts
            part a1 = new inHouse(1, "Part A1", 2.99, 10, 5, 100, 101);
            part a2 = new inHouse(3, "Part A2", 4.99, 11, 5, 100, 103);
            part b = new inHouse(2, "Part B", 3.99, 9, 5, 100, 102);
            inv.addPart(a1);
            inv.addPart(b);
            inv.addPart(a2);
            inv.addPart(new inHouse(4, "Part A3", 5.99, 15, 5, 100, 104));
            inv.addPart(new inHouse(5, "Part A4", 6.99, 5, 5, 100, 105));
            //Add OutSourced Parts
            part o1 = new outSourced(6, "Part O1", 2.99, 10, 5, 100, "ACME Co.");
            part p = new outSourced(7, "Part P", 3.99, 9, 5, 100, "ACME Co.");
            part q = new outSourced(8, "Part Q", 2.99, 10, 5, 100, "FLORIDA Co.");
            inv.addPart(o1);
            inv.addPart(p);
            inv.addPart(q);
            inv.addPart(new outSourced(9, "Part R", 2.99, 10, 5, 100, "FLORIDA Co."));
            inv.addPart(new outSourced(10, "Part O2", 2.99, 10, 5, 100, "NY Co."));
            //Add Products
            Product prod1 = new Product(100, "Product 1", 9.99, 20, 5, 100);
            prod1.addAssociatedPart(a1);
            prod1.addAssociatedPart(o1);
            inv.addProduct(prod1);
            Product prod2 = new Product(200, "Product 2", 9.99, 29, 5, 100);
            prod2.addAssociatedPart(a2);
            prod2.addAssociatedPart(p);
            inv.addProduct(prod2);
            Product prod3 = new Product(300, "Product 3", 9.99, 30, 5, 100);
            prod3.addAssociatedPart(b);
            prod3.addAssociatedPart(q);
            inv.addProduct(prod3);
            inv.addProduct(new Product(400, "Product 4", 29.99, 20, 5, 100));
            inv.addProduct(new Product(500, "Product 5", 29.99, 9, 5, 100));

        }
    
}

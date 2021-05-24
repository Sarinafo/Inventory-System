package sample;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenu.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1200, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {





        Part part1 = new InHouse(1, "Brakes", 10.99, 6, 2, 12, 2);
        Part part2 = new Outsourced(2, "Wheel", 16.99, 5, 2, 10, "Acme");
        Part part3 = new InHouse(3, "Seat", 10.99, 15, 6, 20, 5);
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);

        Product product1 = new Product(1000, "Giant Bike", 299.99, 5, 2, 10);
        Product product2 = new Product(1001, "Tricycle", 99.99, 9, 2, 10);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);









        launch(args);
    }
}


package Controller;
/** This class creates an Inventory System Application
 *
 * @author Sarina Foreman
 *
 * A feature that I would add to the application would be creating a database.
 * */
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The MainMenuController class implements an inventory system that adds, deletes, and modifies parts and products.
 */
public class MainMenuController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private TextField searchBar;

    @FXML
    private TextField searchBarpr;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Part, Integer> partID;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Integer> partInventory;

    @FXML
    private TableColumn<Part, Double> partPrice;


    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productID;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Integer> productInventory;

    @FXML
    private TableColumn<Product, Double> productPrice;

    private ObservableList<Product> productInv = FXCollections.observableArrayList();

    /**
     * This method opens the Add part menu screen
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionAddPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * The method opens the Add Product Menu Screen.
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionAddProducts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * This method deletes a part from the Part Table
     * @param event
     */
    @FXML
    void OnActionDeletePart(ActionEvent event) {
        Part i = (Part)partTableView.getSelectionModel().getSelectedItem();
        if(i != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Please confirm deletion of: " + i.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                Inventory.deletePart(i);
            }
        }

        }

    /**
     * This method deletes a Product from the Product Table
     * @param event
     */
    @FXML
    void OnActionDeleteProducts(ActionEvent event) {
        Product removeProduct = productTableView.getSelectionModel().getSelectedItem();
        if(Inventory.getAllProducts().isEmpty() == true){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("A product must be entered before you may delete");
            alert.showAndWait();
        }
        else if(productTableView.getSelectionModel().isEmpty() == true){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Please select a part to be deleted");
            alert.showAndWait();
        }
        //prevents products with associated parts from being deleted
        else if(!removeProduct.getAllAssociatedParts().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                alert.setContentText("Please delete all parts associated with Product first");
                alert.showAndWait();
        }

        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Please confirm deletion of: " + removeProduct.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                Inventory.getAllProducts().remove(removeProduct);
            }
        }
    }


    /**
     * This method exits the program
     * @param event
     */
    @FXML
    void OnActionExit(ActionEvent event) {
        System.exit(0);

    }

    /**
     * This method opens the Modift Part Screen
     * @param event
     * @throws IOException
     */

    @FXML
    void OnActionModifyPart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPartMenu.fxml"));
        loader.load();
        ModifyPartMenuController MPMcontroller = loader.getController();

try{
        if(MPMcontroller != null) {
            MPMcontroller.sendPart(partTableView.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }


    } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Oops!");
    alert.setHeaderText(null);
    alert.setContentText("Please choose a part to Modify!");
    alert.showAndWait();
}
}

    /**
     * This method opens the Modify Parts Screen.
     * This method had an IOException error and I added the throws IOException to the method.
     * @param event
     * @throws IOException
     */
        @FXML
    void OnActionModifyProducts(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductMenu.fxml"));
            Parent a = loader.load();
            Scene i = new Scene(a);
            ModifyProductMenuController MPrMcontroller = loader.getController();
            MPrMcontroller.sendProducts(productTableView.getSelectionModel().getSelectedItem());
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(i);
            window.show();
        }

    /**
     * This method searches parts in the Search part bar
     * In this method I recieved a NumberFormatException error. I fixed the issue by creating a try and catch clause.
     * @param event
     */
    @FXML
    void SearchPartHandler(ActionEvent event) {
        ObservableList<Part> searchParts = FXCollections.observableArrayList();
        String parts = searchBar.getText();
        for (Part p : Inventory.getAllParts()) {
            if (p.getName().contains(parts))
                searchParts.add(p);

        }
        if(searchParts.isEmpty() ) {

            try {
                int partId = Integer.parseInt(parts);

                Part p = Inventory.lookupPart(partId);
                if (p == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error dialog");
                    alert.setContentText("Please enter a part");
                    alert.showAndWait();
                    return;
                }
                searchParts.add(p);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error dialog");
                alert.setContentText("Part not found");
                alert.showAndWait();
                return;
            }
        }

        partTableView.setItems(searchParts);
        partTableView.refresh();

    }


    /**
     * This method searches parts in the search product bar
     * @param event
     */
    @FXML
    void SearchProductHandler(ActionEvent event) {
        ObservableList<Product> searchProducts = FXCollections.observableArrayList();
        String products = searchBarpr.getText();
        for (Product i : Inventory.getAllProducts()) {
            if (i.getName().contains(products))
                searchProducts.add(i);
        }
        if(searchProducts.isEmpty() ) {
            try {
                int productId = Integer.parseInt(products);

                Product i = Inventory.lookupProduct(productId);
                if (i == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error dialog");
                    alert.setContentText("Please enter a part");
                    alert.showAndWait();
                    return;
                }
                searchProducts.add(i);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error dialog");
                alert.setContentText("Part not found");
                alert.showAndWait();
                return;
            }
        }

        productTableView.setItems(searchProducts);
        productTableView.refresh();
        }


    /**
     *This method sets parts and products in the columns on the main screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTableView.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));









    }

}
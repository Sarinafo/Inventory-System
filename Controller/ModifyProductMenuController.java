package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The ModifyProductMenuController implements a new screen that modifies existing products on the program.
 */
public class ModifyProductMenuController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField idtxt;

    @FXML
    private TextField nametxt;

    @FXML
    private TextField invtxt;

    @FXML
    private TextField pricetxt;

    @FXML
    private TextField maxtxt;

    @FXML
    private TextField mintxt;

    @FXML
    private TextField searchproduct;

    @FXML
    private TableColumn<Part, Integer> partID;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Integer> partInv;

    @FXML
    private TableColumn<Part, Double> partPrice;

    @FXML
    private TableView<Part> partstable;

    @FXML
    private TableView<Part> assoctable;

    @FXML
    private TableColumn<Part, Integer> assocId;

    @FXML
    private TableColumn<Part, String> assocName;

    @FXML
    private TableColumn<Part, Integer> assocInv;

    @FXML
    private TableColumn<Part, Double> assocPrice;

    private Product selectedProducts;
    private String errorMessage = "";
    Product newproduct = new Product(0,"",0.0,0,0,0);

    ObservableList<Part> productPart = FXCollections.observableArrayList();


    /**
     * This method adds a part associated with a product to the associated parts table
     * @param event
     */
    @FXML
    void addPartHandler(ActionEvent event) {
        Part selectedPart = partstable.getSelectionModel().getSelectedItem();
        newproduct.addAssociatedPart(selectedPart);
        assoctable.setItems(newproduct.getAllAssociatedParts());

        if (!assoctable.getItems().contains(selectedPart)) {
            Product.associatedParts.add(selectedPart);
        }
    }

    /**
     * This method cancels the modification of a product and returns to the main screen.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation dialog");
        alert.setContentText("Are you sure you would like to return to the Main Screen w/o saving?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


    /**
     * This method removes a part from the associated parts table
     * @param event
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        Part selectedPart = (Part)assoctable.getSelectionModel().getSelectedItem();

        if (selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Please confirm deletion of: " + selectedPart.getName());
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                Product.deleteAssociatedPart(selectedPart);
            }
        }
        //Error message if a part is not selected from assoc table to delete
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wait!");
            alert.setContentText("You have not selected a part to delete!");
            alert.showAndWait();
        }
    }

    /**
     * This method saves the new modified product to the products table on the main screen/
     * I received a NumberFormatException error. I fixed the issue by creathing a try and catch clause.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {

            Product newProduct = new Product(Integer.parseInt(idtxt.getText()), nametxt.getText(), Double.parseDouble(pricetxt.getText()), Integer.parseInt(invtxt.getText()), Integer.parseInt(mintxt.getText()), Integer.parseInt(maxtxt.getText()));
            int index = Inventory.getAllProducts().indexOf(selectedProducts);

            int id = Integer.parseInt(idtxt.getText());
            String name = nametxt.getText();
            double price = Double.parseDouble(pricetxt.getText());
            int inventory = Integer.parseInt(invtxt.getText());
            int min = Integer.parseInt(mintxt.getText());
            int max = Integer.parseInt(maxtxt.getText());

            //creates an error message for empty fields.
            errorMessage = Product.productValid(name, price, inventory, min, max, errorMessage);
            if (errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
                return;
            }

            //Error message if min is higher than max and vice versa.
            boolean b = Integer.parseInt(mintxt.getText()) >= Integer.parseInt(maxtxt.getText());
            //Error message if inventory is not between min and max.
            boolean a = Integer.parseInt(invtxt.getText()) > Integer.parseInt(maxtxt.getText()) || Integer.parseInt(invtxt.getText()) < Integer.parseInt(mintxt.getText());
            if (b == true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error dialog");
                alert.setContentText("Please enter a valid min and max value");
                alert.showAndWait();
                return;
            } else if (a == true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error dialog");
                alert.setContentText("Please enter a valid inventory between the min and max values");
                alert.showAndWait();
                return;
            }

            //updates product
            Inventory.updateProduct(index, newProduct);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

        catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error dialog");
                alert.setContentText("Please enter a valid value for each text field");
                alert.showAndWait();
        }
    }

    /**
     * This method sets the ID, name, Inv, and Price items in the text fields on the Modify Part Screen.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partstable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        assocId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInv.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        assocPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method sends data from the main menu screen parts table to the parts table on the Modify Product screen.
     * @param product
     */
    public void sendProducts(Product product){
        selectedProducts = product;
        idtxt.setText(String.valueOf(selectedProducts.getId()));
        nametxt.setText(selectedProducts.getName());
        invtxt.setText(String.valueOf(selectedProducts.getInventory()));
        pricetxt.setText(String.valueOf(selectedProducts.getPrice()));
        mintxt.setText(String.valueOf(selectedProducts.getMin()));
        maxtxt.setText(String.valueOf(selectedProducts.getMax()));
        assoctable.setItems(selectedProducts.getAllAssociatedParts());

    }
    @FXML
    void SearchHandler(ActionEvent event) {
        ObservableList<Part> searchParts = FXCollections.observableArrayList();
        String parts = searchproduct.getText();
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
        partstable.setItems(searchParts);
        partstable.refresh();
    }

}

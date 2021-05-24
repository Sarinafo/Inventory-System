package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;

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
 * The AddProductMenuController implements a screen that add news products to the program.
 */
public class AddProductMenuController implements Initializable {

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
    private TextField searchpartsbar;

    @FXML
    private TableColumn<Part, Integer> partID;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Inventory> partInv;

    @FXML
    private TableColumn<Part, Double> partPrice;

    @FXML
    private TableView<Part> addproducttable;

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

    @FXML
    private Button addbtn;
Product newproduct = new Product(0,"",0.0,0,0,0);

    static ObservableList<Part> productParts = FXCollections.observableArrayList();
    private String errorMessage = "";


    /**
     * This method adds a part from the part table to the associated parts table.
     * @param event
     */
    @FXML
    void onActionAddProduct(ActionEvent event) {
        Part selectedPart = addproducttable.getSelectionModel().getSelectedItem();
        newproduct.addAssociatedPart(selectedPart);
        assoctable.setItems(newproduct.getAllAssociatedParts());
        if (!assoctable.getItems().contains(selectedPart)) {
            productParts.add(selectedPart);
        }

    }


    /**
     * This method cancels adding a new product and returns to the home screen
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
     * This method removes parts from the associated parts table
     * @param event
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        Part selectedPart = assoctable.getSelectionModel().getSelectedItem();
        if (selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Please confirm deletion of: " + selectedPart.getName());
            Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    Product.deleteAssociatedPart(selectedPart);
                }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wait!");
            alert.setContentText("You have not selected a part to delete!");
            alert.showAndWait();
        }
    }


    /**
     * This method saves a new product and returns to the main mennu screen
     * I received a NumberFormatException error. I fixed the issue by creating a try and catch clause.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(idtxt.getText());
            String name = nametxt.getText();
            double price = Double.parseDouble(pricetxt.getText());
            int inventory = Integer.parseInt(invtxt.getText());
            int min = Integer.parseInt(mintxt.getText());
            int max = Integer.parseInt(maxtxt.getText());

            errorMessage = Product.productValid(name, price, inventory, min, max, errorMessage);
            if(errorMessage.length() > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage ="";
                return;
            }

            boolean b = Integer.parseInt(mintxt.getText()) >= Integer.parseInt(maxtxt.getText());
            boolean a = Integer.parseInt(invtxt.getText()) > Integer.parseInt(maxtxt.getText()) || Integer.parseInt(invtxt.getText()) < Integer.parseInt(mintxt.getText());
            if(b == true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error dialog");
                alert.setContentText("Please enter a valid min and max value");
                alert.showAndWait();
                return;
            }
            else if(a == true){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error dialog");
                alert.setContentText("Please enter a valid inventory between the min and max values");
                alert.showAndWait();
                return;
            }

            Inventory.addProduct(new Product(id, name, price, inventory, min, max));


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error dialog");
            alert.setContentText("Please complete all text fields!");
            alert.showAndWait();

        }
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method searches the parts in the parts table
     * @param event
     */
    @FXML
    void searchBarHandler(ActionEvent event) {
        ObservableList<Part> searchParts = FXCollections.observableArrayList();
        String parts = searchpartsbar.getText();
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
        addproducttable.setItems(searchParts);
        addproducttable.refresh();
    }

    /**
     * This method sets the parts and products in the appropriate columns in the tables.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idtxt.setText(String.valueOf(Inventory.getAllProducts().size()+1));
        addproducttable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        assocId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInv.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        assocPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}


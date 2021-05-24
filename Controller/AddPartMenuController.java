package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * The AddPartMenuController class implements a screen that adds new parts to the program.
 */
public class AddPartMenuController implements Initializable {

    Stage stage;
    Parent scene;

    private boolean isOutsourced;


    @FXML
    private RadioButton inhousebtn;

    @FXML
    private RadioButton outsourcebtn;

    @FXML
    private Label machinelbl;

    @FXML
    private ToggleGroup PartTGroup;

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
    private TextField machineidtxt;

    @FXML
    private TextField mintxt;

    Scanner keyboard = new Scanner(System.in);
    private String errorMessage = "";

    /**
     * This method cancels a part from being added and returns to the main screen.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation dialog");
        alert.setContentText("Are you sure you would like to return to the Main Screen w/o saving?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method is for the In-house radio button and it changes the text to Machine label when selected.
     * @param event
     */
    @FXML
    void InHouseRadioBtn(ActionEvent event) {
        if(inhousebtn.isSelected())
            machinelbl.setText("Machine ID");

        isOutsourced = false;
    }

    /**
     * This method is for the outsourced radio button and it changes the label to Company name when selected
     * @param event
     */
    @FXML
    void outsourcedRadioBtn(ActionEvent event) {
        if(outsourcebtn.isSelected())
            machinelbl.setText("Company Name");
    }



    /**
     * This method saves the part and adds it to the table on the main screen.
     * I recieved a NumberFormatException error. I fixed the issue by creating a try and catch lause
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(idtxt.getText());
            String name = nametxt.getText();
            int inventory = Integer.parseInt(invtxt.getText());
            double price = Double.parseDouble(pricetxt.getText());
            int max = Integer.parseInt(maxtxt.getText());
            int min = Integer.parseInt(mintxt.getText());
            boolean isInhouse;

            //error message for name, price, inv, min and max
            errorMessage = Part.partValid(name, price, inventory, min, max, errorMessage);
                if(errorMessage.length() > 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(errorMessage);
                    alert.showAndWait();
                    errorMessage ="";
                    return;
                }
            //min must be less than max and inv must be in between
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
            if(inhousebtn.isSelected()) {

                int machineid = Integer.parseInt(machineidtxt.getText());
                Inventory.addPart(new InHouse(id, name, price, inventory, min, max, machineid));
        }
            else {
                String companyname = machineidtxt.getText();
                while(!companyname.matches("[a-zA-Z ]+")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error dialog");
                    alert.setContentText("Please enter a valid Company Name");
                    alert.showAndWait();
                    return;
                }
                Inventory.addPart(new Outsourced(id, name, price, inventory, min, max, companyname));

            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error dialog");
            alert.setContentText("Please complete all text fields!");
            alert.showAndWait();
            return;

        }
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method gets the value of the ID text field
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    idtxt.setText(String.valueOf(Inventory.getAllParts().size()+1));





    }
}

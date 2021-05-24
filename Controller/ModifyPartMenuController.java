package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * The ModifyPartsMenuController implements a new screen that modifies parts in the program.
 */

public class ModifyPartMenuController {

    Stage stage;
    Parent scene;


    @FXML
    private ToggleGroup ToggleTGroup;

    @FXML
    private RadioButton inhousebtn;

    @FXML
    private RadioButton outsourcebtn;

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

    @FXML
    private Label machinelbl;

private Part selectedParts;
    private String errorMessage = "";

    /**
     * This method cancels the modification of a part and returns to the main menu.
     * @param event
     * @throws IOException
     */

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation dialog");
            alert.setContentText("Are you sure you would like to return to the Main Screen w/o saving?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method saves the new modifications to the part and returns to the main menu.
     * I received an IO exception error and I fixed it by adding the throws IO exception to the method.
     * @param event
     * @throws IOException
     */
        @FXML
    void onActionSave(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(idtxt.getText());
            String name = nametxt.getText();
            int inventory = Integer.parseInt(invtxt.getText());
            double price = Double.parseDouble(pricetxt.getText());
            int max = Integer.parseInt(maxtxt.getText());
            int min = Integer.parseInt(mintxt.getText());

            errorMessage = Part.partValid(name, price, inventory, min, max, errorMessage);
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

            if (inhousebtn.isSelected()) {
                InHouse newInhousePart = new InHouse(Integer.parseInt(idtxt.getText()), nametxt.getText(), Double.parseDouble(pricetxt.getText()),Integer.parseInt(invtxt.getText()), Integer.parseInt(mintxt.getText()), Integer.parseInt(maxtxt.getText()),Integer.parseInt(machineidtxt.getText()));
                int index = Inventory.getAllParts().indexOf(selectedParts);
                Inventory.updatePart(index, newInhousePart);
            }


            else if(outsourcebtn.isSelected()) {
                Outsourced newOutsourcedPart = new Outsourced(Integer.parseInt(idtxt.getText()), nametxt.getText(), Double.parseDouble(pricetxt.getText()), Integer.parseInt(invtxt.getText()), Integer.parseInt(mintxt.getText()), Integer.parseInt(maxtxt.getText()), machineidtxt.getText());
                int index = Inventory.getAllParts().indexOf(selectedParts);
                String companyname = machineidtxt.getText();
                while (!companyname.matches("[a-zA-Z ]+")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error dialog");
                    alert.setContentText("Please enter a valid Company Name");
                    alert.showAndWait();
                    return;

                }
                Inventory.updatePart(index, newOutsourcedPart);
            }
        } catch (Exception e) {
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
     * This method changes the label to Machine ID when the in-house button is selected.
     * @param event
     */
    @FXML
    void onActionInhouse(ActionEvent event) {
        if(inhousebtn.isSelected())
            machinelbl.setText("Machine ID");

    }

    /**
     * This method changes the Machine ID label to Company Name when the outsourced button is selected.
     * @param event
     */
    @FXML
    void onActionOutsourced(ActionEvent event) {
        if(outsourcebtn.isSelected())
            machinelbl.setText("Company Name");

    }

    /**
     * This method sends data from the main menu screen parts table to the Modify Parts Menu.
     * @param part
     */
    public void sendPart(Part part){
            selectedParts = part;
            idtxt.setText(String.valueOf(part.getId()));
            nametxt.setText(part.getName());
            invtxt.setText(String.valueOf(part.getInventory()));
            pricetxt.setText(String.valueOf(part.getPrice()));
            mintxt.setText(String.valueOf(part.getMin()));
            maxtxt.setText(String.valueOf(part.getMax()));



            if(part instanceof InHouse){
                machineidtxt.setText(String.valueOf(((InHouse) part).getMachineid()));
                machinelbl.setText("Machine ID");
                inhousebtn.setSelected(true);
            }
            else{
                machineidtxt.setText(((Outsourced) part).getCompanyName());
                machinelbl.setText("Company Name");
                outsourcebtn.setSelected(true);
            }



}}


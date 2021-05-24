package Model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class adds, updates, and deletes products for program.
 */
public class Product {


    private int id;
    private String name;
    private double price;
    private int inventory;
    private int min;
    private int max;


    public static ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public static ObservableList<Part> getAllAssociatedParts = FXCollections.observableArrayList();

    public Product(int id, String name, double price, int inventory, int min, int max) {
        this.id = id;
        this.name = name;
        this.inventory = inventory;
        this.price = price;
        this.min = min;
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void addAssociatedPart(Part part) {
        getAllAssociatedParts.add(part);
    }

    public static boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        getAllAssociatedParts.remove(selectedAssociatedPart);
        return true;
    }

    public ObservableList<Part> getAllAssociatedParts()
    {
        return getAllAssociatedParts;
    }

    public int getPartslistSize(){
        return associatedParts.size();
    }

    public static String productValid(String name, double price, int inventory, int min, int max, String productError) {
        if (name.length() == 0) {
            productError = ("Part name cannot be empty.");
        } else if (price <= 0) {
            productError = ("Part price cannot be less than 0");
        } else if (inventory <= 0) {
            productError = ("Part inventory cannot be less than 0 and must be b/w min and max");
        } else if (min <= 0) {
            productError = ("Part minimum must be greater than 0");
        } else if (max <= 0) {
            productError = ("Part maximum must be greater than 0 and min");
        }
        return productError;
    }
}
package Model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class adds, updates, and deletes the inventory for parts and products.
 */
public class Inventory {


    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //add items
    public static void addPart(Part newPart)
    {
        allParts.add(newPart);
    }
    public static void addProduct(Product newProduct)
    {
        allProducts.add(newProduct);
    }

    //lookup items

    public static Part lookupPart(int partId)
   {
       for(Part p: allParts){
           if(p.getId() == partId)
               return p;
       }
       return null;
   }

    public static Product lookupProduct(int productId)
    {
        for(Product i: allProducts){
            if(i.getId() == productId)
                return i;
        }
        return null;
    }
    public static ObservableList<Part> lookupPart(String parts)
   {
       ObservableList<Part> searchParts = FXCollections.observableArrayList();
       for (Part p : allParts) {
           if (p.getName().contains(parts))
               searchParts.add(p);
       }

       return searchParts;
   }
    public static ObservableList<Product> lookupProduct(String products)
   {
       ObservableList<Product> searchProducts = FXCollections.observableArrayList();
       for (Product i : allProducts) {
           if (i.getName().contains(products))
               searchProducts.add(i);
       }
       return allProducts;
   }
    //update items
    public static void updatePart(int index, Part selectedPart)
   {

       allParts.set(index, selectedPart);
   }
    public static void updateProduct(int index, Product newProduct)
   {
       allProducts.set(index, newProduct);
   }
    //delete parts
    public static boolean deletePart(Part selectedPart)
   {
       allParts.remove(selectedPart);
       return true;
   }
   //deletes products
   public static boolean deleteProduct(Product selectedProduct)
   {

       allProducts.remove(selectedProduct);
       return true;
   }
   //get all parts
   public static ObservableList<Part> getAllParts()
   {
       return allParts;
   }
   //get all products
   public static ObservableList<Product> getAllProducts()
   {
       return allProducts;
   }










}

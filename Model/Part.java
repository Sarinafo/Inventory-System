package Model;

/**
 * Supplied class Part.java
 */

/**
 *
 * @author Place Your Name Here
 */
public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int inventory;
    private int min;
    private int max;

    public Part(int id, String name, double price, int inventory, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the inventory
     */
    public int getInventory() {
        return inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    public static String partValid(String name, double price, int inventory, int min, int max, String partError) {
        if (name.length() == 0) {
            partError = ("Part name cannot be empty.");
        }
        else if (price <= 0){
            partError = ("Part price cannot be less than 0");
        }
        else if(inventory <=0){
            partError =("Part inventory cannot be less than 0 and must be b/w min and max");
        }
      else if(min <= 0){
          partError = ("Part minimum must be greater than 0");
        }
      else if (max <= 0){
          partError = ("Part maximum must be greater than 0 and min");
        }

        return partError;
    }
}
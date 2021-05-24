package Model;

/**
 * This class extends Part and adds functionalities for In-house button
 */
public class InHouse extends Part{

    private int machineid;

    public InHouse(int id, String name, double price, int inventory, int min, int max, int machineid) {
        super(id, name, price, inventory, min, max);
        this.machineid = machineid;
    }

    public int getMachineid() {
        return machineid;
    }

    public void setMachineid(int machineid) {
        this.machineid = machineid;
    }
}

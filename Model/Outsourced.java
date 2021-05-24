package Model;

/**
 * This class extends Part and adds functionalities for Outsourced button
 */
public class Outsourced extends Part{

    private String companyName;

    public Outsourced(int id, String name, double price, int inventory, int min, int max, String companyName) {
        super(id, name, price, inventory, min, max);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

package pl.edu.pk.biblioteka.data;

public class Charge {
    private int chargeId;
    private String type;
    private String description;
    private double amount;

    public Charge(int chargeId, String type, String description, double amount) {
        this.chargeId = chargeId;
        this.type = type;
        this.description = description;
        this.amount = amount;
    }

    public int getChargeId() {
        return chargeId;
    }

    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

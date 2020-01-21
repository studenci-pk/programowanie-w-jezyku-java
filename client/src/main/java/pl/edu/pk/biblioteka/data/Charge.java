package pl.edu.pk.biblioteka.data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return chargeId == ((Charge) o).chargeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Opłata %.2fzł", amount));
        if (type != null && !type.isEmpty()) {
            sb.append(String.format(", typ: %s", type));
        }
        if (description != null && !description.isEmpty()) {
            sb.append(String.format(", opis: %s", description));
        }
        return sb.toString();
    }

    public boolean isZero(double threshold){
        return amount >= -threshold && amount <= threshold;
    }
}

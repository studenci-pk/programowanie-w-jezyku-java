package pl.edu.pk.biblioteka.data;

import java.util.List;

public class Cart {
    private int accountId;
    private List<Integer> copyIds;

    public Cart(int accountId, List<Integer> copyIds) {
        this.accountId = accountId;
        this.copyIds = copyIds;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public List<Integer> getCopyIds() {
        return copyIds;
    }

    public void setCopyIds(List<Integer> copyIds) {
        this.copyIds = copyIds;
    }
}

package pl.edu.pk.biblioteka.dto;

import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.Copy;

import java.util.List;

public class CartDto {
    private Account account;
    private List<Copy> copies;

    public CartDto(Account account, List<Copy> copies) {
        this.account = account;
        this.copies = copies;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }
}

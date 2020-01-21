package pl.edu.pk.biblioteka.data;

import java.sql.Date;
import java.util.Optional;

public class Account {
    private int accountId;
    private String login;
    private String password;
    private String email;
    private Date createDate;
    private int permissionId;

    public Account(int accountId, String login, String password, String email, Date createDate, int permissionId) {
        this.accountId = accountId;
        this.login = login;
        this.password = password;
        this.email = email;
        this.createDate = createDate;
        this.permissionId = permissionId;
    }

    public Account(String login, String password, String email, Date createDate, int permissionId) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.createDate = createDate;
        this.permissionId = permissionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
}

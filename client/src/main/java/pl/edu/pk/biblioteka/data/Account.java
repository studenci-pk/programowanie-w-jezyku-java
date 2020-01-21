package pl.edu.pk.biblioteka.data;

public class Account {
    private int permissionId;
    private int accountId;
    private String login;
    private String email;
    private String createDate;

    public Account(int permissionId, int accountId, String login, String email, String createDate) {
        this.permissionId = permissionId;
        this.accountId = accountId;
        this.login = login;
        this.email = email;
        this.createDate = createDate;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return accountId == ((Account) o).accountId;
    }
}

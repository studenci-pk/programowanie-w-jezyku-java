package pl.edu.pk.biblioteka.data;

public class AccountHistory {
    private int accountId;
    private int loanId;

    public AccountHistory(int accountId, int loanId) {
        this.accountId = accountId;
        this.loanId = loanId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }
}

package pl.edu.pk.biblioteka.dto;

import pl.edu.pk.biblioteka.data.Account;
import java.util.Objects;

public class AccountHistoryDto {
    private Account account;
    private LoanDto loan;

    public AccountHistoryDto(Account account, LoanDto loan) {
        this.account = account;
        this.loan = loan;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LoanDto getLoan() {
        return loan;
    }

    public void setLoan(LoanDto loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccountHistoryDto that = (AccountHistoryDto) o;
        return Objects.equals(account, that.account) &&
                Objects.equals(loan, that.loan);
    }
}

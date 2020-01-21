package pl.edu.pk.biblioteka.data;

import java.sql.Date;

public class LoanDto {
    private int loanId;
    private Copy copy;
    private Account account;
    private ReservationStatus reservationStatus;
    private Charge charge;
    private Date reservationDate;
    private Date  expireDate;
    private String pickupPoint;
    private Date  startDate;
    private Date  endDate;

    public LoanDto(int loadId, Copy copy, Account account, ReservationStatus reservationStatus, Charge charge,
                   Date reservationDate, Date expireDate, String pickupPoint, Date startDate, Date endDate) {
        this.loanId = loadId;
        this.copy = copy;
        this.account = account;
        this.reservationStatus = reservationStatus;
        this.charge = charge;
        this.reservationDate = reservationDate;
        this.expireDate = expireDate;
        this.pickupPoint = pickupPoint;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return loanId == ((LoanDto) o).loanId;
    }

    @Override
    public String toString() {
        return "LoanDto{" +
                "copy=" + copy +
                ", reservationStatus=" + reservationStatus +
                ", charge=" + charge +
                ", reservationDate=" + reservationDate +
                ", expireDate=" + expireDate +
                ", pickupPoint='" + pickupPoint + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

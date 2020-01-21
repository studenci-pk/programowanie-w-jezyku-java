package pl.edu.pk.biblioteka.data;

public class ReservationStatus {
    public static final int FREE = 1;
    public static final int RESERVED = 2;
    public static final int LOANED = 3;

    private int reservationStatusId;
    private String reservationStatus;

    public ReservationStatus(int reservationStatusId, String reservationStatus) {
        this.reservationStatusId = reservationStatusId;
        this.reservationStatus = reservationStatus;
    }

    public int getReservationStatusId() {
        return reservationStatusId;
    }

    public void setReservationStatusId(int reservationStatusId) {
        this.reservationStatusId = reservationStatusId;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}

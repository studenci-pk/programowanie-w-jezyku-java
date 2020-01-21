package pl.edu.pk.biblioteka.data;

public class Copy {
    private int copyId;
    private int bookId;
    private boolean withdrawn;
    private int reservationStatusId; // Not in DB

    public Copy(int copyId, int bookId, boolean withdrawn, int reservationStatusId) {
        this.copyId = copyId;
        this.bookId = bookId;
        this.reservationStatusId = reservationStatusId;
        this.withdrawn = withdrawn;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }

    public int getReservationStatusId() {
        return reservationStatusId;
    }

    public void setReservationStatusId(int reservationStatusId) {
        this.reservationStatusId = reservationStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return copyId == ((Copy) o).copyId;
    }
}

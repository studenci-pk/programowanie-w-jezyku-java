package pl.edu.pk.biblioteka.controllers.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.controllers.AccountDetailsPaneController;
import pl.edu.pk.biblioteka.data.LoanDto;
import pl.edu.pk.biblioteka.data.ReservationStatus;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class LoanListCell extends ListCell<LoanDto> {
    private static final Logger logger = Logger.getLogger(LoanListCell.class.getName());

    private HBox content;
    private Text date1;
    private Text date2;
    private Text copy;
    private Button button;
    EventHandler<ActionEvent> handler;

    public LoanListCell(AccountDetailsPaneController accountDetailsPaneController) {
        super();
        date1 = new Text();
        date2 = new Text();
        copy = new Text();
        button = new Button("Przyjęto");
        button.setMinWidth(80);
        handler = (event) -> {
            logger.info("TODO: przyjęte");
            Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("reservation/accepted")
                    .addParameter("loanid", String.valueOf(getItem().getLoanId()))
                    .addParameter("reservationstatusid", String.valueOf(ReservationStatus.FREE))
                    .build();

            httpResponse.ifPresent(entity -> {
                ResponseHandler.getBuilder(entity)
                        .setOnSuccess(logger::info)
                        .setOnFailure(logger::error)
                        .setAtTheEnd(accountDetailsPaneController::reload)
                        .handle();
            });
        };
        button.addEventHandler(ActionEvent.ACTION, handler);

        VBox vBox = new VBox(date1, date2, copy);
        content = new HBox(vBox, button);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(LoanDto item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty &&
                item.getReservationStatus().getReservationStatusId() != ReservationStatus.FREE) {
            date1.setText(String.format("Data rezerwacji %s,", item.getReservationDate()));
            date2.setText(String.format("Data zwrotu %s,", item.getEndDate()));
            copy.setText(String.format("Id egzemplarza %d, id książki %d, status: %d",
                    item.getCopy().getCopyId(), item.getCopy().getBookId(),
                    item.getCopy().getReservationStatusId()));
            if (item.getReservationStatus().getReservationStatusId() == ReservationStatus.FREE) {
                button.setDisable(true);
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

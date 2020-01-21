package pl.edu.pk.biblioteka.controllers.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.LoanDto;

public class ClientLoanListCell extends ListCell<LoanDto> {
    private static final Logger logger = Logger.getLogger(ClientLoanListCell.class.getName());

    private HBox content;
    private Text account;
    private Text date;
    private Text copy;
    private Button button;
    EventHandler<ActionEvent> handler;

    public ClientLoanListCell() {
        super();
        account = new Text();
        date = new Text();
        copy = new Text();
        button = new Button("Prolonguj");
        button.setVisible(false);
        button.setMinWidth(80);
        handler = (event) -> {
            logger.info("TODO: prolonguj");
        };
        button.addEventHandler(ActionEvent.ACTION, handler);

        VBox vBox = new VBox(date, copy);
        content = new HBox(vBox, button);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(LoanDto item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty) {
//            account.setText(String.format("Login %s, PESEL %s, ",
//                    item.getAccount().getLogin(), item.getAccount().getPesel()));
            date.setText(String.format("Data rezerwacji %s,  data zwrotu %s",
                    item.getReservationDate(), item.getEndDate()));
            copy.setText(String.format("Id egzemplarza %d, id książki %d, status: %d",
                    item.getCopy().getCopyId(), item.getCopy().getBookId(),
                    item.getCopy().getReservationStatusId()));
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

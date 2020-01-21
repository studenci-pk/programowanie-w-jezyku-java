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
import pl.edu.pk.biblioteka.data.Charge;
import pl.edu.pk.biblioteka.data.LoanDto;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class ChargeListCell extends ListCell<LoanDto> {
    private static final Logger logger = Logger.getLogger(ChargeListCell.class.getName());

    private HBox content;
    private Text text;
    private Text copyText;
    private Button button;
    EventHandler<ActionEvent> handler;

    public ChargeListCell(AccountDetailsPaneController accountDetailsPaneController) {
        super();
        text = new Text();
        copyText = new Text();
        button = new Button("Opłacone");
        button.setMinWidth(80);
        handler = (event) -> {
            logger.info("TODO: opłacenie");
            Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("charge")
                    .addParameter("loanid", String.valueOf(getItem().getLoanId()))
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

        VBox vBox = new VBox(copyText, text);
        content = new HBox(vBox, button);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(LoanDto item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty &&
                !item.getCharge().isZero(0.00001)) {
            Charge c = item.getCharge();
            text.setText(c.toString());
            copyText.setText("Id egzemplarza: " + item.getCopy().getCopyId());
            if (c.isZero(0.00001)) {
                button.setDisable(true);
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

package pl.edu.pk.biblioteka.controllers.model;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.controllers.BrowseResourcesPaneController;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.ReservationStatus;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class CopyListCellClient extends ListCell<Copy> {
    private static final Logger logger = Logger.getLogger(CopyListCellClient.class.getName());

    private HBox content;
    private Text text;
    private Button button;

    public CopyListCellClient(BrowseResourcesPaneController browseResourcesPaneController) {
        super();
        text = new Text();
        button = new Button("Zamów");
        button.setPrefWidth(100);

        button.addEventHandler(ActionEvent.ACTION, (ignored) -> {
            Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("resources/reserve")
                    .addParameter("copyId", String.valueOf(getItem().getCopyId()))
                    .build();

            logger.info(httpResponse.isPresent());
            httpResponse.ifPresent(closeableHttpResponse -> ResponseHandler.getBuilder(closeableHttpResponse)
                    .setOnSuccess(entity -> logger.info(EntityUtils.toString(entity)))
                    .setOnFailure(logger::error)
                    .setAtTheEnd(browseResourcesPaneController::reload)
                    .handle());
        });

        VBox vBox = new VBox(text);
        content = new HBox(vBox, button);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(Copy item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty) {
            text.setText(item.toString());

            button.setDisable(item.getReservationStatusId() != ReservationStatus.FREE);
//            if (item.getReservationStatusId() == 1) {
//                button.setText("Zamów");
//            } else {
//                button.setText("Zarezerwuj");
//            }

            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

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
import pl.edu.pk.biblioteka.controllers.EditResourcesPaneController;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class CopyListCell extends ListCell<Copy> {
    private static final Logger logger = Logger.getLogger(CopyListCell.class.getName());

    private HBox content;
    private Text text;
    private Button button;
    EventHandler<ActionEvent> handler;

    public CopyListCell(EditResourcesPaneController editResourcesPaneController) {
        super();
        text = new Text();
        button = new Button("Usuń");
        button.setMinWidth(100);
        handler = (event) -> {
            Copy copy = getItem();
            if (copy != null) {
                Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("resources/remove")
                        .addParameter("copyid", String.valueOf(copy.getCopyId()))
                        .build();

                logger.info("httpResponse.isPresent() = " + httpResponse.isPresent());
                if (httpResponse.isPresent()) {
                    ResponseHandler.getBuilder(httpResponse.get())
                            .setOnSuccess((ignored) -> {
                                logger.info("succeed");
                                editResourcesPaneController.reload();
                                logger.info("realoaded");
                            })
                            .setOnFailure((ignored) -> logger.info("failure"))
                            .handle();
                }
            }
        };

        button.addEventHandler(ActionEvent.ACTION, handler);

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
            setGraphic(content);
        } else {
//            button.removeEventHandler(ActionEvent.ACTION, handler);
            setGraphic(null);
        }
    }
}

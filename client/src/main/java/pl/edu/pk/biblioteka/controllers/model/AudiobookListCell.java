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
import pl.edu.pk.biblioteka.data.Audiobook;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class AudiobookListCell extends ListCell<Audiobook> {
    private static final Logger logger = Logger.getLogger(AudiobookListCell.class.getName());

    private HBox content;
    private Text text;
    private Button button;
    EventHandler<ActionEvent> handler;

    public AudiobookListCell(EditResourcesPaneController editResourcesPaneController) {
        super();
        text = new Text();
        button = new Button("Usuń");
        button.setMinWidth(100);
        handler = (event) -> {
            Audiobook audiobook = getItem();
            if (audiobook != null) {
                Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("resources/remove")
                        .addParameter("audiobookid", String.valueOf(audiobook.getAudiobookId()))
                        .build();

                logger.info("httpResponse.isPresent() = " + httpResponse.isPresent());
                if (httpResponse.isPresent()) {
                    ResponseHandler.getBuilder(httpResponse.get())
                            .setOnSuccess((ignored) -> {
                                editResourcesPaneController.reload();
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
    protected void updateItem(Audiobook item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty) {
            text.setText(item.toString());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

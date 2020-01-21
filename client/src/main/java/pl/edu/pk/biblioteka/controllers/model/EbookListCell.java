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
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class EbookListCell extends ListCell<Ebook> {
    private static final Logger logger = Logger.getLogger(EbookListCell.class.getName());

    private HBox content;
    private Text text;
    private Button button;
    EventHandler<ActionEvent> handler;

    public EbookListCell(EditResourcesPaneController editResourcesPaneController) {
        super();
        text = new Text();
        button = new Button("Usuń");
        button.setMinWidth(100);
        handler = (event) -> {
            Ebook ebook = getItem();
            if (ebook != null) {
                Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("resources/remove")
                        .addParameter("ebookid", String.valueOf(ebook.getEbookId()))
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
    protected void updateItem(Ebook item, boolean empty) {
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

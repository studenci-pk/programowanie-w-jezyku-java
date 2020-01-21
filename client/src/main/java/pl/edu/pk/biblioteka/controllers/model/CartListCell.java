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
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.controllers.CartStageController;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.PostBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class CartListCell extends ListCell<String> {
    private static final Logger logger = Logger.getLogger(CartListCell.class);
    private HBox content;
    private Text text;
    private Button button;

    public CartListCell(CartStageController cartStageController) {
        super();

        text = new Text();
        button = new Button("Usuń");
        button.setMinWidth(80);
        button.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("cart/remove")
                        .addParameter("name", getItem())
                        .build();

                httpResponse.ifPresent(this::removeCartResponsePresent);
            }

            private void removeCartResponsePresent(CloseableHttpResponse httpResponse) {
                ResponseHandler.getBuilder(httpResponse)
                        .setOnSuccess(logger::info)
                        .setOnFailure(logger::error)
                        .setAtTheEnd(cartStageController::reload)
                        .handle();
            }
        });

        VBox vBox = new VBox(text);
        content = new HBox(vBox, button);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty) {
            text.setText(item);
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

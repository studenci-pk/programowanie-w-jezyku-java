package pl.edu.pk.biblioteka.controllers.model;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.Charge;

public class ClientChargeListCell extends ListCell<Charge> {
    private static final Logger logger = Logger.getLogger(ClientChargeListCell.class.getName());

    private HBox content;
    private Text text;

    public ClientChargeListCell() {
        super();
        text = new Text();
        VBox vBox = new VBox(text);
        content = new HBox(vBox);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(Charge item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty && !item.isZero(0.00001)) {
            text.setText(item.toString());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

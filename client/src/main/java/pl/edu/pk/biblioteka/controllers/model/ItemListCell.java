package pl.edu.pk.biblioteka.controllers.model;

import javafx.scene.control.ListCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.edu.pk.biblioteka.controllers.model.Item;
import pl.edu.pk.biblioteka.utils.ImageManager;

public class ItemListCell extends ListCell<Item> {
    private HBox content;
    private Text title;
    private Text author;

    public ItemListCell() {
        super();
        title = new Text();
        author = new Text();
        VBox vBox = new VBox(title, author);
        Image image = ImageManager.getImage(ImageManager.BOOK);
        ImageView imageView = new ImageView(image);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(64);
        imageView.setBlendMode(BlendMode.DARKEN);
        content = new HBox(imageView, vBox);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            title.setText(String.format("Tytul: %s", item.getTitle()));
            author.setText(String.format("Autor: %s", item.getAuthor()));
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
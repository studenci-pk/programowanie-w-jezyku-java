package pl.edu.pk.biblioteka.controllers.model;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.controllers.BrowsePaneController;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ImageManager;
import pl.edu.pk.biblioteka.utils.PostBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import javax.xml.ws.Response;
import java.util.Optional;

public class BookDtoListCell extends ListCell<BookDto> {
    private static final Logger logger = Logger.getLogger(BookDtoListCell.class.getName());
    private HBox content;
    private Text signature;
    private Text title;
    private Text author;
    private Button[] buttons;
    private Button withdrawn;
    BookDto book;

    public BookDtoListCell(BrowsePaneController browsePaneController, ButtonBeh... buttons) {
        super();
        signature = new Text();
        title = new Text();
        author = new Text();
        withdrawn = new Button("Przywróć");
        withdrawn.setOnAction((ignored) -> {
            Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("book/restore")
                    .addParameter("bookid", getItem().getBookId())
                    .build();

            if (httpResponse.isPresent()) {
                ResponseHandler.getBuilder(httpResponse.get())
                        .setOnSuccess(logger::info)
                        .setOnFailure(logger::error)
                        .setAtTheEnd(browsePaneController::reload)
                        .handle();
            }
        });

        VBox vBox = new VBox(signature, title, author);
        Image image = ImageManager.getImage(ImageManager.BOOK);
        ImageView imageView = new ImageView(image);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(64);
        imageView.setBlendMode(BlendMode.DARKEN);

        HBox.setHgrow(vBox, Priority.ALWAYS);
        HBox hBox = new HBox(imageView, vBox);

        hBox.getChildren().add(withdrawn);
        this.buttons = new Button[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            this.buttons[i] = new Button(buttons[i].getText());
//            this.buttons[i].setOnAction(buttons[i].getEvent(book));
            int finalI = i;
            this.buttons[i].setOnAction((event) -> {
                buttons[finalI].move(book);
            });
            hBox.getChildren().add(this.buttons[i]);
        }

        content = hBox; //new HBox(imageView, vBox);
        content.setSpacing(10);
    }

    public BookDtoListCell(ButtonBeh... buttons) {
        super();
        signature = new Text();
        title = new Text();
        author = new Text();

        VBox vBox = new VBox(signature, title, author);
        Image image = ImageManager.getImage(ImageManager.BOOK);
        ImageView imageView = new ImageView(image);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(64);
        imageView.setBlendMode(BlendMode.DARKEN);

        HBox.setHgrow(vBox, Priority.ALWAYS);
        HBox hBox = new HBox(imageView, vBox);

        this.buttons = new Button[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            this.buttons[i] = new Button(buttons[i].getText());
//            this.buttons[i].setOnAction(buttons[i].getEvent(book));
            int finalI = i;
            this.buttons[i].setOnAction((event) -> {
                buttons[finalI].move(book);
            });
            hBox.getChildren().add(this.buttons[i]);
        }

        content = hBox; //new HBox(imageView, vBox);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(BookDto book, boolean empty) {
        super.updateItem(book, empty);
        if (book != null && !empty) { // <== test for null item and empty parameter
            this.book = book;
            signature.setText(String.format("Sygnatura: %s", book.getSignature()));
            title.setText(String.format("Tytul: %s", book.getTitle()));
            author.setText(String.format("Autor: %s", book.getAuthor().toString()));

            if (withdrawn != null) {
                for (Button button : buttons) {
                    button.setVisible(!book.isWithdrawn());
                }
                withdrawn.setVisible(book.isWithdrawn());
            }

            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

package pl.edu.pk.biblioteka.controllers.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.data.Permissions;
import pl.edu.pk.biblioteka.utils.ImageManager;
import pl.edu.pk.biblioteka.utils.PostBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

public class UserDtoListCell extends ListCell<UserDto> {
    private static final Logger logger = Logger.getLogger(UserDtoListCell.class.getName());

    private HBox content;
    private Text title;
    private Text login;
    private Text name;

    private Image approvedImage;
    private Image blockedImage;
    private ImageView imageView;

    private ToggleButton button;
    private ChangeListener<Boolean> listener;

    public UserDtoListCell() {
        super();
        title = new Text();
        login = new Text();
        name = new Text();
        button = new ToggleButton();
        button.setMinWidth(100);
        VBox vBox = new VBox(title, login, name);
        {
            approvedImage = ImageManager.getImage(ImageManager.APPROVED_USER);
            blockedImage = ImageManager.getImage(ImageManager.BLOCKED_USER);
            imageView = new ImageView();
            imageView.setSmooth(true);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(64);
        }
        content = new HBox(imageView, vBox, button);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(UserDto item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            name.setText(String.format("Imie i nazwisko: %s %s", item.getName(), item.getSurname()));
            boolean notBlocked = item.getAccount().getPermissionId() != Permissions.BLOCKED_ACCOUNT;
            imageView.setImage(notBlocked ? approvedImage : blockedImage);
            button.selectedProperty().setValue(notBlocked);
            button.setText(notBlocked ? "Zablokuj" : "Aktywuj");

            if (item instanceof LibrarianDto) {
                LibrarianDto librarian = (LibrarianDto) item;
                title.setText("Pracownik");
                login.setText(String.format("Login: %s", item.getAccount().getLogin()));
                listener = (obs, o, n) -> {
                    Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("account/block")
                            .addParameter("librarianid", String.valueOf(librarian.getLibrarianId()))
                            .build();

                    updateButton(obs, o, httpResponse);
                };

            } else if (item instanceof ReaderDto) {
                ReaderDto reader = (ReaderDto) item;
                title.setText("Czytelnik");
                login.setText(String.format("Login: %s", item.getAccount().getLogin()));
                listener = (obs, o, n) -> {
                    Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("account/block")
                            .addParameter("readerid", String.valueOf(reader.getReaderId()))
                            .build();

                    updateButton(obs, o, httpResponse);
                };
            } else {
                logger.warn("nor LibrarianDto or ReaderDto");
            }


            button.selectedProperty().addListener(listener);
            setGraphic(content);

        } else {
            if (listener != null) {
                button.selectedProperty().removeListener(listener);
            }
            setGraphic(null);
        }
    }

    private void updateButton(ObservableValue<? extends Boolean> obs, Boolean o, Optional<CloseableHttpResponse> httpResponse) {
        httpResponse.ifPresent(response -> ResponseHandler.getBuilder(response)
                .setOnSuccess(entity -> {
                    logger.info("ok");
                    imageView.setImage(imageView.getImage().equals(blockedImage) ? approvedImage : blockedImage);
                    button.setText(button.getText().equals("Aktywuj") ? "Zablokuj" : "Aktywuj");
                })
                .setOnFailure(entity -> {
                    obs.removeListener(listener);
                    button.setSelected(o);
                    obs.addListener(listener);
                })
                .handle());
    }
}

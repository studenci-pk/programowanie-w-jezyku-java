package pl.edu.pk.biblioteka.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;
import pl.edu.pk.biblioteka.controllers.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountBrowsePaneController {
    private static final Logger logger = Logger.getLogger(AccountBrowsePaneController.class.getName());
    private ObservableList<UserDto> users = FXCollections.observableArrayList();

    @FXML public ListView<UserDto> listView;
    @FXML public TextField nameField;
    @FXML public TextField surnameField;
    @FXML public TextField loginField;
    @FXML public RadioButton librarianRadio;
    @FXML public RadioButton readerRadio;
    @FXML public Pane pane;

    @FXML
    void initialize() {
        listView.setCellFactory(ignored -> new UserDtoListCell());
        listView.setItems(users);
        listView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    if (listView.getSelectionModel().getSelectedItem() instanceof ReaderDto) {
                        ReaderDto reader = (ReaderDto) listView.getSelectionModel().getSelectedItem();
                        logger.info(String.format("Wybrano %s %s",reader.getName(), reader.getSurname()));

                        pane.getChildren().forEach(node -> node.setVisible(false));
                        FXMLLoader fxmlLoader = App.getLoader("activateAccountPane");
                        try {
                            fxmlLoader.load();
                            loadActivateAccountPane(reader);
                        } catch (IOException e) {
                            logger.error(e);
                        }
                    }
                }
            }
        });

        nameField.setPromptText("imie");
        surnameField.setPromptText("nazwisko");
        loginField.setPromptText("login");
    }

    public void searchBtnAction(ActionEvent actionEvent) {
        String login = loginField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        Boolean getLibrarians = librarianRadio.isSelected();
        Boolean getReaders = readerRadio.isSelected();

        if (getLibrarians || getReaders) {
            Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("browse/accounts")
                    .addParameter("login", login)
                    .addParameter("name", name)
                    .addParameter("surname", surname)
                    .addParameter("librarians", getLibrarians.toString())
                    .addParameter("readers", getReaders.toString())
                    .build();

            httpResponse.ifPresent(this::onProperSearch);

        } else {
            users.clear();
        }
    }

    private void onProperSearch(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::onSuccess)
                .setOnFailure((entity -> logger.error("Response: Failed!")))
                .handle();
    }

    private void onSuccess(HttpEntity httpEntity) throws IOException {
        String accountsString = EntityUtils.toString(httpEntity);
        logger.info(accountsString);

        Gson gson = new Gson();
        JsonObject job = gson.fromJson(accountsString, JsonObject.class);
        List<UserDto> users = new ArrayList<>();
        if (job.has("librarians")) {
            JsonElement entry = job.getAsJsonArray("librarians");
            Type type = new TypeToken<List<LibrarianDto>>(){}.getType();
            users.addAll(gson.fromJson(entry, type));
        }
        if (job.has("readers")) {
            JsonElement entry = job.getAsJsonArray("readers");
            Type type = new TypeToken<List<ReaderDto>>(){}.getType();
            users.addAll(gson.fromJson(entry, type));
        }

        listView.setCellFactory(ignored -> new UserDtoListCell());
        this.users.clear();
        this.users.addAll(users);
    }

    public void loadActivateAccountPane(ReaderDto reader) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + "librarianStage" + ".fxml"));
        if (loader != null) {
            Pane pane = loader.load();
            LibrarianStageController controller = (LibrarianStageController) loader.getController();
            if (controller != null) {
                FXMLLoader activateAccountPane = App.getLoader("activateAccountPane");
                Pane p = activateAccountPane.load();
                ((AccountDetailsPaneController) activateAccountPane.getController()).setReader(reader);
                controller.loadPane(p);
                App.setRoot(pane);
            } else {
                logger.error("Controller is null! ");
            }
        } else {
            logger.error("Loader is null! ");
        }
    }
}
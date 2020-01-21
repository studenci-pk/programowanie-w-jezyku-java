package pl.edu.pk.biblioteka.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.util.Optional;

public class LibrarianStageController {
    private static final Logger logger = Logger.getLogger(LibrarianStageController.class.getName());

    @FXML public Pane rootPane;
    @FXML public Menu logoutMenu;
    @FXML public Menu statMenu;
    @FXML public Menu settingsMenu;
    @FXML public Menu readersMenu;
    @FXML public Pane pane;

    @FXML
    void initialize() {
        initLogoutMenu();
        initStatMenu();
        initSettingsMenu();
        initReadersMenu();
    }

    public void loadAddBookPane(ActionEvent actionEvent) {
        loadPane("addBookPane");
    }

    public void loadItemBrowserPane(ActionEvent actionEvent) {
        loadPane("bookBrowsePane");
    }

    public Pane loadPane(String name) {
        pane.getChildren().clear();
        Pane pane = App.loadPane(name);
        if ( pane != null) {
            pane.prefWidthProperty().bind(this.pane.widthProperty());
            pane.prefHeightProperty().bind(this.pane.heightProperty());
        }
        this.pane.getChildren().add(pane);
        return pane;
    }

    public Pane loadPane(Pane pane) {
        pane.prefWidthProperty().bind(this.pane.widthProperty());
        pane.prefHeightProperty().bind(this.pane.heightProperty());
        this.pane.getChildren().clear();
        this.pane.getChildren().add(pane);
        return pane;
    }

    private void initLogoutMenu() {
        Label menuLabel = new Label("Wyloguj");
        menuLabel.setOnMouseClicked(event -> logout());

        logoutMenu.setGraphic(menuLabel);
        logoutMenu.setText("");
    }

    private void initStatMenu() {
        Label statLabel = new Label("Statystyki");
        statLabel.setOnMouseClicked(event -> loadPane("statPane"));

        statMenu.setGraphic(statLabel);
        statMenu.setText("");
    }

    private void initSettingsMenu() {
        Label statLabel = new Label("Ustawienia");
        statLabel.setOnMouseClicked(event -> loadPane("settingsPane"));

        settingsMenu.setGraphic(statLabel);
        settingsMenu.setText("");
    }

    private void initReadersMenu() {
        Label statLabel = new Label("Użytkownicy");
        statLabel.setOnMouseClicked(event -> loadPane("accountBrowsePane"));

        readersMenu.setGraphic(statLabel);
        readersMenu.setText("");
    }

    public void logout() {
        Optional<CloseableHttpResponse> response = GetBuilder.getBuilder("logout")
                .build();

        response.ifPresent(this::onProperLogout);
    }

    private void onProperLogout(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(entity -> logger.info("Wylogowanie poprawnie!"))
                .setOnFailure(entity -> logger.info(String.format("Entity: %s", entity)))
                .setAtTheEnd(() -> App.setRoot("loginStage"))
                .handle();
    }
}

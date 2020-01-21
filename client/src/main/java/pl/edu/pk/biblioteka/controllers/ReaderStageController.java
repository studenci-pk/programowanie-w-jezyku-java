package pl.edu.pk.biblioteka.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;
import java.util.Optional;

public class ReaderStageController {

    private static final Logger logger = Logger.getLogger(ReaderStageController.class.getName());

    @FXML Pane pane;
    @FXML Menu browserMenu;
    @FXML Menu logoutMenu;
    @FXML Menu cartMenu;

    @FXML
    void initialize() {
        initLogoutMenu();
        initBrowserMenu();
        initCartMenu();
    }

    public void loadRentalPane(ActionEvent actionEvent) {
        loadPane("rentalPane");
    }

    public void loadSettingsPane(ActionEvent actionEvent) {
        loadPane("settingsPane");
    }

    public void loadOwingPane(ActionEvent actionEvent) {
        loadPane("owingPane");
    }

    public Pane loadPane(Pane pane) {
        this.pane.getChildren().clear();
        pane.prefWidthProperty().bind(this.pane.widthProperty());
        pane.prefHeightProperty().bind(this.pane.heightProperty());
        this.pane.getChildren().add(pane);
        return pane;
    }

    private void loadPane(String name) {
        pane.getChildren().clear();
        Pane pane = App.loadPane(name);
        if (pane != null) {
            pane.prefWidthProperty().bind(this.pane.widthProperty());
            pane.prefHeightProperty().bind(this.pane.heightProperty());
        }
        this.pane.getChildren().add(pane);
    }

    private void initLogoutMenu() {
        Label menuLabel = new Label("Wyloguj");
        menuLabel.setOnMouseClicked(event -> logout());

        logoutMenu.setGraphic(menuLabel);
        logoutMenu.setText("");
    }

    private void initCartMenu() {
        Label menuLabel = new Label("Koszyk");
        menuLabel.setOnMouseClicked(event -> loadPane("cartStage"));

        cartMenu.setGraphic(menuLabel);
        cartMenu.setText("");
    }

    private void initBrowserMenu() {
        Label statLabel = new Label("Wyszukiwarka");
        statLabel.setOnMouseClicked(event -> loadPane("bookBrowsePaneClient"));

        browserMenu.setGraphic(statLabel);
        browserMenu.setText("");
    }

    public void logout() {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("logout")
                .build();

        httpResponse.ifPresent(this::onProperLogout);
    }

    private void onProperLogout(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(entity -> logger.info("Wylogowanie poprawnie!"))
                .setOnFailure(entity -> logger.info(String.format("Entity: %s", entity)))
                .setAtTheEnd(() -> App.setRoot("loginStage"))
                .handle();
    }
}

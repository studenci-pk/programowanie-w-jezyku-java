package pl.edu.pk.biblioteka.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.utils.Connector;

/**
 * Kontroler ustwień serwera
 */
public class SettingsStageController {

    @FXML TextField addressField;
    @FXML TextField portField;

    @FXML
    void initialize() {
        addressField.setText(Connector.getHost());
        portField.setText(Connector.getPort().toString());
    }

    public void moveToLoginStage(ActionEvent actionEvent) {
        App.setRoot("loginStage");
    }

    public void applied(ActionEvent actionEvent) {
        Connector.setHost(addressField.getText());
        Connector.setPort(Integer.valueOf(portField.getText()));
    }
}

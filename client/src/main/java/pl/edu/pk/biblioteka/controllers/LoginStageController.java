package pl.edu.pk.biblioteka.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import pl.edu.pk.biblioteka.App;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Optional;
//Agata - dodane
import pl.edu.pk.biblioteka.data.Permissions;
import pl.edu.pk.biblioteka.utils.AccountType;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.PostBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

public class LoginStageController {
    private static Logger logger = Logger.getLogger(LoginStageController.class);

    @FXML public Button settingsBtn;
    @FXML public Button loginBtn;
    @FXML public TextField loginField;
    @FXML public PasswordField passField;
    @FXML public Label statusLabel;

    @FXML
    public void initialize() {
        loginField.setPromptText("Login");
        passField.setPromptText("Hasło");
        loginBtn.setDisable(true);

        ChangeListener<String> textChanged = (observable, oldValue, newValue) -> {
            loginBtn.setDisable(loginField.getText().isEmpty() || passField.getText().isEmpty());
        };

        loginField.textProperty().addListener(textChanged);
        passField.textProperty().addListener(textChanged);

        Image gearIcon = new Image(getClass().getResourceAsStream("/images/gear.png"));
        ImageView gearIconView = new ImageView(gearIcon);
        gearIconView.setFitWidth(32);
        gearIconView.setFitHeight(32);
        gearIconView.setSmooth(true);
        settingsBtn.setGraphic(gearIconView);
    }

    @FXML
    public void onAction(ActionEvent actionEvent) {
        sendPost();
    }

    public void moveToRegisterStage(ActionEvent actionEvent) {
        App.setRoot("registrationStage");
    }

    public void moveToBrowseStage(ActionEvent actionEvent) {
        App.setRoot("browseStage");
    }

    public void moveToSettingsStage(ActionEvent actionEvent) {
        App.setRoot("settingsStage");
    }

    private void sendPost() {
        String login = loginField.getText();
        String pass = passField.getText();

        Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("login")
                .addParameter("username", login)
                .addParameter("userpass", pass)
                .build();

        httpResponse.ifPresent(this::onProperLogin);
    }

    private void onProperLogin(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::onSuccess)
                .setOnFailure(this::onFailure)
                .handle();
    }

    private void onSuccess(HttpEntity httpEntity) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(responseString);
        if (!responseJson.isNull("accountType")) {
            logger.info("accountType: " + responseJson.getString("accountType"));
            AccountType accountType = AccountType.valueOf(responseJson.getString("accountType").toUpperCase());
            switch (accountType) {
                case LIBRARIAN:
                    App.setRoot("librarianStage");
                    break;
                case READER:
                    App.setRoot("readerStage");
                    break;
            }
        } else {
            statusLabel.setText("Konto nieaktywne/zablokowane");
        }

    }

    private void onFailure(HttpEntity httpEntity) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(responseString);
        String message = responseJson.optString("message", "Nieznany błąd");
        logger.error(message);
        passField.clear();
        loginField.clear();
        statusLabel.setText(message);
    }
}

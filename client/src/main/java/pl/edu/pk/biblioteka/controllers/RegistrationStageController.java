package pl.edu.pk.biblioteka.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.App;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.utils.PostBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.io.IOException;
import java.util.Optional;

public class RegistrationStageController {
    private static Logger logger = Logger.getLogger(RegistrationStageController.class);

    @FXML public TextField loginField;
    @FXML public PasswordField passField;
    @FXML public PasswordField repeatPassField;
    @FXML public TextField emailField;
    @FXML public TextField peselField;
    @FXML public TextField nameField;
    @FXML public TextField surnameField;
    @FXML public Label statusLabel;
    @FXML public Pane centerPane;
    @FXML public AnchorPane mainPane;
    @FXML public TextField additionalField1;
    @FXML public TextField additionalField2;
    @FXML public TextField additionalField3;
    @FXML public ToggleGroup accountType;
    @FXML public RadioButton librarianRadio;
    @FXML public RadioButton readerRadio;

    @FXML
    public void initialize() {
        loginField.setPromptText("Login");
        passField.setPromptText("Hasło");
        repeatPassField.setPromptText("Powtórz hasło");
        emailField.setPromptText("e-mail");
        peselField.setPromptText("numer PESEL");
        nameField.setPromptText("Imię");
        surnameField.setPromptText("Nazwisko");

        additionalField1.setVisible(false);
        additionalField2.setVisible(false);
        additionalField3.setVisible(false);

        centerPane.layoutXProperty().set(0);
        centerPane.layoutYProperty().set(0);
        mainPane.widthProperty().addListener((obj, o, n) ->
                centerPane.layoutXProperty().set((n.doubleValue()-centerPane.getPrefWidth())/2));
        mainPane.heightProperty().addListener((obj, o, n) ->
                centerPane.layoutYProperty().set((n.doubleValue()-centerPane.getPrefHeight())/2));
    }

    @FXML
    public void onAction(ActionEvent actionEvent) {
        if (validateDetails()) {
            String login = loginField.getText();
            String email = emailField.getText();
            String pesel = peselField.getText();
            String password = passField.getText();
            String encryptedPassword = DigestUtils.md5Hex(password);
            String accType = (accountType.getSelectedToggle().equals(librarianRadio) ? "librarian" : "reader");
            String name = nameField.getText();
            String surname = surnameField.getText();

            PostBuilder postBuilder = PostBuilder.getBuilder("register")
                    .addParameter("login", login)
                    .addParameter("password", encryptedPassword)
                    .addParameter("email", email)
                    .addParameter("pesel", pesel)
                    .addParameter("accType", accType)
                    .addParameter("name", name)
                    .addParameter("surname", surname);

            if (accountType.getSelectedToggle().equals(librarianRadio)) {
                // ...
            } else if (accountType.getSelectedToggle().equals(readerRadio)) {
                postBuilder.addParameter("faculty", additionalField1.getText())
                           .addParameter("subject", additionalField2.getText());
            }

            Optional<CloseableHttpResponse> httpResponse = postBuilder.build();

            httpResponse.ifPresent(this::onProperSend);
        }
    }

    private void onProperSend(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess((entity) -> statusLabel.setText("Utworzono konto"))
                .setOnFailure(this::onFailure)
                .handle();
    }

    private void onFailure(HttpEntity httpEntity) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(responseString);
        String message = responseJson.optString("message", "Nieznany błąd");
        statusLabel.setText(message);
    }

    public void selectStudent(ActionEvent actionEvent) {
        additionalField1.setVisible(true);
        additionalField2.setVisible(true);
        additionalField3.setVisible(false);
        additionalField1.setPromptText("Wydział");
        additionalField2.setPromptText("Kierunek");
    }

    public void selectLibrarian(ActionEvent actionEvent) {
        additionalField1.setVisible(false);
        additionalField2.setVisible(false);
        additionalField3.setVisible(false);
    }

    @FXML
    public void moveToBrowseStage(ActionEvent actionEvent) {
        App.setRoot("browseStage");
    }

    @FXML
    public void moveToLoginStage(ActionEvent actionEvent) {
        App.setRoot("loginStage");
    }

    private boolean validateDetails() {
        boolean result = true;

        String pass1 = passField.getText();
        if (!validatePassword(pass1)) {
            passField.getStyleClass().add("error");
            result = false;
        } else {
            passField.getStyleClass().removeIf(s -> s.equals("error"));
        }

        String pass2 = repeatPassField.getText();
        if (!pass1.equals(pass2)) {
            repeatPassField.getStyleClass().add("error");
            result = false;
        } else {
            repeatPassField.getStyleClass().removeIf(s -> s.equals("error"));
        }

        String email = emailField.getText();
        if (!validateEmail(email)) {
            emailField.getStyleClass().add("error");
            result = false;
        } else {
            emailField.getStyleClass().removeIf(s -> s.equals("error"));
        }

        String phone = peselField.getText();
        if (!validatePeselNumber(phone)) {
            peselField.getStyleClass().add("error");
            result = false;
        } else {
            peselField.getStyleClass().removeIf(s -> s.equals("error"));
        }

        String login = loginField.getText();
        if (!validateLogin(login)) {
            loginField.getStyleClass().add("error");
            result = false;
        } else {
            loginField.getStyleClass().removeIf(s -> s.equals("error"));
        }

        return result;
    }

    private void registerPresentResponse(CloseableHttpResponse closeableHttpResponse) {
        ResponseHandler.getBuilder(closeableHttpResponse)
                .setOnSuccess(entity -> {
                    String message = "Utworzono konto";
                    logger.info(message);
                    statusLabel.setText(message);
                })
                .setOnFailure(entity -> {
                    String responseString = EntityUtils.toString(entity);
                    JSONObject responseJson = new JSONObject(responseString);
                    String message = responseJson.optString("message", "Nieznany błąd");
                    logger.error(message);
                    statusLabel.setText(message);
                })
                .handle();
    }

    private boolean validateLogin(String login) {
        return login.matches("^[a-zA-Z\\d]{3,}$");
    }

    private boolean validatePassword(String password) {
        return password.length() > 7;
    }

    private boolean validatePeselNumber(String pesel) {
        return pesel.matches("[0-9]{11}");
    }

    private boolean validateEmail(String email) {
        return email.matches(".+@.+\\..+");
    }
}

package pl.edu.pk.biblioteka.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.controllers.model.LibrarianDto;
import pl.edu.pk.biblioteka.controllers.model.ReaderDto;
import pl.edu.pk.biblioteka.utils.PostBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

public class SettingsPaneController {

    @FXML Label passwordLabel;
    @FXML Label dataLabel;
    @FXML PasswordField oldPasswordField;
    @FXML PasswordField newPasswordField;
    @FXML PasswordField repeatPasswordField;
    @FXML TextField nameField;
    @FXML TextField surnameField;
    @FXML TextField emailField;
    @FXML TextField peselField;
    @FXML TextField additionalField1;
    @FXML TextField additionalField2;

    private static final Logger logger = Logger.getLogger(SettingsPaneController.class.getName());

    @FXML
    void initialize() {
        passwordLabel.setText("");
        dataLabel.setText("");
        additionalField1.setVisible(false);
        additionalField2.setVisible(false);

        Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("account")
                .build();

        httpResponse.ifPresent(this::onProperAccountResponse);
    }

    private void onProperAccountResponse(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::loadData)
                .handle();
    }

    private void loadData(HttpEntity httpEntity) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);

        Gson gson = new GsonBuilder().create();
        JsonObject job = gson.fromJson(responseString, JsonObject.class);
        if (job.has("librarian")) {
            JsonElement entry = job.getAsJsonObject("librarian");
            Type type = new TypeToken<LibrarianDto>(){}.getType();
            LibrarianDto l = gson.fromJson(entry, type);
            updateFields(l.getName(), l.getSurname(), l.getPesel(), l.getAccount().getEmail());

        } else if (job.has("reader")) {
            JsonElement entry = job.getAsJsonObject("reader");
            Type type = new TypeToken<ReaderDto>(){}.getType();
            ReaderDto r = gson.fromJson(entry, type);
            updateFields(r.getName(), r.getSurname(), r.getPesel(), r.getAccount().getEmail());
        }
    }

    private void updateFields(String name, String surname, String pesel, String email) {
        emailField.setText(email);
        nameField.setText(name);
        surnameField.setText(surname);
        peselField.setText(pesel);
    }

    public void changePasswordAction(ActionEvent actionEvent) {
        if (!newPasswordField.getText().equals(repeatPasswordField.getText())) {
            passwordLabel.setText("Podane hasła różnią się");
            return;
        }

        String encryptedOldPassword = DigestUtils.md5Hex(oldPasswordField.getText());
        String encryptedNewPassword = DigestUtils.md5Hex(newPasswordField.getText());

        Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("change")
                .addParameter("oldpassword", encryptedOldPassword)
                .addParameter("newpassword", encryptedNewPassword)
                .build();

        httpResponse.ifPresent(this::onProperPassSend);
    }

    private void onProperPassSend(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(entity -> passwordLabel.setText("Zmienino hasło"))
                .setOnFailure(this::onFailure)
                .handle();
    }

    private void onFailure(HttpEntity httpEntity) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(responseString);
        String message = responseJson.optString("message", "Nieznany błąd");
        passwordLabel.setText(message);
    }

    public void updateBtn(ActionEvent actionEvent) {
        Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("change")
            .addParameter("name", nameField.getText())
            .addParameter("surname", surnameField.getText())
            .addParameter("email", emailField.getText())
            .build();

        httpResponse.ifPresent(this::onProperChangeSend);
    }

    private void onProperChangeSend(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(entity -> dataLabel.setText("Zmienino dane"))
                .setOnFailure(entity -> dataLabel.setText("Nie udało się zmienić danych"))
                .handle();
    }
}

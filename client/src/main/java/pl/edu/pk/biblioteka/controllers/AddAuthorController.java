package pl.edu.pk.biblioteka.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.PaneUtils;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

public class AddAuthorController {

    private static final Logger logger = Logger.getLogger(AddAuthorController.class.getName());

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button OkBtn;
    @FXML private Label Label;
    @FXML private Button BackBtn;
    @FXML private TextField Add_author_imie;
    @FXML private TextField Add_author_nazwisko;
    @FXML private TextField Add_author_kraj_pochodzenia;

    @FXML
    void initialize() {
    }

    @FXML
    void OkBtn_action(ActionEvent event) {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("add-author")
                .addParameter("name", Add_author_imie.getText())
                .addParameter("surname", Add_author_nazwisko.getText())
                .addParameter("country", Add_author_kraj_pochodzenia.getText())
                .build();

        httpResponse.ifPresent(this::onSucceedSend);
    }

    private void onSucceedSend(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess((entity -> logger.info("Succeed")))
                .handle();
    }

    @FXML
    void moveToAddResourceBookStage(ActionEvent event) throws IOException {
        PaneUtils.backToAddBookPane();
    }
}

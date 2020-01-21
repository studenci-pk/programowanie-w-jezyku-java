//package pl.edu.pk.biblioteka.controllers;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import pl.edu.pk.biblioteka.App;
//
//public class AddPublisherController {
//
//    @FXML
//    private ResourceBundle resources;
//
//    @FXML
//    private URL location;
//
//    @FXML
//    private Button OkBtn;
//
//    @FXML
//    private Label Label;
//
//    @FXML
//    private Button BackBtn;
//
//    @FXML
//    private TextField Add_category_imie;
//
//    @FXML
//    void OkBtn_action(ActionEvent event) {
//
//    }
//
//    @FXML
//    void moveToAddResourceBookStage(ActionEvent event) {
//        App.setRoot("addResourceBook");
//    }
//
//    @FXML
//    void initialize() {
//        assert OkBtn != null : "fx:id=\"OkBtn\" was not injected: check your FXML file 'addPublisher.fxml'.";
//        assert Label != null : "fx:id=\"Label\" was not injected: check your FXML file 'addPublisher.fxml'.";
//        assert BackBtn != null : "fx:id=\"BackBtn\" was not injected: check your FXML file 'addPublisher.fxml'.";
//        assert Add_category_imie != null : "fx:id=\"Add_category_imie\" was not injected: check your FXML file 'addPublisher.fxml'.";
//
//    }
//}

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
import javafx.scene.layout.AnchorPane;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.PaneUtils;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

public class AddPublisherController {

    private static final Logger logger = Logger.getLogger(AddPublisherController.class.getName());

    @FXML public AnchorPane aPane;
    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button OkBtn;
    @FXML private Label Label;
    @FXML private Button BackBtn;
    @FXML private TextField Add_category_imie;
    @FXML private TextField Add_category_miejscowosc;
    @FXML private TextField Add_category_kraj;

    @FXML
    void initialize() {
    }

    @FXML
    void OkBtn_action(ActionEvent event) {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("add-publisher")
                .addParameter("name", Add_category_imie.getText())
                .addParameter("address", Add_category_miejscowosc.getText())
                .addParameter("country", Add_category_kraj.getText())
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


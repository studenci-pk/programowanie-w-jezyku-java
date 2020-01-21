package pl.edu.pk.biblioteka.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.controllers.model.CartListCell;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.PostBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

public class CartStageController {
    private static final Logger logger = Logger.getLogger(CartStageController.class.getName());
    ObservableList<String> list = FXCollections.observableArrayList();

    @FXML private ListView<String> cartView;
    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Label Label;
    @FXML private Button BackBtn;
    @FXML private ImageView image_cart;

    @FXML
    void initialize() {
        cartView.setCellFactory((ignore) -> new CartListCell(this));
        cartView.setItems(list);

        reload();
    }

    public void reload() {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/cart").build();

        ResponseHandler.getBuilder(httpResponse.get())
                .setOnSuccess(this::OnSuccess)
                .setOnFailure(logger::error)
                .handle();
    }

    private void OnSuccess(HttpEntity httpEntity)  throws IOException {
        String response = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(response);
        JSONArray titles = responseJson.getJSONArray("titles");
        list.clear();
        for (int i = 0; i < titles.length(); i++) {
            String title = titles.getString(i);
            list.add(title);
        }
    }
}

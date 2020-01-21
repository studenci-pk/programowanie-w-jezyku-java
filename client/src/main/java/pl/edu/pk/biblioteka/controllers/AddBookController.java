package pl.edu.pk.biblioteka.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.utils.*;
import pl.edu.pk.biblioteka.data.Author;
import pl.edu.pk.biblioteka.data.Department;
import pl.edu.pk.biblioteka.data.Publisher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddBookController {

    private static final Logger logger = Logger.getLogger(AddBookController.class.getName());

    private ObservableList<Author> authors = FXCollections.observableArrayList();
    private ObservableList<Publisher> publishers = FXCollections.observableArrayList();
    private ObservableList<Department> departments = FXCollections.observableArrayList();

    @FXML public Pane rootPane;
    @FXML private ComboBox<Author> authorComboBox;
    @FXML private ComboBox<Publisher> publisherComboBox;
    @FXML private ComboBox<Department> departmentComboBox;
    @FXML private TextField keywordsField;
    @FXML private TextField titleField;
    @FXML private Hyperlink addAuthorLink;
    @FXML private Hyperlink addPublisherLink;
    @FXML private Hyperlink addDepartmentLink;
    @FXML private TextField categoryField;
    @FXML private Button addBtn;

    @FXML
    void initialize() {
        authorComboBox.setItems(authors);
        publisherComboBox.setItems(publishers);
        departmentComboBox.setItems(departments);


        titleField.textProperty().addListener((observable -> addBtn.setDisable(titleField.getText().isEmpty())));
        addBtn.setDisable(true);

        Platform.runLater(this::getAllData);
    }

    @FXML void addAction(ActionEvent event) {
        String title = titleField.getText();
        String keywords = keywordsField.getText();
        String category =  categoryField.getText();

        SingleSelectionModel model1 = authorComboBox.getSelectionModel();
        Author author = (Author) model1.getSelectedItem();

        SingleSelectionModel model2 = publisherComboBox.getSelectionModel();
        Publisher publisher = (Publisher) model2.getSelectedItem();

        SingleSelectionModel model3 = departmentComboBox.getSelectionModel();
        Department department = (Department) model3.getSelectedItem();

        logger.info(title);
        logger.info(keywords);
        logger.info(category);

        logger.info(author);
        logger.info(publisher);
        logger.info(department);

        Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("add/book")
                .addParameter("title", title)
                .addParameter("keywords", keywords)
                .addParameter("category", category)
                .addParameter("authorId", String.valueOf(author.getAuthorId()))
                .addParameter("publisherId", String.valueOf(publisher.getPublisherId()))
                .addParameter("departmentId", String.valueOf(department.getDepartmentId()))
                .build();

        httpResponse.ifPresent(this::onBookSucceed);
    }

    private void onBookSucceed(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess((entity -> logger.info("Succeed")))
                .setOnFailure((entity -> logger.info("Failure")))
                .handle();
    }

    @FXML
    void moveToAddAuthorStage(ActionEvent event) throws IOException {
        PaneUtils.loadLibrarianPane("addAuthor");
    }

    @FXML
    void moveToAddDepartmentStage(ActionEvent event) throws IOException {
        PaneUtils.loadLibrarianPane("addDepartment");
    }

    @FXML
    void moveToAddPublisherStage(ActionEvent event) throws IOException {
        PaneUtils.loadLibrarianPane("addPublisher");
    }

    private void getAllData() {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/info")
                .build();

        httpResponse.ifPresent(this::onSucceedGet);
    }

    private void onSucceedGet(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::onSucceedResponse)
                .setOnFailure(this::onFailedResponse)
                .handle();
    }

    private void onSucceedResponse(HttpEntity httpEntity) throws IOException {
        DataUtils.loadData(httpEntity,
                this.departments, departmentComboBox,
                this.authors, authorComboBox,
                this.publishers, publisherComboBox);
    }

    private void onFailedResponse(HttpEntity httpEntity) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(responseString);
        String message = responseJson.optString("message", "Nieznany błąd");
        logger.error(message);
    }
}

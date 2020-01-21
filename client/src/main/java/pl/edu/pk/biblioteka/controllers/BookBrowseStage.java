package pl.edu.pk.biblioteka.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.controllers.model.ButtonBeh;
import pl.edu.pk.biblioteka.data.Author;
import pl.edu.pk.biblioteka.data.Department;
import pl.edu.pk.biblioteka.data.Publisher;
import pl.edu.pk.biblioteka.utils.DataUtils;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.controllers.model.BookDtoListCell;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;

public class BookBrowseStage {
    private static final Logger logger = Logger.getLogger(BookBrowseStage.class.getName());
    private ObservableList<Author> authors = FXCollections.observableArrayList();
    private ObservableList<Publisher> publishers = FXCollections.observableArrayList();
    private ObservableList<Department> departments = FXCollections.observableArrayList();
    private ObservableList<BookDto> books = FXCollections.observableArrayList();

    @FXML public Button searchBtn;
    @FXML public Hyperlink advancedLink;
    @FXML public Pane advancedPane;
    @FXML public TextField titleField;
    @FXML public ComboBox<Author> authorBox;
    @FXML public ComboBox<Publisher> publisherBox;
    @FXML public ComboBox<Department> departmentBox;
    @FXML public TextField categoryField;
    @FXML public TextField keywordsField;
    @FXML public ListView<BookDto> listView;

    @FXML
    void initialize() {
        advancedPane.setVisible(false);

        authorBox.setItems(authors);
        publisherBox.setItems(publishers);
        departmentBox.setItems(departments);

        listView.setItems(books);
        listView.setCellFactory((ignored) -> new BookDtoListCell());


        Platform.runLater(this::getAllData);
    }

    public void advancedClick(ActionEvent actionEvent) {
        advancedPane.setVisible(!advancedPane.isVisible());
    }

    public void searchBtnAction(ActionEvent actionEvent) {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("browse/books")
                .addParameter("title", titleField.getText())
                .build();

        httpResponse.ifPresent(this::onProperResponse);
    }

    private void onProperResponse(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::gotBooks)
                .setOnFailure((entity -> logger.info("Failure")))
                .handle();
    }

    private void gotBooks(HttpEntity httpEntity) throws IOException {
        DataUtils.loadBooks(httpEntity, this.books);
    }

    public void advancedSearchAction(ActionEvent actionEvent) {
        Optional<CloseableHttpResponse> httpResponse = DataUtils.getBooksResponse(
                departmentBox, publisherBox, authorBox, titleField, categoryField, keywordsField);

        httpResponse.ifPresent(this::onProperResponse);
    }

    private void getAllData() {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/info")
                .build();

        httpResponse.ifPresent(this::onSucceedGet);
    }

    private void onSucceedGet(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::onSucceedResponse)
                .handle();
    }

    private void onSucceedResponse(HttpEntity httpEntity) throws IOException {
        DataUtils.loadData(httpEntity, this.departments, this.authors, this.publishers);
    }

    public void moveToLoginStage(ActionEvent actionEvent) {
        App.setRoot("loginStage");
    }
}

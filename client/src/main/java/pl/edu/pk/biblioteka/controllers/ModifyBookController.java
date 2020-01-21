package pl.edu.pk.biblioteka.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.utils.*;
import pl.edu.pk.biblioteka.data.Author;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.data.Department;
import pl.edu.pk.biblioteka.data.Publisher;

import java.io.IOException;
import java.util.Optional;

public class ModifyBookController {

    private static final Logger logger = Logger.getLogger(ModifyBookController.class.getName());

    private ObservableList<Author> authors = FXCollections.observableArrayList();
    private ObservableList<Publisher> publishers = FXCollections.observableArrayList();
    private ObservableList<Department> departments = FXCollections.observableArrayList();

    private int bookId;
    public ComboBox<Department> departmentComboBox;
    public ComboBox<Publisher> publisherComboBox;
    public ComboBox<Author> authorComboBox;
    public TextField categoryField;
    public TextField keywordsField;
    public TextField titleFiled;

    @FXML
    public void initialize() {
        authorComboBox.setItems(authors);
        publisherComboBox.setItems(publishers);
        departmentComboBox.setItems(departments);

        Platform.runLater(this::getAllData);
    }

    public void moveToAddAuthorStage(ActionEvent actionEvent) throws IOException {
        PaneUtils.loadLibrarianPane("addAuthor");
    }

    public void moveToAddDepartmentStage(ActionEvent actionEvent) throws IOException {
        PaneUtils.loadLibrarianPane("addDepartment");
    }

    public void moveToAddPublisherStage(ActionEvent actionEvent) throws IOException {
        PaneUtils.loadLibrarianPane("addPublisher");
    }

    public void applyAction(ActionEvent actionEvent) {
        String title = titleFiled.getText();
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

        Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("edit/book")
                .addParameter("bookId", String.valueOf(bookId))
                .addParameter("title", title)
                .addParameter("keywords", keywords)
                .addParameter("category", category)
                .addParameter("authorId", String.valueOf(author.getAuthorId()))
                .addParameter("publisherId", String.valueOf(publisher.getPublisherId()))
                .addParameter("departmentId", String.valueOf(department.getDepartmentId()))
                .build();

        httpResponse.ifPresent(this::editResponsePresent);
    }

    private void editResponsePresent(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess((entity -> logger.info("ok")))
                .setOnFailure((entity -> logger.info("x")))
                .handle();
    }

    public void setBook(BookDto book) {
        Platform.runLater(() -> {
            bookId = book.getBookId();
            categoryField.setText(book.getCategory());
            keywordsField.setText(book.getKeywords());
            titleFiled.setText(book.getTitle());

            authorComboBox.getSelectionModel().select(book.getAuthor());
            publisherComboBox.getSelectionModel().select(book.getPublisher());
            departmentComboBox.getSelectionModel().select(book.getDepartment());
        });
    }

    private void getAllData() {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/info")
                .build();

        httpResponse.ifPresent(this::onSucceedGet);
    }

    private void onSucceedGet(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::onSucceedResponse)
                .setOnFailure(logger::error)
                .handle();
    }

    private void onSucceedResponse(HttpEntity httpEntity) throws IOException {
        DataUtils.loadData(httpEntity,
                this.departments, departmentComboBox,
                this.authors, authorComboBox,
                this.publishers, publisherComboBox);
    }



    public void removeBook(ActionEvent actionEvent) {
        Optional<CloseableHttpResponse> httpResponse = PostBuilder.getBuilder("book/remove")
                .addParameter("bookId", String.valueOf(this.bookId))
                .build();

        httpResponse.ifPresent(this::removeBookPresentResponse);
    }

    private void removeBookPresentResponse(CloseableHttpResponse closeableHttpResponse) {
        ResponseHandler.getBuilder(closeableHttpResponse)
                .setOnSuccess(logger::info)
                .setOnFailure(logger::error)
                .setAtTheEnd(() -> {
                    try {
                        PaneUtils.loadLibrarianPane("bookBrowsePane");
                    } catch (IOException e) {
                        logger.error(e);
                    }
                })
                .handle();
    }
}

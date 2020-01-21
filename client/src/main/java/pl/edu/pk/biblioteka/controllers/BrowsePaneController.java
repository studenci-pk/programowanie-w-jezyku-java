package pl.edu.pk.biblioteka.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.controllers.model.BookDtoListCell;
import pl.edu.pk.biblioteka.utils.DataUtils;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;
import pl.edu.pk.biblioteka.controllers.model.ButtonBeh;
import pl.edu.pk.biblioteka.data.*;

import java.io.IOException;
import java.util.Optional;

public class BrowsePaneController {
    private static final Logger logger = Logger.getLogger(BrowsePaneController.class.getName());
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
        listView.setCellFactory((ignored) -> new BookDtoListCell(
                this,
                new ButtonBeh("Edytuj", (book) -> {
                    logger.info("="+book.getAuthor());
                    try {
                        loadModifyBookPane(book, "librarianStage");
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }),
                new ButtonBeh("Zasoby", (book) -> {
                    logger.info(">"+book.getAuthor());
                    try {
                        loadEditResourcesPane(book, "librarianStage");
                    } catch (IOException e) {
                        logger.error(e);
                    }
                })
        ));

        Platform.runLater(this::getAllData);
    }

    public void loadModifyBookPane(BookDto book, String mainStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + mainStage + ".fxml"));
        if (loader != null) {
            Pane pane = loader.load();
            LibrarianStageController controller = (LibrarianStageController) loader.getController();
            if (controller != null) {
                FXMLLoader modifyBookPane = App.getLoader("modifyBookPane");
                Pane p = modifyBookPane.load();
                ((ModifyBookController) modifyBookPane.getController()).setBook(book);
                controller.loadPane(p);
                App.setRoot(pane);
            } else {
                logger.error("Controller is null! ");
            }
        } else {
            logger.error("Loader is null! ");
        }
    }

    public void loadEditResourcesPane(BookDto book, String mainStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + mainStage + ".fxml"));
        if (loader != null) {
            Pane pane = loader.load();
            LibrarianStageController controller = (LibrarianStageController) loader.getController();
            if (controller != null) {
                FXMLLoader modifyBookPane = App.getLoader("editResourcesPane");
                Pane p = modifyBookPane.load();
                ((EditResourcesPaneController) modifyBookPane.getController()).setBook(book);
                controller.loadPane(p);
                App.setRoot(pane);
            } else {
                logger.error("Controller is null! ");
            }
        } else {
            logger.error("Loader is null! ");
        }
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
        TextField departmentBoxEditor = departmentBox.getEditor();
        TextField publisherBoxEditor = publisherBox.getEditor();
        TextField authorBoxEditor = authorBox.getEditor();
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("browse/books")
                .addParameter("title", titleField.getText())
                .addParameter("category", categoryField.getText())
                .addParameter("keywords", keywordsField.getText())
                .addParameter("department", departmentBoxEditor.getText())
                .addParameter("publisher", publisherBoxEditor.getText())
                .addParameter("author", authorBoxEditor.getText())
                .build();

        logger.info(authorBox.getAccessibleText());
        logger.info(authorBox.getValue());

        httpResponse.ifPresent(this::onProperResponse);
    }

    public void reload() {
        advancedSearchAction(null);
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
}

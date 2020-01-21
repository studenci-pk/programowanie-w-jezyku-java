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

public class BrowsePaneClientController {
    private static final Logger logger = Logger.getLogger(BrowsePaneClientController.class.getName());
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
                new ButtonBeh("Wybierz", (book -> {
                    try {
                        loadBrowseResourcesPane(book, "readerStage");
                    } catch (IOException e) {
                        logger.error(e);
                    }
                })),
                new ButtonBeh("Wpisz tytuł do koszyka", (book -> {
                    Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("add-to-cart")
                            .addParameter("tytul", book.getTitle())
                            .build();

                    if (httpResponse.isPresent()) {
                        ResponseHandler.getBuilder(httpResponse.get()) //obsługa odpowiedzi
                                .setOnSuccess(logger::info)
                                .setOnFailure(logger::error)
                                .handle();
                    }
                }))));

        Platform.runLater(this::getAllData);
    }

    private void loadBrowseResourcesPane(BookDto book, String mainStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + mainStage + ".fxml"));
        if (loader != null) {
            Pane pane = loader.load();
            ReaderStageController controller = (ReaderStageController) loader.getController();
            if (controller != null) {
                FXMLLoader browseResourcesPane = App.getLoader("browseResourcesPane");
                Pane p = browseResourcesPane.load();
                ((BrowseResourcesPaneController) browseResourcesPane.getController()).setBook(book);
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
                .setOnFailure(logger::error)
                .handle();
    }

    private void onSucceedResponse(HttpEntity httpEntity) throws IOException {
        DataUtils.loadData(httpEntity, this.departments, this.authors, this.publishers);
    }
}

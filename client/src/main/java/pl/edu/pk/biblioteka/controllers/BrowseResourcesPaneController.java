package pl.edu.pk.biblioteka.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.controllers.model.AudiobookListCellClient;
import pl.edu.pk.biblioteka.controllers.model.CopyListCellClient;
import pl.edu.pk.biblioteka.controllers.model.EbookListCellClient;
import pl.edu.pk.biblioteka.data.Audiobook;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class BrowseResourcesPaneController {
    private static final Logger logger = Logger.getLogger(BrowseResourcesPaneController.class.getName());
    private ObservableList<Copy> copies = FXCollections.observableArrayList();
    private ObservableList<Ebook> ebooks = FXCollections.observableArrayList();
    private ObservableList<Audiobook> audiobooks = FXCollections.observableArrayList();
    private BookDto book;

    @FXML public ListView<Copy> copyView;
    @FXML public ListView<Ebook> ebookView;
    @FXML public ListView<Audiobook> audiobookView;
    @FXML public Tab copyTab;
    @FXML public Tab ebookTab;
    @FXML public Tab audiobookTab;

    @FXML
    void initialize() {
        copyView.setItems(copies);
        ebookView.setItems(ebooks);
        audiobookView.setItems(audiobooks);

        copyView.setCellFactory((ignored) -> new CopyListCellClient(this));
        ebookView.setCellFactory((ignored) -> new EbookListCellClient(this));
        audiobookView.setCellFactory((ignored) -> new AudiobookListCellClient(this));
    }


    public void setBook(BookDto book) {
        logger.debug("setBook");
        this.book = book;
        reload();
    }

    public BookDto getBook() {
        return this.book;
    }

    private void loadResources(BookDto book) {
        logger.debug("loadResources");
        logger.info("bookId: " + book.getBookId());
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("resources/all")
                .addParameter("bookId", String.valueOf(book.getBookId()))
                .build();

        httpResponse.ifPresent(this::loadResourcesResponsePresent);
    }

    private void loadResourcesResponsePresent(CloseableHttpResponse httpResponse) {
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::loadResourcesSucceed)
                .setOnFailure((ignored) -> logger.error("failed"))
                .handle();
    }

    private void loadResourcesSucceed(HttpEntity entity) throws IOException {
        String resourcesString = EntityUtils.toString(entity);
        logger.debug("loadResourcesSucceed\n"+resourcesString);

        Gson gson = new Gson();
        JsonObject job = gson.fromJson(resourcesString, JsonObject.class);
        if (job.has("copies")) {
            JsonElement entry = job.getAsJsonArray("copies");
            Type type = new TypeToken<List<Copy>>(){}.getType();
            List<Copy> copies = gson.fromJson(entry, type);
            this.copies.clear();
            this.copies.addAll(copies);
            this.copyTab.setDisable(false);
        } else {
            this.copyTab.setDisable(true);
        }

        if (job.has("ebooks")) {
            JsonElement entry = job.getAsJsonArray("ebooks");
            Type type = new TypeToken<List<Ebook>>(){}.getType();
            List<Ebook> ebooks = gson.fromJson(entry, type);
            this.ebooks.clear();
            this.ebooks.addAll(ebooks);
            this.ebookTab.setDisable(false);
        } else {
            this.ebookTab.setDisable(true);
        }

        if (job.has("audiobooks")) {
            JsonElement entry = job.getAsJsonArray("audiobooks");
            Type type = new TypeToken<List<Audiobook>>(){}.getType();
            List<Audiobook> audiobooks = gson.fromJson(entry, type);
            this.audiobooks.clear();
            this.audiobooks.addAll(audiobooks);
            this.audiobookTab.setDisable(false);
        } else {
            this.audiobookTab.setDisable(true);
        }
    }

    public void addCopy(ActionEvent actionEvent) {
        logger.info("addCopy");
        if (book != null) {
            logger.info("book != null");
            Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("resources/add/copy")
                    .addParameter("bookId", String.valueOf(book.getBookId()))
                    .build();

            httpResponse.ifPresent(this::addCopyResponsePresent);
        }
    }

    private void addCopyResponsePresent(CloseableHttpResponse httpResponse) {
        logger.info("addCopyResponsePresent");
        ResponseHandler.getBuilder(httpResponse)
                .setOnSuccess(this::addCopySucceed)
                .setOnFailure((ignored) -> logger.error("failed"))
                .handle();
    }

    private void addCopySucceed(HttpEntity entity) throws IOException {
        String responseString = EntityUtils.toString(entity);
        logger.info(responseString);
        reload();
    }

    public void reload() {
        if (book != null) {
            loadResources(book);
        }
    }
}








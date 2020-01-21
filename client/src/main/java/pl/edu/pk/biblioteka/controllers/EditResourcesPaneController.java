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
import javafx.stage.FileChooser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.controllers.model.AudiobookListCell;
import pl.edu.pk.biblioteka.controllers.model.CopyListCell;
import pl.edu.pk.biblioteka.controllers.model.EbookListCell;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;
import pl.edu.pk.biblioteka.data.Audiobook;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.Ebook;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class EditResourcesPaneController {
    private static final Logger logger = Logger.getLogger(EditResourcesPaneController.class.getName());
    private ObservableList<Copy> copies = FXCollections.observableArrayList();
    private ObservableList<Ebook> ebooks = FXCollections.observableArrayList();
    private ObservableList<Audiobook> audiobooks = FXCollections.observableArrayList();
    private BookDto book;

    @FXML public ListView<Copy> copyView;
    @FXML public ListView<Ebook> ebookView;
    @FXML public ListView<Audiobook> audiobookView;

    @FXML
    void initialize() {
        copyView.setItems(copies);
        ebookView.setItems(ebooks);
        audiobookView.setItems(audiobooks);

        copyView.setCellFactory((ignored) -> new CopyListCell(this));
        ebookView.setCellFactory((ignored) -> new EbookListCell(this));
        audiobookView.setCellFactory((ignored) -> new AudiobookListCell(this));
    }


    public void setBook(BookDto book) {
        logger.debug("setBook");
        this.book = book;
        reload();
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

        this.copies.clear();
        if (job.has("copies")) {
            JsonElement entry = job.getAsJsonArray("copies");
            Type type = new TypeToken<List<Copy>>(){}.getType();
            List<Copy> copies = gson.fromJson(entry, type);
            this.copies.addAll(copies);
        }

        this.ebooks.clear();
        if (job.has("ebooks")) {
            JsonElement entry = job.getAsJsonArray("ebooks");
            Type type = new TypeToken<List<Ebook>>(){}.getType();
            List<Ebook> ebooks = gson.fromJson(entry, type);
            this.ebooks.addAll(ebooks);
        }

        this.audiobooks.clear();
        if (job.has("audiobooks")) {
            JsonElement entry = job.getAsJsonArray("audiobooks");
            Type type = new TypeToken<List<Audiobook>>(){}.getType();
            List<Audiobook> audiobooks = gson.fromJson(entry, type);
            this.audiobooks.addAll(audiobooks);
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

    public void addEbook(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz ebook");
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        File file = fileChooser.showOpenDialog(App.getStage());
        uploadFile(file);
    }

    public void addAudiobook(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz audibook");
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("MP3", "*.mp3"));

        File file = fileChooser.showOpenDialog(App.getStage());
        uploadFile(file);
    }

    public void reload() {
        if (book != null) {
            loadResources(book);
        }
    }

    private void uploadFile(File file) {
        if (file != null) {
            logger.info(String.format("File: %s", file.toString()));
            try {
                HttpPost httpPost = new HttpPost("http://localhost:8080/add/resource");
                FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
                StringBody bookId = new StringBody(String.valueOf(book.getBookId()), ContentType.MULTIPART_FORM_DATA);


                HttpEntity entity = MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .addPart("bookid", bookId)
                        .addPart(
                                FormBodyPartBuilder.create("upfile", fileBody).build())
                        .build();

                httpPost.setEntity(entity);
                CloseableHttpResponse response = App.getHttpClient().execute(httpPost);

                ResponseHandler.getBuilder(response)
                        .setOnSuccess((e) -> reload())
                        .setOnFailure(logger::error)
                        .handle();

            } catch (IOException e) {
                logger.error(e);
            }
        } else {
            logger.error("File is null");
        }
    }
}



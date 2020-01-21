package pl.edu.pk.biblioteka.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.Author;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.data.Department;
import pl.edu.pk.biblioteka.data.Publisher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * Klasa z metodami do obsługi danych. Stworzona w celu unikniecia powtarzania kodu.
 */
public class DataUtils {
    private static final Logger logger = Logger.getLogger(DataUtils.class);

    public static void loadData(HttpEntity httpEntity,
                                ObservableList<Department> departmentsList, ComboBox<Department> departmentComboBox,
                                ObservableList<Author> authorsList, ComboBox<Author> authorComboBox,
                                ObservableList<Publisher> publishersList, ComboBox<Publisher> publisherComboBox) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(responseString);
        JSONArray departments = responseJson.getJSONArray("departments");
        JSONArray authors = responseJson.getJSONArray("authors");
        JSONArray publishers = responseJson.getJSONArray("publishers");


        departmentsList.clear();
        departments.forEach((d) -> departmentsList.add(Department.valueOf((JSONObject) d)));
        departmentComboBox.getSelectionModel().selectFirst();


        authorsList.clear();
        authors.forEach((a) -> authorsList.add(Author.valueOf((JSONObject) a)));
        authorComboBox.getSelectionModel().selectFirst();

        publishersList.clear();
        publishers.forEach((p) -> publishersList.add(Publisher.valueOf((JSONObject) p)));
        publisherComboBox.getSelectionModel().selectFirst();
    }

    public static void loadBooks(HttpEntity httpEntity, ObservableList<BookDto> bookList) throws IOException {
        String booksString = EntityUtils.toString(httpEntity);
        logger.info(booksString);

        Gson gson = new Gson();
        JsonObject job = gson.fromJson(booksString, JsonObject.class);
        JsonElement entry = job.getAsJsonArray("books");
        Type type = new TypeToken<List<BookDto>>(){}.getType();
        List<BookDto> books = gson.fromJson(entry, type);

        bookList.clear();
        bookList.addAll(books);
    }

    public static void loadData(HttpEntity httpEntity,
                                ObservableList<Department> departmentsList,
                                ObservableList<Author> authorsList,
                                ObservableList<Publisher> publishersList) throws IOException {
        String responseString = EntityUtils.toString(httpEntity);
        JSONObject responseJson = new JSONObject(responseString);
        JSONArray departments = responseJson.getJSONArray("departments");
        JSONArray authors = responseJson.getJSONArray("authors");
        JSONArray publishers = responseJson.getJSONArray("publishers");


        departmentsList.clear();
        departments.forEach((d) -> departmentsList.add(Department.valueOf((JSONObject) d)));

        authorsList.clear();
        authors.forEach((a) -> authorsList.add(Author.valueOf((JSONObject) a)));

        publishersList.clear();
        publishers.forEach((p) -> publishersList.add(Publisher.valueOf((JSONObject) p)));
    }

    public static Optional<CloseableHttpResponse> getBooksResponse(ComboBox<Department> departmentBox, ComboBox<Publisher> publisherBox, ComboBox<Author> authorBox,
                                                                       TextField titleField, TextField categoryField, TextField keywordsField) {
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
        return httpResponse;
    }
}

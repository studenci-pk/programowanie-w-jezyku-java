package pl.edu.pk.biblioteka;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.BookDto;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Głowna klasa aplikacji
 */
public class App extends Application {
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;
    private static final Logger logger = Logger.getLogger(App.class);
    private static Scene scene;
    private static Stage stage;
    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("loginStage")); //loginStage  raisin
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
        App.stage = stage;
    }

    /**
     * Wczytuje panel z pliku .fxml
     * @param fxml nazwa pliku fxml
     * @return
     */
    public static Pane loadPane(String fxml) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        try {
            return loader.load();
        } catch (IOException e) {
            logger.error("Error for file /fxml/" + fxml + ".fxml", e);
        }
        return null;
    }

    /**
     * Wczytuje panel z pliku .fxml i ustawia minimalne wymiary
     * @param fxml nazwa pliku fxml
     * @return panel
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        Pane pane = fxmlLoader.load();
        pane.setMinSize(MIN_WIDTH, MIN_HEIGHT);
        return pane;
    }

    public static FXMLLoader getLoader(String fxml) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return loader;
    }

    /**
     * Ustawia główny panel na podany z parametru
     * @param pane panel
     */
    public static void setRoot(Pane pane) {
        scene.setRoot(pane);
    }

    /**
     * Ustawia główny panel z podanego pliku
     * @param fxml nazwa pliku .fxml
     */
    public static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}

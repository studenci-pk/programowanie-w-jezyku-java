package pl.edu.pk.biblioteka.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import org.apache.log4j.Logger;

public class RaisinController {
    private static final Logger logger = Logger.getLogger(RaisinController.class);
    @FXML public WebView webView;
    @FXML public AnchorPane rootPane;

    @FXML
    void initialize() {
        webView.getEngine().load("https://helion.pl/eksiazki/mysql-mechanizmy-wewnetrzne-bazy-danych-sasha-pachev,msqlme.htm"); //https://www.viewstl.com/

        rootPane.widthProperty().addListener((observable -> {
            webView.getEngine().reload();
        }));
    }
}

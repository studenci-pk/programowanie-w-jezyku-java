package pl.edu.pk.biblioteka.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.controllers.LibrarianStageController;

import java.io.IOException;

public class PaneUtils {
    private static final Logger logger = Logger.getLogger(PaneUtils.class.getName());

    public static void backToAddBookPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + "librarianStage" + ".fxml"));
        if (loader != null) {
            Pane pane = loader.load();
            LibrarianStageController controller = (LibrarianStageController) loader.getController();
            if (controller != null) {
                controller.loadPane("addBookPane");
                App.setRoot(pane);
                logger.info("ok");
            } else {
                logger.error("Controller is null! ");
            }
        } else {
            logger.error("Loader is null! ");
        }
    }

    public static void loadLibrarianPane(String paneName) throws  IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + "librarianStage" + ".fxml"));
        if (loader != null) {
            Pane pane = loader.load();
            LibrarianStageController controller = (LibrarianStageController) loader.getController();
            if (controller != null) {
                controller.loadPane(paneName);
                App.setRoot(pane);
                logger.info("ok");
            } else {
                logger.error("Controller is null! ");
            }
        } else {
            logger.error("Loader is null! ");
        }
    }
}

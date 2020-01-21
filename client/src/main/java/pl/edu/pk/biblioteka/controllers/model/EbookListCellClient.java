package pl.edu.pk.biblioteka.controllers.model;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;
import pl.edu.pk.biblioteka.controllers.BrowseResourcesPaneController;
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.io.*;
import java.util.Optional;

public class EbookListCellClient extends ListCell<Ebook> {
    private static final Logger logger = Logger.getLogger(EbookListCellClient.class.getName());

    private HBox content;
    private Text text;
    private Button download;

    public EbookListCellClient(BrowseResourcesPaneController browseResourcesPaneController) {
        super();
        text = new Text();
        download = new Button("Pobierz");
        download.setPrefWidth(100);

        download.addEventHandler(ActionEvent.ACTION, (ignored) -> {
            Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("download")
                    .addParameter("ebookid", String.valueOf(getItem().getEbookId()))
                    .build();

            logger.info(httpResponse.isPresent());
            httpResponse.ifPresent(closeableHttpResponse -> ResponseHandler.getBuilder(closeableHttpResponse)
                    .setOnSuccess( (entity) -> {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Zapisz ebook");
                        fileChooser.setInitialFileName(browseResourcesPaneController.getBook().getTitle()+".pdf");
                        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

                        File file = fileChooser.showSaveDialog(App.getStage());
                        if (file != null) {
                            logger.info(file);
                            try {
                                InputStream inputStream = entity.getContent();
                                OutputStream outputStream = new FileOutputStream(file);

                                int c;
                                byte[] buf = new byte[1024];

                                while ((c = inputStream.read(buf, 0, buf.length)) > 0) {
                                    outputStream.write(buf, 0, c);
                                    outputStream.flush();
                                }

                                outputStream.close();
                                inputStream.close();
                            } catch (IOException e) {
                                logger.error(e);
                            }
                        }
                    })
                    .setOnFailure(logger::error)
                    .setAtTheEnd(browseResourcesPaneController::reload)
                    .handle());
        });

        VBox vBox = new VBox(text);
        content = new HBox(vBox, download);
        content.setSpacing(10);
        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(Ebook item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item != null && !empty) {
            text.setText(item.toString());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}

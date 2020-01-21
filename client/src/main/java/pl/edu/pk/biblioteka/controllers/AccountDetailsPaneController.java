package pl.edu.pk.biblioteka.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.controllers.model.ChargeListCell;
import pl.edu.pk.biblioteka.controllers.model.LoanListCell;
import pl.edu.pk.biblioteka.controllers.model.ReaderDto;
import pl.edu.pk.biblioteka.data.Charge;
import pl.edu.pk.biblioteka.data.LoanDto;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.PaneUtils;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountDetailsPaneController {
    private static final Logger logger = Logger.getLogger(AccountDetailsPaneController.class.getName());
    private ObservableList<LoanDto> charges = FXCollections.observableArrayList();
    private ObservableList<LoanDto> loans = FXCollections.observableArrayList();
    private ReaderDto reader;

    @FXML public Pane infoPane;
    @FXML public ListView<LoanDto> chargeView;
    @FXML public ListView<LoanDto> loanView;

    @FXML
    void initialize() {
        chargeView.setItems(charges);
        loanView.setItems(loans);

        chargeView.setCellFactory(listView -> new ChargeListCell(this));
        loanView.setCellFactory(listView -> new LoanListCell(this));
    }

    public void moveBack(ActionEvent actionEvent) throws IOException {
        PaneUtils.loadLibrarianPane("accountBrowsePane");
    }

    public void setReader(ReaderDto reader) {
        this.reader = reader;

        if (reader != null) {
            fillInfoPane(reader);
            loadLoans(reader);
        }
    }

    private HBox createHBox(String attr, String value) {
        HBox hbox = new HBox();
        Text attrText = new Text(attr);
        Text valueText = new Text(value);
        attrText.setStyle("-fx-font-weight: bold");
        valueText.setStyle("-fx-font-weight: normal");
        hbox.getChildren().addAll(attrText, valueText);
        return hbox;
    }

    private VBox createVBox(HBox... hbox) {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox);
        return vbox;
    }

    private void fillInfoPane(ReaderDto reader) {
        VBox column1 = createVBox(
                createHBox("Login: ", reader.getAccount().getLogin()),
                createHBox("Imię: ", reader.getName()),
                createHBox("Nazwisko: ", reader.getSurname())
        );

        column1.setPadding(new Insets(0,20,0,0));

        VBox column2 = createVBox(
                createHBox("Email: ", reader.getAccount().getEmail()),
                createHBox("Pesel: ", reader.getPesel()),
                createHBox("Data utworzenia: ", reader.getAccount().getCreateDate())
        );

        infoPane.getChildren().clear();
        infoPane.getChildren().add(new HBox(column1, column2));
    }

    public void reload() {
        if (reader != null) {
            loadLoans(reader);
        }
    }

    private void loadLoans(ReaderDto reader) {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/loans")
                .addParameter("accountid", String.valueOf(reader.getAccount().getAccountId()))
                .build();

        httpResponse.ifPresent(this::getLoansPresentResponse);
    }

    private void getLoansPresentResponse(CloseableHttpResponse closeableHttpResponse) {
        ResponseHandler.getBuilder(closeableHttpResponse)
                .setOnSuccess(entity -> {
                    String responseString = EntityUtils.toString(entity);
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();
                    JsonObject job = gson.fromJson(responseString, JsonObject.class);
                    JsonElement entry = job.getAsJsonArray("loans");

                    Type type = new TypeToken<ArrayList<LoanDto>>(){}.getType();
                    ArrayList<LoanDto> loans = gson.fromJson(entry, type);

                    List<Charge> charges = loans.stream().map(LoanDto::getCharge).collect(Collectors.toList());

                    loans.sort((o1, o2) -> Double.compare(o2.getCharge().getAmount(), o1.getCharge().getAmount()));
                    this.charges.clear();
                    this.charges.addAll(loans);
                    loans.sort((o1, o2) -> o2.getReservationStatus().getReservationStatusId()
                            - o1.getReservationStatus().getReservationStatusId());
                    this.loans.clear();
                    this.loans.addAll(loans);
                })
                .setOnFailure(logger::error)
                .handle();
    }
}



package pl.edu.pk.biblioteka.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.controllers.model.ClientChargeListCell;
import pl.edu.pk.biblioteka.data.Charge;
import pl.edu.pk.biblioteka.data.LoanDto;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OwingPaneController {
    private static final Logger logger = Logger.getLogger(OwingPaneController.class.getName());
    private ObservableList<Charge> owings = FXCollections.observableArrayList();

    @FXML
    ListView<Charge> owingView;

    @FXML
    public void initialize() {
        owingView.setItems(owings);

        owingView.setCellFactory(param -> new ClientChargeListCell());

        loadOwings();
    }

    private void loadOwings() {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/loans")
                .build();

        httpResponse.ifPresent(this::getLoansPresentResponse);
    }

    private void getLoansPresentResponse(CloseableHttpResponse closeableHttpResponse) {
        ResponseHandler.getBuilder(closeableHttpResponse)
                .setOnSuccess(entity -> {
                    String responseString = EntityUtils.toString(entity);
                    logger.info(responseString);
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();
                    JsonObject job = gson.fromJson(responseString, JsonObject.class);
                    JsonElement entry = job.getAsJsonArray("loans");

                    Type type = new TypeToken<ArrayList<LoanDto>>(){}.getType();
                    ArrayList<LoanDto> loans = gson.fromJson(entry, type);

                    // Wyciągnięcie opłat z 'loans' i stworzenie z nich listy
                    List<Charge> charges = loans.stream().map(LoanDto::getCharge).collect(Collectors.toList());
                    owings.addAll(charges);

                    logger.info(entry.toString());
                })
                .handle();
    }
}
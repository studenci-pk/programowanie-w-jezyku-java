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
import pl.edu.pk.biblioteka.controllers.model.ClientLoanListCell;
import pl.edu.pk.biblioteka.data.LoanDto;
import pl.edu.pk.biblioteka.utils.GetBuilder;
import pl.edu.pk.biblioteka.utils.ResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;

public class RentalPaneController {
    private ObservableList<LoanDto> rentals = FXCollections.observableArrayList();
    @FXML public ListView<LoanDto> listView;

    @FXML
    public void initialize() {
        listView.setItems(rentals);

        listView.setCellFactory(param -> new ClientLoanListCell());
        loadLoans();
    }

    private void loadLoans() {
        Optional<CloseableHttpResponse> httpResponse = GetBuilder.getBuilder("get/loans")
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

                    loans.forEach(l -> rentals.add(l));
                })
                .handle();
    }
}

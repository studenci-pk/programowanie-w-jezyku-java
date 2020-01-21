package pl.edu.pk.biblioteka.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;

import java.io.IOException;
import java.util.Optional;

import static pl.edu.pk.biblioteka.utils.Connector.getBasicURL;

/**
 * Klasa do komunikacji za pomoca metody GET
 */
public class GetBuilder {

    private static final Logger logger = Logger.getLogger(GetBuilder.class.getName());
    private HttpGet httpGet;

    public GetBuilder(String uri) {
        this.httpGet = new HttpGet(uri);
    }

    public static GetBuilder getBuilder(String name) {
        return new GetBuilder(getBasicURL().concat("/"+name));
    }

    public GetBuilder addParameter(String name, String value) {
        httpGet.addHeader(name, value);
        return this;
    }

    public Optional<CloseableHttpResponse> build() {
        try {
            httpGet.setHeader("Accept-Encoding","UTF-8");
            return Optional.of(App.getHttpClient().execute(httpGet));
        } catch (IOException e) {
            logger.error(e);
            App.setRoot("loginStage");
        }
        return Optional.empty();
    }
}
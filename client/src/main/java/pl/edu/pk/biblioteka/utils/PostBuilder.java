package pl.edu.pk.biblioteka.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.App;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa do komunikacji za pomoca metody POST
 */
public class PostBuilder {
    private List<NameValuePair> urlParameters = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(PostBuilder.class.getName());
    private HttpPost httpPost;

    private PostBuilder(String uri) {
        this.httpPost = new HttpPost(uri);
    }

    public static PostBuilder getBuilder(String name) {
        return new PostBuilder(Connector.getBasicURL().concat("/"+name));
    }

    public PostBuilder addParameter(String name, String value) {
        urlParameters.add(new BasicNameValuePair(name, value));
        return this;
    }

    public PostBuilder addParameter(String name, Integer value) {
        urlParameters.add(new BasicNameValuePair(name, String.valueOf(value)));
        return this;
    }

    public Optional<CloseableHttpResponse> build() {
        if(!urlParameters.isEmpty()) {
            // Dodanie parametrów
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(urlParameters, "UTF-8");
                httpPost.setEntity(formEntity);
            } catch (UnsupportedEncodingException e) {
                logger.error(e);
            }
        }

        try {
            // Wykonanie i zwrócenie odpowiedzi
            return Optional.of(App.getHttpClient().execute(httpPost));
        } catch (IOException e) {
            logger.error(e);
            App.setRoot("loginStage"); // Wróc do logowania jesli blad
        }

        return Optional.empty();
    }
}
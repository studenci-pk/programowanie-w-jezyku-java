package pl.edu.pk.biblioteka.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Klasa obsługująca odpowiedzi '(Closeable)HttpResponse'
 */
public class ResponseHandler {

    private ResponseHandler(CloseableHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    /**
     * Utworzenie obiektu budowniczego
     * @param httpResponse
     * @return
     */
    public static ResponseHandler getBuilder(CloseableHttpResponse httpResponse) {
        return new ResponseHandler(httpResponse);
    }

    /**
     * Ustawienie metody wykonującej się na sam koniec
     * @param atTheEnd
     * @return
     */
    public ResponseHandler setAtTheEnd(Runnable atTheEnd) {
        this.atTheEnd = atTheEnd;
        return this;
    }

    /**
     * Ustawienie metody wykonującej się gdy zajdzie blad
     * @param errorOccurred
     * @return
     */
    public ResponseHandler setErrorOccurred(Runnable errorOccurred) {
        this.errorOccurred = errorOccurred;
        return this;
    }

    /**
     * Ustawienie metody wykonującej się gdy otrzymano poprawna odpowiedz
     * @param onSuccess
     * @return
     */
    public ResponseHandler setOnSuccess(EntityRunnableWithIOException onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    /**
     * Ustawienie metody wykonującej się gdy otrzymano blad
     * @param onFailure
     * @return
     */
    public ResponseHandler setOnFailure(EntityRunnableWithIOException onFailure) {
        this.onFailure = onFailure;
        return this;
    }

    /**
     * Wykoanie obsługi odpowiedzi
     */
    public void handle() {
        StatusLine statusLine = httpResponse.getStatusLine();
        HttpEntity entity = httpResponse.getEntity();

        try {
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                if (onSuccess != null)
                    onSuccess.run(entity);
            } else {
                if (onFailure != null)
                    onFailure.run(entity);
            }
        } catch(IOException e){
            logger.error(e);
            if (errorOccurred != null) {
                errorOccurred.run();
                logger.error(e.getMessage(), e);
            }
        } finally {
            try {
                EntityUtils.consume(entity);
                httpResponse.close();
            } catch (IOException ignored) { }
        }

        if (atTheEnd != null)
            atTheEnd.run();
    }

    private Runnable atTheEnd = null;
    private Runnable errorOccurred = null;
    private EntityRunnableWithIOException onSuccess = null;
    private EntityRunnableWithIOException onFailure = null;
    private CloseableHttpResponse httpResponse;
    private static final Logger logger = Logger.getLogger(ResponseHandler.class.getName());
}
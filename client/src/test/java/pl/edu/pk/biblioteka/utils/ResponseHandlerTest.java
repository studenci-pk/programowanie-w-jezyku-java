package pl.edu.pk.biblioteka.utils;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ResponseHandlerTest {

    @Test
    public void httpStatusIsSC_OK() {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(response.getStatusLine()).thenReturn(statusLine);

        EntityRunnableWithIOException onSuccess = mock(EntityRunnableWithIOException.class);
        EntityRunnableWithIOException onFailure = mock(EntityRunnableWithIOException.class);

        ResponseHandler.getBuilder(response)
                .setOnSuccess(onSuccess)
                .setOnFailure(onFailure)
                .handle();

        try {
            verify(onSuccess, times(1)).run(null);
            verify(onFailure, times(0)).run(null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void httpStatusIsNotSC_OK() {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        when(response.getStatusLine()).thenReturn(statusLine);

        EntityRunnableWithIOException onSuccess = mock(EntityRunnableWithIOException.class);
        EntityRunnableWithIOException onFailure = mock(EntityRunnableWithIOException.class);

        ResponseHandler.getBuilder(response)
                .setOnSuccess(onSuccess)
                .setOnFailure(onFailure)
                .handle();

        try {
            verify(onSuccess, times(0)).run(null);
            verify(onFailure, times(1)).run(null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test//(expected = IOException.class)
    public void IOErrorThrow() throws IOException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(response.getStatusLine()).thenReturn(statusLine);

        EntityRunnableWithIOException onSuccess = mock(EntityRunnableWithIOException.class);
        doThrow(new IOException()).when(onSuccess).run(null);

        Runnable onError = mock(Runnable.class);

        ResponseHandler.getBuilder(response)
                .setOnSuccess(onSuccess)
                .setErrorOccurred(onError)
                .handle();

        verify(onSuccess, times(1)).run(null);
        verify(onError, times(1)).run();
    }
}
package pl.edu.pk.biblioteka.utils;

import org.apache.http.HttpEntity;
import java.io.IOException;

public interface EntityRunnableWithIOException {
    void run(HttpEntity entity) throws IOException;
}

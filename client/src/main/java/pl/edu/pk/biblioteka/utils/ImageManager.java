package pl.edu.pk.biblioteka.utils;

import javafx.scene.image.Image;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa do ladowania obrazkow
 */
public class ImageManager {

    private static final Logger logger = Logger.getLogger(ImageManager.class.getName());
    // Nazwy obrazkow
    public static final String BOOK = "book";
    public static final String EBOOK = "ebook";
    public static final String AUDIOBOOK = "audiobook";
    public static final String NO_IMAGE = "no-image";
    public static final String APPROVED_USER = "approved-user";
    public static final String BLOCKED_USER = "blocked-user";

    //Mapa z obrazkami i ich nazwami
    private static Map<String, Image> images;
    static {
        images = new HashMap<>();
        loadImage(BOOK);
        loadImage(EBOOK);
        loadImage(AUDIOBOOK);
    }

    private ImageManager() { }

    /**
     * Funckja zwracajaca obrazek na podstawie klucza-nazwy
     * @param key obrazek o podanej nazwie lub obrazek NO_IMAGE (jesli nie znaleziono)
     * @return
     */
    public static Image getImage(String key) {
        if (!images.containsKey(key)) {
            loadImage(key);
        }

        return images.getOrDefault(key, loadImage(NO_IMAGE));
    }

    /**
     * Funkcja wczytujaca obrazki
     * @param fileName nazwa pliku
     * @return wczytany obrazek
     */
    private static Image loadImage(String fileName) {
        Image image = null;
        try {
            image = new Image(ImageManager.class.getResourceAsStream(String.format("/images/%s.png", fileName)));
            images.put(fileName, image);
        } catch (Exception e) {
            logger.error("Unable to load image. Loaded default icon", e);
        }

        return image;
    }
}

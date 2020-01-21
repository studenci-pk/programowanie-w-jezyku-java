package pl.edu.pk.biblioteka.utils;

import java.util.Map;

/**
 * Klasa z pomocniczymi funkcjami dla 'Map'
 */
public class MapUtils {
    /**
     * Zamienia (jeśli istnieje) klucz typu String na inny zachowując ten sam obiekt
     * @param map
     * @param oldKey
     * @param newKey
     * @param <String>
     * @param <V>
     */
    public static <String, V> void renameKey(Map<String, V> map, String oldKey, String newKey) {
        V v = map.remove(oldKey);
        if (v != null) {
            map.put(newKey, v);
        }
    }
}

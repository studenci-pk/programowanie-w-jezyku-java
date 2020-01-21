package pl.edu.pk.biblioteka.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MapUtilsTest {

    @Test
    public void renameKey() {
        Map<String, Object> stringObjectMap = new HashMap<String, Object>() {{
            put("key1", new Object());
        }};

        MapUtils.renameKey(stringObjectMap, "key1", "key2");

        assertTrue(stringObjectMap.containsKey("key2"));
        assertFalse(stringObjectMap.containsKey("key1")); // 4.72
    }
}
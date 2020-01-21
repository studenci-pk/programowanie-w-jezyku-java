package pl.edu.pk.biblioteka.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa z pomocniczymi funkcjami
 */
public class WebUtils {
    /**
     * Metoda wyciągająca wszystkie pary klucz-wartosc z nagłowka
     * @param request
     * @return mapa z kluczem i wartością
     */
    public static Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    /**
     * Metoda wyciągająca wszystkie pary klucz-wartosc przesłanych parametrów
     * @param request
     * @return mapa z kluczem i wartością
     */
    public static Map<String, String> getParametersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();
        Enumeration parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }

        return map;
    }
}
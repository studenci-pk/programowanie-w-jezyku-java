package pl.edu.pk.biblioteka.utils;

import org.junit.Test;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebUtilsTest {

    @Test
    public void getHeadersInfo() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Map<String, String> headers = new HashMap<String, String>() {{
            put("attr1", "val1");
            put("attr2", "val2");
            put("attr3", "val3");
            put("attr4", "val4");
        }};

        when(request.getHeaderNames()).thenReturn(new Vector<>(headers.keySet()).elements());
        headers.forEach((key, value) -> when(request.getHeader(key)).thenReturn(value));

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);

        for (Map.Entry<String, String> header: headers.entrySet()) {
            assertEquals(header.getValue(), headersInfo.get(header.getKey()));
        }
    }

    @Test
    public void getParametersInfo() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Map<String, String> headers = new HashMap<String, String>() {{
            put("attr1", "val1");
            put("attr2", "val2");
            put("attr3", "val3");
            put("attr4", "val4");
            put("attr5", "val5");
            put("attr6", "val6");
        }};

        when(request.getHeaderNames()).thenReturn(new Vector<>(headers.keySet()).elements());
        headers.forEach((key, value) -> when(request.getHeader(key)).thenReturn(value));

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);

        for (Map.Entry<String, String> header: headers.entrySet()) {
            assertEquals(header.getValue(), headersInfo.get(header.getKey()));
        }
    }
}
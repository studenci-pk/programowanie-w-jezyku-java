package pl.edu.pk.biblioteka.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Filtr ustawiający kodowanie UTF-8
 */
public @WebFilter
class UTF8CharacterEncodingFilter implements Filter {
    private static final Logger logger = Logger.getLogger(UTF8CharacterEncodingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}

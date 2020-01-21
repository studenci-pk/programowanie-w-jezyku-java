package pl.edu.pk.biblioteka;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // Skanuje projekt w poszukwianiu serwletów, filtrów
@SpringBootApplication // Auto-konfiguruje projekt
public class App {
    private static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
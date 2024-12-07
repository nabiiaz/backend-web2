package ufpr.web.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    static {
        try {
            String projectRoot = System.getProperty("user.dir");
            if (projectRoot == null || projectRoot.isBlank()) {
                throw new IllegalStateException("Unable to determine project root directory");
            }

            Dotenv dotenv = Dotenv.configure()
                    .directory(projectRoot)
                    .load();

            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            System.err.println("Failed to load .env configuration: " + e.getMessage());
            throw e;
        }
    }
}

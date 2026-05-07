package taka.example.spring_project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(List<String> allowedOrigins) {

    public String[] allowedOriginsArray() {
        if (allowedOrigins == null) {
            return new String[0];
        }
        return allowedOrigins.toArray(String[]::new);
    }
}

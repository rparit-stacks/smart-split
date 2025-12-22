package com.rps.smartsplit.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${CLOUDINARY.URL}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", extractCloudName(cloudinaryUrl));
        config.put("api_key", extractApiKey(cloudinaryUrl));
        config.put("api_secret", extractApiSecret(cloudinaryUrl));
        return new Cloudinary(config);
    }

    private String extractCloudName(String url) {
        // cloudinary://api_key:api_secret@cloud_name
        if (url != null && url.contains("@")) {
            return url.substring(url.lastIndexOf("@") + 1);
        }
        return "";
    }

    private String extractApiKey(String url) {
        // cloudinary://api_key:api_secret@cloud_name
        if (url != null && url.contains("://") && url.contains(":")) {
            String part = url.substring(url.indexOf("://") + 3);
            if (part.contains(":")) {
                return part.substring(0, part.indexOf(":"));
            }
        }
        return "";
    }

    private String extractApiSecret(String url) {
        // cloudinary://api_key:api_secret@cloud_name
        if (url != null && url.contains(":") && url.contains("@")) {
            String part = url.substring(url.indexOf("://") + 3);
            if (part.contains(":")) {
                String afterColon = part.substring(part.indexOf(":") + 1);
                if (afterColon.contains("@")) {
                    return afterColon.substring(0, afterColon.indexOf("@"));
                }
            }
        }
        return "";
    }
}


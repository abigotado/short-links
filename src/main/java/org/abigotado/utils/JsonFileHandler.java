package org.abigotado.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.abigotado.config.Messages;
import org.abigotado.links.entity.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonFileHandler {
    private static final Logger logger = LoggerFactory.getLogger(JsonFileHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static Map<String, Link> loadLinks(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            logger.error(Messages.JSON_LOAD_ERROR, e);
            return new HashMap<>();
        }
    }

    public static void saveLinks(Map<String, Link> links, String filePath) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), links);
        } catch (IOException e) {
            logger.error(Messages.JSON_SAVE_ERROR, e);
        }
    }
}
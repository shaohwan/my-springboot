package com.demo.daniel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Conversion from object to json string error: {}", e.getLocalizedMessage());
        }
        return null;
    }

    public static <T> T toObject(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.error("Conversion from json string to object error: {}", e.getLocalizedMessage());
        }
        return null;
    }

    public static JsonNode parse(String jsonStr) {
        try {
            return OBJECT_MAPPER.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            log.error("Conversion from json string to json node error: {}", e.getLocalizedMessage());
        }
        return null;
    }
}

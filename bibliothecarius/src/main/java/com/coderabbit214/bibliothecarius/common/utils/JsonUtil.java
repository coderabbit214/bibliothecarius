package com.coderabbit214.bibliothecarius.common.utils;

import com.coderabbit214.bibliothecarius.common.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Mr_J
 */
public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }


    public static String toJsonString(Object object) {
        if (object == null) {
            throw new BusinessException("object is null, unable to convert");
        }

        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BusinessException("JSON transformation error");
        }
    }

    public static <T> T toObject(String json, Class<T> cla, String exceptionContent) {
        try {
            return MAPPER.readValue(json, cla);
        } catch (IOException e) {
            e.printStackTrace();
            if ("".equals(exceptionContent) || exceptionContent == null) {
                throw new BusinessException("json string cannot be converted to object");
            }
            throw new BusinessException(exceptionContent);
        }
    }

    public static <T> T toObject(String json, Class<T> cla) {
        return toObject(json, cla, null);
    }


    public static <T> List<T> toArray(String json, Class<T> cla) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, cla);
        try {
            return MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new BusinessException("json string cannot be converted to array");
        }
    }

    public static <T> T toObject(String json, Type type) {
        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructType(type));
        } catch (Exception e) {
            throw new BusinessException("json string cannot be converted to type");
        }
    }


    public static <T> T toObject(InputStream in, Class<T> cla) {
        if (in == null) {
            throw new BusinessException("InputStream is null");
        }

        try {
            return MAPPER.readValue(in, cla);
        } catch (IOException e) {
            throw new BusinessException("json string cannot be converted");
        }
    }

    public static <T> T toObject(Reader reader, Class<T> cla) {
        if (reader == null) {
            throw new BusinessException("Reader is null");
        }

        try {
            return MAPPER.readValue(reader, cla);
        } catch (IOException e) {
            throw new BusinessException("json string cannot be converted");
        }
    }
}
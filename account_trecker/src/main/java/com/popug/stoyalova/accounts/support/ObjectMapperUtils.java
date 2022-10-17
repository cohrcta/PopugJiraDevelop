package com.popug.stoyalova.accounts.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectMapperUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setTimeZone(TimeZone.getDefault())
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

    public static String toJson(Object bean) {
        if (bean == null) {
            return null;
        }

        try {
            return objectMapper().writeValueAsString(bean);
        } catch (Exception e) {
            throw new JsonConverterParseException(
                    "Exception has occured while printing bean " + bean.getClass() + " to string", e);
        }
    }

    public static <T> T toBean(String src, Class<T> typeReference) {
        if (StringUtils.isBlank(src)) {
            return null;
        }

        try {
            return objectMapper().readValue(src, typeReference);
        } catch (Exception e) {
            throw new JsonConverterParseException(
                    "Exception has occured while reading " + src + " to bean " + typeReference, e);
        }
    }

}

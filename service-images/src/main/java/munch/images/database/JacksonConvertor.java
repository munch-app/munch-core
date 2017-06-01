package munch.images.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.munch.utils.block.BlockConverter;

import java.io.IOException;

/**
 * Jackson convertor using com.fasterxml.jackson
 * All exception is wrapped into RuntimeException
 * <p>
 * Created By: Fuxing Loh
 * Date: 1/6/2017
 * Time: 6:30 PM
 * Project: munch-core
 */
@Singleton
public final class JacksonConvertor implements BlockConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String toJson(Object object) {
        if (object == null) return null;
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(Class<T> clazz, String content) {
        if (content == null) return null;

        try {
            return mapper.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

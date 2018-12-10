package munch.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import munch.restful.core.exception.JsonException;
import munch.restful.server.JsonTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:37 PM
 * Project: munch-core
 */
@Singleton
public final class CleanerTransformer extends JsonTransformer {

    private final ObjectMapper objectMapper;

    /**
     * Dunno wtf I am doing just copying from:
     * https://stackoverflow.com/questions/25511604/jackson-json-modify-object-before-serialization/25513874
     */
    @Inject
    public CleanerTransformer(Set<ObjectCleaner> cleaners) {
        Map<Class<?>, JsonSerializer<?>> serializerMap = new HashMap<>();

        SimpleModule module = new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer modifySerializer(SerializationConfig config, BeanDescription desc, JsonSerializer serializer) {
                        for (ObjectCleaner cleaner : cleaners) {
                            if (cleaner.getClazz() == desc.getBeanClass()) {
                                return serializerMap.computeIfAbsent(cleaner.getClazz(), aClass -> new JsonSerializer() {

                                    @Override
                                    @SuppressWarnings("unchecked")
                                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                                        cleaner.clean(value);
                                        serializer.serialize(value, gen, serializers);
                                    }
                                });
                            }
                        }

                        return serializer;
                    }
                });
            }
        };

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @Override
    protected String toString(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }
}

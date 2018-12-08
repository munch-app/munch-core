package munch.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import munch.restful.core.exception.JsonException;
import munch.restful.server.JsonTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
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

    private final BeanSerializerFactoryWithVisibleConstructingMethod beanSerializer = new BeanSerializerFactoryWithVisibleConstructingMethod();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public CleanerTransformer(Set<ObjectCleaner> cleaners) {
        SimpleModule module = new SimpleModule();
        cleaners.forEach(cleaner -> {
            JsonSerializer<Object> serializer = new JsonSerializer<>() {
                JavaType type = TypeFactory.defaultInstance().constructType(cleaner.getClazz());

                @Override
                @SuppressWarnings("unchecked")
                public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                    cleaner.clean(value);

                    BeanDescription description = provider.getConfig().introspect(type);
                    JsonSerializer<Object> defaultSerializer = beanSerializer.constructBeanSerializer(provider, description);
                    defaultSerializer.serialize(value, gen, provider);

                }
            };
            module.addSerializer(cleaner.getClazz(), serializer);
        });
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

    /**
     * Dunno wtf I am doing just copying from:
     * https://stackoverflow.com/questions/25511604/jackson-json-modify-object-before-serialization/25513874#25513874
     */
    static class BeanSerializerFactoryWithVisibleConstructingMethod extends BeanSerializerFactory {
        BeanSerializerFactoryWithVisibleConstructingMethod() {
            super(BeanSerializerFactory.instance.getFactoryConfig());
        }

        @Override
        public JsonSerializer<Object> constructBeanSerializer(SerializerProvider prov, BeanDescription beanDesc) throws JsonMappingException {
            return super.constructBeanSerializer(prov, beanDesc);
        }

    }
}

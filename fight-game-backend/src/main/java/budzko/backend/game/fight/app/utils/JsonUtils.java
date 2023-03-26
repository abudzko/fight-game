package budzko.backend.game.fight.app.utils;

import budzko.backend.game.fight.app.messaging.message.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER = build();

    public static ObjectMapper jsonMapper() {
        return OBJECT_MAPPER;
    }

    public static ObjectMapper build() {
        return Jackson2ObjectMapperBuilder.json()
                .indentOutput(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .modules(
                        new JavaTimeModule(),
                        new Jdk8Module()
                ).build();
    }

    /**
     * @throws RuntimeException if object can't be converted into string
     */
    public static String toString(Object object) {
        try {
            return jsonMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't convert to string. Reason: %s".formatted(e.getMessage()));
        }
    }

    /**
     * @throws RuntimeException if string can't be converted into T
     */
    public static <T> T parse(String source, Class<T> type) {
        try {
            return jsonMapper().readValue(source, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't convert to string. Reason: %s".formatted(e.getMessage()));
        }
    }

    public static void main(String[] args) {
        //TODO move to unit tests
        Message m = Message.create();
        String s = toString(m);
        System.out.println(s);
        s = "{\"data\":null,\"msgType\":null, \"userId\": 123}";
        Message message = parse(s, Message.class);
        System.out.println(message);
    }
}

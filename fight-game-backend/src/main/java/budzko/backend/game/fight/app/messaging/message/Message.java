package budzko.backend.game.fight.app.messaging.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
public class Message {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Boolean> arrowEvent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> mouseEvent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fightId;
    private MessageType msgType;
    @JsonIgnore
    private String userId;

    public static Message create() {
        return new Message();
    }
}

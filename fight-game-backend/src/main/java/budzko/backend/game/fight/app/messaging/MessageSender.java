package budzko.backend.game.fight.app.messaging;

import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageSender {

    private static final String TOPIC_RESPONSE = "/topic/response";
    private final SimpMessagingTemplate messagingTemplate;

    public void send(Message message) {
        String payload;
        try {
            payload = JsonUtils.toString(message);
            messagingTemplate.convertAndSendToUser(message.getUserId(), TOPIC_RESPONSE, payload);
            log.debug("Message was sent: %s".formatted(payload));
        } catch (RuntimeException e) {
            log.error("Failed: can't send message %s. Reason: %s.".formatted(message, e.getMessage()));
        }
    }
}

package budzko.backend.game.fight.app.web.websocket.controller;

import budzko.backend.game.fight.app.messaging.MessageReceiver;
import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@Slf4j
@MessageMapping("/game")
@RequiredArgsConstructor
public class WebSocketMessageController {

    private final MessageReceiver messageReceiver;

    /**
     * Handle incoming messages
     *
     * @param messageSource  string representation of the message(json)
     * @param headerAccessor access to message headers
     */
    @MessageMapping("/message")
    public void message(
            String messageSource,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Message message = JsonUtils.parse(messageSource, Message.class);
        Optional.ofNullable(headerAccessor.getUser())
                .ifPresent((principal) -> message.setUserId(principal.getName()));
        messageReceiver.receive(message);
    }
}

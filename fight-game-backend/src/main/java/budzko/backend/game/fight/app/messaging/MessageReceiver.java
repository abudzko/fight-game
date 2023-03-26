package budzko.backend.game.fight.app.messaging;

import budzko.backend.game.fight.app.messaging.handler.Handler;
import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.messaging.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageReceiver {

    private final Map<MessageType, Handler> handlers;

    public void receive(Message message) {
        Optional.ofNullable(handlers.get(message.getMsgType()))
                .ifPresentOrElse(
                        handler -> handler.handle(message),
                        () -> log.warn("Ignored. Can't find handler for message %s".formatted(message))
                );
    }
}

package budzko.backend.game.fight.app.messaging;

import budzko.backend.game.fight.app.messaging.handler.Handler;
import budzko.backend.game.fight.app.messaging.message.MessageType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MessageConfig {
    @Bean
    public Map<MessageType, Handler> handlers(Collection<Handler> handlers) {
        return handlers.stream().collect(Collectors.toMap(Handler::getType, Function.identity()));
    }
}

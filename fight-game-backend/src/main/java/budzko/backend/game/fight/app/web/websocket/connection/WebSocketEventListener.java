package budzko.backend.game.fight.app.web.websocket.connection;

import budzko.backend.game.fight.app.game.manager.FightManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final FightManager fightManager;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        Optional.ofNullable(event.getUser())
                .ifPresent(principal -> {
                        }
                );
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        Optional.ofNullable(event.getUser())
                .ifPresentOrElse(principal -> fightManager.deletePlayer(principal.getName()),
                        () -> log.warn("Principal wasn't set. %s".formatted(event))
                );
    }
}

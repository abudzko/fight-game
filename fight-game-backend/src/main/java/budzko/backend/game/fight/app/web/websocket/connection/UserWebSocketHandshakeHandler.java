package budzko.backend.game.fight.app.web.websocket.connection;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * To send dedicated to user message we have to assign some name to principal(until we will add authentication)<br>
 * On client side we must subscribe on <strong>/user</strong>/topic/response<br>
 * In this case server will subscribe /user/<strong>/>userName</strong>/topic/response to send message to specific user<br>
 * <a href="https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket">Sending message to specific user on Spring Websocket</a><br>
 * For new web socket connection - new user id will be generated! So it's more like connection id.
 *
 * @see SimpMessagingTemplate#convertAndSendToUser(java.lang.String, java.lang.String, java.lang.Object, java.util.Map, org.springframework.messaging.core.MessagePostProcessor)
 */
public class UserWebSocketHandshakeHandler extends DefaultHandshakeHandler {

    /**
     * Generate a username(id) for websocket connection
     */
    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        return new Principal() {
            private final String userName = UUID.randomUUID().toString();

            @Override
            public String getName() {
                return userName;
            }
        };
    }
}

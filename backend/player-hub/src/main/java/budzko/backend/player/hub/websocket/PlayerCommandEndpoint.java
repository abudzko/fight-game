package budzko.backend.player.hub.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@ServerEndpoint(value = "/websocket/message/player")
@Slf4j
@Component
public class PlayerCommandEndpoint {
    @OnMessage
    public void onMessage(Session session, byte[] msg) {
        log.debug("onMessage: sessionId = %s. %s".formatted(session.getId(), new String(msg, StandardCharsets.UTF_8)));
////        try {
//        byte[] bytes = "Response: %s".formatted(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8);
//        session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
//        } catch (IOException e) {
//            log.info(e.getMessage(), e);
//        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.debug("onOpen: sessionId = %s. Player: %s".formatted(session.getId(), session.getUserPrincipal().getName()));
    }

    @OnClose
    public void onClose(Session session) {
        log.debug("onClose: sessionId = %s".formatted(session.getId()));
    }

    @OnError
    public void onError(Session session, Throwable ex) {
        log.error("onError: sessionId = %s. %s".formatted(session.getId(), ex.toString()));
    }
}

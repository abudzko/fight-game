package budzko.backend.game.fight.app.messaging.handler;

import budzko.backend.game.fight.app.game.manager.FightManager;
import budzko.backend.game.fight.app.messaging.MessageSender;
import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.messaging.message.MessageFactory;
import budzko.backend.game.fight.app.messaging.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinFightHandler implements Handler {

    private final FightManager fightManager;
    private final MessageSender messageSender;
    private final MessageFactory messageFactory;

    @Override
    public MessageType getType() {
        return MessageType.JOIN_FIGHT;
    }

    @Override
    public void handle(Message message) {
        Optional.ofNullable(message.getFightId())
                .ifPresentOrElse(
                        fightId -> joinFight(fightId, message),
                        () -> fightManager.getRandFightId()
                                .ifPresentOrElse(
                                        fightId -> joinFight(fightId, message),
                                        () -> log.info("Failed to join into random fight. Can't find now one fight")
                                )
                );
    }

    private void joinFight(String fightId, Message message) {
        String userId = message.getUserId();
        String playerPublicId = fightManager.addPlayer(message.getUserId(), fightId);
        log.info("User %s joined to fight %s".formatted(userId, fightId));
        Message joinedToFightMessage = messageFactory.buildJoinedToFightMessage(
                userId,
                playerPublicId,
                fightId
        );
        messageSender.send(joinedToFightMessage);
    }
}

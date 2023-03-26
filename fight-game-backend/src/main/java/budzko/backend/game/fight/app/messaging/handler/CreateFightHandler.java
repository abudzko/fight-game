package budzko.backend.game.fight.app.messaging.handler;

import budzko.backend.game.fight.app.game.manager.FightManager;
import budzko.backend.game.fight.app.messaging.MessageSender;
import budzko.backend.game.fight.app.messaging.message.Message;
import budzko.backend.game.fight.app.messaging.message.MessageFactory;
import budzko.backend.game.fight.app.messaging.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * User sends {@code CREATE_FIGHT} message to create new fight
 * Server should send information about created fight back to user
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateFightHandler implements Handler {
    private final FightManager fightManager;
    private final MessageSender messageSender;
    private final MessageFactory messageFactory;

    @Override
    public MessageType getType() {
        return MessageType.CREATE_FIGHT;
    }

    @Override
    public void handle(Message message) {
        String userId = message.getUserId();
        String fightId = fightManager.createFight();
        String playerPublicId = fightManager.addPlayer(userId, fightId);
        log.info("Fight was created: %s".formatted(fightId));
        Message fightCreatedMessage = messageFactory.buildFightCreatedResponseMessage(
                userId,
                playerPublicId,
                fightId
        );
        messageSender.send(fightCreatedMessage);
    }
}

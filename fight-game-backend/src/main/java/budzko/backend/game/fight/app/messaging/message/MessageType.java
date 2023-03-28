package budzko.backend.game.fight.app.messaging.message;

public enum MessageType {
    /**
     * Request to create a fight
     */
    CREATE_FIGHT,
    /**
     * Response for {@link MessageType#CREATE_FIGHT}
     */
    FIGHT_CREATED,
    /**
     * Outgoing message about changes in fight
     */
    FIGHT_UPDATE,
    /**
     * Request to join into the fight
     */
    JOIN_FIGHT,
    /**
     * Response for {@link MessageType#JOIN_FIGHT}
     */
    JOINED_TO_FIGHT,
    /**
     * Incoming message with user events(mouse clicked, button pressed)
     */
    PLAYER_EVENT
}

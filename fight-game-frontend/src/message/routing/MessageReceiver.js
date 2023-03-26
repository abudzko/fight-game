import FightCreatedHandler from "../handler/FightCreatedHandler";
import FightUpdateHandler from "../handler/FightUpdateHandler"
import JoinedToFightHandler from "../handler/JoinedToFightHandler"

export default class MessageReceiver {

    constructor(
        playerController,
        graphicController
    ) {
        this.id = Math.random();
        let fightCreatedHandler = new FightCreatedHandler(playerController, graphicController);
        let fightUpdateHandler = new FightUpdateHandler(playerController, graphicController)
        let joinedToFightHandler = new JoinedToFightHandler(playerController, graphicController)
        this.handlers = {};
        this.handlers[fightCreatedHandler.getType()] = fightCreatedHandler;
        this.handlers[fightUpdateHandler.getType()] = fightUpdateHandler;
        this.handlers[joinedToFightHandler.getType()] = joinedToFightHandler;
    }

    receive(msg) {
        let message = JSON.parse(msg);
        this.handlers[message.msgType].handle(message);
        // console.log(`${this.id} Message received: ${msg}`);
    }
}

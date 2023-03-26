import { FIGHT_UPDATE } from "../Message";

export default class FightUpdateHandler {
    constructor(
        playerController,
        graphicController
    ) {
        this.playerController = playerController;
        this.graphicController = graphicController;
    }

    getType() {
        return FIGHT_UPDATE;
    }

    handle(message) {
        let states = message.states;
        this.graphicController.updatePlayersByStates(states);
    }
}

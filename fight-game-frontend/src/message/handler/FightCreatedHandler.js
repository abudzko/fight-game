import Player from "../../game/Player";
import { FIGHT_CREATED } from "../Message";

export default class FightCreatedHandler {

    constructor(playerController, graphicController) {
        this.playerController = playerController;
        this.graphicController = graphicController;
    }

    getType() {
        return FIGHT_CREATED;
    }

    handle(message) {
        // console.log(message);
        let player = new Player();
        player.id = message.playerPublicId;
        player.fightId = message.fightId;
        this.playerController.player = player;
        this.graphicController.addPlayer(player);
    }
}

import Player from "../../game/Player";
import { JOINED_TO_FIGHT } from "../Message";

export default class JoinedToFightHandler {

    constructor(playerController, graphicController) {
        this.playerController = playerController;
        this.graphicController = graphicController;
    }

    getType() {
        return JOINED_TO_FIGHT;
    }

    handle(message) {
        console.log(message);
        this.playerController.onJoinedCallback();
        let player = new Player();
        player.id = message.playerPublicId;
        player.fightId = message.fightId;
        this.playerController.player = player;
        this.graphicController.addPlayer(player);
        this.playerController.onJoinedCallback();
    }
}

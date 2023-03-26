import DrawablePlayer from "./objects/DrawablePlayer";

export default class GraphicController {
    drawableObjects = new Map();
    playerIds = [];

    addPlayer(player) {
        this.drawableObjects.set(player.id, new DrawablePlayer(player));
    }

    updateByState(state) {
        this.drawableObjects.set(state.playerId, new DrawablePlayer(state));
    }

    updatePlayersByStates(states) {
        for (let state of states) {
            this.updateByState(state);
        }
        //Find and remove nonexistent player id(player left fight)
        let playerIdArray = Array.from(states).map(state => state.playerId);
        let playerIdSet = new Set(playerIdArray);
        this.playerIds.filter(playerId => !playerIdSet.has(playerId))
            .forEach(playerId => this.drawableObjects.delete(playerId));
        this.playerIds = playerIdArray;
    }
}

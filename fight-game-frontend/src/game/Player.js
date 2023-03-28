export class PlayerMoveDirection {
    up = false;
    down = false;
    left = false;
    right = false;
}

export default class Player {
    playerPublicId;
    fightId;
    x = -1;
    y = -1;
    radius = 0;
    color = "black";

    constructor() {
        this.moveDirection = new PlayerMoveDirection();
        this.mouse = {};
    }
}

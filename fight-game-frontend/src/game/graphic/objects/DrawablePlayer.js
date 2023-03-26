import Drawable from "./Drawable";

class DrawablePlayer extends Drawable {

    constructor(player) {
        super(player);
        this.player = player;
    }

    draw(canvasContext) {
        let player = this.player
        canvasContext.beginPath();
        canvasContext.fillStyle = player.color;
        canvasContext.arc(player.x, player.y, player.radius, 0, 2 * Math.PI);
        canvasContext.fill();
    }

    getId() {
        return this.player.id;
    }
}
export default DrawablePlayer;

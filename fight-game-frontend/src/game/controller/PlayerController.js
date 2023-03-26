import { Message, PLAYER_STATE, CREATE_FIGHT, JOIN_FIGHT, FIGHT_ID } from '../../message/Message'

export default class PlayerController {

    constructor(config) {
        this.id = Math.random();
        this.configId = config.id;
        this.player = null;
        this.config = config;
        this.messageRouter = null;
        this.stateChanged = false;

        this.stepPixels = config.action.stepPixels;
        this.canvasWidth = config.canvas.width;
        this.canvasHeight = config.canvas.height;

        console.log(`PlayerController: created - ${this.id} timerId ${this.timerId}`);
    }

    action() {
        this.sendMoveState();
    }

    attach(element) {
        this.element = element;

        this.keyDownEventListerer = (event) => {
            this.changeDirection(event, true);
        }
        this.element.addEventListener("keydown", this.keyDownEventListerer);

        this.keyUpEventListener = (event) => {
            this.changeDirection(event, false);
        }
        this.element.addEventListener("keyup", this.keyUpEventListener);

        this.timerId = setInterval(() => this.action(), this.config.action.actionIntervalMs);
    }

    changeDirection(event, move) {
        if (!this.player) {
            console.log("Skip:", "player wasn't set")
            return;
        }
        let key = event.key;
        // console.log("Change", key);
        switch (key) {
            case 'ArrowUp':
                this.player.moveDirection.up = move;
                this.stateChanged = true;
                break;
            case 'ArrowDown':
                this.player.moveDirection.down = move;
                this.stateChanged = true;
                break;
            case 'ArrowLeft':
                this.player.moveDirection.left = move;
                this.stateChanged = true;
                break;
            case 'ArrowRight':
                this.player.moveDirection.right = move;
                this.stateChanged = true;
                break;
            default:
                break;
        }
    }

    sendMoveState() {
        if (this.stateChanged) {
            let message = Message.create();
            message.msgType = PLAYER_STATE;
            let move = this.player.moveDirection

            let event = {};
            message.arrowEvent = event;
            message.arrowEvent.up = move.up;
            message.arrowEvent.down = move.down;
            message.arrowEvent.left = move.left;
            message.arrowEvent.right = move.right;

            let jsonMsg = JSON.stringify(message);
            this.messageRouter.send(jsonMsg);
            this.stateChanged = false;
        }
    }

    createFight() {
        let message = Message.create();
        message.msgType = CREATE_FIGHT;
        let jsonMsg = JSON.stringify(message);
        this.messageRouter.send(jsonMsg);
    }

    joinFight(fightId, onJoinedCallback) {
        console.log("Join", fightId ? fightId : "random", "fight");
        this.onJoinedCallback = onJoinedCallback;
        let message = Message.create();
        message.fightId = fightId;

        message.msgType = JOIN_FIGHT;
        let jsonMsg = JSON.stringify(message);
        this.messageRouter.send(jsonMsg);
    }

    detach() {
        console.log(`Detach - ${this.id} timerId = ${this.timerId}`)
        clearInterval(this.timerId);
        this.element.removeEventListener("keydown", this.keyDownEventListerer);
        this.element.removeEventListener("keyup", this.keyUpEventListener);
    }
}

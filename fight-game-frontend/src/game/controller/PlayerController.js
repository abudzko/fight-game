import { Message, PLAYER_EVENT, CREATE_FIGHT, JOIN_FIGHT } from '../../message/Message'

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
        this.sendEvents();
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

        this.mouseClickEventListener = (event) => {
            this.setOnClickMousePosition(event);
        }
        this.element.addEventListener("click", this.mouseClickEventListener);

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

    setOnClickMousePosition(event) {
        if (!this.player) {
            console.log("Skip:", "player wasn't set")
            return;
        }
        this.player.mouse.x = event.clientX;
        this.player.mouse.y = event.clientY;
        this.mouseClicked = true;
    }

    sendEvents() {
        if (this.stateChanged || this.mouseClicked) {
            let message = Message.create();
            message.msgType = PLAYER_EVENT;

            if (this.stateChanged) {
                message.arrowEvent = this.player.moveDirection;
                this.stateChanged = false;
            }

            if (this.mouseClicked) {
                message.mouseEvent = this.player.mouse;
                this.mouseClicked = false;
            }

            let jsonMsg = JSON.stringify(message);
            this.messageRouter.send(jsonMsg);
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
        this.element.removeEventListener("click", this.mouseClickEventListener);
    }
}

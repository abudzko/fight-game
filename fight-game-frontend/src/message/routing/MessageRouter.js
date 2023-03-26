import WebSocketMessageRouter from "../websocket/WebSocketMessageRouter"
import MessageSender from "./MessageSender"
import MessageReceiver from "./MessageReceiver"

export default class MessageRouter {
    constructor(config) {
        this.config = config
    }

    init() {
        this.messageReceiver = new MessageReceiver(this.playerController, this.graphicController);
        this.webSocketMessageRouter = new WebSocketMessageRouter(this.messageReceiver, this.config)
        this.messageSender = new MessageSender(this.webSocketMessageRouter);
    }

    send(message) {
        this.messageSender.send(message);
    }

    receive(message) {
        this.messageReceiver.receive(message);
    }
}

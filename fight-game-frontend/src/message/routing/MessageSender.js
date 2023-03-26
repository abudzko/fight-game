export default class MessageSender {

    constructor(messageRouter) {
        this.router = messageRouter;
    }

    send(msg) {
        // console.log(msg);
        this.router.send(msg)
    }
}

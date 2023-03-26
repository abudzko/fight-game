import { Client } from '@stomp/stompjs';

export default class WebSocketMessageRouter {

    constructor(messageReceiver, config) {
        this.id = Math.random();
        console.log(`Created ${this.id}`);
        this.messageReceiver = messageReceiver;
        this.webSocketUrl = `${config.webSocket.url}${config.webSocket.port}${config.webSocket.path}`;
        this.destinationPath = `${config.webSocket.destinationPath}`;
        this.webSocketSubscribePath = `${config.webSocket.userSubscribePath}`;
        this.isReady = false;

        this.client = new Client({
            brokerURL: this.webSocketUrl,
            // connectHeaders: {
            //     login: 'user',
            //     passcode: 'password',
            // },
            // debug: function (str) {
            //     console.log(str);
            // },
            reconnectDelay: 4000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                this.client.subscribe(
                    this.webSocketSubscribePath,
                    message => {
                        this.messageReceiver.receive(message.body);
                    }
                );
                console.log(`WebSocket connection was esteblished ${this.id}`);
                this.isReady = true;
            },
            onStompError: function (frame) {
                this.isReady = false;
                console.log(`Broker reported error: ${frame.headers['message']}. Details: ${frame.body}`);
            },
        });
        this.client.activate();
    }

    /**
     * @param msg string to send
     */
    send(msg) {
        let isReady = this.isReady;
        if (isReady) {
            this.client.publish({ destination: this.destinationPath, body: msg });
            // console.log(`Message was sent: ${msg}`);
        } else {
            console.log(`Can't send message ${msg}. Socket not ready: ${isReady}. ${this.id}`);
        }
    }

    destroy() {
        console.log(`Destroyed ${this.id}`);
        this.client.deactivate()
    }
}

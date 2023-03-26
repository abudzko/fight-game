class Config {
    canvas = {
        width: window.innerWidth * 0.99,
        height: window.innerHeight * 0.99,

    };
    animation = {
        animationIntervalMs: 10
    };
    action = {
        actionIntervalMs: 20,
        stepPixels: 10
    };
    webSocket = {
        url: "ws://192.168.1.104",
        port: ":8080",
        path: "/webapp",
        destinationPath: "/game/message",
        userSubscribePath: "/user/topic/response"
    }
};

export default Config;

package budzko.backend.game.fight.app.web.controller.config.ui;

import lombok.Data;

@Data
public class WebSocket {
    private String url;
    private String port;
    private String path;
    private String destinationPath;
    private String userSubscribePath;
}

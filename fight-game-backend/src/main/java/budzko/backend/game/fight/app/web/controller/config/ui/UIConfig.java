package budzko.backend.game.fight.app.web.controller.config.ui;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ui")
public class UIConfig {
    private Canvas canvas;
    private Animation animation;
    private Action action;
    private WebSocket webSocket;
}

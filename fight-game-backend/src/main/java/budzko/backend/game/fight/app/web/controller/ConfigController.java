package budzko.backend.game.fight.app.web.controller;

import budzko.backend.game.fight.app.web.controller.config.ui.UIConfig;
import budzko.backend.game.fight.app.web.controller.view.UIConfigView;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @Autowired
    private UIConfig uiConfig;
    private UIConfigView uiConfigView;

    @PostConstruct
    void init() {
        uiConfigView = UIConfigView.builder()
                .canvas(uiConfig.getCanvas())
                .action(uiConfig.getAction())
                .animation(uiConfig.getAnimation())
                .webSocket(uiConfig.getWebSocket())
                .build();
    }

    @CrossOrigin(originPatterns = "*")
    @RequestMapping("/config")
    public UIConfigView uiConfig() {
        return uiConfigView;
    }
}

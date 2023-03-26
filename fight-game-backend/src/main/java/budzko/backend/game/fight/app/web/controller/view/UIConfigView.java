package budzko.backend.game.fight.app.web.controller.view;

import budzko.backend.game.fight.app.web.controller.config.ui.Action;
import budzko.backend.game.fight.app.web.controller.config.ui.Animation;
import budzko.backend.game.fight.app.web.controller.config.ui.Canvas;
import budzko.backend.game.fight.app.web.controller.config.ui.WebSocket;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UIConfigView {
    private Canvas canvas;
    private Animation animation;
    private Action action;
    private WebSocket webSocket;
}

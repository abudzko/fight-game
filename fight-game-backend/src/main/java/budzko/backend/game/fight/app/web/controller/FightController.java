package budzko.backend.game.fight.app.web.controller;

import budzko.backend.game.fight.app.game.manager.FightManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FightController {

    private final FightManager fightManager;

    @CrossOrigin(originPatterns = "*")
    @RequestMapping("/fights")
    public List<String> fights() {
        return fightManager.getFightIds();
    }
}

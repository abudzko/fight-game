import { useState } from "react";
import GameComponent from "./game/GameComponent"


function App(props) {
  let config = props.config;
  const [showMenu, setShowMenu] = useState(true);
  const [startGame, setStartGame] = useState(false);
  const createFightCallback = () => {
    props.playerController.createFight();
    setShowMenu(false);
    setStartGame(true);
  }
  const onJoinedCallback = () => {
    setShowMenu(false);
    setStartGame(true);
  }
  const joinFightCallback = () => {
    let fightId = prompt("Please enter fight id", "");
    if (fightId) {
      props.playerController.joinFight(fightId, onJoinedCallback);
    }
  }
  const joinRandomFightCallback = () => {
    props.playerController.joinFight(null, onJoinedCallback);
  }
  let menu;
  if (showMenu) {
    menu =
      <div className="gameMenu">
        {/* <div className="menuList"> */}
          <button className="menuButton" onClick={createFightCallback}>Create Fight</button>
          {/* <div> */}
          <button className="menuButton" onClick={joinFightCallback}>Join Fight</button>
          <button className="menuButton" onClick={joinRandomFightCallback}>Join Random Fight</button>
          {/* </div> */}
        {/* </div> */}
      </div>
  }
  let game;
  if (startGame) {
    game = <GameComponent
      config={config}
      playerController={props.playerController}
      graphicController={props.graphicController}
    ></GameComponent>;
  }
  return (
    <div>
      {showMenu && menu}
      {startGame && game}
    </div>
  );
}

export default App;

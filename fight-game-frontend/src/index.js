import React from 'react';
import ReactDOM from 'react-dom/client';
import './css/index.css';
import App from './App';
import MessageRouter from './message/routing/MessageRouter';
import GraphicController from './game/graphic/GraphicController';
import PlayerController from './game/controller/PlayerController';
import Config from './game/Config';


const root = ReactDOM.createRoot(document.getElementById('root'));
const host = "http://192.168.1.104:8080";
let config = requestConfig();

let graphicController = new GraphicController();
let playerController = new PlayerController(config);
let messageRouter = new MessageRouter(config);
messageRouter.graphicController = graphicController;
messageRouter.playerController = playerController;
playerController.messageRouter = messageRouter;
messageRouter.init();


root.render(
  <React.StrictMode>
    <App
      playerController={playerController}
      graphicController={graphicController}
      config={config}
    />
  </React.StrictMode>
);


function requestConfig() {
  try {
    var request = new XMLHttpRequest();
    request.open('GET', `${host}/config`, false);
    request.send();
    let responseJson = request.responseText;
    return JSON.parse(responseJson);
  } catch (e) {
    console.log(
      `Can't connect to server: ${host}\n%cUse 'fight-game-backend' project to start server`,
      "color : blue; font-size: 16px"
    );
    return new Config();
  }
}

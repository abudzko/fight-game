- User establishes websocket connection
- From websocket connection we have websocket session id. Also if http session is used - http session id - which is more durable
- TODO - implement authentication and then we can authenticate user,
  TODO so each user connection can be bind with user name(remove player personal id)
- Now we generate random user personal id  - UserWebSocketHandshakeHandler on each new websocket session
- User sends create fight request - we creates new Fight and Player and return public player id back to user
- Also we can return player personal id ONLY to its user
- When user loses connection we should give chance to reconnect
- To reconnect user can send fight id and its player personal id
- If player lost connection and didn't reconnect remove him after N seconds


- On frontend side listen kye and mouse event, update state and only once in actionIntervalMs ms send player state to backend
  to control message rate
- On backend side handle each event(key pressed, mouse clicked) and update player state,
  but only once in actionIntervalMs ms recalculate player position and etc to avoid influence of messages rate on behavior

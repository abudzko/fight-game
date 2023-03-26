export class Message {

    msgType = "";
    arrowEvent;

    static create() {
        return new Message();
    }
}

export const CREATE_FIGHT = "CREATE_FIGHT";
export const FIGHT_CREATED = "FIGHT_CREATED";
export const JOIN_FIGHT = "JOIN_FIGHT";
export const JOINED_TO_FIGHT = "JOINED_TO_FIGHT";
export const PLAYER_STATE = "PLAYER_STATE";
export const FIGHT_UPDATE = "FIGHT_UPDATE";


export const PLAYER_ID = "player_id";
export const FIGHT_ID = "fight_id";

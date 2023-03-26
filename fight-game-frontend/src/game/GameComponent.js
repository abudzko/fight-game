import Canvas from "./graphic/Canvas"

export default function GameComponent(props) {
    const config = props.config;
    return (
        <Canvas
            config={config}
            graphicController={props.graphicController}
            playerController={props.playerController}
        />
    )
}

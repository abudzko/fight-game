import React, { useRef, useEffect } from "react";
import CanvasElementRenderer from "./CanvasElementRenderer";

export default function Canvas(props) {
    const config = props.config;
    const canvasRef = useRef(null)

    useEffect(() => {
        const graphicController = props.graphicController;
        const playerController = props.playerController;

        const canvasElementRenderer = new CanvasElementRenderer(
            props.config,
            graphicController
        );

        const canvas = canvasRef.current
        const context = canvas.getContext('2d')
        canvasElementRenderer.startAnimationLoop(context);
        playerController.attach(canvas)

        return function cleanup() {
            canvasElementRenderer.destroy();
            playerController.detach();
        }

    }, [props]);

    return (
        <canvas
            className="canvasClass"
            tabIndex={Math.random()}
            ref={canvasRef}
            width={config.canvas.width}
            height={config.canvas.height}>
        </canvas>
    )
}

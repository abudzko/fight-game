class CanvasElementRenderer {

    constructor(config, graphicController) {
        this.id = Math.random();
        console.log(`Created - ${this.id}`)

        this.lastTick = 0;
        this.elapsed = 0;
        this.config = config;
        this.graphicController = graphicController;
        this.animationIntervalMs = config.animation.animationIntervalMs
    }

    startAnimationLoop(canvasContext) {
        this.canvasContext = canvasContext;
        this.requestAnimationFrameId = window.requestAnimationFrame((tick) => this.animationLoop(tick));
    }

    animationLoop(tick) {
        // console.log(this.id);
        let lastTick = this.lastTick;
        let elapsed = this.elapsed;
        let animationIntervalMs = this.animationIntervalMs;
        let diff = tick - lastTick;
        elapsed += diff;
        if (elapsed > animationIntervalMs) {
            this.renderElements();
            elapsed -= animationIntervalMs;
        }
        this.lastTick = tick;
        window.requestAnimationFrame((tick) => this.animationLoop(tick));
    }

    renderElements() {
        this.canvasContext.clearRect(0, 0, this.config.canvas.width, this.config.canvas.height);
        this.graphicController.drawableObjects.forEach(drawable => {
            drawable.draw(this.canvasContext)
        });
    }

    destroy() {
        console.log(`Destroyed - ${this.id}`)
        window.cancelAnimationFrame(this.requestAnimationFrameId);
    }
}

export default CanvasElementRenderer;

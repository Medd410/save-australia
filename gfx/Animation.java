package gfx;

import java.awt.image.BufferedImage;

public class Animation {
    private int speed, index;
    private long lastTime, timer;
    private BufferedImage[] frames;

    public Animation(int speed, BufferedImage[] frames) {
        this.speed = speed;
        this.frames = frames;
        this.index = 0;
        this.timer = 0;
        this.lastTime = System.currentTimeMillis();
    }

    public void update() {
        this.timer += System.currentTimeMillis() - lastTime;
        this.lastTime = System.currentTimeMillis();
        if (this.timer >= this.speed) {
            this.nextFrame();
            this.timer = 0;
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[index];
    }

    public void nextFrame() {
        this.index++;
        if (this.index >= this.frames.length)
            this.index = 0;
    }

    public void reset() {
        this.index = 0;
    }
} 
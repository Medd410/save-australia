package entities.beings.trees.cherries;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import entities.beings.trees.Tree;
import games.Handler;
import gfx.Assets;

public class Cherry extends Tree {

    public Cherry(Handler handler, int gridX, int gridY) {
        super(handler, gridX, gridY);
        burningRate = 1;
    }

    public Cherry(Handler handler, int gridX, int gridY, int health, boolean onFire, boolean burned, double burningTime) {
        super(handler, gridX, gridY);
        this.setHealth(health);
        this.onFire = onFire;
        this.burned = burned;
        this.burningTime = burningTime;
        burningRate = 2.0;
    }

    @Override
    public void update() {
        if (burned)
            return;
        if (onFire) {
            onFireAnim.update();
            double d = (double)1/(double)60;
            burningTime += d;
            updateTime += d;
            if (updateTime > 1.0) {
                setHealth((int)(health-burningTime/2*burningRate));
                updateTime--;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(getSprite(), getX(), getY(), null);
        if (onFire) {
            int fireSize = 8 + (int)(burningTime*burningRate); if (fireSize > 24) fireSize = 24; fireSize -= fireSize%2;
            g.drawImage(onFireAnim.getCurrentFrame(), getX()+(16-fireSize)/2, getY()+(16-fireSize)/2, fireSize, fireSize, null);
        }
    }

    public BufferedImage getSprite() {
        if (!burned) {
            return Assets.assets.get(Assets.Cherry);
        } else {
            return Assets.assets.get(Assets.Burned);
        }
    }

}
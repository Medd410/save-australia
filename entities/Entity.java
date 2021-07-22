package entities;

import java.awt.Graphics;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import games.EntityManager;
import games.Handler;

public abstract class Entity {

    
    protected int gridX, gridY, health, maxhealth, point;
    protected Handler handler;
    protected Boolean hovering;

    public Entity(Handler handler, int gridX, int gridY) {
        this.hovering = false;
        this.handler = handler;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getX() {
        return gridX*16;
    }

    public int getGridY() {
        return gridY;
    }

    public int getY() {
        return gridY*16;
    }

    public abstract void update();
    
    public abstract void render(Graphics g);

    public abstract BufferedImage getSprite();

    public void onMouseMove(MouseEvent e) {
        if(EntityManager.bounds(e.getX(), e.getY(), getX(), getY(), getX()+16, getY()+16)) {
            hovering = true;
            return;
        }
        hovering = false;
    }

    public void onMouseRelease(MouseEvent e) {
        if (hovering) {
            handler.getWorld().getEntityManager().setSelectedEntity(this);
        }
    }
}
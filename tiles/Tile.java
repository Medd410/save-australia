package tiles;

import java.awt.Graphics;

import gfx.Assets;

public class Tile {

    private int id, gridX, gridY;

    public Tile(int id, int gridX, int gridY) {
        this.id = id;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getX() {
        return gridX*16;
    }

    public int getY() {
        return gridY*16;
    }

    public void update() {

    }
    
    public void render(Graphics g) {
        g.drawImage(Assets.assets.get(this.id), this.getX(), this.getY(), null);
    }
}
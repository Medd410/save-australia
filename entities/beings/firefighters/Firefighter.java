package entities.beings.firefighters;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import entities.Entity;
import entities.beings.Being;
import entities.beings.trees.Tree;
import java.awt.image.BufferedImage;
import games.Handler;
import gfx.Animation;
import gfx.Assets;
import tiles.Tile;
import world.World;

public class Firefighter extends Being {

    public enum Move {
        Up,
        Down,
        Left,
        Right
    }

    private Handler handler;
    private Animation upAnim, downAnim, leftAnim, rightAnim, waterAnim;
    private Move lastMove;
    private int water, maxWater;
    private int moveDelay, sprayDelay = 0, reloadDelay = 0, burnDelay = 0;
    private boolean inAction = false;

    public Firefighter(Handler handler, int gridX, int gridY) {
        super(handler, gridX, gridY);
        this.handler = handler;
        init();
        setHealth(1000);
        setWater(100);
    }

    public Firefighter(Handler handler, int gridX, int gridY, int h, int w) {
        super(handler, gridX, gridY);
        this.handler = handler;
        init();
        setHealth(h);
        setWater(w);
    }

    private void init() {
        lastMove = Move.Down;
        maxHealth = 1000;
        maxWater = 100;
        upAnim = new Animation(60, Assets.playerUp);
        downAnim = new Animation(60, Assets.playerDown);
        leftAnim = new Animation(60, Assets.playerLeft);
        rightAnim = new Animation(60, Assets.playerRight);
        waterAnim = new Animation(120, Assets.water);
    }

    @Override
    public void update() {
        waterAnim.update();
        if (handler.getWorld().getEntityManager().getSelectedEntity() == this) {
            upAnim.update();
            downAnim.update();
            leftAnim.update();
            rightAnim.update();
            if ( moveDelay == 0 ) {
                upAnim.reset();
                downAnim.reset();
                leftAnim.reset();
                rightAnim.reset();
                if (handler.getGame().getKeyManager().space) {
                    if (!inAction && water > 0) {
                        waterAnim.reset();
                        inAction = true;
                    }
                } else if (handler.getGame().getKeyManager().up) {
                    move(Move.Up);
                } else if (handler.getGame().getKeyManager().down) {
                    move(Move.Down);
                } else if (handler.getGame().getKeyManager().left) {
                    move(Move.Left);
                } else if (handler.getGame().getKeyManager().right) {
                    move(Move.Right);
                }
            }
        }
        int aF = aroundFire();
        if (aF > 0) {
            if (burnDelay > 0) {
                burnDelay--;
            } else if (burnDelay == 0) {
                setHealth(getHealth()-aF);
                burnDelay = 60;
            }
        } 
        if (reloadDelay == 0 && aroundWater()) {
            setWater(getWater()+10);
            reloadDelay=60;
        }
        if (inAction && sprayDelay == 0) {
            setWater(getWater()-5);
            updateWater();
        }
        if (burnDelay > 0)
            burnDelay--;
        if (reloadDelay > 0)
            reloadDelay--;
        if (sprayDelay > 0)
            sprayDelay--;
        if (moveDelay > 0)
            moveDelay--;
    }

    private void move(Move m) {
        lastMove = m;
        inAction = false;
        Entity[][] e = handler.getWorld().getEntities();
        Tile[][] t = handler.getWorld().getTiles();
        int x = gridX, y = gridY;
        switch (m) {
            case Up:
                if (y - 1 < 0 || e[x][y-1] != null) return;
                if (t[x][y-1].getId() == Assets.Water) return;
                gridY-=1; e[x][y] = null; e[x][y-1] = this;
                break;
            case Down:
                if (y + 1 > World.GRID_SIZE-1 || e[x][y+1] != null) return;
                if (t[x][y+1].getId() == Assets.Water) return;
                gridY+=1; e[x][y] = null; e[x][y+1] = this;
                break;
            case Left:
                if (x - 1 < 0 || e[x-1][y] != null) return;
                if (t[x-1][y].getId() == Assets.Water) return;
                gridX-=1; e[x][y] = null; e[x-1][y] = this;
                break;
            case Right:
                if (x + 1 > World.GRID_SIZE-1 || e[x+1][y] != null) return;
                if (t[x+1][y].getId() == Assets.Water) return;
                gridX+=1; e[x][y] = null; e[x+1][y] = this;
                break;
        }
        e[x][y] = null;
        moveDelay = 16;
    }

    @Override
    public void render(Graphics g) {
        if (inAction) renderWater(g);
        if (lastMove == Move.Up) {
            g.drawImage(upAnim.getCurrentFrame(), getX(), getY()-8+moveDelay, null);
        } else if (lastMove == Move.Down) {
            g.drawImage(downAnim.getCurrentFrame(), getX(), getY()-8-moveDelay, null);
        } else if (lastMove == Move.Left) {
            g.drawImage(leftAnim.getCurrentFrame(), getX()+moveDelay, getY()-8, null);
        } else if (lastMove == Move.Right) {
            g.drawImage(rightAnim.getCurrentFrame(), getX()-moveDelay, getY()-8, null);
        }
        if (aroundWater() && moveDelay == 0) g.drawImage(waterAnim.getCurrentFrame(), getX(), getY(), null);
    }

    public Map<Integer, Integer[]> getWaterCells(Move move) {
        Integer x1 = 0, y1 = 0, x2 = 0, y2 = 0, count = 0;
        Map<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();
        switch (move) {
            case Up:
                x1 = -1; y1 = -4; x2 = 1; y2 = -1;
                break;
            case Down:
                x1 = -1; y1 = 1; x2 = 1; y2 = 4;
                break;
            case Left:
                x1 = -4; y1 = -1; x2 = -1; y2 = 1;
                break;
            case Right:
                x1 = 1; y1 = -1; x2 = 4; y2 = 1;
                break;
        }
        for (Integer i = x1; i <= x2; i+=1) { // X
            for (Integer j = y1; j <= y2; j+=1) { // Y
                if (!(gridX+i < 0 || gridX+i >= World.GRID_SIZE || gridY+j < 0 || gridY+j >= World.GRID_SIZE)) {
                    Integer[] array = new Integer[2];
                    array[0] = gridX+i; array[1] = gridY+j;
                    map.put(count, array);
                    count++;
                }
            }
        }
        return map;
    }

    public void renderWater(Graphics g) {
        Map<Integer, Integer[]> map = getWaterCells(lastMove);
        for (Integer[] cell : map.values()) {
            g.drawImage(waterAnim.getCurrentFrame(), cell[0]*16, cell[1]*16, null);
        }
    }

    public void updateWater() {
        Map<Integer, Integer[]> map = getWaterCells(lastMove);
        Entity[][] entities = handler.getWorld().getEntities();
        for (Integer[] cell : map.values()) {
            if (entities[cell[0]][cell[1]] == null) continue;
            if (entities[cell[0]][cell[1]].getClass().getSimpleName().equals("Firefighter")) continue;
            Tree tree = (Tree)entities[cell[0]][cell[1]];
            tree.setBurningTime(tree.getBurningTime() - tree.getBurningRate() * 3);
        }
        sprayDelay = 60;
    }

    public boolean aroundWater() {
        Tile[][] t = handler.getWorld().getTiles();
        if (!(gridX+1 >= World.GRID_SIZE) && t[gridX+1][gridY].getId() == Assets.Water) {
            return true;
        }
        if (!(gridX-1 < 0) && t[gridX-1][gridY].getId() == Assets.Water) {
            return true;
        }
        if (!(gridY-1 < 0) && t[gridX][gridY-1].getId() == Assets.Water) {
            return true;
        }
        if (!(gridY+1 >= World.GRID_SIZE) && t[gridX][gridY+1].getId() == Assets.Water) {
            return true;
        }
        return false;
    }

    public int aroundFire() {
        Entity[][] entities = handler.getWorld().getEntities();
        Tree tree;
        int life = 0;
        if (!(gridX+1 >= World.GRID_SIZE) && entities[gridX+1][gridY] != null && entities[gridX+1][gridY].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX+1][gridY];
            if (tree.isOnFire()) {
                life+=5;
            }
        }
        if (!(gridX-1 < 0) && entities[gridX-1][gridY] != null && entities[gridX-1][gridY].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX-1][gridY];
            if (tree.isOnFire()) {
                life+=5;
            }
        }
        if (!(gridY-1 < 0) && entities[gridX][gridY-1] != null && entities[gridX][gridY-1].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX][gridY-1];
            if (tree.isOnFire()) {
                life+=5;
            }
        }
        if (!(gridY+1 >= World.GRID_SIZE) && entities[gridX][gridY+1] != null && entities[gridX][gridY+1].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX][gridY+1];
            if (tree.isOnFire()) {
                life+=5;
            }
        }
        return life;
    }

    @Override
    public void setHealth(int health) {
        if (health > maxHealth) {
            this.health = maxHealth;
        } else if (health <= 0) {
            this.health = 0;
        } else {
            this.health = health;
        }
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        if (water > maxWater) {
            this.water = maxWater;
        } else if (water < 0) {
            this.water = 0;
            this.inAction = false;
        } else {
            this.water = water;
        }
    }

    public int getMaxWater() {
        return maxWater;
    }

    public BufferedImage getSprite() {
        return Assets.playerDown[0];
    }
}
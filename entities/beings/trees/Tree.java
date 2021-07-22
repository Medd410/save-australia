package entities.beings.trees;

import entities.Entity;
import entities.beings.Being;
import games.Handler;
import gfx.Animation;
import gfx.Assets;
import world.World;

public abstract class Tree extends Being {

    protected Animation onFireAnim;
    protected boolean onFire;
    protected boolean burned;
    protected double burningTime;
    protected double updateTime;
    protected double burningRate;

    public Tree(Handler handler, int gridX, int gridY) {
        super(handler, gridX, gridY);
        maxHealth = this.health = 500;
        point = 1;
        onFireAnim = new Animation(100, Assets.fire);
        onFire = false;
        burned = false;
        burningTime = updateTime = 0.0;
    }

    public int spreadFire() {
        Entity[][] entities = handler.getWorld().getEntities();
        Tree tree;
        double perc = 0;
        if (!(gridX+1 >= World.GRID_SIZE) && entities[gridX+1][gridY] != null && entities[gridX+1][gridY].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX+1][gridY];
            if (tree.isOnFire()) {
                perc+=5;
            }
        }
        if (!(gridX-1 < 0) && entities[gridX-1][gridY] != null && entities[gridX-1][gridY].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX-1][gridY];
            if (tree.isOnFire()) {
                perc+=5;
            }
        }
        if (!(gridY-1 < 0) && entities[gridX][gridY-1] != null && entities[gridX][gridY-1].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX][gridY-1];
            if (tree.isOnFire()) {
                perc+=5;
            }
        }
        if (!(gridY+1 >= World.GRID_SIZE) && entities[gridX][gridY+1] != null && entities[gridX][gridY+1].getClass().getSuperclass().getSimpleName().equals("Tree")) {
            tree = (Tree)entities[gridX][gridY+1];
            if (tree.isOnFire()) {
                perc+=5;
            }
        }
        perc *= burningRate;
        return (int)perc;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }

    public boolean isBurned() {
        return burned;
    }

    public double getBurningTime() {
        return burningTime;
    }

    public void setBurningTime(double burningTime) {
        if (burningTime <= 0.0) {
            onFire = false;
            burningTime = 0.0;
            updateTime = 0.0;
        }
        this.burningTime = burningTime;
    }

    public double getBurningRate() {
        return burningRate;
    }
 
    @Override
    public void setHealth(int health) {
        if (health > maxHealth) {
            this.health = maxHealth;
        } else if (health <= 0) {
            this.health = 0;
            handler.getGame().getGameState().setPoints(handler.getGame().getGameState().getPoints()+1);
            onFire = false;
            burned = true;
            burningTime = 0.0;
        } else {
            this.health = health;
        }
    }
}
package entities.beings;

import entities.Entity;
import games.Handler;

public abstract class Being extends Entity {
    
    protected int health;
    protected int maxHealth;
    protected int point;

    public Being(Handler handler, int gridX, int gridY) {
        super(handler, gridX, gridY);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
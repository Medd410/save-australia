package games;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.MouseEvent;

import java.awt.Graphics;

import entities.Entity;
import entities.beings.firefighters.Firefighter;
import entities.beings.trees.Tree;

public class EntityManager {
    
    private Handler handler;
    private ArrayList<Tree> trees;
    private ArrayList<Firefighter> firefighters;
    private Entity selectedEntity;
    long timer, timeFire, timeSpread;

    public EntityManager(Handler handler) {
        this.handler = handler;
        selectedEntity = null;
        trees = new ArrayList<Tree>();
        firefighters = new ArrayList<Firefighter>();
        timer = System.currentTimeMillis();
    }

    public void update() {
        timeFire += System.currentTimeMillis() - timer;
        timeSpread += System.currentTimeMillis() - timer;
        timer = System.currentTimeMillis();
        
        if (treesOnFire())
            timeFire = 0;

        if (timeFire >= 5000)
            igniteRandomFire();

        for (Tree t : trees) {
            if (timeSpread >= 1000) {
                if (Game.rand.nextInt(100) < t.spreadFire()) {
                    if (!t.isBurned()) {
                        t.setOnFire(true);
                    }
                }
            }
            t.update();
        }

        if (timeSpread >= 1000)
            timeSpread = 0;
        
        Iterator<Firefighter> itr = firefighters.iterator();
        while(itr.hasNext()) {
            Firefighter f = itr.next();
            f.update();
            if (f.getHealth() == 0) {
                handler.getWorld().getEntities()[f.getGridX()][f.getGridY()] = null;
                handler.getGame().getGameState().setPoints(handler.getGame().getGameState().getPoints()+10000);
                itr.remove();
            }
        }
    }

    public void render(Graphics g) {
        for (Tree t : trees) {
            t.render(g);
        }
        for (Firefighter f : firefighters) {
            f.render(g);
        } 
    }

    public void onMouseMove(MouseEvent e) {
        for (Tree t : trees) {
            t.onMouseMove(e);
        }
        for (Firefighter f : firefighters) {
            f.onMouseMove(e);
        } 
    }

    public void onMouseRelease(MouseEvent e) {
        selectedEntity = null;
        for (Tree t : trees) {
            t.onMouseRelease(e);
        }
        for (Firefighter f : firefighters) {
            f.onMouseRelease(e);
        } 
    }
    
    public void setSelectedEntity(Entity e) {
        selectedEntity = e;
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public ArrayList<Firefighter> getFirefighters() {
        return firefighters;
    }

    public void addTree(Tree t) {
        trees.add(t);
    }

    public void addFirefighter(Firefighter f) {
        firefighters.add(f);
    }

    public void igniteRandomFire() {
        Tree tree = trees.get(Game.rand.nextInt(trees.size()));
        if (!tree.isBurned()) {
            tree.setOnFire(true);
        } else {
            igniteRandomFire();
        }
    }

    public Boolean treesOnFire() {
        for(Tree t : trees) {
            if (t.isOnFire())
                return true;
        }
        return false;
    }

    public static boolean bounds(int x, int y, int x1, int y1, int x2, int y2) {
        if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
            return true;
        }
        return false;
    }
}
package states;

import java.awt.Color;
import java.awt.Graphics;

import entities.Entity;
import entities.beings.Being;
import entities.beings.trees.Tree;
import entities.beings.firefighters.Firefighter;
import games.EntityManager;
import games.Handler;
import gfx.Assets;
import tiles.Tile;
import world.World;

public class GameState extends State {

    boolean spawnFirefighter;
    int maxFirefighter, countFirefighter, points;
    long timer, timeBombardier;

    public GameState(Handler handler) {
        super(handler);
        spawnFirefighter = false;
        maxFirefighter = 5;
        points = 0;
        timer = System.currentTimeMillis();
    }

    @Override
    public void update() {
        EntityManager entityManager = handler.getWorld().getEntityManager();
        entityManager.update();
        countFirefighter = entityManager.getFirefighters().size();
        timeBombardier += System.currentTimeMillis() - timer;
        timer = System.currentTimeMillis();

        if (handler.getKeyManager()._1) {
            handler.getWorld().getEntityManager().setSelectedEntity(null);
            spawnFirefighter = true;
        } else if (handler.getKeyManager()._2 && timeBombardier >= 30000) {
            System.out.println("BOMBARDER");
            for (Tree t : entityManager.getTrees()) {
                t.setBurningTime(t.getBurningTime()-10.0);
            }
            timeBombardier = 0;
        }

        if (handler.getMouseManager().isLeftPressed()) {
            int mouseX = handler.getMouseManager().getMouseX();
            int mouseY = handler.getMouseManager().getMouseY();
            Tile[][] t = handler.getWorld().getTiles();
            Entity[][] e = handler.getWorld().getEntities();
            if (spawnFirefighter && mouseX <= 512 && mouseY <= 512) {
                int x = mouseX/16, y = mouseY/16;
                if (countFirefighter < maxFirefighter) {
                    if (t[x][y].getId() != Assets.Water) {
                        if (e[x][y] == null) {
                            Firefighter f = new Firefighter(handler, x, y);
                            entityManager.addFirefighter(f);
                            e[x][y] = f;
                        }
                    }
                }
                spawnFirefighter = false;
            } else if (EntityManager.bounds(mouseX, mouseY, 336, 528, 464, 560)) {
                handler.getWorld().saveWorld(handler.getWorld().getCurrentWorld());
            }
        }

        if (handler.getMouseManager().isRightPressed()) {
            spawnFirefighter = false;
        }
    }

    @Override
    public void render(Graphics g) {
        //RENDER GAME
        Tile[][] tiles = handler.getWorld().getTiles();
        for (int y = 0; y < World.GRID_SIZE; y++) {
            for (int x = 0; x < World.GRID_SIZE; x++) {
                if (tiles[x][y] != null)
                    tiles[x][y].render(g);
            }
        }

        EntityManager entities = handler.getWorld().getEntityManager();
        entities.render(g);
        
        //RENDER UI
        g.drawImage(Assets.userPanel, 0, 512, null);
        g.setFont(handler.getGame().getFont());
        
        Entity entity = handler.getWorld().getEntityManager().getSelectedEntity();
        if (entity != null) {
            Being being = (Being)entity;
            g.drawImage(Assets.assets.get(Assets.Hearth), 64, 536, null);
            g.setColor(Color.WHITE);
            g.fillRect(80, 536, 64, 16);
            g.setColor(Color.RED);
            g.fillRect(81, 537, being.getHealth()*62/being.getMaxHealth(), 14);
            if (entity.getClass().getSimpleName().equals("Firefighter")) {
                Firefighter firefighter = (Firefighter)being;
                g.drawImage(entity.getSprite(), 32, 532, null);
                g.drawImage(Assets.water[0], 144, 536, null);
                g.setColor(Color.WHITE);
                g.fillRect(160, 536, 64, 16);
                g.setColor(Color.BLUE);
                g.fillRect(161, 537, firefighter.getWater()*62/firefighter.getMaxWater(), 14);
            } else {
                g.drawImage(entity.getSprite(), 32, 536, null);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString("Points : " + Integer.toString(points), 80, 534);
        g.drawImage(Assets.playerDown[0], 254, 540, null);
        g.drawString("1", 258, 536);
        g.drawImage(Assets.bombardier, 286, 540, null);
        g.drawString("2", 290, 536);
        g.drawImage(Assets.saveButton, 336, 528, null);
        g.drawString("SAVE & EXIT", 348, 550);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
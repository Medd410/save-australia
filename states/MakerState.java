package states;

import java.awt.Color;
import java.awt.Graphics;

import entities.Entity;
import entities.beings.trees.walnuts.Walnut;
import entities.beings.trees.mangos.Mango;
import entities.beings.trees.cherries.Cherry;
import games.EntityManager;
import games.Handler;
import gfx.Assets;
import tiles.Tile;
import world.World;

public class MakerState extends State {

    private int selected = 0;

    public MakerState(Handler handler) {
        super(handler);
    }

    @Override
    public void update() {
        if (handler.getKeyManager().save) {
            handler.getWorld().saveWorld();
        } else if (handler.getKeyManager()._1) {
            selected = Assets.Grass;
        } else if (handler.getKeyManager()._2) {
            selected = Assets.Dirt;
        } else if (handler.getKeyManager()._3) {
            selected = Assets.Water;
        } else if (handler.getKeyManager()._4) {
            selected = Assets.Walnut;
        } else if (handler.getKeyManager()._5) {
            selected = Assets.Cherry;
        } else if (handler.getKeyManager()._6) {
            selected = Assets.Mango;
        }
        int mouseX = handler.getMouseManager().getMouseX();
        int mouseY = handler.getMouseManager().getMouseY();
        if (EntityManager.bounds(mouseX, mouseY, 0, 0, 512, 512)) {
            if (handler.getMouseManager().isLeftPressed()) {
                switch (selected) {
                    case 0:
                    case 1:
                    case 2:
                        handler.getWorld().getTiles()[mouseX/16][mouseY/16].setId(selected);
                        break;
                    case 3:
                        handler.getWorld().getEntities()[mouseX/16][mouseY/16] = new Walnut(handler, mouseX/16, mouseY/16);
                        break;
                    case 4:
                        handler.getWorld().getEntities()[mouseX/16][mouseY/16] = new Cherry(handler, mouseX/16, mouseY/16);
                        break;
                    case 5:
                        handler.getWorld().getEntities()[mouseX/16][mouseY/16] = new Mango(handler, mouseX/16, mouseY/16);
                        break;
                }
            } else if (handler.getMouseManager().isRightPressed()) {
                handler.getWorld().getEntities()[mouseX/16][mouseY/16] = null;
            }
        } else {
            if (handler.getMouseManager().isLeftPressed()) {
                if (EntityManager.bounds(mouseX, mouseY, 336, 528, 464, 560)) {
                    handler.getWorld().saveWorld();
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        // DRAW WORLD
        Tile[][] tiles = handler.getWorld().getTiles();
        Entity[][] entities = handler.getWorld().getEntities();
        for (int y = 0; y < World.GRID_SIZE; y++) {
            for (int x = 0; x < World.GRID_SIZE; x++) {
                if (tiles[x][y] != null)
                    tiles[x][y].render(g);
                if (entities[x][y] != null)
                    entities[x][y].render(g);
            } 
        }
        g.drawImage(Assets.userPanel, 0, 512, null);
        g.setFont(handler.getGame().getFont());
        g.setColor(Color.WHITE);
        for(int i = 48, l = 1; i <= 208 ; i+= 32, l++) {
            g.drawImage(Assets.assets.get(l-1), i, 544, null);
            g.drawString(Integer.toString(l), i+4, 540);
        }
        g.drawImage(Assets.assets.get(selected), 272, 544, null);
        g.drawString("S", 276, 540);
        g.drawImage(Assets.saveButton, 336, 528, null);
        g.drawString("SAVE WORLD", 344, 550);
    }

}
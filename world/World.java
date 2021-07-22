package world;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONArray;
import org.json.JSONObject;

import entities.Entity;
import entities.beings.firefighters.Firefighter;
import entities.beings.trees.cherries.Cherry;
import entities.beings.trees.mangos.Mango;
import entities.beings.trees.walnuts.Walnut;
import games.EntityManager;
import games.Handler;
import states.State;
import tiles.Tile;

public class World {

    public static final int GRID_SIZE = 32;

    private Handler handler;
    private Tile[][] tiles = new Tile[GRID_SIZE][GRID_SIZE];
    private Entity[][] entities = new Entity[GRID_SIZE][GRID_SIZE];
    private EntityManager entityManager;
    private String currentWorld;
    

    public World(Handler handler) {
        this.handler = handler;
        entityManager = new EntityManager(handler);
    }

    public void loadEmptyWorld() {
        for (int i = 0; i < GRID_SIZE; i++) { // LIGNE Y
            for (int j = 0; j < GRID_SIZE; j++) { // COLONNE X
                this.tiles[i][j] = new Tile(0, i, j);
                this.entities[i][j] = null;
            }
        }
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public Entity[][] getEntities() {
        return this.entities;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public String getCurrentWorld() {
        return currentWorld;
    }

    public void saveWorld() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose world to save");  
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            saveWorld(fileToSave.getAbsolutePath());
        }
    }

    public void saveWorld(String path) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jWorld = new JSONObject();
            JSONArray jtArray = new JSONArray();
            for (int y = 0; y < 32; y++) {
                JSONArray jtArray2 = new JSONArray();
                for (int x = 0; x < 32; x++) {
                    jtArray2.put(this.tiles[x][y].getId());
                }
                jtArray.put(jtArray2);
            }
            JSONArray jeArray = new JSONArray();
            for (int y = 0; y < 32; y++) {
                JSONArray jeArray2 = new JSONArray();
                for (int x = 0; x < 32; x++) {
                    JSONObject e = new JSONObject();
                    if (this.entities[x][y] != null) {
                        String n = this.entities[x][y].getClass().getSimpleName();
                        switch(n) {
                            case "Walnut":  e.put("t", "W");
                                            e.put("h", ((Walnut)this.entities[x][y]).getHealth());
                                            e.put("oF", ((Walnut)this.entities[x][y]).isOnFire());
                                            e.put("iB", ((Walnut)this.entities[x][y]).isBurned());
                                            e.put("bT", ((Walnut)this.entities[x][y]).getBurningTime());
                                            break;
                            case "Mango":   e.put("t", "M");
                                            e.put("h", ((Mango)this.entities[x][y]).getHealth());
                                            e.put("oF", ((Mango)this.entities[x][y]).isOnFire());
                                            e.put("iB", ((Mango)this.entities[x][y]).isBurned());
                                            e.put("bT", ((Mango)this.entities[x][y]).getBurningTime());
                                            break;
                            case "Cherry":  e.put("t", "C");
                                            e.put("h", ((Cherry)this.entities[x][y]).getHealth());
                                            e.put("oF", ((Cherry)this.entities[x][y]).isOnFire());
                                            e.put("iB", ((Cherry)this.entities[x][y]).isBurned());
                                            e.put("bT", ((Cherry)this.entities[x][y]).getBurningTime());
                                            break;
                            case "Firefighter": e.put("t", "F");
                                                e.put("h", ((Firefighter)this.entities[x][y]).getHealth());
                                                e.put("w", ((Firefighter)this.entities[x][y]).getWater());
                                                break;
                        }
                    }
                    jeArray2.put(e);
                }
                jeArray.put(jeArray2);
            }
            jWorld.put("tiles", jtArray);
            jWorld.put("entities", jeArray);
            jsonObject.put("world", jWorld);
            Files.write(Paths.get(path), jsonObject.toString(1).getBytes());
            State.setState(handler.getGame().getMenuState());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadWorld() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".json", "json");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Choose world to load");  
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            currentWorld = fileToSave.getAbsolutePath();
            loadWorld(currentWorld);
            return true;
        }
        return false;
    }

    public void loadWorld(String path) {
        String data;
        try {
            data = new String(Files.readAllBytes(Paths.get(path)));
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jWorld = jsonObject.getJSONObject("world");
            JSONArray jtArray = jWorld.getJSONArray("tiles");
            for (int y = 0; y < jtArray.length(); y++) {
                JSONArray jtArray2 = jtArray.getJSONArray(y);
                for (int x = 0; x < jtArray2.length(); x++) {
                    int id = jtArray2.getInt(x);
                    this.tiles[x][y] = new Tile(id, x, y);
                }
            }
            JSONArray jeArray = jWorld.getJSONArray("entities");
            for (int y = 0; y < jeArray.length(); y++) {
                JSONArray jeArray2 = jeArray.getJSONArray(y);
                for (int x = 0; x < jeArray2.length(); x++) {
                    JSONObject e = jeArray2.getJSONObject(x);
                    if (e.isEmpty()) continue;
                    String t = e.getString("t");
                    switch (t) {
                        case "W":   Walnut w = new Walnut(handler, x, y, e.getInt("h"), e.getBoolean("oF"), e.getBoolean("iB"), e.getDouble("bT"));
                                    entityManager.addTree(w);
                                    entities[x][y] = w;
                                    break;
                        case "M":   Mango m = new Mango(handler, x, y, e.getInt("h"), e.getBoolean("oF"), e.getBoolean("iB"), e.getDouble("bT"));
                                    entityManager.addTree(m);
                                    entities[x][y] = m;
                                    break;
                        case "C":   Cherry c = new Cherry(handler, x, y, e.getInt("h"), e.getBoolean("oF"), e.getBoolean("iB"), e.getDouble("bT"));
                                    entityManager.addTree(c);
                                    entities[x][y] = c;
                                    break;
                        case "F":   Firefighter f = new Firefighter(handler, x, y, e.getInt("h"), e.getInt("w"));
                                    entityManager.addFirefighter(f);
                                    entities[x][y] = f;
                                    break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
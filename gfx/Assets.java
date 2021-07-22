package gfx;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Assets {
    private static final int w = 16, h = 16;
    
    public static final Integer Grass = 0;
    public static final Integer Dirt = 1;
    public static final Integer Water = 2;
    public static final Integer Walnut = 3;
    public static final Integer Cherry = 4;
    public static final Integer Mango = 5;
    public static final Integer Rock = 6;
    public static final Integer Burned = 7;
    public static final Integer Hearth = 8;

    public static Map<Integer, BufferedImage> assets = new HashMap<Integer, BufferedImage>();
    public static BufferedImage menuBackground;
    public static BufferedImage userPanel;
    public static BufferedImage saveButton;
    public static BufferedImage bombardier;
    public static BufferedImage[] fire;
    public static BufferedImage[] water;
    public static BufferedImage[] playerUp, playerDown, playerLeft, playerRight;

    public static void init() {
        SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/sprites.png"));
        assets.put(Grass, sheet.crop(0*w, 0*h, w, h));
        assets.put(Dirt, sheet.crop(1*w, 0*h, w, h));
        assets.put(Water, sheet.crop(2*w, 0*h, w, h));
        assets.put(Walnut, sheet.crop(0*w, 1*h, w, h));
        assets.put(Cherry, sheet.crop(1*w, 1*h, w, h));
        assets.put(Mango, sheet.crop(2*w, 1*h, w, h));
        assets.put(Rock, sheet.crop(0*w, 2*h, w, h));
        assets.put(Burned, sheet.crop(1*w, 2*h, w, h));
        assets.put(Hearth, sheet.crop(0*w, 5*h, w, h));
        menuBackground = ImageLoader.loadImage("/textures/menu.png");
        userPanel = ImageLoader.loadImage("/textures/ui.png");
        saveButton = ImageLoader.loadImage("/textures/save.png");
        bombardier = ImageLoader.loadImage("/textures/bombardier.png");
        fire = new BufferedImage[7];
        for (int i = 0; i < fire.length; i++) {
            fire[i] = sheet.crop(i*w, 3*w, w, h);
        }
        water = new BufferedImage[3];
        for (int i = 0; i< water.length; i++) {
            water[i] = sheet.crop(i*w, 4*w, w, h);
        }
        SpriteSheet playerSheet = new SpriteSheet(ImageLoader.loadImage("/textures/character.png"));
        playerUp = new BufferedImage[4];
        playerUp[0] = playerSheet.crop(0, 68, 16, 24);
        playerUp[1] = playerSheet.crop(16, 68, 16, 24);
        playerUp[2] = playerSheet.crop(32, 68, 16, 24);
        playerUp[3] = playerSheet.crop(48, 68, 16, 24);
        playerDown = new BufferedImage[4];
        playerDown[0] = playerSheet.crop(0, 4, 16, 24);
        playerDown[1] = playerSheet.crop(16, 4, 16, 24);
        playerDown[2] = playerSheet.crop(32, 4, 16, 24);
        playerDown[3] = playerSheet.crop(48, 4, 16, 24);
        playerLeft = new BufferedImage[4];
        playerLeft[0] = playerSheet.crop(0, 100, 16, 24);
        playerLeft[1] = playerSheet.crop(16, 100, 16, 24);
        playerLeft[2] = playerSheet.crop(32, 100, 16, 24);
        playerLeft[3] = playerSheet.crop(48, 100, 16, 24);
        playerRight = new BufferedImage[4];
        playerRight[0] = playerSheet.crop(0, 36, 16, 24);
        playerRight[1] = playerSheet.crop(16, 36, 16, 24);
        playerRight[2] = playerSheet.crop(32, 36, 16, 24);
        playerRight[3] = playerSheet.crop(48, 36, 16, 24);
    }
}
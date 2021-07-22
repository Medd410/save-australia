package states;

import java.awt.Graphics;

import games.EntityManager;
import games.Handler;
import gfx.Assets;
import world.World;

public class MenuState extends State {

    public MenuState(Handler handler) {
        super(handler);
    }

    @Override
    public void update() {
        if (handler.getMouseManager().isLeftPressed()) {
            int mouseX = handler.getMouseManager().getMouseX();
            int mouseY = handler.getMouseManager().getMouseY();
            if (EntityManager.bounds(mouseX, mouseY, 137, 208, 375, 272)) {
                handler.setWorld(new World(handler));
                if (handler.getWorld().loadWorld()) {
                    handler.getMouseManager().setEntityManager(handler.getWorld().getEntityManager());
                    State.setState(handler.getGame().getGameState());
                }
            } else if (EntityManager.bounds(mouseX, mouseY, 153, 320, 359, 384)) {
                handler.setWorld(new World(handler));
                handler.getWorld().loadEmptyWorld();
                State.setState(handler.getGame().getMakerState());
            } else if (EntityManager.bounds(mouseX, mouseY, 198, 432, 314, 496)) {
                System.exit(0);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.menuBackground, 0, 0, null);
    }

}
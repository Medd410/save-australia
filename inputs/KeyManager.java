package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private boolean[] keys;
    public boolean up, down, left, right, space, _1, _2, _3, _4, _5, _6, save;

    public KeyManager() {
        keys = new boolean[256];
    }

    public void update() {
        up = keys[KeyEvent.VK_Z];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_Q];
        right = keys[KeyEvent.VK_D];
        space = keys[KeyEvent.VK_SPACE];
        _1 = keys[KeyEvent.VK_1];
        _2 = keys[KeyEvent.VK_2];
        _3 = keys[KeyEvent.VK_3];
        _4 = keys[KeyEvent.VK_4];
        _5 = keys[KeyEvent.VK_5];
        _6 = keys[KeyEvent.VK_6];
        save = keys[KeyEvent.VK_CONTROL] && keys[KeyEvent.VK_S];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
}
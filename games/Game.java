package games;

import gfx.Assets;
import inputs.KeyManager;
import inputs.MouseManager;
import states.GameState;
import states.MakerState;
import states.MenuState;
import states.State;

import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.Random;
import java.lang.InterruptedException;
import java.awt.Font;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {    
    
    public static Random rand;

    private Thread thread;
    private boolean isRunning = false;

    private Canvas canvas;
    private BufferStrategy bS;
    private Graphics g;
    private Font font;

    private KeyManager keyManager;
    private MouseManager mouseManager;

    private Handler handler;
    private State menuState, makerState, gameState;

    public Game(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);

        keyManager = new KeyManager();
        addKeyListener(this.keyManager);
        mouseManager = new MouseManager();
        addMouseListener(this.mouseManager);
        addMouseMotionListener(this.mouseManager);
        canvas.addMouseListener(this.mouseManager);
        canvas.addMouseMotionListener(this.mouseManager);

        font = new Font("Arial", Font.BOLD, 16);

        add(canvas);
        pack();
    }

    private void init() {
        Assets.init();
        Game.rand = new Random();

        handler = new Handler(this);
        menuState = new MenuState(handler);
        gameState = new GameState(handler);
        makerState = new MakerState(handler);
        State.setState(menuState);

        this.canvas.createBufferStrategy(3);
    }

    private void update() {
        this.keyManager.update();
        if (State.getState() != null)
            State.getState().update();
    }

    private void render() {
        this.bS = this.canvas.getBufferStrategy();
        this.g = this.bS.getDrawGraphics();
        this.g.clearRect(0, 0, this.getWidth(), this.getHeight());
        
        if (State.getState() != null)
            State.getState().render(g);

        this.bS.show();
        this.g.dispose();
    }

    public Handler getHandler() {
        return handler;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }
    
    public MenuState getMenuState() {
        return (MenuState)menuState;
    }

    public GameState getGameState() {
        return (GameState)gameState;
    }

    public MakerState getMakerState() {
        return (MakerState)makerState;
    }

    public Font getFont() {
        return font;
    }

    public void run() {
        this.init();

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while(this.isRunning) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;

            if (delta >= 1) {
                this.update();
                this.render();
                delta--;
            }
        }
        this.stop();
    }

    public synchronized void start() {
        if(!this.isRunning) {
            this.isRunning = true;
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public synchronized void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            try {
                this.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
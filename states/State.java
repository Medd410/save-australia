package states;

import java.awt.Graphics;

import games.Handler;

public abstract class State {

    private static State currentState = null;
    protected Handler handler;

    public State(Handler handler) {
        this.handler = handler;
    }

    public static void setState(State state) {
        currentState = state;
    }

    public static State getState() {
        return currentState;
    }

    public Handler getHandler() {
        return handler;
    }

    public abstract void update();

    public abstract void render(Graphics g);
}
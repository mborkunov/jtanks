package jtanks.game.scene;

import java.awt.Graphics2D;


public abstract class Renderer {

    protected Node node;

    /**
     * Create renderer
     */
    public Renderer() {
    }

    /**
     * Get the value of node
     *
     * @return the value of node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Set the value of node
     *
     * @param node new value of node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Render node
     * 
     * @param g
     */
    public abstract void render(Graphics2D g);

    /**
     * Render node model
     * 
     * @param g
     */
    public abstract void renderModel(Graphics2D g);
}

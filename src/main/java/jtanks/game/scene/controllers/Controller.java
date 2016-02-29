package jtanks.game.scene.controllers;

import jtanks.game.scene.Node;

public abstract class Controller {

    /**
     * Node that will be controlled
     */
    protected Node node;

    /**
     * Last controller update executing
     */
    protected long lastCallTime;

    /**
     * Get the value of lastCallTime
     *
     * @return the value of lastCallTime
     */
    public long getLastCallTime() {
        return lastCallTime;
    }

    /**
     * Set the value of lastCallTime
     *
     * @param lastCallTime new value of lastCallTime
     */
    public void setLastCallTime(long lastCallTime) {
        this.lastCallTime = lastCallTime;
    }

    /**
     * Set controller node
     *
     * @param node
     */
    public void register(Node node) {
        this.node = node;
    }

    /**
     * Make updates on the registered node
     *
     * @param time A time elapsed since last call in milliseconds
     */
    abstract public void update(long time);
}

/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.scene;

import java.awt.Color;
import jtanks.game.scene.controllers.Controller;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import jtanks.game.geometry.Model;
import jtanks.game.scene.units.Unit;
import jtanks.game.util.Cache;
import jtanks.system.Registry;

public abstract class Node {

    public boolean highlight = true;

    protected boolean remove = false;
    protected Model model;
    protected CopyOnWriteArrayList<Controller> controllers = new CopyOnWriteArrayList<Controller>();
    protected List<Node> children;
    protected Node parent = null;
    protected Cache cache = new Cache<String,Object>();
    public ReentrantLock lock = new ReentrantLock();
    protected boolean hidden = false;
    protected Renderer renderer;

    /**
     * Get the value of renderer
     *
     * @return the value of renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Set the value of renderer
     *
     * @param renderer new value of renderer
     */
    public void setRenderer(Renderer renderer) {
        renderer.setNode(this);
        this.renderer = renderer;
    }

    public void affect(Unit unit) {
    }

    public void affect(Node unit) {
    }

    /**
     * Get the value of controller
     *
     * @return the value of controller
     */
    public List<Controller> getControllers() {
        return controllers;
    }

    /**
     * Add controller
     * @param controller new controller
     */
    public void addController(Controller controller) {
        if (!controllers.contains(controller)) {
            controller.register(this);
            controllers.add(controller);
        }
    }

    /**
     * Remove all controllers
     */
    public void clearControllers() {
        controllers.clear();
    }

    /**
     * Get the value of model
     *
     * @return the value of model
     */
    public Model getModel() {
        return model;
    }

    /**
     * Set the value of model
     *
     * @param model new value of model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    public List<Node> hasCollision(final Node node) {
        if (equals(node)) {
            return null;
        }

        List<Node> nodes = new ArrayList<Node>();

        if (model != null && node.getModel() != null) {
            Model m1, m2;
            if (this instanceof Unit) {
                m1 = ((Unit) this).getModel(((Unit) this).getMotion().getDirection());
            } else {
                m1 = this.getModel();
            }

            if (node instanceof Unit) {
                m2 = ((Unit) node).getModel(((Unit) node).getMotion().getDirection());
            } else {
                m2 = node.getModel();
            }

            if (m2.intersects(m1)) {
                nodes.add(node);
            }
        }

        if (node.getChildren() != null) {
            for (Node child : node.getChildren()) {
                List<Node> result;
                if ((result = this.hasCollision(child)) != null) {
                    nodes.addAll(result);
                }
            }
        }
        return nodes;
    }

    /**
     * @throws IllegalStateException when node is scheduled for deleting
     * @param child
     */
    public void addChild(Node child) {
        if (remove) {
            throw new IllegalStateException("This node is scheduled for deleting");
        }
        if (children == null) {
            children = new CopyOnWriteArrayList<Node>();
        }
        child.parent = this;
        lock.lock();
        try {
            children.add(child);
        } finally {
            lock.unlock();
        }
    }

    /*
     * Remove node only after all children node will be removed.
     */
    public void remove() {
        remove = true;
        lock.lock();
        try {
            if (children == null || children.size() == 0) {
                removeNow();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Remove this node immediate
     * @return
     */
    public boolean removeNow() {
        return parent.removeChild(this);
    }

    public boolean removeChild(Node child) {
        lock.lock();
        try {
            if (children != null) {
                return children.remove(child);
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * Replaces child with another node
     *
     * @param child Node that should be replaced
     * @param replace New node
     */
    public void replaceChild(Node child, Node replace) {
        lock.lock();
        try {
            int index = children.indexOf(child);
            children.set(index, replace);
        } finally {
            lock.unlock();
        }
    }

    public List<Node> getChildren() {
        lock.lock();
        try {
            return children;
        } finally {
            lock.unlock();
        }
    }

    public void update() {
        updateControllers();
        updateChildren();
    }

    private void updateControllers() {
        if (!controllers.isEmpty()) {
            for (Controller controller : controllers) {
                long time = 0;
                if (controller.getLastCallTime() != 0) {
                    time = System.currentTimeMillis() - controller.getLastCallTime();
                }
                controller.setLastCallTime(System.currentTimeMillis());
                controller.update(time);
            }
        }
    }

    private void updateChildren() {
        lock.lock();
        try {
            if (children != null) {
                if (!children.isEmpty()) {
                    for (Node child : children) {
                        child.update();
                    }
                } else if (remove) {
                    removeNow();
                }
            } else if (remove) {
                removeNow();
            }
        } finally {
            lock.unlock();
        }
    }

    public void setParent(Node node) {
        parent = node;
    }

    public Node getRoot() {
        Node root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    public void renderModel(Graphics2D g) {
        if (model != null && Registry.get("debug").equals(Boolean.TRUE)) {
            if (highlight) {
                g.setColor(Color.GREEN);
                highlight = false;
            } else {
                g.setColor(Color.RED);
            }

            int size = (Integer) Cache.GLOBAL.get("areaSize");

            int x, y, width, height;
            Model _model;
            if (this instanceof Unit) {
                Unit unit = (Unit) this;
                _model = unit.getModel(unit.getMotion().getDirection());
            } else {
                _model = model;
            }

            double lx = _model.getPosition().getX() + _model.getOffset().getX();
            double ly = _model.getPosition().getY() + _model.getOffset().getY();

            x = (int) Math.round(lx * size);
            y = (int) Math.round(ly * size);
            width = (int) Math.round(_model.getWidth() * size);
            height = (int) Math.round(_model.getHeight() * size);

            g.drawRect(x, y, width, height);
        }
        lock.lock();
        try {
            if (children != null) {
                for (Node child : children) {
                    child.renderModel(g);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void render(Graphics2D g) {
        lock.lock();
        try {
            if (children != null) {
                for (Node child : children) {
                    child.render(g);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void hide() {
        lock.lock();
        try {
            hidden = true;
        } finally {
            lock.unlock();
        }
    }

    public void show() {
        lock.lock();
        try {
            hidden = false;
        } finally {
            lock.unlock();
        }
    }

    public List<VolatileImage> getAnimationImages() {
        String key = "animation-" + getClass().getName();
        return (List<VolatileImage>) Cache.GLOBAL.get(key);
    }

    public int getAnimationTime() {
        String key = "animation-time-" + getClass().getName();
        Integer time = (Integer) Cache.GLOBAL.get(key);

        try {
            return time;
        } catch (NullPointerException e) {
            return 100;
        }
    }

    public void setImage(VolatileImage img) {
        try {
            cache.put("sprite", img);
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getSimpleName()).append("-").append(getModel()).toString();
    }

}

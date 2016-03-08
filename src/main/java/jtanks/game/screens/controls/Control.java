package jtanks.game.screens.controls;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import jtanks.game.geometry.Position;
import jtanks.game.scene.Node;
import jtanks.game.screens.helpers.Renderable;
import jtanks.system.Config;

public abstract class Control extends Node implements Renderable {


    private static List<Control> controls = new ArrayList<Control>();
    protected boolean focused = false;
    protected boolean enabled = true;
    protected String label;
    protected int width;
    protected int height;
    protected Font font;
    protected Color background = Color.GRAY;
    protected Color color = Color.WHITE;
    protected Binding binding;

    {
        String stringFont = Config.getInstance().get("font.family") +
                " " + Config.getInstance().get("font.style") +
                " " + height;
        font = Font.decode(stringFont);
    }

    public Control(String name) {
        this.label = name;
        controls.add(this);
    }

    public Control() {
        controls.add(this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void requestFocus() {
        lock.lock();
        try {
            for (Control control : controls) {
                control.blur();
            }
        } finally {
            lock.unlock();
        }
        focused = true;
    }

    public boolean hasFocus() {
        return focused;
    }

    public void blur() {
        focused = false;
    }

    public abstract void processEvent(KeyEvent e);
    public abstract void processEvent(MouseEvent e);


    /**
     * Get the value of binding
     *
     * @return the value of binding
     */
    public Binding getBinding() {
        return binding;
    }

    /**
     * Set the value of binding
     *
     * @param binding new value of binding
     */
    public void setBinding(Binding binding) {
        binding.setControl(this);
        this.binding = binding;
    }

    protected void setPosition(Position position) {
        if (model != null) {
            model.setPosition(position);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " - " + label;
    }
}


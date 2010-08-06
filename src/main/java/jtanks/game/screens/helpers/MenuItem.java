/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens.helpers;

import jtanks.game.screens.Screen;
import jtanks.system.Registry;
import jtanks.system.SoundManager;

public class MenuItem<T> {

    private String name;
    private Class<Screen> screen;
    private boolean status;
    private boolean isActive;
    private boolean disabled;
    private Screen screenInstance;
    private Screen caller;
    private T object;

    public MenuItem(String string, Class cls) {
        name = string;
        status = false;
        screen = cls;
    }

    public MenuItem(String string, Class cls, boolean selected) {
        name = string;
        status = selected;
        screen = cls;
    }
    
    public void set(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }

    public void setCaller(Screen caller) {
        this.caller = caller;
    }

    public Screen getScreen() throws InstantiationException, IllegalAccessException {
        if (screenInstance == null) {
            screenInstance = screen.newInstance();
            if (this.caller != null) {
                screenInstance.setCaller(caller);
            }
        }
        return screenInstance;
    }

    public void select(boolean status) {
        this.status = status;
        if (status) {
            ((SoundManager) Registry.get(SoundManager.class)).play("menu");
        }
    }

    public boolean isSelected() {
        return this.status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}

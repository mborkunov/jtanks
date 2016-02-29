package jtanks.game.screens.helpers;

import java.util.ArrayList;
import java.util.List;

import jtanks.game.screens.Screen;

public class Menu<T> {

    /**
     * Menu items storage
     */
    private ArrayList<MenuItem> menu = new ArrayList<MenuItem>();

    /**
     * Add new menu item to menu storage
     * 
     * @param item menu item
     */
    public void add(MenuItem<T> item) {
        this.menu.add(item);
    }

    /**
     * Return selected menu item
     * 
     * @return Active menu item
     */
    public MenuItem<T> getSelected() {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    public List<MenuItem> getNextItems(MenuItem item) {
        ArrayList<MenuItem> result = new ArrayList<MenuItem>();
        boolean found = false;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem it = menu.get(i);
            if (it == item) {
                found = true;
                continue;
            }
            if (found) {
                result.add(it);
            }
        }
        return result;
    }

    private List<MenuItem> getPreviousItems(MenuItem<T> item) {
        ArrayList<MenuItem> result = new ArrayList<MenuItem>();
        boolean found = true;
        for (int i = menu.size() - 1; i >= 0; i--) {
            MenuItem it = menu.get(i);
            if (it == item) {
                found = true;
                continue;
            }
            if (found) {
                result.add(it);
            }
        }
        return result;
    }

    public boolean hasNext() {
        List<MenuItem> nextItems = getNextItems(getSelected());
        for (MenuItem nextItem : nextItems) {
            if (!nextItem.isDisabled()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPrevious() {
        List<MenuItem> prevItems = getPreviousItems(getSelected());
        for (MenuItem prevItem : prevItems) {
            if (!prevItem.isDisabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mark a next of a selected menu item as selected
     */
    public void selectNext() {
        if (!hasNext()) {
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            if (item.isSelected() && i < (menu.size() - 1)) {
                item.select(false);
                while (true) {
                    MenuItem it = menu.get(++i);
                    if (it.isDisabled()) {
                        continue;
                    }
                    it.select(true);
                    break;
                }
                break;
            }
        }
    }

    /**
     * Mark a previous of a selected menu item as selected
     */
    public void selectPrevious() {
        if (!hasPrevious()) {
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            if (item.isSelected() && i > 0) {
                item.select(false);
                while (true) {
                    MenuItem it = menu.get(--i);
                    if (it.isDisabled()) {
                        continue;
                    }
                    it.select(true);
                    break;
                }
                break;
            }
        }
    }

    /**
     * Return an index number of current selected menu item
     * 
     * @return menu item index
     */
    public int selectedIndex() {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.get(i).isSelected()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Return menu item by index
     * 
     * @param b Index of an item
     * @return 
     */
    public MenuItem<T> getItem(byte b) {
        return menu.get(b);
    }

    /**
     * Select given menu item as selected
     * 
     * @param item
     */
    public void select(MenuItem item) {
        for (MenuItem menuItem : this.toArray()) {
            menuItem.select(menuItem.equals(item));
        }
    }

    /**
     * Mark menu item as selected by index number
     * @param index
     */
    public void select(int index) {
        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).select(i == index);
        }
    }

    /**
     * Return current menu storage as array
     * @return Menu array
     */
    public MenuItem<T>[] toArray() {
        return menu.toArray(new MenuItem[menu.size()]);
    }

    public void setCaller(Screen caller) {
        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).setCaller(caller);
        }
    }

    /**
     * Return size of current menu storage
     * 
     * @return
     */
    public int size() {
        return menu.size();
    }
}

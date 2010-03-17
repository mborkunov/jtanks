/*
 * GNU General Public License v2
 *
 * @version $Id: Menu.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.screens.helpers;

import java.util.ArrayList;
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

    /**
     * Mark a next of a selected menu item as selected
     */
    public void selectNext() {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            if (item.isSelected() && i < (menu.size() - 1)) {
                item.select(false);
                menu.get(i + 1).select(true);
                break;
            }
        }
    }

    /**
     * Mark a previous of a selected menu item as selected
     */
    public void selectPrevious() {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            if (item.isSelected() && i > 0) {
                item.select(false);
                menu.get(i - 1).select(true);
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
            menuItem.select(menuItem.equals(item) ? true : false);
        }
    }

    /**
     * Mark menu item as selected by index number
     * @param index
     */
    public void select(int index) {
        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).select(i == index ? true : false);
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

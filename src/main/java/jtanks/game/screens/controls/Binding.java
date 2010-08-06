/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens.controls;

public abstract class Binding {

    protected Control control;

    public Binding() {
    }

    public Binding(Control control) {
        this.control = control;
    }

    /**
     * Get the value of control
     *
     * @return the value of control
     */
    public Control getControl() {
        return control;
    }

    /**
     * Set the value of control
     *
     * @param control new value of control
     */
    public void setControl(Control control) {
        this.control = control;
    }

    abstract public void process();
}

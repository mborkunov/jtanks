/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens.helpers;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;

public class MultilineText {

    protected String text;

    protected Integer wrapWidth;

    protected Font font;

    protected ArrayList<String> lines = new ArrayList<String>();

    /**
     * Get the value of font
     *
     * @return the value of font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Set the value of font
     *
     * @param font new value of font
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Get the value of wrapWidth
     *
     * @return the value of wrapWidth
     */
    public Integer getWrapWidth() {
        return wrapWidth;
    }

    /**
     * Set the value of wrapWidth
     *
     * @param wrapWidth new value of wrapWidth
     */
    public void setWrapWidth(Integer wrapWidth) {
        this.wrapWidth = wrapWidth;
    }

    /**
     * Get the value of text
     *
     * @return the value of text
     */
    public String getText() {
        return text;
    }

    /**
     * Set the value of text
     *
     * @param text new value of text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Break given text in accordance with font size and wrap width
     */
    public void breakLines() {
        final AttributedString attStr = new AttributedString(text);
        attStr.addAttribute(TextAttribute.FONT, font);
        final LineBreakMeasurer measurer =
                new LineBreakMeasurer(attStr.getIterator (), new FontRenderContext (null, true, true));

        int pos = 0, newPos = 0;
        while (measurer.nextLayout(wrapWidth) != null) {
            newPos = measurer.getPosition();
            lines.add(text.substring(pos, newPos));
            pos = newPos;
        }
    }

    /**
     * Return breaked lines
     * 
     * @return
     */
    public ArrayList<String> getLines() {
        return lines;
    }
}

/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.system;

public class Quote {

    /**
     * Quote author
     */
    protected String author;

    /**
     * Quote text
     */
    protected String content;

    public Quote() {
    }

    public Quote(String author, String content) {
        this.author = author;
        this.content = content;
    }

    /**
     * Get the value of author
     *
     * @return the value of author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the value of author
     *
     * @param author new value of author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the value of content
     *
     * @return the value of content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the value of content
     *
     * @param content new value of content
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return author + ": \"" + content + "\"";
    }
}

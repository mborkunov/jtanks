package jtanks.game.gameplay;

public class State {

    protected int lives = 3;
    protected int rank = 1;

    public State() {
    }

    /**
     * Get the value of rank
     *
     * @return the value of rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Set the value of rank
     *
     * @param rank new value of rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Get the value of lives
     *
     * @return the value of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Set the value of lives
     *
     * @param lives new value of lives
     */
    public void setLives(int lives) {
        this.lives = lives;
    }
}

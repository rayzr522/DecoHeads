/**
 *
 */
package me.rayzr522.decoheads.gui.system;

/**
 * An immutable class representing an X size and a Y size
 *
 * @author Rayzr
 */
public class Dimension {

    public static final Dimension ONE = new Dimension(1, 1);

    /**
     * The size in the x direction
     */
    private int x;
    /**
     * The size in the y direction
     */
    private int y;

    /**
     * Creates a new dimension with the given x and y values
     *
     * @param x the size in the x direction
     * @param y the size in the y direction
     */
    public Dimension(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x size
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x size to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y size
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y size to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns whether this dimension fits inside the {@code other} dimension
     *
     * @param other the other dimension to check
     * @return Whether this dimension fits inside the {@code other} dimension
     */
    public boolean fitsInside(Dimension other) {
        return x >= 0 && y >= 0 && x <= other.getX() && y <= other.getY();
    }

    /**
     * Adds this dimension to the other dimension
     *
     * @param other the dimension to add
     * @return The combined dimension
     */
    public Dimension add(Dimension other) {
        return new Dimension(x + other.getX(), y + other.getY());
    }

    /**
     * Subtracts the other dimension from this dimension
     *
     * @param other the dimension to subtract
     * @return The subtracted dimension
     */
    public Dimension subtract(Dimension other) {
        return new Dimension(x - other.getX(), y - other.getY());
    }

    /**
     * Displays as {@code x,y}
     */
    @Override
    public String toString() {
        return x + "," + y;
    }

}

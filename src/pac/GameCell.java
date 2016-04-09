package pac;

import javax.swing.*;
import java.awt.*;

/**
 * The model of a single cell.
 *
 * @author Claudia Panoch
 */
public class GameCell
{

    /**
     * The number of images the game uses in total.
     */
    private static final int NUMBER_OF_IMAGES = 13;

    /**
     * Array of all images the game uses.
     */
    private static final Image[] IMAGES = new Image[NUMBER_OF_IMAGES];

    /* Image indices for special cell functions. */
    private static final int IMAGE_MINE = 9;
    private static final int IMAGE_COVERED = 10;
    private static final int IMAGE_FLAGGED = 11;
    private static final int IMAGE_FLAGGED_WRONG = 12;

    /* Initialize the static image array. */
    static
    {
        for (int i = 0; i < NUMBER_OF_IMAGES; i++)
        {
            IMAGES[i] = (new ImageIcon("res/j" + i + ".png")).getImage();
        }
    }

    /**
     * Indicator if the cell is a mine.
     */
    private boolean isMine = false;

    /**
     * Indicator if the cell has been flagged as mine by the user.
     */
    private boolean isFlagged = false;

    /**
     * The number of mines in the neighborhood.
     */
    private int neighborMinesCount = 0;

    /**
     * Indicator weather the cell has been clicked or not.
     */
    private boolean covered = true;

    /**
     * Get the visual representation of the cell.
     *
     * @param alive True if the game is alive, otherwise false.
     * @return The image for the cell's representation.
     */
    public Image getImage(final boolean alive)
    {

        /* If the cell has been flagged it will get a flag, except the cell was a mine and the game is over. */
        if (this.isFlagged())
        {
            if (alive || !this.isMine())
            {
                return IMAGES[IMAGE_FLAGGED];
            }
            return IMAGES[IMAGE_FLAGGED_WRONG];
        }

        /* If the game is not over and the cell remains covered it will be drawn as covered. */
        if (alive && this.isCovered())
        {
            return IMAGES[IMAGE_COVERED];
        }

        /* If the game is over, the cell was not flagged and the cell was a mine it is drawn as mine. */
        if (!alive && this.isMine())
        {
            return IMAGES[IMAGE_MINE];
        }

        /* In all other cases the number of mines in the neighborhood will be the result image. */
        return IMAGES[this.getNeighbourMineCount()];
    }

    /**
     * Mark the cell as mine.
     */
    public final void dropMine()
    {
        this.isMine = true;
    }

    /**
     * Mark the cell as flagged.
     */
    public final void flag()
    {
        this.isFlagged = true;
    }

    /**
     * Remove the flag marker.
     */
    public final void unFlag()
    {
        this.isFlagged = false;
    }

    /**
     * Uncover the cell and remove flag markers.
     */
    public void unCover()
    {
        this.covered = false;
        this.unFlag();
    }

    /**
     * Increase the count of mines in the neighborhood.
     */
    public final void increaseNeighborMineCount()
    {
        this.neighborMinesCount++;
    }

    /**
     * Returns true if the cell is a mine.
     *
     * @return true if the cell is a mine.
     */
    public final boolean isMine()
    {
        return this.isMine;
    }

    /**
     * Returns true if the cell has been flagged.
     *
     * @return true if the cell has been flagged.
     */
    public final boolean isFlagged()
    {
        return this.isFlagged;
    }

    /**
     * Returns true if the cell is still uncovered.
     *
     * @return true if the cell is still uncovered.
     */
    public final boolean isCovered()
    {
        return covered;
    }

    /**
     * Returns the number of cells in the neighborhood.
     *
     * @return The number of cells in the neighborhood.
     */
    public int getNeighbourMineCount()
    {
        return this.neighborMinesCount;
    }

}

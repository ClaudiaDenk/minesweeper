package pac;

import java.util.Random;

/**
 * The model of the game.
 *
 * @author Claudia Panoch
 */
public class GameModel
{

    /**
     * The number of the mines placed on the game board.
     */
    public static final int NUMBER_OF_MINES = 40;

    /**
     * The number of rows.
     */
    public static final int NUMBER_OF_ROWS = 16;

    /**
     * The number of columns.
     */
    public static final int NUMBER_OF_COLS = 16;

    /**
     * The 2d array that contains the cells.
     */
    private GameCell[][] cells = null;

    /**
     * Indicator if the game has been finished.
     */
    private boolean alive = true;

    /**
     * Indicator if the game was won.
     */
    private boolean won = false;

    /**
     * The number of flags that can be placed on the board.
     */
    private int flagsLeft = NUMBER_OF_MINES;

    /**
     * Initialize the model.
     */
    public GameModel()
    {
        final Random random = new Random();

        /* Initialize all cells. */
        this.cells = new GameCell[NUMBER_OF_ROWS][NUMBER_OF_COLS];
        for (int row = 0; row < NUMBER_OF_ROWS; row++)
        {
            for (int col = 0; col < NUMBER_OF_COLS; col++)
            {
                this.cells[row][col] = new GameCell();
            }
        }

        /* Now let's place the mines. We guess positions as long as we have enough mines. */
        int mineCounter = 0;
        while (mineCounter < NUMBER_OF_MINES)
        {
            final int row = random.nextInt(NUMBER_OF_ROWS);
            final int col = random.nextInt(NUMBER_OF_COLS);
            if (!this.getCell(row, col).isMine())
            {
                this.getCell(row, col).dropMine();
                this.updateNeighborMineCount(row, col);
                mineCounter++;
            }
        }
    }

    /**
     * Increase the neighbour mine counter of each neighbor by one.
     *
     * @param row The row of the min.
     * @param col The column of the mine.
     */
    private void updateNeighborMineCount(final int row, final int col)
    {

        /* We use min/max in order to prevent index out of bounds exceptions. */
        for (int neighborRow = Math.max(0, row - 1); neighborRow <= Math.min(NUMBER_OF_ROWS - 1, row + 1); neighborRow++)
        {
            for (int neighborCol = Math.max(0, col - 1); neighborCol <= Math.min(NUMBER_OF_COLS - 1, col + 1); neighborCol++)
            {

                /* Except the current cell. */
                if ((neighborRow != row) || (neighborCol != col))
                {
                    this.getCell(neighborRow, neighborCol).increaseNeighborMineCount();
                }
            }
        }
    }

    /**
     * Auto uncover neighbor cells.
     *
     * @param row The row.
     * @param col The column.
     */
    public void uncoverNeighbours(final int row, final int col)
    {

        /* Stop when the cell has neighbor mines. */
        if (this.getCell(row, col).getNeighbourMineCount() > 0)
        {
            return;
        }

        /* We use min/max in order to prevent index out of bounds exceptions. */
        for (int neighborRow = Math.max(0, row - 1); neighborRow <= Math.min(NUMBER_OF_ROWS - 1, row + 1); neighborRow++)
        {
            for (int neighborCol = Math.max(0, col - 1); neighborCol <= Math.min(NUMBER_OF_COLS - 1, col + 1); neighborCol++)
            {

                /* Except the current cell. */
                if ((neighborRow != row) || (neighborCol != col))
                {

                    /* Uncover the cell if it's no mine and if it's not flagged. */
                    final GameCell cell = this.getCell(neighborRow, neighborCol);
                    if (cell.isCovered() && !cell.isMine() && !cell.isFlagged())
                    {
                        cell.unCover();
                        this.uncoverNeighbours(neighborRow, neighborCol);
                    }
                }
            }
        }
    }

    /**
     * Check if all cells are uncovered and all flags are set. The game is won.
     */
    public final void updateGameState()
    {
        int uncover = 0;

        /* Iterate over all cells. If a cell is still covered we are not done. */
        for (int row = 0; row < NUMBER_OF_ROWS; row++)
        {
            for (int col = 0; col < NUMBER_OF_COLS; col++)
            {
                if (this.getCell(row, col).isCovered())
                {
                    uncover++;
                }
            }
        }

        /* Win the game. */
        if (this.alive && (uncover == 0))
        {
            this.alive = false;
            this.won = true;
        }
    }

    /**
     * Gets a cell of the game.
     *
     * @param row The row.
     * @param col The column.
     * @return The cell.
     */
    public final GameCell getCell(final int row, final  int col)
    {
        return this.cells[row][col];
    }

    /**
     * Returns true if the game was won.
     *
     * @return true if the game was won.
     */
    public final boolean hasWon()
    {
        return this.won;
    }

    /**
     * Returns true if the game is active.
     *
     * @return true if the game is active.
     */
    public final boolean isAlive()
    {
        return this.alive;
    }

    /**
     * The game was lost, a mine was clicked.
     */
    public void lose()
    {
        this.alive = false;
        this.won = false;
    }

    /**
     * Unset a flag.
     */
    public final void increaseFlagsLeft()
    {
        this.flagsLeft++;
    }

    /**
     * Set a flag.
     */
    public final void decreaseFlagsLeft()
    {
        this.flagsLeft--;
    }

    /**
     * Returns the count of the flags left.
     *
     * @return The count of the flags left.
     */
    public final int getFlagsLeft()
    {
        return this.flagsLeft;
    }

}

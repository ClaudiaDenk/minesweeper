package pac;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * The view and the integrated controller of the application.
 * This does not implement MVC atm.
 *
 * @author Claudia Panoch
 */
public class GameFrame extends JPanel
{

    /* Messages of the status bar. */
    private static final String MESSAGE_WON = "Game won!";
    private static final String MESSAGE_LOST = "Game lost!";
    private static final String MESSAGE_NO_MARKS = "No marks left";

    /**
     * The width / height of a cell image.
     */
    private static final int CELL_SIZE = 15;

    /**
     * The status bar for win/lose messages and the flag count.
     */
    private JLabel statusBar = null;

    /**
     * The model. This will be replaced for every round.
     */
    private GameModel model = new GameModel();

    /**
     * Initialize the main form of the game.
     *
     * @param statusBar The status bar of the application.
     */
    public GameFrame(final JLabel statusBar)
    {
        this.setPreferredSize(new Dimension((CELL_SIZE * GameModel.NUMBER_OF_COLS), (CELL_SIZE * GameModel.NUMBER_OF_ROWS)));
        this.statusBar = statusBar;
        this.setDoubleBuffered(true);

        /* The click handler for clicks on cells. */
        this.addMouseListener(new MouseAdapter()
        {

            /**
             * Handle a mouse click.
             *
             * @param event The mouse event.
             */
            @Override
            public void mouseClicked(final MouseEvent event)
            {
                handleMouseEvent(event);
            }

        });
    }

    /**
     * Draws the cells and updates the status bar.
     *
     * @param graphics The graphics object of the main form.
     */
    @Override
    public void paintComponent(final Graphics graphics)
    {

        /* Iterate over all cells and draw each cell image. */
        for (int row = 0; row < GameModel.NUMBER_OF_ROWS; row++)
        {
            for (int col = 0; col < GameModel.NUMBER_OF_COLS; col++)
            {
                graphics.drawImage(this.model.getCell(row, col).getImage(this.model.isAlive()), (col * CELL_SIZE), (row * CELL_SIZE), this);
            }
        }

        /* If the game has been finished display the win/lose message. */
        if (!this.model.isAlive())
        {
            this.statusBar.setText(this.model.hasWon() ? GameFrame.MESSAGE_WON : GameFrame.MESSAGE_LOST);
        }
        else
        {
            this.statusBar.setText(Integer.toString(this.model.getFlagsLeft()));
        }
    }

    /**
     * Handle a click on the cells.
     *
     * @param event The mouse event.
     */
    public void handleMouseEvent(final MouseEvent event)
    {

        /* Game already finished. Restart. */
        if (!this.model.isAlive())
        {
            this.model = new GameModel();
        }

        /* Transform the mouse click coordinates to cell coordinates. */
        final int row = event.getY() / CELL_SIZE;
        final int col = event.getX() / CELL_SIZE;

        /* The click happened in the area of cells. */
        if ((row >= GameModel.NUMBER_OF_ROWS) || (col >= GameModel.NUMBER_OF_COLS))
        {
            return;
        }

        /* The left mouse button uncovers a cell. */
        final GameCell cell = this.model.getCell(row, col);
        if (SwingUtilities.isLeftMouseButton(event))
        {

            /* When the cell is uncovered the trivial neighbours are uncovered automatically. */
            if (cell.isCovered())
            {
                cell.unCover();
                this.model.uncoverNeighbours(row, col);
            }
            if (cell.isMine())
            {
                this.model.lose();
            }
        }

        /* The right mouse button handles the flags. */
        else if (SwingUtilities.isRightMouseButton(event))
        {
            if (cell.isCovered())
            {
                if (cell.isFlagged())
                {
                    this.model.increaseFlagsLeft();
                    cell.unFlag();
                    this.statusBar.setText(Integer.toString(this.model.getFlagsLeft()));
                }
                else
                {
                    if (this.model.getFlagsLeft() > 0)
                    {
                        cell.flag();
                        this.model.decreaseFlagsLeft();
                        this.statusBar.setText(Integer.toString(this.model.getFlagsLeft()));
                    }
                    else
                    {
                        this.statusBar.setText(GameFrame.MESSAGE_NO_MARKS);
                    }
                }
            }
        }

        /* Update the game state, if all flags are set the cam could be won. Then repaint. */
        this.model.updateGameState();
        this.repaint();
    }

}

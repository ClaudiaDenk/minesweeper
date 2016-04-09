package pac;

import java.awt.BorderLayout;
import javax.swing.*;

/**
 * The main class of the game.
 *
 * Originally based on http://zetcode.com/tutorials/javagamestutorial/minesweeper/. Heavily rewritten.
 *
 * @author Claudia Panoch
 */
public class Minesweeper
{

    /**
     * The title of the main form.
     */
    private static final String TITLE = "Mineswepper";

    /**
     * The one and only constructor of this class. It creates a new JFrame that holds the game panel and the status bar
     * of the game. After the creation and configuration of the gui components the form is set visible.
     */
    public Minesweeper()
    {
        final JLabel statusBar = new JLabel();
        final JFrame gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setTitle(Minesweeper.TITLE);
        gameFrame.add(statusBar, BorderLayout.SOUTH);
        gameFrame.add(new GameFrame(statusBar));
        gameFrame.pack();
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
    }

    /**
     * Run the application by creating a new instance of this class.
     *
     * @param args The command-line arguments.
     */
    public static void main(final String[] args)
    {
        SwingUtilities.invokeLater(Minesweeper::new);
    }

}

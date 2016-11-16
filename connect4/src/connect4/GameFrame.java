package connect4;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

//Programmer: Patrick Eschbach
//Date: December, 2015
//Purpose: Contains methods/attributes to represent the game frame of the
//Connect4 GUI

public class GameFrame extends JFrame
{
  // Declaring the client and potentially server
  private ClientServerSocket client;

  // Declaring Frame Components
  private URL redURL, whiteURL, yellowURL, arrowURL;
  private ImageIcon redChip, yellowChip, whiteChip, arrow, myCol, oppCol, temp;
  private JLabel[][] board;
  private JLabel instructions, currentTurn;
  private JButton[] columnButton;
  private JPanel gamePanel, oppPanel, instPanel, turnPanel, gridLayout;
  private ButtonAction chipListener;

  // Declaring game state variables and constants
  boolean isServer, gameOver = false;
  int oppMove;
  static final int QUIT_CODE = 100, LOSE_CODE = 150, NEW_GAME_CODE = 200,
  MAX_ROWS = 6, MAX_COLUMNS = 7, WIN_NUM = 4;

  // Frame Constructor
  GameFrame(String inTitle, boolean isServerIn,
            String myColIn, String oppColIn)
  {
    super(inTitle);

    // Setting up the client
    isServer = isServerIn;
    client = new ClientServerSocket("127.0.0.1", 45000);

    // If the client is also the server
    if (isServer)
    {
      client.startServer();
    }
    // If not
    else
    {
      client.startClient();
    }

    // Setting up the Panels and starting message
    // Big Panel
    gamePanel = new JPanel();
    gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.PAGE_AXIS));
    add(gamePanel);

    // Intro message panels
    oppPanel = new JPanel(new FlowLayout());
    instPanel = new JPanel(new FlowLayout());
    turnPanel = new JPanel(new FlowLayout());
    gamePanel.add(oppPanel);
    gamePanel.add(instPanel);
    gamePanel.add(turnPanel);

    // Grid panel
    gridLayout = new JPanel(new GridLayout(MAX_COLUMNS, MAX_COLUMNS));
    gamePanel.add(gridLayout);

    // Initializing Frame Components
    redURL = getClass().getResource("/images/red.jpg");
    whiteURL = getClass().getResource("/images/white.jpg");
    yellowURL = getClass().getResource("/images/yellow.jpg");
    arrowURL = getClass().getResource("/images/downArrow.png");
    redChip = new ImageIcon(redURL, "Player One Move");
    yellowChip = new ImageIcon(yellowURL, "Player Two Move");
    whiteChip = new ImageIcon(whiteURL, "Unplayed Square");
    arrow = new ImageIcon(arrowURL, "Move Button");
    chipListener = new ButtonAction();

    // Setting up the player colors
    // Player 1
    if (myColIn == "Red")
    {
      myCol = redChip;
    }
    else
    {
      myCol = yellowChip;
    }
    // Player 2
    if (oppColIn == "Red")
    {
      oppCol = redChip;
    }
    else
    {
      oppCol = yellowChip;
    }


    // Initializing the game board as a 2d array, and adding it to the frame
    // First adding the play buttons, including listener functionality
    columnButton = new JButton[MAX_COLUMNS];
    for (int i = 0; i < MAX_COLUMNS; ++i)
    {
      columnButton[i] = new JButton(arrow);
      columnButton[i].addActionListener(chipListener);
      gridLayout.add(columnButton[i]);
    }
    // Then adding the visual component of the board
    board = new JLabel[MAX_ROWS][MAX_COLUMNS];
    for (int r = 0; r < MAX_ROWS; ++r)
    {
      for (int c = 0; c < MAX_COLUMNS; ++c)
      {
        board[r][c] = new JLabel(whiteChip);
        gridLayout.add(board[r][c]);
      }
    }

    // Adding the rest of the components to the correct panels
    // Adding info message
    instructions = new JLabel("Click the down arrows to drop a chip down " +
      "into that respective column.");
    currentTurn = new JLabel("It is currently your turn");
    instPanel.add(instructions);
    turnPanel.add(currentTurn);

    // If it's the client, we must have them wait for the server to move first
    if (!isServer)
    {
      new moveHandler().execute();
    }
  }


  // Function that checks to see if anyone has one the game
  public boolean gameWon(boolean isYou)
  {
    int count = 0;
    boolean gameOver = false;
    JLabel currentChip = board[0][0];

    // Checks the rows for 4 in a row
    for (int r = 0; r < MAX_ROWS; ++r)
    {
      // Resets the currentChip and count for each new row
      count = 0;
      currentChip = board[r][0];

      for (int c = 0; c < MAX_COLUMNS; ++c)
      {
        if (currentChip.getIcon() != whiteChip &&
          board[r][c].getIcon() == currentChip.getIcon())
        {
          ++count;
          if (count == WIN_NUM)
          {
            gameOver = true;
          }
        }
        else
        {
          // Resets the currentChip and count for the current tile
          currentChip = new JLabel(board[r][c].getIcon());
          count = 1;
        }
      }
    }

    // Checks the columns for 4 in a row
    count = 0;
    if (!gameOver)
    {
      for (int c = 0; c < MAX_COLUMNS; ++c)
      {
        // Resets the count and currentChip for each new column
        count = 0;
        currentChip = board[0][c];

        for (int r = 0; r < MAX_ROWS; ++r)
        {
          if (currentChip.getIcon() != whiteChip &&
            board[r][c].getIcon() == currentChip.getIcon())
          {
            ++count;
            if (count == WIN_NUM)
            {
              gameOver = true;
            }
          }
          else
          {
            // Resets the currentChip and count
            currentChip = new JLabel(board[r][c].getIcon());
            count = 1;
          }
        }
      }
    }

    // Checks the diagonals for 4 in a row
    // Diagonals one way
    if (!gameOver)
    {
      // Attributes used for calculation
      int startRow = 2, startCol = 0, endCol = 3, r = startRow, c = startCol;
      currentChip = new JLabel(board[r][c].getIcon());
      count = 0;

      while (startCol <= endCol)
      {
        while (r < MAX_ROWS && c < MAX_COLUMNS)
        {
          if (currentChip.getIcon() != whiteChip &&
            board[r][c].getIcon() == currentChip.getIcon())
          {
            ++count;
            if (count == WIN_NUM)
            {
              gameOver = true;
            }
          }
          else
          {
            // Resets the currentChip and count
            currentChip = new JLabel(board[r][c].getIcon());
            count = 1;
          }

          // Increment
          ++r;
          ++c;
        }

        // Increment
        if (startRow != 0)
        {
          --startRow;
        }
        else
        {
          ++startCol;
        }
        r = startRow;
        c = startCol;
      }
    }
    // Diagonals going the reverse way
    if (!gameOver)
    {
      // Attributes
      int startRow = 2, startCol = 6, endCol = 3, r = startRow, c = startCol;
      currentChip = new JLabel(board[r][c].getIcon());
      count = 0;

      while (startCol >= endCol)
      {
        while (r < MAX_ROWS && c >= 0)
        {
          if (currentChip.getIcon() != whiteChip &&
            board[r][c].getIcon() == currentChip.getIcon())
          {
            ++count;
            if (count == WIN_NUM)
            {
              gameOver = true;
            }
          }
          else
          {
            // Resets the currentChip and count
            currentChip = new JLabel(board[r][c].getIcon());
            count = 1;
          }

          // Increment
          ++r;
          --c;
        }

        // Increment
        if (startRow != 0)
        {
          --startRow;
        }
        else
        {
          --startCol;
        }
        r = startRow;
        c = startCol;
      }
    }

    // If someone won, and the game is over
    if(gameOver)
    {
      // Displays win text
      // If you won, you get to choose whether you want to play again
      int result = 0;
      if (isYou)
      {
        final JOptionPane winDialog = new JOptionPane();
        result = winDialog.showConfirmDialog(null,
          "Would you like to play again?",
          "You Win!", JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
      }

      // Determines whether or not to start another game

      // If the winner doesn't want to play again, send a signal through
      // to indicate to the other frame it needs to shut down
      if (result == 1)
      {
        client.sendMove(LOSE_CODE);
        this.dispose();
      }
      // If the winner does want to play again, send a signal to reload the
      // game frames
      else
      {
        // Updates the board back to the start
        for (int r = 0; r < MAX_ROWS; ++r)
        {
          for (int c = 0; c < MAX_COLUMNS; ++c)
          {
            board[r][c].setIcon(whiteChip);
          }
        }

        // Swaps colors for the next round
        temp = myCol;
        myCol = oppCol;
        oppCol = temp;

        // Indicates to the other player that a rematch is occuring
        client.sendMove(NEW_GAME_CODE);
      }
    }

    return gameOver;
  }


  // Function that finds what row to drop the chip in
  public int chipRow(int columnIn)
  {
    int dropPoint = 5;

    // Go down the column until you find an unplayed square
    while (dropPoint >= 0 && board[dropPoint][columnIn].getIcon() !=
      new JLabel(whiteChip).getIcon())
    {
      --dropPoint;
    }

    return dropPoint;
  }


  // Function that updates the game board to reflect the opponent's move
  public void opponentMove()
  {
    // Find the row
    int row = chipRow(oppMove);

    // Updates board accordingly
    board[row][oppMove].setIcon(oppCol);

    // Checks to see if you lost
    boolean isMe = false;
    // If the game isn't already over yet, check to see if it is
    if (!gameOver)
    {
      gameWon(isMe);
    }

    // if no one has won yet, updates move counter
    currentTurn.setText("It is currently your turn");
  }

  // Function to handle the ending of a game
  public void gameTerminate(String action)
  {
    // If the other player quit
    if (action == "Lose")
    {
      JOptionPane.showMessageDialog(null, "You lose! The other player does " +
        "not want to rematch",
        "Exiting Game", JOptionPane.INFORMATION_MESSAGE);
      this.dispose();
    }

    // If the other player lost and wants no rematch
    else if (action == "No rematch")
    {
      JOptionPane.showMessageDialog(null, "Sorry, the other player does not" +
        " wish to rematch",
        "Exiting Game", JOptionPane.INFORMATION_MESSAGE);
      this.dispose();
    }

    // The other player wants a rematch
    else
    {
      int result = 0;
      final JOptionPane loseDialog = new JOptionPane();
      result = loseDialog.showConfirmDialog(null,
        "Would you like to play again?",
        "You lose!", JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

      // Determines whether or not to start another game

      // If the player doesn't want to rematch
      if (result == 1)
      {
        client.sendMove(QUIT_CODE);
        this.dispose();
      }
      // If they do, Updates the board back to the start
      else
      {
        for (int r = 0; r < MAX_ROWS; ++r)
        {
          for (int c = 0; c < MAX_COLUMNS; ++c)
          {
            board[r][c].setIcon(whiteChip);
          }
        }

        // Swaps colors for the next round
        temp = myCol;
        myCol = oppCol;
        oppCol = temp;
      }
    }
  }

  // Listener class that can respond to button inputs
  public class ButtonAction implements ActionListener
  {
    // Putting the chip into the right portion of the board
    public void actionPerformed(ActionEvent e)
    {
      // Tracker variables
      int row, col = 0;

      // Find the column
      while (e.getSource() != columnButton[col])
      {
        ++col;
      }

      // Find the row
      row = chipRow(col);

      // Place the chip there, or inform the player they cannot play a chip
      // in that location
      if (row < 0)
      {
        JOptionPane.showMessageDialog(null, "You cannot place a chip here!",
          "Invalid Move", JOptionPane.WARNING_MESSAGE);
      }
      else
      {
        board[row][col].setIcon(myCol);

        // Check to see if your move wins you the game
        boolean isMe = true;
        boolean iWon = gameWon(isMe);

        // Use worker to wait for the next move
        new moveHandler().execute();

        // Send the move over
        client.sendMove(col);
      }

      return;
    }
  }


  // Responsible for handling blocking calls to make sure GUI doesn't freeze
  public class moveHandler extends SwingWorker<Integer, Integer>
  {
    public Integer doInBackground()
    {
      // Updates the game status
      currentTurn.setText("Waiting on opponent's move");

      // First disables all the buttons while waiting for the opponent move
      for (int i = 0; i < MAX_COLUMNS; ++i)
      {
        columnButton[i].setEnabled(false);
      }

      // Tries to receive a move
      oppMove = client.recvMove();

      // If the opponent won
      // If the opponent won, and wants to quit
      if (oppMove == LOSE_CODE)
      {
        oppMove = client.recvMove();
        gameOver = true;
        opponentMove();
        gameTerminate("Lose");
      }

      // If you won, and the opponent doesn't want to rematch
      else if (oppMove == QUIT_CODE)
      {
        gameTerminate("No rematch");
      }

      // If the opponent won, and wants to rematch
      else if(oppMove == NEW_GAME_CODE)
      {
        oppMove = client.recvMove();
        gameOver = true;
        opponentMove();
        gameTerminate("Rematch");
      }

      // if the opponent didn't win
      else
      {
        opponentMove();
      }

      return 0;
    }

    public void done()
    {
      // Re-enable the buttons
      for (int i = 0; i < MAX_COLUMNS; ++i)
      {
        columnButton[i].setEnabled(true);
      }
    }
  }
}

package connect4;

import javax.swing.JFrame;

//Programmer: Patrick Eschbach
//Date: December, 2015
//Purpose: Contains main function to run first instance of the Connect4 GUI
public class Connect4GUI
{
  // Player 1 Main
  public static void main(String [] args)
  {
    // Creating first game (with server)
    GameFrame gameScreen;
    gameScreen = new GameFrame("Game Screen", true, "Red", "Yellow");
    gameScreen.pack();
    gameScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Creating our title frame, and making it visible
    TitleFrame title;
    title = new TitleFrame("Server Game", gameScreen);
    title.pack();
    title.setVisible(true);
    title.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

}
package connect4;

import javax.swing.JFrame;

//Programmer: Patrick Eschbach
//Date: December, 2015
//Purpose: Contains main function to run second instance of the Connect4 GUI
public class Connect4GUI2
{
  // Player 2 Main
  public static void main(String [] args)
  {
    // Creating second game (without server)
    GameFrame gameScreen;
    gameScreen = new GameFrame("Game Screen", false, "Yellow", "Red");
    gameScreen.pack();
    gameScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Creating our title frame, and making it visible
    TitleFrame title;
    title = new TitleFrame("Client Game", gameScreen);
    title.pack();
    title.setVisible(true);
    title.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

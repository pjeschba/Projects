package connect4;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;

//Programmer: Patrick Eschbach
//Date: December, 2015
//Purpose: Contains methods/attributes to represent the title frame of the
//Connect4 GUI

public class TitleFrame extends JFrame
{
  // Declaring Frame Components
  private URL titleURL;
  private JLabel imgLabel, titleMessage, titleCredit;
  private JButton advance;
  private ButtonAction advanceListener;
  private JPanel titlePanel, row1, row2, row3, row4;
  private GameFrame gameScreen;

  // Frame Constructor
  TitleFrame(String inTitle, GameFrame gameScreenIn)
  {
    super(inTitle);

    // Setting up the corresponding gameScreen
    gameScreen = gameScreenIn;

    // Setting up the Panels
    // Big panel
    titlePanel = new JPanel();
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.PAGE_AXIS));
    add(titlePanel);
    // Second panel used to center
    row1 = new JPanel(new FlowLayout());
    row2 = new JPanel(new FlowLayout());
    row3 = new JPanel(new FlowLayout());
    row4 = new JPanel(new FlowLayout());

    // Initializing Frame Components
    titleMessage = new JLabel("Welcome To Connect Four!");
    titleURL = getClass().getResource("/images/titleScreen.jpg");
    imgLabel = new JLabel(new ImageIcon(titleURL, "Startup Picture"));
    advance = new JButton("Play");
    titleCredit = new JLabel("Coded by Patrick Eschbach");

    // Adding the components to the correct panels
    row1.add(titleMessage);
    row2.add(imgLabel);
    row3.add(advance);
    row4.add(titleCredit);
    titlePanel.add(row1);
    titlePanel.add(row2);
    titlePanel.add(row3);
    titlePanel.add(row4);

    // Listener setup
    advanceListener = new ButtonAction();
    advance.addActionListener(advanceListener);
  }

  // Class that terminates the intro screen
  public void terminate()
  {
    this.dispose();
    gameScreen.setVisible(true);
  }

  // Listener class that can respond to button inputs
  public class ButtonAction implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == advance)
      {
        terminate();
      }
    }
  }
}
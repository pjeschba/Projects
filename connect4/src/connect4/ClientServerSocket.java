package connect4;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import static java.lang.System.out;

//Programmer: Patrick Eschbach
//Date: December, 2015
//Purpose: Contains methods/attributes to represent methods/attributes of the
//client and server objects

public class ClientServerSocket
{
  // Attributes
  private String ipAddr;
  private int portNum;
  private Socket socket;
  private DataOutputStream outData;
  private DataInputStream inData;

  // Constructor
  public ClientServerSocket(String inIPAddr, int inPortNum)
  {
    ipAddr = inIPAddr;
    portNum = inPortNum;
    inData = null;
    outData = null;
    socket = null;
  }

  // Starts instance of a client
  public void startClient()
  {
    try
    {
      socket = new Socket(ipAddr, portNum);
      outData = new DataOutputStream(socket.getOutputStream());
      inData = new DataInputStream(socket.getInputStream());
    }
    catch (IOException ioe)
    {
      out.println("ERROR: Unable to connect - " +
        "is the server running?");
      System.exit(10);
    }
  }

  // Starts instance of a server
  public void startServer()
  {
    ServerSocket serverSock;
    try
    {
      serverSock = new ServerSocket(portNum);
      out.println("Waiting for clients to connect...");
      socket = serverSock.accept();
      outData = new DataOutputStream(socket.getOutputStream());
      inData = new DataInputStream(socket.getInputStream());
      out.println("Client connections accepted");
    }
    catch (IOException ioe)
    {
      out.println("ERROR: Caught exception starting server");
      System.exit(7);
    }
  }

  // Attempts to send the column number of the move
  public void sendMove(int colNum)
  {
    try
    {
       outData.writeInt(colNum);
    }
    catch (IOException ioe)
    {
      out.println("ERROR: Sending Move");
      System.exit(11);
    }
  }

  // Attempts to receive the column number of the move sent
  public int recvMove()
  {
    int moveIn = -1;
    try
    {
      moveIn = inData.readInt();
    }
    catch (IOException ioe)
    {
      out.println("ERROR: receiving move from socket");
      System.exit(8);
    }

    return (moveIn);
  }
}
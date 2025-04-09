//package Java-CMSC-3320.Program7;

import java.io.*;
import java.awt.*;
import java.net.*;

public class Chat implements Runnable, ActionListener, WindowListener {
    
    BufferedReader br;//input
    PrintWriter pw;//output

    //auto flush for print write constructor for readability
    protected final static boolean auto_flush = true;

    //buttons
    Button ChangePortButton = new Button("Change Port");
    Button SendButton = new Button("   Send    ");
    Button ServerButton = new Button("Start Server");
    Button ClientButton = new Button("   Connect   ");
    Button DisconnectButton = new Button("Disconnect");
    Button ChangeHostButton = new Button("Change Host");

    Label PortLabel = new Label("Port:");
    Label HostLabel = new Label("Host:");

    TextField ChatText = new TextField(70);
    TextField PortText = new TextField(10);
    TextField HostText = new TextField(10);

    Frame DispFrame;
    Thread TheThread;

    TextArea DialogScreen = new TextArea("", 10,80);
    TextArea MessageScreen = new TextArea("", 3,80);

    Socket client;
    Socket server;

    ServerSocket listen_socket;

    String host = "";
    int DEFAULT_PORT = 44004;

    int port = DEFAULT_PORT;
    //3 states
    //- initial where it is waiting to be identified
    //- server where it is the server portion
    //- client where it is the client portion
    int service = 0;
    //specify the wait time for connection
    int timeout = 1000;

    //control process loop for the program when it is either the server or the client
    boolean more = true;


    //-----------------CHAT CONSTRUCTOR----------------------------
    Chat(){}
    //-----------------END CHAT CONSTRUCTOR------------------------

    //-----------------MAIN---------------------------------------
    public static void main(String[] args) {
        new Chat();
    }
    //-----------------END MAIN-----------------------------------

    //========================WINDOW LISTENER METHODS=================================
    //add all 6 Window Listener Methods
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}
                
    public void windowDeactivated(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    //========================END WINDOW LISTENER METHODS=================================


    
    
}

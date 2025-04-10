//package Java-CMSC-3320.Program7;
/*******************HEADER*******************************/
/*  CMSC-3320 Technical Computing Using Java		    */   
/* 	Chat Program					                    */
/*	Group 1												*/
/*	Group Names: 										*/
/*     -Margo Bonal,      bon8330@pennwest.edu			*/
/*     -Luke Ruffing,     ruf96565@pennwest.edu 		*/
/*     -Ethan Janovich,   jan60248@pennwest.edu			*/
/*     -Nikolaus Roebuck, roe01807@pennwest.edu  		*/
/*******************END HEADER***************************/

//-------LIBRARIES---------
import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
//-------END LIBRARIES------


//================================BEGIN CLASS CHAT==================================================================
public class Chat implements Runnable, ActionListener, WindowListener {
    
    //-----------------------GLOBAL VARS-------------------------------------------------
    BufferedReader br;//input
    PrintWriter pw;//output

    //auto flush for print write constructor for readability
    protected final static boolean auto_flush = true;

    //buttons
    Button ChangePortButton = new Button("Change Port ");
    Button SendButton = new Button("     Send      ");
    Button ServerButton = new Button("Start Server");
    Button ClientButton = new Button("   Connect  ");
    Button DisconnectButton = new Button("Disconnect");
    Button ChangeHostButton = new Button("Change Host");

    Label PortLabel = new Label("Port:");
    Label HostLabel = new Label("Host:");

    TextField ChatText = new TextField(70);
    TextField PortText = new TextField(10);
    TextField HostText = new TextField(10);

    Frame DispFrame;
    Thread TheThread;
    private TextArea TopArea;//create area for typing
    private int sw = 650, sh=480;//screen width and height
     // New panels for layout
     private Panel sheet = new Panel();
     private Panel control = new Panel();


    TextArea DialogScreen = new TextArea("", 10,80);
    
    TextArea BottomArea = new TextArea("", 3,80);

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
    private static int timeout = 1000;

    //control process loop for the program when it is either the server or the client
    boolean more = true;

    boolean running=false;
    //-----------------------END GLOBAL VARS---------------------------------------------


    //-----------------CHAT CONSTRUCTOR----------------------------
    Chat(int timeout_num){
       try {
           initComponents();
           service = 0;
           more = true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public void initComponents() throws Exception {

        DispFrame = new Frame("Chat");
        DispFrame.setSize(sw, sh);
        DispFrame.setLayout(new GridBagLayout());
        DispFrame.setBackground(Color.white);
        DispFrame.setForeground(Color.black);
        DispFrame.addWindowListener(this); // Important!
    
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        c.gridx = 0;
    
        // ----------- TOP: TopArea -------------
        TopArea = new TextArea("", 10, 80);
        // TopArea.setEditable(false);
    
        c.gridy = 0;
        c.weighty = 0.3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        DispFrame.add(TopArea, c);
    
        // ----------- MIDDLE: Controls Panel -------------
        Panel middlePanel = new Panel(new GridBagLayout());
        GridBagConstraints m = new GridBagConstraints();
       // m.insets = new Insets(4, 4, 4, 4);
        middlePanel.setBackground(Color.lightGray);

        m.insets = new Insets(5, 5, 5, 5);


        // Row 0: Chat Text Field and Send Button
        m.gridx = 0; 
        m.gridy = 0;  // Start at column 0, row 0
        m.fill = GridBagConstraints.HORIZONTAL;
        m.weightx = 1.0; // Stretch across the width
        middlePanel.add(ChatText, m);

        //m.fill = GridBagConstraints.NONE;


        m.gridx = 0; 
        m.gridy = 1;
        m.anchor = GridBagConstraints.WEST;
        m.fill = GridBagConstraints.NONE;
        m.weightx = 0;
        middlePanel.add(HostLabel, m);

        //m.fill = GridBagConstraints.NONE;

        m.gridx = 0;
        m.fill = GridBagConstraints.HORIZONTAL;
        m.weightx = 1.0;
        middlePanel.add(HostText, m);

        m.gridx = 2;
        m.fill = GridBagConstraints.NONE;
        m.weightx = 0;
        middlePanel.add(ChangeHostButton, m);


        // Row 2: Port Label + Port Text + Change Port
        m.gridx = 0; 
        m.gridy = 2;
        m.anchor = GridBagConstraints.WEST;
        m.fill = GridBagConstraints.NONE;
        middlePanel.add(PortLabel, m);

        m.gridx = 0;
        m.fill = GridBagConstraints.HORIZONTAL;
        m.weightx = 1.0;
        middlePanel.add(PortText, m);

        m.gridx = 2;
        m.fill = GridBagConstraints.NONE;
        m.weightx = 0;
        middlePanel.add(ChangePortButton, m);


        // Row 0: Send Button
        m.gridx = 3; 
        m.gridy = 0;
        // m.fill = GridBagConstraints.NONE;
        middlePanel.add(SendButton, m);

        // Row 1–3: Server, Client, Disconnect buttons
        m.gridx = 3; 
        m.gridy = 1;
        middlePanel.add(ServerButton, m);

        m.gridx = 3; 
        m.gridy = 2;
        middlePanel.add(ClientButton, m);

        m.gridx = 3; 
        m.gridy = 3;
        middlePanel.add(DisconnectButton, m);


        //------------
        c.gridy = 1;
        c.weighty = 0.4;
        DispFrame.add(middlePanel, c);
    
        // ----------- BOTTOM: BottomArea -------------
        BottomArea = new TextArea("", 3, 80);
        //BottomArea.setEditable(true);
    
        c.gridy = 2;
        c.weighty = 0.3;
        DispFrame.add(BottomArea, c);

        //-------LISTENERS-----------------
        DispFrame.addWindowListener(this);
        ChangePortButton.addActionListener(this);
        SendButton.addActionListener(this);
        ServerButton.addActionListener(this);
        ClientButton.addActionListener(this);
        DisconnectButton.addActionListener(this);
        ChangeHostButton.addActionListener(this);
        ChatText.addActionListener(this);
        //BottomArea.addActionListener(this);
        PortText.addActionListener(this);
        HostText.addActionListener(this);

        //-------END LISTENERS-------------

        // ----------- Final Setup -------------
        DispFrame.setVisible(true);
    }
    //----------------END INIT COMPONENTS: SET UP FRAMES-------------
    void messageDisplay(String msg) {
        String type ="";

        switch(service){
            case 1:
                type = "Server";
                BottomArea.append("Server: " + msg + "\n");
                ChatText.requestFocus(); // Set focus to ChatText field
                break;
            case 2:
                type = "Client";
                BottomArea.append("Client: " + msg + "\n");
                ChatText.requestFocus(); // Set focus to ChatText field
                break;
            default:
                type = "Unknown";
                BottomArea.append("Unknown: " + msg + "\n");
                ChatText.requestFocus(); // Set focus to ChatText field
                break;
        }
        // Append the role and message to the status area
        TopArea.append(type + msg + "\n");

        // Return focus to the ChatText field
        ChatText.requestFocus();   
    }
   

    //----------------ACTION HANDLER------------------------------
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        //-----------------CHAT TEXT AND SEND BUTTON------------------
        if(source == ChatText || source == SendButton){
            String msg = ChatText.getText(); //get text from text field
        
            if (!msg.isEmpty()) {
                // Append to the dialog screen
                DialogScreen.append("out: " + msg + "\n");
    
                // Send through the socket
                if (pw != null) {
                    pw.println(msg);
                } else {
                    DialogScreen.append("Error: Not connected to any output stream.\n");
                }
                // Clear the chat input
                ChatText.setText("");
            }
            // Set focus back to chat field
            ChatText.requestFocus();
        }

        if (source == ServerButton){
            try {
                ServerButton.setEnabled(false);
                ClientButton.setEnabled(false);

                if(listen_socket != null){
                    listen_socket.close(); //close the socket
                    listen_socket = null; //null the socket
                }
                //messageDisplay(host + " " + port);
                messageDisplay("Connecting...");
                listen_socket = new ServerSocket(port);
                messageDisplay("Connected");

                listen_socket.setSoTimeout(10*timeout); // Set timeout for server socket

                if(client != null){
                    client.close(); //close the socket
                    client = null; //null the socket
                }
            } catch (IOException ex) {
                // TODO: handle exception
                System.out.println("Exception:   " + ex);
                messageDisplay("Error!!");
                close();
            }

            try {

                messageDisplay("Client");
                client = listen_socket.accept(); //accept the connection
                messageDisplay("Connected to client");
                DispFrame.setTitle("Server");
                messageDisplay("Server: connection from" +client.getInetAddress());
                
            } catch (Exception ep) {
                // TODO: handle exception
                System.err.println("Server: accept failed: " + ep);
                messageDisplay("Error!!");
                close();
            }

            try{
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                pw = new PrintWriter(client.getOutputStream(), auto_flush); //auto flush for print writer
                service = 1; //set service to server
                ChatText.setEnabled(true); //enable chat text field
                more = true;
                start(); //start the thread
            }catch(IOException er){
                System.out.println("Exception: " + er);
                messageDisplay("Error!!");
                close();
            }
        }
        // }catch(SocketTimeoutException s){
        //     System.out.println("Exception: " + s);
        //     close();
        // }
        //-----------------END CHAT TEXT AND SEND BUTTON---------------

        
        //-------------------CLIENT BUTTON------------------------
        if(source == ClientButton){
            //service = 2;
            messageDisplay("Now in Client Mode");

            try{
                ServerButton.setEnabled(false);
                ClientButton.setEnabled(false);
                if(server != null){
                    server.close(); //close the socket
                    server = null; //null the socket
                }
                server = new Socket(); //create a new socket
                messageDisplay("New Socket Created");
                server.setSoTimeout(timeout); // Set timeout for client socket
            }catch(IOException e1){
                System.out.println("Exception: " + e1);
                messageDisplay("Error!!");
                close();
            }

            try {
                messageDisplay("Attempting Connection...");
                server.connect(new InetSocketAddress(host, port)); //connect to the server
                DispFrame.setTitle("Client");
                messageDisplay("Connected to server: " + host + " " + port);
            } catch(IOException e2){
                System.out.println("Exception: " + e2);
                messageDisplay("Error!!");
                close();
            }

            try{
                br = new BufferedReader(new InputStreamReader(server.getInputStream()));
                pw = new PrintWriter(server.getOutputStream(), auto_flush); //auto flush for print writer
                service = 2; //set service to client
                ChatText.setEnabled(true); //enable chat text field
                more = true;
                start(); //start the thread
            }catch(IOException e3){
                System.out.println("Exception: " + e3);
                messageDisplay("Error!!");
                close();
            }

            //-------------------DISCONECT BUTTON----------------------
            
            //-----------------END DISCONNECT BUTTON-------------------



        }
        

    }
    //----------------END ACTION HANDLER--------------------------


    //========================WINDOW LISTENER METHODS=================================
    //add all 6 Window Listener Methods
    public void windowClosing(WindowEvent e) {
        stop();
    }
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}
                
    public void windowDeactivated(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    //========================END WINDOW LISTENER METHODS=================================

    //-------------THREAD STOP METHOD--------------------------------------
    public void stop() {
        // Remove window listener
        DispFrame.removeWindowListener(this);
    
        // Remove action listeners
        ChangePortButton.removeActionListener(this);
        SendButton.removeActionListener(this);
        ServerButton.removeActionListener(this);
        ClientButton.removeActionListener(this);
        DisconnectButton.removeActionListener(this);
        ChangeHostButton.removeActionListener(this);
        ChatText.removeActionListener(this);
        PortText.removeActionListener(this);
        HostText.removeActionListener(this);

        close();
    
        // Dispose the window
        DispFrame.dispose();
    
        // Stop the thread if it's running
        if (TheThread != null && TheThread.isAlive()) {
            TheThread.interrupt();
            
        }
    }
    //-------------END THREAD STOP METHOD----------------------------------

    //---------------THREAD CLOSE METHOD-----------------------------

    public void close(){
        try{
            if(server!=null){    // does server socket exist?
                if(pw!=null){    // does printwriter exist?
                    pw.print("");   //send null to other device
                  }  
             server.close();      //close the socket
             server=null;       //null the socket
                
            }
        }catch(IOException e){

        }
        //RESET BUTTONS AND HOST TEXTFIELD??
        ChangePortButton.addActionListener(this);
        SendButton.addActionListener(this);
        ServerButton.addActionListener(this);
        ClientButton.addActionListener(this);
        DisconnectButton.addActionListener(this);
        ChangeHostButton.addActionListener(this);
        HostText.addActionListener(this);

        service=0; //reset service to initial state

        TheThread=null;

    }

    //------------END THREAD CLOSE METHOD--------------------------------

    //-------------THREAD START METHOD-------------------------------------
    public void start(){
        if(TheThread == null) {
            TheThread = new Thread(this);
            TheThread.start();
            
            run();
        } else {
            System.out.println("Thread already started.");
        }
    }


    //-------------THREAD RUNNABLE METHOD--------------------------------------
    public void run() {
        System.out.println("Thread is running");
        
        while (more){
            try{
                String line= br.readLine();  //read line
                if(line!=null){
                    DialogScreen.append("in: "+ line+"\n"); //place line on dialogScreen
                }else{
                    more=false;
                }

            }catch(IOException e){
                more=false;
            }   
            System.out.println("All text has been read\n");
            System.out.println("resetting...\n");
            
            stop();
            
        }
    }
    //-------------END THREAD RUNNABLE METHOD----------------------------------

    //-----------------MAIN---------------------------------------
    public static void main(String[] args) {
       
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        
            try {
                System.out.print("Enter timeout in milliseconds (or press Enter to use default): ");
                String input = keyboard.readLine();
        
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        timeout = Integer.parseInt(input.trim());
                        System.out.println("Using timeout: " + timeout + " ms");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid!! Default timeout: " + timeout + " ms");
                    }
                } else {
                    System.out.println("No Input. Default timeout: " + timeout + " ms");
                }
            } catch (IOException e) {
                System.out.println("Error Reading!! Default timeout: " + timeout + " ms");
            }
        
            Chat chat = new Chat(timeout); 
        }
        
    }
    //-----------------END MAIN-----------------------------------

//================================END CLASS CHAT==================================================================


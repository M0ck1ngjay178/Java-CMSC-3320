/*******************HEADER*******************************/
/*  CMSC-3320 Technical Computing Using Java		    */   
/* 	Bounce Program					                    */
/*	Group 1												*/
/*	Group Names: 										*/
/*     -Margo Bonal,      bon8330@pennwest.edu			*/
/*     -Luke Ruffing,     ruf96565@pennwest.edu 		*/
/*     -Ethan Janovich,   jan60248@pennwest.edu			*/
/*     -Nikolaus Roebuck, roe01807@pennwest.edu  		*/
/*******************END HEADER***************************/

//---------LIBRARIES--------------
package BouncingBall;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
//---------END LIBRARIES-----------

//---------------------------------------------CLASS BOUNCE-------------------------------------------------------------------------
public class BouncingBall extends Frame implements WindowListener, ComponentListener, ActionListener, AdjustmentListener, Runnable {

    private static final long serialVersionUID = 10L;

    //constants
    private final int WIDTH=640; //initial frame width
    private final int HEIGHT=400;//initial frame height
    private final int BUTTONH=20;//button height
    private final int BUTTONHS=5;//button height spacing

    private final int MAXObj=100; //max object size
    private final int MINObj=10;//min object size
    private final int SPEED=50;//initial speed
    private final int SBvisible=10;//visible Scroll bar
    private final int SBunit=1;// unit step size
    private final int SBblock=10;//block step size
    private final int SCROLLBARH=BUTTONH;//scrollbar height
    private final int SOBJ=21;//initial object width

    //Program variable declarations
    private int WinWidth=WIDTH;//initial frame width
    private int WinHeight=HEIGHT;//initial frame height
    private int ScreenWidth;//drawing screen width
    private int ScreenHeight;//drawing screen height
    private int WinTop=10;//top of frame
    private int WinLeft=10;//left side of frame
    private int BUTTONW=50;//initial button width
    private int CENTER=(WIDTH/2);//initial screen center
    private int BUTTONS=BUTTONW/4;//initial button spacing

    private int SObj=SOBJ;//initial object width
    private int SpeedSBmin=1;//speed scrollbar min value
    private int SpeedSBmax=100+SBvisible;//speed scrollbar max with visible offset
    private int SpeedSBinit=SPEED;//initial speed scrollbar value
    private int ScrollBarW;//Scrollbar width

    private Insets I;//insets of frame

    //timer delay constant
    private final int DELAY = 50; //animation delay
    boolean runBall;              //flag that controls ball animation
    boolean TimerPause;           //flag that controls animation pause
    boolean started;              //flag to track if animation started
    int speed, delay;             //speed and delay for animation
    private int dx, dy;           //object movement direction
    boolean tailSet = true;       //flag that tracks tail visibility

    Button Start,Shape,Clear,Tail,Quit; //Buttons
    private Panel sheet, control; //add panels

    //objects
    private Objc Obj; //object to draw
    private Label SPEEDL= new Label("Speed", Label.CENTER); // label for speed scroll bar
    private Label SIZEL=new Label("Size",Label.CENTER);// label for size scroll bar
    Scrollbar SpeedScrollBar, ObjSizeScrollBar;//scrollbars
    private Thread thethread; //thread for timer delay

    
    public BouncingBall() {
        setTitle("Bouncing Ball");
        setLayout(new BorderLayout()); // Set the layout for the main frame to BorderLayout
        setVisible(true);

        started = false;
        ScreenWidth = WinWidth - WinLeft;
        ScreenHeight = WinHeight - WinTop;
        Obj = new Objc(50, ScreenWidth, ScreenHeight); // Example constructor for Objc
        MakeSheet();
        try {
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SizeScreen();
        start();
    }

    public void initComponents() throws Exception {
        // Initialize buttons
        Start = new Button("Run");
        Shape = new Button("Circle");
        Clear = new Button("Clear");
        Tail = new Button("No Tail");
        Quit = new Button("Quit");
    
        // Initialize Scrollbars (Horizontal)
        SpeedScrollBar = new Scrollbar(Scrollbar.HORIZONTAL);
        SpeedScrollBar.setMaximum(SpeedSBmax);
        SpeedScrollBar.setMinimum(SpeedSBmin);
        SpeedScrollBar.setUnitIncrement(SBunit);
        SpeedScrollBar.setBlockIncrement(SBblock);
        SpeedScrollBar.setValue(SpeedSBinit);
        SpeedScrollBar.setVisibleAmount(SBvisible);
        SpeedScrollBar.setBackground(Color.gray);
    
        ObjSizeScrollBar = new Scrollbar(Scrollbar.HORIZONTAL);
        ObjSizeScrollBar.setMaximum(MAXObj);
        ObjSizeScrollBar.setMinimum(MINObj);
        ObjSizeScrollBar.setUnitIncrement(SBunit);
        ObjSizeScrollBar.setBlockIncrement(SBblock);
        ObjSizeScrollBar.setValue(SOBJ);
        ObjSizeScrollBar.setVisibleAmount(SBvisible);
        ObjSizeScrollBar.setBackground(Color.gray);
    
        // Set preferred size to make scrollbars shorter and smaller
        SpeedScrollBar.setPreferredSize(new Dimension(150, 15));  // Set width and height
        ObjSizeScrollBar.setPreferredSize(new Dimension(150, 15));  // Set width and height
    
        // Create panels with GridBagLayout
        sheet = new Panel();
        sheet.setLayout(new GridBagLayout()); // Use GridBagLayout for the sheet panel
        control = new Panel();
        control.setLayout(new GridBagLayout());  // Use GridBagLayout for control panel
    
        // Add components to the sheet panel (canvas area)
        Obj.setBackground(Color.white);
        sheet.add(Obj, new GridBagConstraints());  // Add Obj (the canvas) to the sheet
        control.setBackground(Color.lightGray);
        setBackground(Color.lightGray);
    
        // Create and set up the button panel
        Panel buttonPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        gbc.gridx = 0; gbc.gridy = 0; buttonPanel.add(Start, gbc);
        gbc.gridx = 1; buttonPanel.add(Shape, gbc);
        gbc.gridx = 2; buttonPanel.add(Tail, gbc);
        gbc.gridx = 3; buttonPanel.add(Clear, gbc);
        gbc.gridx = 4; buttonPanel.add(Quit, gbc);
    
        // Add button panel to the control panel (Buttons will be centered in GridBagLayout)
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 1;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.gridwidth = 3; // Span across columns
        control.add(buttonPanel, buttonPanelConstraints);
    
        // Add Scrollbars to the control panel (East and West positions)
        GridBagConstraints sbConstraints = new GridBagConstraints();
        sbConstraints.fill = GridBagConstraints.HORIZONTAL;
    
        // Left position (SpeedScrollBar) - Place it at the far left side of the row
        sbConstraints.gridx = 0;  // Far left side of the row
        sbConstraints.gridy = 1;  // New row for the scrollbars
        sbConstraints.gridwidth = 1;  // Only one column wide
        sbConstraints.weightx = 1;  // Let it expand if needed
        control.add(SpeedScrollBar, sbConstraints);
    
        // Right position (ObjSizeScrollBar) - Place it at the far right side of the row
        sbConstraints.gridx = 4;  // Far right side of the row
        sbConstraints.gridy = 1;  // Same row as SpeedScrollBar
        sbConstraints.gridwidth = 1;  // Only one column wide
        sbConstraints.weightx = 1;  // Let it expand if needed
        control.add(ObjSizeScrollBar, sbConstraints);
    
        // Add panels to the frame using BorderLayout
        add(sheet, BorderLayout.CENTER);  // Sheet panel in the center
        add(control, BorderLayout.SOUTH);  // Control panel at the bottom
    
        // Add action listeners to buttons and scrollbars
        Start.addActionListener(this);
        Shape.addActionListener(this);
        Tail.addActionListener(this);
        Clear.addActionListener(this);
        Quit.addActionListener(this);
        SpeedScrollBar.addAdjustmentListener(this);
        ObjSizeScrollBar.addAdjustmentListener(this);
    
        // Window settings
        // pack();
        // setMinimumSize(getPreferredSize());
        // setBounds(WinLeft, WinTop, WinWidth, WinHeight);
        validate();
    }
    
    
    
    
    

    //creates layout and calculates screen size
    private void MakeSheet(){
        I=getInsets();
        ScreenWidth=WinWidth-I.left-I.right;
        ScreenHeight=WinHeight-I.top-2*(BUTTONH+BUTTONHS)-I.bottom;
        
        setSize(WinWidth,WinHeight);
        CENTER=(ScreenWidth/2);
        BUTTONW=ScreenWidth/11;
        BUTTONS=BUTTONW/4;
        //setBackground(Color.lightGray);

        ScrollBarW=2*BUTTONW; // scroll bar width
    }


    private void SizeScreen() {
        // Get insets
        I = getInsets();
        
        // Recalculate screen size
        ScreenWidth = WinWidth - I.left - I.right;
        ScreenHeight = WinHeight - I.top - 2 * (BUTTONH + BUTTONHS) - I.bottom;
    
        // Set the bounds of the main drawing area (canvas)
        Obj.setBounds(I.left, I.top, ScreenWidth, ScreenHeight);
    
    }
    
    

    //stops the program, removes listeners, and exits the program
    public void stop(){

        //stop the ball movement
        runBall = false;

        //removes action listeners
        Start.removeActionListener(this);
        Shape.removeActionListener(this);
        Clear.removeActionListener(this);
        Tail.removeActionListener(this);
        Quit.removeActionListener(this);

        //removes component and window listeners
        this.removeComponentListener(this);
        this.removeWindowListener(this);

        //removes adjustment listeners from scrollbars
        SpeedScrollBar.removeAdjustmentListener(this);
        ObjSizeScrollBar.removeAdjustmentListener(this);

        //disposes the frame and stops the thread
        dispose();
        thethread.interrupt();

        //terminates the program
        System.exit(0);
    }

    //starts the program
    public void start(){
        Obj.repaint(); //repaint object

        if(thethread == null){ //create a thread if it doesnt exist
            thethread = new Thread(this);//create a new thread
            thethread.start();//start the thread
        }
    }

    //========================ACTION HANDLER==========================================

    //handles button actions
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); //get current source
        //--------Start Button-----------------
        if(source==Start){
            if(Start.getLabel()=="Pause"){ //if the button is paused
                Start.setLabel("Run");  //set the label to run              
                TimerPause = true;//set timer to true to stop thread
            }
            else{
                Start.setLabel("Pause"); //otherwise, set label back to pause
                TimerPause = false; //set timer back to false to restat the thread
                started =true;     //reset stated to true
            }
        }
        //--------End Start Button-----------------

        //--------Tail Button-----------------
        if(source==Tail){
            if(Tail.getLabel()=="Tail"){ //check if button is tail
                Tail.setLabel("No Tail");// if it is, set label to no tail for next state transition
                started = true; //set started to true
                Obj.setTail(true);// call setTail to turn tail mode on, pass true
            }
            else{
                Tail.setLabel("Tail");//otherwise, set label to tail
                started = false;    //set started to false
                Obj.setTail(false);// pass false to setTail, to remove tail
            }
        }
        //--------End Tail Button-----------------

        //--------Shape Button-----------------
        if(source==Shape){
            if(Shape.getLabel()=="Circle"){//check if button is circle
                Shape.setLabel("Square");//set it back to square for te next state transition
                Obj.rectangle(false);       //call rectangle method, pass false to signal the drawing a circle   
            }
            else{
                Shape.setLabel("Circle"); //otherwise, set lable to circle
                Obj.rectangle(true);//draw square by passing true
            }
            Obj.repaint();// force repaint of object
        }
        //--------End Shape Button-----------------

        //--------Clear Button-----------------
        if(source==Clear){// check if source is equal to clear
            Obj.Clear(); //call clear 
            Obj.repaint();//force repaint
        }
        //--------End Clear Button----------------

        //--------Quit Button-----------------
        if(source==Quit){//if source is equal to quit button
            stop();//call stop to close action listerners and proccesses, windowClosing method
        }
        //--------End Quit Button---------------
        
    }
    //========================END ACTION HANDLER=======================================


    //handles adjustments to the scrollbars
    public void adjustmentValueChanged(AdjustmentEvent e) {

        int TS;
        Scrollbar sb = (Scrollbar) e.getSource(); // Get the scrollbar that triggered the event
    
        // Speed Scrollbar
        if (sb == SpeedScrollBar) {
            // Recalculate the delay based on the speed value
            int speedValue = SpeedScrollBar.getValue();
            delay = 100 - speedValue + 1; // Calculate delay

    
            // Interrupt the thread to apply the new delay
            if (thethread != null) {
                thethread.interrupt();
            }
        }
    
        // Object Size Scrollbar
        if (sb == ObjSizeScrollBar) {

            // Get the new size value from the scrollbar
            TS = e.getValue();
            
            // Make sure the size is an odd number for center alignment
            if(TS%2==0){
                TS=TS+1;
            }
            
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS<=ScreenWidth && TS <= ScreenHeight) {

                // Update the object size if it fits
                Obj.update(TS);
                
                
            } else {
                // Revert the scrollbar value if the size doesn't fit
                ObjSizeScrollBar.setValue(SObj); // Set back to previous valid size
            }
    
            //after updating, check if in No Tail mode, clear the display if necessary
            if (Tail.getLabel().equals("Tail")) {// when the text is Tail, that means there is currently no tail, so clear
                Obj.Clear(); // Clear the object if in No Tail mode
            }
        }
    
        // Repaint the object after the size change
        Obj.repaint();
    }

    //========================WINDOW LISTENER METHODS=================================
    
    //add all 6 Window Listener Methods
    public void windowClosing(WindowEvent e){
       stop();
    }

    public void windowClosed(WindowEvent e){}
    public void windowOpened(WindowEvent e){}

    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}

    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    //========================END WINDOW LISTENER METHODS=================================


    //====================COMPONENT METHODS============

    //checks if the object size is too large and adjusts it to fit within the screen
    private void checkSize() {

        // Get the current object size
        int currentSize = Obj.getSizeObj();
    
        // Check if the current object size is larger than the screen's width or height
        if (currentSize > ScreenWidth || currentSize > ScreenHeight) {
            // Adjust the size to fit within the screen
            currentSize = Math.min(ScreenWidth, ScreenHeight);
    
            // Update the object with the new size
            Obj.update(currentSize);
    
            // Update the scrollbar to reflect the new size
            ObjSizeScrollBar.setValue(currentSize);
        }
    }
    
    //handles the resizing of the component
    public void componentResized(ComponentEvent e){

        //update the window width and height based on the current size
        WinWidth=getWidth();
        WinHeight=getHeight();

        MakeSheet();

        //resize the object to fit the new screen dimensions
        Obj.reSize(ScreenWidth,ScreenHeight);

        //checks the object size
        checkSize();

        //reposition and resize screen and components
        SizeScreen();
    }
    
    public void componentHidden(ComponentEvent e){}

    public void componentShown(ComponentEvent e){}
    
    public void componentMoved(ComponentEvent e){}

    //=================END COMPONENT METHODS=========

    //============DRAWING OBJECT CLASS==============

    class Objc extends Canvas{

        // declare/initialize variables
        private static final long serialVersionUID=11L;
        private int ScreenWidth;
        private int ScreenHeight;
        private int SObj;
        int prevX, prevY;

        private int x, y;
        private boolean rect=true;
        private boolean clear=false;
        int offset = (SObj -1)/2;

        //constructor
        public Objc(int SB,int w, int h){
            ScreenWidth=w;
            ScreenHeight=h;
            SObj=SB;
            rect=true;
            clear=false;
        
            x = (SObj / 2)+1; //calculate offset for x
            y = (SObj / 2)+1; //calculate offset for y

            dy = 1; //set initial y flags to true
            dx = 1;//set initial x flags to true
        }
        
        //mutators
        public void rectangle(boolean r){
            rect=r;
        }

        public void update(int NS){
            SObj=NS;
        }

        public void setTail(boolean mode){
            tailSet=mode;
            //if (!tailSet) Clear();
        }

        public void stayInBounds(int newScreenWidth, int newScreenHeight) {
            // Calculate right and bottom 
            int right = x + SObj / 2;
            int bottom = y + SObj / 2;
        
            // Ensure the object is within bounds
            if (right > newScreenWidth) {
                x = newScreenWidth - SObj/2;
            }
            if (bottom > newScreenHeight) {
                y = newScreenHeight - SObj/2;
            }
        
            //dont let past edge
            if (x < 0) x = 0;
            if (y < 0) y = 0;

        }
        
        //resizes the screen
        public void reSize(int w, int h){
            ScreenWidth=w;
            ScreenHeight=h;
            stayInBounds(w, h);
        }

        //clears screen
        public void Clear(){
            clear=true;
        }

        //--------------------Get/Set---------------
        public int getXPos() { return x; }
        public int getYPos() { return y; }
        public int getSizeObj() { return SObj; }

        public void setXPos(int newX) { x = newX; }
        public void setYPos(int newY) { y = newY; }
        public void setSizeObj(int newSize) { SObj = newSize; }
        //-----------------End Get/Set--------------------

        //PAINT
        public void paint(Graphics g){
            update(g);
        }

        //UPDATE graphics for the ball
        public void update(Graphics g){
            
            if(clear){ //if passed clear,
                super.paint(g);// paint over canvas
                clear=false;//set clear back to false for next iteration
            }

            if (!tailSet) { //if tail is not on, draw object
                g.setColor(getBackground());//get background color
               
                if (rect) {
                    g.fillRect(prevX - (SObj - 1) / 2 - 1, prevY - (SObj - 1) / 2 - 1, SObj + 2, SObj + 2); //draw square
                } else {
                    g.fillOval(prevX - (SObj - 1) / 2 - 1, prevY - (SObj - 1) / 2 - 1, SObj + 2, SObj + 2);//draw circle
                }

            }
            //otherwise draw for tail mode
            if(rect){
                g.setColor(Color.lightGray);
                g.fillRect(x-(SObj-1)/2, y-(SObj-1)/2,SObj,SObj);
                g.setColor(Color.black);
                g.drawRect(x-(SObj-1)/2, y-(SObj-1)/2,SObj-1,SObj-1);
            }else{
                g.setColor(Color.lightGray);
                g.fillOval(x-(SObj-1)/2, y-(SObj-1)/2,SObj,SObj);
                g.setColor(Color.black);
                g.drawOval(x-(SObj-1)/2, y-(SObj-1)/2,SObj-1,SObj-1);
            }
            //draw background canvas at end to prevend thread updating glitches
            g.setColor(Color.red);
            g.drawRect(0, 0, ScreenWidth-1, ScreenHeight-1);

            //store previous values of x and y before next position
            prevX = x;
            prevY = y;
           
        }

        //moves the object within the screens boundaries
        private void move() {
            if (y + (SObj-1)/2 >= ScreenHeight || y-(SObj-1)/2<=0 ) {
                dy = -dy;
            }
            if (x + (SObj-1)/2 >= ScreenWidth || x-(SObj-1)/2<=0) {
                dx = -dx;
            }
            x += dx;//update position of x
            y += dy;//update position of y
            repaint();//force repaint
        }
    }

    //runs in a seperate thread to control the movement and updating of the object
    public void run() {

            //while the ball is active
            while (runBall) {

                //small delay, outside of decison but in loop
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {}
				if(!TimerPause)
				{
                	Obj.move();    //move the object based on its direction and position
                	Obj.repaint(); //repaint the object
				}
            }
    }
    //===========END OBJECT CLASS==================


    /*****************MAIN**************************/
    public static void main(String[] args) {
        new BouncingBall();
    }
    /*****************END MAIN********************/

}
//---------------------------------------------END CLASS BOUNCE-------------------------------------------------------------------------


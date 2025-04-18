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
import java.util.Vector;
//---------END LIBRARIES-----------


//import BouncingBall.Ball;

//---------------------------------------------CLASS BOUNCE-------------------------------------------------------------------------
public class BouncingBall extends Frame implements WindowListener, ComponentListener, ActionListener, AdjustmentListener, Runnable, MouseListener, MouseMotionListener{

    private static final long serialVersionUID = 10L;

    //constants

    private final int WIDTH=640; //initial frame width //out for prog 5
    private final int HEIGHT=400;//initial frame height //out for prog 5
    

    private final int BUTTONH=20;//button height
    private final int BUTTONHS=5;//button height spacing

    private final int MAXBall=100; //max Ballect size
    private final int MINBall=10;//min Ballect size
    private final int SPEED=50;//initial speed
    private final int SBvisible=10;//visible Scroll bar
    private final int SBunit=1;// unit step size
    private final int SBblock=10;//block step size
    private final int SCROLLBARH=BUTTONH;//scrollbar height
    private final int SBALL=21;//initial Ballect width

    //Program variable declarations

    private int WinWidth=WIDTH;//initial frame width  //out for prog 5
    private int WinHeight=HEIGHT;//initial frame height   //out for prog 5
    private int ScreenWidth;//drawing screen width  //out for prog 5
    private int ScreenHeight;//drawing screen height //out for prog 5

    
    private int WinTop=10;//top of frame
    private int WinLeft=10;//left side of frame
    private int BUTTONW=50;//initial button width
    private int CENTER=(WIDTH/2);//initial screen center
    private int BUTTONS=BUTTONW/4;//initial button spacing

    private int SBall=SBALL;//initial Ballect width
    private int SpeedSBmin=1;//speed scrollbar min value
    private int SpeedSBmax=100+SBvisible;//speed scrollbar max with visible offset
    private int SpeedSBinit=SPEED;//initial speed scrollbar value
    private int ScrollBarW;//Scrollbar width

    private Insets I;//insets of frame

    //timer delay constant
    private final int DELAY = 50; //animation delay
    boolean runBall;              //flag that controls Ballc animation
    boolean TimerPause;           //flag that controls animation pause
    boolean started;              //flag to track if animation started
    int speed, delay;             //speed and delay for animation
    private int dx, dy;           //Ballect movement direction
    boolean tailSet = true;       //flag that tracks tail visibility

    Button Start,Stop, Quit; //Buttons

    //Ballects
    private Ballc Ball; //Ballect to draw
    private Label SPEEDL= new Label("Speed", Label.CENTER); // label for speed scroll bar
    private Label SIZEL=new Label("Size",Label.CENTER);// label for size scroll bar
    Scrollbar SpeedScrollBar, BallSizeScrollBar;//scrollbars
    private Thread thethread; //thread for timer delay
    

    // New panels for layout
    private Panel sheet = new Panel();
    private Panel control = new Panel();

    private GridBagConstraints gbc;       
    private GridBagLayout gbl; 

    //Mouse points
    private Point m1 = new Point(0, 0); //first mouse point
    private Point m2 = new Point(0, 0); //second mouse point

    private final Point FrameSize = new Point(640, 400); //initial frame size
    private Point Screen = new Point(FrameSize.x - 1, FrameSize.y - 1); //drawing screen size

    // Rectangles
    private Rectangle Perimeter = new Rectangle(0, 0, ScreenWidth, ScreenHeight); // bouncing perimeter
    private Rectangle db = new Rectangle(); // drag box rectangle
    private static final Rectangle ZERO = new Rectangle(0, 0, 0, 0); // zero rectangle
    Rectangle temp = new Rectangle(); // temporary rectangle

    //rectangle vector
   // private Vector<Rectangle> Walls = new Vector<Rectangle>();

    //Bounce Constructor
    public BouncingBall() {
        setTitle("Bouncing Ball");
        setLayout(new BorderLayout());
        setVisible(true);
        MakeSheet();
        //started = false;
        
        Ball = new Ballc(SBall, ScreenWidth, ScreenHeight); // create a new Ballc object
        Ball.setBackground(Color.white); // set the background color of the Ballc
    
    
        // Initialize the Perimeter rectangle
        Perimeter = new Rectangle(0, 0, Screen.x, Screen.y);
        Perimeter.grow(-1, -1); // shrink the rectangle one pixel on all sides
        
        try{
            initComponents();
        }catch (Exception e){
            e.printStackTrace();
        }
        SizeScreen();

        start();
    }

    //initialize componenets
    public void initComponents()throws Exception, IOException{
        delay = DELAY;
        TimerPause = true;
        runBall = true;
    
        setLayout(new BorderLayout()); // layout for the frame
        setBounds(WinLeft, WinTop, FrameSize.x, FrameSize.y); // set the frame size
        this.setBounds(Perimeter); // set the frame size
        setBackground(Color.lightGray); // set the background color
        setVisible(true); // set the frame to be visible
    
        gbc = new GridBagConstraints(); // create a new GridBagConstraints object
        gbl = new GridBagLayout(); // create a new GridBagLayout object
        BorderLayout bl = new BorderLayout(); // create a new BorderLayout object
    
        // Ball
        // Ball = new Ballc(SBall, ScreenWidth, ScreenHeight); // create a new Ballc object
        // Ball.setBackground(Color.white); // set the background color of the Ballc
    
        // initialize buttons
        Start = new Button("Run");
        Stop = new Button("Pause");
        Quit = new Button("Quit");
        Start.setEnabled(false);
        Stop.setEnabled(true);
    
        // initialize points
        m1.setLocation(0, 0);
        m2.setLocation(0, 0);
    
        // rectangle
        Perimeter.setBounds(0, 0, ScreenWidth, ScreenHeight);
    
        // create and set up the panels
        sheet.setLayout(bl);
        sheet.setVisible(true);
    
        // Add the drawing Ballc to the center of the sheet
        sheet.add("Center", Ball);
    
        control.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        control.setBackground(Color.lightGray);
    
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Span across columns
        gbc.weightx = 1;  // Let it expand if needed
        control.add(Start, gbc);
        // ADDS PAUSE
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Span across columns
        gbc.weightx = 1;  // Let it expand if needed
        control.add(Stop, gbc);
        // ADDS QUIT
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Span across columns
        gbc.weightx = 1;  // Let it expand if needed
        control.add(Quit, gbc);
    
        // Left position (SpeedScrollBar) - Place it at the far left side of the row
        gbc.gridx = 0;  // Far left side of the row
        gbc.gridy = 0;  // New row for the scrollbars
        gbc.gridwidth = 1;  // Only one column wide
        gbc.weightx = 1;  // Let it expand if needed
        control.add(SpeedScrollBar, gbc);
    
        gbc.gridy = 1;  // Same row as SpeedScrollBar
        control.add(SPEEDL, gbc);
    
        // Right position (BallSizeScrollBar) - Place it at the far right side of the row
        gbc.gridx = 4;  // Far right side of the row
        gbc.gridy = 0;  // Same row as SpeedScrollBar
        gbc.gridwidth = 1;  // Only one column wide
        gbc.weightx = 1;  // Let it expand if needed
        control.add(BallSizeScrollBar, gbc);
    
        gbc.gridy = 1;  // Same row as SizeScrollBar
        control.add(SIZEL, gbc);
    
        sheet.add("Center", sheet);
        add("South", control);
        control.setVisible(true);
    
        // add buttons to frame
        Start.addActionListener(this);
        Stop.addActionListener(this);
        Quit.addActionListener(this);
    
        // add window and component listeners
        this.addComponentListener(this);
        this.addWindowListener(this);
    
        Ball.addMouseMotionListener(this);
        Ball.addMouseListener(this);
    
        // initialize scrollbars
        SpeedScrollBar = new Scrollbar(Scrollbar.HORIZONTAL);
        SpeedScrollBar.setMaximum(SpeedSBmax);
        SpeedScrollBar.setMinimum(SpeedSBmin);
        SpeedScrollBar.setUnitIncrement(SBunit);
        SpeedScrollBar.setBlockIncrement(SBblock);
        SpeedScrollBar.setValue(SpeedSBinit);
        SpeedScrollBar.setVisibleAmount(SBvisible);
        SpeedScrollBar.setBackground(Color.gray);
    
        BallSizeScrollBar = new Scrollbar(Scrollbar.HORIZONTAL);
        BallSizeScrollBar.setMaximum(MAXBall);
        BallSizeScrollBar.setMinimum(MINBall);
        BallSizeScrollBar.setUnitIncrement(SBunit);
        BallSizeScrollBar.setBlockIncrement(SBblock);
        BallSizeScrollBar.setValue(SBall);
        BallSizeScrollBar.setVisibleAmount(SBvisible);
        BallSizeScrollBar.setBackground(Color.gray);
    
        Ball.setBackground(Color.white);
    
        // add scrollbars
        add(SpeedScrollBar);
        add(BallSizeScrollBar);
        add(SPEEDL);
        add(SIZEL);
        add(Ball);
    
        // add scrollbar listeners
        SpeedScrollBar.addAdjustmentListener(this);
        BallSizeScrollBar.addAdjustmentListener(this);
    
        // Add the panels to the frame
        add(sheet, BorderLayout.CENTER);
        add(control, BorderLayout.SOUTH);
    
        validate();
    
        setVisible(true);
    }

    //creates layout and calculates screen size
    private void MakeSheet(){
        I=getInsets();
        ScreenWidth=WinWidth-I.left-I.right;
        ScreenHeight=WinHeight-I.top-2*(BUTTONH+BUTTONHS)-I.bottom;
        CENTER=(ScreenWidth/2);
        setSize(WinWidth,WinHeight);
        
       // BUTTONW=ScreenWidth/11;
       // BUTTONS=BUTTONW/4;
        //setBackground(Color.lightGray);

       // ScrollBarW=2*BUTTONW; // scroll bar width
    }

    //positions and sizes buttons, scrollbars, and labels on the screen
    private void SizeScreen(){

       Ball.setBounds(0,0,ScreenWidth,ScreenHeight);
    }

    //stops the program, removes listeners, and exits the program
    public void stop(){

        //stop the Ballc movement
        runBall = false;

        //removes action listeners
        Start.removeActionListener(this);
        Stop.removeActionListener(this);
        
        Quit.removeActionListener(this);

        //removes component and window listeners
        this.removeComponentListener(this);
        this.removeWindowListener(this);
        this.removeMouseMotionListener(this);
        this.removeMouseListener(this);

        //removes adjustment listeners from scrollbars
        SpeedScrollBar.removeAdjustmentListener(this);
        BallSizeScrollBar.removeAdjustmentListener(this);

        //disposes the frame and stops the thread
        dispose();
        thethread.interrupt();

        //terminates the program
        System.exit(0);
    }

    //starts the program
    public void start(){

        Ball.repaint(); //repaint Ball

        if(thethread == null){ //create a thread if it doesnt exist
            thethread = new Thread(this);//create a new thread
            thethread.start();//start the thread
        }

        runBall = true;
        Ball.repaint();
    }

    //========================ACTION HANDLER==========================================

    //handles button actions
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); //get current source
      
        //--------Start Button-----------------
        if(source==Start){


            Start.setEnabled(false);
            Stop.setEnabled(true);
            
           
           // started = true;
            TimerPause = false; //set timer to false to start the thread
            //thethread.start();
        }
        //--------End Start Button-----------------

        //--------Pause Button-----------------
        if(source==Stop){


            Start.setEnabled(true);
            Stop.setEnabled(false);
           
           // started = false;
            TimerPause = true; //set timer to false to start the thread
            //thethread.interrupt();

        }
        //--------End Pause Button-----------------


        //--------Quit Button-----------------
        if(source==Quit){//if source is equal to quit button
            stop();//call stop to close action listerners and proccesses, windowClosing method
        }
        //--------End Quit Button---------------
        
    }
    //========================END ACTION HANDLER=======================================


    //handles adjustments to the scrollbars
    public void adjustmentValueChanged(AdjustmentEvent e) {

        int i;
        boolean ok;
        Rectangle t;
        int half;
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
    
        // Ballect Size Scrollbar
        if (sb == BallSizeScrollBar) {

            // Get the new size value from the scrollbar
            TS = e.getValue();
            
            // Make sure the size is an odd number for center alignment
            //if(TS%2==0){
           //     TS=TS+1;
           // }
           TS = (TS/2)*2+1; // Ensure TS is odd
            half=(TS-1)/2;


            // Validate if the new size will fit inside the screen without touching the borders
            if (TS<=ScreenWidth && TS <= ScreenHeight) {

                // Update the Ballect size if it fits
                Ball.updateSize(TS);
                
                
            } else {
                // Revert the scrollbar value if the size doesn't fit
                BallSizeScrollBar.setValue(SBall); // Set back to previous valid size
            }

            Rectangle b = new Rectangle();
            b.setBounds(Ball.getX()-half-1, Ball.getY()-half-1, TS+2, TS+2);
    
            if (b.equals(Perimeter.intersection(b))) {
                // Ball is within the perimeter
                i = 0;
                ok = true;
                while ((i < Ball.getWallSize()) && ok){
                    t = Ball.getOne(i);
                    if(t.intersects(b)){
                        ok = false;
                    }
                    i++;
                }

                if(ok){
                    SBall=TS;
                    Ball.updateSize(SBall);
                }
    
            } else {
                // Ball is beyond the perimeter
                BallSizeScrollBar.setValue(SBall); // Set back to previous valid size
            }

        }
    
        // Repaint the Ballect after the size change
        Ball.repaint();
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

  //========================MOUSE LISTENER METHODS=================================

    public void mousePressed(MouseEvent e){
        m1.setLocation(e.getPoint());
    }
    public void mouseDragged(MouseEvent e){
        db.setBounds(getDragBox(e));
        if(Perimeter.contains(db)){
            Ball.setDragBox(db);
            Ball.repaint();
        }
    }
    public void mouseReleased(MouseEvent e){
        Rectangle b = new Rectangle(Ball.getBounds());
        b.grow(1, 1);

        if (!Perimeter.contains(db)) {
            db = Perimeter.intersection(db);
        }
    
        // Check if the new rectangle is covered by any rectangle in the Vector
        boolean covered = false;
        for (int i = 0; i < Ball.getWallSize(); i++) {
            Rectangle r = Ball.getOne(i);
            if (r.intersection(db).equals(db)) {
                covered = true;
                break;
            }
        }
    
        // If not covered, store the rectangle
        if (!covered) {
            Ball.addOne(new Rectangle(db));
        }
    
        // Check if the new rectangle covers any rectangle in the Vector and delete the covered rectangle
        for (int i = 0; i < Ball.getWallSize(); i++) {
            Rectangle r = Ball.getOne(i);
            if (db.intersection(r).equals(r)) {
                Ball.removeOne(i);
                i--; // Adjust index after removal
            }
        }
        Ball.repaint();
    }

    public void mouseClicked(MouseEvent e){
        Point p = new Point(e.getX(), e.getY());
        int i = 0;
        Rectangle b;

        while (i < Ball.getWallSize()) {
            b = Ball.getOne(i);
          // Rectangle b = Walls.get(i);
            if (b.contains(p)) {
             Ball.removeOne(i);
             //Walls.remove(i);
            } 
            else {
            i++;
            }
        }   

    Ball.repaint();
    }
    public void mouseEntered(MouseEvent e){
        Ball.repaint();
    }
    public void mouseExited(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}

  //===============================================================================


    //====================COMPONENT METHODS============

    public Rectangle getDragBox(MouseEvent e){

        m2.setLocation(e.getPoint());

        int x = Math.min(m1.x, m2.x);
        int y = Math.min(m1.y, m2.y);
        int width = Math.abs(m1.x - m2.x);
        int height = Math.abs(m1.y - m2.y);
    
        return new Rectangle(x, y, width, height);
        
    }

    
    //handles the resizing of the component
    public void componentResized(ComponentEvent e){

        Rectangle r = new Rectangle();
        int mr, mb;

        r.setBounds(Ball.getOne(0));
        mr = r.x + r.width;
        mb = r.y + r.height;

        // Use a for loop to process the remaining elements of the Vector
        for (int i = 1; i < Ball.getWallSize(); i++) {
            r.setBounds(Ball.getOne(i)); // get ith rectangle
            mr = Math.max((r.x + r.width), mr); // keep max right
            mb = Math.max((r.y + r.height), mb); // keep max bottom
        }

         // Process the ball
       // r.setBounds(Ball.getOne(i)); // get ball rectangle
       // mr = Math.max((r.x + r.width), mr); // keep max right
       // mb = Math.max((r.y + r.height), mb); // keep max bottom

        // Define expansion and insets
        int EXPAND = 10; // small border beyond the rectangles
        int lw = I.left + I.right;
        int lh = I.top + I.bottom;

        if (mr > Screen.x || mb > Screen.y) {
            setSize(Math.max((mr + EXPAND), Screen.x) + lw, Math.max((mb + EXPAND), Screen.y) + lh + 2 * BUTTONH);
        }

        // Force a Frame refresh to the new size
        setExtendedState(ICONIFIED);
        setExtendedState(NORMAL);

        // Update the Screen point
        Screen.setLocation(sheet.getWidth() - 1, sheet.getHeight() - 1);

        // Update the Perimeter rectangle
        Perimeter.setBounds(getX(), getY(), Screen.x, Screen.y);
        Perimeter.grow(-1, -1); // shrink the rectangle one pixel on all sides

        // Send the new Screen size to the Ball object
        Ball.reSize(Screen.x, Screen.y); // resize the ball screen

        MakeSheet();
        SizeScreen();
        Ball.repaint(); // repaint
        //update the window width and height based on the current size
        //WinWidth=getWidth();
        //WinHeight=getHeight();

        //

        //resize the Ballect to fit the new screen dimensions
        //Ball.reSize(ScreenWidth,ScreenHeight);

        //checks the Ballect size
       // checkSize();

        //reposition and resize screen and components
        //
    }
    
    public void componentHidden(ComponentEvent e){}

    public void componentShown(ComponentEvent e){}
    
    public void componentMoved(ComponentEvent e){}

    //=================END COMPONENT METHODS=========

    //============DRAWING BallECT CLASS==============

    class Ballc extends Canvas{

        // declare/initialize variables
        private static final long serialVersionUID=11L;
        private int ScreenWidth;
        private int ScreenHeight;
        private int SBall;
        int prevX, prevY;

        private int x, y;
        private boolean rect=true;
        private boolean clear=false;
        int offset = (SBall -1)/2;

        Image buffer;
        Graphics g;

        private Rectangle dragrec = new Rectangle();
        private Vector<Rectangle> Walls = new Vector<Rectangle>();
        

        //constructor
        public Ballc(int SB,int w, int h){
            ScreenWidth=w;
            ScreenHeight=h;
            SBall=SB;
            rect=true;
            clear=false;
        
            x = (SBall / 2)+1; //calculate offset for x
            y = (SBall / 2)+1; //calculate offset for y
            dx = 1;//set initial x flags to true
        }
        
        //mutators
        public void setDragBox(Rectangle r){
            temp.setBounds(r);
        }

        public void addOne(Rectangle r){
            Walls.addElement(r);
        }

        public void removeOne(int i){
            Walls.removeElementAt(i);
        }

        public Rectangle getOne(int i){
            return Walls.elementAt(i);
        }

        public int getWallSize(){
            return Walls.size();
        }
        
        public void rectangle(boolean r){
            rect=r;
        }

        public void updateSize(int NS){
            SBall=NS;
        }

        public void setTail(boolean mode){
            tailSet=mode;
            //if (!tailSet) Clear();
        }

        public void stayInBounds(int newScreenWidth, int newScreenHeight) {
            // Calculate right and bottom 
            int right = x + SBall / 2;
            int bottom = y + SBall / 2;
        
            // Ensure the Ballect is within bounds
            if (right > newScreenWidth) {
                x = newScreenWidth - SBall/2;
            }
            if (bottom > newScreenHeight) {
                y = newScreenHeight - SBall/2;
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
        public int getSizeBall() { return SBall; }

        public void setXPos(int newX) { x = newX; }
        public void setYPos(int newY) { y = newY; }
        public void setSizeBall(int newSize) { SBall = newSize; }
        //-----------------End Get/Set--------------------

        //PAINT
        public void paint(Graphics cg){

            Rectangle temp;

            buffer = createImage(ScreenWidth, ScreenHeight); 
            if(g!=null){
                g.dispose();
            }
            g = buffer.getGraphics();

            g.setColor(Color.red);
            g.fillOval(Ball.getXPos(), Ball.getYPos(), Ball.getSizeBall(), Ball.getSizeBall());
            //update(g);
            g.setColor(Color.black);
            g.drawOval(Ball.getXPos(), Ball.getYPos(), Ball.getSizeBall(), Ball.getSizeBall());
            
            

            for(int i=0; i < Walls.size(); i++){
                temp = Walls.elementAt(i);
                g.setColor(Color.black);
                g.fillRect(temp.x, temp.y, temp.width, temp.height);
            }

            g.drawRect(dragrec.x, dragrec.y, dragrec.width, dragrec.height);

            cg.drawImage(buffer,0,0,null);
        }

        //UPDATE graphics for the Ballc
        public void update(Graphics g){
            
          paint(g);
        }

        //moves the object within the screens boundaries
        private void move() {
            Ball.x += dx;
            Ball.y += dy;
        
            Rectangle b = new Rectangle(Ball.getBounds());
            b.grow(1, 1); 
        
            for (Rectangle rect : Walls) {
                Rectangle leftEdge = new Rectangle(rect.x - 1, rect.y + 1, 1, rect.height - 2);
                Rectangle rightEdge = new Rectangle(rect.x + rect.width, rect.y + 1, 1, rect.height - 2);
                Rectangle topEdge = new Rectangle(rect.x + 1, rect.y - 1, rect.width - 2, 1);
                Rectangle bottomEdge = new Rectangle(rect.x + 1, rect.y + rect.height, rect.width - 2, 1);
        
                if (b.intersects(leftEdge) || b.intersects(rightEdge)) {
                    dx = -dx;
                    break;
                }
        
                if (b.intersects(topEdge) || b.intersects(bottomEdge)) {
                    dy = -dy;
                    break;
                }
            }
            repaint();
        }

        public Rectangle isTouching() 
        {
            Rectangle r = new Rectangle(ZERO); // Initialize a rectangle of zero
            Rectangle b = new Rectangle(Ball.getBounds()); // Create a rectangle representing the ball
            b.grow(1, 1); // Grow the rectangle by one pixel on all sides
    
            boolean ok = true;
            int i = 0;
    
            // Sequential search to check for intersections
            while ((i < Walls.size()) && ok) {
                r = Walls.elementAt(i); // Get the ith rectangle
                if (r.intersects(b)) { // Check if the rectangle intersects with the grown ball rectangle
                    ok = false; // Intersection found
                } else {
                    i++; // Move to the next rectangle
                }
            }
    
            // Return the touching rectangle or a zero rectangle if no intersections are found
            if (!ok) {
                return r;
            } else {
                return ZERO;
            }
        }

    }

    //runs in a seperate thread to control the movement and updating of the Ballect
    public void run() {

            //while the Ballc is active
            while (runBall) {

                //small delay, outside of decison but in loop
                try {
                    thethread.sleep(1);
                } catch (InterruptedException e) {}

				if(!TimerPause)
				{
                    started = true;
                	Ball.move();    //move the Ballect based on its direction and position
                	Ball.repaint(); //repaint the Ballect
				}
            }
    }
    //===========END BallECT CLASS==================


    /*****************MAIN**************************/
    public static void main(String[] args) {
        new BouncingBall();
    }
    /*****************END MAIN********************/

}
//---------------------------------------------END CLASS BOUNCE-------------------------------------------------------------------------


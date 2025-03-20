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
//package BouncingBall;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
//---------END LIBRARIES-----------
import java.util.Vector;

//---------------------------------------------CLASS BOUNCE-------------------------------------------------------------------------
public class BouncingBall extends Frame implements WindowListener, ComponentListener, ActionListener, AdjustmentListener, Runnable, MouseListener,MouseMotionListener {

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
    private int CENTER = (WIDTH/2);//initial screen center
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
    int speed;
    int delay = DELAY;             //speed and delay for animation
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

    //private Vector<Rectangle> Walls = new Vector<Rectangle>();
    
    public BouncingBall() {
        setTitle("Bouncing Ball");
       // setLayout(new BorderLayout()); // Set the layout for the main frame to BorderLayout
       //setLayout(null);
        //setVisible(true);

        runBall = true;
        started = false;
        TimerPause = true;
        tailSet = false;
        //where to put this??
        //Perimeter.grow(-1, -1); // shrink the rectangle one pixel on all sides
        
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
        Stop = new Button("Pause");
        Quit = new Button("Quit");

        Stop.setEnabled(false); // Disable Pause button initially
    //set delay
        delay=100-SPEED+1;

         // Initialize Scrollbars (Horizontal)
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

        // Initialize the control panel layout (GridBagLayout)
        control.setLayout(new GridBagLayout());
        m1.setLocation(0, 0);
        m2.setLocation(0, 0);
    
        // Set Perimeter bounds
        Perimeter.setBounds(0, 0, ScreenWidth, ScreenHeight);

        setLayout(new BorderLayout()); // Set the layout for the main frame to BorderLayout
        setBounds(WinLeft, WinTop, FrameSize.x, FrameSize.y); // Set the frame bounds
        setBackground(Color.lightGray); // Set the background color for the frame
        setVisible(true); // Make the frame visible
        
        control.setLayout(new GridBagLayout());  // Use GridBagLayout for control panel
        control.setSize(FrameSize.x, 2*BUTTONH); // Set the size of the control panel

        // Add button panel to the control panel (Buttons will be centered in GridBagLayout)
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.insets = new Insets(5, 5, 5, 5);
        buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

        //ADDS RUN
        buttonPanelConstraints.gridx = 1;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.gridwidth = 1; // Span across columns
        buttonPanelConstraints.weightx = 1;  // Let it expand if needed
        control.add(Start, buttonPanelConstraints);
        //ADDS PAUSE
        buttonPanelConstraints.gridx = 2;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.gridwidth = 1; // Span across columns
        buttonPanelConstraints.weightx = 1;  // Let it expand if needed
        control.add(Stop, buttonPanelConstraints);
        //ADDS QUIT
        buttonPanelConstraints.gridx = 3;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.gridwidth = 1; // Span across columns
        buttonPanelConstraints.weightx = 1;  // Let it expand if needed
        control.add(Quit, buttonPanelConstraints);
       

        // Add Scrollbars to the control panel (East and West positions)
        GridBagConstraints sbConstraints = new GridBagConstraints();
        sbConstraints.insets = new Insets(5, 5, 5, 5);
        sbConstraints.fill = GridBagConstraints.HORIZONTAL;
    

        // Left position (SpeedScrollBar) - Place it at the far left side of the row
        sbConstraints.gridx = 0;  // Far left side of the row
        sbConstraints.gridy = 0;  // New row for the scrollbars
        sbConstraints.gridwidth = 1;  // Only one column wide
        sbConstraints.weightx = 1;  // Let it expand if needed
        control.add(SpeedScrollBar, sbConstraints);

        sbConstraints.gridy = 1;  // Same row as SpeedScrollBar
        control.add(SPEEDL, sbConstraints);
    

        // Right position (BallSizeScrollBar) - Place it at the far right side of the row
        sbConstraints.gridx = 4;  // Far right side of the row
        sbConstraints.gridy = 0;  // Same row as SpeedScrollBar
        sbConstraints.gridwidth = 1;  // Only one column wide
        sbConstraints.weightx = 1;  // Let it expand if needed
        control.add(BallSizeScrollBar, sbConstraints);
        
        sbConstraints.gridy = 1;  // Same row as SizeScrollBar
        control.add(SIZEL, sbConstraints);

        sheet.setLayout(new BorderLayout(0,0));
        Ball = new Ballc(SBall, Screen);
        Ball.setBackground(Color.white);
        sheet.add("Center", Ball);
        sheet.setVisible(true);

        add("Center", sheet);
        add("South", control);

        validate();

        // Add action listeners
        Start.addActionListener(this);
        Stop.addActionListener(this);
        Quit.addActionListener(this);
        SpeedScrollBar.addAdjustmentListener(this);
        BallSizeScrollBar.addAdjustmentListener(this);
        this.addWindowListener(this);
        this.addComponentListener(this);
        Ball.addMouseListener(this);
        Ball.addMouseMotionListener(this);
    }
    


    //creates layout and calculates screen size
    private void MakeSheet(){
        I=getInsets();
        ScreenWidth=WinWidth-I.left-I.right;
        ScreenHeight=WinHeight-I.top-2*(BUTTONH+BUTTONHS)-I.bottom;
        
        setSize(WinWidth,WinHeight);
        CENTER=(ScreenWidth/2);
    }


     //positions and sizes buttons, scrollbars, and labels on the screen
    private void SizeScreen(){

        Ball.setBounds(0,0,ScreenWidth,ScreenHeight);
        //Ball.setBounds(0,0,50000,50000);
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
                       
             TimerPause = false; //set timer to false to start the thread
             
         }
         //--------End Start Button-----------------
 
         //--------Pause Button-----------------
         if(source==Stop){
 
 
             Start.setEnabled(true);
             Stop.setEnabled(false);        
           
             TimerPause = true; //set timer to false to start the thread
         
 
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
    
        // Ball Size Scrollbar
        if (sb == BallSizeScrollBar) {
            // Get the new size value from the scrollbar
            int TS = e.getValue();
            TS = (TS / 2) * 2 + 1; // Ensure TS is odd
            int half = (TS - 1) / 2;
            
    
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS <= ScreenWidth && TS <= ScreenHeight) {
                // Update the Ball size if it fits
                Ball.updateSize(TS);
            } else {
                // Revert the scrollbar value if the size doesn't fit
                BallSizeScrollBar.setValue(TS); // Set back to previous valid size
            }
    
            Rectangle b = new Rectangle();
            b.setBounds(Ball.getX() - half - 1, Ball.getY() - half - 1, TS + 2, TS + 2);
    
            if (b.equals(Perimeter.intersection(b))) {
                // Ball is within the perimeter
                int i = 0;
                boolean ok = true;
                while ((i < Ball.getWallSize()) && ok) {
                    Rectangle t = Ball.getOne(i);
                    if (t.intersects(b)) {
                        ok = false;
                    }
                    i++;
                }
    
                if (ok) {
                    SBall = TS;
                    Ball.updateSize(SBall);
                    BallSizeScrollBar.setValue(TS);
                }
            } else {
                // Ball is beyond the perimeter
                BallSizeScrollBar.setValue(TS); // Set back to previous valid size
            }
        }
    
        // Repaint the Ball after the size change
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
     public void mouseReleased(MouseEvent e) {
        Rectangle b = new Rectangle(Ball.getBounds());
        b.grow(1, 1);
    
        if (!Perimeter.contains(db.x, db.y, db.width, db.height)) {
            db = Perimeter.intersection(db);
        }
    
        // Check if the new rectangle is covered by any rectangle in the Vector
        boolean covered = false;
        Rectangle r = new Rectangle();
        // if (r ==null) {
        //     r = new Rectangle();
        // }
        
        for (int i = 0; i < Ball.getWallSize(); i++) {
            r = Ball.getOne(i);
            if (r.contains(db)) {
                covered = true;
                break;
            }
        }

        // create a rectangle for the Ball's position
    Rectangle ballRectangle=new Rectangle(Ball.getXPos(),Ball.getYPos(),SBall,SBall);
    // if new rectangle intersects ball it can't be created, so covered=true
       if(db.intersects(ballRectangle))
       {
        covered=true;
       }
        // If not covered, store the rectangle
        if (!covered) {
            System.out.println("Adding rectangle: " + db);
            Ball.addOne(new Rectangle(db));
        } else {
            System.out.println("Rectangle is covered: " + db);
        }
    
        // Check if the new rectangle covers any rectangle in the Vector and delete the covered rectangle
        for (int i = 0; i < Ball.getWallSize(); i++) {
                r = Ball.getOne(i);
            if (!r.equals(db) && db.contains(r)) {
                System.out.println("Removing covered rectangle: " + r);
                Ball.removeOne(i);
                i--; // Adjust index after removal
            }
        }
        db.setBounds(0, 0, 0, 0);
        Ball.setDragBox(db);
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
        //----------debugging checks------------------------------------------------------------------
         if (Ball.getWallSize() == 0) {
            System.out.println("No rectangles to resize, skipping componentResized.");
            return; // Do nothing if there are no rectangles
        }

        Rectangle firstRect = Ball.getOne(0);
        if (firstRect == null) {
            return; // Exit if firstRect is null (extra safety check)
        }
        //----------end debugging checks--------------------------------------------------------------

        r.setBounds(firstRect);

 
         //r.setBounds(Ball.getOne(0));
         mr = r.x + r.width;
         mb = r.y + r.height;
 
         // Use a for loop to process the remaining elements of the Vector
         for (int i = 1; i < Ball.getWallSize(); i++) {
            // Rectangle rect = Ball.getOne(i);
            // if(rect!=null){
            //     r.setBounds(rect); // get ith rectangle
            //     mr = Math.max((r.x + r.width), mr); // keep max right
            //     mb = Math.max((r.y + r.height), mb); // keep max bottom}
            // }
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
         Perimeter.setBounds(0, 0, Screen.x, Screen.y);
         Perimeter.grow(-1, -1); // shrink the rectangle one pixel on all sides
 
         // Send the new Screen size to the Ball Ballect
         Ball.reSize(Screen.x, Screen.y); // resize the ball screen
 
         //MakeSheet();
         //SizeScreen();
         //Ball.reSize(this.getWidth(), this.getHeight());  // resize the ball screen
         Ball.repaint(); // repaint
    
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
         private int Screen;
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
         public Ballc(int SB,Point screen){//int w, int h){
            //  ScreenWidth=w; 
            //  ScreenHeight=h;
            ScreenWidth = screen.x;  // Use screen.x for width
            ScreenHeight = screen.y; 
            //  ScreenWidth = screen.getWidth();
            // ScreenHeight = screen.getHeight();
             SBall=SB;
             rect=true;
             clear=false;
         
             x = (SBall / 2)+1; //calculate offset for x
             y = (SBall / 2)+1; //calculate offset for y
             dx = 1;//set initial x flags to true
             dy = 1;//set initial y flags to true
         }
         
         //mutators
         public void setDragBox(Rectangle r){
             dragrec.setBounds(r);
         }
 
         public void addOne(Rectangle r){
             Walls.addElement(r);
         }
 
         public void removeOne(int i){
             Walls.removeElementAt(i);
         }

         public Rectangle getOne(int i){
             //return Walls.elementAt(i);
             if (i >= 0 && i < Walls.size()) {
                return Walls.elementAt(i);
            }
            return null; // Return null if index is out of bounds
            //return new Rectangle(0,0,0,0);
            //return DEFAULT_RECTANGLE;
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
             
            int half = SBall/2;

            // Ensure the ball stays inside new screen dimensions
            if (x + half > newScreenWidth) {
                x = newScreenWidth - half;
            }
            if (y + half > newScreenHeight) {
                y = newScreenHeight -half;
            }

            if (x < 0) x = half;
            if (y < 0) y = half;
 
         }
         
         //resizes the screen
         public void reSize(int w, int h){
             ScreenWidth=w;
             ScreenHeight=h;
             stayInBounds(w, h);
             repaint();
             
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
 
    
        @Override
        public void paint(Graphics cg) {
            Rectangle temp;

            // Get the current width and height of the component
            int width = getWidth();
            int height = getHeight();

            // Create a new buffer image with the updated dimensions
            buffer = createImage(width, height);  
            if (g != null) {
                g.dispose();
            }
            g = buffer.getGraphics();

            // Draw the ball
            g.setColor(Color.red);
            g.fillOval(Ball.getXPos(), Ball.getYPos(), Ball.getSizeBall(), Ball.getSizeBall());
            g.setColor(Color.black);
            g.drawOval(Ball.getXPos(), Ball.getYPos(), Ball.getSizeBall(), Ball.getSizeBall());

            // Draw the boundary rectangle
            g.setColor(Color.blue);
            g.drawRect(0, 0, width - 1, height - 1);

            // Draw the walls
            for (int i = 0; i < Walls.size(); i++) {
                temp = Walls.elementAt(i);
                g.setColor(Color.black);
                g.fillRect(temp.x, temp.y, temp.width, temp.height);
               // System.out.println("drawing");
            }

            // Draw the draggable rectangle
            g.drawRect(dragrec.x, dragrec.y, dragrec.width, dragrec.height);

            // Draw the buffered image onto the component
            cg.drawImage(buffer, 0, 0, null);
        }

 
         //UPDATE graphics for the Ballc
         public void update(Graphics g){
             
           paint(g);
         }
 
        private void move(){
            // Get the current screen size dynamically
            int width = getWidth();
            int height = getHeight();

            // Update ball position
            x += dx;
            y += dy;

        // Check for collisions with the screen boundaries and change directions
            if (y + SBall >= height || y <= 0) {
                dy = -dy;
            }
            if (x + SBall >= width || x <= 0) {
                dx = -dx;
            }
    
            // Create a rectangle representing the ball's current position
            Rectangle b = new Rectangle(x, y, SBall, SBall);
            b.grow(1, 1);

            // Check for collisions with the rectangles
            for (Rectangle rect : Walls) {
                Rectangle leftEdge = new Rectangle(rect.x - 1, rect.y, 1, rect.height);
                Rectangle rightEdge = new Rectangle(rect.x + rect.width, rect.y, 1, rect.height);
                Rectangle topEdge = new Rectangle(rect.x, rect.y - 1, rect.width, 1);
                Rectangle bottomEdge = new Rectangle(rect.x, rect.y + rect.height, rect.width, 1);

                if (b.intersects(leftEdge) || b.intersects(rightEdge)) {
                    dx = -dx;
                    break;
                }

                if (b.intersects(topEdge) || b.intersects(bottomEdge)) {
                    dy = -dy;
                    break;
                }
            }
            // Repaint the screen
            repaint();
        }
        
         //moves the object within the screens boundaries
         public Rectangle isTouching(){
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
                     Thread.sleep(1);
                 } catch (InterruptedException e) {}
 
                 if(!TimerPause)
                 {
                     started = true;
                     try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {}

                    Ball.isTouching(); //check for collisions
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
 
 
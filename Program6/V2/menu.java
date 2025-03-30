import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class menu extends Frame implements ActionListener, WindowListener, ItemListener, ComponentListener, AdjustmentListener, Runnable, MouseListener,MouseMotionListener {

    
    //---------------------------------MENU FRAME-------------------------------------------------------------------
    private int sw = 650, sh=480;//screen witdh and height
    private Frame EditorFrame;//create object frame
    private TextArea EditArea;//create area for typing
    
    private MenuBar MMB;//menu bar
    private Menu CONTROL,PARAMETERS,ENVIRONMENT;//main items on the menu bar
    private Menu  SIZE, SPEED;//sub menu items under items

    private MenuItem PAUSE,RUN,RESTART;//terminal items in new menu
    private MenuItem QUIT;//menu item

    private CheckboxMenuItem Sxs,Ss,Sm,Sl,Sxl;//checkbox menu items for size
    private CheckboxMenuItem MERCURY,VENUS,EARTH,MARS,JUPITER,SATURN,URANUS,NEPTUNE,PLUTO;//checkboxes for Environment
    private CheckboxMenuItem XS,SLOW,MEDIUM,FAST,XF;//checkbox menu items for font    

    private int FontType = Font.PLAIN;
    private String FontStyle = "Times New Roman";
    private int FontSize = 14;
    //---------------------------------END MENU FRAME-----------------------------------------------------------------

    //-----------------------------------RECTANGLE-----------------------------------------------------------------
    private static final long serialVersionUID = 10L;

    //constants
    private final int WIDTH=640; //initial frame width //out for prog 5
    private final int HEIGHT=400;//initial frame height //out for prog 5
    

    private final int BUTTONH=20;//button height
    private final int BUTTONHS=5;//button height spacing

    private final int MAXBall=100; //max Ballect size
    private final int MINBall=10;//min Ballect size
    private final int SPEEDD=50;//initial speed
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
    private int SpeedSBinit=SPEEDD;//initial speed scrollbar value
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

    //Button Start,Stop, Quit; //Buttons

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
    //private Rectangle Perimeter = new Rectangle(0, 0, ScreenWidth, ScreenHeight); // bouncing perimeter
    private Rectangle Perimeter = new Rectangle(0, 0, ScreenWidth, ScreenHeight); // bouncing perimeter
    private Rectangle db = new Rectangle(); // drag box rectangle
    private static final Rectangle ZERO = new Rectangle(0, 0, 0, 0); // zero rectangle
    Rectangle temp = new Rectangle(); // temporary rectangle
    //-----------------------------------RECTANGLE-----------------------------------------------------------------

    //----------------------MAIN METHOD-----------------
    public static void main(String[] args) {
        new menu();
    }
    //---------------------END MAIN METHOD-----------------

    public menu(){
        //setTitle("Menu");

        runBall = true;
        started = false;
        TimerPause = true;
        tailSet = false;
        
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

        EditArea = new TextArea("",sw-10,sh-10, TextArea.SCROLLBARS_BOTH);

        EditorFrame = new Frame("");
        EditorFrame.setLayout(new BorderLayout(0,0));
        EditorFrame.setBackground(Color.lightGray);
        EditorFrame.setForeground(Color.black);
        EditorFrame.add("Center", EditArea);

        MMB = new MenuBar();//create menu bar
        CONTROL = new Menu("Control");//create first menu entry for menu bar
        PAUSE = new MenuItem("Pause",new MenuShortcut(KeyEvent.VK_P));//create first entry for SLOWNTROL menu
        RUN=new MenuItem("Run",new MenuShortcut(KeyEvent.VK_R));
        RESTART=new MenuItem("Restart");


        //add and finish the first menu bar enrty SLOWNTROL
        CONTROL.add(PAUSE);
        CONTROL.add(RUN);
        CONTROL.add(RESTART);
        CONTROL.addSeparator();
        //add menu item quit with short cut key to menu entry SLOWNTROL
        QUIT = new MenuItem("Quit");
        CONTROL.add(QUIT);


        PARAMETERS = new Menu("Parameters"); //create seSLOWnd menu entry for menu bar
        SIZE = new Menu("Size");//create first entry for PARAMETERS menu
        SPEED = new Menu("Speed");//create seSLOWnd entry for PARAMETERS menu

        SIZE.add(Sxs = new CheckboxMenuItem("x-small"));//checkbox menuitem size x-small
        SIZE.add(Ss = new CheckboxMenuItem("small"));//checkbox menuitem size small
        SIZE.add(Sm = new CheckboxMenuItem("medium"));//checkbox menuitem size medium  
        SIZE.add(Sl = new CheckboxMenuItem("large"));//checkbox menuitem size large
        SIZE.add(Sxl = new CheckboxMenuItem("x-large"));//checkbox menuitem size x-large

        Ss.setState(true); //initialize checkbox selection to 14
        PARAMETERS.add(SIZE);//add size menu to PARAMETERS menu

        SPEED.add(XS = new CheckboxMenuItem("x-slow"));//checkbox menuitem font Times New Roman
        SPEED.add(SLOW = new CheckboxMenuItem("slow"));//checkbox menuitem font SLOWurier
        SPEED.add(MEDIUM = new CheckboxMenuItem("medium"));
        SPEED.add(FAST = new CheckboxMenuItem("fast"));
        SPEED.add(XF = new CheckboxMenuItem("x-fast"));

        SLOW.setState(true);//initialize checkbox selection to Times New Roman
        PARAMETERS.add(SPEED);//add font menu to PARAMETERS menu

        ENVIRONMENT=new Menu("Environment");

        ENVIRONMENT.add(MERCURY = new CheckboxMenuItem("Mercury"));
        ENVIRONMENT.add(VENUS = new CheckboxMenuItem("Venus"));
        ENVIRONMENT.add(EARTH = new CheckboxMenuItem("Earth"));
        ENVIRONMENT.add(MARS = new CheckboxMenuItem("Mars"));
        ENVIRONMENT.add(JUPITER = new CheckboxMenuItem("Jupiter"));
        ENVIRONMENT.add(SATURN = new CheckboxMenuItem("Saturn"));
        ENVIRONMENT.add(URANUS = new CheckboxMenuItem("Uranus"));
        ENVIRONMENT.add(NEPTUNE = new CheckboxMenuItem("Neptune"));
        ENVIRONMENT.add(PLUTO = new CheckboxMenuItem("Pluto"));

        EARTH.setState(true);
        
        MMB.add(CONTROL);//add SLOWNTROL menu to menu bar
        MMB.add(PARAMETERS);//add PARAMETERS menu to menu bar
        MMB.add(ENVIRONMENT);//add ENVIRONMENT menu to menu bar

        //turn on action listeners for menuitems, menushortcuts, and checkbox menuitems
        QUIT.addActionListener(this);
        Sxs.addItemListener(this);
        Ss.addItemListener(this);
        Sm.addItemListener(this);
        Sl.addItemListener(this);
        Sxl.addItemListener(this);
        MERCURY.addItemListener(this);
        VENUS.addItemListener(this);
        EARTH.addItemListener(this);
        MARS.addItemListener(this);
        JUPITER.addItemListener(this);
        SATURN.addItemListener(this);
        URANUS.addItemListener(this);
        NEPTUNE.addItemListener(this);
        PLUTO.addItemListener(this);
        XS.addItemListener(this);
        SLOW.addItemListener(this);
        MEDIUM.addItemListener(this);
        FAST.addItemListener(this);
        XF.addItemListener(this);



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
        //Perimeter.setBounds(0, 0, ScreenWidth, ScreenHeight);
        Perimeter.setBounds(0, 0, Screen.x, Screen.y);
        Perimeter.grow(-1, -1); // Shrink the rectangle one pixel on all sides

        //setLayout(new BorderLayout()); // Set the layout for the main frame to BorderLayout
        //setBounds(WinLeft, WinTop, FrameSize.x, FrameSize.y); // Set the frame bounds5
        //setBackground(Color.lightGray); // Set the background color for the frame
        //setVisible(true); // Make the frame visible
        
        control.setLayout(new GridBagLayout());  // Use GridBagLayout for control panel
        control.setSize(FrameSize.x, 2*BUTTONH); // Set the size of the control panel

        // Add button panel to the control panel (Buttons will be centered in GridBagLayout)
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.insets = new Insets(5, 5, 5, 5);
        buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

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


        //---------------Ball ------------------
        // Ball = new Ballc(SBall, Screen);
        // Ball.setBackground(Color.white);

        // EditorFrame.add(Ball, BorderLayout.CENTER);
        // EditorFrame.setVisible(true);
        sheet.setLayout(new BorderLayout(0,0));
        Ball = new Ballc(SBall, Screen);
        Ball.setBackground(Color.white);
        sheet.add("Center", Ball);
        sheet.setVisible(true);

        EditorFrame.add("Center", sheet);
        EditorFrame.add("South", control);
        //------------------------------------


        SpeedScrollBar.addAdjustmentListener(this);
        BallSizeScrollBar.addAdjustmentListener(this);
        this.addWindowListener(this);
        this.addComponentListener(this);
        Ball.addMouseListener(this);
        Ball.addMouseMotionListener(this);
        //-----------------End Ball ------------------


        EditorFrame.setMenuBar(MMB);//add menu bar to frame 
        EditorFrame.addWindowListener(this);//add window listener to frame          
        EditorFrame.setSize(sw,sh);//set frame size
        EditorFrame.setResizable(true);//set frame resizable
        EditorFrame.setVisible(true);//set frame visible
        EditorFrame.validate();

        setTheFont();//set the font to the default font ????????
    }


    //creates layout and calculates screen size
    private void MakeSheet(){
        I=getInsets();
        ScreenWidth=WinWidth-I.left-I.right;
        ScreenHeight = WinHeight - I.top - control.getHeight() - I.bottom;
        
        setSize(WinWidth,WinHeight);
        CENTER=(ScreenWidth/2);
    }

     //positions and sizes buttons, scrollbars, and labels on the screen
    private void SizeScreen(){ 

        Ball.setBounds(0,0,ScreenWidth,ScreenHeight);
    }

    public void itemStateChanged(ItemEvent e) {
        
        CheckboxMenuItem checkbox = (CheckboxMenuItem)e.getSource();

        if(checkbox == Sxs||checkbox == Ss||checkbox == Sm){
            Sxs.setState(false);
            Ss.setState(false);
            Sm.setState(false);
            checkbox.setState(true);
        }
        if(checkbox == XS||checkbox == SLOW){
            XS.setState(false);
            SLOW.setState(false);
            checkbox.setState(true);
        } 
        setTheFont();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source == QUIT){
            stop();
        }

        if(source==RUN){
            TimerPause = false; //set timer to false to start the thread
        }
    
    }

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
                Ball.stayInBounds(ScreenWidth, ScreenHeight); // Ensure ball remains in bounds
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

    public void stop(){

        //stop the Ballc movement
        runBall = false; 

        QUIT.removeActionListener(this);
        Sxs.removeItemListener(this);
        Ss.removeItemListener(this);       
        Sm.removeItemListener(this);
        XS.removeItemListener(this);
        SLOW.removeItemListener(this);
        EditorFrame.removeWindowListener(this);

        //removes component and window listeners
        this.removeComponentListener(this);
        //this.removeWindowListener(this);
        this.removeMouseMotionListener(this);
        this.removeMouseListener(this); 

        //removes adjustment listeners from scrollbars
        SpeedScrollBar.removeAdjustmentListener(this);
        BallSizeScrollBar.removeAdjustmentListener(this);

        EditorFrame.dispose();
        thethread.interrupt();

        //terminates the program
        System.exit(0);
    }

    //SHOULDN'T NEED THIS BUT MIGHT BE NICE START FOR NEEDED FUNCTION
    public void setTheFont(){
        //set the font to the selected font
        FontSize = 10;
        if(Sxs.getState() == true){
            FontSize = 10;
        }
        if(Ss.getState() == true){
            FontSize = 14;
        }
        if(Sm.getState() == true){
            FontSize = 18;
        }   

        FontStyle = "TimesNewRoman";
        if(XS.getState()== true){
            FontStyle = "Times New Roman";
        }
        if(SLOW.getState()== true){
            FontStyle = "Courier";
        }
        EditArea.setFont(new Font(FontStyle, FontType, FontSize));
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
    public void mouseDragged(MouseEvent e) {
        Rectangle newDb = getDragBox(e);
        if (!newDb.equals(db)) {  // Only update if drag box actually changed
            db.setBounds(newDb);
            if (Perimeter.contains(db)) {
                Ball.setDragBox(db);
                Ball.repaint();
            } else {
                db.setBounds(Perimeter.intersection(db)); // Force it inside
            }
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
            //System.out.println("Adding rectangle: " + db);//debugging
            Ball.addOne(new Rectangle(db));
        } else {
            //System.out.println("Rectangle is covered: " + db);//debugging
        }
    
        // Check if the new rectangle covers any rectangle in the Vector and delete the covered rectangle
        for (int i = 0; i < Ball.getWallSize(); i++) {
                r = Ball.getOne(i);
            if (!r.equals(db) && db.contains(r)) {
                //System.out.println("Removing covered rectangle: " + r);//debugging
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
                if (b.contains(p)) {
                Ball.removeOne(i);
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

        // Ensure that the window size is correctly taken into account during dragging
        int currentWidth = getWidth();
        int currentHeight = getHeight();

        int x = Math.min(m1.x, m2.x);
        int y = Math.min(m1.y, m2.y);
        int width = Math.abs(m1.x - m2.x);
        int height = Math.abs(m1.y - m2.y);

        // Keep the drag box within the bounds of the updated window size
        width = Math.min(width, currentWidth - x);
        height = Math.min(height, currentHeight - y);

        // Ensure width and height are at least 1
        width = Math.max(1, width);
        height = Math.max(1, height);

        return new Rectangle(x, y, width, height); 
    }


    public void componentResized(ComponentEvent e) {
        // Update the window width and height based on the current size
        WinWidth = getWidth();
        WinHeight = getHeight();

        // Ensure that width and height are always positive
        if (WinWidth <= 0 || WinHeight <= 0) {
            return; // Prevents resizing issues
        }

        // Update the Perimeter bounds to match the window size
        Perimeter.setBounds(0, 0, WinWidth, WinHeight);
        Perimeter.grow(-1, -1); // Shrink the rectangle one pixel on all sides

        // Define expansion and insets for screen adjustment
        int EXPAND = 10; // Small border beyond the rectangles
        int lw = I.left + I.right;
        int lh = I.top + I.bottom;

        // Adjust the screen size if necessary
        int mr = 0, mb = 0;

        // Loop through the Ball objects and calculate the max right and bottom values
        for (int i = 0; i < Ball.getWallSize(); i++) {
            Rectangle r = Ball.getOne(i);

            if (r != null) {
                mr = Math.max((r.x + r.width), mr); // Update max right
                mb = Math.max((r.y + r.height), mb); // Update max bottom

                // Ensure rectangles are within the bounds
                if (r.width < 0) {
                    r.x += r.width;
                    r.width = -r.width;
                }
                if (r.height < 0) {
                    r.y += r.height;
                    r.height = -r.height;
                }

                // Make sure rectangles stay within the new window bounds
                r.width = Math.max(1, r.width);
                r.height = Math.max(1, r.height);
                r.x = Math.min(r.x, Math.max(0, getWidth() - r.width));
                r.y = Math.min(r.y, Math.max(0, getHeight() - r.height));
            }
        }

        // Resize the window if necessary
        if (mr > Screen.x || mb > Screen.y) {
            setSize(Math.max((mr + EXPAND), Screen.x) + lw, Math.max((mb + EXPAND), Screen.y) + lh + 2 * BUTTONH);
        }
        

        // Ensure the drag box stays visible
        if (db.width > 0 && db.height > 0) {
            db.setBounds(Perimeter.intersection(db));
        }

        // Send the new screen size to the Ball object
        Ball.reSize(Screen.x, Screen.y);

        // Rebuild the sheet and update screen size
        MakeSheet();
        SizeScreen();

        // Repaint the Ball object to reflect the new size
        Ball.repaint();
    }


    public void componentHidden(ComponentEvent e){}

    public void componentShown(ComponentEvent e){}
    
    public void componentMoved(ComponentEvent e){}

 //=================END COMPONENT METHODS=========




//========================CLASS CANVAS=================================
     //create a class to draw the canvas for the frame
      class Ballc extends Canvas{
 
         // declare/initialize variables
         private static final long serialVersionUID=11L;
        //  private int ScreenWidth;
        //  private int ScreenHeight;
         private int SBall;
        //  private int Screen;
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
            ScreenWidth = screen.x;  // Use screen.x for width
            ScreenHeight = screen.y; 
          
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
 
        public void addOne(Rectangle r) {
            if (r.width < 0) {
                r.x += r.width;  // Shift x to correct position
                r.width = -r.width;  // Make width positive
            }
            if (r.height < 0) {
                r.y += r.height;  // Shift y to correct position
                r.height = -r.height;  // Make height positive
            }
            Walls.addElement(r);
            //System.out.println("Corrected & added rectangle: " + r);//debugging
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
        }
 
        
        public void stayInBounds(int newScreenWidth, int newScreenHeight) {
             
            int half = SBall/2;

            // Ensure the ball stays inside new screen dimensions (right and bottom edges)
            if (x + SBall > newScreenWidth) {
                x = newScreenWidth - SBall;  // Position the ball at the right edge
                dx = -Math.abs(dx); // Reverse direction
            }
            if (y + SBall > newScreenHeight) {
                y = newScreenHeight - SBall;  // Position the ball at the bottom edge
                dy = -Math.abs(dy); // Reverse direction
            }

            // Ensure the ball doesn't go beyond the left and top edges
            if (x < 0) {
                x = 0;  // Position the ball at the left edge
                dx = -Math.abs(dx); // Reverse direction
            }
            if (y < 0) {
                y = 0;  // Position the ball at the top edge
                dy = -Math.abs(dy); // Reverse direction
            }
         }
          
         //resizes the screen
         public void reSize(int w, int h){
             ScreenWidth=w;
             ScreenHeight=h;

            for (Rectangle r : Walls){
                r.x = Math.min(r.x, w - r.width);
                r.y = Math.min(r.y, h - r.height);
            }
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

            g.setColor(Color.orange);
            // Draw the walls
            for (int i = 0; i < Walls.size(); i++) {
                temp = Walls.elementAt(i);
                g.setColor(Color.orange);
                g.fillRect(temp.x, temp.y, temp.width, temp.height);
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
     //=========================END CLASS CANVAS============================




}

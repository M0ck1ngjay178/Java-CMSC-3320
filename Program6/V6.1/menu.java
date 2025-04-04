import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class menu implements ActionListener, WindowListener, ItemListener, ComponentListener, AdjustmentListener, Runnable, MouseListener,MouseMotionListener {

    
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
    private int xsSize=10;
    private int sSize=30;
    private int mSize=50;
    private int lSize=70;
    private int xlSize=90;

    private CheckboxMenuItem MERCURY,VENUS,EARTH,MARS,JUPITER,SATURN,URANUS,NEPTUNE,PLUTO;//checkboxes for Environment
    private CheckboxMenuItem XS,SLOW,MEDIUM,FAST,XF;//checkbox menu items for font    
    private int xsSPEED=10;
    private int sSPEED=30;
    private int mSPEED=50;
    private int fSPEED=70;
    private int xfSPEED=90;

    //Polygon points
    private int angle;
    //private Point a= new Point();

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
    private final int MINBall=0;//min Ballect size
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
    private Ballc Projectile; //Projectile to draw
    private boolean ProjectileActive = false; // Flag to track if a Projectile is active

    private Label SPEEDL= new Label("Velocity", Label.CENTER); // label for speed scroll bar
    private Label AngleL=new Label("Angle",Label.CENTER);// label for size scroll bar
    Scrollbar SpeedScrollBar, AngleScrollBar;//scrollbars
    Label Time=new Label("Time: ");
    Label ballScore=new Label("Ball: ");
    Label playerScore=new Label("Player: ");

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


    Polygon cannon=new Polygon();

    private double gravity;
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
        
        try {
            initComponents();
         MakeSheet();
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
        RUN.addActionListener(this);
        PAUSE.addActionListener(this);
        RESTART.addActionListener(this);
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
     
       
        AngleScrollBar = new Scrollbar(Scrollbar.HORIZONTAL);
        AngleScrollBar.setMaximum(100);
        AngleScrollBar.setMinimum(0);
        AngleScrollBar.setUnitIncrement(SBunit);
        AngleScrollBar.setBlockIncrement(SBblock);
        AngleScrollBar.setValue(90);
        AngleScrollBar.setVisibleAmount(SBvisible);
        AngleScrollBar.setBackground(Color.gray);

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
    
        sbConstraints.gridx=1;
        sbConstraints.gridy=0;
        control.add(Time,sbConstraints);

        sbConstraints.gridx=2;
        sbConstraints.gridy=0;
        control.add(ballScore,sbConstraints);

        sbConstraints.gridx=3;
        sbConstraints.gridy=0;
        control.add(playerScore,sbConstraints);

        // Right position (AngleScrollBar) - Place it at the far right side of the row
        sbConstraints.gridx = 4;  // Far right side of the row
        sbConstraints.gridy = 0;  // Same row as SpeedScrollBar
        sbConstraints.gridwidth = 1;  // Only one column wide
        sbConstraints.weightx = 1;  // Let it expand if needed
        control.add(AngleScrollBar, sbConstraints);
        
        sbConstraints.gridy = 1;  // Same row as SizeScrollBar
        control.add(AngleL, sbConstraints);


        //---------------Ball ------------------
        // Ball = new Ballc(SBall, Screen);
        // Ball.setBackground(Color.white);

        // EditorFrame.add(Ball, BorderLayout.CENTER);
        // EditorFrame.setVisible(true);
        sheet.setLayout(new BorderLayout(0,0));
        Ball = new Ballc(SBall, Screen, 0, 0); // Initialize Ball with size and screen
        Ball.setBackground(Color.white);
        sheet.add("Center", Ball);
        sheet.setVisible(true);

        EditorFrame.add("Center", sheet);
        //add("Center", sheet);

        EditorFrame.add("South", control);
        //------------------------------------

        


        SpeedScrollBar.addAdjustmentListener(this);
        AngleScrollBar.addAdjustmentListener(this);
        //this.addWindowListener(this);
        EditorFrame.addWindowListener(this);
        EditorFrame.addComponentListener(this);
        Ball.addMouseListener(this);
        Ball.addMouseMotionListener(this);
        //-----------------End Ball ------------------


        EditorFrame.setMenuBar(MMB);//add menu bar to frame 
 //       EditorFrame.addWindowListener(this);//add window listener to frame
        //this.addWindowListener(this);//add window listener to frame   
        EditorFrame.setSize(sw,sh);//set frame size
        //EditorFrame.setResizable(true);//set frame resizable
//        ComponentEvent resizeEvent = new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED);
//        componentResized(resizeEvent);

        //this.setResizable(true);//set frame resizable
        EditorFrame.setVisible(true);//set frame visible
        EditorFrame.validate();

        setTheSize();//set the font to the default font ????????
    }


    //creates layout and calculates screen size
    private void MakeSheet(){
        I=EditorFrame.getInsets();
        ScreenWidth=WinWidth-I.left-I.right;
        ScreenHeight = WinHeight - I.top - control.getHeight() - I.bottom;
        
        EditorFrame.setSize(WinWidth,WinHeight);
        CENTER=(ScreenWidth/2);
    }

     //positions and sizes buttons, scrollbars, and labels on the screen
    private void SizeScreen(){ 

        Ball.setBounds(0,0,ScreenWidth,ScreenHeight);
    }

    public void itemStateChanged(ItemEvent e) {
        
        CheckboxMenuItem checkbox = (CheckboxMenuItem)e.getSource();

        if(checkbox == Sxs||checkbox == Ss||checkbox == Sm || checkbox==Sl || checkbox==Sxl){
            Sxs.setState(false);
            Ss.setState(false);
            Sm.setState(false);
            Sl.setState(false);
            Sxl.setState(false);
            checkbox.setState(true);
        }
        if(checkbox == XS||checkbox == SLOW || checkbox==MEDIUM ||checkbox==FAST || checkbox==XF){
            XS.setState(false);
            SLOW.setState(false);
            MEDIUM.setState(false);
            FAST.setState(false);
            XF.setState(false);
            checkbox.setState(true);
        }
        if(checkbox == MERCURY || checkbox == VENUS || checkbox == EARTH || checkbox == MARS || checkbox == JUPITER || checkbox == SATURN || checkbox == URANUS || checkbox == NEPTUNE || checkbox == PLUTO){
            MERCURY.setState(false);
            VENUS.setState(false);
            EARTH.setState(false);
            MARS.setState(false);
            JUPITER.setState(false);
            SATURN.setState(false);
            URANUS.setState(false);
            NEPTUNE.setState(false);
            PLUTO.setState(false);
            checkbox.setState(true);
            setGravity(checkbox.getLabel());
            System.out.println("Gravity set to: " + gravity);
        } 
        setTheSize();
        setTheSpeed();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source == QUIT){
            stop();
        }

        if(source== RUN){
            TimerPause = false; //set timer to false to start the thread
        }
        if(source== PAUSE){
            TimerPause = true; //set timer to true to pause the thread
        }
        if(source== RESTART){
            TimerPause = true; //set timer to false to start the thread
            //Ball.repaint();
            Ball.resetBall(0,0);
        }
    
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        Scrollbar sb = (Scrollbar) e.getSource(); // Get the scrollbar that triggered the event
    
        if (sb == SpeedScrollBar) {
            // Get the scrollbar value and map it to the velocity range (100 - 1200 ft/sec)
            int minVel = 100;
            int maxVel = 1200;
            
            int scrollValue = SpeedScrollBar.getValue();  // Get raw scrollbar value
            int velocity = minVel + (scrollValue * (maxVel - minVel)) / SpeedScrollBar.getMaximum(); 
            
            Ball.setVelocity(velocity); // Set the new velocity to the Ball
            System.out.println("Velocity set to: " + velocity);
        }

       
        if (sb == AngleScrollBar) {
            // Get the value of the angle from the scrollbar ( 0 to 360 degrees)
            Ball.setAngle(AngleScrollBar.getValue()); // Set the angle of the cannon using the scrollbar value
            //Repaint the cannon after the angle change
            Ball.repaint();
        }

        // Repaint the cannon after the angle change
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
        EditorFrame.removeComponentListener(this);
        //this.removeWindowListener(this);
        Ball.removeMouseMotionListener(this);
        Ball.removeMouseListener(this); 

        //removes adjustment listeners from scrollbars
        SpeedScrollBar.removeAdjustmentListener(this);
        AngleScrollBar.removeAdjustmentListener(this);

        EditorFrame.dispose();
        thethread.interrupt();

        //terminates the program
        System.exit(0);
    }

    public void setTheSpeed(){
        if(XS.getState()==true){
            // Recalculate the delay based on the speed value
            int speedValue = xsSPEED;
            delay = 100 - speedValue + 1; // Calculate delay
    
            // Interrupt the thread to apply the new delay
            if (thethread != null) {
                thethread.interrupt();
            }
        }
        if(SLOW.getState()==true){
            // Recalculate the delay based on the speed value
            int speedValue = sSPEED;
            delay = 100 - speedValue + 1; // Calculate delay
    
            // Interrupt the thread to apply the new delay
            if (thethread != null) {
                thethread.interrupt();
            }
        }
        if(MEDIUM.getState()==true){
            // Recalculate the delay based on the speed value
            int speedValue = mSPEED;
            delay = 100 - speedValue + 1; // Calculate delay
    
            // Interrupt the thread to apply the new delay
            if (thethread != null) {
                thethread.interrupt();
            }
        }
        if(FAST.getState()==true){
            // Recalculate the delay based on the speed value
            int speedValue = fSPEED;
            delay = 100 - speedValue + 1; // Calculate delay
    
            // Interrupt the thread to apply the new delay
            if (thethread != null) {
                thethread.interrupt();
            }
        }
        if(XF.getState()==true){
            // Recalculate the delay based on the speed value
            int speedValue = xfSPEED;
            delay = 100 - speedValue + 1; // Calculate delay
    
            // Interrupt the thread to apply the new delay
            if (thethread != null) {
                thethread.interrupt();
            }
        }
    }
    private void setGravity(String planet) {
        switch (planet) {
            case "Mercury": 
                gravity = 3.7; 
                break;
            case "Venus": 
                gravity = 8.87; 
                break;
            case "Earth": 
                gravity = 9.81; 
                break;
            case "Mars": 
                gravity = 3.71; 
                break;
            case "Jupiter": 
                gravity = 24.79; 
                break;
            case "Saturn":
                gravity = 10.44; 
                break;
            case "Uranus": 
                gravity = 8.69; 
                break;
            case "Neptune": 
                gravity = 11.15; 
                break;
            case "Pluto": 
                gravity = 0.62; 
                break;
            default: 
                gravity = 9.81; // Default to Earth
        }
    }


    public void setTheSize(){
        //set the font to the selected font
       // FontSize = 10;
        if(Sxs.getState() == true){
            // Get the new size value from the checkbox
            int TS = xsSize;
            TS = (TS / 2) * 2 + 1; // Ensure TS is odd
            int half = (TS - 1) / 2; 
    
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS <= ScreenWidth && TS <= ScreenHeight) {
                // Update the Ball size if it fits
                Ball.updateSize(TS);
                Ball.stayInBounds(ScreenWidth, ScreenHeight); // Ensure ball remains in bounds
            } else {
                Ball.updateSize(SBALL); // Set back to previous valid size
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
                }
            } else {
                // Ball is beyond the perimeter
                Ball.updateSize(TS); // Set back to previous valid size
            }
        }
        if(Ss.getState() == true){
            // Get the new size value from the checkbox
            int TS = sSize;
            TS = (TS / 2) * 2 + 1; // Ensure TS is odd
            int half = (TS - 1) / 2; 
    
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS <= ScreenWidth && TS <= ScreenHeight) {
                // Update the Ball size if it fits
                Ball.updateSize(TS);
                Ball.stayInBounds(ScreenWidth, ScreenHeight); // Ensure ball remains in bounds
            } else {
                Ball.updateSize(SBALL); // Set back to previous valid size
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
                }
            } else {
                // Ball is beyond the perimeter
                Ball.updateSize(TS); // Set back to previous valid size
            }
        }
        if(Sm.getState() == true){
            // Get the new size value from the checkbox
            int TS = mSize;
            TS = (TS / 2) * 2 + 1; // Ensure TS is odd
            int half = (TS - 1) / 2; 
    
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS <= ScreenWidth && TS <= ScreenHeight) {
                // Update the Ball size if it fits
                Ball.updateSize(TS);
                Ball.stayInBounds(ScreenWidth, ScreenHeight); // Ensure ball remains in bounds
            } else {
                Ball.updateSize(SBALL); // Set back to previous valid size
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
                }
            } else {
                // Ball is beyond the perimeter
                Ball.updateSize(TS); // Set back to previous valid size
            }
        }   

        if(Sl.getState() == true){
            // Get the new size value from the checkbox
            int TS = lSize;
            TS = (TS / 2) * 2 + 1; // Ensure TS is odd
            int half = (TS - 1) / 2; 
    
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS <= ScreenWidth && TS <= ScreenHeight) {
                // Update the Ball size if it fits
                Ball.updateSize(TS);
                Ball.stayInBounds(ScreenWidth, ScreenHeight); // Ensure ball remains in bounds
            } else {
                Ball.updateSize(SBALL); // Set back to previous valid size
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
                }
            } else {
                // Ball is beyond the perimeter
                Ball.updateSize(TS); // Set back to previous valid size
            }
        }   
        if(Sxl.getState() == true){
            // Get the new size value from the checkbox
            int TS = xlSize;
            TS = (TS / 2) * 2 + 1; // Ensure TS is odd
            int half = (TS - 1) / 2; 
    
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS <= ScreenWidth && TS <= ScreenHeight) {
                // Update the Ball size if it fits
                Ball.updateSize(TS);
                Ball.stayInBounds(ScreenWidth, ScreenHeight); // Ensure ball remains in bounds
            } else {
                Ball.updateSize(SBALL); // Set back to previous valid size
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
                }
            } else {
                // Ball is beyond the perimeter
                Ball.updateSize(TS); // Set back to previous valid size
            }
        }   
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

            int cannonCenterX = Ball.getWidth() - 37;
             int cannonCenterY = Ball.getHeight() - 37;
             int cannonRadius = 37;

             if (p.distance(cannonCenterX, cannonCenterY) <= cannonRadius) {
                if (!ProjectileActive) { // If no Projectile is active, launch a new one
                    System.out.println("click in cannon");
                    launchProjectile();// launchs the Projectile from cannon
                }
            }

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
    private void launchProjectile(){

        int cannonCenterX = Ball.getWidth() - 37;
        int cannonCenterY = Ball.getHeight() - 37;
        int velocity = Ball.getVelocity();
        int angle = Ball.getAngle(); // Get the angle from the cannon
        //int angle = Ball.cannonAngle;
        //double velocity = Ball.getVelocity();
        //double angle = Math.toRadians(Ball.cannonAngle); // Convert angle to radians

        Ball.v0x = velocity * Math.cos(angle); // X velocity
        Ball.v0y = velocity * Math.sin(angle); // Y velocity

        // Set initial position
        Ball.x0 = cannonCenterX;
        Ball.y0 = cannonCenterY;
        Ball.px = Ball.x0;
        System.out.println("Center of cannon: " + cannonCenterX + " " + cannonCenterY);
        System.out.println("Ball.x0 = " + Ball.x0);
        System.out.println("Ball.y0 = " + Ball.y0);
        Ball.py = Ball.y0;

        // Reset time
        Ball.time = 0;

        Projectile = new Ballc(SBall, Screen, cannonCenterX, cannonCenterY); // Create a new Projectile object


        System.out.println("Projectile launched");
        ProjectileActive = true;
        System.out.println("Projectile landed");
       // ProjectileActive = false;

        //dont have to call repaint any more the current ball will call paint enough
        //just add the projectile to the paint method so it will be updated as often as the 
        //current ball
    }
    
    public Rectangle getDragBox(MouseEvent e){
        m2.setLocation(e.getPoint());

        // Ensure that the window size is correctly taken into account during dragging
        int currentWidth = Ball.getWidth();
        int currentHeight = Ball.getHeight();

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
        // Ensure EditorFrame is not null
        if (EditorFrame == null) {
            System.err.println("Error: EditorFrame is null!");
            return;
        }
    
        // Get the current window size
        WinWidth = EditorFrame.getWidth();
        WinHeight = EditorFrame.getHeight();
    
        // Ensure window dimensions are valid
        if (WinWidth <= 0 || WinHeight <= 0) {
            System.err.println("Error: Invalid window size: " + WinWidth + "x" + WinHeight);
            return;
        }
    
        // Ensure Perimeter is initialized
        if (Perimeter == null) {
            System.err.println("Error: Perimeter is null!");
            return;
        }
    
        // Update the Perimeter bounds
        Perimeter.setBounds(0, 0, WinWidth, WinHeight);
        Perimeter.grow(-1, -1); // Shrink by 1 pixel
    
        // Ensure I (Insets) is initialized
        if (I == null) {
            I = EditorFrame.getInsets();
            if (I == null) {
                //System.err.println("Error: Insets could not be retrieved!");
                return;
            }
        }
    
        // Define expansion and insets
        int EXPAND = 10; 
        int lw = I.left + I.right;
        int lh = I.top + I.bottom;
    
        int mr = 0, mb = 0;
    
        // Ensure Ball class functions correctly
        if (Ball == null) {
            //System.err.println("Error: Ball class reference is null!");
            return;
        }
    
        int wallSize = Ball.getWallSize();
        if (wallSize <= 0) {
           // System.err.println("Warning: No walls exist in Ball.");
        }
    
        // Loop through all Ball walls
        for (int i = 0; i < wallSize; i++) {
            Rectangle r = Ball.getOne(i);
            if (r == null) {
               // System.err.println("Warning: Ball rectangle at index " + i + " is null!");
                continue;
            }
    
            //System.out.println("Rectangle " + i + ": " + r);
    
            mr = Math.max((r.x + r.width), mr);
            mb = Math.max((r.y + r.height), mb);
    
            // Ensure rectangle sizes are positive
            if (r.width < 0) {
                r.x += r.width;
                r.width = -r.width;
            }
            if (r.height < 0) {
                r.y += r.height;
                r.height = -r.height;
            }
    
            // Ensure rectangles stay within bounds
            int ballWidth = Ball.getWidth();
            int ballHeight = Ball.getHeight();
    
            if (ballWidth > 0 && ballHeight > 0) {
                r.width = Math.max(1, r.width);
                r.height = Math.max(1, r.height);
                r.x = Math.min(r.x, Math.max(0, ballWidth - r.width));
                r.y = Math.min(r.y, Math.max(0, ballHeight - r.height));
            } else {
                //System.err.println("Warning: Ball dimensions are invalid!");
            }
        }
    
        // Ensure Screen dimensions are valid
        if (Screen == null) {
           // System.err.println("Error: Screen reference is null!");
            return;
        }
    
        // Resize window if necessary
        if (mr > Screen.x || mb > Screen.y) {
            EditorFrame.setSize(
                Math.max((mr + EXPAND), Screen.x) + lw, 
                Math.max((mb + EXPAND), Screen.y) + lh + 2 * BUTTONH
            );
        }
    
        // Ensure db (Drag Box) is not null before modifying bounds
        if (db != null && db.width > 0 && db.height > 0) {
            db.setBounds(Perimeter.intersection(db));
        } else {
           // System.err.println("Warning: Drag Box is invalid or null!");
        }
    
        // Resize Ball and ensure values are positive
        int newBallWidth = Screen.x + 100;
        int newBallHeight = Screen.y + 100;
    
        if (newBallWidth > 0 && newBallHeight > 0) {
            Ball.reSize(newBallWidth, newBallHeight);
        } else {
            //System.err.println("Warning: Invalid Ball resize dimensions.");
        }
    
        // Update UI elements
        MakeSheet();
        SizeScreen();
    
        // Repaint Ball to reflect the new size
        Ball.repaint();
    
       // System.out.println("Final window size: " + WinWidth + "x" + WinHeight);
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
         private double px, py;
         private int pdx, pdy;
         private boolean rect=true;
         private boolean clear=false;
         int offset = (SBall -1)/2;
 
         Image buffer;
         Graphics g;
         
 
         private Rectangle dragrec = new Rectangle();
         private Vector<Rectangle> Walls = new Vector<Rectangle>();
        
         int cannonX, cannonY;
         int barrelLength = 100;
         int barrelWidth = 10;
         int cannonAngle = 90; // Default angle (horizontal)

         private int velocity = 100;
         private double time = 0;  // Elapsed time since launch
         private double x0, y0;    // Initial position
         private double v0x, v0y;  // Initial velocity components
        //private double ax, ay;    // Current position of the projectile
         private double gravity = 9.81;  // Default gravity (Earth)
         

         
 
         //constructor
         public Ballc(int SB,Point screen, int startX, int startY){//int w, int h){
            ScreenWidth = screen.x;  // Use screen.x for width
            ScreenHeight = screen.y; 
          
            SBall=SB;
            rect=true;
            clear=false;

            px = startX;
            py = startY;
            //----------
            x0 = startX;
            y0 = startY;


            cannonX = getWidth() - 37;
            cannonY = getHeight() - 37;
            this.velocity = velocity; // Default velocity

         
            x = (SBall / 2)+1; //calculate offset for x
            y = (SBall / 2)+1; //calculate offset for y
            dx = 1;//set initial x flags to true
            dy = 1;//set initial y flags to true
         }
         public void resetBall(int startX, int startY) {
            x = startX;
            y = startY;
            dx = 1; // Reset x direction
            dy = 1; // Reset y direction
            //add reset for timer labels
        }
        public void setVelocity(int newVel) {
            this.velocity = newVel;
        }
        public int getVelocity() {
            return velocity;
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

            //draw the cannon wheel
            //g.setColor(Color.green);
            
            //g.fillOval(width-75, height-75,75, 75);
            
            int cannonCenterX = width-37;
            int cannonCenterY = height-37;
            
            if (ProjectileActive && Projectile != null) {
                g.setColor(Color.blue);
                // g.fillOval(Projectile.px, Projectile.py, Projectile.getSizeBall(), Projectile.getSizeBall());
                // g.setColor(Color.black);
                // g.drawOval(Projectile.px, Projectile.py, Projectile.getSizeBall(), Projectile.getSizeBall());
                g.fillOval((int) Projectile.px, (int) Projectile.py, Projectile.getSizeBall(), Projectile.getSizeBall());
                g.setColor(Color.black);
                g.drawOval((int) Projectile.px, (int) Projectile.py, Projectile.getSizeBall(), Projectile.getSizeBall());
            }

 

            updateCannon(cannonCenterX, cannonCenterY);
            g.setColor(Color.DARK_GRAY);
            g.fillPolygon(cannon);

            g.setColor(new Color(139, 69, 19));//// Brown color for the cannon wheel
            g.fillOval(cannonCenterX - 37, cannonCenterY - 37, 75, 75);



            // Draw the buffered image onto the component
            cg.drawImage(buffer, 0, 0, null);
        }

        private void updateCannon(int cannonX , int cannonY) {
            cannon.reset();
            double rad = Math.toRadians(cannonAngle); //to rotate the cannon in same direction as scrollbar, multiply by -1 and add 180
            //double rad = Math.toRadians(Ball.getAngle()); 
            int cannonBx,cannonTx,cannonBy,cannonTy,cx,cy;

            cannonBx=cannonX;
            cannonBy=cannonY;

            cx=(int) (barrelLength* Math.cos(rad));
            cy=(int) (barrelLength* Math.sin(rad));

            cannonTx=cannonBx-cx;
            cannonTy=cannonBy-cy;

            int x1 = (int) (cannonTx - (barrelWidth * Math.sin(rad)));  // Change here for counterclockwise
            int y1 = (int) (cannonTy + (barrelWidth * Math.cos(rad)));  // Keep this to preserve upward direction

            int x2 = (int) (cannonTx + (barrelWidth * Math.sin(rad)));
            int y2 = (int) (cannonTy - (barrelWidth * Math.cos(rad)));
            
            // Additional points to create the barrel polygon
            int x3 = (int) (cannonBx + (barrelWidth * Math.sin(rad)));
            int y3 = (int) (cannonBy - (barrelWidth * Math.cos(rad)));
            
            int x4 = (int) (cannonBx - (barrelWidth * Math.sin(rad)));
            int y4 = (int) (cannonBy + (barrelWidth * Math.cos(rad)));
    
            cannon.addPoint(x1, y1);
            cannon.addPoint(x2, y2);
            cannon.addPoint(x3, y3);
            cannon.addPoint(x4, y4);
        }
        
        
        //helper function to set angle
        public void setAngle(int angle) {
            // keep the angle stays within the 0-360 range
            if (angle >= 0 && angle <= 360) {
                cannonAngle = angle;
                repaint(); // Repaint the cannon
            }
        }

        public int getAngle() {
            return cannonAngle;
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

        // public void moveProjectile() {
        //     // Update projectile position
        //     px += pdx;
        //     py += pdy;

        //     py += -4+pdy;

        //     px += -4+pdx;

        //     System.out.println("Projectile position: " + px + ", " + py); // Debugging output
            
        //     // Check if the projectile is out of bounds // THIS IS NOT CORRECTLY CHECKING FOR BOUNDS
        //     if (px < 0-SBall || px > EditorFrame.getWidth()+SBall || py < 0-SBALL || py > EditorFrame.getHeight()+SBall ) {
                
        //         //Maybe add a timer so the ball can be out of the frame for a certain amount of time before it deactivates
        //         //or maybe just dont count out of bouncds if it shoots straight up because it can still fall back down
        //         //type shit
        //         ProjectileActive = false;
        //         System.err.println("OUT OF BOUNDS which are: " + ScreenWidth + "x" + ScreenHeight); // Debugging output
        //         sheet.remove(this); // Remove the projectile from the canvas
        //     }


        //     // Repaint the screen
        //     repaint();
        // }
        public void moveProjectile(double deltaTime) {
            // Update time
            time += deltaTime;
        
            // Horizontal motion (no acceleration in x)
            px = x0 + v0x * time;
        
            // Vertical motion (affected by gravity)
            py = y0 + v0y * time - 0.5 * gravity * time * time;
        
            // Update vertical velocity due to gravity
            v0y = v0y - gravity * time; 
        
            System.out.println("Projectile Position: (" + px + ", " + py + ")");
        
            // Check for out-of-bounds
            if (px < 0 || px > EditorFrame.getWidth() || py < 0 || py > EditorFrame.getHeight()) {
                ProjectileActive = false;
                System.err.println("OUT OF BOUNDS");
            }
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

                     if (ProjectileActive) {
                        Projectile.moveProjectile(0.2); // Move the projectile
                         //Projectile.moveProjectile(); // Move the projectile
                       Projectile.repaint(); // Repaint the projectile
                    }
                 }
            }
    }
     //=========================END CLASS CANVAS============================




}
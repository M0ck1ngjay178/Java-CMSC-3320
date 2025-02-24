package Bounce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class Bounce extends Frame implements WindowListener, ComponentListener, ActionListener, AdjustmentListener, Runnable {
    private static final long serialVersionUID = 10L;
    //constants
    private final int WIDTH=640; //initial frame width
    private final int HEIGHT=400;//initial frame height
    private final int BUTTONH=20;//button height
    private final int BUTTONHS=5;//button height spacing

    private final int MAXObj=100; //max objecct size
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
    private final double DELAY = 5;
    boolean run;
    boolean TimerPause;
    boolean started;
    int speed, delay;
    private int dx, dy, newSize;

    Button Start,Shape,Clear,Tail,Quit;//Buttons

    //objects
    private Objc Obj; //object to draw
    private Label SPEEDL= new Label("Speed", Label.CENTER); // label for speed scroll bar
    private Label SIZEL=new Label("Size",Label.CENTER);// label for size scroll bar
    Scrollbar SpeedScrollBar, ObjSizeScrollBar;//scrollbars
    private Thread thethread; //thread for timer delay

    
    Bounce() {
        setTitle("Bouncing Ball");
        setLayout(null);
        setVisible(true);
        MakeSheet();
        started = false;

        try{
            initComponents();
        }catch (Exception e){
            e.printStackTrace();
        }
        SizeScreen();

       start();
    }

    public void initComponents()throws Exception, IOException{
         //initialize componenets
         //start timerPaused as true to pause animation
         // TODO: idk how to calculate the delay rn?! calculate offset, tail not mowrking, cant get it the top left corner 
         TimerPause = true;
         run = true;
         //buttons
         Start=new Button("Run");
         Shape= new Button("Circle");
         Clear= new Button("Clear");
         Tail=new Button("No Tail");
         Quit=new Button("Quit");

         add("Center",Start);
         add("Center",Shape);
         add("Center",Tail);
         add("Center",Clear);
         add("Center",Quit);

         Start.addActionListener(this);
         Shape.addActionListener(this);
         Tail.addActionListener(this);
         Clear.addActionListener(this);
         Quit.addActionListener(this);
         this.addComponentListener(this);
         this.addWindowListener(this);
         setPreferredSize(new Dimension(WIDTH,HEIGHT));
         setMinimumSize(getPreferredSize());
         setBounds(WinLeft,WinTop,WIDTH,HEIGHT);

         //scrollbars
         SpeedScrollBar=new Scrollbar(Scrollbar.HORIZONTAL);
         SpeedScrollBar.setMaximum(SpeedSBmax);
         SpeedScrollBar.setMinimum(SpeedSBmin);
         SpeedScrollBar.setUnitIncrement(SBunit);
         SpeedScrollBar.setBlockIncrement(SBblock);
         SpeedScrollBar.setValue(SpeedSBinit);
         SpeedScrollBar.setVisibleAmount(SBvisible);
         SpeedScrollBar.setBackground(Color.gray);

         ObjSizeScrollBar=new Scrollbar(Scrollbar.HORIZONTAL);
         ObjSizeScrollBar.setMaximum(MAXObj);
         ObjSizeScrollBar.setMinimum(MINObj);
         ObjSizeScrollBar.setUnitIncrement(SBunit);
         ObjSizeScrollBar.setBlockIncrement(SBblock);
         ObjSizeScrollBar.setValue(SOBJ);
         ObjSizeScrollBar.setVisibleAmount(SBvisible);
         ObjSizeScrollBar.setBackground(Color.gray);
         Obj=new Objc(SObj,ScreenWidth,ScreenHeight);
         Obj.setBackground(Color.white);

         add(SpeedScrollBar);
         add(ObjSizeScrollBar);
         add(SPEEDL);
         add(SIZEL);
         add(Obj);
         SpeedScrollBar.addAdjustmentListener(this);
         ObjSizeScrollBar.addAdjustmentListener(this);


         validate();
         
    }

    private void MakeSheet(){
        I=getInsets();
        ScreenWidth=WinWidth-I.left-I.right;
        ScreenHeight=WinHeight-I.top-2*(BUTTONH+BUTTONHS)-I.bottom;
        
        setSize(WinWidth,WinHeight);
        CENTER=(ScreenWidth/2);
        BUTTONW=ScreenWidth/11;
        BUTTONS=BUTTONW/4;
        setBackground(Color.lightGray);

        ScrollBarW=2*BUTTONW; // scroll bar width
    }

    private void SizeScreen(){
        //position the buttons
        Start.setLocation(CENTER-2*(BUTTONW+BUTTONS)-BUTTONW/2,ScreenHeight+BUTTONHS+I.top);
        Shape.setLocation(CENTER-BUTTONW-BUTTONS-BUTTONW/2,ScreenHeight+BUTTONHS+I.top);
        Tail.setLocation(CENTER-BUTTONW/2,ScreenHeight+BUTTONHS+I.top);
        Clear.setLocation(CENTER+BUTTONS+BUTTONW/2,ScreenHeight+BUTTONHS+I.top);
        Quit.setLocation(CENTER+BUTTONW+2*BUTTONS+BUTTONW/2,ScreenHeight+BUTTONHS+I.top);

        //size the buttons
        Start.setSize(BUTTONW,BUTTONH);
        Shape.setSize(BUTTONW,BUTTONH);
        Tail.setSize(BUTTONW,BUTTONH);
        Clear.setSize(BUTTONW,BUTTONH);
        Quit.setSize(BUTTONW,BUTTONH);

        //position the scrollbars object and labels
        SpeedScrollBar.setLocation(I.left+BUTTONS,ScreenHeight+BUTTONHS+I.top);
        ObjSizeScrollBar.setLocation(WinWidth-ScrollBarW-I.right-BUTTONS,ScreenHeight+BUTTONHS+I.top);
        SPEEDL.setLocation(I.left+BUTTONS,ScreenHeight+BUTTONHS+BUTTONH+I.top);
        SIZEL.setLocation(WinWidth-ScrollBarW-I.right,ScreenHeight+BUTTONHS+BUTTONH+I.top);
        SpeedScrollBar.setSize(ScrollBarW,SCROLLBARH);
        ObjSizeScrollBar.setSize(ScrollBarW,SCROLLBARH);
        SPEEDL.setSize(ScrollBarW,BUTTONH);
        SIZEL.setSize(ScrollBarW,SCROLLBARH);
        Obj.setBounds(I.left,I.top,ScreenWidth,ScreenHeight);
    }

    public void stop(){
        run = false;
        Start.removeActionListener(this);
        Shape.removeActionListener(this);
        Clear.removeActionListener(this);
        Tail.removeActionListener(this);
        Quit.removeActionListener(this);

        this.removeComponentListener(this);
        this.removeWindowListener(this);

        SpeedScrollBar.removeAdjustmentListener(this);
        ObjSizeScrollBar.removeAdjustmentListener(this);

        dispose();
        thethread.interrupt();
        System.exit(0);
    }

    public void start(){
        Obj.repaint(); //repaint object

        if(thethread == null){ //create a thread if it doesnt exist
            thethread = new Thread(this);//create a new thread
            thethread.start();//start the thread
        }
    }
    //========================ACTION HANDLER==========================================

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); //get current source
        if(source==Start){
            if(Start.getLabel()=="Pause"){
                Start.setLabel("Run");
                //started =true;
                TimerPause = true;
                
                //thethread.interrupt();
            }
            else{
                Start.setLabel("Pause");
                TimerPause = false;
                started =true;
                //thethread.interrupt();
            }
        }
        if(source==Tail){
            if(Tail.getLabel()=="Tail"){
                Tail.setLabel("No Tail");
                started = true;
                Obj.Clear();
            }
            else{
                Tail.setLabel("Tail");
                started = false;
                //start tail mode here
            }
        }
        if(source==Shape){

            if (!started) {
                Obj.Clear(); // Clear the object if animation hasn't started
            }

            if(Shape.getLabel()=="Circle"){
                Shape.setLabel("Square");
                Obj.rectangle(false);
            }
            else{
                Shape.setLabel("Circle");
                Obj.rectangle(true);
            }
            Obj.repaint();
        }
        if(source==Clear){
            Obj.Clear();
            Obj.repaint();
        }
        if(source==Quit){
            stop();
        }
        
    }


    public void adjustmentValueChanged(AdjustmentEvent e) {
        int TS;
        Scrollbar sb = (Scrollbar) e.getSource(); // Get the scrollbar that triggered the event
    
        // Speed Scrollbar
        if (sb == SpeedScrollBar) {
            // Recalculate the delay based on the speed value
            int speedValue = SpeedScrollBar.getValue();
            delay = Math.max(1, 100 - speedValue); // Calculate delay (inversely proportional to speed)
    
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
            TS = (TS / 2) * 2 + 1; // Ensure the size is odd to keep it centered
            
            // Validate if the new size will fit inside the screen without touching the borders
            if (TS <= ScreenWidth && TS <= ScreenHeight) {
                // Update the object size if it fits
                Obj.update(TS);
            } else {
                // Revert the scrollbar value if the size doesn't fit
                ObjSizeScrollBar.setValue(SObj); // Set back to previous valid size
            }
    
            // After updating, check if in No Tail mode and clear the display if necessary
            if (Tail.getLabel().equals("No Tail")) {
                Obj.Clear(); // Clear the object if in No Tail mode
            }
        }
    
        // Repaint the object after the size change
        Obj.repaint();
    }
    


    //========================END ACTION HANDLER=======================================

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
    

    public void componentResized(ComponentEvent e){
        WinWidth=getWidth();
        WinHeight=getHeight();
        MakeSheet();

        Obj.reSize(ScreenWidth,ScreenHeight);
        //check obj size
        checkSize();

        SizeScreen();
    }
    
    public void componentHidden(ComponentEvent e){}

    public void componentShown(ComponentEvent e){}
    
    public void componentMoved(ComponentEvent e){}

    //=================END COMPONENT METHODS=========

    //============DRAWING OBJECT CLASS==============

    class Objc extends Canvas{
        //declare/initialize variables
        private static final long serialVersionUID=11L;
        private int ScreenWidth;
        private int ScreenHeight;
        private int SObj;

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
            //y=ScreenHeight/2;??? what do theses do??
            //x=ScreenWidth/2;
            // x = Math.max(offset, Math.min(ScreenWidth - offset, x));
            // y = Math.max(offset, Math.min(ScreenHeight - offset, y));


            boolean down = true;
            boolean right = true;

            x = SObj / 2;
            y = SObj / 2;

            dy = 1;
            dx = 1;

        }

        // public void calcBounds() {
        //     int offset = (SObj - 1) / 2;  // Calculate the offset
    
        //     // Calculate the min and max X and Y values for the object's display area
        //     int minX = x - offset;
        //     int maxX = x + offset;
        //     int minY = y - offset;
        //     int maxY = y + offset;
        // }

        
    
        //mutators
        public void rectangle(boolean r){
            rect=r;
        }

        public void update(int NS){
            SObj=NS;
        }

        public void stayInBounds(int newScreenWidth, int newScreenHeight) {
            // Calculate right and bottom 
            int right = x + SObj / 2;
            int bottom = y + SObj / 2;
        
            // Ensure the object is within bounds
            if (right > newScreenWidth) {
                x = newScreenWidth - SObj / 2;
            }
            if (bottom > newScreenHeight) {
                y = newScreenHeight - SObj / 2;
            }
        
            // make sure doesn't move past the left/top edge
            // if (x - SObj / 2 < 0) {
            //     x = SObj / 2;
            // }
            // if (y - SObj / 2 < 0) {
            //     y = SObj / 2;
            // }
            if (x < 0) x = 0;
            if (y < 0) y = 0;

        }
        

        public void reSize(int w, int h){
            ScreenWidth=w;
            ScreenHeight=h;
            y=ScreenHeight/2;
            x=ScreenWidth/2;
            stayInBounds(w, h);
        }

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

        //------------------------------------------

        //PAINT
        public void paint(Graphics g){
            g.setColor(Color.red);
            g.drawRect(0, 0, ScreenWidth-1, ScreenHeight-1);
            update(g);
        }
        //UPDATE
        public void update(Graphics g){
            if(clear){
                super.paint(g);
                clear=false;
                g.setColor(Color.red);
                g.drawRect(0, 0, ScreenWidth-1, ScreenHeight-1);
            }

            if(rect){
                g.setColor(Color.lightGray);
                g.fillRect(x-(SObj-1)/2, y-(SObj-1)/2,SObj,SObj);
                g.setColor(Color.black);
                g.drawRect(x-(SObj-1)/2, y-(SObj-1)/2,SObj-1,SObj-1);
            }
            else{
                g.setColor(Color.lightGray);
                g.fillOval(x-(SObj-1)/2, y-(SObj-1)/2,SObj,SObj);
                g.setColor(Color.black);
                g.drawOval(x-(SObj-1)/2, y-(SObj-1)/2,SObj-1,SObj-1);
            }

            // if(!tail){
            //     g.setColor(getBackground());
            //     if
            // }
        }

        // test for debugging
        // public void move() {
        //     if (x <= 0 || x + SOBJ >= getWidth()) dx = -dx;
        //     if (y <= 0 || y + SOBJ >= getHeight()) dy = -dy;
        //     x += dx;
        //     y += dy;
        // }

        private void move() {
            if (y + SObj >= ScreenHeight || y <= 0) {
                dy = -dy;
            }
            if (x + SObj >= ScreenWidth || x <= 0) {
                dx = -dx;
            }
            x += dx;
            y += dy;
            repaint();
        }
    }

   
    public void run() {
   
            while (run) {
                if (!TimerPause) {
                    started = true;
                    
                    try {
                        Thread.sleep(delay); // Control animation speed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
        
                    // int oldX = Obj.getXPos();
                    // int oldY = Obj.getYPos();
                    // int oldSize = Obj.getSizeObj();
        
                    // Obj.setSizeObj(newSize);  // Update the object's size
                    // Obj.setXPos(Obj.getXPos() + dx); // Move horizontally
                    // Obj.setYPos(Obj.getYPos() + dy); // Move vertically
        
                    // if (!started) { // Check if tails are off
                    //     Obj.repaint(oldX - (oldSize - 1) / 2, oldY - (oldSize - 1) / 2, oldSize, oldSize);
                    // }
        
                    // Obj.repaint(Obj.getXPos() - (Obj.getSizeObj() - 1) / 2, Obj.getYPos() - (Obj.getSizeObj() - 1) / 2, Obj.getSizeObj(), Obj.getSizeObj());
                }

                //small delay, outside of decison but in loop
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}

                Obj.move();
                Obj.repaint();
            }

    }




    //===========END OBJECT CLASS==================


    public static void main(String[] args) {
        new Bounce();
    }

}

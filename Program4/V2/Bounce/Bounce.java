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
         // TODO: idk how to calculate the delay rn?! 
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
            }
            else{
                Start.setLabel("Pause");
            }
        }
        if(source==Tail){
            if(Tail.getLabel()=="Tail"){
                Tail.setLabel("No Tail");
            }
            else{
                Tail.setLabel("Tail");
            }
        }
        if(source==Shape){
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

    public void adjustmentValueChanged(AdjustmentEvent e){
        int TS;
        Scrollbar sb=(Scrollbar)e.getSource();//get scroll bar that triggered event
        if(sb==SpeedScrollBar){

        }
        if(sb==ObjSizeScrollBar){
            TS=e.getValue();
            TS=(TS/2)*2+1;//make odd to account for center positions
            Obj.update(TS);
        }
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

    public void componentResized(ComponentEvent e){
        WinWidth=getWidth();
        WinHeight=getHeight();
        MakeSheet();

        Obj.reSize(ScreenWidth,ScreenHeight);

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
        //constructor
        public Objc(int SB,int w, int h){
            ScreenWidth=w;
            ScreenHeight=h;
            SObj=SB;
            rect=true;
            clear=false;
            y=ScreenHeight/2;
            x=ScreenWidth/2;
        }
        //mutators
        public void rectangle(boolean r){
            rect=r;
        }

        public void update(int NS){
            SObj=NS;
        }

        public void reSize(int w, int h){
            ScreenWidth=w;
            ScreenHeight=h;
            y=ScreenHeight/2;
            x=ScreenWidth/2;
        }

        public void Clear(){
            clear=true;
        }

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
        }
    }

   
    public void run() {
        /*while (run) { // Continue looping while the run flag is true
            if (!TimerPause) { // Only proceed if not paused
                started = true; // Mark that animation has started

                try {
                    Thread.sleep(delay); // Control animation speed
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Handle interruption properly
                }

                // Retrieve object properties using getters (if x and y are private)
                int oldX = getX();
                int oldY = getY();
                int oldSize = getSize();

                // Update object's size and position
                setSize(newSize); // Assuming newSize is defined somewhere
                setX(getX() + getDX()); // Move horizontally
                setY(getY() + getDY()); // Move vertically

                // If no tails are requested, erase old object
                if (!showTails) {
                    repaint(oldX - (oldSize - 1) / 2, oldY - (oldSize - 1) / 2, oldSize, oldSize);
                }

                // Repaint new object at updated position
                repaint(getX() - (getSize() - 1) / 2, getY() - (getSize() - 1) / 2, getSize(), getSize());
            }
        }*/
    }




    //===========END OBJECT CLASS==================


    public static void main(String[] args) {
        new Bounce();
    }

}

import java.awt.*;
import java.awt.event.*;

public class menu implements ActionListener, WindowListener, ItemListener {
    
    
    private int sw = 650, sh=480;//screen witdh and height
    private Frame EditorFrame;//create object frame
    private TextArea EditArea;//create area for typing
    
    private MenuBar MMB;//menu bar
    private Menu CONTROL,PARAMETERS,ENVIRONMENT;//main items on the menu bar
    private Menu  SIZE,SPEED;//sub menu items under items

    private MenuItem PAUSE,RUN,RESTART;//terminal items in new menu
    private MenuItem QUIT;//menu item

    private CheckboxMenuItem Sxs,Ss,Sm,Sl,Sxl;//checkbox menu items for size
    private CheckboxMenuItem MERCURY,VENUS,EARTH,MARS,JUPITER,SATURN,URANUS,NEPTUNE,PLUTO;//checkboxes for Environment
    private CheckboxMenuItem XS,SLOW,MEDIUM,FAST,XF;//checkbox menu items for font    

    private int FontType = Font.PLAIN;
    private String FontStyle = "Times New Roman";
    private int FontSize = 14;

    public static void main(String[] args) {
        new menu();
    }

    public menu(){

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

        EditorFrame.setMenuBar(MMB);//add menu bar to frame 
        EditorFrame.addWindowListener(this);//add window listener to frame          
        EditorFrame.setSize(sw,sh);//set frame size
        EditorFrame.setResizable(true);//set frame resizable
        EditorFrame.setVisible(true);//set frame visible
        EditorFrame.validate();

        setTheFont();//set the font to the default font ????????
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
    
    }

    public void stop(){
        QUIT.removeActionListener(this);
        Sxs.removeItemListener(this);
        Ss.removeItemListener(this);       
        Sm.removeItemListener(this);
        XS.removeItemListener(this);
        SLOW.removeItemListener(this);
        EditorFrame.removeWindowListener(this);
        EditorFrame.dispose();

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

     




}

import java.awt.*;
import java.awt.event.*;

public class menu implements ActionListener, WindowListener, ItemListener {
    
    
    private int sw = 650, sh=480;//screen witdh and height
    private Frame EditorFrame;//create object frame
    private TextArea EditArea;//create area for typing
    
    private MenuBar MMB;//menu bar
    private Menu FILE,TEXT;//main items on the menu bar
    private Menu NEW, SIZE,FONT;//sub menu items under items

    private MenuItem FOLDER,DOCUMENT;//terminal items in new menu
    private MenuItem QUIT;//menu item

    private CheckboxMenuItem S10,S14,S18;//checkbox menu items for size
    private CheckboxMenuItem TNR,CO;//checkbox menu items for font    

    private int FontType = Font.PLAIN;
    private String FontStyle = "Times New Roman";
    private int FontSize = 14;

    public static void main(String[] args) {
        new menu();
    }

    public menu(){

        EditArea = new TextArea("",sw-10,sh-10, TextArea.SCROLLBARS_BOTH);

        EditorFrame = new Frame("Editor");
        EditorFrame.setLayout(new BorderLayout(0,0));
        EditorFrame.setBackground(Color.lightGray);
        EditorFrame.setForeground(Color.black);
        EditorFrame.add("Center", EditArea);

        MMB = new MenuBar();//create menu bar
        FILE = new Menu("File");//create first menu entry for menu bar
        NEW = new Menu("New");//create first entry for file menu

        //add Menuitem folder with short cut key to menu entry new
        FOLDER = new MenuItem("Folder Ctl+F", new MenuShortcut(KeyEvent.VK_F));
        //add Menuitem document with short cut key to menu entry new
        DOCUMENT = new MenuItem("Document Ctl+D", new MenuShortcut(KeyEvent.VK_D));

        //add and finish the first menu bar enrty file
        FILE.add(NEW);
        FILE.addSeparator();
        //add menu item quit with short cut key to menu entry file
        QUIT = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q));

        TEXT = new Menu("TEXT"); //create second menu entry for menu bar
        SIZE = new Menu("Size");//create first entry for text menu
        FONT = new Menu("Font");//create second entry for text menu

        SIZE.add(S10 = new CheckboxMenuItem("10"));//checkbox menuitem size 10
        SIZE.add(S14 = new CheckboxMenuItem("14"));//checkbox menuitem size 14
        SIZE.add(S18 = new CheckboxMenuItem("18"));//checkbox menuitem size 18  

        S14.setState(true); //initialize checkbox selection to 14
        TEXT.add(SIZE);//add size menu to text menu

        FONT.add(TNR = new CheckboxMenuItem("Times New Roman"));//checkbox menuitem font Times New Roman
        FONT.add(CO = new CheckboxMenuItem("Courier"));//checkbox menuitem font Courier

        TNR.setState(true);//initialize checkbox selection to Times New Roman
        TEXT.add(FONT);//add font menu to text menu

        MMB.add(FILE);//add file menu to menu bar
        MMB.add(TEXT);//add text menu to menu bar

        //turn on action listeners for menuitems, menushortcuts, and checkbox menuitems
        DOCUMENT.addActionListener(this);
        FOLDER.addActionListener(this);
        QUIT.addActionListener(this);
        S10.addItemListener(this);
        S14.addItemListener(this);
        S18.addItemListener(this);
        TNR.addItemListener(this);
        CO.addItemListener(this);

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

        if(checkbox == S10||checkbox == S14||checkbox == S18){
            S10.setState(false);
            S14.setState(false);
            S18.setState(false);
            checkbox.setState(true);
        }
        if(checkbox == TNR||checkbox == CO){
            TNR.setState(false);
            CO.setState(false);
            checkbox.setState(true);
        } 
        setTheFont();
    }

    public void actionPerformed(ActionEvent e) {
        
        Object source = e.getSource();
        if(source == FOLDER){
            EditArea.append("\nFolder\n");
        }
        if(source == DOCUMENT){
            EditArea.append("\nDocument\n");
        }
        if(source == QUIT){
            stop();
        }
    
    }

    public void stop(){
        DOCUMENT.removeActionListener(this);
        FOLDER.removeActionListener(this);
        QUIT.removeActionListener(this);
        S10.removeItemListener(this);
        S14.removeItemListener(this);       
        S18.removeItemListener(this);
        TNR.removeItemListener(this);
        CO.removeItemListener(this);
        EditorFrame.removeWindowListener(this);
        EditorFrame.dispose();

        //terminates the program
        System.exit(0);
    }

    public void setTheFont(){
        //set the font to the selected font
        FontSize = 10;
        if(S10.getState() == true){
            FontSize = 10;
        }
        if(S14.getState() == true){
            FontSize = 14;
        }
        if(S18.getState() == true){
            FontSize = 18;
        }   

        FontStyle = "TimesNewRoman";
        if(TNR.getState()== true){
            FontStyle = "Times New Roman";
        }
        if(CO.getState()== true){
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

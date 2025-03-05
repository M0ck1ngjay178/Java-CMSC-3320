package BouncingBall;

import java.awt.*;
import java.awt.event.*;



public class BouncingBall extends Frame implements WindowListener, MouseListener, MouseMotionListener{

    final int   WinLeft = 10;//left side of frame
    final int   WinTop = 10;//top of frame
    Point FrameSize = new Point(640, 400);
    Panel sheet = new Panel();
    List list = new List(13);

    BouncingBall(){

        setLayout(new BorderLayout());//layout border
        setBounds(WinLeft, WinTop, FrameSize.x, FrameSize.y);//size and position the frame
        setBackground(Color.lightGray);
        sheet.setLayout(new BorderLayout(0,0));//sheet border layout
        sheet.setVisible(true);
        sheet.add("Center", list);//add the list to the center of the panel sheet
        add("Center", sheet);//add sheet panel to center of application
        addWindowListener(this);
        list.addMouseListener(this);
        list.addMouseMotionListener(this);
        setVisible(true);
        validate();//make it visible

    }

       //stops the program, removes listeners, and exits the program
       public void stop(){

        // //stop the ball movement
        // runBall = false;

        // //removes action listeners
        // Start.removeActionListener(this);
        // Shape.removeActionListener(this);
        // Clear.removeActionListener(this);
        // Tail.removeActionListener(this);
        // Quit.removeActionListener(this);

        // //removes component and window listeners
        // this.removeComponentListener(this);
        // this.removeWindowListener(this);

        // //removes adjustment listeners from scrollbars
        // SpeedScrollBar.removeAdjustmentListener(this);
        // ObjSizeScrollBar.removeAdjustmentListener(this);

        // //disposes the frame and stops the thread
        dispose();
        // thethread.interrupt();

        //terminates the program
        System.exit(0);
    }

    //========================WINDOW LISTENER METHODS=================================
    
    //add all 6 Window Listener Methods
    public void windowClosing(WindowEvent e){
        stop();
     }
 
     public void windowClosed(WindowEvent e){}
     public void windowOpened(WindowEvent e){
        //list.add("Window opened");
     }
 
     public void windowActivated(WindowEvent e){
       // list.add("Window activated");

     }
     public void windowDeactivated(WindowEvent e){
        //list.add("Window Deactivated");
     }
 
     public void windowIconified(WindowEvent e){
        //list.add("Window Iconified");
     }
     public void windowDeiconified(WindowEvent e){
        //list.add("Window Deiconified");
     }
    //========================END WINDOW LISTENER METHODS=================================

    //========================MOUSE METHODS=================================

    public void mousePressed(MouseEvent e){

        String button = "";

        if(e.getButton() == MouseEvent.BUTTON1){
            button = "Left";
        }else if(e.getButton() == MouseEvent.BUTTON2){
            button = "Center";
        }else if(e.getButton() == MouseEvent.BUTTON3){
            button = "Right";
        }

        list.add(button+"mouse button"+e.getButton()+"pressed");

    }

    public void mouseReleased(MouseEvent e){

        list.add("Mouse button"+e.getButton()+"released");
    }

    public void mouseClicked(MouseEvent e){

        list.add("Mouse Clicked"+e.getClickCount()+"clicks");
    }
    public void mouseMoved(MouseEvent e){

        //list.add("Mouse Moved");
    }

    public void mouseDragged(MouseEvent e){

        //list.add("Mouse Moved");
    }

    public void mouseEntered(MouseEvent e){

        //list.add("Mouse Entered");
    }

    public void mouseExited(MouseEvent e){

        //list.add("Mouse Exited");
    }




    /*****************MAIN**************************/
    public static void main(String[] args) {
        new BouncingBall();
    }
    /*****************END MAIN********************/




}

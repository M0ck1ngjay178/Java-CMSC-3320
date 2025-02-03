package Program3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;



public class Window extends Frame implements WindowListener{

    private static final long serialVersionUID = 1L;
    Label MessageLabel = new Label("This is a window.    ");


    Window(){
        //set up for GridBagLayout

        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout displ = new GridBagLayout();
        double colWeight[] = {1};
        double rowWeight[] = {1};
        int colWidth[] = {1};
        int rowHeight[] ={1};
        displ.rowHeights = rowHeight;
        displ.columnWidths = colWidth;
        displ.columnWeights = colWeight;
        displ.rowWeights = rowWeight;


        //set up window---------
        this.setBounds(20,20,200,100);
        this.setLayout(displ);
        displ.setConstraints(MessageLabel, c);
        this.setVisible(true);
        this.addWindowListener(this);
        //----------------------
    }
    
    //add all 6 Window Listener Methods

    public void windowClosing(WindowEvent e){
        this.removeWindowListener(this);
        this.dispose();
    }

    public void windowClosed(WindowEvent e){}
    public void windowOpened(WindowEvent e){}
    
    public void windowActivated(WindowEvent e){
        MessageLabel.setText("A window activated");
    }

    public void windowDeactivated(WindowEvent e){
        MessageLabel.setText("A window deactivated");
    }


    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
   
    public static void main(String[] args) {
        new Window();
    }

}

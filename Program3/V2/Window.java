package Program3.V2;

import java.awt.*;
import java.awt.event.*;
//import java.io.*;



public class Window extends Frame implements WindowListener, ActionListener{

    private static final long serialVersionUID = 1L;
    Label SourceLabel = new Label("Source:");
    Label SourceText= new Label("source text"); //the text is just a placeholder, will eventually be the source of file selected?
    Label FileName= new Label("File Name:");
    Button Ok=new Button("OK");
    Button Target=new Button("Target");
    TextField FileText=new TextField("File text placeholder",50);
    //Label MessageLabel=new Label("hello"); 
    //Label SelectTarget= new Label("Select Target Directory:");
    Label MessageLabel=new Label(); //need to be empty to display msgs
    Label SelectTarget= new Label();//need to be empty to display msgs

    


    Window(){
        //set up for GridBagLayout
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout displ = new GridBagLayout();
        double colWeight[] = {1,5,1};
        double rowWeight[] = {10,1,1,1,1};
        int colWidth[] = {1,1,1};
        int rowHeight[] ={1,1,1,1,1};
        displ.rowHeights = rowHeight;
        displ.columnWidths = colWidth;
        displ.columnWeights = colWeight;
        displ.rowWeights = rowWeight;

        /*----------LIST----------*/
        List list= new List(25,false); //creates a list with 25 rows, no multiple selection
        list.add("..");     //dots to go back

        //set up window---------
        this.setBounds(20,20,800,500);
        this.setLayout(displ);
       // displ.setConstraints(MessageLabel, c);
        this.setVisible(true);
        Ok.addActionListener(this);
        Target.addActionListener(this);
        this.addWindowListener(this);
        //----------------------

        c.anchor=GridBagConstraints.CENTER;
        c.weightx=1;
        c.weighty=1;
        c.gridwidth=1;
        c.gridheight=1;
        c.fill=GridBagConstraints.BOTH;

        //place for list
        c.gridx=0;
        c.gridy=0;
        displ.setConstraints(list,c);
        this.add(list);
         

        c.gridx=0;
        c.gridy=1;
        displ.setConstraints(SourceLabel, c);
        this.add(SourceLabel);

        c.gridx=GridBagConstraints.RELATIVE;//1
        c.gridy=1;
        displ.setConstraints(SourceText, c);
        this.add(SourceText);

        c.gridx=0;
        c.gridy=GridBagConstraints.RELATIVE;//3
        displ.setConstraints(Target, c);
        this.add(Target);

        c.gridx=GridBagConstraints.RELATIVE;//1
        c.gridy=2;
        displ.setConstraints(SelectTarget, c);
        this.add(SelectTarget);

        c.gridx=0;
        c.gridy=GridBagConstraints.RELATIVE;//4
        displ.setConstraints(FileName, c);
        this.add(FileName);

        c.gridx=GridBagConstraints.RELATIVE; //1
        c.gridy=3;
        displ.setConstraints(FileText, c);
        this.add(FileText);

        c.gridx=GridBagConstraints.RELATIVE; //2
        c.gridy=3;
        displ.setConstraints(Ok, c);
        this.add(Ok);

        c.gridx=1;
        c.gridy=4;
        displ.setConstraints(MessageLabel, c);
        this.add(MessageLabel);

    }
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); //get current source
        if(source == Ok){
            MessageLabel.setText("Clicked Ok");
            SelectTarget.setText("");
        }
        if(source == Target){
            SelectTarget.setText("Clicked Target");
            MessageLabel.setText("");
        }
    }
    

    //add all 6 Window Listener Methods

    public void windowClosing(WindowEvent e){
        this.removeWindowListener(this);
        Ok.removeActionListener(this);
        Target.removeActionListener(this);
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

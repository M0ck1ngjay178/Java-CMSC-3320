package Program3.V4_1;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.stream.Stream;

public class Main extends Frame implements WindowListener, ActionListener {

    private static final long serialVersionUID = 1L;
    Label SourceLabel = new Label("Source:");
    Label SourceText = new Label("source text");
    Label FileName = new Label("File Name:");
    Button Ok = new Button("OK");
    Button Target = new Button("Target");
    TextField FileText = new TextField("File text placeholder", 50);
    Label SelectTarget = new Label("Select Target Directory:");
    Label MessageLabel = new Label();
    String fileN;
    File curDir = new File(fileN);
    List list = new List(25, false);
    
    Main(File dir){
    
        this.curDir = dir;
        this.setTitle(curDir.getAbsolutePath());
        //------------SET UP WINDOW, LAYOUT, LIST---------------
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout displ = new GridBagLayout();
        setLayout(displ);

        // Create the List component
        // List list = new List(25, false);
        list.add("...");

        setBounds(20, 20, 800, 500);
        setVisible(true);
        Ok.addActionListener(this);
        Target.addActionListener(this);
        addWindowListener(this);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER; // full width
        c.gridx = 0;
        c.gridy = 0;
        displ.setConstraints(list, c);
        add(list);

        c.gridwidth = 1; // Reset width
        c.gridx = 0;
        c.gridy = 1;
        displ.setConstraints(SourceLabel, c);
        add(SourceLabel);

        c.gridx = 1;
        displ.setConstraints(SourceText, c);
        add(SourceText);

        c.gridx = 0;
        c.gridy = 2;
        displ.setConstraints(Target, c);
        add(Target);

        c.gridx = 1;
        displ.setConstraints(SelectTarget, c);
        add(SelectTarget);

        c.gridx = 0;
        c.gridy = 3;
        displ.setConstraints(FileName, c);
        add(FileName);

        c.gridx = 1;
        displ.setConstraints(FileText, c);
        add(FileText);

        c.gridx = 2;
        displ.setConstraints(Ok, c);
        add(Ok);

        c.gridx = 1;
        c.gridy = 4;
        displ.setConstraints(MessageLabel, c);
        add(MessageLabel);
        //------------SET UP WINDOW, LAYOUT, LIST---------------

        updateList();
        
    }


  // UPDATELIST FUNCTION-----------------
    public void updateList(){
    
        File directory = new File(getTitle());

        if (directory.exists() && directory.isDirectory()) { //check if directory exists and is a directory
            File[] files = directory.listFiles();
            
            if (files != null) {
                for (File file : files) {
                   // System.out.println(file.getName()); just for testing
                   if (file.isDirectory()){
                    list.add(file.getName()+ " +"); // + to show it is a directory
                   }
                   else{
                    list.add(file.getName());  // adds full path of each file/directory to list
                   }
                      

                }
            }
        } else {
           MessageLabel.setText("The provided path is not a directory.");
        }
    }
//--------------END UPDATELIST---------------------------


    //========================ACTION HANDLER==========================================

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
    //========================END ACTION HANDLER=======================================

    //========================WINDOW LISTENER METHODS=================================
    

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
        // MessageLabel.setText("A window activated");
    }

    public void windowDeactivated(WindowEvent e){
        // MessageLabel.setText("A window deactivated");
    }


    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    //========================END WINDOW LISTENER METHODS=================================

   


   //+++++++++++++++++++MAIN+++++++++++++++++++++++
    public static void main(String[] args) {

        /* 
        String file_name;
        File dir = new File(file_name);

        try{
            switch(args.length){                                   
                case 0:
                    new Main(new File(new File(System.getProperty("user.dir")).getAbsolutePath()));				                                
                    break;
                case 1:
                    file_name = args[0];

                    if(file_name.isDirectory()){ //is directory needs initialized
                        new Main(new File(dir.getAbsolutePath()));			                               
                        break;

                    }else{
                        System.out.println("Does not exist!!");
                    }     

            }
        }catch(Exception e){}
        
        */
        String file_name;

        try{
            switch(args.length){                                   
                case 0:
                    new Main(new File(new File(System.getProperty("user.dir")).getAbsolutePath()));				                                
                    break;
                case 1:
                    file_name = args[0];

                    File dir = new File(file_name); //was originally above try, but file_name wasn't defined there

                    if(dir.isDirectory()){ //can't be file_name because it's a string
                    new Main(new File(dir.getAbsolutePath()));			                               
                        break;

                    }else{
                        System.out.println("Does not exist!!");
                    }     

            }
        }catch(Exception e){  //keeps going here. 
            System.out.println("Exception error"); 
        }
        
        
    }

    }
    //+++++++++++++END MAIN++++++++++++++++++++++++

 /*
    public static void main(String[] args) {

        String file_name;

        try{
            switch(args.length){                                   
                case 0:
                    new Main(new File(new File(System.getProperty("user.dir")).getAbsolutePath()));				                                
                    break;
                case 1:
                    file_name = args[0];

                    File dir = new File(file_name); //was originally above try, but file_name wasn't defined there

                    if(dir.isDirectory()){ //can't be file_name because it's a string
                    new Main(new File(dir.getAbsolutePath()));			                               
                        break;

                    }else{
                        System.out.println("Does not exist!!");
                    }     

            }
        }catch(Exception e){  //keeps going here. 
            System.out.println("Exception error"); 
        }
        
        
    }*/




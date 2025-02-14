package Program3.v5_1;

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
    List list = new List(25, false);
    File curDir;
    boolean sourceFlag = false, targetFlag = false, outfileFlag = false;
    
    Main(File dir) {
        this.curDir = dir;
        sourceFlag = targetFlag = outfileFlag = false; // Initialize flags
        setTitle(curDir.getAbsolutePath());
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        list.add("...");
        Target.setEnabled(false); // Disable target button

        // Add components to layout
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        add(list, c);

        c.gridwidth = 1;
        c.gridy = 1;
        add(SourceLabel, c);
        c.gridx = 1;
        add(SourceText, c);

        c.gridx = 0;
        c.gridy = 2;
        add(Target, c);
        c.gridx = 1;
        add(SelectTarget, c);

        c.gridx = 0;
        c.gridy = 3;
        add(FileName, c);
        c.gridx = 1;
        add(FileText, c);
        c.gridx = 2;
        add(Ok, c);

        c.gridx = 1;
        c.gridy = 4;
        add(MessageLabel, c);

        // Add listeners
        Ok.addActionListener(this);
        Target.addActionListener(this);
        addWindowListener(this);

        setBounds(0, 0, 300, 900); // Set frame size
        pack(); // Adjust layout
        setVisible(true); // Make visible
        updateList();
    }

    public void updateList() {
        list.removeAll();
        //File[] files = curDir.listFiles();//just be .list()?
        this.setTitle(curDir.getAbsolutePath());
        String[] fileNames=curDir.list();

        if(curDir.getParent()!= null){
            list.add("..");
        }

        if (fileNames != null) {

            for(int i=0;i<fileNames.length;i++){  //loop through to see if it is a directory, and if it has directories
                File f=new File(curDir,fileNames[i]);
                if(f.isDirectory()){
                    String[] children=f.list();
                    int j=0;
                    Boolean Bool=true;
                    while(Bool){ //loop through children to look for directory
                        File ff=new File(f,children[j]);
                        if (ff.isDirectory()){
                            fileNames[i]+= "+";
                            Bool=false;
                        }
                        j++;
                        if (j>children.length){
                            Bool=false;
                        }
                    }   
                }
                list.add(fileNames[i]); //does fileNames now need to be File type? video doesnt say. could use f?
            }
        }else{
            MessageLabel.setText("The provided path is not a directory.");
        }
    }
    public void displayFiles(String fileORdir){
        
        if(fileORdir == null){
            updateList();
        }

        if("...".equals(fileORdir)){
                curDir = new File(curDir.getParent());
                updateList();
        }else{
                File file_file = new File(curDir, fileORdir);

                if(file_file.isDirectory()){
                    //curDir=new File(curDir,fileORdir);
                    curDir = file_file;
                    updateList();
                }else {
                    if(!sourceFlag || !targetFlag){
                        SourceLabel.setText(curDir.getAbsolutePath()+"\\"+ fileORdir);
                        sourceFlag=true;
                        Target.setEnabled(true);
                        FileText.setText(fileORdir);
                        outfileFlag=true;
                    }
                }
                if(file_file.exists()){
                    MessageLabel.setText("Selected: "+file_file.getAbsolutePath());
                }else{
                    MessageLabel.setText("ERROR!! Does Not Exist");

                }
               // updateList();
            
            }
        
    }

    //========================ACTION HANDLER==========================================

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); //get current source
        if(source == Ok){
            MessageLabel.setText("Clicked Ok");
            SelectTarget.setText("");
        }else if (source == Target){
            SelectTarget.setText("Clicked Target");
            MessageLabel.setText("");
        }else if (source == list){
            String select = list.getSelectedItem();
            displayFiles(select);
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

    public static void main(String[] args) {
        String file_name;

        try {
            switch (args.length) {
                case 0:
                    new Main(new File(new File(System.getProperty("user.dir")).getAbsolutePath()));
                    break;
                case 1:
                    file_name = args[0];
                    File dir = new File(file_name);
                    if (dir.isDirectory()) {
                        new Main(new File(dir.getAbsolutePath()));
                        break;
                    } else {
                        System.out.println("Does not exist!!");
                    }
            }
        } catch (Exception e) {
            System.out.println("Exception error");
        }
       // displayFiles(file_name);
    }

}

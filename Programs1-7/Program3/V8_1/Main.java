/*******************HEADER*******************************/
/*  CMSC-3320 Technical Computing Using Java		    */   
/* 	GUI FILE COPY						                */
/*	Group 1												*/
/*	Group Names: 										*/
/*     -Margo Bonal,      bon8330@pennwest.edu			*/
/*     -Luke Ruffing,     ruf96565@pennwest.edu 		*/
/*     -Ethan Janovich,   jan60248@pennwest.edu			*/
/*     -Nikolaus Roebuck, roe01807@pennwest.edu  		*/
/*******************END HEADER***************************/
package Program3.V8_1; //TODO: remeber to take out package for submisssion!!!! or will break

//----------LIBRARIES-------------
import java.awt.*;
import java.awt.event.*;
import java.io.*;
//----------END LIBRARIES---------

//----------------------------CLASS MAIN---------------------------------------------------------
public class Main extends Frame implements WindowListener, ActionListener {
    
    //--------------GLOBAL VARIABLES-------------------------------------
    private static final long serialVersionUID = 1L;
    Label SourceLabel = new Label("Source:");
    Label SourceText = new Label("");
    Label FileName = new Label("File Name:");
    Button Ok = new Button("OK");
    Button Target = new Button("Target");
    TextField FileText = new TextField("", 50);
    Label SelectTarget = new Label("Select Target Directory:");
    Label MessageLabel = new Label();
    List list = new List(25, false);
    File curDir;
    boolean sourceFlag = false, targetFlag = false, outfileFlag = false;
    //--------------END GLOBAL VARIABLES------------------------------------
    
    //------------------MAIN CONSTRUCTOR------------------------------------
    Main(File dir){
        
            //------------CONSTRUCTOR VARIABLES----------------------------
            this.curDir = dir;
            sourceFlag = targetFlag = outfileFlag = false; //init flags
            setTitle(curDir.getAbsolutePath());
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            list.add(".."); 
            Target.setEnabled(false); // Disable target button
            //------------END CONSTRUCTOR VARIABLES--------------------------
            
            //--------BUILD WINDOW LAYOUT: Add components-----------------
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
            //--------END BUILD WINDOW LAYOUT: Add components-----------------

            //------------ADD LISTENERS--------------------------------------
            Ok.addActionListener(this);
            FileText.addActionListener(this); // adds listener for enter key
            Target.addActionListener(this);
            list.addItemListener(e -> displayFiles(list.getSelectedItem())); // ADDED
            addWindowListener(this);
            //------------END ADD LISTENERS------------------------------------

            //-----------PACK FRAME/UPDATE LIST-----------------------------
            setBounds(0, 0, 300, 900); // Set frame size
            pack();// Adjust layout
            setVisible(true); // Make visible
            updateList(); //call helper function update list 
            //-----------PACK FRAME/UPDATE LIST-----------------------------

    }
    //------------------END MAIN CONSTRUCTOR------------------------------------

    //---------------------UPDATE LIST FUNCTION-----------------------------------
    //updates the file list in the current directory
    public void updateList() {
        try {
            list.removeAll(); //removes the whole list
            setTitle(curDir.getAbsolutePath());
            String[] fileNames = curDir.list();
            
            if (curDir.getParent() != null) { // if there is a parent directory
                list.add(".."); //adds option to go to parent directory
            }
           
            if (fileNames != null) {
                for (String name : fileNames) {
                    File f = new File(curDir, name);
    
                    if (f.isDirectory()) {
                        list.add(name + (lookforSubDir(f) ? "+" : "")); //call helper function to find directory in another direcoty, and place + coorespondingly
                    } else {
                        list.add(name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();//prints the stack trace helps with debuging
            MessageLabel.setText("UPDATE LIST ERROR");
        }
    }
    //---------------------END UPDATE LIST FUNCTION---------------------------------------

    //----------------LOOKFORSUBDIR FUNCTION-------------------
    private boolean lookforSubDir(File dir) { //function to return t or f is there is a dirctory in another directory
        File[] sub = dir.listFiles();

        if (sub != null) {
            for (File subFile : sub) {
                if (subFile.isDirectory()) {
                    return true; //found subdirectory
                }
            }
        }
        return false; //Not found
    }
    //----------------END LOOKFORSUBDIR FUNCTION-------------------


    //----------------DISPLAY FILES FUNCTION------------------------------------
    public void displayFiles(String fileORdir) {//helper function t display files in list viewer

        try {

            if(fileORdir != null) {    
            
                //If the file is a directory this lower line removes the + so it can actually be accessed and not be seen as non existant
            if(fileORdir.endsWith("+"))
            {
                    fileORdir = fileORdir.substring(0, fileORdir.length()-1);
            }

                File file_file = new File(curDir, fileORdir); 

                if("..".equals(fileORdir)) { //if filordir equals "..." then...

                    curDir = curDir.getParentFile();//navigates the list to the parent directory
                    updateList();// updates the list
                } 
                else if(file_file.isDirectory()){ // if the file selected is a directory...
                    curDir = file_file;// navigates to selected file
                    updateList();//updates list
                } 
                else{
                    if(!targetFlag) {

                    SourceText.setText(file_file.getAbsolutePath()); // sets the source file path
                        sourceFlag = true;

                        Target.setEnabled(true);
                    } else{
                        FileText.setText(fileORdir); // sets the ffiles name
                    }

                }
            } 
            else{
                return; 
            }  
        } catch (Exception e) {
            e.printStackTrace(); // prinst the stack trace helps with debuging
            MessageLabel.setText("DISPLAYFILES ERROR");
        }
    }
    //----------------END DISPLAY FILES FUNCTION------------------------------------

    //---------------------COPY FILE--------------------------------------
    private void copyFile(File source, File target) {

        boolean found = false;
        if (target.isDirectory()) {
            target = new File(target, source.getName());
        }
        try{

            BufferedReader read = new BufferedReader(new FileReader(source));
            PrintWriter writer = new PrintWriter(new FileWriter(target));

            int lines;

            while ((lines = read.read()) != -1) {

                writer.write(lines);
                found = true;
            }
            MessageLabel.setText("File copied EUREKA!");

            writer.flush(); //make sure data is written to file
            read.close();//close fle for reading
            writer.close();//close file for writing
            //debugging check
            if (found) {
                MessageLabel.setText("File copied successfully!");
            } else {
                MessageLabel.setText("File copied, but it was empty!");
            }
            //reset
            sourceFlag = false;
            targetFlag = false;
            outfileFlag = false;
            SourceText.setText(""); // Reset source file display
            FileText.setText("");   // Reset target file name
            SelectTarget.setText("Select Target Directory:");
            Target.setEnabled(false); // Disable Target button

        } catch (IOException e){
            e.printStackTrace();
            MessageLabel.setText("An io errar occurred");
        }
    }
    //---------------------END COPY FILE------------------------------------------
    
    //========================ACTION HANDLER==========================================
    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource(); //get current source
            if (source == Ok || source == FileText) {


                File sourceFile = new File(SourceText.getText());
                String targetPath = SelectTarget.getText().replace("Target: ", "").trim();
                File targetDir = new File(targetPath);
                

                if (!sourceFlag) { // if source is not specified
                    MessageLabel.setText("Source file is not specified");
                } else if (!targetFlag) {// if the target is not specified
                    MessageLabel.setText("target directory is not specified");
                } else {
                    outfileFlag = true;
                    File targetFile = new File(targetDir, FileText.getText());
                    copyFile(sourceFile, targetFile);// attempts to copy file
                }
            } else if (source == Target) {// checks if target button has been pressed

                MessageLabel.setText("");
                SelectTarget.setText("Target: " + curDir.getAbsolutePath());
                targetFlag = true;// sets the flag
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            MessageLabel.setText("ACTIONPERFORMED ERROR");
        }
    }
    //========================END ACTION HANDLER=======================================

    
    //========================WINDOW LISTENER METHODS=================================
    //add all 6 Window Listener Methods
    public void windowClosing(WindowEvent e) {
        this.removeWindowListener(this);
        Ok.removeActionListener(this);
        FileText.removeActionListener(this);
        list.removeActionListener(this);
        Target.removeActionListener(this);
        this.dispose();
    }
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {
        // MessageLabel.setText("A window activated");
    }
                
    public void windowDeactivated(WindowEvent e) {
        // MessageLabel.setText("A window deactivated");
    }

    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    //========================END WINDOW LISTENER METHODS=================================
    
    //++++++++++++++++++++++++++++MAIN+++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
        }
    //++++++++++++++++++++++++++++END MAIN+++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
}
//---------------------------- END CLASS MAIN---------------------------------------------------------

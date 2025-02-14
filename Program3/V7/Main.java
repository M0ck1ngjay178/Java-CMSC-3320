//SAVE2
package Program3.V7;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.stream.Stream;

public class Main extends Frame implements WindowListener, ActionListener {
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
            list.addItemListener(e -> displayFiles(list.getSelectedItem())); // ADDED
            addWindowListener(this);

            setBounds(0, 0, 300, 900); // Set frame size
            pack();// Adjust layout
            setVisible(true); // Make visible
            updateList();
       
    }

    //updates the file list in the current directory
    public void updateList() {
        try {
            list.removeAll(); //removes the whole list
            //File[] files = curDir.listFiles();//just be .list()?
            setTitle(curDir.getAbsolutePath());
            String[] fileNames = curDir.list();
            
            if (curDir.getParent() != null) { // if there is a parent directory
                list.add("..."); //adds option to go to parent directory
            }
            if (fileNames != null) {
                for (String name : fileNames) { // for every name in file name
                    File f = new File(curDir, name);
                    list.add(name + (f.isDirectory() ? "+" : "")); // adds a + to end of file name if its a directory
                }
            }
        } catch (Exception e) {
            e.printStackTrace();//prints the stack trace helps with debuging
            MessageLabel.setText("UPDATE LIST ERROR");
        }
    }
    
    public void displayFiles(String fileORdir) {
        try {
            if(fileORdir == null) {

                return;
            }

      //If the file is a directory this lower line removes the + so it can actually be accessed and not be seen as non existant
            File file_file = new File(curDir, fileORdir.replace("+", ""));
            
            if("...".equals(fileORdir)) { //if filordir equals "..." then...

                curDir = curDir.getParentFile();//navigates the list to the parent directory
                updateList();// updates the list
            } 
            else if(file_file.isDirectory()) { // if the file selected is a directory...
                //curDir=new File(curDir,fileORdir);

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

        } catch (Exception e) {

                e.printStackTrace(); // prinst the stack trace helps with debuging
            MessageLabel.setText("DISPLAYFILES ERROR");
        }
    }
    

     //========================ACTION HANDLER==========================================


    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource(); //get current source
            if (source == Ok) {

                //makes files for source and target i think im thinking about target wrong
                File sourceFile = new File(SourceText.getText());
                File targetFile = new File(SelectTarget.getText() + "/" + FileText.getText());
                
                if (!sourceFlag) { // if source is not specified
                    MessageLabel.setText("Source file is not specified");
                } else if (!targetFlag) {// if the target is not specified
                    MessageLabel.setText("target directory is not specified");
                } else {
                    copyFile(sourceFile, targetFile);// attempts to copy file
                }
            } else if (source == Target) {// checks if target button has been pressed

                targetFlag = true;// sets the flag
                SelectTarget.setText("Target: " + curDir.getAbsolutePath());

                MessageLabel.setText("");
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            MessageLabel.setText("ACTIONPERFORMED ERROR");
        }
    }
    
 //========================END ACTION HANDLER=======================================

  //========================COPY FILE=======================================


    private void copyFile(File source, File target) {

        File targetFile = new File(target, "CHILD.txt");

        try (BufferedReader read = new BufferedReader(new FileReader(source));
             PrintWriter writer = new PrintWriter(new FileWriter(targetFile))) {
            
            String lines;

            while ((lines = read.readLine()) != null) {

                writer.println(lines);
            }
            
            MessageLabel.setText("File copied EUREKA!");

        } catch (IOException e) {

            e.printStackTrace();
            MessageLabel.setText("An io errar occurred");
        }
    }

    //========================END COPY FILE=======================================
    
    
    //========================WINDOW LISTENER METHODS=================================

    //add all 6 Window Listener Methods
    public void windowClosing(WindowEvent e) {
        this.removeWindowListener(this);
        Ok.removeActionListener(this);
        Target.removeActionListener(this);
        this.dispose();
    }
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}
                 // MessageLabel.setText("A window activated");


    public void windowDeactivated(WindowEvent e) {}
            // MessageLabel.setText("A window deactivated");


    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
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

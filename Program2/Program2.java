/*******************HEADER*******************************/
/*  CMSC-3320 Technical Computing Using Java		    */   
/* 	File Processing Program							    */
/*	Group 1												*/
/*	Group Names: 										*/
/*     -Margo Bonal,      bon8330@pennwest.edu			*/
/*     -Luke Ruffing,     ruf96565@pennwest.edu 		*/
/*     -Ethan Janovich,   jan60248@pennwest.edu			*/
/*     -Nikolaus Roebuck, roe01807@pennwest.edu  		*/
/*******************END HEADER***************************/

/*--------LIBRARIES------------*/
import java.io.*;
import java.util.StringTokenizer;
/*--------END LIBRARIES---------*/

class Program2 {

    public static void main(String[] arg) throws IOException{
        //------------VARIABLES---------------------
        //initialize and assign variables 
        int i = 0;                              //array index
        int count =0;                           //number of Word objects in word array
        int sum = 0;                            //sum of integers
        String word = null;                     //token
        String num = null;                      //String of number
        String inbuffer;                        //input file buffer
        String[] ionames = new String[2];       //array for input and output file names
        Word[] words = new Word[100];           //Array of Word objects
        StringTokenizer inline;                 //StringTokenizer of current line
        BufferedReader infile;                  //input file handle
        PrintWriter outfile;                    //output file handle

        //------------END VARIABLES-------------------

        //psuedocode from lecture:
        /**
         * Uses getnames for the file names in ionnames 
         * opens the input file and output file
         * reads a line from the input file
         * while there is a line:
         *          convert the line into tokens using stringtokenizer
         *          if the token begins with an alpha:
         *                      checks if the word already exists:
         *                                      increment count
         *                      else creates the word object and add to the array
         *          else convert the number and accumulate it
         * print the word and counts
         * print the number of words and the sum of the integers
         * closes the input and output files
         */
    }
    //-------------ISINT----------------------------------
    static Boolean IsInt(String word){                                          //Function Purpose: method to check if string is an integer
        try {                                                                   //try_catch to process error handling
            Integer.parseInt(word);                                             //convert String word into an Integer
            return true;                                                        //Conversion successful, true

        } catch (NumberFormatException e) {     
            System.err.println("STRING TO INTEGER CONVERSION = FAILED!!!");   //display error msg
            return false;                                                       //Conversion failed, false
        }
    }
    //-------------END ISINT-------------------------------

    
}//end class Program2

class IOfile{
    //-------------GETNAMES----------------------------------
    public static boolean getnames(String[] args, String[] ioname){ //NOTE: I AM CONFUSUSED ON THIS PROCESS, STOPPED 1/26/25- 12:34PM


        BufferedReader keysIn = new BufferedReader(new InputStreamReader(System.in));// open keyboard to read

        try{
            switch(args.length){
                case 0:
                    System.out.print("ENTER Input Filename: ");
                    ioname[0] = keysIn.readLine();
                    System.out.print("ENTER Output Filename: ");
                    ioname[1] = keysIn.readLine();
                    break;

                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
                
            }

        }catch(IOException e){
            System.err.println("ERROR reading user input");
            return false;
        }
    }
    //-------------END GETNAMES------------------------------

    //-------------FILEEXIST----------------------------------
    boolean FileExist(String name){         //Function Purpose: to Return true/ false if exists
        boolean exist;                      //bool value to store if file exists
        File dummy = new File(name);        //store incoming file name in temp varaible
        exist = dummy.exists();             // use method .exists() to check if valid, store in variable exist
        return exist;                       //return the existing string of file name
    }
    //-------------END FILEEXIST------------------------------

    //-------------FILEBACKUP---------------------------------
    void FileBackup(String name, String ext){       //Function Purpose: renames specified file to same name and specified extention
        String file_part = FileName(name);          //store incoming file name
        String newname = file_part + ext;           //concatenate filename and extention
        File old = new File(name);                  //place name in temp location old                  
        File back = new File(newname);              //create backup file

        if(FileExist(newname)){                     //check if file exits
            back.delete();                          //if it does, delete it
        }
        old.renameTo(back);                         //rename old file to the back file                                                          
    }
    //-------------END FILEBACKUP------------------------------

    //-------------FILEEXTENTION---------------------------------
    String FileExtention(String name){                          //Function Purpose: extract the extention from name and returns the String extention
        String extention;                                       //varible to hold extention
        int start;                                              //varible to hold begining of parsed string
        start = name.lastIndexOf(".");                      //find  last index of the dot by using lastIndex method
        extention = name.substring(start+1, name.length());     //extract the substring from the position to the end 
        return extention;                                       // return the extention part of the string
    }
    //-------------END FILEEXTENTION-----------------------------

    //-------------FILENAME-------------------------------------
    String FileName(String name){                       //Function Purpose: extract file name from name and return the string file name
        int startIndex, endIndex;                       //variables to hold positioning
        startIndex = name.lastIndexOf("\\");        //find filename between \ and last dot of path, store in startIndex
        endIndex = name.lastIndexOf(".");           //find filename between \ and last dot of path, store in endIndex
        return name.substring(startIndex+1, endIndex);  //return the found subtring part of the filename
    }   
    //-------------END FILENAME---------------------------------

    //-------------FILEPATH-------------------------------------
    String FilePath(String name){                                           //Function Purpose: extract path name from name and return the string path
        return name.substring(0, name.lastIndexOf("\\"));    //return the found subtring path found between : and last\
    }                                                                         
    //-------------END FILEPATH---------------------------------

    //-------------OPENIN--------------------------------------
    BufferedReader openin(String name){                                     //Function Purpose: opens a file for reading specified by string name and returns bufferedreader              

        BufferedReader in = null;                                           //initialize buffered reader, set to null
        try {                                                               //try_catch to process opening errors
            in = new BufferedReader(new FileReader(name));                  //try to open buffered reader
            
        }catch (IOException e){                                             //catch IO errors,i.e: file opening errors
            System.err.println("ERROR opening/reading file!!!");          //display error message
        }
        return in;                                                          //return BufferedReader in
    }
    //------------END OPENIN-----------------------------------

    //-------------OPENOUT-------------------------------------
    PrintWriter openout(String name){                                       //Function Purpose: opens a file for writing specified by string name and returns printwriter              
        PrintWriter out = null;                                             //initialize PrintWriter, set to null
        try{                                                                //try_catch to process opening errors
            out = new PrintWriter(new FileWriter(name));                    //try to open PrintWriter, out
            
        }catch (IOException e) {                                            //catch IO errors,i.e: file opening errors
            System.err.println("ERROR opening/writing file!!!");          //display error message
        }
        return out;                                                         //return PrintWriter, out
    }                                                       
    //-------------END OPENOUT---------------------------------

}//end class IO



class Word{

    Word(String word){}
    int getCount(){}
    String getWord(){}
    boolean isWord(String word){}
    boolean isWordIgnoreCase(String word){}
    void addOne(){}
    void print(PrintWriter out){}
    int FindWord(Word[] list, String word, int n){}

}

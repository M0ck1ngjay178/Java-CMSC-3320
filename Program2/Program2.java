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

// Get the input/output libraries for java
import java.io.*;
//added structure from Lecure 4 video - M.Bonal, 1/23/25
import java.util.StringTokenizer;

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

    static Boolean IsInt(String word){}

    
}//end class Program2

class IOfile{
    //-------------GETNAMES----------------------------------
    public static boolean getnames(String[] args, String[] ioname){


        BufferedReader keysIn = new BufferedReader(new InputStreamReader(System.in));

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
    boolean FileExist(String name){
        boolean exist;
        //if(name){
            File dummy = new File(name);
            exist = dummy.exists();
            return exist;
       // }
    }
    //-------------END FILEEXIST------------------------------

    //-------------FILEBACKUP---------------------------------
    void FileBackup(String name, String ext){
        String file_part = FileName(name);
        String newname = file_part + ext;
        File old = new File(name);
        File back = new File(newname);

        if(FileExist(newname)){
            back.delete();
        }
        old.renameTo(back);

    }
    //-------------END FILEBACKUP------------------------------

    //-------------FILEEXTENTION---------------------------------
    String FileExtention(String name){
        String extention;
        int start;
        start = name.lastIndexOf(".");
        extention = name.substring(start+1, name.length());
        return extention;
    }
    //-------------END FILEEXTENTION-----------------------------

    //-------------FILENAME-------------------------------------
    String FileName(String name){
        int startIndex, endIndex;
        startIndex = name.lastIndexOf("\\");
        endIndex = name.lastIndexOf(".");
        return name.substring(startIndex+1, endIndex);
    }
    //-------------END FILENAME---------------------------------

    //-------------FILEPATH-------------------------------------
    String FilePath(String name){
        return name.substring(0, name.lastIndexOf("\\"));
    }
    //-------------END FILEPATH---------------------------------

    //-------------OPENIN--------------------------------------
    BufferedReader openin(String name){

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(name));
            
        }catch (IOException e){
            System.err.println("ERROR opening/reading file!!!");
        }
        return in;
    }
    //------------END OPENIN-----------------------------------

    //-------------OPENOUT-------------------------------------
    PrintWriter openout(String name){
        PrintWriter out = null;
        try{
            out = new PrintWriter(new FileWriter(name));
            
        }catch (IOException e) {
            System.err.println("ERROR opening/writing file!!!");
        }
        return out;
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

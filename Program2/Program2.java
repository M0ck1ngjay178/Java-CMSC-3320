/*******************HEADER*******************************/
/*  CMSC-3320 Technical Computing Using Java		    */   
/* 	Average Program										*/
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

    static Boolean IsInt(String s){}

    
}

class IOfile{

    boolean getnames(String[] args, String[] ioname){}
    boolean FileExist(String name){
        if(name){
            File dummy = new File(name);
            exists = dummy.exists();
        }
    }
    void FileBackup(String name, String ext){
        File old = new File(name);
        File back = new File(newname);

        if(newname.FileExist()){
            back.delete();
            old.renaleTo(back);//does this go in an else? or in the if??
        }
    }
    String FileExtention(String name){}
    String FileName(String name){}
    String FilePath(String name){}
    BufferedReader openin(String name){}
    PrintWriter openout(String name){}
}

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

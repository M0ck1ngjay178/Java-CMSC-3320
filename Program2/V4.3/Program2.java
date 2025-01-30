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
        //String num = null;                      //String of number, WHAT IS THIS FOR??? I DIDNT USE IT??
        String inbuffer;                        //input file buffer
        String[] ionames = new String[2];       //array for input and output file names
        Word[] words = new Word[100];           //Array of Word objects
        StringTokenizer inline;                 //StringTokenizer of current line
        BufferedReader infile;                  //input file handle
        PrintWriter outfile;                    //output file handle
        boolean check;
        //------------END VARIABLES-------------------

        //-------GET FILE NAMES--------
        IOfile.getnames(arg, ionames);  		//gets file names
        //----------------------------

        //----------OPEN INPUT AND OUTPUT-------------------
        infile = new BufferedReader(new FileReader(ionames[0]));	//opens input file
        outfile = new PrintWriter(new FileWriter(ionames[1]));		//opens output file
        //-----------------------------------------------------

        //------------WHILE: READ LINE FROM INPUT------------------------
        while ((inbuffer = infile.readLine()) != null){
            inline = new StringTokenizer(inbuffer);
            
            while (inline.hasMoreTokens()) {
                word = inline.nextToken();
                
                // If the token begins with an alpha character
                if (Character.isLetter(word.charAt(0))) {
                    check = false;
                    for (i = 0; i < count; i++) {
                        if (words[i].isWord(word)) {
                            words[i].addOne();
                            check = true;
                            break;
                        }
                    }
                    if(!check){
                        words[count] = new Word(word);
                        count++;
                    }
                } else if(IsPart(word)){    // if word is part int and part string, we need to seperate them
                    for( i=1; i<word.length(); i++){    // ran this loop just to get the index of the first letter/special character. (COULD GET INDEX FROM ISPART??)
                        if (Character.isLetter(word.charAt(i))){    //keeps i at the position of the first letter.
                            break;
                         }else if(!Character.isLetterOrDigit(word.charAt(i))){
                            break;
                         }
                    }
                    String word1=null;        // variable to get the number portion
                        String word2=null;    // variable to get the string portion
                        word1=word.substring(0, i);  //puts the number portion into word1
                        word2=word.substring(i, word.length()); //puts the string portion into word2
                        sum+= Integer.parseInt(word1);        // converts word1 into an int and adds it onto the sum.
                       
                        check = false;
                    for (i = 0; i < count; i++) {
                        if (words[i].isWord(word2)) {   //if word2 = any word in words
                            words[i].addOne();         //add one to that word count
                            check = true;
                            break;
                        }
                    }
                    if(!check){                  // if word2 is unique, create a new word in words
                        words[count] = new Word(word2);
                        count++;
                    }
                    
                }else if (IsInt(word)) { // If number
                    sum += Integer.parseInt(word);
		
                }
            }
        }
        //-------------------END WHILE-------------------------------------
        
        outfile.println("---------OUTPUT FILE HEADER-----------------");
        // Print words and their counts
        for (i = 0; i < count; i++) {
            words[i].print(outfile);
        }
        outfile.println("--------------------------------------------");
        
        //-------PRINT TO FILE--------------------
        outfile.println("------RESULTS------");
        outfile.println("WORDS: " + count);
        outfile.println("SUM: " + sum);
        outfile.println("--------------------");
        //------------------------------------------

        //------CLOSE FILES-----
        infile.close();
        outfile.close();
        //----------------------



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
   
    // ISPART----------------------------
    static Boolean IsPart(String word){       //function purpose: method to check if string is part int and part string
        for(int i=1; i<word.length(); i++){    // this function is used after isLetter so know index 0 is not a letter, we can start at index 1, checking each char to see if it's a letter
            if (Character.isLetter(word.charAt(i))){ //if it is a letter we can return true, the string is part int and part string
                return true;
             }
             else if(!Character.isLetterOrDigit(word.charAt(i))){
                return true;
             }
        }
        return false;          // the function is not part int and part string.
    }
//------------------------END ISPART------------------


}//end class Program2

class IOfile{
    //-------------GETNAMES----------------------------------
    public static boolean getnames(String[] args, String[] ioname){ //NOTE: I AM CONFUSUSED ON THIS PROCESS, STOPPED 1/26/25- 12:34PM

		String tempfile;
    
        BufferedReader keysIn = new BufferedReader(new InputStreamReader(System.in));// open keyboard to read

        try{
            switch(args.length){    //should this be ioname.length? M.B.B-> NO! i dont think so, this check the string argument list to pull the file names
                case 0:				//Case if no arguments are in command line
                    System.out.print("ENTER Input Filename: ");
                    ioname[0] = keysIn.readLine();
                    
                    if(FileExist(ioname[0])==false) {
	               	 System.out.println("ERROR!!! Input file doesnt exist");
	               	 return false;
                	 }
                	 
                    System.out.print("ENTER Output Filename: ");
                    
		             while(FileExist(tempfile = keysIn.readLine()) == true){
	                    System.out.print("ERROR: Output file already exists. "); 
	                    System.out.println("Please enter a new output file name!!"); 
                    }
                    
		            ioname[1] = tempfile;
                    break;
                    

                case 1:				//Case if only input is in command line
                	 
                	if(FileExist(args[0])== false) {
	               		 System.out.println("ERROR!!! Input file doesnt exist");
	               		 return false;
                	 }
                	 
                	ioname[0] = args[0]; 
		            System.out.print("ENTER Output Filename: ");  //if length is one, we have the input file name, need output file
                   
		           	
		             while(FileExist(tempfile = keysIn.readLine()) == true){
	                    System.out.print("ERROR: Output file already exists. "); 
	                    System.out.println("Please enter a new output file name!!"); 
                    }
                    
		            ioname[1] = tempfile;
                    break;
                    
                    //INPUT FILE MUST EXIST OUTPUT MUST NOT EXIST
                case 2:							//Case if both arguments are in command line
               	 if(FileExist(args[0])== true) {
	               	 ioname[0] = args[0];
                	 }
                	 
                	else{
	                System.out.println("ERROR!!! Input file doesnt exist");
	                return false;
	                }
	                
                 if(FileExist(args[1])== false) {
	               	 ioname[1] = args[1];			//if output file doesnt exist: ioname[1] = args[1]
                  	}
                  	
                	else{
	                	tempfile = args[1];
	                	while(FileExist(tempfile) == true){ //if output does already exist
	                		System.out.print("ERROR: Output file already exists. "); 
	                		System.out.println("Please enter a new output file name!!"); 
	                		tempfile = keysIn.readLine();
                    	}
                    	
		            ioname[1] = tempfile;
		             System.out.println("Input and Output Files Recived");
		             break;
	                }
                    
	                
	            
                    
                    System.out.println("Input and Output Files Recived From Command Line");
                    break;     // if length is 2 we have both file names already

                default:
                    break; 
            }
               
                
                

        }catch(IOException e){
            System.err.println("ERROR reading user input");
            return false;
        }
        
       return true;       // returns true if we have both file names
    }
    //-------------END GETNAMES------------------------------

    //-------------FILEEXIST----------------------------------
    public static boolean FileExist(String name){         //Function Purpose: to Return true/ false if exists
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
	
	private String word;
	private int quant;

    Word(String word){
	    this.word = word;											 //constructs word to be set as "word"
	    this.quant = 1;												//constructs quant to be set to 1
	    }
	    
    int getCount(){
	    return quant;												//returns the quantity of the current object
	    }
	    
    String getWord(){
	    return word;												//returns the word of the current object
	    }
	    
    boolean isWord(String word){
	    return this.word.equals(word);								//checks if the parameters word matchs the objects word
	    }
	    
    boolean isWordIgnoreCase(String word){
	    return this.word.equals(word);								//checks if the parameters word matchs the objects word, ignoring the case.
	    }
	    
    void addOne(){
	    this.quant += 1;											//Adds one to the quant object
	    }
	    
    void print(PrintWriter out){
        //out.println(word+"\t\t\t"+quant); 								//prints this object word and quantity to the PrintWriter
	    out.println(String.format("%-20s %5d", word, quant));
    }
	    
    int FindWord(Word[] list, String word, int n){
	    
	    return 1; // ADDED FOR COMPILATION
	    }

}


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
         * 
         * 
         * 
         * //if(!FileExists()){
                    //  System.err.println("ERROR!!! Both files need to exist");
                    //  return false;
                    //}
         */
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

class Program2{

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
       if( (IOfile.getnames(arg, ionames)  )== true){//this IF is used incase user wants to end program without opening any files
                 									//Also grabs the file names
        //----------------------------

        //----------OPEN INPUT AND OUTPUT-------------------	        		
	        infile = new BufferedReader(new FileReader(ionames[0]));	//opens input file
            outfile = new PrintWriter(new FileWriter(ionames[1]));	    //opens output file           
        //-----------------------------------------------------

        //------------WHILE: READ LINE FROM INPUT------------------------
        while ((inbuffer = infile.readLine()) != null){
            inline = new StringTokenizer(inbuffer, "\t\r\n !@#$%^&*()_/?;:<>.,{}[]\\");     //set stringtokenizer with delimeters
            
            while (inline.hasMoreTokens()) {                //while look to run on condition of moreTokens
                word = inline.nextToken();                  //pull tokens from line read from keayboard
                
                // If the token begins with an alpha character
                if (Character.isLetter(word.charAt(0))) {
                    check = false;                              //set check condition to false
                    for (i = 0; i < count; i++) {               //for loop to continue until count is greater
                        if (words[i].isWord(word)) {            //if a word is a work, store it in the words array
                            words[i].addOne();                  //use of function .addOne()
                            check = true;                       //set check to true
                            break;                              //break out of condition
                        }
                    }
                    if(!check){                                 //if check is flase
                        words[count] = new Word(word);          //the word is unique, not found, and needs added to the word array
                        count++;                                //increment count
                    }
                } else if(IsPart(word)){                                 // if word is part int and part string, we need to seperate them
                    for( i=1; i<word.length(); i++){                    // ran this loop just to get the index of the first letter/special character. (COULD GET INDEX FROM ISPART??)
                        if (Character.isLetter(word.charAt(i))){        //keeps i at the position of the first letter.
                            break;
                         }else if(!Character.isLetterOrDigit(word.charAt(i))){
                            break;
                         }
                    }
                    String word1=null;                            // variable to get the number portion
                        String word2=null;                       // variable to get the string portion
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
        infile.close();  //close input file
        outfile.close(); //close outputfile
        //----------------------

	}
	else{
	    System.err.println("Program Finished and Opened No Files"); //print error if no files were opened, triggered by enter
    }
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
    static Boolean IsPart(String word){             //function purpose: method to check if string is part int and part string
        for(int i=1; i<word.length(); i++){          //this function is used after isLetter so know index 0 is not a letter, we can start at index 1, checking each char to see if it's a letter
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
    public static boolean getnames(String[] args, String[] ioname){ 

		//String tempfile;
    
        BufferedReader keysIn = new BufferedReader(new InputStreamReader(System.in));// open keyboard to read
		
        try{
            switch(args.length){                                    //this check the string argument list to pull the file names
                case 0:				                                //Case if no arguments are in command line
                    System.out.print("ENTER Input Filename: ");   //enter input file prompt
                    ioname[0] = keysIn.readLine();                  //read from sting arguments from cmd
                    
                    if(ioname[0].equals("")){	           //check if ioname has an argument
	                   return false;  
                    }
                    
                    while(FileExist(ioname[0])==false) {            //if file does not exits, initiate process of reprompting
	               	  System.out.println("ERROR: Input File Does Not Exist. Please Enter a Valid Input File");
	               	  ioname[0] = keysIn.readLine();                //read again, this time read from the keayboard
                	 }
                	 
                    System.out.print("ENTER Output Filename: ");      //read filename for outputfile
                    ioname[1] = keysIn.readLine();                      //read from keyboard
                    
		              if(FileExist(ioname[1])== false){                          //check if output file exists, by calling fileexists function
			           	System.out.println("Input and Output Files Recived");	 //if exists, display msg
	               		break;                                                   //break condition
                  	}
                	else{
	                	
	                	if(FileExist(ioname[1]) == true){                   //if output does already exist
	                		System.out.println("ERROR: Output file already exists Choose one of the following. "); //display error msg, file already exists
	                		 	                		
	                		if(!OptionPick(ioname, keysIn)){			// gives options incase output file already exists
		                		return false;						    //returns false to getnames method to end program 
	                		}                			                		
                    	}						
		            
		             System.out.println("Input and Output Files Recived");	    //display msg that files now recived        
	                }
                
                    break;                                                         //break out of case0
                //------------------CASE1------------------------------------------------------
                case 1:				//Case if only input is in command line 
                ioname[0] = args[0];
                	while(FileExist(ioname[0])==false) {
	               	 System.out.println("ERROR: Input File Does Not Exist. Please Enter a Valid Input File");  //display error msg for input
	               	  ioname[0] = keysIn.readLine();                         //read valid input file from keyboard
	               	  
	               	   if(ioname[0].equals("")){			        //if user enters equal, break program            
	                    return false;                                       
                  	  }	               	  
                	}
                        System.out.println("Input File Now Successful!");   //display that input file now sucessfull
               	
		            System.out.print("ENTER Output Filename: ");  //if length is one, we have the input file name, need output file
                   
		           	ioname[1] = keysIn.readLine();
		           	
		           	if(ioname[1].equals("")){			            
	                   return false;	            
                    }
                    
		             if(FileExist(ioname[1])== false){
			             System.out.println("Input and Output Files Recived");	
	               		break;
                  	}
                	else{
	                	
	                	if(FileExist(ioname[1]) == true){ //if output does already exist
	                		System.out.println("ERROR: Output file already exists Choose one of the following. "); 
	                		 	                		
	                		if(!OptionPick(ioname, keysIn)){			// gives options incase output file already exists
		                		return false;						    //returns false to getnames method to end program 
	                		}                			                		
                    	}						
		            
		             System.out.println("Input and Output Files Recived");		           
	                }
                
                    break;
                //------------------END CASE1------------------------------------------------------
                //INPUT FILE MUST EXIST OUTPUT MUST NOT EXIST
                
                //------------------CASE2------------------------------------------------------
                case 2:							        //Case if both arguments are in command line
               	 if(FileExist(args[0])== true) {        //check if file exists
	               	 ioname[0] = args[0];               //if true, pull name from arguments
                	}
                	else{ 
	                		                	
	                    System.out.println("ERROR!!! Input file doesnt exist, Reprompt: ");   //reprompt
                        ioname[0] = keysIn.readLine();                                          //read input file from keyboard
                         if(ioname[0].equals("")){			                            //if user supplies enter, return false to break
	                    return false;                   
                    }
                    
                        System.out.println("Input File Now Successful!");                  
                 
	                }
	                
                 if(FileExist(args[1])== false){
	               	 ioname[1] = args[1];			//if output file doesnt exist: ioname[1] = args[1]
                  	}
                	else{
	                	
	                	if(FileExist(args[1]) == true){ //if output does already exist
	                		System.out.println("ERROR: Output file already exists Choose one of the following. "); 
	                		 
	                		
	                		if(!OptionPick(args, keysIn)){			// gives options incase output file already exists
		                		return false;						//returns false to getnames method to end program 
	                		}
	                		ioname[1] = args[1];
	                		
                    	}
                    	else{
	                    	ioname[1] = args[1];
	                    	}
		            
		             System.out.println("Input and Output Files Recived");
		          
		             break;
	                }
                    
                    System.out.println("Input and Output Files Recived From Command Line");
                    break;     // if length is 2 we have both file names already

                default://what goes in defualt??
                    System.out.println("default");
                    break; 
            }
            //------------------END CASE2------------------------------------------------------

            
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
    static void FileBackup(String name, String esxt){       //Function Purpose: renames specified file to same name and specified extention
        String file_part = FileName(name);          //store incoming file name
        String newname = file_part + esxt;           //concatenate filename and extention
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
    static String FileName(String name){                       //Function Purpose: extract file name from name and return the string file name
        int startIndex, endIndex;                              //variables to hold positioning
        startIndex = name.lastIndexOf("\\");               //find filename between \ and last dot of path, store in startIndex
        endIndex = name.lastIndexOf(".");                  //find filename between \ and last dot of path, store in endIndex
        return name.substring(startIndex+1, endIndex);         //return the found subtring part of the filename
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

    //------------Case conditions: helper function for IO.getnames()----
    public static Boolean OptionPick(String[] ioname, BufferedReader keysIn)throws IOException{ //NOTE: i was gonna then call this in getnames, if there is an output file then options are available
    //then if not dont show them, then check if null, terminate
        boolean look = true;                //bool for while statement
        String getChoice;
        if(FileExist(ioname[1])){
            //Banner for special options
            System.out.println("------Pick Option-------");
            System.out.println("--[B] Back Up File    --");
            System.out.println("--[O] Overwrite File  --");
            System.out.println("--[N] New Output File --");
            System.out.println("--[Q] Quit            --");
            System.out.println("------------------------");
        }
        while(look){
            getChoice = keysIn.readLine();                          //read choice from user
            switch (getChoice.toUpperCase()) {                      //force choice to upper case
                
                case "B":							                // BACKUP WORKS
                    FileBackup(ioname[1], ".bak");             //use FileBackup function to back up the outputfile, giving it a new extention of .bak  
                    System.out.println("Backup Created. Please Choose Another Option"); //display completion msg
                    break;
                    
                    
                case "O":										//OVERWRITE WORKS
               			 look = false;
               			 System.out.println(ioname[1] + " Has Been Overwritten");
                		break; // if overwrite i think it just break and use. SWAG :)
                		
                case "N":										                  // New Overwrite works and it makes a new file and uses that instead of the one entered
                	 System.out.print("Give A New Output File Name: ");         //prompt for new name for out file
                    ioname[1] = keysIn.readLine();                                //read new name from keyboard
                    if (ioname[1] == null || ioname[1].isEmpty()){                //check for empyt or null
                        ioname[1] = null; // Quit                                 //null will quit
                        System.out.print("THE END IS NEAR ");                   //exit msg
             
                    }
                     look = false;                                              //set look to false, to exit case
                    break;
                
                case "Q":	//QUIT WORKS!
                    System.out.println("Quiting...");   //display quit msg
                    return false;

                default:
                    return false;

                }
        }
        return true;
				
    }
    //------------------------------------------------------------------

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
        //prints this object word and quantity to the PrintWriter
	    out.println(String.format("%-20s %5d", word, quant));
    }
	    
    int FindWord(Word[] list, String word, int n){
	    
	    return 1; // ADDED FOR COMPILATION
	}

}

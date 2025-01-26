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
/*=====================CLASS AVERAGE======================================*/
// create the main class for the program
public class Average{

	
	public static void main(String[] args) throws IOException
	{
		//---------------WELCOME BANNER------------------------------------
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("                   -WELCOME TO THE AVERAGE PROGRAM-                      ");
        System.out.println("  Enter a value between 0-100 inclusive to add a grade to be calculated. ");
        System.out.println(" Enter a value outside 0-100 to end the calculations and display results.");
		System.out.println("-------------------------------------------------------------------------");
		//---------------END WELCOME BANNER---------------------------------


		//Initialize Input Handler
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		//------------VARIABLES---------------------
        //initialize and assign variables 
		String line;			//variable for read input data
		double grade = 0; 		//variable for parsed grade out of String line
		int gradecount = 0;		//variable to track how many grades entered
		int gradeIteration = 0;	//variable to track iteration number of grades entered, seperate from gradecount for division protection
		double gradestotal = 0;	//variable to track total sum of grades entered
		double avggrade = 0;	//varaible to store calculated average of total grades
		double numResult = 0;	//varaible to store calculated average of total grades, seperately used for valid check
		Double validTest;		//wrapper instance of the grade average
		//------------END VARIABLES-------------------

		//---------------MAIN PROCESSING LOOPS------------------------------
		// loop the input for the grades until out of range
		while(grade >= 0.0 && grade <= 100.0){
			System.out.print("Enter Grade " +(gradeIteration + 1)+ " : ");
			System.out.flush();
			line = stdin.readLine();
			grade = Double.parseDouble(line);
			
		// if within range, add to the total and grade count
			if(grade >= 0.0 && grade <= 100.0){
				gradestotal += grade;	//sum grades
				gradecount++;			//count grades
				gradeIteration ++; 		//track iteration
 			}//end if
		}//end while
		//---------------END MAIN PROCESSING LOOPS------------------------------
		
		//--------------------CALCULATIONS---------------------------------------
		numResult = gradestotal / gradecount;   //store grade average in seperate variable so not corrupted
		validTest = Double.valueOf(numResult); //use wrapper method to store instance of te average
		
		//-----------------VALID CHECK---------------------------------------------
		if(validTest.isNaN()){ //check if the average is not a number
			System.out.println("INVALID: Result is Not a Number!!!"); //print error msg for NaN
		}else if(validTest.isInfinite()){//check if the average is infinite
			System.out.println("INVALID: Result is Infinite!!!"); //print error msg for infinite
		//-----------------VALID CHECK---------------------------------------------

		}else{
			avggrade = gradestotal / gradecount;  // calculate average
		
			// print all the data collected and calculated
			System.out.println("\n---------FINAL CALCULATIONS--------------");
			System.out.println("Total Sum of Grades:  " + gradestotal);
			System.out.println("There were " + gradecount+ " Valid Grades");
			System.out.println("Average Grade:  " + avggrade);
			System.out.println("-----------------------------------------");
			}//end else
		//--------------------END CALCULATIONS---------------------------------------

	}//end main
	
}//end class
/*=====================END CLASS AVERAGE======================================*/



package Program1; // for Vs Code
import java.io.*;
//import java.io.BufferedReader;


public class Average{

    //---------------GLOBAL VARIABLES------------------
    double grade, totalGrades; // type double variables for grade input and total sumed grades
    int gradeCount; // type integer counter for # of grades
    String line;    //input variable for line reading
    //-------------END GLOBAL VARIABLES------------------

    
    public Average(){
        //program functioning 
        //BufferedReader stdin = new BufferedReader(is);
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter A String");
        System.out.flush();
        //line = stdin.readLine() ;error 
        System.out.println("You Enterend: " +line+"\n");
        
    }

    //new functions



    /****************MAIN***********************/
    public static void main(String[] args) throws IOException{

        System.out.println("!!Welcome to the average program!!");
        new Average();

    }
    /****************END MAIN********************/
    
}

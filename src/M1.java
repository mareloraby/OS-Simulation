import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files



public class M1 {

    static ArrayList<String> ReadProgram(String path){
        ArrayList<String> arr = new ArrayList<>();

        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                arr.add(line);
                //System.out.println(line);
            }
            myReader.close();
            return arr;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }

    }

    static void printVariable(String vname)
    {
        //DO Something
        System.out.print("print what's inside the variable");
    }

    static String userInput(){

        Scanner sc= new Scanner(System.in);
        System.out.print("input: ");
        return sc.nextLine();

    }



    public static void main(String [] args){

        ArrayList<String> program = ReadProgram("Resources/Program 1.txt");
        String userInput;

        for (String line : program){
            if (line.contains("print")){
                String [] d = line.split("\\s");
                if (Character.isUpperCase(d[1].charAt(0)))
                    System.out.println(d[1]);
                else printVariable(d[1]);

            }
            if (line.contains("input")){
                userInput= userInput();

            }

        }



    }





}

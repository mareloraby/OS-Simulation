import java.io.*;
import java.util.*;


public class M1 {
    static Hashtable<String, String> Memory;


    static ArrayList<String> ReadProgram(String path) {
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

    static void printVariable(String vname) {
        //Check if variable exists
        System.out.print(readFromMemory(vname) + "");
    }

    static String userInput() {
        BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
        System.out.print("input: ");
        try {
            return br.readLine();

        } catch (IOException e) {
            System.out.println("Error Taking the input");
        }
        return null;
    }

    static void assign(String variable, String[] values) {
        String s = null; // String to be inserted
        if (values.length == 1) {
            if (values[0].equals("input")) {
                s = userInput();
            } else {
                String c = readFromMemory(values[0]);
                if (c != null) {
                    s = c;
                } else {
                    s = values[0];
                }
            }
        } else {
            if (values[0].equals("readFile")) {
                s = readFile(values[1]);
            }
        }
        if (s != null) {
            writeInMemory(variable,s);
        }
    }

    public static void Addition(String x, String y) {
        String a = readFromMemory(x);
        String b = readFromMemory(y);
        if (a!=null && b!=null) {
            try {
                int m = Integer.parseInt(a);
                int n = Integer.parseInt(b);
                writeInMemory(x, (m + n) + "");
            } catch (NumberFormatException e) {
                System.out.println("Cannot add Strings");
            }
        }
    }
    public static String readFromMemory(String x){
        return Memory.get(x);
    }
    public static void writeInMemory(String x, String y){
        Memory.put(x, y);
    }

    public static void writeFile(String x, String y){
        String z =readFromMemory(y);
        if(z!= null){
            y= z;
        }
        try{
            FileWriter myWriter = new FileWriter("Resources/Files/"+ x+".txt");
            myWriter.write(y);
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("File cant be created :(");
        }
    }

    public static String readFile(String x){
        File myObj = new File("Resources/Files/"+ x+".txt");
        String z =readFromMemory(x);
        if(z!= null){
            x= z;
        }
        StringBuilder result= new StringBuilder();
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                result.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File "+x +" does not exist");
        }
        return result.toString();
    }
    public static void executeProgram(String programPath){
        ArrayList<String> program = ReadProgram(programPath);
        for (String line : program) {
            if (line.contains("print")) {
                String[] d = line.split(" ");
                if (Character.isUpperCase(d[1].charAt(0)))
                    System.out.println(d[1]);
                else printVariable(d[1]);
            } else {//code for every thing other than input and print
                String[] lineWords = line.split("\\s");
                switch (lineWords[0]) {
                    case "assign":
                        assign(lineWords[1], Arrays.copyOfRange(lineWords, 2, lineWords.length));
                        break;
                    case "add":
                        Addition(lineWords[1],lineWords[2]);
                        break;
                    case "writeFile":
                        writeFile(lineWords[1],lineWords[2]);
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Memory = new Hashtable<>();
        String programPath = "Resources/Program 3.txt";
        executeProgram(programPath);
    }
}

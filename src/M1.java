import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.lang.reflect.Member;
import java.util.*;


public class M1 {
    static Hashtable<String, String> Memory;
    private static String userInput;


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
        System.out.print(Memory.get(vname) + "");
    }

    static String userInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("input: ");
        String temp = sc.nextLine();
        sc.close();
        return temp;
    }

    static void assign(String variable, String[] values) {
        String s = null; // String to be inserted
        if (values.length == 1) {
            if (values[0].equals("input")) {
                s = userInput();
            } else {
                String c = Memory.get(values[0]);
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
            Memory.put(variable, s);
        }
    }

    public static void Addition(String x, String y) {
        if (Memory.containsKey(x) && Memory.containsKey(y)) {
            try {
                int a = Integer.parseInt(Memory.get(x));
                int b = Integer.parseInt(Memory.get(y));
                Memory.put(x, (a + b) + "");
            } catch (NumberFormatException e) {

            }
        }
    }

    public static void main(String[] args) {
        Memory = new Hashtable<>();
        ArrayList<String> program = ReadProgram("Resources/Program 2.txt");
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
                }
            }
        }
    }
}

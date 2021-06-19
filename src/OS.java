import java.io.*;
import java.util.*;

/* Memory:
 ---------------------------------------------------------------------------------------
| P1                   |    P2                | P3                                     |
| pcb1 variables1 loc3 | pcb2 variables2 loc3 | pcb3 variables3 loc3                   |
|  4       5       10
 ---------------------------------------------------------------------------------------



pcb size: 4
vars size: 5
loc size: 10

size of memory : 1900 word?


PCB:
1. Process ID (Assume that the ID corresponds to the program number)
2. Process State
3. Program Counter
4. Memory Boundaries ()
* */


public class OS {
    static int I= 1;
    static Object [] BigMemory = new Object [1900];
    static int PID = 0;
    static Queue<Integer> ReadyQueue = new LinkedList<>();
    static class Variable{
        String key;
        String value;
        public Variable(String key,String value){
            this.key = key;
            this.value=value;
        }
    }

    static void assignLocs(String path)
    {

        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            int p =0;
            while(p<1900){
                if (BigMemory[p]==null) {
                    break;
                }
                p+=19;
            }
            //PCB
            BigMemory[p] = new Variable("Process ID",p+"");
            BigMemory[p+1] = new Variable("Process State","Ready");
            BigMemory[p+2] = new Variable("Program Counter",(p+9)+"");
            BigMemory[p+3] = new Variable("Memory Boundry",(p+18)+"");
            int l = 0;
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                BigMemory[p + 9 + l] = line;
                l++;
            }
            myReader.close();
            ReadyQueue.add(p);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static Hashtable<String, String> Memory;

    //System Calls:
    public static String readFromMemory(String x){
        return Memory.get(x);
    }

    public static void writeInMemory(String x, String y){
        Memory.put(x, y);
    }


    public static void writeInBigMemory(String x, String y){
        Variable var = new Variable(x,y);

        for (int v = (PID+4); v < PID+9; v++){

            if (BigMemory[v] == null){
                BigMemory[v] = var;
                break;
            }

            if (((Variable) BigMemory[v]).key ==var.key){
                ((Variable) BigMemory[v]).value = var.value;
                break;
            }

        }

    }

    public static String readFromBigMemory(String x){

        for (int v = (PID+4); v < PID+9; v++){
            if (((Variable) BigMemory[v]).key ==x){
                return ((Variable) BigMemory[v]).value;
            }
        }
        return null;
    }



    public static void writeFile(String x, String y){
        String z =readFromBigMemory(y);
        if(z!= null){
            y= z;
        }
         z =readFromMemory(x);
        if(z!= null){
            x= z;
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
        String z =readFromBigMemory(x);
        if(z!= null){
            x= z;
        }
        File myObj = new File("Resources/Files/"+ x+".txt");
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
   //-----------------------------------------------------------------------------------------------------
    static void assign(String variable, String[] values) {
        String s = null; // String to be inserted
        if (values.length == 1) {
            if (values[0].equals("input")) {
                s = userInput();
            } else {
                String c = readFromBigMemory(values[0]);
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
            writeInBigMemory(variable,s);
        }
    }

    public static void add(String x, String y) {
        String a = readFromBigMemory(x);
        String b = readFromBigMemory(y);
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

    public static void executeProgram(String programName){
        programName = "Resources/"+ programName+".txt";
        ArrayList<String> program = ReadProgram(programName);
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
                        add(lineWords[1],lineWords[2]);
                        break;
                    case "writeFile":
                        writeFile(lineWords[1],lineWords[2]);
                        break;
                }
            }
        }
    }
    public static void executeInstruction(String instruction){
        if (instruction.contains("print")) {
            String[] d = instruction.split(" ");
            if (Character.isUpperCase(d[1].charAt(0)))
                System.out.println(d[1]);
            else printVariable(d[1]);
        } else {//code for every thing other than input and print
            String[] lineWords = instruction.split("\\s");
            switch (lineWords[0]) {
                case "assign":
                    assign(lineWords[1], Arrays.copyOfRange(lineWords, 2, lineWords.length));
                    break;
                case "add":
                    add(lineWords[1],lineWords[2]);
                    break;
                case "writeFile":
                    writeFile(lineWords[1],lineWords[2]);
                    break;
            }
        }
    }
    public static void schedule(){
        while(!ReadyQueue.isEmpty()){
            int index = ReadyQueue.poll();
            executeInstruction((String)BigMemory[(Integer.parseInt(((Variable)BigMemory[index+2]).value))]);
            ((Variable)BigMemory[index+2]).value=(Integer.parseInt(((Variable)BigMemory[index+2]).value)+1)+"";
            int second=(Integer.parseInt(((Variable)BigMemory[index+2]).value));
            if(second> (Integer.parseInt(((Variable)BigMemory[index+3]).value)) || BigMemory[second]==null){
                continue;
            }
            executeInstruction((String)BigMemory[(Integer.parseInt(((Variable)BigMemory[index+2]).value))]);
            ((Variable)BigMemory[index+2]).value=(Integer.parseInt(((Variable)BigMemory[index+2]).value)+1)+"";
            ReadyQueue.add(index);


        }

    }

    public static void main(String[] args) {
     //Memory = new Hashtable<>();
        assignLocs("Program 1");
        assignLocs("Program 2");
        assignLocs("Program 3");
        schedule();

    String programName = "Program 2";
   // executeProgram(programName);
    }
}

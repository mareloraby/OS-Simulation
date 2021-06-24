import java.io.*;
import java.util.*;

/* Memory:
 ---------------------------------------------------------------------------------------
| P1                   | P2                   | P3                                     |
| pcb1 variables1 loc1 | pcb2 variables2 loc2 | pcb3 variables3 loc3                   |
|  5       5       10
 ---------------------------------------------------------------------------------------



pcb size: 5
vars size: 5
loc size: 10

size of memory : 2000 word?


PCB:
1. Process ID (Assume that the ID corresponds to the program number)
2. Process State
3. Program Counter
4. Memory Boundaries ()
* */


public class OS {
    static int I= 1;
    static Object [] BigMemory = new Object [2000];
    static int PID = 0;
    static Queue<Integer> ReadyQueue = new LinkedList<>();
    static HashMap<Integer,Integer> quantas=new HashMap<>();

    static class Variable{
        String key;
        String value;
        public Variable(String key,String value){
            this.key = key;
            this.value=value;
        }

        @Override
        public String toString() {
            return "(" +
                    "key:'" + key + '\'' +
                    ", value:'" + value + '\'' +
                    ')';
        }
    }



    static void assignLocs(String programName)
    {

        try {
           // System.out.println(programName);
            File myObj = new File("Resources/"+ programName+".txt");
            Scanner myReader = new Scanner(myObj);
            int p =0;
            while(p<2000){
                if (BigMemory[p]==null) {
                    break;
                }
                p+=20;
            }


            //PCB
            quantas.put(Integer.parseInt(programName.split(" ")[1]),0);
            BigMemory[p] = new Variable("Process ID",programName.split(" ")[1]);
            BigMemory[p+1] = new Variable("Process State","Not Running");
            BigMemory[p+2] = new Variable("Program Counter",(p+10)+"");
            BigMemory[p+3] = new Variable("Memory Upper Boundary",p+"");
            BigMemory[p+4] = new Variable("Memory Lower Boundary",(p+19)+"");

            System.out.println("Writing PCB of Program " +programName.split(" ")[1] +" :" );
            printPCB(p);
            System.out.println();



            int l = 0;
            System.out.println("Writing LOC of Program " + programName.split(" ")[1]+ " : ");
            System.out.println("Index | Word");


            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                BigMemory[p + 10 + l] = line;

                System.out.print((p+10+l) + "    |" );
                System.out.print(" "+line +"\n");


                l++;
            }
            System.out.println("");
            System.out.println("--------------------------");
            System.out.println();




            myReader.close();
            ReadyQueue.add(p);
        //    System.out.println(Arrays.toString(BigMemory));
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static void printPCB(int p){
        System.out.println("Index | Word");
        int j=0;
        for (int i=p; i<p+5;i++, j++){
            System.out.print(i + "    |" );
            System.out.print(" "+BigMemory[i] +"\n");

        }
    }




    //System Calls:
    // M1
    static Hashtable<String, String> Memory;
    public static String readFromMemory(String x){
        return Memory.get(x);
    }

    public static void writeInMemory(String x, String y){
        Memory.put(x, y);
    }

    //M2
    public static void writeInBigMemory(String x, String y){
        Variable var = new Variable(x,y);

        for (int v = (PID+5); v < PID+10; v++){

            if (BigMemory[v] == null){
                System.out.println("| Writing Word: " +  var +" At index: " + v);
                BigMemory[v] = var;
                break;
            }

            if (((Variable) BigMemory[v]).key.equals(var.key)){
                System.out.println("| Writing Word: " + var +" At index: " + v);
                ((Variable) BigMemory[v]).value = var.value;
                break;
            }

        }

    }

    public static String readFromBigMemory(String x){


        for (int v = (PID+5); v < PID+10 && BigMemory[v]!= null; v++){
            if (((Variable) BigMemory[v]).key.equals(x)){
                System.out.println("| Reading Word: " + ((Variable) BigMemory[v]).toString() +" From index: " + v);
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
         z =readFromBigMemory(x);
        if(z!= null){
            x= z;
        }
        try{
            FileWriter myWriter = new FileWriter("Resources/Files/"+ x+".txt");
            myWriter.write(y);
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("| File cant be created :(");
            e.printStackTrace();
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
            System.out.println("| File "+x +" does not exist");
        }
        return result.toString();
    }

    static void printVariable(String vname) {
        //Check if variable exists
        System.out.println( "| " + readFromBigMemory(vname) + "");
    }

    static String userInput() {
        BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
        System.out.print("| input: ");
        try {
            return br.readLine();

        } catch (IOException e) {
            System.out.println(" Error Taking the input");
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
            System.out.println(" An error occurred.");
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
                writeInBigMemory(x, (m + n) + "");
            } catch (NumberFormatException e) {
                System.out.println("| Cannot add Strings");
            }
        }
    }


    public static void executeProgram(String programName){ //Milestone 1
        programName = "Resources/"+ programName+".txt";
        ArrayList<String> program = ReadProgram(programName);
        for (String line : program) {
            if (line.contains("print")) {
                String[] d = line.split(" ");
                if (Character.isUpperCase(d[1].charAt(0)))
                    System.out.println(" "+d[1]);
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

    public static void executeInstruction(String instruction) { //Milestone 2

        if (instruction.contains("print")) {
            String[] d = instruction.split(" ");
            if (Character.isUpperCase(d[1].charAt(0)))
                System.out.println("| "+d[1]);
            else printVariable(d[1]);
        } else {//code for every thing other than input and print
            String[] lineWords = instruction.split("\\s");
            switch (lineWords[0]) {
                case "assign":
                    assign(lineWords[1], Arrays.copyOfRange(lineWords, 2, lineWords.length));
                    break;
                case "add":
                    add(lineWords[1], lineWords[2]);
                    break;
                case "writeFile":
                    writeFile(lineWords[1], lineWords[2]);
                    break;
            }
        }
    }

    public static void schedule(){
        while(!ReadyQueue.isEmpty()){
            PID = ReadyQueue.poll();


            int quanta = quantas.get(Integer.valueOf(((Variable)BigMemory[PID]).value)) ;

            quantas.replace(Integer.valueOf(((Variable)BigMemory[PID]).value), quanta,quanta+1 );

            System.out.println("Running Process " + ((Variable)BigMemory[PID]).value);
            // System.out.println(Arrays.toString(BigMemory));

            BigMemory[PID+1] = new Variable("Process State","Running");
            //execute(BigMemory[BigMemory[pc]]);

            System.out.println("| Reading Instruction: '"+(String)BigMemory[(Integer.parseInt(((Variable)BigMemory[PID+2]).value))]+"'" + " From index: " + (Integer.parseInt(((Variable)BigMemory[PID+2]).value)));
            executeInstruction((String)BigMemory[(Integer.parseInt(((Variable)BigMemory[PID+2]).value))]);


            //pc=pc+1
            ((Variable)BigMemory[PID+2]).value=(Integer.parseInt(((Variable)BigMemory[PID+2]).value)+1)+"";

            int second=(Integer.parseInt(((Variable)BigMemory[PID+2]).value));
            //if(pc> upperBoundry || Mem[pc]=null)
            if(second> (Integer.parseInt(((Variable)BigMemory[PID+4]).value)) || BigMemory[second]==null){
                System.out.println("Process "+((Variable)BigMemory[PID]).value  +" ENDED. It ran for " + quantas.get(Integer.valueOf(((Variable)BigMemory[PID]).value))  + " quantas");
                System.out.println();
                continue;
            }
            System.out.println();
            System.out.println("| Reading Instruction: '"+(String)BigMemory[(Integer.parseInt(((Variable)BigMemory[PID+2]).value))]+"'" + " From index: " + (Integer.parseInt(((Variable)BigMemory[PID+2]).value)));

            executeInstruction((String)BigMemory[(Integer.parseInt(((Variable)BigMemory[PID+2]).value))]);
            ((Variable)BigMemory[PID+2]).value=(Integer.parseInt(((Variable)BigMemory[PID+2]).value)+1)+"";
            BigMemory[PID+1] = new Variable("Process State","Not Running");
            int next=(Integer.parseInt(((Variable)BigMemory[PID+2]).value));
            if(!(next> (Integer.parseInt(((Variable)BigMemory[PID+4]).value)) || BigMemory[next]==null)) ReadyQueue.add(PID);
            else {
                System.out.println("Process "+((Variable)BigMemory[PID]).value  +" ENDED. It ran for " + quantas.get(Integer.valueOf(((Variable)BigMemory[PID]).value))  + " quantas");


            }


            System.out.println("");
            System.out.println("");

        }

    }

    public static void main(String[] args) {
//     //Memory = new Hashtable<>();
        assignLocs("Program 1");
//        assignLocs("Program 2");
//        assignLocs("Program 3");
        schedule();

//    String programName = "Program 2";
   // executeProgram(programName);

    }
}

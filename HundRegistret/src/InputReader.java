import java.util.Scanner;

public class InputReader {
    private Scanner scanner;
    private static boolean exists = false;

    public InputReader() {
       
        this(new Scanner(System.in)); // anropa den andra konstrukorn
       
        
    }

    public InputReader(Scanner scanner) {
        if(!exists){
        this.scanner = scanner;
        exists = true;
        }
        else{
            throw new IllegalStateException("error can only have 1 InputReader");
        }
    }

    public int readInt(String string) {
        int tempInt = -1;
        do {
            System.out.print(string + " ?>");
            if (scanner.hasNextInt()) {

                tempInt = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.print("error invalid input please enter a number\n");
                scanner.nextLine();
                tempInt = -1;
                continue;

            }
            if (tempInt < 0) {
                System.out.print("error invalid input number cant be less than 0\n");
                continue;
            }
        } while (tempInt < 0);
        //scanner.nextLine();
        return tempInt;

    }

    public String readString(String string) {
        
        String tempString = "";

        while (tempString == null || tempString.isBlank()) {

            System.out.print(string + " ?>");
            tempString = scanner.nextLine();
            tempString = tempString.trim();


            if (tempString == null || tempString.isBlank()) {
                System.out.print("error invalid input\n");
            }

        }
        return tempString;

    }
    public double readDouble(String string){
        double tempDouble = -1;
        do {
            System.out.print(string + " ?>");
            if (scanner.hasNextDouble()) {

                tempDouble = scanner.nextDouble();
                scanner.nextLine();
            } else {
                System.out.print("error invalid input please enter a number\n");
                scanner.nextLine();
                tempDouble = -1;
                continue;

            }
            if (tempDouble < 0) {
                System.out.print("error invalid input number cant be less than 0\n");
                continue;
            }
        } while (tempDouble < 0);
        //scanner.nextLine();
        return tempDouble;
    }
    public void close(){
        scanner.close();
    }
}

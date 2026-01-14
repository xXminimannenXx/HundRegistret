public class Tester {
    public static void main(String[] args){
        InputReader inputReader = new InputReader();
        int age = inputReader.readInt("enter dog age");
       while(age != 10){
        age = inputReader.readInt("enter dog age");
       }
       String name = inputReader.readString("enter owner name");
       while (!name.equals("Anton")) {
        name = inputReader.readString("enter owner name");
        
       }

    }
}

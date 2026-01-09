public class Tester {
    public static void main(String[] args){
        Dog hund1 = new Dog("kalle", "kallehund", 4, 5);
        Dog hund2 = new Dog("Jens", "kallehund", 4, 5);
        Dog hund3 = new Dog("Julia", "kallehund", 4, 5);
        Owner Anton = new Owner("Anton", hund1,hund2,hund3);
        System.out.print(Anton.toString());
       

    }
}

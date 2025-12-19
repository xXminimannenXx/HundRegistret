public class Dog {
    public final double TAX_TAIL_LENGHT = 3.7;
    String name;
    String breed;
    int age;
    int weight;
   

    public Dog(String name, String breed, int age, int weight, double tailLenght){

        this.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        this.breed = breed.substring(0,1).toUpperCase() + breed.substring(1).toLowerCase();
    }
    public String getName(){
        return name;
    }
    public String getBreed(){

        return breed;
    }
    public int getAge(){
        return age;
    }
    public int getWeight(){
        return weight;
    }
    public double getTailLenght(){ //TODO: fixa språk/standarisera de #supportaAllaSpråk
        
        if(breed != "Tax")return (age*weight)/10;
        else return TAX_TAIL_LENGHT;
        
    }
    @Override
    public String toString(){
        return name + breed + age + weight + getTailLenght();
    }
}


import java.util.List;

public class Dog {
    public static final double TAX_TAIL_LENGHT = 3.7;
    String name;
    String breed;
    int age;
    int weight;
    static final List<String> TAX_OLIKA_SPRÅK = List.of(
    "bassê",
    "bassotto",
    "broc_chú",
    "brochgi",
    "broghki",
    "cão-salsicha",
    "coo brock",
    "dachs",
    "dachshound",
    "dachshund",
    "dachshunde",
    "dackel",
    "dafadog",
    "dakel",
    "dakhund",
    "daks",
    "dakshund",
    "dakshunt",
    "dakszli",
    "dashhound",
    "dashond",
    "gravhund",
    "gravhundur",
    "grevlingshund",
    "jamnik",
    "jazbečar",
    "jazvečík",
    "jezevčík",
    "jôpscownik",
    "kolbasohundo",
    "mäyräkoira",
    "melhundo",
    "ofi' falaa'",
    "omnsrørhund",
    "omnsrøyrhund",
    "ovnsrørhund",
    "perro salchicha",
    "pølsehund",
    "salsichinha",
    "tacskó",
    "taksas",
    "taksis",
    "tax",
    "teckel",
    "wiener_dog",
    "ντάκσχουντ",
    "да́кел",
    "јазавичар",
    "такса",
    "та́кса",
    "ტაქસા",
    "דאַכסהונט טאַקסע",
    "דקל",
    "דָּקֶל",
    "תחש",
    "תַּחַשׁ",
    "ഡാഷ്ഹണ്ട്",
    "แด็กซันด์",
    "닥스훈트",
    "ダックスフント",
    "腊肠犬",
    "臘腸犬",
    "达克斯猎犬",
    "達克斯獵犬"
);


    public Dog(String name, String breed, int age, int weight){
        //göra en tax check på olika språk sedan standarisera inputen
        breed = breed.trim();
        breed = breed.toLowerCase();
        if(TAX_OLIKA_SPRÅK.contains(breed))breed = "Tax";
        name = name.trim();
        this.name = cap(name);
        this.breed = cap(breed);
        this.age = age;
        this.weight = weight;
        
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
    public double getTailLenght(){ 
        
        if(!"Tax".equals(breed))return (age*weight)/10; 
        else return TAX_TAIL_LENGHT;
        
    }
    @Override
    public String toString(){
        return name + " " + breed + " " + age  + " " + weight + " " + getTailLenght();
    }
    private String cap(String s){
        if(s.isEmpty()) return s;
        else{
            return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
        }
    }
}

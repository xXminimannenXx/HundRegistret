import java.util.List;
import java.util.Optional;

public class Dog {
    private static final double TAX_TAIL_LENGTH = 3.7;
    private static final List<String> DACHSHUND_ALIASES = List.of(
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
            "達克斯獵犬");
    private String name;
    private String breed;
    private int age;
    private int weight;
    private Owner owner;

    public Dog(String name, String breed, int age, int weight) {
        // göra en tax check på olika språk sedan standarisera inputen
        if (breed == null || breed.isBlank()) {
            throw new IllegalArgumentException("breed cant be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cant be null");
        }
        if (age < 0) {
            throw new IllegalArgumentException("age cant be negative");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("weight cant be negative");
        }

        breed = breed.trim();
        breed = breed.toLowerCase();
        if (DACHSHUND_ALIASES.contains(breed))
            breed = "Tax";
        name = name.trim();
        this.name = capName(name);
        this.breed = capName(breed);
        this.age = age;
        this.weight = weight;

    }

    public Dog(String name, String breed, int age, int weight, Owner owner) {
        this(name, breed, age, weight);

        this.owner = owner;

    }

    public String getName() {
        return name;
    }

    public String getBreed() {

        return breed;
    }

    public int getAge() {
        return age;
    }

    public Integer getWeight() {
        return weight;
    }

    public double getTailLength() {

        if (!"Tax".equals(breed))
            return ((double) age * (double) weight) / 10;
        else
            return TAX_TAIL_LENGTH;

    }

    @Override
    public String toString() {
        return name + " " + breed + " " + age + " " + weight + " " + getTailLength();
    }

    private String capName(String s) {
        if (s.isEmpty())
            return s;
        else {
            return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
    }

    public Optional<Owner> getOwner() {
        return Optional.ofNullable(owner);
    }

    public boolean setOwner(Owner o) {
        if (owner != o) { // om de inte är samma ägare så updatereas den annars gör den inte de eftersom
                          // de inte finns något att updatera och returenar fasle eftersom inget har
                          // ändrasts
            owner = o;
            return true;
        } else {
            return false;
        }

    }

    public void updateAge(int newAge) {

        if (newAge > age) {
            age = newAge;
        } else {
            throw new RuntimeException("Not a valid age");
        }
    }
}

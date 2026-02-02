import java.util.List;

public class Dog {
    private static final double DACHSHUND_TAIL_LENGTH = 3.7;
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
        this.name = capitalizeName(name);
        this.breed = capitalizeName(breed);
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

    public int getWeight() {
        return weight;
    }

    public double getTailLength() {

        if (!"Tax".equals(breed))
            return ((double) age * (double) weight) / 10;
        else
            return DACHSHUND_TAIL_LENGTH;

    }

    @Override
    public String toString() {
        if (owner != null) {
            return name + " " + breed + " " + age + " " + weight + " " + getTailLength() + " " + owner.getName();
        }
        return name + " " + breed + " " + age + " " + weight + " " + getTailLength();
    }

    private String capitalizeName(String name) {
        if (name.isEmpty())
            return name;
        else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }

    /*
     * fick problem av denna eftrsom jag fick en optional<owner> istället för en
     * owner och när jag sökte runt hur man kan lösa de så stötte jag på andra
     * lösningar där en av dem var att använda den singelton så de är vad jag väljer
     * att använda istället
     * Men när jag försökte använda de så klarade jag inte junit testerna så jag
     * läste om kraven för getOwner och har typ valt att bara kolla om de är null
     * istället på ställena de används för att jag inte helt förstår mig på optional
     * public Optional<Owner> getOwner() {
     * return Optional.ofNullable(owner);
     * }
     */
    public Owner getOwner() {

        return owner;
    }

    // första setOwner
    /*
     * public boolean setOwner(Owner o) {
     * 
     * if (owner != o) {
     * // om de inte är samma ägare så updatereas den annars gör den inte de
     * // eftersom
     * // de inte finns något att updatera och returenar fasle eftersom inget har
     * // ändrasts
     * 
     * owner = o;
     * return true;
     * } else {
     * return false;
     * }
     * 
     * }
     */
    /*
     * public boolean setOwner(Owner o){
     * if(o == null){
     * if(owner != null){
     * owner.removeDog(this);
     * owner = o;
     * return true;
     * }
     * }
     * if(!o.ownsMaxDogs()){
     * if(owner == null){
     * owner = o;
     * owner.addDog(this);
     * return true;
     * }
     * if(owner != null){
     * if(owner == o){
     * if(o.ownsDog(this)){
     * o.addDog(this);
     * return true;
     * }
     * }
     * if(owner != o){
     * owner.removeDog(this);
     * owner = o;
     * o.addDog(this);
     * }
     * }
     * }
     * return false;
     * }
     */
    public boolean setOwner(Owner newOwner) {
        if (owner == newOwner) { // ingen förändring
            return false;
        }

        // Ta bort från gammal ägare
        if (owner != null) {
            Owner oldOwner = owner;
            owner = null;
            oldOwner.removeDog(this);
        }

        // Lägg till hos ny ägare om det inte är null
        if (newOwner != null && !newOwner.ownsMaxDogs()) {
            owner = newOwner;
            newOwner.addDog(this); 
        }

        return true;
    }

    public void updateAge(int increment) {
        if (increment <= 0)
            return;
        if (increment == Integer.MAX_VALUE || ((long) age + (long) increment) >= Integer.MAX_VALUE) { // tror hunden är
                                                                                                      // död om den är
                                                                                                      // såhär gammal
            age = Integer.MAX_VALUE;
            return;
        }
        age += increment;

    }
}

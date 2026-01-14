import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DogRegister {

    private boolean running = true;
    OwnerCollection ownerCollection = new OwnerCollection();
    InputReader input = new InputReader();

    public static void main(String[] args) {

        new DogRegister().startProgram();

    }

    private void onExit() {
        System.out.print("exiting...");
        playExitSound();
        input.close();
    }

    private void startProgram() {
        onStart(); // skriva ut välkomst meddelande + lite ljud
        clearScreen();
        runCommandLoop();
        onExit(); // stänger ner allt + lite ljud
    }

    private void onStart() {

        System.out.print("-------------------------\n");
        System.out.print("|< Antons Dog Register >|\n");
        System.out.print("-------------------------\n");
        playWelcomeSound();

    }

    private void runCommandLoop() {
        do {
            clearScreen();

            // skriva ut input grejer
            userOptionText();

            // ta input
            String uInput = input.readString("enter a command").toLowerCase();
            // executa input
            chooseCommand(uInput);

        } while (running);
    }

    private void userOptionText() {

        System.out.print("commands:\n");
        System.out.print("0. help\n");
        System.out.print("1. add owner\n");
        System.out.print("2. remove owner\n");
        System.out.print("3. add dog\n");
        System.out.print("4. remove dog\n");
        System.out.print("5. change owner\n");
        System.out.print("6. list owners\n");
        System.out.print("7. list dogs\n");
        System.out.print("8. increase age\n");
        System.out.print("9. exit\n");

    }

    private void chooseCommand(String uInput) {
        playInteracSound();
        switch (uInput) {
            case "0":
            case "h":
            case "help":
                helpText();
                break;
            case "1":
            case "ao":
            case "add owner":
                // lägga till owner
                addOwner();
                break;
            case "2":
            case "ro":
            case "remove owner":
                // ta bort owner
                removeOwner();
                break;
            case "3":
            case "ad":
            case "add dog":
                addDog();
                // lägga till owner
                break;
            case "4":
            case "rd":
            case "remove dog":
                removeDog();
                // ta bort hund
                break;
            case "5":
            case "co":
            case "change owner":
                // byt ägare
                break;
            case "6":
            case "lo":
            case "list owners":
                // lista ägarna
                listOwners();
                break;
            case "7":
            case "ld":
            case "list dogs":
                // lista hundarna
                break;
            case "8":
            case "ia":
            case "increase age":
                // öka age
                break;
            case "9":
            case "e":
            case "exit":
                // stäng av progamet
                running = false;
                break;
            default:
                System.out.print("error: invalid command\n");
                waitFor(1000);
                break;
        }
    }

    private void addOwner() {
        boolean succses = false;
        String ownerName = "";
        while (!succses) {
            ownerName = input.readString("enter the owners name");
            playInteracSound();
            if (ownerCollection.containsOwner(ownerName)) {
                System.out.print("error owner already exists\n");
            } else {
                succses = true;
            }
        }
        ownerCollection.addOwner(new Owner(ownerName));
    }

    /*
     * blev så clutterd och funkade typ inte trodde jag men hade fel på min null
     * check i removedog skrev != istället för ==
     * private void addDog() {
     * if (ownerCollection.getAllOwners().size() > 0) {
     * String ownerName =
     * input.readString("enter the name of the owner the dog is registerd under");
     * if (ownerCollection.getOwner(ownerName) == null) {
     * System.out.print("error that owner does not exists\n");
     * waitForUserInput();
     * }
     * if (ownerCollection.getOwner(ownerName).ownsMaxDogs()) {
     * System.out.
     * print("error that owner already ownes the max (7) amount of dogs\n");
     * waitForUserInput();
     * } else {
     * 
     * String dogName = input.readString("enter the dogs name");
     * if (ownerCollection.getOwner(ownerName).ownsDog(dogName)) {
     * System.out.print("error owner already owns a dog with the same name\n");
     * waitForUserInput();
     * } else {
     * String dogBreed = input.readString("enter the dog breed");
     * int dogAge = input.readInt("enter the dogs age");
     * int dogWeight =
     * input.readInt("enter the dogs weight in whole numbers rounded up");
     * ownerCollection.getOwner(ownerName).addDog(new Dog(dogName, dogBreed, dogAge,
     * dogWeight));
     * }
     * }
     * 
     * }
     * }
     */
    private void addDog() {
        if (ownerCollection.getAllOwners().size() > 0) {
            String ownerName = input.readString("enter the name of the owner the dog is registered under");

            if (!ownerCollection.containsOwner(ownerName)) {
                System.out.print("error that owner does not exist\n");
                waitForUserInput();
                return;
            }

            Owner owner = ownerCollection.getOwner(ownerName);

            if (owner.ownsMaxDogs()) {
                System.out.print("error that owner already owns the max (7) amount of dogs\n");
                waitForUserInput();
                return;
            }

            String dogName = input.readString("enter the dogs name");
            if (owner.ownsDog(dogName)) {
                System.out.print("error owner already owns a dog with the same name\n");
                waitForUserInput();
                return;
            }

            String dogBreed = input.readString("enter the dog breed");
            int dogAge = input.readInt("enter the dogs age");
            int dogWeight = input.readInt("enter the dogs weight in whole numbers rounded up");

            Dog newDog = new Dog(dogName, dogBreed, dogAge, dogWeight);

            boolean success = newDog.setOwner(owner);

            if (success) {
                System.out.print("Dog added successfully\n");
            } else {
                System.out.print("error: could not add dog\n");
            }
            waitForUserInput();
        }
    }

    private void changeOwner() {
        if(ownerCollection.getAllOwners().size() < 2){
            System.out.print("error need at least 2 owners to change owner\n");
            waitForUserInput();
            return;
        }
        boolean dogExsists = false;
        for(Owner o : ownerCollection.getAllOwners()){
            if(o.ownsAnyDog()){
                dogExsists = true;
            }
            
        }
        if(!dogExsists){
            System.out.print("error no dogs in system\n");
            waitForUserInput();
            return;
        }
        String ownerName = input.readString("enter the name of the dogs current owner");
        if(!ownerCollection.containsOwner(ownerName) && !ownerCollection.getOwner(ownerName).ownsAnyDog()){
            System.out.print("error the owner does not exist or does not own any dogs\n");
            waitForUserInput();
            return;
        }
        String dogName = input.readString("enter the name for the dog that should change owner");
        if(!ownerCollection.getOwner(ownerName).ownsDog(dogName)){
            System.out.print("error the owner does not own a dog with that name\n");
            waitForUserInput();
            return;
        }
    }

    private void listOwners() {
        playInteracSound();
        if (ownerCollection.getAllOwners().size() > 0) {
            int charToAdd = 0;
            for (Owner o : ownerCollection.getAllOwners()) {
                charToAdd += o.getName().length();

            }
            charToAdd += ownerCollection.getAllOwners().size();
            printTopBar(charToAdd);
            for (Owner o : ownerCollection.getAllOwners()) {
                System.out.print(o.getName() + " ");
            }
            System.out.println();

        } else {
            System.out.print("error no owners exists\n");
        }
        waitForUserInput();

    }

    private void removeOwner() {
        String tempOwner = "";

        if (ownerCollection.getAllOwners().size() > 0) {
            tempOwner = input.readString("enter the owner you want to remove");
            playInteracSound();
            if (ownerCollection.containsOwner(tempOwner)) {
                ownerCollection.removeOwner(tempOwner);
                System.out.print("owner removed\n");
                waitForUserInput();

            } else {
                System.out.print("error that owner does not exist\n");
                waitForUserInput();
            }
        } else {
            System.out.print("error no owners exists\n");
            waitForUserInput();
        }

    }

    private void removeDog() {
        boolean dogExsists = false;
        for (Owner o : ownerCollection.getAllOwners()) {
            if (o.ownsAnyDog() == true) {
                dogExsists = true;
            }
        }
        if (dogExsists == false) {
            System.out.print("error no dogs exsists");
            waitForUserInput();
        } else {
            String ownerName = input.readString("enter the name of the owner of the dog");
            if (ownerCollection.getOwner(ownerName) == null || !ownerCollection.getOwner(ownerName).ownsAnyDog()) {
                System.out.print("error the owner does not exist or the owner dosent have any dogs\n");
                waitForUserInput();

            } else {
                String dogName = input.readString("enter the name of the dog to be removed");
                if (ownerCollection.getOwner(ownerName).ownsDog(dogName)) {
                    ownerCollection.getOwner(ownerName).removeDog(dogName);
                    System.out.print("dog removed from owner\n");
                    waitForUserInput();
                } else {
                    System.out.print("error the owner does not own that dog\n");
                    waitForUserInput();
                }
            }
        }
    }

    private void printTopBar(int num) {
        for (int i = 0; i < num; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    private void waitForUserInput() {
        input.waitForUserInput();
        playInteracSound();
        clearScreen();
    }

    private void helpText() {
        System.out.print(
                "this program is used to register owners and dogs.\nall commands can be used by typing the number, \nthe full command or the initals of the command\n");
        waitForUserInput();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J"); // flyttar markör till början och sedan rensa skärm
        System.out.flush(); // tar bort om de är något som är kvar i bufferten
    }

    private void waitFor(int timeToWait) {
        try {
            Thread.sleep(timeToWait);
        } catch (Exception e) {

        }
    }

    // ville försöka att ha lite ljud med som en välkomst jingle så här är mitt
    // försökt till de:
    /*
     * försök1:
     * private static void playWelcomeSound(){
     * String filePath = "sound1.wav";
     * String filePath2 = "sound2.wav";
     * File musicPath2 = new File(filePath2);
     * File musicPath = new File(filePath);
     * 
     * 
     * try {
     * 
     * if(musicPath.exists() && musicPath2.exists()){
     * AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
     * AudioInputStream audioInput2 = AudioSystem.getAudioInputStream(musicPath2);
     * Clip clip = AudioSystem.getClip();
     * clip.open(audioInput);
     * clip.start();
     * clip.start();
     * clip.open(audioInput2);
     * clip.start();
     * }
     * 
     * } catch (Exception e) {
     * 
     * }
     * 
     * 
     * }
     */
    // använde mig av dessa tutorials: https://www.youtube.com/watch?v=SyZQVJiARTQ
    // och https://www.youtube.com/watch?v=wJO_cq5XeSA
    // försök 2(lyckat):
    private void playWelcomeSound() {
        playSound("HundRegistret/resources/sound6.wav");
        waitFor(500);
        playSound("HundRegistret/resources/sound6.wav");
        waitFor(500);
        playSound("HundRegistret/resources/sound7.wav");

    }

    private void playExitSound() {
        playSound("HundRegistret/resources/sound6.wav");
        waitFor(500);
        playSound("HundRegistret/resources/sound7.wav");
        waitFor(500);

    }

    private void playInteracSound() {
        playSound("HundRegistret/resources/sound6.wav");
        waitFor(500);
    }

    private void playSound(String fileName) {
        File file = new File(fileName); // vilken fil som ska spelas/skapar ett fil objekt
        if (file.exists()) { // kollar att filen faktiskt finns så man inte
            // exploderar och craschar
            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(file); // typ läser in ljudet så java kan
                                                                                     // spela det förstod jag de som
                Clip clip = AudioSystem.getClip(); // skapar en clip som behövs för att spela ljud typ som en cd spelare
                clip.open(audioInput); // laddar "cd spelaren" med ljudet jag valt
                clip.start(); // spela ljudet
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
import java.util.Scanner;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DogRegister {

    private static boolean running = true;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        // skriva ut välkomst meddelande + lite ljud
        onStart();
        do {
            clearScreen();
            // skriva ut input grejer
            userOptionText();

            // ta input
            String uInput = scanner.nextLine().toLowerCase();
            // executa input
            chooseCommand(uInput);

        } while (running);
       onExit(scanner); // stänger ner allt + lite ljud
    }
    private static void onExit(Scanner scanner){
        System.out.print("exiting...");
        playExitSound();
        scanner.close();
    }
    private static void onStart() {

        System.out.print("-------------------------\n");
        System.out.print("|< Antons Dog Register >|\n");
        System.out.print("-------------------------\n");
        playWelcomeSound();

    }

    private static void userOptionText() {

        System.out.print("commands:\n");
        System.out.print("1. add owner\n");
        System.out.print("2. remove owner\n");
        System.out.print("3. add dog\n");
        System.out.print("4. remove dog\n");
        System.out.print("5. change owner\n");
        System.out.print("6. list owners\n");
        System.out.print("7. list dogs\n");
        System.out.print("8. increase age\n");
        System.out.print("9. exit\n");

        System.out.print("?>");

    }

    private static void chooseCommand(String uInput) {
        playInteracSound();
        switch (uInput) {
            case "1":
            case "ao":
            case "add owner":
                // lägga till owner
                break;
            case "2":
            case "ro":
            case "remove owner":
                // ta bort owner
                break;
            case "3":
            case "ad":
            case "add dog":
                // lägga till owner
                break;
            case "4":
            case "rd":
            case "remove dog":
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

    private static void clearScreen() {
        System.out.print("\033[H\033[2J"); // flyttar markör till början och sedan rensa skärm
        System.out.flush(); // tar bort om de är något som är kvar i bufferten
    }

    private static void waitFor(int timeToWait) {
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
    //använde mig av dessa tutorials: https://www.youtube.com/watch?v=SyZQVJiARTQ och https://www.youtube.com/watch?v=wJO_cq5XeSA
    // försök 2(lyckat):
    private static void playWelcomeSound() {
        playSound("src/sound6.wav");
        waitFor(500);
        playSound("src/sound6.wav");
        waitFor(500);
        playSound("src/sound7.wav");

    }
    private static void playExitSound(){
        playSound("src/sound6.wav");
        waitFor(500);
        playSound("src/sound7.wav");
         waitFor(500);
      
       
    }
    private static void playInteracSound(){
        playSound("src/sound6.wav");
        waitFor(500);
    }

    private static void playSound(String fileName) {
        File file = new File(fileName); // vilken fil som ska spelas/skapar ett fil objekt
        if(file.exists()){ //kollar att filen faktiskt finns så man inte exploderar och craschar
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file); // typ läser in ljudet så java kan
                                                                                 // spela det förstod jag de som
            Clip clip = AudioSystem.getClip(); // skapar en clip som behövs för att spela ljud typ som en cd spelare
            clip.open(audioInput); // laddar "cd spelaren" med ljudet jag valt
            clip.start(); // spela ljudet
        } catch (Exception e) {

        }
        }
    }

}
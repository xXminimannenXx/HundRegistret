import java.io.*;
import java.lang.StackWalker.StackFrame;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

/**
 * Dessa testfall är <u>mycket</u> begränsade och kommer att utökas under
 * juluppehållet. De testar de grundläggande funktioner som beskrivs i
 * instruktionerna, men på enklast möjliga vis, och feedbacken från dem är
 * begränsad. Se metoden <code>executeScenario</code> för mer information om
 * feedbacken som testen ger för tillfället.
 * <p>
 * Till skillnad från tidigare uppgifter, där ordningen mellan testen mest varit
 * till för att ge en möjlig ordning att implementera saker och ting i, så är
 * ordningen <u>mycket</u> viktig här. Eftersom testen inte har någon möjlighet
 * att titta på hundregistrets interna tillstånd kan de bara titta på vad som
 * skrivs ut. Detta betyder att alla test avslutas med antingen
 * <code>list owners</code> eller <code>list dogs</code>. Om dessa kommandon
 * inte fungerar, eller om <code>add owner</code> eller <code>add dog</code> som
 * de är beroende av inte gör det, så kommer <u>alla</u> test troligen att
 * misslyckas.
 * <p>
 * <u>Alla</u> kontroller av innehållet i utskrifter från programmet görs utan
 * hänsyn till stora och små bokstäver. Detta eftersom vi inte vet om det
 * skriver namn som <code>Fido</code> eller <code>FIDO</code>.
 * <p>
 * Obs! Eftersom dessa test kommunicerar med "användaren" via
 * <code>System.in</code> och <code>System.out</code> så fungerar det
 * <b><u>inte</u></b> med spårutskrifter. Det går att kringå detta genom att
 * skriva ut på <code>System.err</code> istället för <code>System.in</code>, men
 * det är bättre att testa manuellt istället om du har behov av mer kontroll.
 * 
 * @author Henrik Bergström
 * @version 0.1
 * @apiNote Testen i denna klass är <b>mycket</b> begränsade än så länge. De
 *          kommer att utökas rejält under juluppehållet, och sedan uppdateras
 *          löpande allteftersom vi får tillgång till fler implementationer att
 *          testa.
 */
@DisplayName("DR15: Grundläggande test av hela hundregistret")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR15DogRegisterTest extends DR01Assertions {

	private static final String INPUT_MARKER_PATTERN = "(\\?>[ ]*)";

	private static final Class<?> CLASS_UNDER_TEST = DogRegister.class;

	// TODO: private static final boolean LOG_ERRORS_TO_FILE = true;

	private static final String DEFAULT_OWNER_NAME = "Bob";
	private static final String DEFAULT_DOG_NAME = "Fido";
	private static final String DEFAULT_DOG_BREED = "Labrador";
	private static final int DEFAULT_DOG_AGE = 12;
	private static final int DEFAULT_DOG_WEIGHT = 34;

	private static final String ADD_OWNER_COMMAND = "ADD OWNER";
	private static final String REMOVE_OWNER_COMMAND = "REMOVE OWNER";
	private static final String ADD_DOG_COMMAND = "ADD DOG";
	private static final String REMOVE_DOG_COMMAND = "REMOVE DOG";
	private static final String CHANGE_OWNER_COMMAND = "CHANGE OWNER";
	private static final String LIST_OWNERS_COMMAND = "LIST OWNERS";
	private static final String LIST_DOGS_COMMAND = "LIST DOGS";
	private static final String INCREASE_AGE_COMMAND = "INCREASE AGE";
	private static final String EXIT_COMMAND = "EXIT";

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###########");

	private static final InputStream ORIGINAL_SYSTEM_IN = System.in;
	private static final PrintStream ORIGINAL_SYSTEM_OUT = System.out;

	private ByteArrayOutputStream systemOut;

	private List<String> input = new ArrayList<>();

	@AfterAll
	public static void restoreSystemInAndOut() {
		System.setIn(ORIGINAL_SYSTEM_IN);
		System.setOut(ORIGINAL_SYSTEM_OUT);
	}

	@BeforeEach
	public void replaceSystemOut() {
		systemOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(systemOut));
	}

	@BeforeEach
	public void resetMultipleInstanceGuard() {
		MULTIPLE_INSTANCE_GUARD.setAndFailIfNotPresent(ORIGINAL_MULTIPLE_INSTANCE_GUARD_VALUE);
	}

	private void setupInputOnSystemIn() {
		String inputLines = input.stream().collect(Collectors.joining("\n"));
		InputStream in = new ByteArrayInputStream(inputLines.getBytes());
		System.setIn(in);
	}

	private void executeScenario(Runnable tests) {
		input(EXIT_COMMAND);
		setupInputOnSystemIn();
		try {
			runProgram();
			tests.run();
		} catch (Throwable exception) {
			// TODO: många av de förbättringar som planeras för dessa test har att göra med
			// hur feedbacken presenteras när något går fel. Just nu är den begränsad till
			// felmeddelandena i JUnit och nedanstående två möjligheter att skriva ut
			// programdialogen.

			// Skriv ut programdialogen om ett fel inträffar, kommentera bort raden om du
			// inte vill ha detta beteende. Den dialog som skrivs ut här är *EXAKT* vad ditt
			// program skrev ut, inget mer, inget mindre.
			// printRawDialog(t);

			// Skriv ut programdialogen om ett fel inträffar, kommentera bort raden om du
			// inte vill ha detta beteende. Den dialog som skrivs ut här är vad ditt program
			// skrev ut, med inputen till programmet inlagd efter varje ?>. Detta betyder
			// att den bara stämmer *OM PROGRAMMET LÄSER IN PÅ RÄTT SÄTT*. Om det läser in
			// något utan att först skriva ut ?> eller om det skriver ut ?> utan att läsa in
			// något kommer dialogen inte att stämma, då är det bättre att använda
			// printRawDialog ovan.
			printCompleteDialog(exception);

			// Kommentera *INTE* bort nedanstående rad, då kommer JUnit inte få veta att ett
			// fel inträffat
			throw exception;
		}
	}

	private void runProgram() {
		assertTestTimesOut(() -> {
			try {
				DogRegister.main(new String[] {});
			} catch (InputMismatchException exception) {
				failTest(exception,
						"programmet kördes, troligen för att programmet försökte läsa in fel typ av data från användare");
			} catch (NoSuchElementException exception) {
				failTest(exception,
						"programmet kördes, troligen för att programmet försökte läsa in för mycket eller i fel ordning");
			} catch (Throwable exception) {
				failTest(exception, "programmet kördes");
			}
		});
	}

	@SuppressWarnings("unused") // Man ska normalt aldrig dölja varningar, men eftersom det enda anropet på
								// metoden är bortkommenterat med avsikt i den utdelade versionen så är det
								// korrekt här
	private void printRawDialog(Throwable exception) {
		System.err.println(systemOut);
		if (exception != null)
			System.err.println(exception);
	}

	/**
	 * Se <code>executeScenario</code> för information om begränsningarna i denna
	 * metods utskrifter.
	 */
	private void printCompleteDialog(Throwable exception) {
		List<StackFrame> stack = StackWalker.getInstance().walk(
				s -> s.filter(sf -> sf.getClassName().equals(this.getClass().getName())).collect(Collectors.toList()));
		StackFrame frame = stack.getLast();
		String header = "Dialogen för scenariot i %s (rad %d)".formatted(frame.getMethodName(), frame.getLineNumber());

		System.err.printf("%s%n%s%n%n".formatted(header, "=".repeat(header.length())));

		String original = systemOut.toString();
		Pattern pattern = Pattern.compile(INPUT_MARKER_PATTERN);
		Matcher matcher = pattern.matcher(original);

		List<String> parts = new ArrayList<>();
		int start = 0;
		int inputIdx = 0;

		while (matcher.find()) {
			String part = original.substring(start, matcher.end());
			part += inputIdx < input.size() ? input.get(inputIdx) : "INGEN INPUT ANGIVEN";
			parts.add(part);
			start = matcher.end();
			inputIdx++;
		}

		// If there's any remaining text after the last marker, keep it too:
		if (start < original.length()) {
			parts.add(original.substring(start));
		}

		System.err.println(String.join("\n", parts));
		if (exception != null)
			System.err.println(exception);

		System.err.println();
	}

	private void input(String s) {
		input.add(s);
	}

	private void input(int i) {
		input.add("" + i);
	}

	private void input(double d) {
		input.add(DECIMAL_FORMAT.format(d));
	}

	private void addDefaultOwner() {
		addOwner(DEFAULT_OWNER_NAME);
	}

	private void addOwner(String name) {
		input(ADD_OWNER_COMMAND);
		input(name);
	}

	private void removeOwner(String name) {
		input(REMOVE_OWNER_COMMAND);
		input(name);
	}

	private void listOwners() {
		input(LIST_OWNERS_COMMAND);
	}

	private void addDefaultDogToDefaultOwner() {
		addDefaultDog(DEFAULT_OWNER_NAME);
	}

	private void addDefaultDog(String owner) {
		addDog(owner, DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
	}

	private void addDog(String owner, String name, String breed, int age, int weight) {
		input(ADD_DOG_COMMAND);
		input(owner);
		input(name);
		input(breed);
		input(age);
		input(weight);
	}

	private void removeDog(String owner, String name) {
		input(REMOVE_DOG_COMMAND);
		input(owner);
		input(name);
	}

	private void listAllDogs() {
		listDogs(0.0);
	}

	private void listDogs(double minTailLegth) {
		input(LIST_DOGS_COMMAND);
		input(minTailLegth);
	}

	private void changeOwner(String oldOwner, String dog, String newOwner) {
		input(CHANGE_OWNER_COMMAND);
		input(oldOwner);
		input(dog);
		input(newOwner);
	}

	private void increaseAge() {
		input(INCREASE_AGE_COMMAND);
	}

	/**
	 * Den sista outputen från programmet innan kommandot <code>EXIT</code> gavs.
	 */
	private String lastOutputBeforeExit() {
		String[] parts = systemOut.toString().split(INPUT_MARKER_PATTERN);

		// Ingen output efter EXIT
		if (parts.length == input.size())
			return parts[parts.length - 1];

		// Output efter EXIT
		if (parts.length == input.size() + 1)
			return parts[parts.length - 2];

		failTest(
				"Antalet delar i programdialogen (output avgränsat med ?>) stämmer inte med den input som givits, kan därför inte identifiera outputen efter det sista kommandot innan EXIT");

		return null; // Kan inte hända pga failTest ovan, men krävs av kompilatorn
	}

	@Order(10)
	@Test
	@DisplayName("Grundläggande krav för skyddsnivåer följs i klassen DogRegister")
	public void basicStyleRulesForAccessModifiersFollowed() {
		assertOnlyAllowedAccessModifiersUsed(CLASS_UNDER_TEST);
	}

	@Order(20)
	@Test
	@DisplayName("Grundläggande regler för hur namn formateras följs på klassnivå i klassen DogRegister")
	public void basicStyleRulesForNamesAtClassLevelFollowed() {
		assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(CLASS_UNDER_TEST);
	}

	@Order(30)
	@Test
	@DisplayName("Lägg till en ägare")
	public void addingNonexistingOwner() {
		addDefaultOwner();

		listOwners(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).contains(DEFAULT_OWNER_NAME).ignoringCase()
					.onErrorReport("Den tillagda ägaren listas inte");
		});
	}

	@Order(40)
	@Test
	@DisplayName("Lägg till flera ägare")
	public void addingSeveralOwners() {
		addOwner("Henrik");
		addOwner("Olle");
		addOwner("Amanda");
		addOwner("Kate");

		listOwners(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).containsAllOf("Amanda", "Henrik", "Kate", "Olle").inThatOrder()
					.ignoringCase().onErrorReport("Den tillagda ägaren listas inte");
		});
	}

	@Order(50)
	@Test
	@DisplayName("Lägg till en ägare två gånger")
	public void addingExistingOwner() {
		addDefaultOwner();
		addDefaultOwner();

		listOwners(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).contains(DEFAULT_OWNER_NAME).onlyOnce().ignoringCase()
					.onErrorReport("Den tillagda ägaren listas inte korrekt");
		});
	}

	@Order(60)
	@Test
	@DisplayName("Lägg till en hund")
	public void addingNonexistingDog() {
		addDefaultOwner();
		addDefaultDogToDefaultOwner();

		listAllDogs(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).contains(DEFAULT_DOG_NAME).onlyOnce().ignoringCase()
					.onErrorReport("Den tillagda hunden listas inte korrekt");
		});
	}

	@Order(70)
	@Test
	@DisplayName("Lägg till flera hundar")
	public void addingSeveralDog() {
		addDefaultOwner();
		addDog(DEFAULT_OWNER_NAME, "Max", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Fido", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Karo", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

		listAllDogs(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).containsAllOf("Fido", "Karo", "Max").inThatOrder().ignoringCase()
					.onErrorReport("Den tillagda hundarna listas inte korrekt");
		});
	}

	@Order(80)
	@Test
	@DisplayName("Ta bort en ägare i början")
	public void removeOwnerFromStartOfList() {
		addOwner("Amanda");
		addOwner("Kate");
		addOwner("Olle");

		removeOwner("Amanda");
		listOwners(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).dontContains("Amanda").ignoringCase()
					.onErrorReport("Den borttagna ägaren finns fortfarande kvar");
			assertThat(lastOutputBeforeExit()).containsAllOf("Kate", "Olle").inThatOrder().ignoringCase()
					.onErrorReport("Övriga ägarna listas inte");
		});
	}

	@Order(90)
	@Test
	@DisplayName("Ta bort en ägare i mitten")
	public void removeOwnerFromMiddleOfList() {
		addOwner("Amanda");
		addOwner("Kate");
		addOwner("Olle");

		removeOwner("Kate");
		listOwners(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).dontContains("Kate").ignoringCase()
					.onErrorReport("Den borttagna ägaren finns fortfarande kvar");
			assertThat(lastOutputBeforeExit()).containsAllOf("Amanda", "Olle").inThatOrder().ignoringCase()
					.onErrorReport("Övriga ägarna listas inte");
		});
	}

	@Order(100)
	@Test
	@DisplayName("Ta bort en ägare i slutet")
	public void removeOwnerFromEndOfList() {
		addOwner("Amanda");
		addOwner("Kate");
		addOwner("Olle");

		removeOwner("Olle");
		listOwners(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).dontContains("Olle").ignoringCase()
					.onErrorReport("Den borttagna ägaren finns fortfarande kvar");
			assertThat(lastOutputBeforeExit()).containsAllOf("Amanda", "Kate").inThatOrder().ignoringCase()
					.onErrorReport("Övriga ägarna listas inte");
		});
	}

	@Order(110)
	@Test
	@DisplayName("Ta bort en hund från början av den enda ägaren")
	public void removeDogFromStartOfOwner() {
		addDefaultOwner();
		addDog(DEFAULT_OWNER_NAME, "Devil", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Fido", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Karo", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

		removeDog(DEFAULT_OWNER_NAME, "Devil");
		listAllDogs(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).dontContains("Devil").ignoringCase()
					.onErrorReport("Den borttagna hunden finns fortfarande kvar");
			assertThat(lastOutputBeforeExit()).containsAllOf("Fido", "Karo").inThatOrder().ignoringCase()
					.onErrorReport("Övriga hundar listas inte");
		});
	}

	@Order(120)
	@Test
	@DisplayName("Ta bort en hund från mitten av den enda ägaren")
	public void removeDogFromMiddleOfOwner() {
		addDefaultOwner();
		addDog(DEFAULT_OWNER_NAME, "Devil", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Fido", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Karo", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

		removeDog(DEFAULT_OWNER_NAME, "Fido");
		listAllDogs(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).dontContains("Fido").ignoringCase()
					.onErrorReport("Den borttagna hunden finns fortfarande kvar");
			assertThat(lastOutputBeforeExit()).containsAllOf("Devil", "Karo").inThatOrder().ignoringCase()
					.onErrorReport("Övriga hundar listas inte");
		});
	}

	@Order(130)
	@Test
	@DisplayName("Ta bort en hund från slutet av den enda ägaren")
	public void removeDogFromEndOfOwner() {
		addDefaultOwner();
		addDog(DEFAULT_OWNER_NAME, "Devil", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Fido", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
		addDog(DEFAULT_OWNER_NAME, "Karo", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

		removeDog(DEFAULT_OWNER_NAME, "Karo");
		listAllDogs(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).dontContains("Karo").ignoringCase()
					.onErrorReport("Den borttagna hunden finns fortfarande kvar");
			assertThat(lastOutputBeforeExit()).containsAllOf("Devil", "Fido").inThatOrder().ignoringCase()
					.onErrorReport("Övriga hundar listas inte");
		});
	}

	@Order(140)
	@Test
	@DisplayName("Byt ägare på hund")
	public void changeOwnerChangesOwner() {
		addOwner("Henrik");
		addOwner("Olle");
		addDog("Henrik", "Fido", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

		changeOwner("Henrik", "Fido", "Olle");
		listOwners(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			// Fido bör komma efter Olle eftersom det är ägarna som listas, och Fido nu är
			// Olles hund
			assertThat(lastOutputBeforeExit()).containsAllOf("Henrik", "Olle", "Fido").inThatOrder().ignoringCase()
					.onErrorReport("Den tillagda ägaren listas inte");
		});
	}

	@Order(150)
	@Test
	@DisplayName("Öka åldern på alla hundar")
	public void increaseAgeChangesTheAgesOfAllDogs() {
		addOwner("Henrik");
		addOwner("Olle");
		addDog("Henrik", "Fido", DEFAULT_DOG_BREED, 2, 1);
		addDog("Henrik", "Karo", DEFAULT_DOG_BREED, 4, 1);
		addDog("Olle", "Max", DEFAULT_DOG_BREED, 6, 1);

		increaseAge();
		listAllDogs(); // Detta är kommandot vars resultat kontrolleras

		executeScenario(() -> {
			assertThat(lastOutputBeforeExit()).containsAllOf("3", "5", "7").inAnyOrder().ignoringCase()
					.onErrorReport("Den tillagda ägaren listas inte");
		});
	}

}

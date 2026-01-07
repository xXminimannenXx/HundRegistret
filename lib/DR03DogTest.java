import java.lang.reflect.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

/**
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          bara mindre saker som behöver kompletteras, främst att ordningen på
 *          testfallen bör gås igenom. Den är okej, men kan bli bättre.
 */
@DisplayName("DR03: Grundläggande test för hundar utan ägare")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR03DogTest extends DR01Assertions {

	private static final Class<?> CLASS_UNDER_TEST = Dog.class;

	private static final String DEFAULT_DOG_NAME = "Fido";
	private static final String DEFAULT_DOG_BREED = "Puli";
	private static final int DEFAULT_DOG_AGE = 4;
	private static final int DEFAULT_DOG_WEIGHT = 5;

	private static final PossiblePublicMethod METHOD_TO_UPDATE_AGE = new PossiblePublicMethod("öka hundens ålder",
			Dog.class, DR03DogTest::possibleMethodToUpdateAge);

	private static boolean possibleMethodToUpdateAge(Method method) {
		String name = method.getName().toLowerCase();
		if (name.equals("getage"))
			return false;

		return name.contains("age") && method.getParameterCount() <= 1;
	}

	/*
	 * De två första testen kontrollerar de mest grundläggande ickefunktionella
	 * kraven för klassen Dog. Dessa test är mycket svaga, och kan bara fånga
	 * riktigt uppenbara fel. Om du för muspekaren över assert-satsen i testen
	 * kommer du att få en mer utförlig beskrivning av vad exakt som testas.
	 * 
	 * En betydligt mer utförlig kontroll av de ickefunktionella kraven kommer att
	 * göras efter granskningen. Se instruktionerna för mer information om detta.
	 */

	@Order(10)
	@Test
	@DisplayName("Grundläggande krav för skyddsnivåer följs i klassen Dog")
	public void basicStyleRulesForAccessModifiersFollowed() {
		assertOnlyAllowedAccessModifiersUsed(CLASS_UNDER_TEST);
	}

	@Order(20)
	@Test
	@DisplayName("Grundläggande regler för hur namn formateras följs på klassnivå i klassen Dog")
	public void basicStyleRulesForNamesAtClassLevelFollowed() {
		assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(CLASS_UNDER_TEST);
	}

	/*
	 * De resterande testerna kontrollerar alla funktionella krav i uppgiften. Ett
	 * tips är att börja med att kommentera ut alla utom det första. Det enda som
	 * krävs för att det ska gå igenom är att konstruktorn och getName finns. När
	 * det fungerar som det ska så kan du avkommentera nästa test, osv. På så sätt
	 * kan du arbeta med en sak i taget.
	 * 
	 * Slutligen: tänk på att testerna BARA är tänkta att testa grundläggande
	 * scenarier. Du måste alltså testa klassen ordentligt med egna test också.
	 */

	@Order(30)
	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "Fido", "Karo" })
	@DisplayName("Konstruktorn sätter namnet")
	public void ctrSetsName(String name) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(name, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			assertThat(dog.getName()).is(name).ignoringCase().onErrorReport("Fel namn returneras av getName");
		});
	}

	@Order(40)
	@ParameterizedTest(name = "{0}")
	@ValueSource(strings = { "Tax", "Yorkshireterrier" })
	@DisplayName("Konstruktorn sätter rasen")
	public void ctrSetsBreed(String breed) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, breed, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			assertThat(dog.getBreed()).is(breed).ignoringCase().onErrorReport("Fel ras returneras av getBreed");
		});
	}

	@Order(50)
	@ParameterizedTest(name = "{0} år")
	@ValueSource(ints = { 1, 2, 3 })
	@DisplayName("Konstruktorn sätter åldern")
	public void ctrSetsAge(int age) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, age, DEFAULT_DOG_WEIGHT);
			assertThat(dog.getAge()).is(age).onErrorReport("Fel ålder returneras av getAge");
		});
	}

	@Order(60)
	@ParameterizedTest(name = "{0} kg")
	@ValueSource(ints = { 1, 2, 3 })
	@DisplayName("Konstruktorn sätter vikten")
	public void ctrSetsWeight(int weight) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, weight);
			assertThat(dog.getWeight()).is(weight).onErrorReport("Fel vikt returneras av getWeight");
		});
	}

	@Order(70)
	@ParameterizedTest(name = "{0} som är {1} år och väger {2} kg")
	@CsvSource({ "Labrador,12,13,15.6", "Collie,2,1,0.2" })
	@DisplayName("Svanslängden är korrekt för andra hundraser än taxar")
	public void tailLengthCalculatedCorrectlyForNonDachshunds(String breed, int age, int weight, double expected) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, breed, age, weight);
			assertThat(dog.getTailLength()).is(expected).withStrictTolerance()
					.onErrorReport("Fel svanslängd returneras av getTailLength");
		});
	}

	@Order(80)
	@ParameterizedTest(name = "{0} som är {1} år och väger {2} kg")
	@CsvSource({ "1, 3, 0.3", "2, 7, 1.4", "3, 3, 0.9", "472, 486, 22939.2", "699, 599, 41870.1",
			"694, 619, 42958.6", })
	@DisplayName("Svanslängden är korrekt för andra hundraser än taxar (värden som ofta ger avrundningsfel)")
	public void tailLengthCalculatedCorrectlyForNonDachshunds(int age, int weight, double expected) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, age, weight);
			assertThat(dog.getTailLength()).is(expected).withStrictTolerance()
					.onErrorReport("Fel svanslängd returneras av getTailLength");
		});
	}

	@Order(90)
	@ParameterizedTest(name = "{0} som är {1} år och väger {2} kg")
	@CsvSource({ "Tax,1,2", "Dachshund,20,12" })
	@DisplayName("Svanslängden är korrekt för taxar")
	public void tailLengthCalculatedCorrectlyForDachshunds(String breed, int age, int weight) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, breed, age, weight);
			assertThat(dog.getTailLength()).is(3.7).withStrictTolerance()
					.onErrorReport("Fel svanslängd returneras av getTailLength");
		});
	}

	private void assertNameIsNormalized(String methodName, String name, String... alternatives) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(name, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			assertThat(dog.getName()).isAnyOf(alternatives)
					.onErrorReport("Fel namn returneras av %s".formatted(methodName));
		});
	}

	@Order(100)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "fido,Fido,FIDO", "KaRo,Karo,KARO" })
	@DisplayName("Namnet normaliseras")
	public void nameIsNormalized(String name, String fullyNormalizedName, String upperCaseName) {
		assertNameIsNormalized("getName", name, fullyNormalizedName, upperCaseName);
	}

	@Order(110)
	@Test
	@DisplayName("Flera namn normaliseras")
	public void multipleNamesAreNormalized() {
		assertNameIsNormalized("getName", "fluffy DESTROYER of WORLDS", "Fluffy destroyer of worlds",
				"FLUFFY DESTROYER OF WORLDS", "Fluffy Destroyer Of Worlds");
	}

	@Order(120)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "pumi,Pumi,PUMI", "tAx,Tax,TAX" })
	@DisplayName("Rasen normaliseras")
	public void breedIsNormalized(String breed, String fullyNormalizedBreed, String upperCaseBreed) {
		assertNameIsNormalized("getBreed", breed, fullyNormalizedBreed, upperCaseBreed);
	}

	@Order(130)
	@Test
	@DisplayName("Raser vars namn består av flera ord normaliseras")
	public void multiplePartBreedsAreNormalized() {
		assertNameIsNormalized("getBreed", "shih TZU", "Shih tzu", "SHIH TZU", "Shih Tzu");
	}

	@Order(140)
	@ParameterizedTest(name = "{0} som är {1} år och väger {2} kg")
	@CsvSource({ "Tax,1,2", "tax,1,2", "TaX,1,2", "Dachshund,20,12", "dachshund,20,12", "DachsHund,20,12" })
	@DisplayName("Svanslängden är korrekt för taxar oavsett hur rasnamet skrivs")
	public void tailLengthCalculatedCorrectlyForDachshundsWrittenDifferently(String breed, int age, int weight) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, breed, age, weight);
			assertThat(dog.getTailLength()).is(3.7).withStrictTolerance()
					.onErrorReport("Fel svanslängd returneras av getTailLength");
		});
	}

	private void increaseAgeOfDog(Dog dog, int increments) throws IllegalAccessException, InvocationTargetException {
		if (METHOD_TO_UPDATE_AGE.parameterCount() == 0) {
			for (int i = 0; i < increments; i++) {
				METHOD_TO_UPDATE_AGE.invokeAndFailIfNotPresent(dog);
			}
		} else {
			METHOD_TO_UPDATE_AGE.invokeAndFailIfNotPresent(dog, increments);
		}
	}

	private void assertAgeChangesCorrectly(int initial, int increments, int expected) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, initial, DEFAULT_DOG_WEIGHT);

			increaseAgeOfDog(dog, increments);

			assertThat(dog.getAge()).is(expected).onErrorReport(
					"Fel ålder på en hund som var %d år från början och försökte ändras med %d år. Metoden som antogs öka åldern var %s",
					initial, increments, METHOD_TO_UPDATE_AGE);
		});
	}

	@Order(150)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "1,1,2", "2,3,5", "2147483646,1,2147483647" })
	@DisplayName("Åldern kan ökas")
	public void updateAge(int initial, int increments, int expected) {
		assertAgeChangesCorrectly(initial, increments, expected);
	}

	@Order(160)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "2147483647,1,2147483647", "2147483646,2,2147483647" })
	@DisplayName("Åldern kan inte få överslag")
	public void updateAgeNoOverflow(int initial, int increments, int expected) {
		assertAgeChangesCorrectly(initial, increments, expected);
	}

	@Order(170)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "5,-1,5", "1,-2,1" })
	@DisplayName("Åldern kan inte minskas")
	public void updateAgeNoNegative(int initial, int increments, int expected) {
		assertAgeChangesCorrectly(initial, increments, expected);
	}

	@Order(180)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "Basenji, 3, 5, 2.0", "Schäfer, 7, 2, 1.6" })
	@DisplayName("Svanslängden ökar när åldern ökar för andra raser än taxar")
	public void updateAgeUpdatesTailLength(String breed, int initialAge, int weight, double expectedTailLength) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, breed, initialAge, weight);

			increaseAgeOfDog(dog, 1);

			assertThat(dog.getTailLength()).is(expectedTailLength).withStrictTolerance().onErrorReport(
					"Fel svanslägd på en hund som var %d år från början och vars ålder ökade med ett år", initialAge);
		});
	}

	@Order(190)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "Tax, 3, 5", "Dachshund, 7, 2" })
	@DisplayName("Svanslängden ökar inte när åldern ökar för taxar")
	public void updateAgeDoesNotUpdateDachshundTailLength(String breed, int initialAge, int weight) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, breed, initialAge, weight);

			increaseAgeOfDog(dog, 1);

			assertThat(dog.getTailLength()).is(3.7).withStrictTolerance().onErrorReport(
					"Fel svanslägd på en tax som var %d år från början och vars ålder ökade med ett år", initialAge);
		});
	}

	@Order(200)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "fido,Fido,FIDO", "KaRo,Karo,KARO" })
	@DisplayName("Namnet finns i strängrepresentationen")
	public void stringRepresentationContainsName(String name, String fullyNormalizedName, String upperCaseName) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(name, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			assertThat(dog.toString()).containsAnyOf(fullyNormalizedName, upperCaseName)
					.onErrorReport("Namnet finns inte i strängrepresentationen");
		});
	}

	@Order(210)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "pumi,Pumi,PUMI", "tAx,Tax,TAX" })
	@DisplayName("Rasen finns i strängrepresentationen")
	public void stringRepresentationContainsBreed(String breed, String fullyNormalizedBreed, String upperCaseBreed) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, breed, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			assertThat(dog.toString()).containsAnyOf(fullyNormalizedBreed, upperCaseBreed)
					.onErrorReport("Rasen finns inte i strängrepresentationen");
		});
	}

	@Order(220)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "8", "12", "37" })
	@DisplayName("Åldern finns i strängrepresentationen")
	public void stringRepresentationContainsAge(int age) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, age, DEFAULT_DOG_WEIGHT);
			assertThat(dog.toString()).contains(age).onErrorReport("åldern finns inte i strängrepresentationen");
		});
	}

	@Order(230)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "8", "12", "37" })
	@DisplayName("Vikten finns i strängrepresentationen")
	public void stringRepresentationContainsWeight(int weight) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, weight);
			assertThat(dog.toString()).contains(weight).onErrorReport("Vikten finns inte i strängrepresentationen");
		});
	}

	@Order(240)
	@ParameterizedTest(name = "{0}år och {1}kg")
	@CsvSource({ "5,8,4.0", "2,3,0.6", "6,8,4.8", "7,2,1.4" })
	@DisplayName("Svanslägden finns i strängrepresentationen")
	public void stringRepresentationContainsTailLength(int age, int weight, double tail) {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, age, weight);
			assertThat(dog.toString()).contains(tail).onErrorReport("Svanslängden finns inte i strängrepresentationen");
		});
	}

}

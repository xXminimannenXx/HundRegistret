import org.junit.jupiter.api.*;

/**
 * @author Henrik Bergström
 * @version 1.1
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR07: Grundläggande test av TailNameComparator")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR07TailNameComparatorTest extends DR01Assertions {

	private static final Class<?> CLASS_UNDER_TEST = TailNameComparator.class;

	private static final TailNameComparator COMPARATOR = new TailNameComparator();

	@Order(10)
	@Test
	@DisplayName("Grundläggande krav för skyddsnivåer följs i klassen TailNameComparator")
	public void basicStyleRulesForAccessModifiersFollowed() {
		assertOnlyAllowedAccessModifiersUsed(CLASS_UNDER_TEST);
	}

	@Order(20)
	@Test
	@DisplayName("Grundläggande regler för hur namn formateras följs på klassnivå i klassen TailNameComparator")
	public void basicStyleRulesForNamesAtClassLevelFollowed() {
		assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(CLASS_UNDER_TEST);
	}

	private void compareDogsInTheRightOrder(Dog firstDog, Dog secondDog) {
		assertTestTimesOut(() -> {
			var actual = COMPARATOR.compare(firstDog, secondDog);
			assertThat(actual).isLessThan(0).onErrorReport("");
		});
	}

	private void compareDogsInTheWrongOrder(Dog firstDog, Dog secondDog) {
		assertTestTimesOut(() -> {
			var actual = COMPARATOR.compare(firstDog, secondDog);
			assertThat(actual).isGreaterThan(0).onErrorReport("");
		});
	}

	@Order(30)
	@Test
	@DisplayName("Hundarnas svanslängd är i rätt ordning")
	public void theFirstDogHaveALongerTailThenTheSecond() {
		Dog firstDog = new Dog("Fido", "Chihuahua", 1, 1);
		Dog secondDog = new Dog("Max", "Tax", 20, 20);

		compareDogsInTheRightOrder(firstDog, secondDog);
	}

	@Order(40)
	@Test
	@DisplayName("Hundarnas svanslängd är i fel ordning")
	public void theFirstDogHaveAShorterTailThanTheSecond() {
		Dog firstDog = new Dog("Fido", "Schäfer", 15, 15);
		Dog secondDog = new Dog("Max", "Tax", 2, 2);

		compareDogsInTheWrongOrder(firstDog, secondDog);
	}

	@Order(50)
	@Test
	@DisplayName("Samma svanslängd och namnen i rätt ordning")
	public void theTailsAreTheSameAndTheFirstDogHaveANameThatComesBeforeTheSecond() {
		Dog firstDog = new Dog("Fido", "Tax", 2, 1);
		Dog secondDog = new Dog("Max", "Tax", 1, 2);

		compareDogsInTheRightOrder(firstDog, secondDog);
	}

	@Order(60)
	@Test
	@DisplayName("Samma svanslängd men namnen i fel ordning")
	public void theTailsAreTheSameButTheFirstDogHaveANameThatComesAfterTheSecond() {
		Dog firstDog = new Dog("Max", "Tax", 2, 1);
		Dog secondDog = new Dog("Fido", "Tax", 1, 2);

		compareDogsInTheWrongOrder(firstDog, secondDog);
	}

	@Order(70)
	@Test
	@DisplayName("Samma svanslängd och namn")
	public void bothTailLengthAndNameAreTheSameForBothDogs() {
		assertTestTimesOut(() -> {
			Dog firstDog = new Dog("Fido", "Tax", 2, 1);
			Dog secondDog = new Dog("Fido", "Tax", 1, 2);

			var actual = COMPARATOR.compare(firstDog, secondDog);

			assertThat(actual).is(0).onErrorReport("Två hundar med samma svanslängd och namn");
		});
	}

}

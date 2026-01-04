import org.junit.jupiter.api.*;

/**
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR04: Grundläggande test för hundar med ägare")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR04DogsWithOwnerTest extends DR01Assertions {

	private static final Class<?> CLASS_UNDER_TEST = Dog.class;

	private static final String DEFAULT_DOG_NAME = "Fido";
	private static final String DEFAULT_DOG_BREED = "Puli";
	private static final int DEFAULT_DOG_AGE = 3;
	private static final int DEFAULT_DOG_WEIGHT = 5;

	private static final String DEFAULT_OWNER_NAME = "Henrik";

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

	@Order(30)
	@Test
	@DisplayName("setOwner sätter ägaren för en hund utan ägare")
	public void setOwnerSetsTheOwnerForADogWithoutAnOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME);

			boolean changed = dog.setOwner(owner);

			assertThat(dog.getOwner()).is(owner).onErrorReport("Fel ägare satt");
			assertThat(changed).isTrue().onErrorReport("setOwner returnerar fel värde när ägaren sätts");
		});
	}

	@Order(40)
	@Test
	@DisplayName("setOwner byter ägare för en hund med en existerande ägare")
	public void setOwnerChangesTheOwnerForADogWithAnotherOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner firstOwner = new Owner("Henrik");
			Owner secondOwner = new Owner("Olle");

			dog.setOwner(firstOwner);
			boolean changed = dog.setOwner(secondOwner);

			assertThat(dog.getOwner()).is(secondOwner).onErrorReport("Fel ägare satt");
			assertThat(changed).isTrue().onErrorReport("setOwner returnerar fel värde när ägaren ändras");
		});
	}

	@Order(50)
	@Test
	@DisplayName("setOwner ändrar inte ägaren om man försöker sätta samma ägare två gånger")
	public void setOwnerDoesNotChangeTheOwnerWhenCalledMultipleTimesWithTheSameOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME);

			dog.setOwner(owner);
			boolean changed = dog.setOwner(owner);

			assertThat(dog.getOwner()).is(owner).onErrorReport("Fel ägare satt");
			assertThat(changed).isFalse()
					.onErrorReport("setOwner returnerar fel värde när samma ägaren sätts två gånger");
		});
	}

	@Order(60)
	@Test
	@DisplayName("setOwner tar bort en existerande ägare om den anropas med null")
	public void setOwnerRemovesTheOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME);

			dog.setOwner(owner);
			boolean changed = dog.setOwner(null);

			assertThat(dog.getOwner()).isNull().onErrorReport("Fel ägare satt");
			assertThat(changed).isTrue().onErrorReport("setOwner returnerar fel värde när ägaren tas bort");
		});
	}

	@Order(70)
	@Test
	@DisplayName("Den överlagrade konstruktorn med fem argument sätter ägaren")
	public void theOverloadedConstructorSetsTheOwner() {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(DEFAULT_OWNER_NAME);
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT, owner);

			assertThat(dog.getOwner()).is(owner).onErrorReport("Fel ägare satt");
		});
	}
}

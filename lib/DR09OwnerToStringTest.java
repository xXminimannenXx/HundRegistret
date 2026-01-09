import org.junit.jupiter.api.*;

/**
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR09: Grundläggande test för ägar och hundklassens toString-metoder")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR09OwnerToStringTest extends DR01Assertions {

	private static final String DEFAULT_OWNER_NAME = "Stefan";
	private static final String DEFAULT_DOG_NAME = "Max";
	private static final String DEFAULT_DOG_BREED = "Tax";
	private static final int DEFAULT_DOG_AGE = 1;
	private static final int DEFAULT_DOG_WEIGHT = 2;

	@Order(10)
	@Test
	@DisplayName("Ägare utan hundar")
	public void ownerWithoutDogs() {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(DEFAULT_OWNER_NAME);
			assertThat(owner.toString()).contains(DEFAULT_OWNER_NAME).ignoringCase().onErrorReport("Ägarnamnet saknas");
		});
	}

	@Order(20)
	@Test
	@DisplayName("Ägare med en hund")
	public void ownerWithOneDog() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME, dog);
			assertThat(owner.toString()).contains(DEFAULT_OWNER_NAME).ignoringCase().onErrorReport("Ägarnamnet saknas");
			assertThat(owner.toString()).contains(DEFAULT_DOG_NAME).ignoringCase().onErrorReport("Hundnamnet saknas");
		});
	}

	@Order(30)
	@Test
	@DisplayName("Ägare med flera hundar")
	public void ownerWithMultipleDogs() {
		assertTestTimesOut(() -> {
			Dog lassie = new Dog("Lassie", "Schäfer", 4, 8);
			Dog snobben = new Dog("Snobben", "Beagle", 8, 6);
			Dog max = new Dog("Max", "Border collie", 8, 1);

			Owner owner = new Owner(DEFAULT_OWNER_NAME, lassie, snobben, max);
			assertThat(owner.toString()).contains(DEFAULT_OWNER_NAME).ignoringCase().onErrorReport("Ägarnamnet saknas");
			assertThat(owner.toString()).containsAllOf("Lassie", "Snobben", "Max").inAnyOrder().ignoringCase()
					.onErrorReport("Minst en hund saknas");
		});
	}

	@Order(40)
	@Test
	@DisplayName("Ägare med max antal hundar")
	public void ownerWithMaxDogs() {
		assertTestTimesOut(() -> {
			Dog daisy = new Dog("Daisy", "Mastiff", 4, 8);
			Dog saga = new Dog("Saga", "Dobermann", 5, 6);
			Dog buster = new Dog("Buster", "Pumi", 7, 8);
			Dog pluto = new Dog("Pluto", "Samojed", 2, 8);
			Dog bonnie = new Dog("Bonnie", "Basenji", 8, 6);
			Dog zeus = new Dog("Zeus", "Mudi", 8, 2);
			Dog siri = new Dog("Siri", "Border collie", 8, 1);

			Owner owner = new Owner(DEFAULT_OWNER_NAME, daisy, saga, buster, pluto, bonnie, zeus, siri);

			assertThat(owner.toString()).contains(DEFAULT_OWNER_NAME).ignoringCase().onErrorReport("Ägarnamnet saknas");
			assertThat(owner.toString()).containsAllOf("Daisy", "Saga", "Buster", "Pluto", "Bonnie", "Zeus", "Siri")
					.inAnyOrder().ignoringCase().onErrorReport("Minst en hund saknas");
		});
	}

	@Order(50)
	@Test
	@DisplayName("Hund utan ägare")
	public void dogWithoutOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			assertThat(dog.toString()).contains(DEFAULT_DOG_NAME).ignoringCase().onErrorReport("Hundnamnet saknas");
		});
	}

	@Order(60)
	@Test
	@DisplayName("Hund med ägare")
	public void dogWithOwner() {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(DEFAULT_OWNER_NAME);
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

			dog.setOwner(owner);

			assertThat(dog.toString()).contains(DEFAULT_DOG_NAME).ignoringCase().onErrorReport("Hundnamnet saknas");
			assertThat(dog.toString()).contains(DEFAULT_OWNER_NAME).ignoringCase().onErrorReport("Ägarnamnet saknas");
		});
	}

}

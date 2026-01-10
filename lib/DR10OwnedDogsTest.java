import org.junit.jupiter.api.*;

/**
 * @author Henrik Bergström
 * @version 0.9
 * @apiNote Testen i denna klass testar den grundläggande funktionaliteten, men
 *          absolut inte allt. De kommer att utökas när vi sett fler
 *          implementationer. Eftersom de täcker en kritisk del av
 *          funktionaliteten är det också troligt att de kommer att uppdateras
 *          efter ordinarie inlämning.
 */
@DisplayName("DR10: Grundläggande test för synkningen av ägare och hundar")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR10OwnedDogsTest extends DR01Assertions {

	private static final String DEFAULT_OWNER_NAME = "Stefan";
	private static final String DEFAULT_DOG_NAME = "Max";
	private static final String DEFAULT_DOG_BREED = "Tax";
	private static final int DEFAULT_DOG_AGE = 1;
	private static final int DEFAULT_DOG_WEIGHT = 2;

	@Order(10)
	@Test
	@DisplayName("Att lägga till en hund hos en ägare sätter också hundens ägare")
	public void addDogToOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME);

			owner.addDog(dog);

			assertThat(owner.getDogs()).containsOnly(dog).onErrorReport("Ägaren äger inte rätt hund(ar)");
			assertThat(dog.getOwner()).is(owner).onErrorReport("Hunden har inte rätt ägare");
		});
	}

	@Order(20)
	@Test
	@DisplayName("Att lägga till en hund hos en ägare via konstruktorn sätter också hundens ägare")
	public void addDogToOwnerCtr() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME, dog);

			assertThat(owner.getDogs()).containsOnly(dog).onErrorReport("Ägaren äger inte rätt hund(ar)");
			assertThat(dog.getOwner()).is(owner).onErrorReport("Hunden har inte rätt ägare");
		});
	}

	@Order(30)
	@Test
	@DisplayName("Att ta bort en hund från en ägare tar också bort hundens ägare")
	public void removeDogFromOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME, dog);

			owner.removeDog(dog);

			assertThat(owner.getDogs()).isEmpty().onErrorReport("Ägaren äger inte rätt hund(ar)");
			assertThat(dog.getOwner()).isNull().onErrorReport("Hunden har inte rätt ägare");
		});
	}

	@Order(40)
	@Test
	@DisplayName("Att ta bort en hund från en ägare via namn tar också bort hundens ägare")
	public void removeDogByNameFromOwner() {
		assertTestTimesOut(() -> {
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Owner owner = new Owner(DEFAULT_OWNER_NAME, dog);

			owner.removeDog(DEFAULT_DOG_NAME);

			assertThat(owner.getDogs()).isEmpty().onErrorReport("Ägaren äger inte rätt hund(ar)");
			assertThat(dog.getOwner()).isNull().onErrorReport("Hunden har inte rätt ägare");
		});
	}

	@Order(50)
	@Test
	@DisplayName("Att sätta en hunds ägare lägger också till den hos ägaren")
	public void setOwner() {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(DEFAULT_OWNER_NAME);
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

			dog.setOwner(owner);

			assertThat(dog.getOwner()).is(owner).onErrorReport("Hunden har inte rätt ägare");
			assertThat(owner.getDogs()).containsOnly(dog).onErrorReport("Ägaren äger inte rätt hund(ar)");
		});
	}

	@Order(60)
	@Test
	@DisplayName("Att ta bort en hunds ägare tar också bort den hos ägaren")
	public void removeOwner() {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(DEFAULT_OWNER_NAME);
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

			dog.setOwner(owner);
			dog.setOwner(null);

			assertThat(dog.getOwner()).isNull().onErrorReport("Hunden har inte rätt ägare");
			assertThat(owner.getDogs()).isEmpty().onErrorReport("Ägaren äger inte rätt hund(ar)");
		});
	}

	@Order(70)
	@Test
	@DisplayName("Att byta en hunds ägare tar också bort hunden från den tidigare ägaren och lägger till den hos den nya")
	public void changeOwner() {
		assertTestTimesOut(() -> {
			Owner firstOwner = new Owner("Primus");
			Owner secondOwner = new Owner("Secundus");
			Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

			dog.setOwner(firstOwner);
			dog.setOwner(secondOwner);

			assertThat(dog.getOwner()).is(secondOwner).onErrorReport("Hunden har inte rätt ägare");
			assertThat(firstOwner.getDogs()).isEmpty().onErrorReport("Den första ägaren äger inte rätt hund(ar)");
			assertThat(secondOwner.getDogs()).containsOnly(dog).onErrorReport("Den nya ägaren äger inte rätt hund(ar)");
		});
	}

	@Order(80)
	@Test
	@DisplayName("Scenariet i figur 10.1 i instruktionerna")
	public void figureTenPointOne() {
		assertTestTimesOut(() -> {
			Owner olle = new Owner("Olle");
			Owner henrik = new Owner("Henrik");

			Dog fido = new Dog("Fido", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Dog karo = new Dog("Karo", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
			Dog max = new Dog("Max", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

			fido.setOwner(olle);
			assertThat(olle.getDogs()).containsOnly(fido).onErrorReport("Olle äger inte rätt hundar efter steg 1");
			assertThat(henrik.getDogs()).isEmpty().onErrorReport("Henrik äger inte rätt hundar efter steg 1");
			assertThat(fido.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 1");
			assertThat(karo.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 1");
			assertThat(max.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 1");

			olle.addDog(fido);
			assertThat(olle.getDogs()).containsOnly(fido).onErrorReport("Olle äger inte rätt hundar efter steg 2");
			assertThat(henrik.getDogs()).isEmpty().onErrorReport("Henrik äger inte rätt hundar efter steg 2");
			assertThat(fido.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 2");
			assertThat(karo.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 2");
			assertThat(max.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 2");

			olle.addDog(karo);
			assertThat(olle.getDogs()).contains(fido, karo).inAnyOrder()
					.onErrorReport("Olle äger inte rätt hundar efter steg 3");
			assertThat(henrik.getDogs()).isEmpty().onErrorReport("Henrik äger inte rätt hundar efter steg 3");
			assertThat(fido.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 3");
			assertThat(karo.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 3");
			assertThat(max.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 3");

			karo.setOwner(olle);
			assertThat(olle.getDogs()).contains(fido, karo).inAnyOrder()
					.onErrorReport("Olle äger inte rätt hundar efter steg 4");
			assertThat(henrik.getDogs()).isEmpty().onErrorReport("Henrik äger inte rätt hundar efter steg 4");
			assertThat(fido.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 4");
			assertThat(karo.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 4");
			assertThat(max.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 4");

			olle.addDog(max);
			assertThat(olle.getDogs()).contains(fido, karo, max).inAnyOrder()
					.onErrorReport("Olle äger inte rätt hundar efter steg 5");
			assertThat(henrik.getDogs()).isEmpty().onErrorReport("Henrik äger inte rätt hundar efter steg 5");
			assertThat(fido.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 5");
			assertThat(karo.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 5");
			assertThat(max.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 5");

			olle.removeDog(karo);
			assertThat(olle.getDogs()).contains(fido, max).inAnyOrder()
					.onErrorReport("Olle äger inte rätt hundar efter steg 6");
			assertThat(henrik.getDogs()).isEmpty().onErrorReport("Henrik äger inte rätt hundar efter steg 6");
			assertThat(fido.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 6");
			assertThat(karo.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 6");
			assertThat(max.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 6");

			fido.setOwner(henrik);
			assertThat(olle.getDogs()).containsOnly(max).onErrorReport("Olle äger inte rätt hundar efter steg 7");
			assertThat(henrik.getDogs()).containsOnly(fido).onErrorReport("Henrik äger inte rätt hundar efter steg 7");
			assertThat(fido.getOwner()).is(henrik).onErrorReport(" har inte rätt ägare efter steg 7");
			assertThat(karo.getOwner()).isNull().onErrorReport(" har inte rätt ägare efter steg 7");
			assertThat(max.getOwner()).is(olle).onErrorReport(" har inte rätt ägare efter steg 7");
		});
	}

}

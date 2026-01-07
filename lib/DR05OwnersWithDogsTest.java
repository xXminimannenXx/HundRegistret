import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är helt nya och testar en central funktion, så
 *          de kommer troligen att uppdateras en eller ett par gånger under
 *          kursen.
 */
@DisplayName("DR05: Grundläggande test för en ägare med hundar")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class DR05OwnersWithDogsTest extends DR01Assertions {

	private static final Class<?> CLASS_UNDER_TEST = Owner.class;

	private static final String DEFAULT_OWNER_NAME = "Olle";
	private static final String DEFAULT_DOG_NAME = "Fido";
	private static final String DEFAULT_DOG_BREED = "Schnauzer";
	private static final int DEFAULT_DOG_AGE = 3;
	private static final int DEFAULT_DOG_WEIGHT = 4;

	@Order(10)
	@Test
	@DisplayName("Grundläggande krav för skyddsnivåer följs i klassen Owner")
	public void basicStyleRulesForAccessModifiersFollowed() {
		assertOnlyAllowedAccessModifiersUsed(CLASS_UNDER_TEST);
	}

	@Order(20)
	@Test
	@DisplayName("Grundläggande regler för hur namn formateras följs på klassnivå i klassen Owner")
	public void basicStyleRulesForNamesAtClassLevelFollowed() {
		assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(CLASS_UNDER_TEST);
	}

	@DisplayName("Test för en ny ägare utan några hundar inlagda")
	@Nested
	@Order(30)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class OwnerOfNoDogsTest {

		@Order(40)
		@Test
		@DisplayName("Denna ska inte anse sig äga några hundar")
		public void ownsAnyDogShouldReturnFalse() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				assertThat(owner.ownsAnyDog()).isFalse()
						.onErrorReport("En ägare utan hundar borde inte anse sig äga några hundar");
			});
		}

		@Order(50)
		@Test
		@DisplayName("Denna ska ha plats för fler hundar")
		public void ownsMaxDogsShouldReturnFalse() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				assertThat(owner.ownsMaxDogs()).isFalse()
						.onErrorReport("En ägare utan hundar borde ha plats att lägga till hundar");
			});
		}

		@Order(60)
		@Test
		@DisplayName("Denna ska förneka att hen äger en hund med ett visst namn")
		public void ownsDogNameShouldReturnFalse() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				assertThat(owner.ownsDog(DEFAULT_DOG_NAME)).isFalse()
						.onErrorReport("En ägare utan hundar äger inte någon specifik hund");
			});
		}

		@Order(70)
		@Test
		@DisplayName("Denna ska förneka att hen äger en viss hund")
		public void ownsDogDogShouldReturnFalse() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
				assertThat(owner.ownsDog(dog)).isFalse()
						.onErrorReport("En ägare utan hundar äger inte någon specifik hund");
			});
		}

		@Order(80)
		@Test
		@DisplayName("Denna äger inga hundar")
		public void getDogsShouldReturnEmptyArray() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);

				assertThat(owner.getDogs()).isEmpty()
						.onErrorReport("En ägare utan hundar borde returnera en tom array av hundar");
			});
		}
	}

	@DisplayName("Test för en ägare med en hund")
	@Nested
	@Order(90)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class OwnerOfOneDogTest {

		@Order(100)
		@Test
		@DisplayName("Denna ska anse sig äga någon hund")
		public void ownsAnyDogShouldReturnTrue() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				owner.addDog(dog);

				assertThat(owner.ownsAnyDog()).isTrue()
						.onErrorReport("En ägare med en hund tillagd borde anse sig äga någon hund");
			});
		}

		@Order(110)
		@Test
		@DisplayName("Denna ska ha plats för fler hundar")
		public void ownsMaxDogsShouldReturnFalse() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				owner.addDog(dog);

				assertThat(owner.ownsMaxDogs()).isFalse()
						.onErrorReport("En ägare med en hund tillagd borde ha plats för fler");
			});
		}

		@Order(120)
		@Test
		@DisplayName("Denna kan bekräfta att hen äger en hund med rätt namn")
		public void ownsDogNameShouldReturnTrueForCorrectNameOfDog() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				owner.addDog(dog);

				assertThat(owner.ownsDog(DEFAULT_DOG_NAME)).isTrue().onErrorReport("%s äger inte %s som förväntat",
						DEFAULT_OWNER_NAME, DEFAULT_DOG_NAME);
			});
		}

		@Order(130)
		@Test
		@DisplayName("Denna ska förneka att hen äger en hund med ett annat namn")
		public void ownsDogNameShouldReturnFalseForWrongNameOfDog() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				owner.addDog(dog);

				assertThat(owner.ownsDog("AnotherDogsName")).isFalse()
						.onErrorReport("%s säger sig äga en hund som inte lagts till", DEFAULT_OWNER_NAME);
			});
		}

		@Order(140)
		@Test
		@DisplayName("Denna kan bekräfta att hen äger hunden")
		public void ownsDogDogShouldReturnTrueForCorrectDog() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				owner.addDog(dog);

				assertThat(owner.ownsDog(dog)).isTrue().onErrorReport("%s äger inte %s som förväntat",
						DEFAULT_OWNER_NAME, DEFAULT_DOG_NAME);
			});
		}

		@Order(150)
		@Test
		@DisplayName("Denna ska förneka att hen äger en annan hund")
		public void ownsDogDogShouldReturnFalseForWrongDog() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				owner.addDog(dog);

				Dog anotherDog = new Dog("AnotherDogsName", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
				assertThat(owner.ownsDog(anotherDog)).isFalse()
						.onErrorReport("%s säger sig äga en hund som inte lagts till", DEFAULT_OWNER_NAME);

			});
		}

		@Order(160)
		@Test
		@DisplayName("Hunden returneras när man ber om ägarens hundar")
		public void getDogsShouldReturnSingleElementArrayWithCorrectDog() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				owner.addDog(dog);

				assertThat(owner.getDogs()).containsOnly(dog)
						.onErrorReport("En ägare utan hundar borde returnera en tom array av hundar");
			});
		}
	}

	@DisplayName("Test för en ägare med flera hundar")
	@Nested
	@Order(170)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class OwnerOfMultipleDogsTest {
		@Order(180)
		@Test
		@DisplayName("Denna ska anse sig äga någon hund")
		public void ownsAnyDogShouldReturnTrue() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);

				owner.addDog(siri);
				owner.addDog(karo);
				owner.addDog(ronja);

				assertThat(owner.ownsAnyDog()).isTrue()
						.onErrorReport("En ägare med flera hund tillagda borde anse sig äga någon hund");
			});
		}

		@Order(190)
		@Test
		@DisplayName("Denna ska ha plats för fler om hen äger mindre än sju hundar")
		public void ownsMaxDogsShouldReturnFalseWithLessThanSevenDogs() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);

				Dog bamse = new Dog("Bamse", "Labrador retriever", 7, 8);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);
				Dog buster = new Dog("Buster", "Irländsk varghund", 8, 6);
				Dog daisy = new Dog("Daisy", "Golden retriever", 3, 4);

				owner.addDog(bamse);
				owner.addDog(lady);
				owner.addDog(king);
				owner.addDog(nova);
				owner.addDog(buster);
				owner.addDog(daisy);

				assertThat(owner.ownsMaxDogs()).isFalse()
						.onErrorReport("En ägare med sex hund tillagd borde ha plats för fler");
			});
		}

		@Order(200)
		@Test
		@DisplayName("Denna ska inte ha plats för fler om hen äger sju hundar")
		public void ownsMaxDogsShouldReturnTrueWithSevenDogs() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);

				Dog bamse = new Dog("Bamse", "Labrador retriever", 7, 8);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);
				Dog buster = new Dog("Buster", "Irländsk varghund", 8, 6);
				Dog daisy = new Dog("Daisy", "Golden retriever", 3, 4);
				Dog lassie = new Dog("Lassie", "Bulldogg", 9, 3);

				owner.addDog(bamse);
				owner.addDog(lady);
				owner.addDog(king);
				owner.addDog(nova);
				owner.addDog(buster);
				owner.addDog(daisy);
				owner.addDog(lassie);

				assertThat(owner.ownsMaxDogs()).isTrue()
						.onErrorReport("En ägare med sju hund tillagd borde inte ha plats för fler");
			});
		}

		@Order(210)
		@ParameterizedTest
		@CsvSource({ "Siri", "Karo", "Ronja" })
		@DisplayName("Denna kan bekräfta att hen äger en hund med rätt namn")
		public void ownsDogNameShouldReturnTrueForCorrectNameOfDog(String name) {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);

				owner.addDog(siri);
				owner.addDog(karo);
				owner.addDog(ronja);

				assertThat(owner.ownsDog(name)).isTrue().onErrorReport("%s äger inte %s som förväntat",
						DEFAULT_OWNER_NAME, name);
			});
		}

		@Order(220)
		@Test
		@DisplayName("Denna ska förneka att hen äger en hund med ett annat namn")
		public void ownsDogNameShouldReturnFalseForWrongNameOfDog() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);

				owner.addDog(siri);
				owner.addDog(karo);
				owner.addDog(ronja);

				assertThat(owner.ownsDog("AnotherDogsName")).isFalse()
						.onErrorReport("%s säger sig äga en hund som inte lagts till", DEFAULT_OWNER_NAME);
			});
		}

		@Order(230)
		@ParameterizedTest
		@CsvSource({ "Siri", "Karo", "Ronja" })
		@DisplayName("Denna kan bekräfta att hen äger en specifik hund")
		public void ownsDogDogShouldReturnTrueForCorrectDog(String name) {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);

				owner.addDog(siri);
				owner.addDog(karo);
				owner.addDog(ronja);

				Dog dog = switch (name) {
				case "Siri" -> siri;
				case "Karo" -> karo;
				case "Ronja" -> ronja;
				// Kan inte hända, men kompilatorn kräver att alla värden hanteras
				default -> throw new IllegalStateException(
						"Oväntat namn i parameterlistan: %s, detta är ett fel i testet, inte i din kod"
								.formatted(name));
				};

				assertThat(owner.ownsDog(dog)).isTrue().onErrorReport("%s äger inte %s som förväntat",
						DEFAULT_OWNER_NAME, name);
			});
		}

		@Order(240)
		@Test
		@DisplayName("Denna ska förneka att hen äger en annan hund")
		public void ownsDogDogShouldReturnFalseForWrongDog() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);

				owner.addDog(siri);
				owner.addDog(karo);
				owner.addDog(ronja);

				Dog anotherDog = new Dog("AnotherDogsName", DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);
				assertThat(owner.ownsDog(anotherDog)).isFalse()
						.onErrorReport("%s säger sig äga en hund som inte lagts till", DEFAULT_OWNER_NAME);

			});
		}

		@Order(250)
		@Test
		@DisplayName("Hundarna returneras (i godtycklig ordning) när man ber om ägarens hundar")
		public void getDogsShouldReturnArrayWithCorrectDogsInAnyOrder() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);

				owner.addDog(siri);
				owner.addDog(karo);
				owner.addDog(ronja);

				Dog[] expected = { siri, karo, ronja };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");
			});
		}
	}

	@DisplayName("Test för att lägga till dubletter och för många hundar")
	@Nested
	@Order(260)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class AddDogProblemTest {

		@Order(270)
		@Test
		@DisplayName("Det ska inte gå att lägga till samma hund flera gånger")
		public void addingSameDogShouldIgnoreTheSecondOne() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog dog = new Dog(DEFAULT_DOG_NAME, DEFAULT_DOG_BREED, DEFAULT_DOG_AGE, DEFAULT_DOG_WEIGHT);

				boolean firstAddReturnValue = owner.addDog(dog);
				boolean secondAddReturnValue = owner.addDog(dog);

				assertThat(owner.getDogs()).containsOnly(dog).onErrorReport("Fel hundar returneras av getDogs");

				assertThat(firstAddReturnValue).isTrue()
						.onErrorReport("addDog ska returnera true när ägarens hundar ändrats");
				assertThat(secondAddReturnValue).isFalse()
						.onErrorReport("addDog ska bara returnera true när ägarens hundar ändrats");
			});
		}

		@Order(280)
		@ParameterizedTest
		@CsvSource({ "0, 1" })
		@DisplayName("Det ska inte gå att lägga till flera hundar med samma namn)")
		public void addingMultipleDogsWithTheSameNameShouldIgnoreTheSecond() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);
				Dog buster = new Dog("Buster", "Irländsk varghund", 8, 6);
				Dog lassie = new Dog("Lassie", "Bulldogg", 9, 3);

				Dog kingOne = new Dog("King", "Samojed", 4, 5);
				Dog kingTwo = new Dog("King", "Tax", 1, 2);
				Dog kingThree = new Dog("King", "Shnauzer", 7, 8);

				owner.addDog(nova);
				boolean firstAddReturnValue = owner.addDog(kingOne);
				owner.addDog(buster);
				boolean secondAddReturnValue = owner.addDog(kingTwo);
				owner.addDog(lassie);
				boolean thirdAddReturnValue = owner.addDog(kingThree);

				Dog[] expected = { nova, buster, lassie, kingOne };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

				assertThat(owner.ownsDog(kingOne)).isTrue()
						.onErrorReport("Ägaren anser sig inte äga den King hen borde äga");
				assertThat(owner.ownsDog(kingTwo)).isFalse()
						.onErrorReport("Ägaren anser sig äga fel King, den andra inlagda");
				assertThat(owner.ownsDog(kingThree)).isFalse()
						.onErrorReport("Ägaren anser sig äga fel King, den tredje inlagda");

				assertThat(firstAddReturnValue).isTrue()
						.onErrorReport("addDog ska returnera true när ägarens hundar ändrats");
				assertThat(secondAddReturnValue).isFalse()
						.onErrorReport("addDog ska bara returnera true när ägarens hundar ändrats");
				assertThat(thirdAddReturnValue).isFalse()
						.onErrorReport("addDog ska bara returnera true när ägarens hundar ändrats");
			});
		}

		@Order(290)
		@Test
		@DisplayName("Det går inte att lägga till mer än sju hundar")
		public void addingMoreThanSevenDogsFail() {
			assertTestTimesOut(() -> {
				Owner owner = new Owner(DEFAULT_OWNER_NAME);

				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);
				Dog bamse = new Dog("Bamse", "Labrador retriever", 7, 8);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);

				owner.addDog(siri);
				owner.addDog(karo);
				owner.addDog(ronja);
				owner.addDog(bamse);
				owner.addDog(lady);
				owner.addDog(king);
				owner.addDog(nova);

				Dog buster = new Dog("Buster", "Irländsk varghund", 8, 6);
				boolean addReturnValue = owner.addDog(buster);

				Dog[] expected = { siri, karo, ronja, bamse, lady, king, nova };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

				assertThat(owner.ownsDog(buster)).isFalse()
						.onErrorReport("Ägaren anser sig äga fel King, den andra inlagda");

				assertThat(addReturnValue).isFalse()
						.onErrorReport("addDog ska bara returnera true när ägarens hundar ändrats");
			});
		}
	}

	@DisplayName("Test av tillägg av hundar via den överlagrade konstruktorn")
	@Nested
	@Order(300)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class CtrVarargsTest {

		@Order(310)
		@Test
		@DisplayName("Konstruktorn kan sätta ägda hundar")
		public void ctrCanAddDogs() {
			assertTestTimesOut(() -> {
				Dog daisy = new Dog("Daisy", "Golden retriever", 3, 4);
				Dog lassie = new Dog("Lassie", "Bulldogg", 9, 3);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, daisy, lassie);

				Dog[] expected = { daisy, lassie };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");
			});
		}

		@Order(320)
		@Test
		@DisplayName("Konstruktorn kan inte lägga till mer än sju ägda hundar")
		public void ctrCantAddMoreThanSevenDogs() {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);
				Dog bamse = new Dog("Bamse", "Labrador retriever", 7, 8);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);
				Dog buster = new Dog("Buster", "Irländsk varghund", 8, 6);

				Owner owner = null;
				try {
					owner = new Owner(DEFAULT_OWNER_NAME, siri, karo, ronja, bamse, lady, king, nova, buster);
				} catch (Exception e) {
					// Avsnitt 5, konstruktorn får kasta vilket undantag som helst om man försöker
					// lägga till för många hundar, men behöver inte göra det.
					return;
				}

				Dog[] expected = { siri, karo, ronja, bamse, lady, king, nova };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

			});
		}

		@Order(330)
		@Test
		@DisplayName("Konstruktorn kan inte lägga in samma hund två gånger")
		public void ctrCantAddTheSameDogTwice() {
			assertTestTimesOut(() -> {
				Dog bamse = new Dog("Bamse", "Labrador retriever", 7, 8);
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);
				Owner owner = new Owner(DEFAULT_OWNER_NAME, bamse, siri, karo, siri, ronja, siri);

				Dog[] expected = { siri, karo, ronja, bamse };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");
			});
		}

		@Order(340)
		@Test
		@DisplayName("Konstruktorn ignorerar dubletter när den kontrollerar om det finns plats kvar")
		public void ctrIgnoresDuplicates() {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronjaAddedSeveralTimes = new Dog("Ronja", "Golden retriever", 2, 6);
				Dog bamse = new Dog("Bamse", "Labrador retriever", 7, 8);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);

				Owner owner = null;
				owner = new Owner(DEFAULT_OWNER_NAME, siri, karo, ronjaAddedSeveralTimes, bamse, lady,
						ronjaAddedSeveralTimes, king, nova, ronjaAddedSeveralTimes);

				Dog[] expected = { siri, karo, ronjaAddedSeveralTimes, bamse, lady, king, nova };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");
			});
		}
	}

	@DisplayName("Test för att ta bort hundar")
	@Nested
	@Order(350)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class RemoveDogTest {

		@Order(360)
		@Test
		@DisplayName("Det går att ta bort en hund man äger via dess namn")
		public void removeDogByName() {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, siri, karo, lady, king, nova);

				assertThat(owner.removeDog("Siri")).isTrue().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort den första hunden man lade till");
				assertThat(owner.removeDog("Lady")).isTrue().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort en hund man lade till i mitten");
				assertThat(owner.removeDog("Nova")).isTrue().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort den sista hunden man lade till");

				Dog[] expected = { karo, king };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs efter borttag");
			});
		}

		@Order(370)
		@Test
		@DisplayName("Det går att ta bort en hund man äger via dess namn")
		public void removeDogByObject() {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, siri, karo, lady, king, nova);

				assertThat(owner.removeDog(siri)).isTrue().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort den första hunden man lade till");
				assertThat(owner.removeDog(lady)).isTrue().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort en hund man lade till i mitten");
				assertThat(owner.removeDog(nova)).isTrue().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort den sista hunden man lade till");

				Dog[] expected = { karo, king };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs efter borttag");
			});
		}

		@Order(380)
		@Test
		@DisplayName("Det går inte att ta bort en hund man inte äger via namn")
		public void removeDogByWrongName() {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, siri, karo);

				assertThat(owner.removeDog("AnotherDogsName")).isFalse().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort en hund man inte lagt till");

				Dog[] expected = { siri, karo };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs efter misslyckat borttag");
			});
		}

		@Order(390)
		@Test
		@DisplayName("Det går inte att ta bort en hund man inte äger")
		public void removeDogByWrongObject() {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, siri, karo);

				Dog anotherDog = new Dog("AnotherDogsName", "Samojed", 4, 5);
				assertThat(owner.removeDog(anotherDog)).isFalse().onErrorReport(
						"removeDog returnerar fel värde när man försöker ta bort en hund man inte lagt till");

				Dog[] expected = { siri, karo };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs efter misslyckat borttag");
			});
		}

		@Order(400)
		@Test
		@DisplayName("Tomma platser efter borttag kan återanvändas")
		public void addingAndRemovingDogs() {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);
				Dog ronja = new Dog("Ronja", "Golden retriever", 2, 6);
				Dog bamse = new Dog("Bamse", "Labrador retriever", 7, 8);
				Dog lady = new Dog("Lady", "Borzoi", 1, 6);
				Dog king = new Dog("King", "Samojed", 4, 5);
				Dog nova = new Dog("Nova", "Bulldogg", 4, 8);

				Dog buster = new Dog("Buster", "Irländsk varghund", 8, 6);
				Dog lassie = new Dog("Lassie", "Bulldogg", 9, 3);
				Dog devil = new Dog("Devil", "Varg", 89, 60);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, siri, karo, ronja, bamse, lady, king, nova);

				Dog[] expected = { siri, karo, ronja, bamse, lady, king, nova };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

				owner.removeDog(ronja);
				owner.addDog(buster);
				expected = new Dog[] { siri, karo, bamse, lady, king, nova, buster };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

				owner.removeDog(siri);
				owner.removeDog(buster);
				owner.addDog(lassie);
				expected = new Dog[] { karo, bamse, lady, king, nova, lassie };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

				owner.removeDog(karo);
				owner.removeDog(king);
				owner.removeDog(bamse);
				owner.removeDog(lassie);
				owner.removeDog(lady);
				owner.removeDog(nova);

				owner.addDog(devil);
				expected = new Dog[] { devil };
				assertThat(owner.getDogs()).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");
			});
		}

		// TODO: Kontrollera attributen efter borttag på olika platser
	}

	@DisplayName("Övriga test för specifika situationer som inte täckts av de tidigare testen")
	@Nested
	@Order(410)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class MiscTest {
		@Order(420)
		@Test
		@DisplayName("Arrayen getDogs returnerar är en kopia")
		public void getDogsPossibleAlias() {
			assertTestTimesOut(() -> {
				Dog daisy = new Dog("Daisy", "Golden retriever", 3, 4);
				Dog lassie = new Dog("Lassie", "Bulldogg", 9, 3);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, daisy);
				Dog[] actualFirstCall = owner.getDogs();

				Dog[] expected = { daisy };
				assertThat(actualFirstCall).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

				// Uppdatera den returnerade arrayen för att kunna se att detta inte påverkar
				// ägaren
				actualFirstCall[0] = lassie;

				Dog[] actualSecondCall = owner.getDogs();
				assertThat(actualSecondCall).contains(expected).inAnyOrder()
						.onErrorReport("Fel hundar returneras av getDogs");

				assertThat(actualFirstCall == actualSecondCall).isFalse()
						.onErrorReport("getDogs returnerar samma array varje gång");
			});
		}

		@Order(430)
		@ParameterizedTest
		@CsvSource({ "Siri", "siri", "SIRI", "sIrI", "Karo", "karo", "KARO", "kArO" })
		@DisplayName("Namn skrivna på olika sätt hanteras korrekt")
		public void namesWrittenInDifferentWaysHandled(String name) {
			assertTestTimesOut(() -> {
				Dog siri = new Dog("Siri", "Greyhound", 1, 8);
				Dog karo = new Dog("Karo", "Samojed", 1, 3);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, siri, karo);

				assertThat(owner.ownsDog(name)).isTrue()
						.onErrorReport("ownsDog returnerar fel värde för %s innan borttag", name);
				assertThat(owner.removeDog(name)).isTrue().onErrorReport("removeDog returnerar fel värde för %s", name);
				assertThat(owner.ownsDog(name)).isFalse()
						.onErrorReport("removeDog returnerar fel värde för %s efter borttag", name);
			});
		}

		@Order(440)
		@Test
		@DisplayName("Två hundar med samma namn är olika hundar")
		public void twoDogsWithTheSameNameHandled() {
			assertTestTimesOut(() -> {
				Dog firstDog = new Dog(DEFAULT_DOG_NAME, "Greyhound", 1, 8);
				Dog secondDog = new Dog(DEFAULT_DOG_NAME, "Samojed", 1, 3);

				Owner owner = new Owner(DEFAULT_OWNER_NAME, firstDog);

				assertThat(owner.ownsDog(firstDog)).isTrue()
						.onErrorReport("ownsDog returnerar fel värde när man försöker kontrollera den första hunden");
				assertThat(owner.ownsDog(secondDog)).isFalse()
						.onErrorReport("ownsDog returnerar fel värde när man försöker kontrollera den andra hunden");
			});
		}
	}
}

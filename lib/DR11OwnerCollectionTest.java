import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

/**
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR11: Grundläggande test för ägarsamlingsklassen")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class DR11OwnerCollectionTest extends DR01Assertions {

	private static final Owner ALVA = new Owner("Alva");
	private static final Owner BENGT = new Owner("Bengt");
	private static final Owner CLEO = new Owner("Cleo");
	private static final Owner DENNIS = new Owner("Dennis");
	private static final Owner EGON = new Owner("Egon");
	private static final Owner FOLKE = new Owner("Folke");

	private static final String NONEXISTING_OWNER_NAME = "Lea";
	private static final Owner NONEXISTING_OWNER = new Owner(NONEXISTING_OWNER_NAME);

	@DisplayName("Test för en tom samling")
	@Nested
	@Order(10)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class EmptyCollectionTest {
		@Order(20)
		@Test
		@DisplayName("Har storleken noll")
		public void sizeIsZero() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				assertThat(collection.size()).is(0).onErrorReport("Fel storlek för en tom samling");
			});
		}

		@Order(30)
		@Test
		@DisplayName("Innehåller inga ägare")
		public void containsNoOwners() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				assertThat(collection.getAllOwners()).isEmpty().onErrorReport("Fel ägare i en tom samling");
			});
		}

		@Order(40)
		@Test
		@DisplayName("Kan inte ta bort en ägare specad med namn")
		public void cantRemoveNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				assertThat(collection.removeOwner(NONEXISTING_OWNER_NAME)).isFalse()
						.onErrorReport("Fel returvärde från removeOwner(String) för en tom samling");
			});
		}

		@Order(50)
		@Test
		@DisplayName("Kan inte ta bort en ägare")
		public void cantRemoveNonexistingOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				assertThat(collection.removeOwner(NONEXISTING_OWNER)).isFalse()
						.onErrorReport("Fel returvärde från removeOwner(Owner) för en tom samling");
			});
		}

		@Order(60)
		@Test
		@DisplayName("Innehåller inte en ägare specad med namn")
		public void dontContainNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				assertThat(collection.containsOwner(NONEXISTING_OWNER_NAME)).isFalse()
						.onErrorReport("Fel returvärde från containsOwner(String) för en tom samling");
			});
		}

		@Order(70)
		@Test
		@DisplayName("Innehåller inte en ägare")
		public void dontContainNonexistingOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				assertThat(collection.containsOwner(NONEXISTING_OWNER)).isFalse()
						.onErrorReport("Fel returvärde från containsOwner(Owner) för en tom samling");
			});
		}

		@Order(80)
		@Test
		@DisplayName("Kan inte hämta ut en ägare")
		public void cantGetNonexistingOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				assertThat(collection.getOwner(NONEXISTING_OWNER_NAME)).isNull()
						.onErrorReport("Fel returvärde från getOwner(String) för en tom samling");
			});
		}
	}

	@DisplayName("Test för samling med en ägare")
	@Nested
	@Order(90)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class SingleOwnerCollectionTest {
		@Order(100)
		@Test
		@DisplayName("Har storleken ett")
		public void sizeIsOne() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(CLEO);
				assertThat(collection.size()).is(1).onErrorReport("Fel storlek för en samling med en ägare");
			});
		}

		@Order(110)
		@Test
		@DisplayName("Innehåller rätt ägare")
		public void containsCorrectOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				assertThat(collection.getAllOwners()).containsOnly(ALVA).onErrorReport("Fel ägare i en samling med en ägare");
			});
		}

		@Order(120)
		@Test
		@DisplayName("Kan ta bort en ägare specad med namn")
		public void canRemoveOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(DENNIS);
				assertThat(collection.removeOwner("Dennis")).isTrue()
						.onErrorReport("Fel returvärde från removeOwner(String) för en samling med en ägare");
				assertThat(collection.size()).is(0)
						.onErrorReport("Fel storlek för en samling där den enda ägaren precis tagits bort");
				assertThat(collection.getAllOwners()).isEmpty()
						.onErrorReport("Fel ägare i en samling där den enda ägaren precis tagits bort");
			});
		}

		@Order(130)
		@Test
		@DisplayName("Kan inte ta bort en ägare specad med namn som inte finns i samlingen")
		public void cantRemoveNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(DENNIS);
				assertThat(collection.removeOwner(NONEXISTING_OWNER_NAME)).isFalse()
						.onErrorReport("Fel returvärde från removeOwner(String) för en samling men en ägare");
				assertThat(collection.size()).is(1).onErrorReport("Fel storlek för en samling med en ägare");
				assertThat(collection.getAllOwners()).containsOnly(DENNIS).onErrorReport("Fel ägare i en samling med en ägare");
			});
		}

		@Order(140)
		@Test
		@DisplayName("Kan ta bort en ägare")
		public void canRemoveOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(BENGT);
				assertThat(collection.removeOwner(BENGT)).isTrue()
						.onErrorReport("Fel returvärde från removeOwner(Owner) för en samling med en ägare");
				assertThat(collection.size()).is(0)
						.onErrorReport("Fel storlek för en samling med en ägare som precis tagits bort");
				assertThat(collection.getAllOwners()).isEmpty()
						.onErrorReport("Fel ägare i en samling med en ägare som precis tagits bort");
			});
		}

		@Order(150)
		@Test
		@DisplayName("Kan inte ta bort en ägare som inte finns i samlingen")
		public void canRemoveNonexistingOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(BENGT);
				assertThat(collection.removeOwner(NONEXISTING_OWNER)).isFalse()
						.onErrorReport("Fel returvärde från removeOwner(Owner) för en samling med en ägare");
				assertThat(collection.size()).is(1).onErrorReport("Fel storlek för en samling med en ägare");
				assertThat(collection.getAllOwners()).containsOnly(BENGT).onErrorReport("Fel ägare i en samling med en ägare");
			});
		}

		@Order(160)
		@Test
		@DisplayName("Innehåller ägaren specad med namn")
		public void containsOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(EGON);
				assertThat(collection.containsOwner("Egon")).isTrue()
						.onErrorReport("Fel returvärde från containsOwner(String) för en samling med en ägare");
			});
		}

		@Order(170)
		@Test
		@DisplayName("Innehåller inte en ägare specad med namn som inte finns i samlingen")
		public void dontContainNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(FOLKE);
				assertThat(collection.containsOwner(NONEXISTING_OWNER_NAME)).isFalse()
						.onErrorReport("Fel returvärde från containsOwner(String) för en tom samling med en ägare");
			});
		}

		@Order(180)
		@Test
		@DisplayName("Innehåller ägaren")
		public void containsOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(FOLKE);
				assertThat(collection.containsOwner(FOLKE)).isTrue()
						.onErrorReport("Fel returvärde från containsOwner(Owner) för en samling med en ägare");
			});
		}

		@Order(190)
		@Test
		@DisplayName("Innehåller inte en ägare som inte finns i samlingen")
		public void dontContainNonexistingOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(FOLKE);
				assertThat(collection.containsOwner(NONEXISTING_OWNER)).isFalse()
						.onErrorReport("Fel returvärde från containsOwner(Owner) för en samling med en ägare");
			});
		}

		@Order(200)
		@Test
		@DisplayName("Kan hämta en ägare")
		public void canGetOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(CLEO);
				assertThat(collection.getOwner("Cleo")).is(CLEO)
						.onErrorReport("Fel returvärde från getOwner(String) för en samling med en ägare");
			});
		}

		@Order(210)
		@Test
		@DisplayName("Kan inte hämta ut en ägare som inte finns i samlingen")
		public void cantGetNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(CLEO);
				assertThat(collection.getOwner(NONEXISTING_OWNER_NAME)).isNull()
						.onErrorReport("Fel returvärde från getOwner(String) för en samling med en ägare");
			});
		}
	}

	@DisplayName("Test för samling med mer än en ägare")
	@Nested
	@Order(220)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class MultipleOwnerCollectionTest {
		@Order(230)
		@Test
		@DisplayName("Har storleken ett")
		public void sizeIsCorrect() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.size()).is(3).onErrorReport("Fel storlek för en samling med mer än en ägare");
			});
		}

		@Order(240)
		@Test
		@DisplayName("Innehåller rätt ägare")
		public void containsCorrectOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.getAllOwners()).contains(ALVA, BENGT, CLEO).inAnyOrder()
						.onErrorReport("Fel ägare i en samling med mer än en ägare");
			});
		}

		private static Stream<Arguments> ownersToRemove() {
			return Stream.of(Arguments.of("Alva", ALVA, new Owner[] { ALVA, BENGT, CLEO }, new Owner[] { BENGT, CLEO }),
					Arguments.of("Bengt", BENGT, new Owner[] { ALVA, BENGT, CLEO }, new Owner[] { ALVA, CLEO }),
					Arguments.of("Cleo", CLEO, new Owner[] { ALVA, BENGT, CLEO }, new Owner[] { ALVA, BENGT }));
		}

		@Order(250)
		@ParameterizedTest(name = "{0}")
		@MethodSource("ownersToRemove")
		@DisplayName("Kan ta bort en ägare specad med namn")
		public void canRemoveOwnerByName(String nameToRemove, Owner ownerToRemove, Owner[] owners,
				Owner[] expectedOwners) {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();

				for (Owner owner : owners) {
					collection.addOwner(owner);
				}

				assertThat(collection.removeOwner(nameToRemove)).isTrue()
						.onErrorReport("Fel returvärde från removeOwner(String) för en samling med mer än en ägare");
				assertThat(collection.size()).is(expectedOwners.length).onErrorReport(
						"Fel storlek för en samling med mer än en ägare där en ägare precis tagits bort via namn");
				assertThat(collection.getAllOwners()).contains(expectedOwners).inAnyOrder().onErrorReport(
						"Fel ägare i en samling med mer än en ägare där en ägare precis tagits bort via namn");
			});
		}

		@Order(260)
		@Test
		@DisplayName("Kan inte ta bort en ägare specad med namn som inte finns i samlingen")
		public void cantRemoveNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.removeOwner(NONEXISTING_OWNER_NAME)).isFalse().onErrorReport(
						"Fel returvärde från removeOwner(String) för en samling men mer än en ägare där man precis försökt ta bort en ägare som inte finns i samlingen via namn");
				assertThat(collection.size()).is(3).onErrorReport(
						"Fel storlek för en samling med mer än en ägare där man precis försökt ta bort en ägare som inte finns i samlingen via namn");
				assertThat(collection.getAllOwners()).contains(ALVA, BENGT, CLEO).inAnyOrder().onErrorReport(
						"Fel ägare i en samling med mer än en ägare där man precis försökt ta bort en ägare som inte finns i samlingen via namn");
			});
		}

		@Order(270)
		@ParameterizedTest(name = "{0}")
		@MethodSource("ownersToRemove")
		@DisplayName("Kan ta bort en ägare")
		public void canRemoveOwner(String nameToRemove, Owner ownerToRemove, Owner[] owners, Owner[] expectedOwners) {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();

				for (Owner owner : owners) {
					collection.addOwner(owner);
				}

				assertThat(collection.removeOwner(ownerToRemove)).isTrue()
						.onErrorReport("Fel returvärde från removeOwner(String) för en samling med mer än en ägare");
				assertThat(collection.size()).is(expectedOwners.length).onErrorReport(
						"Fel storlek för en samling med mer än en ägare där en ägare precis tagits bort");
				assertThat(collection.getAllOwners()).contains(expectedOwners).inAnyOrder()
						.onErrorReport("Fel ägare i en samling med mer än en ägare där en ägare precis tagits bort");
			});
		}

		@Order(280)
		@Test
		@DisplayName("Kan inte ta bort en ägare som inte finns i samlingen")
		public void canRemoveNonexistingOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.removeOwner(NONEXISTING_OWNER)).isFalse()
						.onErrorReport("Fel returvärde från removeOwner(Owner) för en samling med mer än en ägare");
				assertThat(collection.size()).is(3).onErrorReport(
						"Fel storlek för en samling med mer än en ägare där man precis försökt ta bort en ägare som inte finns i samlingen");
				assertThat(collection.getAllOwners()).contains(ALVA, BENGT, CLEO).inAnyOrder().onErrorReport(
						"Fel ägare i en samling med mer än en ägare där man precis försökt ta bort en ägare som inte finns i samlingen");
			});
		}

		private static Stream<Arguments> ownersToGet() {
			return Stream.of(Arguments.of("Alva", ALVA), Arguments.of("Bengt", BENGT), Arguments.of("Cleo", CLEO));
		}
		
		@Order(290)
		@ParameterizedTest(name = "{0}")
		@MethodSource("ownersToGet")
		@DisplayName("Innehåller ägaren specad med namn")
		public void containsOwnerByName(String name, Owner owner) {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.containsOwner(name)).isTrue()
						.onErrorReport("Fel returvärde från containsOwner(String) för en samling med mer än en ägare");
			});
		}

		@Order(300)
		@Test
		@DisplayName("Innehåller inte en ägare specad med namn som inte finns i samlingen")
		public void dontContainNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.containsOwner(NONEXISTING_OWNER_NAME)).isFalse().onErrorReport(
						"Fel returvärde från containsOwner(String) för en tom samling med mer än en ägare");
			});
		}

		@Order(310)
		@ParameterizedTest(name = "{0}")
		@MethodSource("ownersToGet")
		@DisplayName("Innehåller ägaren")
		public void containsOwner(String name, Owner expectedToExists) {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.containsOwner(expectedToExists)).isTrue()
						.onErrorReport("Fel returvärde från containsOwner(Owner) för en samling med mer än en ägare");
			});
		}

		@Order(320)
		@Test
		@DisplayName("Innehåller inte en ägare som inte finns i samlingen")
		public void dontContainNonexistingOwner() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.containsOwner(NONEXISTING_OWNER)).isFalse()
						.onErrorReport("Fel returvärde från containsOwner(Owner) för en samling med mer än en ägare");
			});
		}

		@Order(330)
		@ParameterizedTest(name = "{0}")
		@MethodSource("ownersToGet")
		@DisplayName("Kan hämta alla ägare")
		public void canGetOwnerByName(String name, Owner expected) {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.getOwner(name)).is(expected)
						.onErrorReport("Fel returvärde från getOwner(String) för en samling med mer än en ägare");
			});
		}

		@Order(340)
		@Test
		@DisplayName("Kan inte hämta ut en ägare som inte finns i samlingen")
		public void cantGetNonexistingOwnerByName() {
			assertTestTimesOut(() -> {
				OwnerCollection collection = new OwnerCollection();
				collection.addOwner(ALVA);
				collection.addOwner(BENGT);
				collection.addOwner(CLEO);
				assertThat(collection.getOwner(NONEXISTING_OWNER_NAME)).isNull()
						.onErrorReport("Fel returvärde från getOwner(String) för en samling med mer än en ägare");
			});
		}
	}

}

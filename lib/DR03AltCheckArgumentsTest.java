import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * Testfallen i denna klass är inte en del av den obligatoriska uppgiften, utan
 * bara exempel på hur test av kontroller av argument kan se ut.
 * 
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR03 extra: Exempel på test av kontroller av argument")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DR03AltCheckArgumentsTest {

	@Order(10)
	@Test
	void validCreationDoesNotThrowException() {
		assertDoesNotThrow(() -> new Dog("Max", "Tax", 1, 2));
	}

	@Order(20)
	@Test
	void emptyNameThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Dog("", "Tax", 1, 2));
	}

	@Order(30)
	@Test
	/**
	 * Detta test är klumpigare än det normalt skulle vara eftersom instruktionerna
	 * presenterar två olika lösningar som kastar olika typer av undantag.
	 */
	void nullNameThrowsException() {
		var e = assertThrows(RuntimeException.class, () -> new Dog(null, "Tax", 1, 2));
		assertTrue(e instanceof IllegalArgumentException || e instanceof NullPointerException,
				e + " är inte ett IllegalArgumentException eller ett NullPointerException");
	}

	@Order(40)
	@Test
	void emptyBreedThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Dog("Max", "", 1, 2));
	}

	@Order(50)
	@Test
	/**
	 * Detta test är klumpigare än det normalt skulle vara eftersom instruktionerna
	 * presenterar två olika lösningar som kastar olika typer av undantag.
	 */
	void nullBreedThrowsException() {
		var e = assertThrows(RuntimeException.class, () -> new Dog("Max", null, 1, 2));
		assertTrue(e instanceof IllegalArgumentException || e instanceof NullPointerException,
				e + " är inte ett IllegalArgumentException eller ett NullPointerException");
	}

	@Order(60)
	@Test
	void negativeAgeThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Dog("Max", "Tax", -1, 2));
	}

	@Order(70)
	@Test
	void negativeWeightThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Dog("Max", "Tax", 1, -2));
	}

}

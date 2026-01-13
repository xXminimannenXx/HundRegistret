import java.util.*;

import org.junit.jupiter.api.*;

/**
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR12: sortera ägare")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR12SortingOwnersTest extends DR01Assertions {

	private static final Random RND = new Random();

	private void randomize(Owner[] owners) {
		for (int i = 0; i < owners.length; i++) {
			int j = RND.nextInt(owners.length);

			Owner temp = owners[i];
			owners[i] = owners[j];
			owners[j] = temp;
		}
	}

	@Order(10)
	@RepeatedTest(5) // Detta test körs fem gånger
	@DisplayName("getOwners ska returnera en sorterad lista av ägare")
	public void getOwnersReturnsSortedList() {
		assertTestTimesOut(() -> {
			Owner asta = new Owner("Asta");
			Owner bo = new Owner("Bo");
			Owner cilla = new Owner("Cilla");
			Owner dag = new Owner("Dag");
			Owner eira = new Owner("Eira");
			Owner folke = new Owner("Folke");
			Owner gun = new Owner("Gun");

			Owner[] owners = { asta, bo, cilla, dag, eira, folke, gun };
			randomize(owners); // Slumpa ordningen ägarna läggs in i samlingen

			OwnerCollection collection = new OwnerCollection();
			for (Owner owner : owners) {
				collection.addOwner(owner);
			}

			List<Owner> actual = collection.getAllOwners();
			assertThat(actual).contains(asta, bo, cilla, dag, eira, folke, gun).inThatOrder()
					.onErrorReport("Ägar-listan getOwners returnerar är inte korrekt sorterade");
		});
	}

	/**
	 * Kontrollerar specialfallet när man försöker sortera en tom lista
	 */
	@Order(20)
	@Test
	@DisplayName("getOwners ska klara av att sortera en tom lista av ägare")
	public void getOwnersReturnsEmptyListForEmptyCollection() {
		assertTestTimesOut(() -> {
			OwnerCollection collection = new OwnerCollection();

			List<Owner> actual = collection.getAllOwners();
			assertThat(actual).isEmpty()
					.onErrorReport("Ägar-listan getOwners returnerar för en tom samling är inte korrekt");
		});
	}

}

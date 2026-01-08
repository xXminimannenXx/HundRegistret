import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

/**
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR06: Grundläggande test av den uppräkningsbara typen")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DR06SortingAlgorithmEnumTest extends DR01Assertions {

	// Jo, en enum är en klass...
	private static final Class<?> CLASS_UNDER_TEST = SortingAlgorithm.class;

	@Order(10)
	@Test
	@DisplayName("Grundläggande krav för skyddsnivåer följs i klassen DogSorter")
	public void basicStyleRulesForAccessModifiersFollowed() {
		assertOnlyAllowedAccessModifiersUsed(CLASS_UNDER_TEST);
	}

	@Order(20)
	@Test
	@DisplayName("Grundläggande regler för hur namn formateras följs på klassnivå i klassen DogSorter")
	public void basicStyleRulesForNamesAtClassLevelFollowed() {
		assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(CLASS_UNDER_TEST);
	}
	
	@Order(30)
	@Test
	@DisplayName("Rätt antal uppräkningsbara värden")
	public void correctNumberOfEnumValues() {
		assertThat(SortingAlgorithm.values().length).isBetween(2, 6)
				.onErrorReport("Fel antal uppräkningsbara värden i SortingAlgoritm");
	}

	@Order(40)
	@ParameterizedTest
	@DisplayName("Bara förväntade uppräkningsbara värden finns")
	@EnumSource(SortingAlgorithm.class)
	public void expectedEnumValuesExists(SortingAlgorithm algorithm) {

		assertThat(algorithm.name())
				.isAnyOf("BUBBLE_SORT", "INSERTION_SORT", "SELECTION_SORT", "HEAP_SORT", "QUICK_SORT", "MERGE_SORT")
				.onErrorReport("Det uppräkningsbara värdet %s i SortingAlgorithm är inte något av de förväntade",
						algorithm.name());
	}

}

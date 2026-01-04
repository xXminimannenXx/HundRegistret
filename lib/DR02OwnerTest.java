import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Denna klass innehåller grundläggande test för klassen <code>Owner</code>.
 * Testen använder sig av metoder från klassen <code>DR01Assertions</code> som
 * måste finnas i samma katalog som denna. (Detta gäller samtliga test-klasser
 * för hundregistret.)
 * <p>
 * De två första testen kontrollerar de mest grundläggande ickefunktionella
 * kraven för klassen Owner. Dessa test är mycket svaga, och kan bara fånga
 * riktigt uppenbara fel. Om du för muspekaren över assert-satsen i testen
 * kommer du att få en mer utförlig beskrivning av vad exakt som testas.
 * <p>
 * En betydligt mer utförlig kontroll av de ickefunktionella kraven kommer att
 * göras efter granskningen. Se instruktionerna för mer information om detta.
 * 
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR02: Grundläggande test för en ägare utan hundar")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR02OwnerTest extends DR01Assertions {

	private static final Class<?> CLASS_UNDER_TEST = Owner.class;

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
	@CsvSource({ "Henrik", "Olle" })
	@DisplayName("Konstruktorn sätter namnet")
	public void ctrSetsName(String name) {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(name);
			assertThat(owner.getName()).is(name).ignoringCase().onErrorReport("Fel namn returneras av getName");
		});
	}

	private void assertNameIsNormalized(String name, String... alternatives) {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(name);
			assertThat(owner.getName()).isAnyOf(alternatives).onErrorReport("Fel namn returneras av getName");
		});
	}

	@Order(40)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "henrik,Henrik,HENRIK", "OllE,Olle,OLLE" })
	@DisplayName("Namnet normaliseras")
	public void nameIsNormalized(String name, String fullyNormalizedName, String upperCaseName) {
		assertNameIsNormalized(name, fullyNormalizedName, upperCaseName);
	}

	@Order(50)
	@Test
	@DisplayName("Flera namn normaliseras")
	public void multipleNamesAreNormalized() {
		assertNameIsNormalized("adam svensson", "Adam svensson", "ADAM SVENSSON", "Adam Svensson");
	}

	@Order(60)
	@Test
	@DisplayName("Sammansatta namn normaliseras")
	public void hyphenatedNamesAreNormalized() {
		assertNameIsNormalized("britt-marie", "Britt-marie", "BRITT-MARIE", "Britt-Marie");
	}

	@Order(70)
	@ParameterizedTest(name = "{0}")
	@CsvSource({ "henrik,Henrik,HENRIK", "OllE,Olle,OLLE" })
	@DisplayName("Namnet finns i strängrepresentationen")
	public void stringRepresentationContainsName(String name, String fullyNormalizedName, String upperCaseName) {
		assertTestTimesOut(() -> {
			Owner owner = new Owner(name);
			assertThat(owner.toString()).containsAnyOf(fullyNormalizedName, upperCaseName)
					.onErrorReport("Namnet finns inte i strängrepresentationen");
		});
	}

}

import static org.junit.jupiter.api.AssertionFailureBuilder.assertionFailure;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.text.*;
import java.time.Duration;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.function.Executable;

/**
 * Denna klass innehåller testmetoder och data som JUnit-testfallen för
 * uppgiften behöver. Filen måste ligga i samma katalog som dessa för att de ska
 * fungera.
 * <p>
 * Eftersom innehållet i denna klass påverkar, och påverkas av, alla andra
 * testfall till uppgiften kommer den troligtvis att behöva uppdateras
 * åtminstone ett par gånger under kursen.
 * <p>
 * Du behöver <u>inte</u> lägga någon tid på förstå det som finns i denna klass.
 * Allting viktigt för uppgiften finns i testfilerna, dvs DR01OwnerTest osv.
 * 
 * @author Henrik Bergström
 * @version 1.0
 * 
 * @apiNote Denna klass påverkas av alla andra testklasser i projektet, och
 *          påverkar i sin tur dessa. Detta betyder att den troligtvis kommer
 *          att behöva uppdateras flera gånger under kursens gång.
 * 
 * @implNote För den som har erfarenhet av JUnit och undrar varför vi inte
 *           använder de vanliga assert-metoderna (assertEquals etc.) i testerna
 *           så beror det på att de blir väldigt klumpiga i flera situationer
 *           som är ovanliga i vanlig testning, men förekommer i detta specifika
 *           sammanhang, till exempel när det finns flera möjliga alternativ som
 *           är korrekta.
 *           <p>
 *           Ett matcherbibliotek som hamcrest skulle fungerat, men också
 *           introducerat ett beroende som tvingat alla studenter att ladda ner
 *           och installera biblioteket.
 *           <p>
 *           Ett eget matcherbibliotek ger också möjlighet att använda JavaDoc
 *           för att ge dokumentation för de olika delstegen i utvärderingen.
 *           <p>
 *           Anledningen till att vi använder arv istället för statiska importer
 *           är att de sistnämnda kräver paket, som inte tas upp på PROG1.
 * 
 */
public abstract class DR01Assertions {

	/**
	 * Hur lång tid ett test får ta på sig innan det automatiskt anses misslyckat
	 * (50ms).
	 */
	private static final Duration TEST_TIMEOUT = Duration.ofMillis(50);

	/*
	 * Konstanter som beskriver mönstren för hur namn får se ut i form av så kallade
	 * reguljära uttryck (eng. regular expression).
	 */

	/**
	 * Klassnamn inleds med stor bokstav, och har stor bokstav på varje nytt ord. De
	 * kan inledas med ett paketnamn.
	 */
	private static final Pattern CLASS_NAMING_PATTERN = Pattern.compile("([a-z][a-z0-9_]*.)*([A-Z][a-z0-9]+)+");

	/**
	 * Konstanter skrivs med stora bokstäver och _ mellan varje ord.
	 * 
	 * @implNote $ läggs in i vissa konstanter som Java-kompilatorn skapar, tex
	 *           <code>ENUM$VALUES</code>. Den ska <u>aldrig</u> användas i dina
	 *           egna namn på konstanter.
	 */
	private static final Pattern CONSTANT_NAMING_PATTERN = Pattern.compile("[A-Z]+([_\\$][A-Z0-9]+)*");

	/**
	 * Variabler och metoder inleds med liten bokstav och har stor bokstav på varje
	 * nytt ord.
	 * 
	 * @implNote $ läggs in i vissa namn som Java-kompilatorn skapar, tex namn på
	 *           lamda-uttryck.
	 */
	private static final Pattern VARIABLE_AND_METHOD_NAMING_PATTERN = Pattern
			.compile("[a-z][a-z0-9$]+([A-Z][a-z0-9]+)*");

	/**
	 * Fält i InputReader som används för att kontrollera att bara en instans skapas
	 */
	protected static final PossibleField MULTIPLE_INSTANCE_GUARD = new PossibleField(
			"kontrollerar att bara en instans av InputReader skapas", "InputReader", boolean.class, true);

	/**
	 * Det ursprungliga värdet för fältet ovan, används för att resetta kontrollen
	 * när testen behöver skapa flera instanser
	 */
	protected static final boolean ORIGINAL_MULTIPLE_INSTANCE_GUARD_VALUE = (boolean) MULTIPLE_INSTANCE_GUARD
			.getAndIgnoreIfNotPresent(false);

	/**
	 * Kontrollera att testet inte tar för lång tid på sig att köra. Om detta test
	 * misslyckas beror det antagligen på en oändlig loop eller rekursion i ditt
	 * program, eller på att det står och väntar på inmatning från användaren som
	 * aldrig kommer.
	 * <p>
	 * Tiden som accepteras är generös, men det går aldrig att <u>helt</u> garantera
	 * att ett misslyckande inte beror på att datorn är upptagen med något annat som
	 * tar alla resurser. Risken för detta är dock <u>mycket</u> liten om du inte
	 * märker själv att hela datorn verkar (mycket) långsam.
	 * <p>
	 * Om du trots ovanstående varning är övertygad om att det är timeout-tiden som
	 * är för kort, och inte din kod som är fel, så kan du testa att öka tiden som
	 * accepteras genom att ändra konstanten <code>TEST_TIMEOUT</code> i
	 * <code>DR00Assertions</code>.
	 * 
	 * @see #TEST_TIMEOUT
	 */
	protected void assertTestTimesOut(Executable executable) {
		assertTimeoutPreemptively(TEST_TIMEOUT, executable, "Testet tog mer tid på sig än det borde");
	}

	/**
	 * Kontrollera att det givna heltalet...
	 */
	protected Value.IntegerValue assertThat(long actual) {
		return new Value.IntegerValue(actual);
	}

	/**
	 * Kontrollera att det givna flyttalet...
	 */
	protected Value.DoubleValue assertThat(double actual) {
		return new Value.DoubleValue(actual);
	}

	/**
	 * Kontrollera att det givna boolean-värdet...
	 */
	protected Value.BooleanValue assertThat(boolean actual) {
		return new Value.BooleanValue(actual);
	}

	/**
	 * Kontrollera att den givna strängen...
	 */
	protected Value.StringValue assertThat(String actual) {
		return new Value.StringValue(actual);
	}

	/**
	 * Kontrollera att det givna objektet...
	 */
	protected Value.ObjectValue assertThat(Object actual) {
		return new Value.ObjectValue(actual);
	}

	/**
	 * Kontrollera att det givna objektet i Optional-instansen...
	 */
	protected Value.ObjectValue assertThat(Optional<?> actual) {
		return new Value.ObjectValue(actual.orElse(null));
	}

	/**
	 * Kontrollera att den givna arrayen...
	 */
	protected <T> Value.ArrayValue<T> assertThat(T[] actual) {
		return new Value.ArrayValue<T>(actual);
	}

	/**
	 * Kontrollera att den givna listan...
	 */
	protected <T> Value.ListValue<T> assertThat(List<T> actual) {
		return new Value.ListValue<T>(actual);
	}

	/**
	 * Kontrollera att de grundläggande ickefunktionella kraven på skyddsnivåer i
	 * uppgiften följs i en given klass. Det som kontrolleras är:
	 * <p>
	 * <ul>
	 * <li>Alla delar av klassen har en explicit skyddsnivå satt.
	 * <li>Bara <code>public</code> och <code>private</code> används.
	 * <li>Alla variabler på klassnivå är <code>private</code>.
	 * </ul>
	 * <p>
	 * Observera att testet <u>inte</u> har någon möjlighet att kontrollera att rätt
	 * skyddsnivåer används på metoder. En sådan kontroll skulle kräva att testet
	 * förstod metodernas syfte vilket det inte kan göra. Detta kommer därför att
	 * kontrolleras i samband med granskningen och den manuella rättningen efter
	 * deadline.
	 * 
	 * @param cut klassen vars skyddsnivåer ska kontrolleras. (CUT står för Class
	 *            Under Test.)
	 */
	protected void assertOnlyAllowedAccessModifiersUsed(Class<?> cut) {
		Style.assertOnlyAllowedAccessModifiersUsed(cut);
	}

	/**
	 * Kontrollera att de grundläggande reglerna för namngivning i Java följs på
	 * klassnivå i en given klass. Det som kontrolleras är:
	 * <p>
	 * <ul>
	 * <li>Att KlassNamn skrivs på detta sätt.
	 * <li>Att KONSTANT_NAMN skrivs på detta sätt.
	 * <li>Att variabelOchMetodNamn skrivs på detta sätt.
	 * <li>Att bara bokstäver i det engelska alfabetet används.
	 * </ul>
	 * <p>
	 * Observera att testet <u>inte</u> har någon möjlighet att kontrollera att
	 * namnet är korrekt, tydligt, inteskrivetpådethärsättet, eller något annat som
	 * skulle kräva att det faktiskt förstod vad namnen betyder. Detta kommer därför
	 * att kontrolleras i samband med granskningen och den manuella rättningen efter
	 * deadline.
	 * <p>
	 * Observera också att testet <u>bara</u> kontrollerar namn på klassnivå, alltså
	 * inga namn inuti metoder.
	 * 
	 * @param cut klassen vars skyddsnivåer ska kontrolleras. (CUT står för Class
	 *            Under Test.)
	 */
	protected void assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(Class<?> cut) {
		Style.assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(cut);
	}

	protected static void failTest(String errorMsg, Object... args) {
		fail(errorMsg.formatted(args));
	}

	protected static void failTest(Throwable cause, String context) {
		fail("Ett undantag av typen %s%s kastades när %s. Du hittar mer information om detta undantag en bit ner i stack/failure tracen, under \"Caused by\"."
				.formatted(cause.getClass().getName(),
						cause.getMessage() == null ? "" : " med meddelandet \"" + cause.getMessage() + "\"", context),
				cause);
	}

	protected static class Value {

		protected static interface DogRegisterAssertion {

			String DEFAULT_ERROR_MSG = "Fel resultat";

			/**
			 * Om testet misslyckas, ge detta felmeddelandet.
			 */
			void onErrorReport(String errorMsg);

			/**
			 * Om testet misslyckas, ge detta felmeddelandet.
			 * 
			 * @see #onErrorReport(String)
			 * @implNote Denna metod gör samma sak som <code>onErrorReport(String)</code>,
			 *           men ger tydligare test om felmeddelandet ska formateras.
			 */
			default void onErrorReport(String template, Object... args) {
				onErrorReport(template.formatted(args));
			}

			/**
			 * Om testet misslyckas så ge ett generellt felmeddelande.
			 * 
			 * @implNote Denna metod ska normalt inte användas, om inte anledningen till
			 *           felet är <b>mycket</b> uppenbart.
			 */
			default void onErrorReportSomethingGeneric() {
				onErrorReport(DEFAULT_ERROR_MSG);
			}

		}

		protected static record IntegerValue(long actual) {

			/**
			 * har detta värde.
			 */
			public IntIsAssertion is(long expected) {
				return new IntIsAssertion(expected, actual);
			}

			/**
			 * ligger inom detta intervall.
			 */
			public IntIsBetweenAssertion isBetween(long min, long max) {
				return new IntIsBetweenAssertion(min, max, actual);
			}

			/**
			 * är lägre än detta värde.
			 */
			public IntIsLessThanAssertion isLessThan(long limit) {
				return new IntIsLessThanAssertion(limit, actual);
			}

			/**
			 * är högre än detta värde.
			 */
			public IntIsGreaterThanAssertion isGreaterThan(long limit) {
				return new IntIsGreaterThanAssertion(limit, actual);
			}

			protected static record IntIsAssertion(long expected, long actual) implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					assertEquals(expected, actual, errorMsg);
				}

			}

			protected static record IntIsBetweenAssertion(long expectedMin, long expectedMax, long actual)
					implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					if (actual < expectedMin || actual > expectedMax)
						assertionFailure().message(errorMsg)
								.expected("Ett heltal mellan %d och %d (inklusive)".formatted(expectedMin, expectedMax))
								.actual(actual).buildAndThrow();
				}

			}

			protected static record IntIsLessThanAssertion(long limit, long actual) implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					if (actual >= limit)
						assertionFailure().message(errorMsg).expected("Ett värde under %d".formatted(limit))
								.actual(actual).buildAndThrow();
				}

			}

			protected static record IntIsGreaterThanAssertion(long limit, long actual) implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					if (actual <= limit)
						assertionFailure().message(errorMsg).expected("Ett värde över %d".formatted(limit))
								.actual(actual).buildAndThrow();
				}

			}

		}

		protected static record DoubleValue(double actual) {

			/**
			 * har detta värde. (±En liten felmarginal eftersom flyttal inte kan
			 * representeras exakt.)
			 */
			public DoubleIsAssertion is(double expected) {
				return new DoubleIsAssertion(expected, actual);
			}

			protected static record DoubleIsAssertion(double expected, double actual, double tolerance)
					implements DogRegisterAssertion {

				private static final double RELAXED_TOLERANCE = 0.01;
				private static final double STRICT_TOLERANCE = 0.0001;
				private static final DecimalFormat FORMATTER = new DecimalFormat("#.#",
						DecimalFormatSymbols.getInstance(Locale.US));

				static {
					FORMATTER.setMaximumFractionDigits(5);
				}

				public DoubleIsAssertion(double expected, double actual) {
					this(expected, actual, RELAXED_TOLERANCE);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					if (Math.abs(expected - actual) > tolerance)
						assertionFailure().message(errorMsg).expected(expected + "±" + FORMATTER.format(tolerance))
								.actual(actual).buildAndThrow();
				}

				/**
				 * Kräv att värdet ligger mycket nära det förväntade.
				 * 
				 * @see #STRICT_TOLERANCE
				 */
				public DoubleIsAssertion withStrictTolerance() {
					return withTolerance(STRICT_TOLERANCE);
				}

				/**
				 * Acceptera att värdet kan ligga lite mer ifrån det förväntade, men fortfarande
				 * nära.
				 * 
				 * @see #RELAXED_TOLERANCE
				 */
				public DoubleIsAssertion withRelaxedTolerance() {
					return withTolerance(RELAXED_TOLERANCE);
				}

				/**
				 * Acceptera att värdet ligger max så här långt från det förväntade.
				 */
				public DoubleIsAssertion withTolerance(double tolerance) {
					return new DoubleIsAssertion(expected, actual, tolerance);
				}

			}

		}

		protected static record BooleanValue(boolean actual) {

			/**
			 * är sann.
			 */
			public BooleanIsAssertion isTrue() {
				return new BooleanIsAssertion(true, actual);
			}

			/**
			 * är falsk.
			 */
			public BooleanIsAssertion isFalse() {
				return new BooleanIsAssertion(false, actual);
			}

			protected static record BooleanIsAssertion(boolean expected, boolean actual)
					implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					assertEquals(expected, actual, errorMsg);
				}

			}

		}

		protected static record StringValue(String actual) {

			private static Function<String, String> NO_OP_TRANSFORMER = s -> s;
			private static Function<String, String> TO_UPPER_TRANSFORMER = s -> s.toUpperCase();

			/**
			 * har detta värde.
			 */
			public StringIsAssertion is(String expected) {
				return new StringIsAssertion(expected, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * har något av dessa värden.
			 */
			public StringIsAnyOfAssertion isAnyOf(String... expectedAlternatives) {
				return new StringIsAnyOfAssertion(expectedAlternatives, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * innehåller denna sträng någonstans.
			 */
			public StringContainsAssertion contains(String expected) {
				return new StringContainsAssertion(expected, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * inte innehåller denna sträng någonstans.
			 */
			public StringDontContainsAssertion dontContains(String expected) {
				return new StringDontContainsAssertion(expected, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * innehåller alla dessa strängar.
			 */
			public StringContainsAllOfAssertionStep containsAllOf(String... expectedAlternatives) {
				return new StringContainsAllOfAssertionStep(expectedAlternatives, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * innehåller någon av dessa strängar.
			 */
			public StringContainsAnyOfAssertion containsAnyOf(String... expectedAlternatives) {
				return new StringContainsAnyOfAssertion(expectedAlternatives, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * innehåller ingen av dessa strängar.
			 */
			public StringDontContainAnyOfAssertion dontContainAnyOf(String... unexpectedAlternatives) {
				return new StringDontContainAnyOfAssertion(unexpectedAlternatives, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * innehåller detta heltal någonstans.
			 */
			public StringContainsAssertion contains(int expected) {
				return new StringContainsAssertion("" + expected, actual, NO_OP_TRANSFORMER);
			}

			/**
			 * innehåller detta flyttal någonstans.
			 */
			public StringContainsAnyOfAssertion contains(double expected) {
				String expectedLocalFormat = "" + expected;
				String expectedSwedishFormat = expectedLocalFormat.replace('.', ',');
				String expectedUSFormat = expectedLocalFormat.replace(',', '.');

				String[] expectedAlternatives = { expectedSwedishFormat, expectedUSFormat };

				return new StringContainsAnyOfAssertion(expectedAlternatives, actual, NO_OP_TRANSFORMER);
			}

			protected static record StringIsAssertion(String expected, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {

				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringIsAssertion ignoringCase() {
					return new StringIsAssertion(expected, actual, TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					if (!Objects.equals(transformer.apply(expected), transformer.apply(actual)))
						assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
				}

			}

			protected static record StringIsAnyOfAssertion(String[] expectedAlternatives, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {
				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringIsAnyOfAssertion ignoringCase() {
					return new StringIsAnyOfAssertion(expectedAlternatives, actual, TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					for (var expected : expectedAlternatives) {
						if (Objects.equals(transformer.apply(expected), transformer.apply(actual)))
							return;
					}

					var items = Arrays.stream(expectedAlternatives).map(alt -> '"' + alt + '"').toList();
					var expected = String.join(", ", items.subList(0, items.size() - 1)) + " eller " + items.getLast();

					assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
				}

			}

			protected static record StringContainsAssertion(String expected, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {

				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringContainsAssertion ignoringCase() {
					return new StringContainsAssertion(expected, actual, TO_UPPER_TRANSFORMER);
				}

				/**
				 * Kontrollera att strängen bara finns en gång.
				 */
				public StringContainsOnceAssertion onlyOnce() {
					return new StringContainsOnceAssertion(expected, actual, transformer);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					if (!transformer.apply(actual).contains(transformer.apply(expected)))
						assertionFailure().message(errorMsg)
								.expected("En sträng innehållandes \"%s\"".formatted(expected)).actual(actual)
								.buildAndThrow();
				}
			}

			protected static record StringDontContainsAssertion(String expected, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {

				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringDontContainsAssertion ignoringCase() {
					return new StringDontContainsAssertion(expected, actual, TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					if (transformer.apply(actual).contains(transformer.apply(expected)))
						assertionFailure().message(errorMsg)
								.expected("En sträng innehållandes \"%s\"".formatted(expected)).actual(actual)
								.buildAndThrow();
				}
			}

			protected static record StringContainsOnceAssertion(String expected, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {

				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringContainsOnceAssertion ignoringCase() {
					return new StringContainsOnceAssertion(expected, actual, TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					Pattern pattern = Pattern.compile(transformer.apply(expected));
					Matcher matcher = pattern.matcher(transformer.apply(actual));
					if (matcher.results().count() != 1)
						assertionFailure().message(errorMsg)
								.expected("En sträng innehållandes \"%s\" exakt en gång".formatted(expected))
								.actual(actual).buildAndThrow();
				}
			}

			protected static record StringContainsAllOfAssertionStep(String[] expectedStrings, String actual,
					Function<String, String> transformer) {

				public StringContainsAllOfInOrderAssertion inThatOrder() {
					return new StringContainsAllOfInOrderAssertion(expectedStrings, actual, transformer);
				}

				public StringContainsAllOfIgnoringOrderAssertion inAnyOrder() {
					return new StringContainsAllOfIgnoringOrderAssertion(expectedStrings, actual, transformer);
				}

			}

			protected static record StringContainsAllOfInOrderAssertion(String[] expectedStrings, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {
				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringContainsAllOfInOrderAssertion ignoringCase() {
					return new StringContainsAllOfInOrderAssertion(expectedStrings, actual, TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					String transformedActual = transformer.apply(actual);
					int lastIdx = -1;
					for (var expected : expectedStrings) {
						int idx = transformedActual.indexOf(transformer.apply(expected));
						if (idx <= lastIdx) {
							var items = Arrays.stream(expectedStrings).map(alt -> '"' + alt + '"').toList();
							expected = "En sträng innehållandes "
									+ String.join(", ", items.subList(0, items.size() - 1)) + " och " + items.getLast()
									+ " i denna ordning";

							assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
						}
						lastIdx = idx;
					}
				}
			}

			protected static record StringContainsAllOfIgnoringOrderAssertion(String[] expectedStrings, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {
				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringContainsAllOfIgnoringOrderAssertion ignoringCase() {
					return new StringContainsAllOfIgnoringOrderAssertion(expectedStrings, actual, TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					String transformedActual = transformer.apply(actual);
					for (var expected : expectedStrings) {
						if (!transformedActual.contains(transformer.apply(expected))) {
							var items = Arrays.stream(expectedStrings).map(alt -> '"' + alt + '"').toList();
							expected = "En sträng innehållandes "
									+ String.join(", ", items.subList(0, items.size() - 1)) + " och " + items.getLast();

							assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
						}
					}
				}
			}

			protected static record StringContainsAnyOfAssertion(String[] expectedAlternatives, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {

				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringContainsAnyOfAssertion ignoringCase() {
					return new StringContainsAnyOfAssertion(expectedAlternatives, actual, TO_UPPER_TRANSFORMER);
				}

				public StringContainsAnyOfCountingAssertion exactlyThisNumberOfTimes(int expectedTimes) {
					return new StringContainsAnyOfCountingAssertion(expectedAlternatives, expectedTimes, actual,
							TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					for (var expected : expectedAlternatives) {
						if (transformer.apply(actual).contains(transformer.apply(expected)))
							return;
					}

					var items = Arrays.stream(expectedAlternatives).map(alt -> '"' + alt + '"').toList();
					var expected = "En sträng innehållandes något av "
							+ String.join(", ", items.subList(0, items.size() - 1)) + " eller " + items.getLast();

					assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
				}

			}

			protected static record StringContainsAnyOfCountingAssertion(String[] expectedAlternatives,
					int expectedTimes, String actual, Function<String, String> transformer)
					implements DogRegisterAssertion {

				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringContainsAnyOfCountingAssertion ignoringCase() {
					return new StringContainsAnyOfCountingAssertion(expectedAlternatives, expectedTimes, actual,
							TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					String regex = Arrays.stream(expectedAlternatives).map(transformer::apply)
							.collect(Collectors.joining("|"));
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(transformer.apply(actual));

					assertEquals(expectedTimes, matcher.results().count(), errorMsg);
				}

			}

			protected static record StringDontContainAnyOfAssertion(String[] unexpectedAlternatives, String actual,
					Function<String, String> transformer) implements DogRegisterAssertion {

				/**
				 * Genomför kontrollen utan hänsyn till stora och små bokstäver. ABC kommer
				 * alltså att anses som samma sak som abc.
				 */
				public StringDontContainAnyOfAssertion ignoringCase() {
					return new StringDontContainAnyOfAssertion(unexpectedAlternatives, actual, TO_UPPER_TRANSFORMER);
				}

				@Override
				public void onErrorReport(String errorMsg) {
					for (var expected : unexpectedAlternatives) {
						if (transformer.apply(actual).contains(transformer.apply(expected))) {
							var items = Arrays.stream(unexpectedAlternatives).map(alt -> '"' + alt + '"').toList();
							var expectedAsString = "En sträng som inte innehåller något av "
									+ String.join(", ", items.subList(0, items.size() - 1)) + " eller "
									+ items.getLast();

							assertionFailure().message(errorMsg).expected(expectedAsString).actual(actual)
									.buildAndThrow();
						}
					}
				}
			}
		}

		protected static record ObjectValue(Object actual) {

			/**
			 * har detta värde.
			 */
			public ObjectIsAssertion is(Object expected) {
				return new ObjectIsAssertion(expected, actual);
			}

			/**
			 * är null.
			 */
			public ObjectIsNullAssertion isNull() {
				return new ObjectIsNullAssertion(actual);
			}

			/**
			 * inte är null.
			 */
			public ObjectIsNotNullAssertion isNotNull() {
				return new ObjectIsNotNullAssertion(actual);
			}

			/**
			 * innehåller något av de givna värdena.
			 */
			public ObjectIsAnyOfAssertion isAnyOf(Object... expectedAlternatives) {
				return new ObjectIsAnyOfAssertion(expectedAlternatives, actual);
			}

			protected static record ObjectIsNullAssertion(Object actual) implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					if (actual != null)
						assertionFailure().message(errorMsg).expected(null).actual(actual).buildAndThrow();
				}

			}

			protected static record ObjectIsNotNullAssertion(Object actual) implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					if (actual == null)
						assertionFailure().message(errorMsg).expected("Ej null").actual("null").buildAndThrow();
				}

			}

			protected static record ObjectIsAssertion(Object expected, Object actual) implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					assertEquals(expected, actual, errorMsg);
				}

			}

			protected static record ObjectIsAnyOfAssertion(Object[] expectedAlternatives, Object actual)
					implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					for (var expected : expectedAlternatives) {
						if (Objects.equals(expected, actual))
							return;
					}

					var items = Arrays.stream(expectedAlternatives).map(alt -> '"' + alt.toString() + '"').toList();
					var expected = String.join(", ", items.subList(0, items.size() - 1)) + " eller " + items.getLast();

					assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
				}

			}
		}

		protected static record ArrayValue<T>(T[] actual) {

			/**
			 * ... är tom.
			 */
			public final ArrayIsInOrderAssertion<T> isEmpty() {
				return contains().inThatOrder();
			}

			/**
			 * ... innehåller bara detta värde.
			 */
			@SuppressWarnings("unchecked") // Att dölja varningar är normalt en *MYCKET* dålig idé. Här är det dock
											// nödvändigt på grund av hur Javas generiska typer fungerar för arrayer.
			public final ArrayIsInOrderAssertion<T> containsOnly(T expected) {
				return new ArrayIsInOrderAssertion<T>((T[]) new Object[] { expected }, actual);
			}

			/**
			 * ... innehåller dessa värden.
			 */
			@SafeVarargs
			public final ArrayContainsAssertionStep<T> contains(T... expected) {
				return new ArrayContainsAssertionStep<T>(expected, actual);
			}

			protected static record ArrayContainsAssertionStep<T>(T[] expected, T[] actual) {
				/**
				 * Ordningen på elementen är viktig. [a, b] är alltså inte samma som [b, a].
				 */
				public ArrayIsInOrderAssertion<T> inThatOrder() {
					return new ArrayIsInOrderAssertion<T>(expected, actual);
				}

				/**
				 * Gör jämförelsen utan hänsyn till ordningen på elementen i listan. [a, b] är
				 * alltså samma som [b, a].
				 */
				public ArrayContainsIgnoringOrderAssertion<T> inAnyOrder() {
					return new ArrayContainsIgnoringOrderAssertion<T>(expected, actual);
				}
			}

			protected static record ArrayIsInOrderAssertion<T>(T[] expected, T[] actual)
					implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					if (!Arrays.equals(expected, actual))
						assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
				}
			}

			protected static record ArrayContainsIgnoringOrderAssertion<T>(T[] expected, T[] actual)
					implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					assertEquals(expected.getClass(), actual.getClass(),
							"Arrayerna är inte av samma typ, och kan därför inte jämföras");

					var expectedAsList = Arrays.asList(expected);
					var actualAsList = Arrays.asList(actual);

					//  Detta villkor hanterar inte dubbletter, och skulle anse [A, A, B] vara
					// samma som [A, B, B]. Dubbletter förekommer inte i testfallen, men det bör
					// hanteras så att vi inte får problem senare.
					boolean same = expected.length == actual.length && expectedAsList.containsAll(actualAsList)
							&& actualAsList.containsAll(expectedAsList);

					if (!same)
						assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
				}
			}
		}

		protected static record ListValue<T>(List<T> actual) {

			/**
			 * ... är tom.
			 */
			public final ListContainsInOrderAssertion<T> isEmpty() {
				return new ListContainsInOrderAssertion<T>(Arrays.asList(), actual);
			}

			/**
			 * ... innehåller bara detta värde.
			 */
			public final ListContainsInOrderAssertion<T> containsOnly(T expected) {
				return new ListContainsInOrderAssertion<T>(Arrays.asList(expected), actual);
			}

			/**
			 * ... innehåller dessa värden.
			 */
			@SafeVarargs
			public final ListContainsAssertionStep<T> contains(T... expected) {
				return new ListContainsAssertionStep<T>(Arrays.asList(expected), actual);
			}

			protected static record ListContainsAssertionStep<T>(List<T> expected, List<T> actual) {

				/**
				 * Ordningen på elementen är viktig. [a, b] är alltså inte samma som [b, a].
				 */
				public ListContainsInOrderAssertion<T> inThatOrder() {
					return new ListContainsInOrderAssertion<T>(expected, actual);
				}

				/**
				 * Gör jämförelsen utan hänsyn till ordningen på elementen i listan. [a, b] är
				 * alltså samma som [b, a].
				 */
				public ListContainsIgnoringOrderAssertion<T> inAnyOrder() {
					return new ListContainsIgnoringOrderAssertion<T>(expected, actual);
				}

			}

			protected static record ListContainsInOrderAssertion<T>(List<T> expected, List<T> actual)
					implements DogRegisterAssertion {
				@Override
				public void onErrorReport(String errorMsg) {
					assertEquals(expected, actual, errorMsg);
				}
			}

			protected static record ListContainsIgnoringOrderAssertion<T>(List<T> expected, List<T> actual)
					implements DogRegisterAssertion {

				@Override
				public void onErrorReport(String errorMsg) {
					//  Detta villkor hanterar inte dubbletter, och skulle anse [A, A, B] vara
					// samma som [A, B, B]. Dubbletter förekommer inte i testfallen, men det bör
					// hanteras så att vi inte får problem senare.
					boolean same = expected.size() == actual.size() && expected.containsAll(actual)
							&& actual.containsAll(expected);

					if (!same)
						assertionFailure().message(errorMsg).expected(expected).actual(actual).buildAndThrow();
				}
			}
		}

	}

	private static class Style {

		public static void assertOnlyAllowedAccessModifiersUsed(Class<?> cut) {
			if(cut.getName().contains("$"))
				return;
			
			List<Executable> checks = new ArrayList<>();

			checks.add(new ClassAccessLevelCheck(cut));
			for (var field : cut.getDeclaredFields()) {
				if (field.getName().contains("$")) {
					continue;
				}

				if (cut.isEnum())
					checks.add(new PublicConstantsAllowedMemberAccessLevelCheck(field));
				else
					checks.add(new PrivateMemberAccessLevelCheck(field));
			}
			for (var constructor : cut.getDeclaredConstructors()) {
				if (constructor.getName().contains("$")) {
					continue;
				}
				checks.add(new PublicOrPrivateMemberAccessLevelCheck(constructor));
			}
			for (var method : cut.getDeclaredMethods()) {
				if (method.getName().contains("$")) {
					continue;
				}
				checks.add(new PublicOrPrivateMemberAccessLevelCheck(method));
			}

			assertAll("Fel skyddsnivå", checks);
		}

		public static void assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(Class<?> cut) {
			List<Executable> checks = new ArrayList<>();

			for (var field : cut.getDeclaredFields()) {
				if (field.getName().contains("$")) {
					continue;
				}

				checks.add(new NamePatternCheck(field, isConstant(field) ? "konstanter" : "variabler",
						isConstant(field) ? CONSTANT_NAMING_PATTERN : VARIABLE_AND_METHOD_NAMING_PATTERN));
			}
			for (var constructor : cut.getDeclaredConstructors()) {
				if (constructor.getName().contains("$")) {
					continue;
				}

				checks.add(new NamePatternCheck(constructor, "konstruktorer", CLASS_NAMING_PATTERN));
			}
			for (var method : cut.getDeclaredMethods()) {
				if (method.getName().contains("$")) {
					continue;
				}

				checks.add(new NamePatternCheck(method, "metoder", VARIABLE_AND_METHOD_NAMING_PATTERN));
			}

			assertAll("Namngivningskonventioner", checks);
		}

		private static interface AccessLevelCheck {

			default void assertIsPublicOrPrivate(String signature, int modifiers) {
				if (!(Modifier.isPublic(modifiers) || Modifier.isPrivate(modifiers)))
					fail("%s är inte public eller private som förväntat".formatted(signature));
			}

			default void assertIsPrivate(String signature, int modifiers) {
				if (!(Modifier.isPrivate(modifiers)))
					fail("%s är inte private som förväntat".formatted(signature));
			}
		}

		private static record ClassAccessLevelCheck(Class<?> cut) implements Executable, AccessLevelCheck {

			@Override
			public void execute() throws Throwable {
				assertIsPublicOrPrivate(cut.getName(), cut.getModifiers());
			}

		}

		private static record PublicConstantsAllowedMemberAccessLevelCheck(Member member)
				implements Executable, AccessLevelCheck {

			@Override
			public void execute() throws Throwable {
				if (isConstant())
					assertIsPublicOrPrivate(member.toString(), member.getModifiers());
				else
					assertIsPrivate(member.toString(), member.getModifiers());
			}

			private boolean isConstant() {
				return Modifier.isStatic(member.getModifiers()) && Modifier.isFinal(member.getModifiers());
			}

		}

		private static record PrivateMemberAccessLevelCheck(Member member) implements Executable, AccessLevelCheck {

			@Override
			public void execute() throws Throwable {
				assertIsPrivate(member.toString(), member.getModifiers());
			}

		}

		private static record PublicOrPrivateMemberAccessLevelCheck(Member member)
				implements Executable, AccessLevelCheck {

			@Override
			public void execute() throws Throwable {
				assertIsPublicOrPrivate(member.toString(), member.getModifiers());
			}

		}

		private static boolean isConstant(Field field) {
			return Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers());
		}

		private static record NamePatternCheck(Member member, String memberTypeName, Pattern expected)
				implements Executable {

			@Override
			public void execute() throws Throwable {
				var matcher = expected.matcher(member.getName());
				if (!matcher.matches())
					fail("Formatet för %s verkar inte följa namngivningskonventionerna för %s."
							.formatted(member.getName(), memberTypeName));
			}

		}

	}

	/**
	 * Denna klass representerar en publik metod som det av någon anledning inte är
	 * säkert att den existerar. Det kan vara en obligatorisk metod som man ska
	 * designa eller namnge själv, eller en metod som är tillåten men inte
	 * obligatorisk.
	 */
	protected static class PossiblePublicMethod {
		private Method method;
		private String errorMsg;

		public PossiblePublicMethod(Method method) {
			this.method = Objects.requireNonNull(method);
		}

		public PossiblePublicMethod(String context, Class<?> clazz, Predicate<Method> candidateIdentifier) {
			List<Method> candidates = new ArrayList<>();

			for (Method method : clazz.getDeclaredMethods()) {
				if (Modifier.isPublic(method.getModifiers()) && candidateIdentifier.test(method)) {
					candidates.add(method);
				}
			}

			switch (candidates.size()) {
			case 1:
				method = candidates.getFirst();
				break;
			case 0:
				errorMsg = "Kunde inte identifiera metoden som ska användas för att " + context
						+ ". Hittar ingen publik metod som verkar matcha";
				break;
			default:
				errorMsg = "Kunde inte identifiera metoden som ska användas för att " + context
						+ ". Hittar flera publika metoder som verkar matcha: "
						+ candidates.stream().map(Method::getName).toList();
			}
		}

		public int parameterCount() {
			return method == null ? 0 : method.getParameterCount();
		}

		public Object invokeAndFailIfNotPresent(Object object, Object... args) {
			if (method == null)
				fail(errorMsg);

			try {
				return method.invoke(object, args);
			} catch (IllegalAccessException e) {
				failTest("Kunde inte komma åt metoden " + method + ": " + e);
				return null;
			} catch (InvocationTargetException e) {
				failTest("Metoden " + method + " kan inte anropas på " + object + ": " + e.getCause());
				return null;
			}
		}

		public Object invokeAndIgnoreIfNotPresent(Object object, Object... args) {
			if (method == null)
				return null;

			try {
				return method.invoke(object, args);
			} catch (IllegalAccessException e) {
				failTest("Kunde inte komma åt metoden " + method + ": " + e);
				return null;
			} catch (InvocationTargetException e) {
				failTest("Metoden " + method + " kan inte anropas på " + object + ": " + e.getCause());
				return null;
			}
		}

		@Override
		public String toString() {
			return method == null ? errorMsg : method.toString();
		}
	}

	/**
	 * Denna klass representerar ett fält/attribut som av någon anledning inte
	 * behöver finnas (ännu).
	 */
	public static class PossibleField {
		private Class<?> clazz;
		private Field field;
		private String errorMsg;

		public PossibleField(String context, String className, Class<?> type, boolean isStatic) {
			this(context, getClassIfAvailable(className), type, isStatic);
			if (clazz == null) {
				errorMsg = "Klassen %s finns inte".formatted(className);
			}
		}

		private static Class<?> getClassIfAvailable(String className) {
			try {
				String packageName = DR01Assertions.class.getPackageName();
				className = packageName.isEmpty() ? className : packageName + "." + className;
				return DR01Assertions.class.getClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}

		public PossibleField(String context, Class<?> clazz, Class<?> type, boolean isStatic) {
			if (clazz == null) {
				errorMsg = "Ingen klass angiven, om du ser detta är det ett fel i testen, inte i din kod";
				return;
			}

			this.clazz = clazz;

			List<Field> candidates = new ArrayList<>();

			for (Field candidate : clazz.getDeclaredFields()) {
				if (candidate.getType() == boolean.class && Modifier.isStatic(candidate.getModifiers())) {
					candidates.add(candidate);
				}
			}

			if (candidates.isEmpty()) {
				errorMsg = "Kunde inte identifiera vilket fält (attribut) i klassen %s som %s"
						.formatted(clazz.getName(), context);
				return;
			}

			if (candidates.size() > 1) {
				errorMsg = "Kunde inte identifiera vilket fält (attribut) %s i klassen %s som %s".formatted(candidates,
						clazz.getName(), context);
				return;
			}

			field = candidates.getFirst();
			field.setAccessible(true);
		}

		public boolean classFound() {
			return clazz != null;
		}

		/**
		 * För statiska fält
		 */
		public Object getAndFailIfNotPresent() {
			if (clazz == null || field == null)
				failTest(errorMsg);

			try {
				return field.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
				return null; // Kan inte hända på grund av fail ovanför, men kompilatorn kräver det.
			}
		}

		/**
		 * För statiska fält
		 */
		public Object getAndIgnoreIfNotPresent(Object defaultValue) {
			if (field == null)
				return defaultValue;

			try {
				return field.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
				return null; // Kan inte hända på grund av fail ovanför, men kompilatorn kräver det.
			}
		}

		public Object getAndFailIfNotPresent(Object instance) {
			if (clazz == null || field == null)
				failTest(errorMsg);

			try {
				return field.get(instance);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
				return null; // Kan inte hända på grund av fail ovanför, men kompilatorn kräver det.
			}
		}

		public Object getAndIgnoreIfNotPresent(Object instance, Object defaultValue) {
			if (field == null)
				return defaultValue;

			try {
				return field.get(instance);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
				return null; // Kan inte hända på grund av fail ovanför, men kompilatorn kräver det.
			}
		}

		/**
		 * För statiska fält
		 */
		public void setAndFailIfNotPresent(Object value) {
			if (clazz == null || field == null)
				failTest(errorMsg);

			try {
				field.set(null, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
			}
		}

		/**
		 * För statiska fält
		 */
		public void setAndIgnoreIfNotPresent(Object value) {
			if (field == null)
				return;

			try {
				field.set(null, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
			}
		}

		public void setAndFailIfNotPresent(Object instance, Object value) {
			if (clazz == null || field == null)
				failTest(errorMsg);

			try {
				field.set(instance, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
			}
		}

		public void setAndIgnoreIfNotPresent(Object instance, Object value) {
			if (field == null)
				return;

			try {
				field.set(instance, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fail(e);
			}
		}

		@Override
		public String toString() {
			return field != null ? field.toString() : errorMsg;
		}

	}

}

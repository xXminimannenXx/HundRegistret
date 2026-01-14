import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Obs! Eftersom dessa test kommunicerar med "användaren" via
 * <code>System.in</code> och <code>System.out</code> så fungerar det
 * <b><u>inte</u></b> med spårutskrifter. Det går att kringå detta genom att
 * skriva ut på <code>System.err</code> istället för <code>System.in</code>, men
 * det är bättre att testa manuellt istället om du har behov av mer kontroll.
 * <p>
 * Du kommer inte heller att se <b><u>något</u></b> av vad din inläsningsklass
 * skriver ut annat än i eventuella felmeddelanden. Detta går inte att kringå på
 * annat sätt än att skriv om testen.
 * 
 * @author Henrik Bergström
 * @version 1.0
 * @apiNote Testen i denna klass är stabila, och det finns i skrivandets stund
 *          inga kända saker som behöver kompletteras.
 */
@DisplayName("DR14: Grundläggande test för inläsningsklassen")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR14InputReaderTest extends DR01Assertions {

	private static final Class<?> CLASS_UNDER_TEST = InputReader.class;

	private static final PossiblePublicMethod INTEGER_NUMBER_METHOD = new PossiblePublicMethod("läsa in ett heltal",
			InputReader.class, DR14InputReaderTest::possibleMethodToReadIntegerInput);
	private static final PossiblePublicMethod DECIMAL_NUMBER_METHOD = new PossiblePublicMethod("läsa in ett decimaltal",
			InputReader.class, DR14InputReaderTest::possibleMethodToReadDecimalInput);
	private static final PossiblePublicMethod TEXT_STRING_METHOD = new PossiblePublicMethod("läsa in text",
			InputReader.class, DR14InputReaderTest::possibleMethodToReadTextInput);;
	private static final PossiblePublicMethod POSSIBLE_CLOSE_METHOD = new PossiblePublicMethod(
			"stänga scannern i inläsningsklassen", InputReader.class,
			DR14InputReaderTest::possibleMethodToCloseReader);;

	private static final String DEFAULT_PROMPT = "PROMPT";
	private static final String INPUT_EXPECTED_INDICATOR = "?>";
	private static final String SWEDISH_ERROR_INDICATOR = "FEL";
	private static final String ENGLISH_ERROR_INDICATOR = "ERROR";

	private static final InputStream ORIGINAL_SYSTEM_IN = System.in;
	private static final PrintStream ORIGINAL_SYSTEM_OUT = System.out;

	private InputReader reader;
	private ByteArrayOutputStream systemOut;

	private static boolean possibleMethodToReadIntegerInput(Method method) {
		return (method.getReturnType() == int.class || method.getReturnType() == long.class)
				&& method.getParameterCount() == 1 && method.getParameterTypes()[0] == String.class;
	}

	private static boolean possibleMethodToReadDecimalInput(Method method) {
		return method.getReturnType() == double.class && method.getParameterCount() == 1
				&& method.getParameterTypes()[0] == String.class;
	}

	private static boolean possibleMethodToReadTextInput(Method method) {
		return method.getReturnType() == String.class && method.getParameterCount() == 1
				&& method.getParameterTypes()[0] == String.class;
	}

	private static boolean possibleMethodToCloseReader(Method method) {
		return method.getParameterCount() == 0;
	}

	@AfterAll
	public static void restoreSystemInAndOut() {
		System.setIn(ORIGINAL_SYSTEM_IN);
		System.setOut(ORIGINAL_SYSTEM_OUT);
	}

	@BeforeEach
	public void resetMultipleInstanceGuardIfPresent() {
		MULTIPLE_INSTANCE_GUARD.setAndIgnoreIfNotPresent(ORIGINAL_MULTIPLE_INSTANCE_GUARD_VALUE);
	}

	@BeforeEach
	public void replaceSystemOut() {
		systemOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(systemOut));
	}

	@AfterEach
	public void closeReader() {
		if (reader != null)
			POSSIBLE_CLOSE_METHOD.invokeAndIgnoreIfNotPresent(reader);
	}

	private void assertInputExpected() {
		String actual = systemOut.toString().trim();
		if (!actual.endsWith(INPUT_EXPECTED_INDICATOR))
			failTest("Ledtexten till användaren: \"%s\" avslutas inte med \"?>\" som förväntat", actual);
	}

	private void assertExpectedPromptPrinted(String prompt) {
		String actual = systemOut.toString().trim();
		if (!actual.startsWith(prompt))
			failTest("Ledtexten till användaren: \"%s\" inleds inte med \"%s\" som förväntat", actual, prompt);
		assertInputExpected();
	}

	private void assertNoErrorMessagePrinted() {
		String actual = systemOut.toString().trim();
		assertThat(actual).dontContainAnyOf(SWEDISH_ERROR_INDICATOR, ENGLISH_ERROR_INDICATOR).ignoringCase()
				.onErrorReport(
						"Ledtexten till användaren innehåller något av orden \"fel\" eller \"error\" som inte förväntades");

	}

	private void assertErrorMessagesPrinted(int expectedNoOfErrors) {
		String actual = systemOut.toString().trim();
		assertThat(actual).containsAnyOf(SWEDISH_ERROR_INDICATOR, ENGLISH_ERROR_INDICATOR)
				.exactlyThisNumberOfTimes(expectedNoOfErrors).ignoringCase()
				.onErrorReport("Fel antal felindikatorer (\"fel\" eller \"error\") i ledtexten: \"%s\"", actual);
	}

	private void readExpectedIntegerNumber(long expected) {
		readExpectedIntegerNumber(DEFAULT_PROMPT, expected);
	}

	private void readExpectedIntegerNumber(String prompt, long expected) {
		replaceSystemOut();

		Number actual = (Number) INTEGER_NUMBER_METHOD.invokeAndFailIfNotPresent(reader, prompt);

		assertThat(actual.longValue()).is(expected).onErrorReport("Fel heltal inläst");
		assertExpectedPromptPrinted(prompt);
		assertNoErrorMessagePrinted();
	}

	private void readNegativeIntegerNumbersFollowedByPositive(int expectedNoOfErrors, long expected) {
		readNegativeIntegerNumbersFollowedByPositive(DEFAULT_PROMPT, expectedNoOfErrors, expected);
	}

	private void readNegativeIntegerNumbersFollowedByPositive(String prompt, int expectedNoOfErrors, long expected) {
		replaceSystemOut();

		Number actual = (Number) INTEGER_NUMBER_METHOD.invokeAndFailIfNotPresent(reader, prompt);

		assertThat(actual.longValue()).is(expected).onErrorReport("Fel heltal inläst");
		assertErrorMessagesPrinted(expectedNoOfErrors);
	}

	private void readExpectedDecimalNumber(double expected) {
		readExpectedDecimalNumber(DEFAULT_PROMPT, expected);
	}

	private void readExpectedDecimalNumber(String prompt, double expected) {
		replaceSystemOut();

		double actual = (double) DECIMAL_NUMBER_METHOD.invokeAndFailIfNotPresent(reader, prompt);

		assertThat(actual).is(expected).withStrictTolerance().onErrorReport("Fel decimaltal inläst");
		assertExpectedPromptPrinted(prompt);
		assertNoErrorMessagePrinted();
	}

	private void readNegativeDecimalNumbersFollowedByPositive(int expectedNoOfErrors, double expected) {
		readNegativeDecimalNumbersFollowedByPositive(DEFAULT_PROMPT, expectedNoOfErrors, expected);
	}

	private void readNegativeDecimalNumbersFollowedByPositive(String prompt, int expectedNoOfErrors, double expected) {
		replaceSystemOut();

		double actual = (double) DECIMAL_NUMBER_METHOD.invokeAndFailIfNotPresent(reader, prompt);

		assertThat(actual).is(expected).withStrictTolerance().onErrorReport("Fel decimaltal inläst");
		assertErrorMessagesPrinted(expectedNoOfErrors);
	}

	private void readExpectedTextString(String expected) {
		readExpectedTextString(DEFAULT_PROMPT, expected);
	}

	private void readExpectedTextString(String prompt, String expected) {
		replaceSystemOut();

		String actual = (String) TEXT_STRING_METHOD.invokeAndFailIfNotPresent(reader, prompt);

		assertThat(actual).is(expected).onErrorReport("Fel textsträng inläst");
		assertExpectedPromptPrinted(prompt);
		assertNoErrorMessagePrinted();
	}

	private void setupReader(String... inputLines) {
		String input = Arrays.stream(inputLines).collect(Collectors.joining("\n", "", "\n"));
		reader = new InputReader(new Scanner(input));
	}

	private void setupReader(int... inputLines) {
		String input = Arrays.stream(inputLines).mapToObj(Integer::toString)
				.collect(Collectors.joining("\n", "", "\n"));
		reader = new InputReader(new Scanner(input));
	}

	private void setupReader(double... inputLines) {
		DecimalFormat decimalFormat = new DecimalFormat("#.###########");
		String input = Arrays.stream(inputLines).mapToObj(decimalFormat::format)
				.collect(Collectors.joining("\n", "", "\n"));
		reader = new InputReader(new Scanner(input));
	}

	@Order(10)
	@Test
	@DisplayName("Grundläggande krav för skyddsnivåer följs i klassen InputReader")
	public void basicStyleRulesForAccessModifiersFollowed() {
		assertOnlyAllowedAccessModifiersUsed(CLASS_UNDER_TEST);
	}

	@Order(20)
	@Test
	@DisplayName("Grundläggande regler för hur namn formateras följs på klassnivå i klassen InputReader")
	public void basicStyleRulesForNamesAtClassLevelFollowed() {
		assertNamesAtClassLevelLooksLikeTheyFollowJavaNamingConventions(CLASS_UNDER_TEST);
	}

	@Order(30)
	@Test
	@DisplayName("Inläsning från System.in via default-konstruktorn")
	public void readingFromSystemInUsingTheDefaulConstructor() {
		assertTestTimesOut(() -> {
			InputStream in = new ByteArrayInputStream("1\n".getBytes());
			System.setIn(in);
			reader = new InputReader();
			readExpectedIntegerNumber(1);
		});
	}

	@Order(40)
	@ParameterizedTest
	@CsvSource({ "ABC", "DEF", "GHI JKL" })
	@DisplayName("Inläsning av en rad text")
	public void readsSingleLineOfText(String expected) {
		assertTestTimesOut(() -> {
			setupReader(expected);
			readExpectedTextString(expected);
		});
	}

	@Order(50)
	@ParameterizedTest
	@CsvSource({ "1", "22", "333", "0" })
	@DisplayName("Inläsning av ett heltal")
	public void readsSingleIntegerNumber(int expected) {
		assertTestTimesOut(() -> {
			setupReader(expected);
			readExpectedIntegerNumber(expected);
		});
	}

	@Order(60)
	@ParameterizedTest
	@CsvSource({ "1.1", "22.22", "333.333", "0.0", "4444" })
	@DisplayName("Inläsning av ett decimaltal")
	public void readsSingleDecimalNumber(double expected) {
		assertTestTimesOut(() -> {
			setupReader(expected);
			readExpectedDecimalNumber(expected);
		});
	}

	@Order(70)
	@ParameterizedTest
	@CsvSource({ "Tal", "Skriv något" })
	@DisplayName("Inläsning av ett heltal")
	public void readsSingleIntegerNumber(String expected) {
		assertTestTimesOut(() -> {
			setupReader(1);
			readExpectedIntegerNumber(expected, 1);
		});
	}

	@Order(80)
	@Test
	@DisplayName("Inläsning av flera rader text")
	public void readsMultipleLinesOfText() {
		assertTestTimesOut(() -> {
			setupReader("Första raden", "Andra raden", "Tredje raden");

			readExpectedTextString("Första raden");
			readExpectedTextString("Andra raden");
			readExpectedTextString("Tredje raden");
		});
	}

	@Order(90)
	@Test
	@DisplayName("Inläsning av flera heltal")
	public void readsMultipleIntegerNumbers() {
		assertTestTimesOut(() -> {
			setupReader(1, 22, 333);

			readExpectedIntegerNumber(1);
			readExpectedIntegerNumber(22);
			readExpectedIntegerNumber(333);
		});
	}

	@Order(100)
	@Test
	@DisplayName("Inläsning av flera decimaltal")
	public void readsMultipleDecimalNumbers() {
		assertTestTimesOut(() -> {
			setupReader(1.1, 22.22, 333.333);

			readExpectedDecimalNumber(1.1);
			readExpectedDecimalNumber(22.22);
			readExpectedDecimalNumber(333.333);
		});
	}

	@Order(110)
	@ParameterizedTest
	@CsvSource({ "en, US", "de, DE" })
	@DisplayName("Kan läsa in decimaltal i det format som systemet är inställt på")
	public void readsDecimalNumbersInLocaleDependentFormat(String language, String country) {
		assertTestTimesOut(() -> {
			Locale.setDefault(Locale.of(language, country));
			setupReader(123.45);
			readExpectedDecimalNumber(123.45);
		});
	}

	@Order(120)
	@Test
	@DisplayName("Kan läsa tal och text blandat")
	public void readsMixedNumbersAndStrings() {
		DecimalFormat decimalFormat = new DecimalFormat("#.###########");

		assertTestTimesOut(() -> {
			setupReader("1", "ABC", decimalFormat.format(22.22), "DEF", "333");
			readExpectedIntegerNumber(1);
			readExpectedTextString("ABC");
			readExpectedDecimalNumber(22.22);
			readExpectedTextString("DEF");
			readExpectedIntegerNumber(333);
		});
	}

	@Order(130)
	@ParameterizedTest
	@CsvSource({ "' abc ', abc, abc", "abc  def, abc  def, abc def", "abc   def, abc   def, abc def" })
	@DisplayName("x")
	public void removesWhitespaceBeforeAndAfterAndOptionallyBetweenWords(String input, String expected,
			String expectedWithExtraWhitespaceRemovedBetweenWords) {
		assertTestTimesOut(() -> {
			setupReader(input);
			String actual = (String) TEXT_STRING_METHOD.invokeAndFailIfNotPresent(reader, DEFAULT_PROMPT);

			// Vill du låsa testet till att kontrollera att blanktecken tas bort mellan ord
			// så ta bort "expected" från isAnyOf nedan.
			assertThat(actual).isAnyOf(expected, expectedWithExtraWhitespaceRemovedBetweenWords)
					.onErrorReport("Fel textsträng inläst");
		});
	}

	@Order(140)
	@Test
	@DisplayName("Ger felmeddelande och ny chans till inmatning om ett negativt heltal läses in")
	public void canHandleNegativeIntegerNumbers() {
		assertTestTimesOut(() -> {
			setupReader(-1, -2, -3, 4);
			readNegativeIntegerNumbersFollowedByPositive(3, 4);
		});
	}

	@Order(150)
	@Test
	@DisplayName("Ger felmeddelande och ny chans till inmatning om ett negativt decimaltal läses in")
	public void canHandleNegativeDecimalNumbers() {
		assertTestTimesOut(() -> {
			setupReader(-1.0, -2, -3.33, 4.444);
			readNegativeDecimalNumbersFollowedByPositive(3, 4.444);
		});
	}

	@Order(160)
	@Test
	@DisplayName("Kan inte skapa mer än en instans av InputReader med default-konstruktorn")
	public void cantCreateMultipleInstancesWithDefaultCtr() {
		assertTestTimesOut(() -> {
			new InputReader();
			assertThrows(IllegalStateException.class, () -> {
				new InputReader(); // Detta försök ska misslyckas
			});
		});
	}

	@Order(170)
	@Test
	@DisplayName("Kan inte skapa mer än en instans av InputReader med konstruktorn som tar en Scanner")
	public void cantCreateMultipleInstancesWithScannerCtr() {
		assertTestTimesOut(() -> {
			new InputReader(new Scanner(""));
			assertThrows(IllegalStateException.class, () -> {
				new InputReader(new Scanner("")); // Detta försök ska misslyckas
			});
		});
	}

}

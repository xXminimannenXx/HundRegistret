import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

/**
 * @author Henrik Bergström
 * @version 0.9
 * @apiNote Testen i denna klass fungerar, men testar väldigt begränsade
 *          scenarier. De kommer att utökas när vi sett fler implementationer av
 *          de olika algoritmerna. Detta betyder att de troligen också kommer
 *          att uppdateras efter inlämningen i samband med kursslutet.
 */
@DisplayName("DR06: Grundläggande test av sorteringsalgoritmen")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DR06DogSorterTest extends DR01Assertions {

	private static final Class<?> CLASS_UNDER_TEST = DogSorter.class;
	private static final String DEFAULT_DOG_BREED = "Schnauzer";
	private static final Random RND = new Random();

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

	// Hjälpmetoder för att skapa arrayer av hundar
	private static Dog[] originalDogs(Dog... dogs) {
		return dogs;
	}

	private static Dog[] expectedDogs(Dog... dogs) {
		return dogs;
	}

	private static List<Arguments> smallSortingScenarios() {
		final Dog ASTRA_OLD_DOG = new Dog("Astra", DEFAULT_DOG_BREED, 10, 5);
		final Dog BALDER_MIDDLE_AGE_DOG = new Dog("Balder", DEFAULT_DOG_BREED, 5, 5);
		final Dog CLEO_YOUNG_DOG = new Dog("Cleo", DEFAULT_DOG_BREED, 1, 5);

		var scenarios = new ArrayList<Arguments>();

		var nameComparator = Comparator.comparing(Dog::getName);
		var ageComparator = Comparator.comparing(Dog::getAge);

		for (var algorithm : SortingAlgorithm.values()) {
			scenarios.add(Arguments.of(algorithm, nameComparator, originalDogs(), expectedDogs(), "tom array"));

			scenarios.add(Arguments.of(algorithm, nameComparator, originalDogs(ASTRA_OLD_DOG),
					expectedDogs(ASTRA_OLD_DOG), "array med en hund"));

			scenarios.add(Arguments.of(algorithm, nameComparator, originalDogs(BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG),
					expectedDogs(ASTRA_OLD_DOG, BALDER_MIDDLE_AGE_DOG),
					"array med två hundar som ska sorteras på namn"));
			scenarios.add(Arguments.of(algorithm, ageComparator, originalDogs(ASTRA_OLD_DOG, BALDER_MIDDLE_AGE_DOG),
					expectedDogs(BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG),
					"array med två hundar som ska sorteras på ålder"));

			scenarios.add(Arguments.of(algorithm, nameComparator,
					originalDogs(ASTRA_OLD_DOG, BALDER_MIDDLE_AGE_DOG, CLEO_YOUNG_DOG),
					expectedDogs(ASTRA_OLD_DOG, BALDER_MIDDLE_AGE_DOG, CLEO_YOUNG_DOG),
					"redan sorterad array med tre hundar som ska sorteras på namn"));
			scenarios.add(Arguments.of(algorithm, ageComparator,
					originalDogs(CLEO_YOUNG_DOG, BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG),
					expectedDogs(CLEO_YOUNG_DOG, BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG),
					"redan sorterad array med tre hundar som ska sorteras på ålder"));

			scenarios.add(Arguments.of(algorithm, nameComparator,
					originalDogs(CLEO_YOUNG_DOG, BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG),
					expectedDogs(ASTRA_OLD_DOG, BALDER_MIDDLE_AGE_DOG, CLEO_YOUNG_DOG),
					"omvänt sorterad array med tre hundar som ska sorteras på namn"));
			scenarios.add(Arguments.of(algorithm, ageComparator,
					originalDogs(ASTRA_OLD_DOG, BALDER_MIDDLE_AGE_DOG, CLEO_YOUNG_DOG),
					expectedDogs(CLEO_YOUNG_DOG, BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG),
					"omvänt sorterad array med tre hundar som ska sorteras på ålder"));

			scenarios.add(Arguments.of(algorithm, nameComparator,
					originalDogs(BALDER_MIDDLE_AGE_DOG, CLEO_YOUNG_DOG, ASTRA_OLD_DOG),
					expectedDogs(ASTRA_OLD_DOG, BALDER_MIDDLE_AGE_DOG, CLEO_YOUNG_DOG),
					"blandad array med tre hundar som ska sorteras på namn"));
			scenarios.add(Arguments.of(algorithm, ageComparator,
					originalDogs(BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG, CLEO_YOUNG_DOG),
					expectedDogs(CLEO_YOUNG_DOG, BALDER_MIDDLE_AGE_DOG, ASTRA_OLD_DOG),
					"blandad array med tre hundar som ska sorteras på ålder"));
		}

		return scenarios;
	}

	@Order(30)
	@ParameterizedTest(name = "{0} kan sortera en {4}")
	@DisplayName("Stödda sorteringsalgoritmer fungerar för små arrayer")
	@MethodSource("smallSortingScenarios")
	public void sorts(SortingAlgorithm algorithm, Comparator<Dog> comparator, Dog[] actual, Dog[] expected,
			String description) {
		assertTestTimesOut(() -> {
			DogSorter.sort(algorithm, comparator, actual);
			assertThat(actual).contains(expected).inThatOrder().onErrorReport("Arrayen är inte korrekt sorterad");
		});
	}

	private void randomize(Dog[] dogs) {
		for (int i = 0; i < dogs.length; i++) {
			int j = RND.nextInt(dogs.length);

			Dog temp = dogs[i];
			dogs[i] = dogs[j];
			dogs[j] = temp;
		}
	}

	private static List<Arguments> multipleIterationsOfAlgorithms() {
		var scenarios = new ArrayList<Arguments>();

		for (var algorithm : SortingAlgorithm.values()) {
			for (int i = 1; i <= 5; i++) {
				scenarios.add(Arguments.of(algorithm, i));
			}
		}

		return scenarios;
	}

	@Order(40)
	@ParameterizedTest(name="{0} försök {1}")
	@MethodSource("multipleIterationsOfAlgorithms")
	@DisplayName("Fler hundar i slumpmässig ordning sorteras på namn")
	public void moreDogsInRandomOrder(SortingAlgorithm algorithm, int iteration) {
		assertTestTimesOut(() -> {
			Dog bamse = new Dog("Bamse", "Basenji", 5, 2);
			Dog bob = new Dog("Bob", "Terrier", 1, 3);
			Dog bobby = new Dog("Bobby", "Bulldogg", 4, 9);
			Dog bonnie = new Dog("Bonnie", "Pumi", 6, 1);
			Dog bosse = new Dog("Bosse", "Whippet", 4, 6);
			Dog buster = new Dog("Buster", "Chihuahua", 7, 9);
			Dog charlie = new Dog("Charlie", "Border collie", 8, 2);
			Dog daisy = new Dog("Daisy", "Staffordshire bullterrier", 6, 3);
			Dog fido = new Dog("Fido", "Schäfer", 9, 8);
			Dog freja = new Dog("Freja", "Pudel", 4, 5);
			Dog karo = new Dog("Karo", "Mudi", 3, 4);
			Dog king = new Dog("King", "Dansk-svensk gårdshund", 3, 4);
			Dog lad = new Dog("Lad", "Spets", 2, 3);
			Dog lady = new Dog("Lady", "Kuvasz", 7, 5);
			Dog luna = new Dog("Luna", "Akita", 7, 9);
			Dog max = new Dog("Max", "Xoloitzcuintle", 7, 7);
			Dog millie = new Dog("Millie", "Drever", 7, 2);
			Dog milo = new Dog("Milo", "Pumi", 2, 7);
			Dog nova = new Dog("Nova", "Alaskan malamute", 3, 4);
			Dog pluto = new Dog("Pluto", "Jämthund", 9, 7);
			Dog rex = new Dog("Rex", "Samojed", 4, 1);
			Dog ronja = new Dog("Ronja", "Cavalier king charles spaniel", 7, 8);
			Dog sigge = new Dog("Sigge", "Golden retriever", 5, 3);
			Dog siri = new Dog("Siri", "Samojed", 4, 5);
			Dog wilma = new Dog("Wilma", "Kuvasz", 8, 8);
			Dog zeus = new Dog("Zeus", "Samojed", 9, 7);
			Dog zoe = new Dog("Zoe", "Mastiff", 5, 2);

			Dog[] expected = { bamse, bob, bobby, bonnie, bosse, buster, charlie, daisy, fido, freja, karo, king, lad,
					lady, luna, max, millie, milo, nova, pluto, rex, ronja, sigge, siri, wilma, zeus, zoe };

			Dog[] actual = expected.clone();
			randomize(actual); // Slumpa ordningen

			DogSorter.sort(algorithm, Comparator.comparing(Dog::getName), actual);
			assertThat(actual).contains(expected).inThatOrder().onErrorReport("Arrayen är inte korrekt sorterad");
		});
	}

	@Order(50)
	@Test
	@DisplayName("Minst två sorteringsalgoritmer")
	public void atLeastTwoSortingAlgorithms() {
		assertThat(SortingAlgorithm.values().length).isGreaterThan(1).onErrorReport("För få sorteringsalgoritmer");
	}

//	/*
//	 * OBS! DET SISTA TESTET I DENNA TESTSVIT ÄR BORTKOMMENTERAT FRÅN BÖRJAN
//	 * EFTERSOM DET ÄR ETT EXPERIMENT. SYFTET MED DET ÄR ATT FÖRSÖKA UPPTÄCKA
//	 * FELAKTIGA IMPLEMENTATIONER AV SORTERINGSALGORITMERNA GENOM ATT TITTA PÅ HUR
//	 * MÅNGA JÄMFÖRELSER DE GÖR FÖR OLIKA SCENARIER. DETTA ÄR LÄTT FÖR VÅRA
//	 * REFERENSIMPLEMENTATIONER, MEN FÖR ATT VARA ANVÄNDBART FÖR ER SÅ BEHÖVER VI FÅ
//	 * TILLGÅNG TILL ETT ANTAL IMPLEMENTATIONER AV VARJE ALGORITM INNAN VI KAN SE
//	 * HUR TILLFÖRLITLIGT DET ÄR, OCH OM DET ÖVERHUVUDTAGET ÄR ANVÄNDBART.
//	 * 
//	 * DU FÅR GÄRNA TESTA ATT AVKOMMENTERA DET, MEN DU KOMMER BARA ATT HA NYTTA AV
//	 * DET OM DU IMPLEMENTERAR BUBBLE SORT PÅ EGEN HAND, UTAN ATT FÖLJA
//	 * INTRODUKTIONSVIDEON.
//	 */
//
//	private static List<Arguments> sortingImplementationScenarios() {
//		final Dog ASTRA = new Dog("Astra", DEFAULT_DOG_BREED, 1, 1);
//		final Dog BALDER = new Dog("Balder", DEFAULT_DOG_BREED, 1, 1);
//		final Dog CLEO = new Dog("Cleo", DEFAULT_DOG_BREED, 1, 1);
//		final Dog DIANA = new Dog("Diana", DEFAULT_DOG_BREED, 1, 1);
//		final Dog EDNA = new Dog("Edna", DEFAULT_DOG_BREED, 1, 1);
//		final Dog FILIP = new Dog("Filip", DEFAULT_DOG_BREED, 1, 1);
//		final Dog GUNNAR = new Dog("Gunnar", DEFAULT_DOG_BREED, 1, 1);
//		final Dog HENNA = new Dog("Henna", DEFAULT_DOG_BREED, 1, 1);
//		final Dog INGO = new Dog("Ingo", DEFAULT_DOG_BREED, 1, 1);
//		final Dog JADE = new Dog("Jade", DEFAULT_DOG_BREED, 1, 1);
//
//		Dog[] sortedDogs = { ASTRA, BALDER, CLEO, DIANA, EDNA, FILIP, GUNNAR, HENNA, INGO, JADE };
//		Dog[] randomlySortedDogs = { DIANA, BALDER, HENNA, EDNA, JADE, INGO, ASTRA, CLEO, FILIP, GUNNAR };
//		Dog[] reverseSortedDogs = { JADE, INGO, HENNA, GUNNAR, FILIP, EDNA, DIANA, CLEO, BALDER, ASTRA };
//
//		var scenarios = new ArrayList<Arguments>();
//
//		scenarios.add(Arguments.of("BUBBLE_SORT", 9, 10, sortedDogs.clone(), "redan sorterad array"));
//		scenarios.add(Arguments.of("BUBBLE_SORT", 40, 50, randomlySortedDogs.clone(), "slumpmässigt sorterad array"));
//		scenarios.add(Arguments.of("BUBBLE_SORT", 40, 50, reverseSortedDogs.clone(), "omvänt sorterad array"));
//
//		// scenarios.add(Arguments.of("_SORT", 9, 27, sortedDogs.clone(),
//		// "redan sorterad array")); // scenarios.add(Arguments.of("_SORT", 9, 27,
//		// randomlySortedDogs.clone(), "slumpmässigt sorterad array")); //
//		// scenarios.add(Arguments.of("_SORT", 9, 27, reverseSortedDogs.clone(),
//		// "omvänt sorterad array"));
//
//		return scenarios;
//	}
//
//	private static record DogComparision(String first, String second) {
//
//	}
//
//	private static class SpyingDogNameComparator implements Comparator<Dog> {
//
//		private int calls;
//		private List<DogComparision> comparisions = new ArrayList<>();
//
//		@Override
//		public int compare(Dog first, Dog second) {
//			calls++;
//			comparisions.add(new DogComparision(first.getName(), second.getName()));
//			return first.getName().compareTo(second.getName());
//		}
//
//		@Override
//		public String toString() {
//			return "%d:%s".formatted(calls, comparisions);
//		}
//
//	}
//
//	@Order(60)
//	@ParameterizedTest(name = "{0} kan sortera en {4} med mellan {1} och {2} jämförelser")
//	@DisplayName("Stödda sorteringsalgoritmer gör ett rimligt antal jämförelser")
//	@MethodSource("sortingImplementationScenarios")
//	public void doesNormalNumberOfComparisions(String algorithmName, int min, int max, Dog[] dogs, String description) {
//		assertTestTimesOut(() -> {
//			Map<String, SortingAlgorithm> algorithms = new HashMap<>();
//			for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
//				algorithms.put(algorithm.name(), algorithm);
//			}
//
//			org.junit.jupiter.api.Assumptions.assumeTrue(algorithms.containsKey(algorithmName), "Algoritmen stöds inte (ännu)");
//
//			var comparator = new SpyingDogNameComparator();
//			DogSorter.sort(algorithms.get(algorithmName), comparator, dogs);
//
//			// System.out.printf("%s %s %d%n%s%n", algorithmName, description,
//			// comparator.calls, comparator.comparisions);
//			assertThat(comparator.calls).isBetween(min, max).onErrorReport("%s gör ett ovanligt antal jämförelser",
//					algorithmName);
//		});
//	}

}

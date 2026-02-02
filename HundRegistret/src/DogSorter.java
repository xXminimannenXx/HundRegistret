import java.util.Comparator;

public class DogSorter {
    private DogSorter() {

    }

    public static void sort(SortingAlgorithm algorithm, Comparator<Dog> comparator, Dog[] dogs) {
        switch (algorithm) {
            case BUBBLE_SORT:
                bubbleSort(comparator, dogs);
                break;
            case SELECTION_SORT:
                selectionSort(comparator, dogs);
                break;
            default:
                break;
        }
    }

    private static void bubbleSort(Comparator<Dog> comparator, Dog[] dogs) {
        int sorted = dogs.length;
        boolean changed;
        do {
            changed = false;
            for (int i = 1; i < sorted; i++) {
                if (comparator.compare(dogs[i], dogs[i - 1]) < 0) {
                    Dog temp = dogs[i];
                    dogs[i] = dogs[i - 1];
                    dogs[i - 1] = temp;
                    changed = true;
                }
            }
            sorted--;

        } while (changed);
    }

    private static void selectionSort(Comparator<Dog> comparator, Dog[] dogs) {

        for (int i = 0; i < dogs.length; i++) {
            Dog min = dogs[i];
            int minIndex = i;
            for (int j = i; j < dogs.length; j++) {
                if (comparator.compare(dogs[j], min) < 0) {
                    min = dogs[j];
                    minIndex = j;

                }

            }
            Dog temp = dogs[i];
            dogs[i] = min;
            dogs[minIndex] = temp;
        }

    }

}

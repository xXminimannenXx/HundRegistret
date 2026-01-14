import java.util.Comparator;

public class TailNameComparator implements Comparator<Dog> {
    public int compare(Dog a, Dog b) {

        if (a.getTailLength() > b.getTailLength()) {
            return 1;
        }
        if (a.getTailLength() < b.getTailLength()) {
            return -1;
        }
        if (a.getTailLength() == b.getTailLength()) {
            if (a.getName().charAt(0) > b.getName().charAt(0)) {
                return 1;
            }
            if (a.getName().charAt(0) < b.getName().charAt(0)) {
                return -1;
            }
        }
        return 0;

    }

}

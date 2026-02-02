import java.util.Comparator;

public class TailNameComparator implements Comparator<Dog> {
    public int compare(Dog dog1, Dog dog2) {

        if (dog1.getTailLength() > dog2.getTailLength()) {
            return 1;
        }
        if (dog1.getTailLength() < dog2.getTailLength()) {
            return -1;
        }
        if (dog1.getTailLength() == dog2.getTailLength()) {
            if (dog1.getName().charAt(0) > dog2.getName().charAt(0)) {
                return 1;
            }
            if (dog1.getName().charAt(0) < dog2.getName().charAt(0)) {
                return -1;
            }
        }
        return 0;

    }

}

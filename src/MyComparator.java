import java.util.Comparator;

public class MyComparator implements Comparator<Train> {
    public int compare(Train o1, Train o2) {
        return o1.getOtpr().compareTo(o2.getOtpr());
    }
}
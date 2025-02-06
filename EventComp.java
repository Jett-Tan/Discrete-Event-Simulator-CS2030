import java.util.Comparator;

public class EventComp implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        return o1.compareTo(o2);
    }
}

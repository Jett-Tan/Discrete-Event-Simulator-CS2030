public interface Event extends Comparable<Event> {
    public Pair<Event, ImList<Counter>> excute(ImList<Counter> counterList);

    public boolean serveable(Counter c);

    public boolean queueable(Counter c);

    public int serve();

    public int leave();

    public int done();

    public double waiting();

    public Event updateQueue(ImList<Counter> counterList);

    public int compareTo(Event e);

    public double compareTime(double time);

    public int compareCustomer(Customer customer);

    public String toString();
}

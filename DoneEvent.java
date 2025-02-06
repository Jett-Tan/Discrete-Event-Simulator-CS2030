
public class DoneEvent implements Event {
    private final double eventTimes;
    private final Customer customer;
    private final Counter counter;

    DoneEvent(double eventTimes, Customer customer, Counter counter) {
        this.eventTimes = eventTimes;
        this.customer = customer;
        this.counter = counter;
    }

    @Override
    public int done() {
        return 1;
    }

    /*
     * Helper methods to check states
     */

    @Override
    public int serve() {
        return 0;
    }

    @Override
    public int leave() {
        return 0;
    }

    @Override
    public double waiting() {
        return 0;
    }

    /*
     * Unused Interface methods
     */
    @Override
    public boolean serveable(Counter c) {
        return c.serveable(eventTimes);
    }

    @Override
    public boolean queueable(Counter c) {
        return c.queueable();
    }

    @Override
    public Pair<Event, ImList<Counter>> excute(ImList<Counter> counterList) {
        ImList<Counter> temp = new ImList<>();
        boolean updated = false;
        for (int i = 0; i < counterList.size() - 1; i++) {
            Counter c = counterList.get(i);
            if (counter.equals(counterList.get(i)) && !updated) {
                c = c.rest();
                updated = true;
            }
            temp = temp.add(c);
        }
        if (!updated) {
            temp = temp.add(counterList.get(counterList.size() - 1).rest());
        } else {
            temp = temp.add(counterList.get(counterList.size() - 1));
        }
        return new Pair<Event, ImList<Counter>>(this, temp);
    }

    @Override
    public Event updateQueue(ImList<Counter> counterList) {
        return this;
    }

    /*
     * Comparable and toString methods
     */
    @Override
    public int compareTo(Event e) {
        double timeDifference = e.compareTime(eventTimes);
        if (timeDifference == 0) { // Same time
            return e.compareCustomer(customer);
        } else if (timeDifference > 0) { // event take longer time than this
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public double compareTime(double otherTime) {
        return this.eventTimes - otherTime;
    }

    @Override
    public int compareCustomer(Customer customer) {
        return this.customer.compareTo(customer);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s done serving by %s",
                eventTimes,
                customer.toString(),
                counter.toString());
    }

}

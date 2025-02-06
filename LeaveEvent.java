
public class LeaveEvent implements Event {
    private final double eventTimes;
    private final Customer customer;

    public LeaveEvent(double eventTimes, Customer customer) {
        this.eventTimes = eventTimes;
        this.customer = customer;
    }

    @Override
    public int leave() {
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
    public int done() {
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
    public Pair<Event, ImList<Counter>> excute(ImList<Counter> counterList) {
        return new Pair<Event, ImList<Counter>>(this, counterList);
    }

    @Override
    public boolean serveable(Counter c) {
        return c.serveable(eventTimes);
    }

    @Override
    public boolean queueable(Counter c) {
        return c.queueable();
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
        return String.format("%.3f %s leaves",
                eventTimes,
                customer.toString());
    }

}

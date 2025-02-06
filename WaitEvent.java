
public class WaitEvent implements Event {
    private final Counter counter;
    private final double eventTimes;
    private final Customer customer;

    public WaitEvent(double eventTimes, Customer customer, Counter counter) {
        this.eventTimes = eventTimes;
        this.customer = customer;
        this.counter = counter;
    }

    @Override
    public Pair<Event, ImList<Counter>> excute(ImList<Counter> counterList) { // edit
        boolean served = false;
        Event returnEvent = this;
        for (int i = 0; i < counterList.size(); i++) {
            Counter counter = counterList.get(i);
            if (this.counter.equals(counter) && !served) {
                returnEvent = this.createServe(counter);
                served = true;
            }
        }
        return new Pair<Event, ImList<Counter>>(returnEvent, counterList);
    }

    /*
     * Helper methods to create Events
     */

    public Event createServe(Counter counter) {
        Counter c = counter.getServableCounter(eventTimes);
        return new ServeEvent(eventTimes, customer, c);
    }

    /*
     * Helper methods for logic
     */

    @Override
    public boolean serveable(Counter c) {
        return c.serveable(eventTimes);
    }

    @Override
    public boolean queueable(Counter c) {
        return c.queueable();
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
        return String.format("%.3f %s waits at %s",
                eventTimes,
                customer.toString(),
                counter.toString());
    }
}

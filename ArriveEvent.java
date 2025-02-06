public class ArriveEvent implements Event {
    private final double eventTimes;
    private final Customer customer;

    ArriveEvent(double eventTimes, Customer customer) {
        this.eventTimes = eventTimes;
        this.customer = customer;
    }

    @Override
    public Pair<Event, ImList<Counter>> excute(ImList<Counter> counterList) {
        ImList<Counter> returnList = new ImList<Counter>();
        boolean served = false;
        boolean queued = false;
        Event returnEvent = this.createLeave();
        for (int i = 0; i < counterList.size(); i++) {
            Counter counter = counterList.get(i);
            if (this.serveable(counter) && !served) {
                returnEvent = this.createServe(counter);
                served = true;
            }
            returnList = returnList.add(counter);
        }
        if (!served) {
            returnList = new ImList<Counter>(); // TODO EDIT
            for (int i = 0; i < counterList.size(); i++) {
                Counter counter = counterList.get(i);
                if (this.queueable(counter) && !queued) {
                    counter = counter.addQueue();
                    returnEvent = this.createWait(counter);
                    queued = true;
                }
                returnList = returnList.add(counter);
            }
        }
        return new Pair<Event, ImList<Counter>>(returnEvent, returnList);
    }

    /*
     * Helper methods to create Events
     */
    public Event createLeave() {
        return new LeaveEvent(eventTimes, customer);
    }

    public Event createServe(Counter counter) {
        Counter c = counter.getServableCounter(eventTimes);
        return new ServeEvent(eventTimes, customer, c);
    }

    public Event createWait(Counter counter) {
        return new WaitEvent(eventTimes, customer, counter);
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
        return String.format("%.3f %s arrives", eventTimes, customer);
    }
}

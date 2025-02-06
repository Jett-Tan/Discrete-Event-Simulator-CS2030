
public class ServeEvent implements Event {
    private final Counter counter;
    private final double eventTimes;
    private final Customer customer;

    private final double waitTime;

    ServeEvent(double eventTimes, Customer customer, Counter counter) {
        this.eventTimes = eventTimes;
        this.customer = customer;
        this.counter = counter;
        this.waitTime = 0;
    }

    ServeEvent(double eventTimes, Customer customer, Counter counter, double waitTime) {
        this.eventTimes = eventTimes;
        this.customer = customer;
        this.counter = counter;
        this.waitTime = waitTime;
    }

    @Override
    public Pair<Event, ImList<Counter>> excute(ImList<Counter> counterList) {
        // System.out.println("SERVES : " + counter);
        return this.createDone(counter, counterList);
    }

    /*
     * Helper methods to create Events
     */
    public Pair<Event, ImList<Counter>> createDone(Counter counter, ImList<Counter> counterList) {
        ImList<Counter> temp = new ImList<Counter>();
        double nextAvailableTime = this.customer.generateServiceTime() + eventTimes;
        Event tempEvent = new DoneEvent(nextAvailableTime, customer, counter);
        boolean created = false;
        for (int i = 0; i < counterList.size(); i++) {
            Counter c = counterList.get(i);
            if (c.equals(counter) && !created) {
                Pair<? extends Counter, ? extends Counter> result = c.serve(nextAvailableTime,
                        counter);
                c = result.second();
                tempEvent = new DoneEvent(nextAvailableTime, customer, result.first());
                created = true;
            }
            temp = temp.add(c);
        }

        // System.out.println("SERVE EXCUTE RAN : " + tempEvent + " " + temp);
        // for (int i = 0; i < counterList.size() - 1; i++) {
        // Counter c = counterList.get(i);
        // if (counter.equals(counterList.get(i)) && !created) {
        // c = c.serve(nextAvailableTime, c);
        // tempEvent = new DoneEvent(nextAvailableTime, customer, c);
        // created = true;
        // }
        // temp = temp.add(c);
        // }
        // if (!created) {
        // temp = temp.add(counterList.get(counterList.size() - 1)
        // .serve(nextAvailableTime, counter));
        // } else {
        // temp = temp.add(counterList.get(counterList.size() - 1));
        // }
        return new Pair<Event, ImList<Counter>>(tempEvent, temp);
    }

    /*
     * Helper methods for logic
     */
    @Override
    public Event updateQueue(ImList<Counter> counterList) {
        Event temp = this.updateServe(counter, waitTime);
        boolean updated = false;
        for (int i = 0; i < counterList.size(); i++) {
            Counter tempCounter = counterList.get(i);
            if (tempCounter.equals(this.counter) && !updated) {
                temp = this.updateServe(tempCounter, waitTime);
                updated = true;
            }
        }
        return temp;
        // return this.updateServe(manager, counter, waitTime);
    }

    public Event updateServe(Counter counter, double waitTime) {
        double updateTime = counter.getTime();
        Counter c = updateTime <= eventTimes
                ? counter.getServableCounter(eventTimes)
                : this.counter;
        updateTime = updateTime > eventTimes
                ? updateTime
                : eventTimes;
        waitTime += updateTime - eventTimes;
        waitTime = waitTime > 0 ? waitTime : 0;
        return new ServeEvent(updateTime, customer, c, waitTime);
    }

    /*
     * Helper methods to check states
     */

    @Override
    public int serve() {
        return 1;
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
        return waitTime;
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
        return String.format("%.3f %s serves by %s",
                eventTimes,
                customer.toString(),
                counter.toString());
        // return String.format("%s serves by %s waited %.5f", super.toString(),
        // counter.toString(), waitTime);
    }

}

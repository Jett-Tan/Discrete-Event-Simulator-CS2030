
public class SelfCounter implements Counter {
    private final int id;
    private final double nextAvailableTime;

    SelfCounter(int id) {
        this.id = id;
        this.nextAvailableTime = 0;
    }

    SelfCounter(int id, double nextAvailableTime) {
        this.id = id;
        this.nextAvailableTime = nextAvailableTime;
    }

    @Override
    public Counter rest() {
        return this;
    }

    @Override
    public Pair<Counter, SelfCounter> serve(double serviceTime, Counter c) {
        SelfCounter temp = new SelfCounter(id, serviceTime);
        return new Pair<Counter, SelfCounter>(temp, temp);
    }

    @Override
    public double getTime() {
        return nextAvailableTime;
    }

    @Override
    public boolean queueable() { // wont use
        return true;
    }

    @Override
    public boolean serveable(double eventTime) {
        return nextAvailableTime <= eventTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof SelfCounter SC) {
            return this.id == SC.id;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("self-check %d", id);
        // return String.format("SC %d %.4f", id, nextAvailableTime);
    }

    @Override
    public Counter getServableCounter(double eventTime) {
        return this;
    }

    @Override
    public SelfCounter addQueue() {
        return this;
    }
}

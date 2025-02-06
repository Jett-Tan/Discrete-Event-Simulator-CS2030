public interface Counter {

    abstract Pair<Counter, ? extends Counter> serve(double serviceTime, Counter counter);

    abstract Counter rest();

    abstract boolean queueable();

    abstract boolean serveable(double eventTime);

    abstract Counter getServableCounter(double eventTime);

    abstract Counter addQueue();

    abstract double getTime();
}

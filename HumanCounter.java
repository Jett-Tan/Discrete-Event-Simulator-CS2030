import java.util.function.Supplier;

public class HumanCounter implements Counter {
    private final int id;
    private final double nextAvailableTime;
    private final int qMax;
    private final int qCur;
    private final Supplier<Double> restSupplier;

    HumanCounter(int id, Supplier<Double> restSupplier, int qMax) {
        this.id = id;
        this.restSupplier = restSupplier;
        this.nextAvailableTime = 0;
        this.qMax = qMax;
        this.qCur = 0;
    }

    HumanCounter(int id,
            Supplier<Double> restSupplier,
            int qMax,
            int qCur,
            double nextAvailableTime) {
        this.id = id;
        this.restSupplier = restSupplier;
        this.nextAvailableTime = nextAvailableTime;
        this.qMax = qMax;
        this.qCur = qCur < 0 ? 0 : qCur;
    }

    @Override
    public Pair<Counter, HumanCounter> serve(double nextAvailableTime, Counter c) {
        HumanCounter temp = new HumanCounter(id, restSupplier, qMax, (qCur - 1),
                nextAvailableTime);

        return new Pair<Counter, HumanCounter>(temp, temp);
    }

    @Override
    public Counter rest() {
        return new HumanCounter(id, restSupplier, qMax, qCur,
                nextAvailableTime + restSupplier.get());
    }

    @Override
    public HumanCounter addQueue() {
        return new HumanCounter(id, restSupplier, qMax, qCur + 1,
                nextAvailableTime);
    }

    @Override
    public boolean queueable() {
        return qCur < qMax;
    }

    @Override
    public boolean serveable(double eventTime) {
        return nextAvailableTime <= eventTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof HumanCounter HC) {
            return this.id == HC.id;
        } else {
            return false;
        }
    }

    @Override
    public double getTime() {
        return this.nextAvailableTime;
    }

    @Override /* Wont use */
    public Counter getServableCounter(double eventTime) {
        return this;
    }

    @Override
    public String toString() {
        // return String.format("%d %.3f %d/%d", id, nextAvailableTime, qCur, qMax);
        return Integer.toString(id);
    }
}

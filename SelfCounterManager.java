public class SelfCounterManager implements Counter {
    private final int id;
    private final ImList<SelfCounter> selfCounterList;
    private final int qMax;
    private final int qCur;

    SelfCounterManager(int numOfcounter, int numOfSelfChecks, int qMax) {
        this.id = numOfcounter + 1;
        this.selfCounterList = createSelfCounterList(numOfcounter, numOfSelfChecks);
        this.qMax = qMax;
        this.qCur = 0;
    }

    SelfCounterManager(int id, ImList<SelfCounter> selfCounterList, int qMax, int qCur) {
        this.id = id;
        this.selfCounterList = selfCounterList;
        this.qMax = qMax;
        this.qCur = qCur < 0 ? 0 : qCur;
    }

    @Override
    public Counter rest() {
        return this;
    }

    private ImList<SelfCounter> createSelfCounterList(int numOfcounter, int numOfSelfChecks) {
        ImList<SelfCounter> temp = new ImList<SelfCounter>();
        for (int i = 1; i < numOfSelfChecks + 1; i++) {
            temp = temp.add(new SelfCounter(numOfcounter + i));
        }
        return temp;
    }

    /* Find which counter is servable */
    public Counter getServableCounter(double eventTimes) { // err
        Counter temp = this;
        for (int i = 0; i < selfCounterList.size(); i++) {
            if (selfCounterList.get(i).serveable(eventTimes)) {
                return selfCounterList.get(i);
            }
        }
        return temp;
    }

    /* Find the selfcounter that is allocated */
    /* Pair<Counter, Counter> ==> Pair<Counter, CounterManager> */
    public Pair<Counter, SelfCounterManager> serve(double nextAvailableTime, Counter counter) {
        ImList<SelfCounter> temp = new ImList<SelfCounter>();
        Counter counterTemp = this;
        boolean served = false;
        boolean assigned = !this.equal(counter);
        // System.out.println(counter + " |||VS||| " + this.toString());
        for (int i = 0; i < selfCounterList.size(); i++) {
            SelfCounter sCounter = selfCounterList.get(i);
            if (assigned) {
                if (sCounter.equals(counter) && !served) {
                    Pair<Counter, SelfCounter> result = sCounter.serve(nextAvailableTime, sCounter);
                    sCounter = result.second();
                    counterTemp = sCounter;
                    served = true;
                }
            } else {
                if (sCounter.serveable(nextAvailableTime) && !served) {
                    Pair<Counter, SelfCounter> result = sCounter.serve(nextAvailableTime, sCounter);
                    sCounter = result.second();
                    counterTemp = sCounter;
                    served = true;
                }
            }
            temp = temp.add(sCounter);
        }
        SelfCounterManager manager = new SelfCounterManager(id, temp, qMax, qCur - 1);
        // System.out.println("SELFCOUNTER MANAGER SERVE " + counterTemp + " " +
        // manager);
        return new Pair<Counter, SelfCounterManager>(counterTemp, manager);
    }

    public boolean serveable(double eventTime) {
        for (int i = 0; i < this.selfCounterList.size(); i++) {
            SelfCounter selfCounter = selfCounterList.get(i);
            if (selfCounter.serveable(eventTime)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SelfCounterManager addQueue() {
        return new SelfCounterManager(id, selfCounterList, qMax, qCur + 1);
    }

    @Override
    public boolean queueable() {
        return selfCounterList.size() > 0 && qMax > qCur;
    }

    @Override
    public String toString() {
        return String.format("self-check %d", id);
        // return String.format("M%d %d/%d %s", id, qCur, qMax,
        // selfCounterList.toString());
    }

    @Override
    public double getTime() {
        return getMinTime();
    }

    private double getMaxTime() {
        double temp = selfCounterList.get(0).getTime();
        for (int i = 0; i < selfCounterList.size(); i++) {
            if (temp < selfCounterList.get(i).getTime()) {
                temp = selfCounterList.get(i).getTime();
            }
        }
        return temp;
    }

    private double getMinTime() {
        double temp = selfCounterList.get(0).getTime();
        for (int i = 0; i < selfCounterList.size(); i++) {
            if (temp > selfCounterList.get(i).getTime()) {
                temp = selfCounterList.get(i).getTime();
            }
        }
        return temp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof SelfCounterManager SCM) {
            return this.id == SCM.id;
        } else {
            for (SelfCounter selfCounter : selfCounterList) {
                if (selfCounter.equals(obj)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean equal(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof SelfCounterManager SCM) {
            return this.id == SCM.id;
        } else {
            return false;
        }
    }
}

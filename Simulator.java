import java.util.function.Supplier;

public class Simulator {
    private final int numOfServers;
    private final int numOfSelfChecks;
    private final int qmax;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTimes;
    private final Supplier<Double> restTimes;

    // private static final int val = 0;

    public Simulator(
            int numOfServers,
            int numOfSelfChecks,
            int qmax,
            ImList<Double> arrivalTimes,
            Supplier<Double> serviceTimes,
            Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qmax = qmax;
        this.arrivalTimes = arrivalTimes;
        this.serviceTimes = serviceTimes;
        this.restTimes = restTimes;
    }

    public String simulate() {
        PQ<Event> eventList = createEventList();
        ImList<Counter> counterList = createCounterList();
        // System.out.println("Initial counter List : " + counterList);
        // System.out.println("Initial Event List : " + eventList);
        String returnstring = "";
        // if (this.numOfSelfChecks > val) {
        // returnstring += counterList;
        // }

        Integer leave = 0;
        Integer done = 0;
        Double waiting = 0.0;
        int debug = 1;
        while (!eventList.isEmpty()) {
            eventList = updateQueue(eventList, counterList);
            Pair<Event, PQ<Event>> pollPair = eventList.poll();
            Event event = pollPair.first();
            eventList = pollPair.second();
            returnstring += event.toString() + "\n";
            // System.out.println("\nBEFORE EXC : " + event.toString() + "\n" +
            // counterList.toString() + "\n");
            leave += event.leave();
            done += event.done();
            waiting += event.waiting();
            Pair<Event, ImList<Counter>> pollPair2 = event.excute(counterList);
            counterList = pollPair2.second();
            if (event.leave() + event.done() > 0) {
                // System.out.println("\nAFTER EXC : " + event.toString() + "\n" +
                // counterList.toString());
                // System.out
                // .println("DONE ?
                // LEAVE===================================================================\n");
                continue;
            }
            event = pollPair2.first();
            eventList = eventList.add(event);
            // System.out.println("\nAFTER EXC : " + event.toString() + "\n" +
            // counterList.toString());
            // System.out.println("===============================================
            // =================================\n");
            if (debug < 10) {
                // debug++;
            } else {
                break;
            }
        }
        double avg = waiting / done;
        // System.out.println("\n\n\n\n\n\n");

        returnstring += String.format("[%.3f %d %d]", avg, done, leave);
        return returnstring;
    }

    private PQ<Event> updateQueue(PQ<Event> eventList, ImList<Counter> counterList) {
        PQ<Event> temp = new PQ<Event>(new EventComp());
        while (!eventList.isEmpty()) {
            Pair<Event, PQ<Event>> pollPair = eventList.poll();
            Event event = pollPair.first();
            eventList = pollPair.second();
            event = event.updateQueue(counterList);
            temp = temp.add(event);
        }
        // System.out.println("QUEUE AFTER SORT : " + temp);
        return temp;
    }

    private PQ<Event> createEventList() {
        PQ<Event> temp = new PQ<Event>(new EventComp());
        for (int i = 0; i < arrivalTimes.size(); i++) {
            temp = temp.add(
                    new ArriveEvent(arrivalTimes.get(i),
                            new Customer(i + 1, serviceTimes)));
        }
        return temp;
    }

    private ImList<Counter> createCounterList() {
        ImList<Counter> temp = new ImList<Counter>();
        for (int i = 1; i < 1 + numOfServers; i++) {
            temp = temp.add(new HumanCounter(i, restTimes, qmax));
        }
        return temp.add(new SelfCounterManager(numOfServers, numOfSelfChecks, qmax));
    }
}

# Discrete Event Simulator (CS2030)

## Overview

This project implements a **Discrete Event Simulator** that models customer interactions with **human servers** and **self-checkout counters**. The simulator processes customer arrivals, server availability, and service times while considering:

- **Human Servers:** Can take occasional breaks with randomly generated rest times.
- **Self-Checkout Counters:** Do not take breaks and share a common queue.
- **Event Queue:** Manages arrival, service, rest, and completion events.
- **Randomized Behavior:** Uses `java.util.Random` for generating customer service and rest times.

## Features

- **Supports both human servers and self-checkout counters.**
- **Handles random rest times for human servers.**
- **Simulates customer arrivals, waiting, service, and departures.**
- **Utilizes event-driven programming with a priority queue.**
- **Efficient queue management with shared queues for self-checkout counters.**

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Jett-Tan/Discrete-Event-Simulator-CS2030.git
   ```
2. Navigate to the project directory:
   ```bash
   cd discrete-event-simulator-CS2030
   ```
3. Compile the Java files:
   ```bash
   javac *.java
   ```
4. Run the simulator with an input file:
   ```bash
   cat 1.in | java Main
   ```

## Usage

### Input Format

The program expects input in the following format:

```
<number_of_human_servers> <number_of_self_checkouts> <max_queue_length> <probability_of_rest>
<arrival_time_1>
<arrival_time_2>
...
<service_time_1>
<service_time_2>
...
```

Example:

```
2 0 2 0
0.500
0.600
0.700
1.500
1.600
1.700
```

### Sample Runs

#### Sample 1: Two human servers, no self-checkouts, max queue length of 2, probability of rest 0.0

```
$ cat 1.in
2 0 2 0
0.500
0.600
0.700
1.500
1.600
1.700

$ cat 1.in | java Main
0.500 1 arrives
0.500 1 serves by 1
0.600 2 arrives
0.600 2 serves by 2
0.700 3 arrives
0.700 3 waits at 1
1.500 1 done serving by 1
1.500 3 serves by 1
1.500 4 arrives
1.500 4 waits at 1
1.600 2 done serving by 2
1.600 5 arrives
1.600 5 serves by 2
1.700 6 arrives
1.700 6 waits at 1
2.500 3 done serving by 1
2.500 4 serves by 1
2.600 5 done serving by 2
3.500 4 done serving by 1
3.500 6 serves by 1
4.500 6 done serving by 1
[0.600 6 0]
```

#### Sample 2: Two human servers, no self-checkouts, ten customers, probability of rest 0.0

```
cat 2.in
2 0 2 0
0.000000
0.313508
1.204910
2.776499
3.876961
3.909737
9.006391
9.043361
9.105379
9.159630

$ cat 2.in | java Main
0.000 1 arrives
0.000 1 serves by 1
0.314 2 arrives
0.314 2 serves by 2
1.000 1 done serving by 1
1.205 3 arrives
1.205 3 serves by 1
1.314 2 done serving by 2
2.205 3 done serving by 1
2.776 4 arrives
2.776 4 serves by 1
3.776 4 done serving by 1
3.877 5 arrives
3.877 5 serves by 1
3.910 6 arrives
3.910 6 serves by 2
4.877 5 done serving by 1
4.910 6 done serving by 2
9.006 7 arrives
9.006 7 serves by 1
9.043 8 arrives
9.043 8 serves by 2
9.105 9 arrives
9.105 9 waits at 1
9.160 10 arrives
9.160 10 waits at 1
10.006 7 done serving by 1
10.006 9 serves by 1
10.043 8 done serving by 2
11.006 9 done serving by 1
11.006 10 serves by 1
12.006 10 done serving by 1
[0.275 10 0]
```

#### Sample 3: Two human servers, no self-checkouts, ten customers, probability of rest 0.5

```
$ cat 3.in
2 0 2 0.5
0.000000
0.313508
1.204910
2.776499
3.876961
3.909737
9.006391
9.043361
9.105379
9.159630

$ cat 3.in | java Main
0.000 1 arrives
0.000 1 serves by 1
0.314 2 arrives
0.314 2 serves by 2
1.000 1 done serving by 1
1.205 3 arrives
1.205 3 serves by 1
1.314 2 done serving by 2
2.205 3 done serving by 1
2.776 4 arrives
2.776 4 waits at 1
3.053 4 serves by 1
3.877 5 arrives
3.877 5 waits at 1
3.910 6 arrives
3.910 6 waits at 1
4.053 4 done serving by 1
4.053 5 serves by 1
5.053 5 done serving by 1
5.902 6 serves by 1
6.902 6 done serving by 1
9.006 7 arrives
9.006 7 serves by 1
9.043 8 arrives
9.043 8 serves by 2
9.105 9 arrives
9.105 9 waits at 1
9.160 10 arrives
9.160 10 waits at 1
10.006 7 done serving by 1
10.006 9 serves by 1
10.043 8 done serving by 2
11.006 9 done serving by 1
11.006 10 serves by 1
12.006 10 done serving by 1
[0.519 10 0]
```

#### Sample 4: One human server, two self-checkouts, ten customers, probability of rest 0.5

```
$ cat 4.in
1 2 2 0.5
0.000000
0.313508
1.204910
2.776499
3.876961
3.909737
9.006391
9.043361
9.105379
9.159630

$ cat 4.in | java Main
0.000 1 arrives
0.000 1 serves by 1
0.314 2 arrives
0.314 2 serves by self-check 2
1.000 1 done serving by 1
1.205 3 arrives
1.205 3 serves by 1
1.314 2 done serving by self-check 2
2.205 3 done serving by 1
2.776 4 arrives
2.776 4 serves by self-check 2
3.776 4 done serving by self-check 2
3.877 5 arrives
3.877 5 serves by self-check 2
3.910 6 arrives
3.910 6 serves by self-check 3
4.877 5 done serving by self-check 2
4.910 6 done serving by self-check 3
9.006 7 arrives
9.006 7 serves by 1
9.043 8 arrives
9.043 8 serves by self-check 2
9.105 9 arrives
9.105 9 serves by self-check 3
9.160 10 arrives
9.160 10 waits at 1
10.006 7 done serving by 1
10.043 8 done serving by self-check 2
10.105 9 done serving by self-check 3
10.854 10 serves by 1
11.854 10 done serving by 1
[0.169 10 0]
```

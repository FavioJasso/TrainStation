/*
 * Author : Favio Valentino Jasso
 * For : Prof. Bamford 
 * Data Structures and Algorithms
 * Last Modified : 04/06/2024
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class FavioStation {

    public static Random generator = new Random();
    public static final int NUM_STATIONS = 5;
    public static final int TRAIN_CAPACITY = 50;
    public static final int TRAIN_INTERVAL = 10;
    public static final int TIME_INTERVAL = 200;
    public static int trainCount = 0;
    public static int passengersOnTrains = 0;
    public static int passengersDelivered = 0;
    public static int passengersCreated = 0;

    /**
     * @param args
     */
    public static void main(String[] args) {
    List<Station> stations = createStations();
    LinkedQueue<Train> trains = new LinkedQueue<>();
    LinkedQueue<Passenger> passengers = new LinkedQueue<>();

    for (int time = 0; time <= TIME_INTERVAL; time++) {
        System.out.println("\nTime Marker " + time + "\t waiting: " + passengersCreated + "\t on trains: " + passengersOnTrains);
        startNewTrain(trains, time);
        passengersCreated = generatePassengers(passengers, stations, time, passengersCreated);
        passengersOnTrains = moveTrains(trains, stations, time, passengersOnTrains);

    }
    finalReport(TIME_INTERVAL, passengersCreated, passengersOnTrains, passengersDelivered, passengers);
}


    /**
     * @return stations 
     */
    public static List< Station> createStations() {
        List< Station> stations = new ArrayList<>();
        for (int i = 0; i < NUM_STATIONS; i++) {
            int timeToNextStation = generator.nextInt(10) + 5;
            stations.add(new Station(timeToNextStation));
            System.out.println("Created station " + i + " time to next is " + timeToNextStation);
        }
        return stations;
    }

    /**
     * @param trains
     * @param time
     */
    public static void startNewTrain(LinkedQueue< Train> trains, int time) {
        if (time % TRAIN_INTERVAL == 0) {
            Train newTrain = new Train(TRAIN_CAPACITY);
            trains.enqueue(newTrain);
            trainCount++;
        }
    }

    /**
     * 
     * @param passengers         The LinkedQueue of passengers to add new passengers to.
     * @param stations           The List of stations to add passengers to.
     * @param time               The current time.
     * @param passengersCreated  The total number of passengers created so far.
     * @return                   The updated total number of passengers created.
     */
    public static int generatePassengers(LinkedQueue< Passenger> passengers, List< Station> stations, int time, int passengersCreated) {
        int numPassengers = generator.nextInt(6);
        for (int i = 0; i < numPassengers; i++) {
            int startStation = generator.nextInt(NUM_STATIONS);
            int stopStation = generator.nextInt(NUM_STATIONS);
            while (startStation == stopStation) {
                stopStation = generator.nextInt(NUM_STATIONS);
            }
            Passenger newPassenger = new Passenger(startStation, stopStation);
            stations.get(startStation).addPassenger(newPassenger);
            passengers.enqueue(newPassenger);
            passengersCreated++;
        }
        return passengersCreated;
    }
    /**
     * Moves the trains, unloads and loads passengers, and updates the train and station information.
     * 
     * @param trains The queue of trains to be moved.
     * @param stations The list of stations.
     * @param time The current time.
     * @param passengersOnTrains The number of passengers currently on the trains.
     * @return The updated number of passengers on the trains after the movement.
     */
    public static int moveTrains(LinkedQueue< Train> trains, List< Station> stations, int time, int passengersOnTrains) {
        int numTrains = trainCount;
        for (int i = 0; i < numTrains; i++) {
            Train currentTrain = trains.dequeue();
            currentTrain.move();

            if (currentTrain.timeToNext() == 0) {
                int currentStation = currentTrain.nextStation();
                int passengersLeaving = currentTrain.unloadPassengers(currentStation);
                passengersOnTrains=currentTrain.loadPassengers(stations.get(currentStation),time);
                passengersOnTrains -= passengersLeaving;
                passengersDelivered += passengersLeaving;

                int nextStationIndex = currentStation + 1;
                if (nextStationIndex < NUM_STATIONS) {
                    currentTrain.updateStation(stations.get(nextStationIndex).getTimeToNextStation());
                    trains.enqueue(currentTrain);
                } else {
                    trainCount--;
                }
            } else {
                trains.enqueue(currentTrain);
                passengersOnTrains++;
            }

        }

        return passengersOnTrains;
    }

    /**
     * Generates the final report for the station.
     *
     * @param clock The current time in the simulation.
     * @param passengersCreated The total number of passengers created.
     * @param passengersOnTrains The number of passengers currently on a train.
     * @param passengersDelivered The number of passengers delivered.
     * @param passengers The queue of passengers.
     */
    public static void finalReport(int clock, int passengersCreated, int passengersOnTrains, int passengersDelivered, QueueInterface< Passenger> passengers) {
        System.out.println("Final Report");
        System.out.println("The total number of passengers is " + passengersCreated);
        System.out.println("The number of passengers currently on a train " + passengersOnTrains);
        System.out.println("The number of passengers delivered is " + passengersDelivered);
        int waitBoardedSum = 0;
        int waitNotBoardedSum = 0;
        for (int i = 0; i < passengersCreated; i++) {
            Passenger p = passengers.dequeue();
            if (p.boarded()) {
                waitBoardedSum += p.waitTime(clock);
            } else {
                waitNotBoardedSum += p.waitTime(clock);
            }
        }
        System.out.println("The average wait time for passengers that have boarded is");
        System.out.println((double) waitBoardedSum / (passengersOnTrains + passengersDelivered));
        System.out.println("The average wait time for passengers that have not yet boarded is");
        System.out.println((double) waitNotBoardedSum / (passengersCreated - passengersOnTrains - passengersDelivered));
    }

}
/*
Created station 0 time to next is 13
Created station 1 time to next is 10
Created station 2 time to next is 9
Created station 3 time to next is 6
Created station 4 time to next is 10

Time Marker 0    waiting: 0      on trains: 0
        Created train 1
        Created passenger at station 0 heading to 2
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 1
        Train 1 unloaded 0 passengers at station 0
                loaded 1 passengers; Space left 49

Time Marker 1    waiting: 3      on trains: 1
        Created passenger at station 1 heading to 4
        Created passenger at station 4 heading to 2

Time Marker 2    waiting: 5      on trains: 2
        Created passenger at station 0 heading to 4
        Created passenger at station 1 heading to 0

Time Marker 3    waiting: 7      on trains: 3
        Created passenger at station 4 heading to 2
        Created passenger at station 2 heading to 0
        Created passenger at station 0 heading to 2

Time Marker 4    waiting: 10     on trains: 4
        Created passenger at station 2 heading to 1
        Created passenger at station 1 heading to 3
        Created passenger at station 0 heading to 2

Time Marker 5    waiting: 13     on trains: 5
        Created passenger at station 3 heading to 2

Time Marker 6    waiting: 14     on trains: 6
        Created passenger at station 4 heading to 2
        Created passenger at station 3 heading to 0
        Created passenger at station 0 heading to 3

Time Marker 7    waiting: 17     on trains: 7
        Created passenger at station 2 heading to 1
        Created passenger at station 3 heading to 0
        Created passenger at station 3 heading to 4
        Created passenger at station 2 heading to 1
        Created passenger at station 3 heading to 0

Time Marker 8    waiting: 22     on trains: 8
        Created passenger at station 4 heading to 2
        Created passenger at station 0 heading to 2
        Created passenger at station 4 heading to 2
        Created passenger at station 2 heading to 4
        Created passenger at station 4 heading to 0

Time Marker 9    waiting: 27     on trains: 9
        Created passenger at station 2 heading to 0
        Created passenger at station 4 heading to 1
        Created passenger at station 2 heading to 3
        Created passenger at station 2 heading to 4
        Created passenger at station 2 heading to 4

Time Marker 10   waiting: 32     on trains: 10
        Created train 2
        Created passenger at station 1 heading to 0
        Train 1 unloaded 0 passengers at station 1
                loaded 4 passengers; Space left 45
        Train 2 unloaded 0 passengers at station 0
                loaded 5 passengers; Space left 45

Time Marker 11   waiting: 33     on trains: 5
        Created passenger at station 2 heading to 0
        Created passenger at station 4 heading to 1

Time Marker 12   waiting: 35     on trains: 7
        Created passenger at station 4 heading to 2
        Created passenger at station 1 heading to 3
        Created passenger at station 1 heading to 0
        Created passenger at station 2 heading to 0

Time Marker 13   waiting: 39     on trains: 9
        Created passenger at station 3 heading to 1
        Created passenger at station 3 heading to 4

Time Marker 14   waiting: 41     on trains: 11
        Created passenger at station 3 heading to 1

Time Marker 15   waiting: 42     on trains: 13
        Created passenger at station 2 heading to 4
        Created passenger at station 2 heading to 0

Time Marker 16   waiting: 44     on trains: 15

Time Marker 17   waiting: 44     on trains: 17
        Created passenger at station 4 heading to 2
        Created passenger at station 0 heading to 3
        Created passenger at station 0 heading to 3

Time Marker 18   waiting: 47     on trains: 19

Time Marker 19   waiting: 47     on trains: 21
        Created passenger at station 4 heading to 3
        Created passenger at station 1 heading to 2
        Created passenger at station 3 heading to 0
        Train 1 unloaded 1 passengers at station 2
                loaded 15 passengers; Space left 31

Time Marker 20   waiting: 50     on trains: 15
        Created train 3
        Train 2 unloaded 0 passengers at station 1
                loaded 3 passengers; Space left 42
        Train 3 unloaded 0 passengers at station 0
                loaded 2 passengers; Space left 48

Time Marker 21   waiting: 50     on trains: 2
        Created passenger at station 0 heading to 4
        Created passenger at station 4 heading to 1
        Created passenger at station 0 heading to 1

Time Marker 22   waiting: 53     on trains: 5
        Created passenger at station 3 heading to 2
        Created passenger at station 2 heading to 0
        Created passenger at station 0 heading to 4

Time Marker 23   waiting: 56     on trains: 8
        Created passenger at station 3 heading to 2
        Created passenger at station 3 heading to 1
        Created passenger at station 0 heading to 3
        Created passenger at station 2 heading to 0

Time Marker 24   waiting: 60     on trains: 11
        Created passenger at station 3 heading to 4
        Created passenger at station 4 heading to 0

Time Marker 25   waiting: 62     on trains: 14
        Train 1 unloaded 2 passengers at station 3
                loaded 13 passengers; Space left 20

Time Marker 26   waiting: 62     on trains: 13

Time Marker 27   waiting: 62     on trains: 16
        Created passenger at station 2 heading to 3

Time Marker 28   waiting: 63     on trains: 19
        Created passenger at station 3 heading to 0
        Created passenger at station 1 heading to 0
        Created passenger at station 0 heading to 2
        Created passenger at station 2 heading to 1

Time Marker 29   waiting: 67     on trains: 22
        Created passenger at station 2 heading to 3
        Created passenger at station 3 heading to 0
        Created passenger at station 1 heading to 2
        Created passenger at station 4 heading to 3
        Created passenger at station 1 heading to 3
        Train 2 unloaded 4 passengers at station 2
                loaded 5 passengers; Space left 41

Time Marker 30   waiting: 72     on trains: 2
        Created train 4
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 1
        Train 3 unloaded 0 passengers at station 1
                loaded 3 passengers; Space left 45
        Train 4 unloaded 0 passengers at station 0
                loaded 5 passengers; Space left 45

Time Marker 31   waiting: 74     on trains: 5
        Created passenger at station 4 heading to 3
        Created passenger at station 2 heading to 3

Time Marker 32   waiting: 76     on trains: 9
        Created passenger at station 2 heading to 4
        Created passenger at station 3 heading to 4
        Created passenger at station 2 heading to 0
        Created passenger at station 2 heading to 0
        Created passenger at station 4 heading to 1

Time Marker 33   waiting: 81     on trains: 13

Time Marker 34   waiting: 81     on trains: 17
        Created passenger at station 1 heading to 0
        Created passenger at station 3 heading to 2
        Created passenger at station 3 heading to 4
        Created passenger at station 4 heading to 1
        Created passenger at station 4 heading to 0

Time Marker 35   waiting: 86     on trains: 21
        Created passenger at station 2 heading to 4
        Created passenger at station 3 heading to 4
        Created passenger at station 0 heading to 4
        Created passenger at station 0 heading to 1
        Train 1 unloaded 8 passengers at station 4
                loaded 18 passengers; Space left 10
        Train 2 unloaded 4 passengers at station 3
                loaded 6 passengers; Space left 39

Time Marker 36   waiting: 90     on trains: 4
        Created passenger at station 4 heading to 2
        Created passenger at station 1 heading to 4

Time Marker 37   waiting: 92     on trains: 7
        Created passenger at station 2 heading to 4
        Created passenger at station 1 heading to 2

Time Marker 38   waiting: 94     on trains: 10
        Created passenger at station 0 heading to 4
        Created passenger at station 4 heading to 0
        Created passenger at station 3 heading to 1

Time Marker 39   waiting: 97     on trains: 13
        Created passenger at station 2 heading to 4
        Train 3 unloaded 1 passengers at station 2
                loaded 9 passengers; Space left 37

Time Marker 40   waiting: 98     on trains: 9
        Created train 5
        Created passenger at station 0 heading to 2
        Created passenger at station 0 heading to 1
        Created passenger at station 1 heading to 2
        Train 4 unloaded 1 passengers at station 1
                loaded 4 passengers; Space left 42
        Train 5 unloaded 0 passengers at station 0
                loaded 5 passengers; Space left 45

Time Marker 41   waiting: 101    on trains: 5

Time Marker 42   waiting: 101    on trains: 9
        Created passenger at station 0 heading to 2

Time Marker 43   waiting: 102    on trains: 13

Time Marker 44   waiting: 102    on trains: 17
        Created passenger at station 1 heading to 0
        Created passenger at station 0 heading to 3
        Created passenger at station 3 heading to 2
        Created passenger at station 2 heading to 3

Time Marker 45   waiting: 106    on trains: 21
        Created passenger at station 2 heading to 4
        Created passenger at station 4 heading to 1
        Train 2 unloaded 4 passengers at station 4
                loaded 3 passengers; Space left 40
        Train 3 unloaded 4 passengers at station 3
                loaded 2 passengers; Space left 39

Time Marker 46   waiting: 108    on trains: 0
        Created passenger at station 1 heading to 2
        Created passenger at station 4 heading to 1
        Created passenger at station 3 heading to 0
        Created passenger at station 2 heading to 4
        Created passenger at station 3 heading to 1

Time Marker 47   waiting: 113    on trains: 3

Time Marker 48   waiting: 113    on trains: 6
        Created passenger at station 4 heading to 0
        Created passenger at station 0 heading to 1

Time Marker 49   waiting: 115    on trains: 9
        Created passenger at station 2 heading to 3
        Created passenger at station 1 heading to 3
        Created passenger at station 3 heading to 0
        Train 4 unloaded 3 passengers at station 2
                loaded 4 passengers; Space left 41

Time Marker 50   waiting: 118    on trains: 2
        Created train 6
        Created passenger at station 4 heading to 1
        Train 5 unloaded 2 passengers at station 1
                loaded 3 passengers; Space left 44
        Train 6 unloaded 0 passengers at station 0
                loaded 3 passengers; Space left 47

Time Marker 51   waiting: 119    on trains: 3
        Created passenger at station 2 heading to 0
        Created passenger at station 3 heading to 2
        Created passenger at station 3 heading to 4

Time Marker 52   waiting: 122    on trains: 7
        Created passenger at station 4 heading to 2
        Created passenger at station 4 heading to 2
        Created passenger at station 3 heading to 1

Time Marker 53   waiting: 125    on trains: 11
        Created passenger at station 0 heading to 4
        Created passenger at station 4 heading to 3
        Created passenger at station 4 heading to 3
        Created passenger at station 2 heading to 0

Time Marker 54   waiting: 129    on trains: 15
        Created passenger at station 3 heading to 2
        Created passenger at station 0 heading to 1
        Created passenger at station 1 heading to 2
        Created passenger at station 1 heading to 2
        Created passenger at station 0 heading to 4

Time Marker 55   waiting: 134    on trains: 19
        Created passenger at station 3 heading to 1
        Created passenger at station 0 heading to 2
        Train 3 unloaded 4 passengers at station 4
                loaded 7 passengers; Space left 36
        Train 4 unloaded 3 passengers at station 3
                loaded 8 passengers; Space left 36

Time Marker 56   waiting: 136    on trains: 7
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 1

Time Marker 57   waiting: 138    on trains: 10

Time Marker 58   waiting: 138    on trains: 13
        Created passenger at station 1 heading to 0
        Created passenger at station 1 heading to 2

Time Marker 59   waiting: 140    on trains: 16
        Created passenger at station 0 heading to 1
        Created passenger at station 2 heading to 4
        Created passenger at station 2 heading to 0
        Created passenger at station 4 heading to 1
        Train 5 unloaded 2 passengers at station 2
                loaded 6 passengers; Space left 40

Time Marker 60   waiting: 144    on trains: 5
        Created train 7
        Created passenger at station 0 heading to 2
        Created passenger at station 2 heading to 4
        Created passenger at station 1 heading to 3
        Created passenger at station 4 heading to 3
        Created passenger at station 1 heading to 2
        Train 6 unloaded 1 passengers at station 1
                loaded 6 passengers; Space left 42
        Train 7 unloaded 0 passengers at station 0
                loaded 6 passengers; Space left 44

Time Marker 61   waiting: 149    on trains: 6
        Created passenger at station 0 heading to 2
        Created passenger at station 0 heading to 1
        Created passenger at station 3 heading to 1

Time Marker 62   waiting: 152    on trains: 10

Time Marker 63   waiting: 152    on trains: 14
        Created passenger at station 1 heading to 3
        Created passenger at station 0 heading to 4
        Created passenger at station 4 heading to 3
        Created passenger at station 4 heading to 1
        Created passenger at station 1 heading to 3

Time Marker 64   waiting: 157    on trains: 18
        Created passenger at station 1 heading to 0
        Created passenger at station 1 heading to 0
        Created passenger at station 4 heading to 2
        Created passenger at station 2 heading to 4

Time Marker 65   waiting: 161    on trains: 22
        Train 4 unloaded 6 passengers at station 4
                loaded 5 passengers; Space left 37
        Train 5 unloaded 1 passengers at station 3
                loaded 1 passengers; Space left 40

Time Marker 66   waiting: 161    on trains: 2

Time Marker 67   waiting: 161    on trains: 5
        Created passenger at station 1 heading to 2
        Created passenger at station 4 heading to 1
        Created passenger at station 4 heading to 0

Time Marker 68   waiting: 164    on trains: 8

Time Marker 69   waiting: 164    on trains: 11
        Created passenger at station 2 heading to 1
        Train 6 unloaded 5 passengers at station 2
                loaded 3 passengers; Space left 44

Time Marker 70   waiting: 165    on trains: -1
        Created train 8
        Created passenger at station 0 heading to 1
        Created passenger at station 2 heading to 1
        Train 7 unloaded 2 passengers at station 1
                loaded 5 passengers; Space left 41
        Train 8 unloaded 0 passengers at station 0
                loaded 4 passengers; Space left 46

Time Marker 71   waiting: 167    on trains: 4
        Created passenger at station 0 heading to 3
        Created passenger at station 2 heading to 4
        Created passenger at station 0 heading to 4

Time Marker 72   waiting: 170    on trains: 8
        Created passenger at station 2 heading to 1
        Created passenger at station 4 heading to 3
        Created passenger at station 3 heading to 1
        Created passenger at station 3 heading to 1

Time Marker 73   waiting: 174    on trains: 12

Time Marker 74   waiting: 174    on trains: 16
        Created passenger at station 4 heading to 0
        Created passenger at station 1 heading to 0
        Created passenger at station 0 heading to 1
        Created passenger at station 1 heading to 0
        Created passenger at station 0 heading to 4

Time Marker 75   waiting: 179    on trains: 20
        Created passenger at station 2 heading to 1
        Created passenger at station 3 heading to 2
        Created passenger at station 4 heading to 3
        Created passenger at station 3 heading to 4
        Created passenger at station 3 heading to 4
        Train 5 unloaded 3 passengers at station 4
                loaded 5 passengers; Space left 38
        Train 6 unloaded 2 passengers at station 3
                loaded 5 passengers; Space left 41

Time Marker 76   waiting: 184    on trains: 5
        Created passenger at station 1 heading to 0
        Created passenger at station 2 heading to 3

Time Marker 77   waiting: 186    on trains: 8
        Created passenger at station 3 heading to 0
        Created passenger at station 1 heading to 0
        Created passenger at station 1 heading to 0
        Created passenger at station 2 heading to 1

Time Marker 78   waiting: 190    on trains: 11
        Created passenger at station 4 heading to 2
        Created passenger at station 0 heading to 3

Time Marker 79   waiting: 192    on trains: 14
        Created passenger at station 3 heading to 2
        Created passenger at station 1 heading to 0
        Created passenger at station 2 heading to 3
        Created passenger at station 0 heading to 1
        Created passenger at station 0 heading to 3
        Train 7 unloaded 3 passengers at station 2
                loaded 7 passengers; Space left 37

Time Marker 80   waiting: 197    on trains: 5
        Created train 9
        Created passenger at station 0 heading to 2
        Created passenger at station 0 heading to 1
        Train 8 unloaded 2 passengers at station 1
                loaded 6 passengers; Space left 42
        Train 9 unloaded 0 passengers at station 0
                loaded 9 passengers; Space left 41

Time Marker 81   waiting: 199    on trains: 9
        Created passenger at station 3 heading to 2
        Created passenger at station 0 heading to 3
        Created passenger at station 1 heading to 2
        Created passenger at station 1 heading to 0

Time Marker 82   waiting: 203    on trains: 13

Time Marker 83   waiting: 203    on trains: 17
        Created passenger at station 0 heading to 2
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 4
        Created passenger at station 3 heading to 4
        Created passenger at station 4 heading to 3

Time Marker 84   waiting: 208    on trains: 21
        Created passenger at station 1 heading to 2
        Created passenger at station 0 heading to 4
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 3

Time Marker 85   waiting: 213    on trains: 25
        Created passenger at station 0 heading to 2
        Created passenger at station 4 heading to 0
        Created passenger at station 1 heading to 3
        Created passenger at station 1 heading to 0
        Train 6 unloaded 4 passengers at station 4
                loaded 3 passengers; Space left 42
        Train 7 unloaded 4 passengers at station 3
                loaded 4 passengers; Space left 37

Time Marker 86   waiting: 217    on trains: 2
        Created passenger at station 2 heading to 1
        Created passenger at station 0 heading to 1
        Created passenger at station 0 heading to 2
        Created passenger at station 3 heading to 0

Time Marker 87   waiting: 221    on trains: 5
        Created passenger at station 0 heading to 3
        Created passenger at station 4 heading to 3

Time Marker 88   waiting: 223    on trains: 8

Time Marker 89   waiting: 223    on trains: 11
        Created passenger at station 0 heading to 3
        Created passenger at station 1 heading to 0
        Train 8 unloaded 1 passengers at station 2
                loaded 6 passengers; Space left 37

Time Marker 90   waiting: 225    on trains: 6
        Created train 10
        Created passenger at station 0 heading to 2
        Created passenger at station 3 heading to 0
        Created passenger at station 0 heading to 1
        Train 9 unloaded 3 passengers at station 1
                loaded 6 passengers; Space left 38
        Train 10 unloaded 0 passengers at station 0
                loaded 10 passengers; Space left 40

Time Marker 91   waiting: 228    on trains: 10
        Created passenger at station 1 heading to 0
        Created passenger at station 0 heading to 1
        Created passenger at station 0 heading to 1
        Created passenger at station 1 heading to 0

Time Marker 92   waiting: 232    on trains: 14
        Created passenger at station 3 heading to 1

Time Marker 93   waiting: 233    on trains: 18

Time Marker 94   waiting: 233    on trains: 22
        Created passenger at station 2 heading to 1
        Created passenger at station 1 heading to 0
        Created passenger at station 4 heading to 1

Time Marker 95   waiting: 236    on trains: 26
        Train 7 unloaded 4 passengers at station 4
                loaded 2 passengers; Space left 39
        Train 8 unloaded 1 passengers at station 3
                loaded 3 passengers; Space left 35

Time Marker 96   waiting: 236    on trains: 4
        Created passenger at station 4 heading to 3

Time Marker 97   waiting: 237    on trains: 7

Time Marker 98   waiting: 237    on trains: 10
        Created passenger at station 0 heading to 4
        Created passenger at station 1 heading to 4
        Created passenger at station 1 heading to 2
        Created passenger at station 4 heading to 0

Time Marker 99   waiting: 241    on trains: 13
        Train 9 unloaded 3 passengers at station 2
                loaded 1 passengers; Space left 40

Time Marker 100  waiting: 241    on trains: -1
        Created train 11
        Created passenger at station 4 heading to 1
        Train 10 unloaded 2 passengers at station 1
                loaded 5 passengers; Space left 37
        Train 11 unloaded 0 passengers at station 0
                loaded 3 passengers; Space left 47

Time Marker 101  waiting: 242    on trains: 3
        Created passenger at station 4 heading to 3
        Created passenger at station 0 heading to 2
        Created passenger at station 1 heading to 3
        Created passenger at station 1 heading to 2
        Created passenger at station 0 heading to 4

Time Marker 102  waiting: 247    on trains: 7
        Created passenger at station 1 heading to 2

Time Marker 103  waiting: 248    on trains: 11
        Created passenger at station 4 heading to 1
        Created passenger at station 1 heading to 2

Time Marker 104  waiting: 250    on trains: 15
        Created passenger at station 4 heading to 1
        Created passenger at station 4 heading to 3

Time Marker 105  waiting: 252    on trains: 19
        Created passenger at station 0 heading to 1
        Created passenger at station 1 heading to 2
        Train 8 unloaded 2 passengers at station 4
                loaded 7 passengers; Space left 30
        Train 9 unloaded 4 passengers at station 3
                loaded 0 passengers; Space left 44

Time Marker 106  waiting: 254    on trains: -2
        Created passenger at station 0 heading to 4

Time Marker 107  waiting: 255    on trains: 1
        Created passenger at station 0 heading to 4
        Created passenger at station 4 heading to 1
        Created passenger at station 1 heading to 4

Time Marker 108  waiting: 258    on trains: 4
        Created passenger at station 0 heading to 1
        Created passenger at station 3 heading to 4
        Created passenger at station 1 heading to 0

Time Marker 109  waiting: 261    on trains: 7
        Created passenger at station 2 heading to 0
        Created passenger at station 1 heading to 2
        Created passenger at station 3 heading to 4
        Created passenger at station 2 heading to 0
        Created passenger at station 4 heading to 2
        Train 10 unloaded 5 passengers at station 2
                loaded 2 passengers; Space left 40

Time Marker 110  waiting: 266    on trains: -2
        Created train 12
        Train 11 unloaded 2 passengers at station 1
                loaded 8 passengers; Space left 41
        Train 12 unloaded 0 passengers at station 0
                loaded 6 passengers; Space left 44

Time Marker 111  waiting: 266    on trains: 6
        Created passenger at station 0 heading to 3

Time Marker 112  waiting: 267    on trains: 10

Time Marker 113  waiting: 267    on trains: 14
        Created passenger at station 4 heading to 2
        Created passenger at station 0 heading to 2

Time Marker 114  waiting: 269    on trains: 18

Time Marker 115  waiting: 269    on trains: 22
        Created passenger at station 4 heading to 1
        Created passenger at station 0 heading to 1
        Train 9 unloaded 2 passengers at station 4
                loaded 4 passengers; Space left 42
        Train 10 unloaded 3 passengers at station 3
                loaded 2 passengers; Space left 41

Time Marker 116  waiting: 271    on trains: 1
        Created passenger at station 3 heading to 2

Time Marker 117  waiting: 272    on trains: 4
        Created passenger at station 2 heading to 4
        Created passenger at station 2 heading to 4

Time Marker 118  waiting: 274    on trains: 7
        Created passenger at station 3 heading to 0
        Created passenger at station 4 heading to 0
        Created passenger at station 1 heading to 4

Time Marker 119  waiting: 277    on trains: 10
        Created passenger at station 3 heading to 4
        Created passenger at station 1 heading to 0
        Created passenger at station 1 heading to 2
        Created passenger at station 1 heading to 3
        Created passenger at station 3 heading to 0
        Train 11 unloaded 5 passengers at station 2
                loaded 2 passengers; Space left 44

Time Marker 120  waiting: 282    on trains: -2
        Created train 13
        Train 12 unloaded 2 passengers at station 1
                loaded 4 passengers; Space left 42
        Train 13 unloaded 0 passengers at station 0
                loaded 3 passengers; Space left 47

Time Marker 121  waiting: 282    on trains: 3
        Created passenger at station 0 heading to 2
        Created passenger at station 2 heading to 1

Time Marker 122  waiting: 284    on trains: 7
        Created passenger at station 0 heading to 4
        Created passenger at station 4 heading to 0

Time Marker 123  waiting: 286    on trains: 11

Time Marker 124  waiting: 286    on trains: 15

Time Marker 125  waiting: 286    on trains: 19
        Train 10 unloaded 4 passengers at station 4
                loaded 2 passengers; Space left 43
        Train 11 unloaded 1 passengers at station 3
                loaded 4 passengers; Space left 41

Time Marker 126  waiting: 286    on trains: 5
        Created passenger at station 3 heading to 2
        Created passenger at station 0 heading to 1
        Created passenger at station 3 heading to 4
        Created passenger at station 4 heading to 0

Time Marker 127  waiting: 290    on trains: 8
        Created passenger at station 2 heading to 1
        Created passenger at station 0 heading to 2
        Created passenger at station 2 heading to 3
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 1

Time Marker 128  waiting: 295    on trains: 11

Time Marker 129  waiting: 295    on trains: 14
        Created passenger at station 1 heading to 2
        Created passenger at station 0 heading to 1
        Created passenger at station 0 heading to 2
        Created passenger at station 0 heading to 1
        Train 12 unloaded 2 passengers at station 2
                loaded 5 passengers; Space left 39

Time Marker 130  waiting: 299    on trains: 4
        Created train 14
        Created passenger at station 0 heading to 1
        Created passenger at station 4 heading to 1
        Train 13 unloaded 1 passengers at station 1
                loaded 1 passengers; Space left 47
        Train 14 unloaded 0 passengers at station 0
                loaded 8 passengers; Space left 42

Time Marker 131  waiting: 301    on trains: 8
        Created passenger at station 2 heading to 4
        Created passenger at station 4 heading to 2
        Created passenger at station 0 heading to 1
        Created passenger at station 3 heading to 0

Time Marker 132  waiting: 305    on trains: 12
        Created passenger at station 3 heading to 4
        Created passenger at station 4 heading to 3

Time Marker 133  waiting: 307    on trains: 16
        Created passenger at station 1 heading to 0
        Created passenger at station 3 heading to 4

Time Marker 134  waiting: 309    on trains: 20
        Created passenger at station 4 heading to 1

Time Marker 135  waiting: 310    on trains: 24
        Created passenger at station 2 heading to 4
        Created passenger at station 0 heading to 4
        Created passenger at station 3 heading to 4
        Created passenger at station 2 heading to 3
        Created passenger at station 4 heading to 0
        Train 11 unloaded 5 passengers at station 4
                loaded 6 passengers; Space left 40
        Train 12 unloaded 2 passengers at station 3
                loaded 6 passengers; Space left 35

Time Marker 136  waiting: 315    on trains: 6
        Created passenger at station 0 heading to 4
        Created passenger at station 0 heading to 3

Time Marker 137  waiting: 317    on trains: 9
        Created passenger at station 0 heading to 1

Time Marker 138  waiting: 318    on trains: 12

Time Marker 139  waiting: 318    on trains: 15
        Train 13 unloaded 2 passengers at station 2
                loaded 3 passengers; Space left 46

Time Marker 140  waiting: 318    on trains: 2
        Created train 15
        Created passenger at station 2 heading to 0
        Created passenger at station 3 heading to 4
        Created passenger at station 0 heading to 2
        Train 14 unloaded 4 passengers at station 1
                loaded 1 passengers; Space left 45
        Train 15 unloaded 0 passengers at station 0
                loaded 6 passengers; Space left 44

Time Marker 141  waiting: 321    on trains: 6
        Created passenger at station 2 heading to 1

Time Marker 142  waiting: 322    on trains: 10
        Created passenger at station 4 heading to 1
        Created passenger at station 1 heading to 2
        Created passenger at station 1 heading to 3
        Created passenger at station 1 heading to 2

Time Marker 143  waiting: 326    on trains: 14

Time Marker 144  waiting: 326    on trains: 18
        Created passenger at station 4 heading to 3

Time Marker 145  waiting: 327    on trains: 22
        Created passenger at station 1 heading to 2
        Created passenger at station 4 heading to 0
        Created passenger at station 3 heading to 4
        Created passenger at station 3 heading to 4
        Created passenger at station 2 heading to 0
        Train 12 unloaded 8 passengers at station 4
                loaded 3 passengers; Space left 40
        Train 13 unloaded 2 passengers at station 3
                loaded 3 passengers; Space left 45

Time Marker 146  waiting: 332    on trains: 3

Time Marker 147  waiting: 332    on trains: 6
        Created passenger at station 1 heading to 2

Time Marker 148  waiting: 333    on trains: 9
        Created passenger at station 2 heading to 3

Time Marker 149  waiting: 334    on trains: 12
        Created passenger at station 4 heading to 3
        Train 14 unloaded 3 passengers at station 2
                loaded 4 passengers; Space left 44

Time Marker 150  waiting: 335    on trains: 2
        Created train 16
        Created passenger at station 1 heading to 2
        Created passenger at station 2 heading to 3
        Created passenger at station 0 heading to 4
        Created passenger at station 0 heading to 1
        Created passenger at station 0 heading to 2
        Train 15 unloaded 2 passengers at station 1
                loaded 6 passengers; Space left 40
        Train 16 unloaded 0 passengers at station 0
                loaded 3 passengers; Space left 47

Time Marker 151  waiting: 340    on trains: 3
        Created passenger at station 1 heading to 2
        Created passenger at station 0 heading to 3
        Created passenger at station 2 heading to 1

Time Marker 152  waiting: 343    on trains: 7

Time Marker 153  waiting: 343    on trains: 11

Time Marker 154  waiting: 343    on trains: 15

Time Marker 155  waiting: 343    on trains: 19
        Created passenger at station 2 heading to 4
        Created passenger at station 0 heading to 2
        Created passenger at station 4 heading to 1
        Created passenger at station 2 heading to 1
        Created passenger at station 0 heading to 2
        Train 13 unloaded 5 passengers at station 4
                loaded 2 passengers; Space left 48
        Train 14 unloaded 1 passengers at station 3
                loaded 0 passengers; Space left 45

Time Marker 156  waiting: 348    on trains: 1
        Created passenger at station 0 heading to 2
        Created passenger at station 0 heading to 1
        Created passenger at station 3 heading to 1

Time Marker 157  waiting: 351    on trains: 4
        Created passenger at station 0 heading to 1
        Created passenger at station 2 heading to 3
        Created passenger at station 3 heading to 4

Time Marker 158  waiting: 354    on trains: 7

Time Marker 159  waiting: 354    on trains: 10
        Created passenger at station 3 heading to 0
        Created passenger at station 2 heading to 4
        Created passenger at station 3 heading to 0
        Train 15 unloaded 6 passengers at station 2
                loaded 6 passengers; Space left 40

Time Marker 160  waiting: 357    on trains: 1
        Created train 17
        Created passenger at station 0 heading to 1
        Created passenger at station 1 heading to 4
        Train 16 unloaded 1 passengers at station 1
                loaded 2 passengers; Space left 46
        Train 17 unloaded 0 passengers at station 0
                loaded 7 passengers; Space left 43

Time Marker 161  waiting: 359    on trains: 7
        Created passenger at station 0 heading to 4
        Created passenger at station 1 heading to 4
        Created passenger at station 0 heading to 1

Time Marker 162  waiting: 362    on trains: 11
        Created passenger at station 1 heading to 3
        Created passenger at station 2 heading to 4

Time Marker 163  waiting: 364    on trains: 15
        Created passenger at station 3 heading to 0
        Created passenger at station 2 heading to 3

Time Marker 164  waiting: 366    on trains: 19
        Created passenger at station 1 heading to 3
        Created passenger at station 3 heading to 0
        Created passenger at station 0 heading to 2

Time Marker 165  waiting: 369    on trains: 23
        Created passenger at station 1 heading to 3
        Created passenger at station 2 heading to 3
        Train 14 unloaded 1 passengers at station 4
                loaded 0 passengers; Space left 46
        Train 15 unloaded 4 passengers at station 3
                loaded 6 passengers; Space left 38

Time Marker 166  waiting: 371    on trains: 4
        Created passenger at station 1 heading to 2
        Created passenger at station 1 heading to 4

Time Marker 167  waiting: 373    on trains: 7
        Created passenger at station 2 heading to 0

Time Marker 168  waiting: 374    on trains: 10
        Created passenger at station 4 heading to 0
        Created passenger at station 0 heading to 1

Time Marker 169  waiting: 376    on trains: 13
        Created passenger at station 4 heading to 2
        Created passenger at station 2 heading to 1
        Created passenger at station 4 heading to 3
        Created passenger at station 4 heading to 3
        Created passenger at station 3 heading to 4
        Train 16 unloaded 2 passengers at station 2
                loaded 5 passengers; Space left 43

Time Marker 170  waiting: 381    on trains: 4
        Created train 18
        Created passenger at station 3 heading to 0
        Created passenger at station 0 heading to 1
        Created passenger at station 3 heading to 1
        Train 17 unloaded 3 passengers at station 1
                loaded 6 passengers; Space left 40
        Train 18 unloaded 0 passengers at station 0
                loaded 5 passengers; Space left 45

Time Marker 171  waiting: 384    on trains: 5
        Created passenger at station 2 heading to 0
        Created passenger at station 4 heading to 3
        Created passenger at station 4 heading to 2
        Created passenger at station 3 heading to 1

Time Marker 172  waiting: 388    on trains: 9
        Created passenger at station 3 heading to 0
        Created passenger at station 0 heading to 2
        Created passenger at station 2 heading to 1
        Created passenger at station 2 heading to 4

Time Marker 173  waiting: 392    on trains: 13
        Created passenger at station 1 heading to 2

Time Marker 174  waiting: 393    on trains: 17
        Created passenger at station 4 heading to 0
        Created passenger at station 0 heading to 4
        Created passenger at station 2 heading to 3

Time Marker 175  waiting: 396    on trains: 21
        Train 15 unloaded 5 passengers at station 4
                loaded 7 passengers; Space left 36
        Train 16 unloaded 2 passengers at station 3
                loaded 5 passengers; Space left 40

Time Marker 176  waiting: 396    on trains: 5
        Created passenger at station 0 heading to 2
        Created passenger at station 0 heading to 4
        Created passenger at station 3 heading to 4
        Created passenger at station 2 heading to 0

Time Marker 177  waiting: 400    on trains: 8
        Created passenger at station 1 heading to 0
        Created passenger at station 0 heading to 2
        Created passenger at station 4 heading to 1
        Created passenger at station 0 heading to 1

Time Marker 178  waiting: 404    on trains: 11
        Created passenger at station 4 heading to 3

Time Marker 179  waiting: 405    on trains: 14
        Created passenger at station 3 heading to 4
        Created passenger at station 1 heading to 0
        Created passenger at station 2 heading to 3
        Created passenger at station 4 heading to 1
        Train 17 unloaded 4 passengers at station 2
                loaded 6 passengers; Space left 38

Time Marker 180  waiting: 409    on trains: 3
        Created train 19
        Created passenger at station 2 heading to 4
        Train 18 unloaded 3 passengers at station 1
                loaded 3 passengers; Space left 45
        Train 19 unloaded 0 passengers at station 0
                loaded 6 passengers; Space left 44

Time Marker 181  waiting: 410    on trains: 6
        Created passenger at station 1 heading to 0

Time Marker 182  waiting: 411    on trains: 10
        Created passenger at station 0 heading to 3

Time Marker 183  waiting: 412    on trains: 14
        Created passenger at station 1 heading to 0
        Created passenger at station 0 heading to 1
        Created passenger at station 4 heading to 2
        Created passenger at station 0 heading to 1

Time Marker 184  waiting: 416    on trains: 18

Time Marker 185  waiting: 416    on trains: 22
        Created passenger at station 4 heading to 0
        Created passenger at station 4 heading to 1
        Created passenger at station 2 heading to 1
        Train 16 unloaded 4 passengers at station 4
                loaded 6 passengers; Space left 38
        Train 17 unloaded 6 passengers at station 3
                loaded 2 passengers; Space left 42

Time Marker 186  waiting: 419    on trains: -2

Time Marker 187  waiting: 419    on trains: 1
        Created passenger at station 3 heading to 1

Time Marker 188  waiting: 420    on trains: 4
        Created passenger at station 3 heading to 2
        Created passenger at station 3 heading to 0
        Created passenger at station 2 heading to 3
        Created passenger at station 2 heading to 4

Time Marker 189  waiting: 424    on trains: 7
        Created passenger at station 0 heading to 1
        Created passenger at station 3 heading to 1
        Created passenger at station 1 heading to 2
        Train 18 unloaded 2 passengers at station 2
                loaded 4 passengers; Space left 43

Time Marker 190  waiting: 427    on trains: 3
        Created train 20
        Created passenger at station 4 heading to 3
        Created passenger at station 3 heading to 1
        Created passenger at station 2 heading to 0
        Created passenger at station 0 heading to 3
        Created passenger at station 2 heading to 1
        Train 19 unloaded 1 passengers at station 1
                loaded 3 passengers; Space left 42
        Train 20 unloaded 0 passengers at station 0
                loaded 5 passengers; Space left 45

Time Marker 191  waiting: 432    on trains: 5

Time Marker 192  waiting: 432    on trains: 9
        Created passenger at station 3 heading to 0
        Created passenger at station 3 heading to 2
        Created passenger at station 2 heading to 0
        Created passenger at station 0 heading to 3

Time Marker 193  waiting: 436    on trains: 13
        Created passenger at station 3 heading to 4
        Created passenger at station 0 heading to 4
        Created passenger at station 1 heading to 4
        Created passenger at station 3 heading to 2
        Created passenger at station 2 heading to 1

Time Marker 194  waiting: 441    on trains: 17
        Created passenger at station 1 heading to 2
        Created passenger at station 3 heading to 4
        Created passenger at station 0 heading to 1
        Created passenger at station 4 heading to 0

Time Marker 195  waiting: 445    on trains: 21
        Train 17 unloaded 5 passengers at station 4
                loaded 2 passengers; Space left 45
        Train 18 unloaded 1 passengers at station 3
                loaded 10 passengers; Space left 34

Time Marker 196  waiting: 445    on trains: 11
        Created passenger at station 2 heading to 0
        Created passenger at station 0 heading to 1

Time Marker 197  waiting: 447    on trains: 14

Time Marker 198  waiting: 447    on trains: 17
        Created passenger at station 2 heading to 4
        Created passenger at station 3 heading to 2

Time Marker 199  waiting: 449    on trains: 20
        Train 19 unloaded 4 passengers at station 2
                loaded 6 passengers; Space left 40

Time Marker 200  waiting: 449    on trains: 3
        Created train 21
        Created passenger at station 0 heading to 1
        Created passenger at station 4 heading to 0
        Created passenger at station 0 heading to 3
        Created passenger at station 2 heading to 3
        Created passenger at station 4 heading to 1
        Train 20 unloaded 3 passengers at station 1
                loaded 2 passengers; Space left 46
        Train 21 unloaded 0 passengers at station 0
                loaded 6 passengers; Space left 44
Final Report
The total number of passengers is 454
The number of passengers currently on a train 6
The number of passengers delivered is 214
The average wait time for passengers that have boarded is
204.24545454545455
The average wait time for passengers that have not yet boarded is
3.3632478632478633
 */
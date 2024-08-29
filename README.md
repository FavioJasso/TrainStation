# TrainStation

<h2> Description </h2>

This project involves developing a program that simulates a train route. The route consists of several stations, starting at a specific station and ending at another. The program will account for the time required for a train to travel between consecutive stations along the route. Each station will have an associated queue of passengers, who are generated at random times, assigned randomly to entry stations, and given random destination stations.

Trains will depart from stations at regular intervals and travel along the route, stopping at each station. At each stop, passengers whose destination matches the current station will disembark. Afterward, passengers waiting in the station's queue will board the train, continuing until either the queue is empty or the train reaches its full capacity. Each train will manage a stack of passengers that tracks who needs to exit at each upcoming station.

/**
   A class that represents a train in a simulation.

   @author Charles Hoot 
   @version STACK
 */
public class Train
{
	private StackInterface<Passenger> onTrain;
	private int numberOnTrain;
	private int capacity;
	private int nextStation;
	private int timeToNextStation;
	private int trainNo;
	private static int trainsCreated = 0;

	public Train(int cap)
	{
		onTrain = new ArrayStack<>();
		numberOnTrain = 0;
		capacity = cap;

		// Will arrive at the first station in 1 minute
		nextStation = 0;
		timeToNextStation = 1;

		trainsCreated++;
		trainNo = trainsCreated;
		System.out.println("\tCreated train " + trainNo);
	} // end constructor

	/** Returns the next station in the sequence.
		@return  The next station. */
	public int nextStation()
	{
		return nextStation;
	} // end nextStation

	/** Returns the time until the next station is reached.
		@return  The time to the next station. */
	public int timeToNext()
	{
		return timeToNextStation;
	} // end timeToNext

	/** "Moves" the train by decreasing the time to the next station by 1. */
	public void move()
	{
		timeToNextStation--;
	} // end move

	/** Updates the next station and updates the time until the next
		station is reached to the given value.
		@param timeToNext  The new time until the next station. */
	public void updateStation(int timeToNext)
	{
		timeToNextStation = timeToNext;
		nextStation++;
	} // end updateStation

	/** Unloads all passengers on the train on a given station.
		@param station  The station stopped at.
		@return  The number of passengers leaving. */
	public int unloadPassengers(int station)
	{
		// temporary stack to hold passengers remaining on train to process properly
		StackInterface<Passenger> riders = new ArrayStack<Passenger> (); 
		int count = numberOnTrain;
		for(int i=0; i < count; i++)
		{
			Passenger person = onTrain.pop();
			if(person.getDestination() != station)
				riders.push(person);	// Not there yet, put them in temporary stack
			else
				numberOnTrain--;     // They arrived
		} // end for
		onTrain = riders; // put stack of passengers left back on train
		int passengersLeaving = count - numberOnTrain;
		System.out.println("\tTrain " + trainNo + " unloaded " 
				+ passengersLeaving + " passengers at station " + station);
		return passengersLeaving;
	} // end unloadPassengers

	/** Loads all passengers at a given station at a given time onto the train.
		@param station  The station that the train is at.
		@param clock    The time of the loading.
		@return  The number of passengers entering. */
	public int loadPassengers(Station station, int clock)
	{
		int count = numberOnTrain;
		boolean passengerWaiting = station.isWaiting();

		while ((numberOnTrain < capacity) && passengerWaiting)
		{
			Passenger boarder = station.getPassenger();
			onTrain.push(boarder);
			boarder.boardTrain(clock);
			passengerWaiting = station.isWaiting();
			numberOnTrain++;
		} // end while

		int passengersEntering = numberOnTrain - count;
		System.out.print("\t\tloaded " + passengersEntering + " passengers");
		System.out.println("; Space left " + (capacity - numberOnTrain) );

		return passengersEntering;
	} // end loadPassengers
} // end Train

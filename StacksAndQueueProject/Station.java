/**
   A class that represents a train station where passengers will wait.

   @author Charles Hoot 
   @version 4.0
 */
public class Station
{
	private QueueInterface<Passenger> waiting;
	private int timeToNextStation;

	public Station(int timeToNext)
	{
		waiting = new LinkedQueue<>();
		timeToNextStation = timeToNext;
	} // end constructor

	/** Adds a passenger to the station.
		@param rider  The passenger to add. */
	public void addPassenger(Passenger rider)
	{
		waiting.enqueue(rider);
	} // end addPassenger

	/** Determines whether or not there are people waiting at the station.
		@return  True if there are people waiting, or false if not.  */
	public boolean isWaiting()
	{
		return !waiting.isEmpty();
	} // end isWaiting

	/** Returns the first passenger in the queue of people waiting.
		@return  The first passenger in the queue. */
	public Passenger getPassenger()
	{
		return waiting.dequeue();
	} // end getPassenger

	/** Returns the time it takes to reach the next station.
		@return  The time to the next station. */
	public int getTimeToNextStation()
	{
		return timeToNextStation;
	} // end getTimeToNextStation
} // end Station

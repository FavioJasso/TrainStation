/**
   A class that represents a train passenger.

   @author Charles Hoot 
   @version 4.0
 */
public class Passenger
{
	private int startedWaiting;
	private int boardedAt;
	private boolean boardedTrain;
	private int destination;

	public Passenger(int createdAt, int dest)
	{
		startedWaiting = createdAt;
		destination = dest;
		boardedTrain = false;
		System.out.println("\tCreated passenger at station " +
				createdAt + " heading to " + dest);
	} // end constructor

	/** Returns the destination of the passenger.
		@return  The destination of the passenger. */
	public int getDestination()
	{
		return destination;
	} // end getDestination

	/** Boards the train at the specified time.
		@param clock  The time the passenger boards. */
	public void boardTrain(int clock)
	{
		boardedAt = clock;
		boardedTrain = true;
	} // end boardTrain

	/** Determines the amount of time the passenger has spent waiting.
		@param clock  The current time.
		@return  The wait time of the passenger. */
	public int waitTime(int clock)
	{
		int result = clock - startedWaiting;
		if(boardedTrain)
			result = boardedAt - startedWaiting;

		return result;
	} // end waitTime

	/** Determines whether or not the passenger has boarded the train.
		@return  True if the passenger has boarded, or false if not. */
	public boolean boarded()
	{
		return boardedTrain;
	} // end boardedTrain
} // end Passenger

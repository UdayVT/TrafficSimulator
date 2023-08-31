/**
 * An abstract data type meant to serve as a representation of a vehicle
 * containing its SerialID and timeStep it arrived. This data type is immutable
 * therefore changes cannot be made to these values once initialized. The class also
 * contains a static int variable called Serial Counter which works as counter for vehicles.
 * 
 * @author 
 * 		Uday Turakhia, SBU ID #: 115102637
 * <dt><b>Assignment:</b><dd>
 *    Homework #4 for CSE 214, Spring 2023
 * 		Recitation #: R03
 * <dt><b>Date:</b><dd>
 *    March 8th, 2023
 */
public class Vehicle 
{
    public static int serialCounter = 0;//Static serialCounter, also works as cars counter
    private int serialId;//Serial Id of the car
    private int timeArrived;// The time step, the car arrived in the queue.

    /**
     * Default Constructor
     * @param initTimeArrived
     *      Time the vehicle arrived at the intersection.
     * @custom.Precondition
     *      initTimeArrived > 0.
     * @throws IllegalArgumentException
     *      If initTimeArrived ≤ 0
     */
    public Vehicle(int initTimeArrived)throws IllegalArgumentException
    {
        if(initTimeArrived<=0)
            throw new IllegalArgumentException();
        
        serialCounter++;
        serialId = serialCounter;
        timeArrived = initTimeArrived;
    }

    /**
     * Acessor of Serial ID
     * @return
     *      The Serial Id of the car
     */
    public int getSerialId() 
    {
        return serialId;
    }

    /**
     * Acessor of Time Step the car arrived 
     * @return
     *      The time step the car arrived
     */
    public int getTimeArrived() 
    {
        return timeArrived;
    }

    /**
     * Returns the serial counter of the Vehicle class
     * @return
     *      the serial counter of the vehicle
     */
    public static int getSerialCounter() 
    {
        return serialCounter;
    }
}

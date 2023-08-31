/**
 * An abstract data type meant to serve as a representation of a Many TwoWayRoads
 * containing 6 lanes being 2 ways to go(forward and backward) and 3 different lanes which are (Left, Middle, and Right). 
 * Each Intersection holds roads in an array and maximum number of road it can have is 4.
 * This class will control countdown timer for each road and ask TwoWayRoad to change light thru proceed. 
 * It will also enqueue vehicle to the specific road, lanes, and way. It also changes to different road as per countdown and timestep. 
 * @author 
 * 		Uday Turakhia, SBU ID #: 115102637
 * <dt><b>Assignment:</b><dd>
 *    Homework #4 for CSE 214, Spring 2023
 * 		Recitation #: R03
 * <dt><b>Date:</b><dd>
 *    March 21th, 2023
 */
public class Intersection {
    
    private TwoWayRoad[] roads;//Array of roads which cross at this intersection.
    private int lightIndex;//Indicates the road in roads with the active light (either green or left turn signal).
    private int countdownTimer;//Tracks the remaining time steps available for the road currently indicated by lightIndex.
    public final int MAX_ROADS = Integer.MAX_VALUE;//Maximum amount of roads

    /**
     * Constructor which initializes the roads array.
     * @param initRoads
     *      Array of roads to be used by this intersection.
     * @custom.Precondition
     *      initRoads is not null.
     *      Length of initRoadsis less than or equal to MAX_ROADS.
     *      All indices of initRoads are not null.
     * @custom.Postcondition
     *      This object has been initialized to a Intersection object managing the roads array.
     * @throws IllegalArgumentException
     *      If initRoads is null.
     *      If any index of initRoads is null.
     *      initRoads.length > MAX_ROADS.
     */
    public Intersection(TwoWayRoad[] initRoads) throws IllegalArgumentException
    {
        //Precondition check 1
        if(initRoads == null)
            throw new IllegalArgumentException("If initRoads is null.");
        
        //Precondition check 2
        if(initRoads.length > MAX_ROADS)
            throw new IllegalArgumentException("initRoads.length > MAX_ROADS.");
        
        //Preconditon check 3
        for(int i = 0; i<initRoads.length;i++)
        {
            if(initRoads[i] == null)
                throw new IllegalArgumentException("If any index of initRoads is null.");
        }

        int lightIndex = 0;
        roads = initRoads;
        roads[lightIndex].setLighttoGreen();
        countdownTimer =  roads[lightIndex].getGreenTime();
    }

    /**
     * Performs a single iteration through the intersection
     * @custom.Postcondition
     *      The intersection has dequeued all lanes with a green light (if non-empty) and returned an array containing the Vehicles.
     * @return 
     *      An array of Vehicles which have passed though the intersection during this time step.
     */
    public Vehicle[] timeStep(boolean Overtimer) 
    {
        //if timeStep>SimulationTime and the road is empty, it will switch to another road
        while(Overtimer && roads[lightIndex].isEmpty())
        {
            lightIndex = (lightIndex+1)%roads.length;
            countdownTimer = roads[lightIndex].getGreenTime();
        }
        //gets passed vehicle from procced and then change countdown and road if countdown=0. 
        Vehicle[] passed = roads[lightIndex].proceed(countdownTimer--,Overtimer);
        if(countdownTimer == 0)
        {
            lightIndex = (lightIndex+1)%roads.length;
            countdownTimer = roads[lightIndex].getGreenTime();
        }
        return passed;
    }

    /**
     * Enqueues a vehicle onto a lane in the intersection.
     * @param roadIndex
     *      Index of the road in roads which contains the lane to enqueue onto.
     * @param wayIndex
     *      Index of the direction the vehicle is headed. Can either be TwoWayRoad.FORWARD or TwoWayRoad.BACKWARD
     * @param laneIndex
     *      Index of the lane on which the vehicle is to be enqueue. Can either be TwoWayRoad.RIGHT_LANE, TwoWayRoad.MIDDLE_LANE, or TwoWayRoad.LEFT_LANE.
     * @param vehicle
     *      The Vehicle to enqueue onto the lane.
     * @custom.Precondition
     *      0 ≤ roadIndex < roads.length.
     *      0 ≤ wayIndex < TwoWayRoad.NUM_WAYS.
     *      0 ≤ laneIndex < TwoWayRoad.NUM_LANES.
     *      vehicle != null.
     * @throws IllegalArgumentException
     *      If vehicle is null.
     *      If any of the index parameters above are not within the valid range.
     */
    public void enqueueVehicle(int roadIndex, int wayIndex, int laneIndex, Vehicle vehicle) throws IllegalArgumentException
    {
        //Precondition check 1
        if(vehicle == null)
            throw new IllegalArgumentException();
        //Precondition check 2
        if(roadIndex < 0 || roadIndex >= roads.length)
            throw new IllegalArgumentException();
        //Precondition check 3
        if(wayIndex < 0 || wayIndex >= TwoWayRoad.NUM_WAYS)
            throw new IllegalArgumentException();
        //Precondition check 4
        if(laneIndex < 0 || laneIndex >= TwoWayRoad.NUM_LANES)
            throw new IllegalArgumentException();

        roads[roadIndex].enqueueVehicle(wayIndex, laneIndex, vehicle);
    }

    /**
     * Prints the intersection to the terminal in a neatly formatted manner.
     */
    public void display()
    {
        for(int i = 0; i<roads.length;i++)
        {
            System.out.println(roads[i]);
        }
    }

    /**
     * Checks if all roads are empty
     * @return
     *      true if all roads are empty, false otherwise
     */
    public boolean isEmpty()
    {
        for(int i = 0; i<roads.length;i++)
        {
            if(!roads[i].isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the current lightIndex
     * @return
     *      current light index
     */
    public int getLightIndex() 
    {
        return lightIndex;
    }

    /**
     * Returns the countdownTimer
     * @return
     *      the countdown timer
     */
    public int getCountdownTimer() 
    {
        return countdownTimer;
    }

}

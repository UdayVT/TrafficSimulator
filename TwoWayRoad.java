/**
 * An abstract data type meant to serve as a representation of a Two Way Road
 * containing 6 lanes being 2 ways to go(forward and backward) and 3 different lanes which are (Left, Middle, and Right). 
 * This also has a traffic light with maximum green time and left signal time. It enqueues and deqeues vehicles for each lane. 
 *
 * @author 
 * 		Uday Turakhia, SBU ID #: 115102637
 * <dt><b>Assignment:</b><dd>
 *    Homework #4 for CSE 214, Spring 2023
 * 		Recitation #: R03
 * <dt><b>Date:</b><dd>
 *    March 20th, 2023
 */
public class TwoWayRoad 
{
    public static final int FORWARD_WAY = 0;//forward way of a lane
    public static final int BACKWARD_WAY = 1;//backward way of a lane
    public static final int NUM_WAYS = 2;//number of ways a car can go in a lane
    
    public static final int LEFT_LANE = 0;//left side of a road
    public static final int MIDDLE_LANE = 1;//middle side of a road
    public static final int RIGHT_LANE = 2;//right lane of a road
    public static final int NUM_LANES = 3;//number of lanes in a road

    private String name;//name of the lane
    private int greenTime;//the maximum total number of steps this road can be active
    private int leftSignalGreenTime;//The number of steps this road remains in the LEFT_SIGNAL state.
    boolean turnLeftSignalOn = false;//A boolean to change SignalLight to left Signal after displaying
    boolean turnRedLightOn = false;//A boolean to change SignalLight to Red after displaying

    private VehicleQueue[][] lanes;//All lanes as 2d array

    private LightValue lightValue = LightValue.RED;//Current light value of the road


    /** 
     * Default Constructor.
     * @param initName
     *      The name of the road.
     * @param initGreenTime
     *      The amount of time that the light will be active for this particular road.
     * @custom.Precondition
     *      initGreenTime > 0.
     * @custom.Postcondition
     *      This road is initialized with all lanes initialized to empty queues, and all instance variables initialized.
     * @throws IllegalArgumentException
     *      If initGreenTime ≤ 0 or initName=null.
     */
    public TwoWayRoad(String initName, int initGreenTime) throws IllegalArgumentException
    {
        //Precondition check
        if(greenTime<0 || initName == null)
            throw new IllegalArgumentException("HELLO");
        
        
        name = initName;
        greenTime = initGreenTime;
        double x = (1.0/NUM_LANES)*initGreenTime;
        leftSignalGreenTime = (Math.floor(x)!=0.0)? (int) Math.floor(x) : 1;

        //initializing every lane
        lanes = new VehicleQueue[NUM_WAYS][NUM_LANES];
        for(int i = 0; i<NUM_WAYS;i++)
        {
            for(int j = 0; j<NUM_LANES;j++)
            {
                lanes[i][j] = new VehicleQueue();
            }
        }
    }
    
    /**
     * Executes the passage of time in the simulation. The timerVal represents the current value of a 
     * countdown timer counting down total green time steps. 
     * The light should be in state GREEN any time the timerval is greater than leftSignalGreenTime.
     *  When timerVal is less than or equal to leftSignalGreenTime, the light should change to LEFT_SIGNAL. 
     * After the execution of timerVal == 1, or if there are no vehicles left the light should change state to RED.
     * @param timerVal
     *      The current timer value, determines the state of the light.
     * @custom.Precondition
     *      The TwoWayRoad object should be instantiated.
     * @return
     *      An array of Vehicles that has been dequeued during this time step.
     * @custom.Postcondition
     *      Any Vehicles that should have been dequeued during this time step should be dequeued and placed in the return array.
     * @throws IllegalArgumentException
     *      If timerval ≤ 0.
     */
    public Vehicle[] proceed(int timerVal, boolean OverTimer) throws IllegalArgumentException
    {
        //Precondition check
        if(timerVal <= 0)
            throw new IllegalArgumentException("Something went wrong");

        //If middle and right lanes were empty after timeStep>simulation Time
        if(OverTimer&&greenLanesEmpty())
            timerVal = leftSignalGreenTime;
        
        //If left lanes were empty after timeStep>simulation Time
        if(OverTimer&&leftLanesEmpty())
            timerVal = leftSignalGreenTime+1;

        //Changes traffic light and dequeues vehicle according to the current light. 
        Vehicle[] dequeue;
        if(timerVal > leftSignalGreenTime)
        {
            lightValue = LightValue.GREEN;
            dequeue = dequeueGreen();
            if(timerVal-1 <= leftSignalGreenTime)
            {
                turnLeftSignalOn = true;
            }
        }
        else
        {
            lightValue =  LightValue.LEFT_SIGNAL;
            if(timerVal == 1)
            {
                turnRedLightOn = true;
            }
            dequeue = dequeueLeftSignal();
        }

        return dequeue;
    }

    /**
     * Dequeues the vehicle when light is green, so does not dequeue
     * left lanes
     * @return
     *      An array of Vehicle which would be dequeued if the queues are not empty.
     */
    public Vehicle[] dequeueGreen()
    {
        Vehicle[] dequeue = new Vehicle[4];
        int index = 0;

        for(int i = 0; i<NUM_WAYS;i++)
        {
            for(int j = 0; j<NUM_LANES;j++)
            {
                if( (i==0 && j==0)||(i==1 && j==2))
                    continue;

                Vehicle v = lanes[i][j].dequeue();

                if(v!=null)
                {
                    dequeue[index++] = v;
                }
            }
        }
        return dequeue;
    }

    /**
     * Dequeues the vehicle when light is Left, so does not dequeue
     * from middle and right lanes
     * @return
     *      An array of Vehicle which would be dequeued if the queues are not empty.
     */
    public Vehicle[] dequeueLeftSignal()
    {
        Vehicle[] dequeue = new Vehicle[4];
        int index = 0;

        for(int i = 0; i<NUM_WAYS;i++)
        {
            for(int j = 0; j<NUM_LANES;j++)
            {
                if(!((i==0 && j==0)||(i==1 && j==2)))
                    continue;

                Vehicle v = lanes[i][j].dequeue();

                if(v!=null)
                {
                    dequeue[index++] = v;
                }
            }
        }
        return dequeue;
    }

    /**
     * Enqueues a vehicle into a the specified lane.
     * @param wayIndex
     *      The direction the car is going in.
     * @param laneIndex
     *      The lane the car arrives in.
     * @param vehicle
     *      The vehicle to enqueue; must not be null.
     * @custom.Precondition
     *      The TwoWayRoad object should be instantiated.
     * @custom.Postcondition 
     *      the vehicle should be added to the end of the proper queue.
     * @throws IllegalArgumentException
     *      if wrong input was givem or vehicle was null
     */
    public void enqueueVehicle(int wayIndex, int laneIndex, Vehicle vehicle) throws IllegalArgumentException
    {
        //Precondition check
        if(wayIndex>2 || wayIndex<0 || laneIndex > 3 || laneIndex < 0 || vehicle == null)
            throw new IllegalArgumentException("Wrong input");

        lanes[wayIndex][laneIndex].enqueue(vehicle);
    } 

    /**
     * Checks if a specified lane is empty.
     * @param wayIndex
     *      The direction of the lane.
     * @param laneIndex
     *      The index of the lane to check.
     * @custom.Precondition
     *      The TwoWayRoad object should be instantiated.
     * @return
     *      true if the lane is empty, else false.
     * @custom.Postcondition
     *      The TwoWayRoad object should remain unchanged
     * @throws IllegalArgumentException
     *      If wrong input is given
     */
    public boolean isLaneEmpty(int wayIndex, int laneIndex) throws IllegalArgumentException
    {
        //Precondition check
        if(wayIndex>2 || wayIndex<0 || laneIndex > 3 || laneIndex < 0 )
            throw new IllegalArgumentException("Wrong input");

        return lanes[wayIndex][laneIndex].isEmpty();
    }

    /**
     * A support method to print the TwoWayRoad in a good formated method.
     * @return
     *      The String of TwoWays Road in a good tabular format.
     */
    public String toString()
    {
        String answer = "";

        answer += name+":\n";
        answer += "                       FORWARD               BACKWARD\n";
        answer += "==============================               ===============================\n";
        answer += String.format("%30s [L] ", lanes[FORWARD_WAY][LEFT_LANE].ForwardToString());
        answer += (lightValue == LightValue.GREEN || lightValue == LightValue.RED)?"x  ":"   ";
        answer += (lightValue == LightValue.LEFT_SIGNAL || lightValue == LightValue.RED)?"x ":"  ";
        answer += "[R] "+lanes[BACKWARD_WAY][LEFT_LANE].BackwardToString()+"\n";
        answer += "------------------------------               -------------------------------\n";
        answer += String.format("%30s [M] ", lanes[FORWARD_WAY][MIDDLE_LANE].ForwardToString());
        answer += (lightValue == LightValue.LEFT_SIGNAL || lightValue == LightValue.RED)?"x  x ":"     ";
        answer += "[M] "+lanes[BACKWARD_WAY][MIDDLE_LANE].BackwardToString()+"\n";
        answer += "------------------------------               -------------------------------\n";
        answer += String.format("%30s [R] ", lanes[FORWARD_WAY][RIGHT_LANE].ForwardToString());
        answer += (lightValue == LightValue.LEFT_SIGNAL || lightValue == LightValue.RED)?"x  ":"   ";
        answer += (lightValue == LightValue.GREEN || lightValue == LightValue.RED)?"x ":"  ";
        answer += "[L] "+lanes[BACKWARD_WAY][RIGHT_LANE].BackwardToString()+"\n";
        answer += "==============================               ===============================\n";


        //Changes the trafficLight on if asked to changed
        if(turnRedLightOn)
        {
            lightValue = LightValue.RED;
            turnRedLightOn = false;
        }

        if(turnLeftSignalOn)
        {
            lightValue = LightValue.LEFT_SIGNAL;
            turnLeftSignalOn = false;
        }

        return answer;
    }

    /**
     * Returns the green time of the road
     * @return
     *      green time of the road
     */
    public int getGreenTime() 
    {
        return greenTime;
    }

    /**
     * Checks if all the lanes are empty or not in the road
     * @return
     *      true if all lanes are empty, false otherwise
     */
    public boolean isEmpty()
    {
        for(int i = 0;  i<NUM_WAYS;i++)
        {
            for(int j = 0; j<NUM_LANES;j++)
            {
                if(!lanes[i][j].isEmpty())
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns the current light value of 
     * @return
     *      The current lightValue of the Road
     */
    public LightValue getLightValue() 
    {
        return lightValue;
    }

    /**
     * Returns the name of the road
     * @return
     *      name of the road
     */
    public String getName() 
    {
        return name;
    }

    /**
     * Checks if the middle and right lanes are empty
     * @return
     *      true if all middle and right lanes are empty, false otherwise. 
     */
    public boolean greenLanesEmpty()
    {
        for(int i = 0; i<NUM_WAYS;i++)
        {
            for(int j = 0; j<NUM_LANES;j++)
            {
                if( (i==0 && j==0)||(i==1 && j==2))
                    continue;

                if(!lanes[i][j].isEmpty())
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks if the left lanes are empty
     * @return
     *      true if left lanes are empty, false otherwise.
     */
    public boolean leftLanesEmpty()
    {
        for(int i = 0; i<NUM_WAYS;i++)
        {
            for(int j = 0; j<NUM_LANES;j++)
            {
                if(!( (i==0 && j==0)||(i==1 && j==2)))
                    continue;

                if(!lanes[i][j].isEmpty())
                    return false;
            }
        }
        return true;
    }

    /**
     * Sets the lightValue to Green. 
     */
    public void setLighttoGreen()
    {
        lightValue = LightValue.GREEN;
    }
}
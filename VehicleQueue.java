/**
 * A queue data structure for Vehicle with common functionalities such as 
 * enqueue, dequeue, size, and isEmpty. 
 * 
 * @author 
 * 		Uday Turakhia, SBU ID #: 115102637
 * <dt><b>Assignment:</b><dd>
 *    Homework #4 for CSE 214, Spring 2023
 * 		Recitation #: R03
 * <dt><b>Date:</b><dd>
 *    March 11th, 2023
 */

 //CODE TAKEN FROM SLIDE
public class VehicleQueue
{
    public final int CAPACITY = 99999; //The maximum capacity of queue
    private Vehicle[] queue; //the queue itself
    private int front; //the front of the queue
    private int rear; //the rear of the queue
    private int size; //amount of vehicle in the queue
    
    /**
     * Default Constructor
     */
    public VehicleQueue()
    {
        front = -1;
        rear = -1;
        size = 0;
        queue = new Vehicle[CAPACITY];
    }

    /**
     * Checks if the queue is empty of not
     * @return
     *      boolean true, if queue is empty otherwise false;
     */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Enqueue a new vehicle to the queue
     * @param v
     *      the new vehicle
     */
    public void enqueue(Vehicle v)
    {
        if (front == -1) 
        {
            front = 0;
            rear = 0;
        }else 
            rear = (rear+1)%CAPACITY;

        size++;
        queue[rear] = v;
    }

    /**
     * Dequeues a vehicle from the queue
     * @return
     *      the vehicle that has been dequeued 
     */
    public Vehicle dequeue()
    {
        Vehicle v;

        //The queue is empty
        if(front == -1)
           return null;

        v = queue[front];
        //Only 1 element in the queue
        if(front == rear)
        {
            front = -1;
            rear = -1;
        }
        else
            front = (front+1)%CAPACITY;

        size--;
        return v;
    }

    /**
     * Returns the amount of the vehicle in the queue
     * @return
     *      the size of the queue
     */
    public int size()
    {
        return size;
    }

    /**
     * A support method to print the queue if the lane was going forward
     * @return
     *      A string which prints the queue neatly
     */
    public String ForwardToString()
    {
        if(isEmpty()) 
            return "";

        String answer="";

        for(int i = rear; i>=front;i--)
        {
            String carSerialId = String.valueOf(queue[i].getSerialId());
            if(carSerialId.length() == 1)
            {
                carSerialId = "00"+carSerialId;
            }
            else if(carSerialId.length() == 2)
            {
                carSerialId = "0"+carSerialId;
            }

            answer += "["+carSerialId+"]";
        }

        return answer;
    }

    /**
     * A support method to print the queue if the lane was going backward
     * @return
     *      A string which prints the queue neatly
     */
    public String BackwardToString()
    {
        if(isEmpty()) 
            return "";

        String answer="";

        for(int i = front; i<=rear;i++)
        {
            String carSerialId = String.valueOf(queue[i].getSerialId());
            if(carSerialId.length() == 1)
            {
                carSerialId = "00"+carSerialId;
            }
            else if(carSerialId.length() == 2)
            {
                carSerialId = "0"+carSerialId;
            }

            answer += "["+carSerialId+"]";
        }

        return answer;
    }
}

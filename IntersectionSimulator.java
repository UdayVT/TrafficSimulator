/**
 * A fully documented class named IntersectionSimulator. This class represents the manager of the simulation
 * it does the heavy lifting, per se. The main function's responsibility is to get the parameters for the
 * simulation pass them to the simulate() method. 
 * @author 
 * 		Uday Turakhia, SBU ID #: 115102637
 * <dt><b>Assignment:</b><dd>
 *    Homework #4 for CSE 214, Spring 2023
 * 		Recitation #: R03
 * <dt><b>Date:</b><dd>
 *    March 21th, 2023
 */
import java.util.Scanner;
 public class IntersectionSimulator {

    /**
     * This method does the actual simulation. This method actually implements 
     * the algorithm described by that diagram, using Intersection, BooleanSourceHW4, and TwoWayRoad.
     * @param simulateTime
     *      The amount of time cars can arrive till
     * @param arrivalProbability
     *      The probability of car arriving
     * @param roadnames
     *      The names of all road
     * @param maxGreenTime
     *      the maximum green time of all road
     */
    public static void simulate(int simulateTime, double arrivalProbability, String[] roadnames, int[] maxGreenTime)
    {
        //Creating important variables.
        BooleanSourceHW4 prob = new BooleanSourceHW4(arrivalProbability);
        int timeStep = 0;
        TwoWayRoad[] roads = new TwoWayRoad[roadnames.length];

        for(int i = 0; i<roads.length;i++)
        {
            roads[i] = new TwoWayRoad(roadnames[i], maxGreenTime[i]);
        }

        int totalCars = 0;
        int carsWaiting = 0;
        int carsPassed = 0;
        int totalWaitTime = 0;
        int maxWaitTime = 0;

        Intersection sim = new Intersection(roads);
        //start
        while(!(timeStep>=simulateTime && sim.isEmpty()))
        {   
            TwoWayRoad ActiveRoad = roads[sim.getLightIndex()];
            LightValue ActiveLightValue = ActiveRoad.getLightValue();
            timeStep++;
            System.out.println();
            System.out.println("################################################################################");
            System.out.println();
            System.out.println("Time step: "+timeStep+".");
            System.out.println();
            if(timeStep>simulateTime && ActiveRoad.greenLanesEmpty())
            {
                System.out.println("Left Arrow for "+ActiveRoad.getName());
            }
            else
            {

                if(ActiveLightValue == LightValue.GREEN || ActiveLightValue == LightValue.RED)
                {
                    System.out.println("Green Light for "+ActiveRoad.getName());
                }
                else
                {
                    System.out.println("Left Arrow for "+ActiveRoad.getName());
                }
            }
            System.out.println("Timer = "+sim.getCountdownTimer());
            System.out.println();
        
            //Cars arriving according to probability and enqueueing them in correct position and adding them to a String
            String arrivingCars = "";

            if(timeStep>simulateTime)
            {
                prob = new BooleanSourceHW4(0);
                arrivingCars += "Cars no longer arriving.\n\n";
            }

            arrivingCars += "ARRIVING CARS:\n";
            for(int i = 0; i<roads.length;i++)
            {
                for(int j = 0; j<TwoWayRoad.NUM_WAYS;j++)
                {
                    for(int k = 0;k<TwoWayRoad.NUM_LANES;k++)
                    {
                        if(prob.occursHW4())
                        {
                            Vehicle v = new Vehicle(timeStep);
                            sim.enqueueVehicle(i, j, k, v);
                            totalCars++;
                            carsWaiting++;
                           
                            String carSerialId = String.valueOf(v.getSerialId());
                            if(carSerialId.length() == 1)
                            {
                                carSerialId = "00"+carSerialId;
                            }
                            else if(carSerialId.length() == 2)
                            {
                                carSerialId = "0"+carSerialId;
                            }

                            String carSeriealString = "["+carSerialId+"]";


                            arrivingCars += "Car"+carSeriealString+" entered "+roadnames[i]+", going ";
                            
                            if(j==0 && k==0)
                            {
                                arrivingCars += "FORWARD in LEFT lane.\n";
                            }
                            else if(j==0 && k == 1)
                            {            
                                arrivingCars += "FORWARD in MIDDLE lane.\n"; 
                            }
                            else if(j==0 && k == 2)
                            {            
                                arrivingCars += "FORWARD in RIGHT lane.\n";                    
                            }
                            else if(j==1 && k == 0)
                            {            
                                arrivingCars += "BACKWARD in RIGHT lane.\n";                    
                            }
                            else if(j==1 && k == 1)
                            {            
                                arrivingCars += "BACKWARD in MIDDLE lane.\n";                    
                            }
                            else if(j==1 && k == 2)
                            {            
                                arrivingCars += "BACKWARD in LEFT lane.\n";                    
                            }
                        }
                    }
                }
            }
            
            //Passing Cars and adding them to a String
            Vehicle[] v = sim.timeStep(timeStep>simulateTime);
            int index = 0;
            String passingCars = "PASSING CARS:\n";
            while(v[index]!=null)
            {
                if(maxWaitTime < (timeStep-v[index].getTimeArrived()))
                    maxWaitTime = (timeStep-v[index].getTimeArrived());
                carsPassed++;
                carsWaiting--;
                totalWaitTime += timeStep-v[index].getTimeArrived();
                
                String carSerialId = String.valueOf(v[index].getSerialId());
                if(carSerialId.length() == 1)
                {
                    carSerialId = "00"+carSerialId;
                }
                else if(carSerialId.length() == 2)
                {
                    carSerialId = "0"+carSerialId;
                }

                String carSeriealString = "["+carSerialId+"]";
                
                passingCars += "Car"+carSeriealString+" passes through. Wait time of "+(timeStep-v[index].getTimeArrived())+"\n";
                index++;

                if(index == 4)
                    break;
            }

            //Printing arriving and passing cars
            System.out.println(arrivingCars);
            System.out.println();
            System.out.println(passingCars);
            System.out.println();
            System.out.println();

            //Printing Roads
            for(int i =0; i<roads.length;i++)
            {
                System.out.println(roads[i]);
            }

            //Printing Statistics
            System.out.println();
            System.out.println("STATISTICS: ");
            System.out.println("Cars currently waiting: "+carsWaiting);
            System.out.println("Total cars passed: "+carsPassed);
            System.out.println("Total wait time: "+totalWaitTime);
            String averageWaitTime = String.format("%.2f", (double) ((totalWaitTime*1.0)/totalCars));
            System.out.println("Average wait time: "+averageWaitTime);
            System.out.println();
        }

        //Printing Simulation Summary
        System.out.println();
        System.out.println("################################################################################");
        System.out.println("################################################################################");
        System.out.println("################################################################################");
        System.out.println();
        System.out.println("SIMULATION SUMMARY:");
        System.out.println();
        System.out.println("Total Time: "+timeStep+" steps");
        System.out.println("Total vehicles: "+totalCars+" vehicles");
        System.out.println("Longest wait time: "+maxWaitTime+" turns");
        System.out.println("Total wait time: "+totalWaitTime+" turns");
        String averageWaitTime = String.format("%.2f", (double) ((totalWaitTime*1.0)/totalCars));
        System.out.println("Average wait time: "+averageWaitTime+" turns");
        System.out.println();
    }   

    /**
     * Start for application, asks user for following values: simulationTime (int), 
     * arrivalProbability (double), numRoads (int), a name for each road, and a "green" time for each road.
     * This method also parses command line for these args. If args.length < 5, the above is read in at execution time
     */
    public static void main(String[] args) 
    {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to IntersectionSimulator 2021\n");
        int simulationTime;
        double arrivalProbability;
        int numStreets;

        //input sim time
        while(true)
        {
            System.out.print("Input the simulation time: ");        
            simulationTime = input.nextInt();
            if(simulationTime <= 0)
            {
                System.out.println("Invalid input, please try again\n");
                continue;
            }
            break;
        }

        //input arrival probability
        while(true)
        {
            System.out.print("Input the arrival probability: ");        
            arrivalProbability = input.nextDouble();
            if(arrivalProbability <= 0 || arrivalProbability>1)
            {
                System.out.println("Invalid input, please try again\n");
                continue;
            }
            break;
        }

        //input num of streets
        while(true)
        {
            System.out.print("Input the number of Streets: ");        
            numStreets = input.nextInt();
            if(numStreets <= 0 || numStreets > Integer.MAX_VALUE)
            {
                System.out.println("Invalid input, please try again\n");
                continue;
            }
            break;
        }
        
        input.nextLine();

        //input Road names
        String[] roadNames = new String[numStreets];

        for(int i = 0; i<numStreets;i++)
        {
            while(true)
            {
                System.out.print("Input Street "+(i+1)+" name: ");
                String name =input.nextLine();
                boolean noRepeat = true;

                for(int j = 0; j<i;j++)
                {
                    if(name.equals(roadNames[j]))
                    {
                        System.out.println("Duplicate Detected.\n");
                        noRepeat = false;
                        continue;
                    }
                }

                if(noRepeat)
                {
                    roadNames[i] = name;
                    break;
                }
            }
        }
        
        //input Road greenTIme
        int[] greenTime = new int[numStreets];
        for(int i = 0; i<numStreets;i++)
        {
            while(true)
            {
                System.out.print("Input max Green time for "+roadNames[i]+": ");
                int time =input.nextInt();

                if(time<2)
                {
                    System.out.println("Invalid input, please try again\n");
                    continue;
                }

                greenTime[i] = time;
                break;
            }
        }

        input.close();
        System.out.println();
        System.out.println("Starting Simulation...");
        simulate(simulationTime, arrivalProbability, roadNames, greenTime);
        System.out.println("End simulation.");
    }
}
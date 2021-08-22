/*
    MADE BY: ANKIT AGRAWAL
    ROLL NO.: IMT2019010

    PURPOSE OF FILE: This file contains the derived class of the
                     parent class Truck. The name of the derived
                     class is "DerivedTruck". This class contains
                     the implementation of how a Truck should move
                     from the source to the destination. The
                     implementation takes care of the routing
                     algorithm, entering a Truck to the Hub, and
                     moving the Truck from Hub to Hub
*/

package demo19010;

import base.*;

import java.util.*;
import java.lang.Math;

public class DerivedTruck extends Truck
{
    DerivedTruck()      //This constructor initialized the variables necessary for maintaining the state of the Truck as it moves in the graph
    {
        Last_exited_hub = null;     //This variable stores the Hub from which the Truck last exited. Initially, it is null
        Total_time_elapsed = 0;     //This variable stores the total time that has elapsed, since the simulation began. It is initially 0
        has_reached_destination = false;    //This variable is true if the Truck has reached its destination, otherwise, it is false
        Current_highway = null;     //This variable stores the Highway on which the Truck is currently moving. It is null, if the Truck is not moving on any Highway
        has_reached_nearest_starting_hub = false;       //This variable is true, if the Truck has reached its nearest starting hub, otherwise it is false
    }

    public String getTruckName()    //This overridden function returns the name of the Truck, in the format: "TruckNNNNN", since my roll number is IMT2019010, it is "Truck19010"
    {
        return "Truck19010";        //We return the name of the Truck
    }

    public Hub getLastHub()     // This function returns the Hub from which it last exited.
    {
        return Last_exited_hub;     //We return the Hub from which the Truck last exited. If the Truck did not exit from any Hub previously, it will return null
    }

    public void enter(Highway hwy)      //This function instructs the Truck to enter on the Highway "hwy". Hubs use this function to direct the Truck to the destination
    {
        Last_exited_hub = hwy.getStart();       //We update the Hub from which the Truck exited, as the Starting Hub of the new Highway on which the Truck is now moving
        Current_highway = hwy;      //We update the Highway on which the Truck is currently moving
    }



    // called every deltaT time to update its status/position
    // If less than startTime, does nothing
    // If at a hub, tries to move on to next highway in its route
    // If on a road/highway, moves towards next Hub
    // If at dest Hub, moves towards dest
    protected void update(int deltaT)       //This function updates the Location of the Truck, if it is moving on a Highway, and manages other logistics for the Truck
    {
        /*
            1. If the Truck reaches the next Hub, then we call the add() method of Hub.
            If true returned by the add() method, then it calls remove() of Highway(),
            to remove itself from the Highway on which it was moving, otherwise, it doesn't do anything.

            2. If the Truck reaches the next Hub, but could not be admitted because the
            Hub was full, the we will again try to add the Truck to next Hub, using
            add() method of the Hub.

            3. This function also handles the case when the Truck has reached its destination Hub. Note,
            that by "reach", I mean that the Truck has not entered the destination Hub, but is near to the
            destination Hub. In reply to my Question posted on LMS, Sir said that we need not to make the
            Truck enter on its destination Hub, therefore, in this case, the update function immediately
            moves the Truck to the Destination Location. (Please see the answer to Question 13, on the LMS,
            of my post titled "Few more doubts in Assignment 6" in the Discussion Forum).

            4. When the truck starts moving, it immediately reaches the nearest Hub. This is not a problem,
            as in a question discussed in the doubt Session with Sir, Sir said that it is not a problem.
            (Although this might not be the best way, yet, it simplifies the code to a big extent, and makes
            it more robust).

            5. If the truck has started moving, but has not reached its next Hub, we simply update its next
            location.
        */

        Total_time_elapsed += deltaT;       //First, we update the amount of time that has elapsed since the simulation started.
        if(getStartTime() <= Total_time_elapsed)        //Now, see if the Start time of the Truck is greater than the time elapsed, only then do we start moving the Truck
        {
            if(has_reached_destination != true)     //We check if the Truck has reached is destination
            {
                if(Current_highway != null)       //We check if the Truck is on a Highway. A truck might not be on a Highway when it is on a Hub.
                {
                    Location current_loc = getLoc();        //We get the current location of the Truck
                    Location prev_hub_loc = (Current_highway.getStart()).getLoc();      //We get the location of the hub from which the Truck last exited
                    Location next_hub_loc = (Current_highway.getEnd()).getLoc();        //We get the location of the hub to which the Truck has to move next

                    int highway_speed = Current_highway.getMaxSpeed();      //We get the Highway speed on which the Truck is moving
                    double deltaT_float = ((double) deltaT)/1000;       //As deltaT is in milliseconds, we divide it by 1000 to convert it into seconds
                    double pixels_travelled = deltaT_float * highway_speed;     //We calculate the pixels traveled by the Truck during this time interval deltaT

                    Location new_loc;       //This variable stores the new location of the Truck, after time deltaT has elapsed

                    if(next_hub_loc.getX() == current_loc.getX() && next_hub_loc.getY() == current_loc.getY())      //We check if the Truck is at a Hub, but has not entered it, because the Hub was at full capacity
                    {
                        //The truck has not entered the next hub, but is waiting to be entered
                        new_loc = new Location(current_loc);        //In this case, we will simply set the new location of the Truck as the current location of the Truck
                    }
                    else        //Otherwise, it means that the Truck is moving on some Highway, and might have reached the next Hub on the Highway
                    {
                        //The new location of the Truck will be calculated based on variables prev_hub_loc and next_hub_loc

                        if(prev_hub_loc.getX() == next_hub_loc.getX())      //If the x-coordinates of both the Hubs are same, then it means that the Truck was moving only along y-coordinate
                        {
                            int pixels_travelled_int = (int) pixels_travelled;      //We convert (double) to (int), because location of the truck has to be in integer coordinates. Note that this variable is >= 0
                            if(pixels_travelled_int == 0)       //We check if rounding off caused the Truck to move by 0 pixels
                            {
                                pixels_travelled_int += 1;      //If such a case happens, then we increase pixels_travelled_int by 1, to make sure that the Truck does some movement
                            }

                            if(next_hub_loc.getY() < prev_hub_loc.getY())       //Now, it might happen that the next Hub has Y-coordinate less than the previous hub, in which case, the Truck should move down (and NOT up)
                            {
                                pixels_travelled_int = pixels_travelled_int * (-1);     //So, we multiply pixels_travelled_int by (-1), to make sure it moves along -ve y-axis
                            }

                            new_loc = new Location(current_loc.getX(), current_loc.getY() + pixels_travelled_int);      //We set new_loc to store the new location of the Truck
                        }
                        else if(prev_hub_loc.getY() == next_hub_loc.getY())     //If the y-coordinates of both the Hubs are same, then it means that the Truck was moving only along x-coordinate
                        {
                            int pixels_travelled_int = (int) pixels_travelled;      //We convert (double) to (int), because location of the truck has to be in integer coordinates. Note that this variable is >= 0
                            if(pixels_travelled_int == 0)       //We check if rounding off caused the Truck to move by 0 pixels
                            {
                                pixels_travelled_int += 1;      //If such a case happens, then we increase pixels_travelled_int by 1, to make sure that the Truck does some movement
                            }

                            if(next_hub_loc.getX() < prev_hub_loc.getX())       //Now, it might happen that the next Hub has x-coordinate less than the previous hub, in which case, the Truck should move left (and NOT right)
                            {
                                pixels_travelled_int = pixels_travelled_int * (-1);     //So, we multiply pixels_travelled_int by (-1), to make sure it moves along -ve x-axis
                            }

                            new_loc = new Location(current_loc.getX() + pixels_travelled_int, current_loc.getY());      //We set new_loc to store the new location of the Truck
                        }
                        else        //This is the general case, when the x-coordinates of both the hubs or y-coordinates of both the hubs are not same
                        {
                            double slope = ((double)(next_hub_loc.getY() - prev_hub_loc.getY()))/((double)(next_hub_loc.getX() - prev_hub_loc.getX()));     //We calculate the slope of the line connecting the Hubs, given by m = (y2 - y1)/(x2 - x1)
                            double cos_theta = ((double)(next_hub_loc.getX() - prev_hub_loc.getX()))/Math.sqrt(next_hub_loc.distSqrd(prev_hub_loc));        //We calculate the cosine of the angle theta, which is given by cos(theta) = (x2 - x1)/hypotenuse (or distance b/w hubs)
                            double dist_x_double = pixels_travelled * cos_theta;        //The distance moved along x axis will be hypotenuse (or distance traveled * cos(theta). This variable could be positive or negative
                            int dist_x_int = (int)(dist_x_double);      //We convert (double) to (int), as floating coordinates are not allowed.
                            if(dist_x_int == 0)     //We check if rounding off caused the Truck to move by 0 pixels
                            {
                                dist_x_int += 1;        //If so, we increase the distance moved by the truck by 1 pixels
                            }

                            if(next_hub_loc.getX() < prev_hub_loc.getX() && (dist_x_int >= 0))      //Now, dist_x_int is positive/zero/negative. If the next hub's x-coordinate is less than previous hub's x-coordinate, and if dist_x_int is positive, it must be made negative
                            {
                                dist_x_int = (dist_x_int) * (-1);       //So, in this case, we make dist_x_int to be negative, by multiplying it with (-1)
                            }

                            int new_loc_x = current_loc.getX() + dist_x_int;        //new_loc_x contains the new x-coordinate of the Truck
                            double new_loc_y_double = (slope * (new_loc_x - prev_hub_loc.getX()));      //Now, we would calculate the y-coordinate of the Truck based on its new x-coordinate. This new y-coordinate is given by equation of line: y = m(x-x1) + y1, where m is the slope we found earlier
                            new_loc_y_double += prev_hub_loc.getY();        //calculating the new y-coordinate, we do need to add y1, which is done here
                            int new_loc_y_int = (int) new_loc_y_double;     //We convert (double) to (int), as we are dealing with integer coordinates
                            if(new_loc_y_int == current_loc.getY())     //We check if the rounding off caused to truck to change its y-coordinate to change by 0 pixels.
                            {
                                if(next_hub_loc.getY() > prev_hub_loc.getY())       //If the y-coordinate of the next hub is greater than the y-coordinate of the previous Hub, we increase its y-coordinate by 1 pixel
                                {
                                    new_loc_y_int += 1;     //Increase y-coordinate by 1 pixel
                                }
                                else if(next_hub_loc.getY() < prev_hub_loc.getY())      //if the y-coordinate of the next hub is less than the y-coordinate of the previous Hub, we decrease its y-coordinate by 1 pixel
                                {
                                    new_loc_y_int -= 1;     //Decrease the y-coordinate by 1 pixel
                                }
                            }

                            new_loc = new Location(new_loc_x, new_loc_y_int);       //We set the new location of the truck
                        }
                    }

                    /*
                        Now, we need to implement an algorithm to make sure that the truck reaches the Hub, and does not miss it. To do so, we use the fact that
                        the x-y coordinate plane is divide into 4 quadrants. There are 4 directions, (+x, -x, +y, -y) and 4 more directions
                        (those lying in first quadrant, those lying in second quadrant, those lying in third quadrant, and those lying in fourth quadrant).
                        Obviously, any direction will definitely lie in these directions. So this is how we will make sure that the truck reaches its
                        nearest hub.
                    */

                    boolean c1 = (prev_hub_loc.getY() == next_hub_loc.getY()) && (prev_hub_loc.getX() < next_hub_loc.getX()) && (new_loc.getX() >= next_hub_loc.getX());
                    boolean c2 = (prev_hub_loc.getY() == next_hub_loc.getY()) && (prev_hub_loc.getX() > next_hub_loc.getX()) && (new_loc.getX() <= next_hub_loc.getX());

                    boolean c3 = (prev_hub_loc.getX() == next_hub_loc.getX()) && (prev_hub_loc.getY() < next_hub_loc.getY()) && (new_loc.getY() >= next_hub_loc.getY());
                    boolean c4 = (prev_hub_loc.getX() == next_hub_loc.getX()) && (prev_hub_loc.getY() > next_hub_loc.getY()) && (new_loc.getY() <= next_hub_loc.getY());

                    boolean c5 = (prev_hub_loc.getX() < next_hub_loc.getX()) && (prev_hub_loc.getY() < next_hub_loc.getY()) && ((new_loc.getX() >= next_hub_loc.getX()) || (new_loc.getY() >= next_hub_loc.getY()));
                    boolean c6 = (prev_hub_loc.getX() > next_hub_loc.getX()) && (prev_hub_loc.getY() > next_hub_loc.getY()) && ((new_loc.getX() <= next_hub_loc.getX()) || (new_loc.getY() <= next_hub_loc.getY()));

                    boolean c7 = (prev_hub_loc.getX() > next_hub_loc.getX()) && (prev_hub_loc.getY() < next_hub_loc.getY()) && ((new_loc.getX() <= next_hub_loc.getX()) || (new_loc.getY() >= next_hub_loc.getY()));
                    boolean c8 = (prev_hub_loc.getX() < next_hub_loc.getX()) && (prev_hub_loc.getY() > next_hub_loc.getY()) && ((new_loc.getX() >= next_hub_loc.getX()) || (new_loc.getY() <= next_hub_loc.getY()));

                    boolean c9 =  (Math.abs(new_loc.getX() - next_hub_loc.getX()) <= 5) && (prev_hub_loc.getX() != next_hub_loc.getX()) && (prev_hub_loc.getY() != next_hub_loc.getY());
                    boolean c10 = (Math.abs(new_loc.getY() - next_hub_loc.getY()) <= 5) && (prev_hub_loc.getX() != next_hub_loc.getX()) && (prev_hub_loc.getY() != next_hub_loc.getY());

                    /*
                        To explain the above conditions, let us denote:

                        x1: x-coordinate of the previous hub
                        x2: x-coordinate of the next hub
                        y1: y-coordinate of the previous hub
                        y2: y-coordinate of the next hub

                        Condition c1: (y1 == y2), (x1 < x2), and the x-coordinate of the truck >= x2
                        Condition c2: (y1 == y2), (x1 > x2), and the x-coordinate of the truck <= x2
                        Condition c3: (x1 == x2), (y1 < y2), and the y-coordinate of the truck >= y2
                        Condition c4: (x1 == x2), (y1 > y2), and the y-coordinate of the truck <= y2

                        Condition c5: (x1 < x2), (y1 < y2), and either x-coordinate of truck >= x2, or y-coordinate of truck >= y2
                        Condition c6: (x1 > x2), (y1 > y2), and either x-coordinate of truck <= x2, or y-coordinate of truck <= y2
                        Condition c7: (x1 > x2), (y1 < y2), and either x-coordinate of truck <= x2, or y-coordinate of truck >= y2
                        Condition c8: (x1 < x2), (y1 > y2), and either x-coordinate of truck >= x2, or y-coordinate of truck <= y2

                        Condition c9: This is a special condition. In case the difference between x-coordinates of the Truck and Hub are <= 5, we request Hub to add the Truck
                        Condition c10: This is a special condition. In case the difference between y-coordinates of the Truck and Hub are <= 5, we request Hub to add the Truck

                        Note that atleast one of the conditions (c1 - c10) will be true if the truck has reached the next Hub, but has not entered it because the Hub
                        is at full capacity. Therefore, the truck will again try to add itself to the next Hub
                    */

                    if(c1 || c2 || c3 || c4 || c5 || c6 || c7 || c8 || c9 || c10)       //We check if either of the above conditions holds true
                    {
                        /*
                            As the truck would've reached the next hub, we check
                            If the next hub is the destination hub. If so, we
                            won't add it to the next hub, rather, just make
                            it move to the destination state.
                        */

                        if((Current_highway.getEnd().getLoc().getX() == Network.getNearestHub(getDest()).getLoc().getX()) && (Current_highway.getEnd().getLoc().getY() == Network.getNearestHub(getDest()).getLoc().getY()))    //We check if the next Hub is the destination Hub
                        {
                            Last_exited_hub = Network.getNearestHub(getDest());     //If so, we set Last_exited_hub as the destination Hub
                            setLoc(getDest());      //We set the Location of the Hub as the Destination Location of the Truck
                            has_reached_destination = true;     //We set has_reached_destination to true, indicating that the truck has reached its destination
                            Current_highway.remove(this);   //We remove the Truck from the Highway on which it is currently moving
                            Current_highway = null;     //We set Current_highway as null, as it is not moving on any Highway, once it has reached its destination

                        }
                        else    //So the next hub is not the destination hub, but the truck has reached the next hub
                        {
                            setLoc(next_hub_loc);       //We set the new location of the Truck as the Location of the next Hub

                            //Now, we will see if the hub has capacity, if so, we will add it
                            if(Current_highway.getEnd().add(this) == true)      //We try to add the Truck to the next Hub
                            {
                                //So, the truck has been added successfully(more precisely, its reference has been added)

                                Current_highway.remove(this);       //We remove the Truck from the Highway on which it was moving
                                Current_highway = null;     //We set Current_Highway to null, as the Truck is not moving on any Highway

                                //Note: Last_exited_hub will be updated when the hub leaves the current hub
                            }
                        }
                    }
                    else        //If either of the above condition are not satisfied...
                    {
                        //... then it means that the truck is still on the highway
                        setLoc(new_loc);        //We set the new location of the Truck
                    }
                }
                else        //NOT REACHED DESTINATION and NOT ON HIGHWAY
                {
                    /*
                        If this condition is invoked, then it could be either
                        of the two cases:

                        1. The truck has just begun from its starting station.
                        If this is the case, then Last_exited_hub = null, and
                        has_reached_nearest_starting_hub = false

                        2. The truck is waiting on some hub. In this case,
                        we cannot do anything. As it is the responsibility
                        of the hub to invoke the enter() of truck to make
                        it move to the next highway.
                    */

                    if(Last_exited_hub == null && has_reached_nearest_starting_hub == false)        //We check if the Truck has started moving or not. If these conditions are satisfied, then the Truck has not started moving
                    {
                        /*
                            There could be a case when the nearest hub is the
                            destination hub, we should take care of that by
                            checking if the nearest hub is the destination
                            hub, and if so, we will not add it to that hub,
                            rather, just make its final location to be the
                            same as its destination location
                        */

                        Hub nearest_starting_hub = Network.getNearestHub(getSource());      //This variable stores the nearest Hub to the starting location of the Truck
                        if((nearest_starting_hub.getLoc().getX() == Network.getNearestHub(getDest()).getLoc().getX()) && (nearest_starting_hub.getLoc().getY() == Network.getNearestHub(getDest()).getLoc().getY()))        //We check if the nearest Hub is the destination Hub
                        {
                            //So, the nearest starting hub was the destination hub
                            Last_exited_hub = Network.getNearestHub(getDest()); //The truck exited the destination hub, and so we update Last_exited_hub
                            setLoc(getDest());  //As the truck has reached destination station, we set the new location of the Truck to be the same as the destination station
                            has_reached_destination = true;     //The truck has reached destination station
                            Current_highway = null;     //The truck is not on any Highway, we indicate this by setting Current_highway to null
                            has_reached_nearest_starting_hub = true;        //As the Truck would've been to the Starting hub, we set has_reached_nearest_starting_hub to true
                        }
                        else
                        {
                            //So, the nearest hub is not the destination hub :)
                            //We simply try to add it to the hub, and update its location
                            //Other state variables will get updated when the
                            //Truck exits the hub
                            if(nearest_starting_hub.add(this) == true)      //We try to add the Truck to its nearest source Hub
                            {
                                setLoc(nearest_starting_hub.getLoc());      //We set the location of the Truck as the Location of its nearest Hub
                                Current_highway = null;     //The truck is not on any Highway, we indicate this by setting Current_highway to null
                                Last_exited_hub = null;     //Although the truck has entered the Hub, yet it has not exited from it. Therefore, Last_exited_hub remains null
                                has_reached_nearest_starting_hub = true;        //As the Truck would've been to the Starting hub, we set has_reached_nearest_starting_hub to true
                            }
                        }
                    }
                }
            }
        }
    }

    private Hub Last_exited_hub;        //This variable stores the Hub which the Truck last exited
    private int Total_time_elapsed;     //This variable stores the Total time elapsed since the beginning of the simulation
    private boolean has_reached_destination;    //This variable is true is the Truck has reached its destination
    private Highway Current_highway;        //This stores the Highway on which the Truck is currently moving. If it is not moving on any Highway, this variable stores null
    private boolean has_reached_nearest_starting_hub;        //When a truck starts, we will need it to check if it has reached the nearest starting hub or not. This variable stores true if it has reached the nearest starting hub, otherwise it stores false
}

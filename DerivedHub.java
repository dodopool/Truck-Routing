/*
    MADE BY: ANKIT AGRAWAL
    ROLL NO.: IMT2019010

    PURPOSE OF FILE: This file contains the derived class of the
                     parent class Hub. The name of the derived
                     class is "DerivedHub". This class contains
                     the implementation of how a Hub should route
                     Trucks to its Destination Hub.
*/

package demo19010;

import base.*;

import java.util.*;

public class DerivedHub extends Hub
{
    public DerivedHub(Location location)        //This is the constructor, which makes the Hub with Location "location"
    {
        super(location);    //We make call the constructor of the base class "Hub"
        TrucksOnHub = new ArrayList <Truck>();      //We initialize the ArrayList TrucksOnHub, which contains a list of trucks on the Hub, waiting to be routed to their next highway
    }

    synchronized public boolean add(Truck truck)    //This method is used to add a truck to the Hub. It returns true if the Truck was added, otherwise, it returns false.
    {
        int HubCapacity = getCapacity();    //We receive the capacity of the Hub, from the Base class "Truck"
        if(TrucksOnHub.size() >= HubCapacity)       //Now, we check if the Hub has capacity to accommodate more trucks. If not, we return false
        {
            return false;       //We return false if the above condition is true
        }
        else        //Otherwise, we will add the Truck to the Hub, and return true
        {
            TrucksOnHub.add(truck);     //We add the Truck to the Hub
            return true;        //We return true, indicating that the Truck was successfully added to the Hub
        }
    }

    synchronized protected void remove (Truck truck)        //This function is used to remove a truck from the Hub
    {
        for(int j = 0 ; j < TrucksOnHub.size(); j++)        //We iterate through the list of Hubs on the truck
        {
            if(TrucksOnHub.get(j) == truck)     //We compare the Truck by reference, if it is found, we remove it
            {
                TrucksOnHub.remove(j);      //We remove the truck if it is found
                break;      //We exit the loop
            }
        }
    }

    public Highway getNextHighway(Hub last, Hub dest)       //This function returns the next Highway for given destination Hub "dest"
    {
        /*
            We do local routing.
            What we will do is that we know the highways that are
            connected to the current hub, so, we decide the next
            Highway to be sent as follows:

            1. For all the end points of highways that are connected
            (and directed away from current hub),
            we check if the location of the end point of the highway
            is same as the dest for truck, if so we send it there.

            2. If the destination hub does not exist for our given hub,
            then we will move the truck to the hub that is nearest to
            "dest" hub of the truck.

            ------------------------------------------------------
            THIS FUNCTION RETURNS null IN CASE:
            ------------------------------------------------------

            1. Current Hub == Destination Hub
            2. No outgoing highways are present from the current hub (definitely not possible)

        */

        if(dest.getLoc().getX() == getLoc().getX() && dest.getLoc().getY() == getLoc().getY())      //If this Hub is the Destination Hub for the Truck, we will return null. We can't route it
        {
            return null;        //Already reached destination hub, so we return null
        }

        ArrayList <Highway> ConnectedHighways = getHighways();      //Now, we find all the outgoing Highways from the Hub
        if(ConnectedHighways.size() == 1)       //If there is only one outgoing Highway, we are forced to send the Truck to that Highway
        {
            return ConnectedHighways.get(0);        //So we return the only outgoing Highway as the next Highway
        }
        else if(ConnectedHighways.size() == 0)      //If there are no outgoing Highways, then we return null, as we can't send the Truck anywhere
        {
            return null;        //So we return null
        }

        int min_distance_sqrd;      //This variable stores the distance of the Hub which is closest to the destination Hub "dest" of the Truck
        int highway_index = -1;     //Initially, the highway to send it is -1, indicating that we don't have a Highway for the Truck to send it to.

        //We now set the value of min_distance_sqrd and highway_index by the following code

        if(last != null)        //As Sir had said, in case the Truck is starting from its source Hub, the value of "last" will be null. This block of code handles the case when "last" != null
        {
            if(ConnectedHighways.get(0).getEnd() == last)       //If the first Highway leads to the Hub from which the Truck last exited...
            {
                highway_index = 1;      //..then, we will temporarily set highway_index to 1, as we would not like to move the Truck to the Highway which leads to "last" Hub
                min_distance_sqrd = ConnectedHighways.get(1).getEnd().getLoc().distSqrd(dest.getLoc());     //We set the value of min_distance_sqrd as square of distance between the Location of "dest" Hub and the Hub at the end of the Highway being considered
            }
            else        //Otherwise...
            {
                highway_index = 0;      //...we temporarily set highway_index to 0
                min_distance_sqrd = ConnectedHighways.get(0).getEnd().getLoc().distSqrd(dest.getLoc());     //We set the value of min_distance_sqrd as square of distance between the Location of "dest" Hub and the Hub at the end of the Highway being considered
            }
        }
        else
        {
            //This means that this hub is the starting hub for a truck, as "last" is null

            if(ConnectedHighways.get(0).getEnd() == this)   //This is for a self loop, a highly impossible scenario of a highway directly to the Hub itself :)
            {
                highway_index = 1;      //..then, we will temporarily set highway_index to 1, as we would not like to move the Truck to the Highway which leads to the current Hub
                min_distance_sqrd = ConnectedHighways.get(1).getEnd().getLoc().distSqrd(dest.getLoc());     //We set the value of min_distance_sqrd as square of distance between the Location of "dest" Hub and the Hub at the end of the Highway being considered
            }
            else        //Otherwise...
            {
                highway_index = 0;      //...we temporarily set highway_index to 0
                min_distance_sqrd = ConnectedHighways.get(0).getEnd().getLoc().distSqrd(dest.getLoc());     //We set the value of min_distance_sqrd as square of distance between the Location of "dest" Hub and the Hub at the end of the Highway being considered
            }
        }


        for(int j = 0 ; j < ConnectedHighways.size(); j++)      //We now find the Highway which leads to the Hub that is closest to the destination Hub "dest"
        {
            Hub Hub_starting = ConnectedHighways.get(j).getStart();     //This variable stores the Starting of the Highway we are currently considering. This is definitely the current Hub
            Location current_hub_location = Hub_starting.getLoc();      //We get the location of this "starting hub" (which is actually the current Hub)
            if(current_hub_location.getX() == getLoc().getX() && current_hub_location.getY() == getLoc().getY())        //We check if the x,y coordinates of the Starting of the Highway is same as the Current Hub. This would mean that the Highway has ending Hub which is not the current Hub. This condition is always true
            {
                if(last != null)        //We check if the "last" hub is null, if not..
                {
                    if(ConnectedHighways.get(j).getEnd().getLoc().getX() != last.getLoc().getX() && ConnectedHighways.get(j).getEnd().getLoc().getY() != last.getLoc().getY())       //There could be a highway with source as current hub and destination as previous hub. We should not send it there
                    {
                        if((ConnectedHighways.get(j).getEnd().getLoc()).distSqrd(dest.getLoc()) < min_distance_sqrd)        //We check if the distance of the ending Hub of the Current Highway is lesses than the current minimum distance
                        {
                            min_distance_sqrd = (ConnectedHighways.get(j).getEnd().getLoc()).distSqrd(dest.getLoc());       //If so, we update min_distance_sqrd
                            highway_index = j;      //We also update highway_index
                        }
                    }
                }
                else        //If "last" Hub is null...
                {
                    if(ConnectedHighways.get(j).getEnd().getLoc().getX() != this.getLoc().getX() && ConnectedHighways.get(j).getEnd().getLoc().getY() != this.getLoc().getY())       //This condition always evaluates to true.
                    {
                        if((ConnectedHighways.get(j).getEnd().getLoc()).distSqrd(dest.getLoc()) < min_distance_sqrd)    //We check if the distance of the ending Hub of the Current Highway is lesses than the current minimum distance
                        {
                            min_distance_sqrd = (ConnectedHighways.get(j).getEnd().getLoc()).distSqrd(dest.getLoc());   //If so, we update min_distance_sqrd
                            highway_index = j;      //We also update highway_index
                        }
                    }
                }
            }
        }

        if(highway_index != -1)         //If highway_index != -1, we return the Highway on which we need to send the truck next
        {
            return ConnectedHighways.get(highway_index);        //We return the Highway on which we need to send the truck next
        }
        else
        {
            return null;        //Otherwise, we return null
        }
    }

    synchronized protected void processQ(int deltaT)        //This function is responsible for sending Trucks to highways
    {
        /*
            As per the problem statement, our hub will be responsible only
            for handling the trucks that have arrived on the hub (and are
            waiting at the hub).

            If our hub is a destination hub for a truck, we will remove it
            from our list of trucks, we need not handle it
        */

        ArrayList <Truck> Indices_to_remove = new ArrayList <Truck>();      //This ArrayList holds the Trucks to be removed (not their Indices) from the Hub

        for(int j = 0 ; j < TrucksOnHub.size(); j++)        //We iterate through all the Trucks on the Hub
        {
            Truck Current_truck = TrucksOnHub.get(j);       //This variable stores the Truck being considered currently
            if(Current_truck.getDest().getX() == getLoc().getX() && Current_truck.getDest().getY() == getLoc().getY())      //If the Hub is the destination Hub for the Truck...
            {
                Indices_to_remove.add(Current_truck);       //... we add it to list of trucks to be removed
            }
            else
            {
                Highway Next_highway = getNextHighway(Current_truck.getLastHub(), Network.getNearestHub(Current_truck.getDest()));      //Otherwise, we find the Next Highway on which the Truck should move
                if(Next_highway != null)        //If the Next Highway is not null...
                {
                    //We try to move this truck to the next highway
                    boolean add_success = Next_highway.add(Current_truck);      //We try to add this Truck to the next Highway
                    if(add_success == true)     //If the Truck was moved to the next Highway
                    {
                        Current_truck.enter(Next_highway);      //We notify the truck that it has moved to the next highway
                        Indices_to_remove.add(Current_truck);   //We remove this truck from the Hub
                    }
                }
                else        //If the Next_highway is null, we remove this Truck
                {
                    Indices_to_remove.add(Current_truck);   //We add it to the list of Trucks to be removed
                }
            }
        }

        for(int j = 0 ; j < Indices_to_remove.size(); j++)      //Now, we remove all the Trucks which are to be removed from the Hub
        {
            remove(Indices_to_remove.get(j));       //We remove the Truck
        }
    }

    private ArrayList <Truck> TrucksOnHub;      //This ArrayList stores the List of Trucks which are on the Hub
}

/*
    MADE BY: ANKIT AGRAWAL
    ROLL NO.: IMT2019010

    PURPOSE OF FILE: This file contains the derived class of the
                     parent class Network. The name of the derived
                     class is "DerivedNetwork". This class holds the
                     list of all the Hubs, Highways and Trucks
                     that are in the graph.
*/

package demo19010;

import base.*;

import java.util.*;

public class DerivedNetwork extends Network
{
    public DerivedNetwork()     //This constructor initializes the ArrayLists holding Hubs, Highways and Trucks in the graph
    {
        ListOfHubs = new ArrayList <Hub>();     //We initialize ListOfHubs - which holds all the Hubs in the graph
        ListOfHighways = new ArrayList <Highway>();     //We initialize ListOfHighways - which holds all the Highways in the graph
        ListOfTrucks = new ArrayList <Truck>();     //We initialize ListOfTrucks - which holds all the Trucks in the graph
    }

    public void add(Hub hub)        //This function is used to add a Hub to the graph
    {
        ListOfHubs.add(hub);        //We add the Hub to the graph - by adding it to ListOfHubs
    }

    public void add(Highway hwy)        //This function is used to add a Highway to the graph
    {
        ListOfHighways.add(hwy);        //We add the Highway to the graph - by adding it to ListOfHighways
    }

    public void add(Truck truck)        //This function is used to add a Truck to the graph
    {
        ListOfTrucks.add(truck);        //We add the Truck to the graph - by adding it to ListOfTrucks
    }

    public void start()     //This function is used to make the Hubs and Trucks in the Current graph work simultaneously
    {
        for(int j = 0; j < ListOfHubs.size(); j++)      //We iterate through all the Hubs in the graph...
        {
            ListOfHubs.get(j).start();      //... and we start each Hub in the graph
        }

        for(int j = 0; j < ListOfTrucks.size(); j++)    //We iterate through all the Trucks in the graph...
        {
            ListOfTrucks.get(j).start();    //... and we start each Truck in the graph
        }
    }

    public void redisplay(Display disp)      //This function is used to display/draw the Hubs, Trucks and Highways in the graph
    {
        for(int j = 0;  j < ListOfHubs.size(); j++)     //We iterate through all the Hubs in the graph...
        {
            ListOfHubs.get(j).draw(disp);       //... and we call draw() on each of the Hub, asking them to draw themselves
        }

        for(int j = 0 ; j < ListOfHighways.size(); j++)     //We iterate through all the Highways in the graph...
        {
            ListOfHighways.get(j).draw(disp);   //... and we call draw() on each of the Highways, asking them to draw themselves
        }

        for(int j = 0 ; j < ListOfTrucks.size(); j++)       //We iterate through all the Trucks in the graph...
        {
            ListOfTrucks.get(j).draw(disp);     //... and we call draw() on each of the Trucks, asking them to draw themselves
        }
    }

    protected Hub findNearestHubForLoc(Location loc)        //This function finds the nearest Hub for a given Location "loc" in the graph
    {
        int nearest_hub_index = 0;      //We assume that the first Hub is the nearest Hub to the given Location
        int nearest_hub_distance = loc.distSqrd(ListOfHubs.get(0).getLoc());    //This variable stores the distance between the Location of the nearest hub and the given Location "loc"
        for(int j = 1; j < ListOfHubs.size(); j++)      //Now, we iterate through all the remaining Hubs in the graph
        {
            if(loc.distSqrd(ListOfHubs.get(j).getLoc()) < nearest_hub_distance)     //We check if the distance of a Hub from the given Location "loc" is shorter than the current shortest distance...
            {
                nearest_hub_distance = loc.distSqrd(ListOfHubs.get(j).getLoc());    //... if so, then we update the nearest_hub_distance, to be the new shortest distance
                nearest_hub_index = j;      //We also update the new index of the hub, which is nearer to the given Location "loc"
            }
        }
        return ListOfHubs.get(nearest_hub_index);       //We return the nearest Hub to the given Location "loc"
    }

    private ArrayList <Hub> ListOfHubs;     //This ArrayList stores the List of Hubs that are in the graph
    private ArrayList <Highway> ListOfHighways;     //This ArrayList stores the List of Highways that are in the graph
    private ArrayList <Truck> ListOfTrucks;     //This ArrayList stores the List of Trucks that are in the graph
}

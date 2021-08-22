/*
    MADE BY: ANKIT AGRAWAL
    ROLL NO.: IMT2019010

    PURPOSE OF FILE: This file contains the derived class of the
                     parent class Factory. The name of the derived
                     class is "FactoryDemo". This class can be
                     used to create instances of derived classes
                     of Network, Highway, Hub and Truck.

*/

package demo19010;

import base.*;

import java.util.*;

public class FactoryDemo extends Factory
{
    public Network createNetwork()      //This function creates a Network, which can be used to hold trucks, highways and hubs
    {
        return new DerivedNetwork();    //We return a newly created instance of DerivedNetwork, which extends abstract class Network
    }

    public Highway createHighway()      //This function creates a Highway, on which trucks can move, and can connect two hubs unidirectionally
    {
        return new DerivedHighway();    //We return a newly created instance of DerivedHighway, which extends abstract class Highway
    }

    public Hub createHub(Location location)     //This function creates a Hub (having Location as parameter "location"), which direct trucks to destination locations
    {
        return new DerivedHub(location);        //We return a newly created instance of DerivedHub, which extends abstract class Hub.
    }

    public Truck createTruck()      //This function creates a Truck, which move from hub to hub, trying to get to its final destination
    {
        return new DerivedTruck();      //We return a newly created instance of DerivedTruck, which extends abstract class Truck
    }
}

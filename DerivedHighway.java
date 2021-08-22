/*
    MADE BY: ANKIT AGRAWAL
    ROLL NO.: IMT2019010

    PURPOSE OF FILE: This file contains the derived class of the
                     parent class Highway. The name of the derived
                     class is "DerivedHighway". Trucks move on a
                     Highway, when between Hubs. Also, Highways are
                     used to connect two Hubs unidirectionally.
*/

package demo19010;

import base.*;

import java.util.*;

public class DerivedHighway extends Highway
{
    public DerivedHighway()     //This constructor initializes the Array List "TrucksOnHighway", which stores Trucks moving on this Highway
    {
        TrucksOnHighway = new ArrayList <Truck>();      //We initialize TrucksOnHighway
    }

    public synchronized boolean hasCapacity()       //This function returns true if the Highway can accommodate, otherwise returns false
    {
        if(TrucksOnHighway.size() < getCapacity())      //We check if the Highway has capacity...
        {
            return true;        //... if it has capacity, we return true
        }
        else
        {
            return false;       //... otherwise we return false
        }
    }

    public synchronized boolean add(Truck truck)    //This function adds a truck to the Highway, if the Highway has capacity, and returns true if added, otherwise false.
    {
        if(hasCapacity() == false)      //We check if the Highway has capacity to accommodate more trucks...
        {
            return false;       //... if it does not have capacity, it returns false
        }
        else        //Otherwise, the Highway has capacity to accommodate atleast one more truck
        {
            TrucksOnHighway.add(truck);     //So, we add the truck to the list of Trucks on the Highway
            return true;        //We return true, indicating that the Truck was successfully added to the Highway
        }
    }

    public synchronized void remove(Truck truck)        //This function is used to remove a Truck from the Highway. Once a truck reached a Hub, it should remove itself from the Highway
    {
        for(int j = 0 ; j < TrucksOnHighway.size(); j++)        //We iterate through all the Trucks that are currently on the Highway
        {
            if(TrucksOnHighway.get(j) == truck)     //We find the Truck to be removed by comparing its reference, if the truck is found...
            {
                TrucksOnHighway.remove(j);      //... we remove it from the list of Trucks on the Highway
                break;      //Now, we exit the loop. As there can be only one truck of a given instance on a Highway
            }
        }
    }

    private ArrayList <Truck> TrucksOnHighway;      //This ArrayList stores all the Trucks that are currently moving on the Highway.
}

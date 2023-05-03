package dev.n7meless.model.comparator;

import dev.n7meless.model.Airport;

import java.util.Comparator;

public class AirportNameComparator implements Comparator<Airport> {
    @Override
    public int compare(Airport a, Airport b) {
        return a.getName().compareTo(b.getName());
    }
}

package org.wei.sptask;

import org.wei.sptask.data.LocationData;
import org.wei.sptask.data.PSIData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by sli on 4/8/2017.
 */

public class LocationDataController {

    private LocationData westReadingPos;
    private LocationData eastReadingPos;
    private LocationData centralReadingPos;
    private LocationData southReadingPos;
    private LocationData northReadingPos;
    private LocationData currentLocation;


    public static final int SOUTHWEST = 0;
    public static final int SOUTH = 1;
    public static final int SOUTHEAST = 2;
    public static final int WEST = 3;
    public static final int CENTRAL = 4;
    public static final int EAST = 5;
    public static final int NORTHWEST = 6;
    public static final int NORTH = 7;
    public static final int NORTHEAST = 8;

    List<Double> westToEastBoarder;
    List<Double> southToNorthBoarder;


    public void setupRegions(LocationData west, LocationData east, LocationData central, LocationData south, LocationData north) {
        westReadingPos = west;
        eastReadingPos = east;
        centralReadingPos = central;
        southReadingPos = south;
        northReadingPos = north;
        setupBoarder();
    }

    private void setupBoarder() {
        westToEastBoarder = Arrays.asList(
                (westReadingPos.getLongtitude() + centralReadingPos.getLongtitude()) / 2,
                (centralReadingPos.getLongtitude() + eastReadingPos.getLongtitude()) / 2
        );

        southToNorthBoarder = Arrays.asList(
                (southReadingPos.getLatitude() + centralReadingPos.getLatitude()) / 2,
                (centralReadingPos.getLatitude() + northReadingPos.getLatitude()) / 2
        );
    }

    public List<Double> getWestToEastBoarder() {
        return westToEastBoarder;
    }

    public List<Double> getSouthToNorthBoarder() {
        return southToNorthBoarder;
    }

    public int findMyRegion(LocationData currentLocation) {
        int x = 0, y = 0;
        double curlong = currentLocation.getLongtitude();
        double curLat = currentLocation.getLatitude();
        for (int i = 0; i < westToEastBoarder.size(); i++) {
            if (curlong < westToEastBoarder.get(i)) {
                x = i;
                break;
            } else if (i + 1 < westToEastBoarder.size() && curlong < westToEastBoarder.get(i + 1)) {
                x = i + 1;
                break;
            } else if ( i == westToEastBoarder.size() -1){
                x = i + 1;
                break;
            } else {
                continue;
            }
        }

        for (int i = 0; i < southToNorthBoarder.size(); i++) {
            if (curLat < southToNorthBoarder.get(i)) {
                y = i;
                break;
            } else if (i + 1 < southToNorthBoarder.size() && curLat < southToNorthBoarder.get(i + 1)) {
                y = i + 1;
                break;
            } else if ( i == westToEastBoarder.size() -1){
                y = i + 1;
                break;
            } else {
                continue;
            }
        }

        return y * 3 + x;
    }

    public double getRegionReadings(int regionIndex, PSIData data) {
        switch (regionIndex) {
            case SOUTHWEST:
                // return the average value of south and west
                return ((data.getSouthReading() + data.getWestReading()) / 2);
            case SOUTH:
                // return south reading;
                return data.getSouthReading();
            case SOUTHEAST:
                return ((data.getSouthReading() + data.getEastReading()) / 2);
            case WEST:
                return data.getWestReading();
            case CENTRAL:
                return data.getCentralReading();
            case EAST:
                return data.getEastReading();
            case NORTHWEST:
                return ((data.getNorthReading() + data.getWestReading()) / 2);
            case NORTH:
                return data.getNorthReading();
            case NORTHEAST:
                return ((data.getNorthReading() + data.getEastReading()) / 2);
            default:
                throw new IllegalArgumentException("not in any region.");
                // TODO error;
        }
    }


}



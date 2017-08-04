package org.wei.sptask.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sli on 4/8/2017.
 */

public class PSIData {
    @SerializedName("west")
    private double west;
    @SerializedName("national")
    private double national;
    @SerializedName("east")
    private double east;
    @SerializedName("central")
    private double central;
    @SerializedName("south")
    private double south;
    @SerializedName("north")
    private double north;

    public PSIData(double west, double east, double central, double south, double north) {
        this.west = west;
        this.east = east;
        this.central = central;
        this.south = south;
        this.north = north;
    }

    public double getWestReading() {
        return west;
    }
    public double getNationalReading() {
        return national;
    }
    public double getEastReading() {
        return east;
    }
    public double getCentralReading() {
        return central;
    }
    public double getSouthReading() {
        return south;
    }
    public double getNorthReading() {
        return north;
    }
}

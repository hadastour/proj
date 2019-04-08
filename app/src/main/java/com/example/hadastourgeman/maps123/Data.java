package com.example.hadastourgeman.maps123;

public class Data {
    public int range1;
    public int range2;
    public int heading;
    public int elevation;

    public Data(int range1, int range2, int heading, int elevation) {
        this.range1 = range1;
        this.range2 = range2;
        this.heading = heading;
        this.elevation = elevation;
    }

    public Data() {
    }

    public void setRange1(int range1) {
        this.range1 = range1;
    }

    public void setRange2(int range2) {
        this.range2 = range2;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public int getRange1() {
        return range1;
    }

    public int getRange2() {
        return range2;
    }

    public int getHeading() {
        return heading;
    }

    public int getElevation() {
        return elevation;
    }
}

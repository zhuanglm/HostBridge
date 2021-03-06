package com.epocal.common.am;

/**
 * This class is a mirror of Point defined in Analytical Manager C++
 * DO NOT EDIT THIS FILE without consulting with AM-C++ team first!
 */
public class Point {
    public float x;
    public float y;

    public Point() {
        this(0, 0);
    }
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

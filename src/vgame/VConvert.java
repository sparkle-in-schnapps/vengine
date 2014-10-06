/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgame;

import java.awt.Point;
import vengine.VPoint;

/**
 *
 * @author yew_mentzaki
 */
public class VConvert {

    public static Point to2DPoint(VPoint v) {
        int rx = 0;
        int ry = (int) -v.z;
        rx += v.x - v.y;
        ry += v.y / 2 + v.x / 2;
        return new Point(rx, ry);
    }

    public static VPoint to2DPoint(double x, double y, double z) {
        int rx = 0;
        int ry = (int) -z;
        rx += x - y;
        ry += y / 2 + x / 2;
        return new VPoint(rx, ry, 0);
    }

    public static VPoint to2DPoint(int x, int y, int z) {
        int rx = 0;
        int ry = -z;
        rx += x - y;
        ry += y / 2 + x / 2;
        return new VPoint(rx, ry, 0);
    }

    public static double dist(double x, double y, double x2, double y2) {
        return Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
    }

    public static VPoint toPoint(int x, int y) {
        int x3 = (x + 2 * y) / 2;
        int y3 = 2 * y - x3;
        return new VPoint(x3, y3, 0);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgame;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.sql.Time;
import java.util.Date;
import java.util.Random;
import org.lwjgl.opengl.Display;
import sun.awt.X11.XConstants;
import vengine.CFG;
import vengine.List;
import vengine.VFileReader;
import vengine.VGraphics;
import vengine.VPoint;

/**
 *
 * @author yew_mentzaki
 */
public class VLandschaft {

    private byte[][] type, levels, fow, fow2;
    private CFG[] types;
    private Random r = new Random();

    private Point point(int x, int y) {
        while (x >= 512) {
            x -= 512;
        }
        while (x < 0) {
            x += 512;
        }
        while (y >= 512) {
            y -= 512;
        }
        while (y < 0) {
            y += 512;
        }
        return new Point(x, y);
    }

    private byte tf(Point p) {

        return (fow[p.x][p.y]);

    }

    private byte tp(Point p) {

        return (type[p.x][p.y]);

    }

    private byte t(Point p) {

        return (levels[p.x][p.y]);

    }

    public void hit_world(int cx, int cy, int cz) {
        int x = cx / 64;
        int y = cy / 64;
        Point p = point(x, y);
        if (!types[type[p.x][p.y]].g("tree").equals("")) {
            type[p.x][p.y] = (byte) types[type[p.x][p.y]].gi("next");
        }
    }

    public void open_map(int power, int cx, int cy, int cz) {
        int x = cx / 64;
        int y = cy / 64;
        int z = cz / 20;
        z = levels[point(x, y).x][point(x, y).y];

        for (float j = 0; j < Math.PI * 2; j += 1f / (float) power) {
            int p = power;
            float r = 0;
            int h = 0;
            while (p > 0) {

                Point pnt = point((int) (x + Math.cos(j) * r), (int) (y + Math.sin(j) * r));

                if (z >= levels[pnt.x][pnt.y] - h) {
                    fow2[pnt.x][pnt.y] = 2;
                } else {
                    h++;
                }
                if (!types[type[pnt.x][pnt.y]].g("tree").equals("")) {
                    p = 0;
                }
                if (z > levels[pnt.x][pnt.y]) {
                    p = 0;
                }

                r++;
                p--;
            }
        }

    }

    public void place_building(int power, int cx, int cy, int cz) {
        int x = cx / 64;
        int y = cy / 64;
        for (int i = -3; i <= 4; i++) {
            for (int j = -3; j <= 4; j++) {
                Point p = point(x + i, y + j);
                levels[p.x][p.y] = 3;
                type[p.x][p.y] = 1;

            }
        }

    }

    public VLandschaft() {
        List l = VFileReader.readList(new File(vengine.VEngine.cfg.g("modhome") + "land.list"));
        types = new CFG[Integer.parseInt(l.get(0))];
        for (int i = 0; i < types.length; i++) {
            types[i] = VFileReader.readCFG(new File(vengine.VEngine.cfg.g("modhome") + "lands/" + i + ".cfg"));
        }
        levels = new byte[512][512];
        type = new byte[512][512];
        fow = new byte[512][512];
        fow2 = new byte[512][512];

        Random r = new Random();

        for (int x = 0; x < 512; x++) {
            for (int y = 0; y < 512; y++) {
                levels[x][y] = (byte) (3);
                fow[x][y] = 10;
                int j = r.nextInt(types.length + 5) + 2;
                if (j > types.length - 1) {
                    j = 2;
                }
                type[x][y] = (byte) j;
            }
        }
        for (int i = 0; i < 700; i++) {
            int px = r.nextInt(500);
            int py = r.nextInt(500);

            for (int x = 0; x < 30; x++) {
                int hx = r.nextInt(12) + px;
                int hy = r.nextInt(12) + py;
                levels[hx][hy] = (byte) Math.max(1, levels[hx][hy] - 1);

            }
        }
        for (int i = 0; i < 700; i++) {
            int px = r.nextInt(500);
            int py = r.nextInt(500);

            for (int x = 0; x < 30; x++) {
                int hx = r.nextInt(12) + px;
                int hy = r.nextInt(12) + py;
                levels[hx][hy] = (byte) Math.min(5, levels[hx][hy] + 1);

            }
        }
    }

    public void render(VGraphics g, Point pcm) {

        for (int x = pcm.x / 64; x < 30 + pcm.x / 64; x++) {
            for (int y = -10 + pcm.y / 64; y < 20 + pcm.y / 64; y++) {

                int h = 20;
                int z = t(point(x, y)) * h;

                VPoint test = VConvert.to2DPoint(x * 64 + -pcm.x / 2, y * 64 + 32 - pcm.y / 2, z);

                int n = t(point(x, y - 1)) * h;
                int o = t(point(x + 1, y)) * h;
                int s = t(point(x, y + 1)) * h;
                int w = t(point(x - 1, y)) * h;
                int no = t(point(x + 1, y - 1)) * h;
                int so = t(point(x + 1, y + 1)) * h;
                int nw = t(point(x - 1, y - 1)) * h;
                int sw = t(point(x - 1, y + 1)) * h;
                if (x == 0 || x == 511 || y == 0 || y == 511) {
                    g.setColor(new Color(235, 0, 0));
                } else {
                    g.setColor(new Color(235, 235, 235));
                
                }
                g.setTexture(types[tp(point(x, y))].g("texture"));

                if (x % 2 == 0) {
                    if (y % 2 == 0) {
                        g.fillPolygon(
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == w) | (z - h == nw) | (z - h == n)) ? z - h : z))
                        );
                    } else {
                        g.fillPolygon(
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == w) | (z - h == nw) | (z - h == n)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z))
                        );

                    }
                } else {
                    if (y % 2 == 0) {
                        g.fillPolygon(
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == w) | (z - h == nw) | (z - h == n)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z))
                        );
                    } else {
                        g.fillPolygon(
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == w) | (z - h == nw) | (z - h == n)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z)),
                                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z))
                        );
                    }
                }
                //*

                if (!"".equals(types[tp(point(x, y))].g("tree"))) {
                    g.setTexture(types[tp(point(x, y))].g("tree"));
                    VPoint v = VConvert.to2DPoint(x * 64 - pcm.x, y * 64 - pcm.y, z);

                    g.drawSprite((int) v.x, (int) v.y, types[tp(point(x, y))].gi("tree_w"), types[tp(point(x, y))].gi("tree_h"));
                } else {

                }
                //*/
                if (tf(point(x, y)) > 0) {
                    g.setColor(new Color(160, 160, 160));
                } else {
                    g.setColor(new Color(60, 60, 60));
                }
                g.setTexture(types[tp(point(x, y))].g("border_texture"));

                g.fillPolygon(
                        VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z)),
                        VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, 0),
                        VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 - 32 - pcm.y, 0),
                        VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z))
                );
                if (tf(point(x, y)) > 0) {
                    g.setColor(new Color(190, 190, 190));
                } else {
                    g.setColor(new Color(90, 90, 90));
                }
                g.fillPolygon(
                        VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z)),
                        VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, 0),
                        VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 + 32 - pcm.y, 0),
                        VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z))
                );

            }

        }
    }
    int i;
    public void render_fow(VGraphics g, Point pcm) {
        g.setTexture("fow/full.png");
        i++;
        for (int x = pcm.x / 64; x < 30 + pcm.x / 64; x++) {
            for (int y = -10 + pcm.y / 64; y < 20 + pcm.y / 64; y++) {

                int h = 20;
                int z = t(point(x, y)) * h;

                VPoint test = VConvert.to2DPoint(x * 64 + -pcm.x / 2, y * 64 + 32 - pcm.y / 2, z);

                if (x == 0 || x == 511 || y == 0 || y == 511) {
                    g.setColor(new Color(235, 0, 0));
                } else if (tf(point(x, y)) > 0) {
                    g.setColor(new Color(235, 235, 235));
                } else {
                    g.setColor(new Color(135, 135, 135));
                }
                VPoint p = VConvert.to2DPoint(x * 64 - pcm.x, y * 64 - pcm.y, z);

                if (tf(point(x, y)) <= 1) {
                    if(tf(point(x, y))==0){
                        g.setColor(new Color(255, 255, 255, 75));
                    }else{
                        g.setColor(new Color(255, 255, 255, 15));
                    }
                    g.drawRect((int) p.x, (int) p.y, 255, 255, i);
                    g.drawRect((int) p.x, (int) p.y, 255, 255, -i);
                }
            }

        }
    }

    public void clear_map() {

        for (int i = 0; i < 512; i++) {
            for (int j = 0; j < 512; j++) {
                fow[i][j] = fow2[i][j];
                fow2[i][j] = (byte) (fow[i][j]>0?1:0);
            }
        }

    }

    public double l(double z1, double z2, int x) {
        double zi = (z1 - z2) / 64;
        double zd = z1 + zi * (x - 64.0 * (int) ((int) x / (int) 64));

        return zd;
    }

    public double get_height(int x, int y) {
        x += 32;
        y += 32;
        int ix = x;
        int iy = y;
        x /= 64;
        y /= 64;
        int h = 20;

        int z = t(point(x, y)) * h;
        int n = t(point(x, y - 1)) * h;
        int o = t(point(x + 1, y)) * h;
        int s = t(point(x, y + 1)) * h;
        int w = t(point(x - 1, y)) * h;
        int no = t(point(x + 1, y - 1)) * h;
        int so = t(point(x + 1, y + 1)) * h;
        int nw = t(point(x - 1, y - 1)) * h;
        int sw = t(point(x - 1, y + 1)) * h;
        int xx = ((((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z));
        int xy = ((((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z));
        int yy = ((((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z));
        int yx = ((((z - h == w) | (z - h == nw) | (z - h == n)) ? z - h : z));

        return (xx + xy + yy + yx) / 4;
    }

    public void sublight(int x, int y, VPoint pcm, VGraphics g) {
        x += 32;
        y += 32;
        int ix = x;
        int iy = y;
        x /= 64;
        y /= 64;
        int h = 20;

        int z = t(point(x, y)) * h;
        int n = t(point(x, y - 1)) * h;
        int o = t(point(x + 1, y)) * h;
        int s = t(point(x, y + 1)) * h;
        int w = t(point(x - 1, y)) * h;
        int no = t(point(x + 1, y - 1)) * h;
        int so = t(point(x + 1, y + 1)) * h;
        int nw = t(point(x - 1, y - 1)) * h;
        int sw = t(point(x - 1, y + 1)) * h;
        int xx = ((((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z));
        int xy = ((((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z));
        int yy = ((((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z));
        int yx = ((((z - h == w) | (z - h == nw) | (z - h == n)) ? z - h : z));

        g.fillPolygon(
                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == w) | (z - h == sw) | (z - h == s)) ? z - h : z)),
                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 + 32 - pcm.y, (((z - h == o) | (z - h == so) | (z - h == s)) ? z - h : z)),
                VConvert.to2DPoint(x * 64 + 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == o) | (z - h == no) | (z - h == n)) ? z - h : z)),
                VConvert.to2DPoint(x * 64 - 32 - pcm.x, y * 64 - 32 - pcm.y, (((z - h == w) | (z - h == nw) | (z - h == n)) ? z - h : z))
        );
    }
}

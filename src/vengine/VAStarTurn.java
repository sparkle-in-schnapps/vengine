/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import java.awt.Point;
import java.util.ArrayList;
import vexamples.units.VUnit;
import vgame.VLandschaft;
import vgame.VObject;

/**
 *
 * @author yew_mentzaki
 */
public class VAStarTurn {

    private static class way {

        VObject vo[];
        VLandschaft vl;
        VPoint f;

        public way(VObject vo[], VLandschaft vl, VPoint f) {
            this.vo = vo;
            this.vl = vl;
            this.f = f;
        }

        private class p {

            p p = null;
            short x, y;

            public p(short x, short y) {
                this.x = x;
                this.y = y;
            }

            public p(short x, short y, p p) {
                this.x = x;
                this.y = y;
                this.p = p;
            }

            public void l(ArrayList<p> pp) {
                if (b(x, y, x - 1, y)) {
                    pp.add(new p((short) (x - 1), (short) (y), this));
                }
                if (b(x, y, x + 1, y)) {
                    pp.add(new p((short) (x + 1), (short) (y), this));
                }
                if (b(x, y, x, y - 1)) {
                    pp.add(new p((short) (x), (short) (y - 1), this));
                }
                if (b(x, y, x, y + 1)) {
                    pp.add(new p((short) (x), (short) (y + 1), this));
                }
            }
        }
        boolean b[][] = new boolean[512][512];

        public boolean b(int x, int y, int x2, int y2) {
            if (x2 >= 0 && y2 >= 0 && x2 < 512 && y2 < 512) {
                if (!b[x2][y2] && Math.abs(vl.get_height(x, y) - vl.get_height(x2, y2)) <= 20) {
                    b[x2][y2] = true;
                    return true;
                }
            }
            return false;
        }

        public void f() {
            ArrayList<p> pp = new ArrayList<p>();
            
            pp.add(new p((short) (f.x / 64), (short) (f.y / 64)));
            for (int i = 0; i < 64; i++) {

                ArrayList<p> ppa = new ArrayList<p>();
                for (p p : pp) {
                    p.l(ppa);

                }
                for (p p : ppa) {
                    for (VObject u : vo) {
                        
                        if (u != null) {
                            VPoint s = new VPoint(u.gd("x"), u.gd("y"), u.gd("z")); 
                            if (p.x == (int) s.x / 64 && p.y == (int) p.y / 64) {
                                Point[] point = new Point[64];
                                for (int j = 0; j < 64; j++) {
                                    point[j] = new Point(p.x, p.y);
                                    if (p.p != null) {
                                        p = p.p;
                                    }
                                }

                                System.arraycopy(point, 0, u.way, 0, 64);
                            }
                        }

                        pp.add(p);
                        return;
                    }
                }
            }

        }

    }

    public static void findWay(VLandschaft vl, VPoint s, VPoint f, VObject... vo) {
        System.out.println("Add way to turn");
        final way w = new way(vo, vl, f);
        Thread t = new Thread("AStar") {

            @Override
            public void run() {
                w.f();
            }

        };
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

}

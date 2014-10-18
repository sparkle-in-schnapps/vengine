/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vexamples.units;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import vengine.Sprite;
import vengine.VGraphics;
import vengine.VPoint;
import vengine.VoxelModel;
import vgame.VConvert;
import vgame.VGame;
import vgame.VObject;
import vgame.VProcessor;

/**
 *
 * @authors yew_mentzaki; whizzpered
 */
public class VBullet extends VProcessor {

    Random r = new Random();

    public VBullet() {
        name = "VBullet";
    }

    @Override
    public void render(VGame vg, VObject vo, VPoint cam, VGraphics g) {
        if (vo.type.g("model").equals("voxel")) {
            g.setColor(Color.white);
            VoxelModel.render(vo.type.g("voxelmodel"), g, new VPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z") - cam.z), new VPoint(0, vo.gd("ay"), vo.gd("a")));

        }
        if (vo.type.g("model").equals("lighting")) {
            try {
                VPoint vp = VConvert.to2DPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z"));
                VPoint tp = VConvert.to2DPoint(vo.gd("tx") - cam.x, vo.gd("ty") - cam.y, vo.gd("tz"));

                for (int i = 0; i < 3; i++) {
                    double x = vp.x;
                    double y = vp.y;
                    double ex = tp.x;
                    double ey = tp.y;
                    while (true) {

                        if (((Math.abs(x - ex) < 30) && (Math.abs(y - ey) < 30))) {
                            break;
                        }
                        double angle = Math.atan2((ey - y), (ex - x));
                        int sx = (int) (x + r.nextInt(20) * Math.cos(angle + r.nextInt(3) - 1)), sy = (int) (y + r.nextInt(20) * Math.sin(angle + r.nextInt(3) - 1));
                        g.setColor(new Color(131, 145, 226, 38));
                        g.lineSize = 6;
                        g.drawLine(x, y, sx, sy);
                        g.lineSize = 0.5f;
                        g.setColor(Color.white);
                        g.drawLine(x, y, sx, sy);
                        x = sx;
                        y = sy;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init(VGame vg, VObject vo) {
        vo.tag("#tick");
        vo.tag("#render");
        vo.tag("#bullet");
        vo.s("hp", vo.type.gi("hp"));

    }

    @Override
    public void post_init(VGame vg, VObject vo) {
        if (vo.type.g("model").equals("voxel")) {
            int r = vo.gi("r");
            vo.s("tx", vo.gi("tx") + this.r.nextInt(r + 1) - r / 2);
            vo.s("ty", vo.gi("ty") + this.r.nextInt(r + 1) - r / 2);

            double a = Math.atan2(vo.gi("ty") - vo.gi("y"), vo.gi("tx") - vo.gi("x"));
            double v = vo.type.gi("v");
            vo.s("hp", Math.sqrt(Math.pow(vo.gi("ty") - vo.gi("y"), 2) + Math.pow(vo.gi("tx") - vo.gi("x"), 2)) / v);
            vo.s("a", a);
            double h = vo.type.gd("g") * (vo.gi("hp") * vo.gi("hp") / 4 / 2);
            vo.s("vz", Math.sqrt(2 * vo.type.gd("g") * h));
            if (vo.type.gi("g") == 0) {
                vo.s("vx", Math.cos(a) * v);

                vo.s("vy", Math.sin(a) * v);

            }

            vo.s("x", vo.gi("x") + vo.gi("vx"));
            vo.s("y", vo.gi("y") + vo.gi("vy"));
            vo.s("z", vo.gd("z") + vo.gd("vz"));
            vo.s("ay", Math.atan2(vo.gd("vz"), vo.type.gd("v")));
            vo.s("vz", vo.gd("vz") + vo.type.gd("g"));
        }
        if (vo.type.g("model").equals("lighting")) {

        }
    }

    @Override
    public void call(VGame vg, VObject vo, String... args) {

    }

    @Override
    public int y(VObject vo) {
        return super.y(vo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tick(VGame vg, VObject vo) {
        vo.s("hp", vo.gi("hp") - 1);
        vg.vl.open_map(2, vo.gi("x"), vo.gi("y"), vo.gi("z"));
        if (vo.gi("hp") <= 0) {
            vg.addSpray(vo.type.gi("boom_type"), new VPoint(vo.gi("x"), vo.gi("y"), vo.gi("z")));
            if (vo.type.g("pit").equals("yes")) {
                vg.vl.pit(vo.gi("x"), vo.gi("y"), vo.gi("z"));
            }
            if (vo.type.g("crater").equals("yes")) {
                vg.vl.crater(vo.gi("x"), vo.gi("y"), vo.gi("z"));
            }
            vo.remove();
        }
        if (vo.type.g("model").equals("voxel")) {
            if (r.nextInt(5) == 0 && vo.type.g("trace").equals("yes")) {

                vg.addSpray(vo.type.gi("trace_type"), new VPoint(vo.gi("x"), vo.gi("y"), vo.gi("z")));
            }
            for (VObject obj : vg.getObjects(new VPoint(vo.gi("x"), vo.gi("y"), vo.gi("z")), 20, "#tick")) {
                if (obj == vo) {
                    continue;
                }
                if (vo.gi("ow") != obj.gi("ow")) {
                    obj.vp.call(vg, obj, "hit", vo.g("pw"), vo.type.g("type"));
                    vo.remove();
                }

            }
            vo.s("x", vo.gi("x") + vo.gi("vx"));
            vo.s("y", vo.gi("y") + vo.gi("vy"));
            vo.s("z", vo.gd("z") + vo.gd("vz"));
            vo.s("ay", Math.atan2(vo.gd("vz"), vo.type.gd("v")));
            vo.s("vz", vo.gd("vz") + vo.type.gd("g"));
        }
    }

}

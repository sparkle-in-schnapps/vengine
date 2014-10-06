/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vexamples.units;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
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
 * @author yew_mentzaki
 */
public class VUnit extends VProcessor {

    Random r = new Random();

    public VUnit() {
        name = "VUnit";
    }

    @Override
    public void render(VGame vg, VObject vo, VPoint cam, VGraphics g) {

        int vs = Math.abs(vo.gi("vs"));

        if (vs > 200) {
            vs = 200;
        } else if (vs < 0) {
            vs = 0;
        }

        if (vo.type.g("model").equals("sprite")) {
            g.setColor(new Color(255, 255, 255, 255));

            if (vo.gi("pt") < vo.type.gi("ptime") / 3 * 2) {
                Sprite.render(vo.type.g("sprite"), g, Math.min(vo.gi("pt") / 5, 9), new VPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z") - cam.z));
            }
            if (vo.gi("pt") > 0) {
                if (r.nextInt(30) == 0) {
                    vo.s("wlx", r.nextInt(120) - 60);
                    vo.s("wly", r.nextInt(120) - 60);
                    vo.s("wlz", r.nextInt(70));
                }
                Sprite.render("welding", g, r.nextInt(4), new VPoint(vo.gd("x") + vo.gd("wlx") - cam.x, vo.gd("y") + vo.gd("wly") - cam.y, vo.gd("z") + vo.gd("wlz") - cam.z));
                Sprite.render("welding", g, r.nextInt(4), new VPoint(vo.gd("x") + vo.gd("wlx") - cam.x, vo.gd("y") + vo.gd("wly") - cam.y, vo.gd("z") + vo.gd("wlz") - cam.z));
                Sprite.render("welding", g, r.nextInt(4), new VPoint(vo.gd("x") + (-vo.gd("wlx") - 20) - cam.x, vo.gd("y") + vo.gd("wly") - cam.y, vo.gd("z") + (50 - vo.gd("wlz")) - cam.z));

            }
            if (vo.gi("pt") > vo.type.gi("ptime") / 2 - 50) {
                int pt = vo.gi("pt");
                if (pt > vo.type.gi("ptime") / 2) {
                    pt = Math.min(Math.max((20 + (pt - vo.type.gi("ptime")) / 5), 10), 19);

                } else if (pt <= vo.type.gi("ptime") / 2) {
                    pt = vo.type.gi("ptime") / 2 - pt;

                    pt = Math.min(Math.max((50 - pt) / 5, 0), 10);
                }

                Sprite.render("building", g, pt, new VPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z") - cam.z));
            }

        }
        if (vo.type.g("model").equals("voxel")) {
            g.setColor(new Color(255 - vs, 255, 255 - vs, 255 - vs));
            VoxelModel.render(vo.type.g("voxelmodel"), g, new VPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z") - cam.z), new VPoint(0, vo.gd("ay"), vo.gd("a")));
            VPoint vp;

            vp = VConvert.to2DPoint((int) (vo.gd("x") - cam.x), (int) (vo.gd("y") - cam.y), (int) (vo.gd("z") - cam.z));
            VPoint vp2 = VConvert.to2DPoint((int) (vo.gd("x") - cam.x), (int) (vo.gd("y") - cam.y), (int) (0));
            g.setColor(new Color(255, 255, 255, 0));
            g.setTexture("power_shell.png");
        }
        /*
         g.drawRect((int) vp.x, (int) vp.y, 96, 96, r.nextInt(360));
         g.removeTexture();
         g.setColor(new Color(255, 0, 0, 100));
         vg.vl.sublight((int) (vo.gd("x")), (int) (vo.gd("y")), cam, g);
         //*/
    }

    @Override
    public void init(VGame vg, VObject vo) {
        vo.tag("#tick");
        vo.tag("#render");
        vo.s("hp", 1);
        if (vo.type.g("building_type").equals("factory")) {
            vo.tag("#factory");
        }
        vo.tag("#" + vo.type.g("gamename"));

        if (vo.type.g("type").equals("building")) {
            vo.s("pt", vo.type.gi("ptime"));
        }
    }

    @Override
    public void post_init(VGame vg, VObject vo) {
        vo.s("!tx", (vo.gi("x") + 100));
        vo.s("!ty", vo.g("y"));
        vo.s("z", vg.vl.get_height(vo.gi("x"), vo.gi("y")));
        if (vo.type.g("building_type").equals("factory")) {
            vo.tag("#factory");
        }
    }

    @Override
    public void call(VGame vg, VObject vo, String... args) {
        if (args[0].equals("move")) {
            if (vg.player == vo.gi("ow")) {
                vo.s("!tx", args[1]);
                vo.s("!ty", args[2]);
            }
        }
        if (args[0].equals("buildunit")) {
            if (vg.player == vo.gi("ow")) {
                vo.s("!cst", args[1]);
                vo.s("!std", vg.type(args[1]).gi("ptime"));
            }
        }
    }

    @Override
    public int y(VObject vo) {
        return super.y(vo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tick(VGame vg, VObject vo) {

        for (VObject obj : vg.getObjects(new VPoint(vo.gi("x"), vo.gi("y"), vo.gi("z")), vo.type.gi("haborites"), "#tick")) {
            if (obj == vo) {
                continue;
            }
            if (vo.type.gi("haborites") > obj.type.gi("haborites")) {
                double a = Math.atan2(obj.gi("y") - vo.gi("y"), obj.gi("x") - vo.gi("x"));
                obj.s("x", obj.gd("x") + Math.cos(a) * 2);
                obj.s("y", obj.gd("y") + Math.sin(a) * 2);
            }
            if (vo.type.gi("haborites") == obj.type.gi("haborites")) {
                double a = Math.atan2(obj.gi("y") - vo.gi("y"), obj.gi("x") - vo.gi("x"));
                obj.s("x", obj.gd("x") + Math.cos(a));
                obj.s("y", obj.gd("y") + Math.sin(a));
                vo.s("x", vo.gd("x") - Math.cos(a));
                vo.s("y", vo.gd("y") - Math.sin(a));
            }
            if (vo.type.gi("haborites") < obj.type.gi("haborites")) {
                double a = Math.atan2(obj.gi("y") - vo.gi("y"), obj.gi("x") - vo.gi("x"));
                vo.s("x", vo.gd("x") - Math.cos(a) * 2);
                vo.s("y", vo.gd("y") - Math.sin(a) * 2);
            }
        }

        vo.tag("#" + vo.type.g("building_type"));
        if (vo.gi("pt") > 0) {
            vo.s("pt", vo.gi("pt") - 1);
        } else {
            vg.players[vo.gi("ow")].vp.call(vg, vg.players[vo.gi("ow")], "pw", String.valueOf(vo.type.gi("energy")));
            int pw = vg.players[vo.gi("ow")].gi("power");
            if (pw > 100 && vo.gi("!std") > 0) {

                if (r.nextInt(10) == 0) {
                    vg.addSpray(1, new VPoint(vo.gi("x") - 64, vo.gi("y") + 5, 128));
                }

                vo.s("!std", vo.gi("!std") - 1);
                if (vo.type.g("building_type").equals("factory") && vo.gi("!std") == 0) {
                    vg.newObject(vo.g("!cst"), "x=" + (vo.gi("x") + 64), "!tx=" + (vo.gi("x") + 350), "y=" + vo.gi("y"), "#render", "#tick");

                }
            }

        }
        if (vo.type.g("invisible").equals("yes")) {
            if (vo.gi("vs") < 255) {
                vo.s("vs", vo.gi("vs") + 1);
            }
        } else {
            if (vo.gi("vs") > 0) {
                vo.s("vs", vo.gi("vs") - 1);
            }
        }

        if (vo.type.g("type").equals("building")) {
            vg.vl.place_building(10, vo.gi("x"), vo.gi("y"), vo.gi("z"));
        }
        if (vo.type.g("building_type").equals("refinery")) {
            vg.players[vo.gi("ow")].s("money", vg.players[vo.gi("ow")].gi("money") + 1);
        }
        if (vo.type.g("type").equals("vehicle")) {
            if (Math.abs(vo.gi("!tx") - (vo.gi("x"))) > 32 || Math.abs(vo.gi("!ty") - (vo.gi("y"))) > 32) {
                double a2 = (Math.atan2(vo.gd("!ty") - vo.gd("y"), vo.gd("!tx") - vo.gd("x")));
                double a1 = vo.gd("a");

                double ts = vo.type.gd("turnspeed");
                if (a1 < -Math.PI) {
                    a1 += 2 * Math.PI;
                }
                if (a1 > Math.PI) {
                    a1 -= 2 * Math.PI;
                }
                if (Math.abs(a2 - a1) <= 2 * Math.PI - Math.abs(a2 - a1)) {
                    if (a2 < a1) {
                        a1 -= ts;
                    }
                    if (a2 > a1) {
                        a1 += ts;
                    }
                }
                if (Math.abs(a2 - a1) > 2 * Math.PI - Math.abs(a2 - a1)) {
                    if (a2 < a1) {
                        a1 += ts;
                    }
                    if (a2 > a1) {
                        a1 -= ts;
                    }
                }

                vo.s("a", a1);
                vo.s("z", (vo.gd("z") * 4 + vg.vl.get_height(vo.gi("x"), vo.gi("y"))) / 5);
                vo.s("ay", (vo.gd("ay") * 8 + Math.atan2(vg.vl.get_height((int) (vo.gd("x") + Math.cos(vo.gd("a")) * 64), (int) (vo.gd("y") + Math.sin(vo.gd("a")) * 64)) - vo.gd("z"), 32)) / 9);
                if (Math.abs(vg.vl.get_height((int) (vo.gd("x") + Math.cos(vo.gd("a")) * 64), (int) (vo.gd("y") + Math.sin(vo.gd("a")) * 64)) - vo.gd("z")) <= 20) {
                    vo.s("x", (vo.gd("x") + Math.cos(vo.gd("a")) * vo.type.gd("speed")));
                    vo.s("y", (vo.gd("y") + Math.sin(vo.gd("a")) * vo.type.gd("speed")));
                    vo.s("tt", vo.gi("tt")-1);
                    if (vo.gi("tt")<0) {
                        vo.s("tt", 22/vo.type.gd("speed"));
                        vg.trace(vo.type.g("voxelmodel"), new VPoint(vo.gd("x"), vo.gd("y"), vo.gd("z")), new VPoint(0, vo.gd("ay"), vo.gd("a")));
                    }
                }
                
            }
        }
        if (vo.type.g("hit_world").equals("yes")) {
            vg.vl.hit_world(vo.gi("x"), vo.gi("y"), vo.gi("z"));
        }
        vg.vl.open_map(vo.type.gi("vision"), vo.gi("x"), vo.gi("y"), vo.gi("z"));
    }

}

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
        if (vo.gi("ow") != vg.player && !vg.vl.get_visible(vo.gi("x"), vo.gi("y")) && vo.gi("hp") > 0) {
            return;
        }
        int vs = Math.abs(vo.gi("vs"));

        if (vs > 200) {
            vs = 200;
        } else if (vs < 0) {
            vs = 0;
        }
        Color c = new Color(255 - vs, 255, 255 - vs, 255 - vs);

        if (vo.gi("hp") < 0) {
            c = (new Color(50, 50, 50, Math.max(0, 255 - vo.gi("rm_time"))));
        }
        if (vo.type.g("model").equals("sprite")) {
            g.setColor(c);

            if (vo.gi("pt") > vo.type.gi("ptime") / 2 - 50) {
                int pt = vo.gi("pt");
                if (pt > vo.type.gi("ptime") / 2) {
                    pt = Math.min(Math.max((20 + (pt - vo.type.gi("ptime")) / 5), 10), 19);

                } else if (pt <= vo.type.gi("ptime") / 2) {
                    pt = vo.type.gi("ptime") / 2 - pt;

                    pt = Math.min(Math.max((50 - pt) / 5, 0), 10);
                }

                Sprite.render("building_back", g, pt, new VPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z") - cam.z));
            }

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

            g.setColor(c);
            VoxelModel.render(vo.type.g("voxelmodel"), g, new VPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z") - cam.z), new VPoint(0, vo.gd("ay"), vo.gd("a")));
            VPoint vp;

            vp = VConvert.to2DPoint((int) (vo.gd("x") - cam.x), (int) (vo.gd("y") - cam.y), (int) (vo.gd("z") - cam.z));
            VPoint vp2 = VConvert.to2DPoint((int) (vo.gd("x") - cam.x), (int) (vo.gd("y") - cam.y), (int) (0));
            g.setColor(new Color(255, 255, 255, 0));

        }
        int v = vo.type.gi("turrets");
        for (int i = 0; i < v; i++) {

            g.setColor(c);

            VoxelModel.render(vo.type.g("turret" + i + "_voxelmodel"), g, new VPoint(vo.gd("t" + i + "_x") - cam.x, vo.gd("t" + i + "_y") - cam.y, vo.gd("t" + i + "_z") - cam.z), new VPoint(0, 0, vo.gd("t" + i
                    + "_ra")), new VPoint(0, vo.gd("ay"), vo.gd("a")));

        }
        VPoint vp = VConvert.to2DPoint(vo.gd("x") - cam.x, vo.gd("y") - cam.y, vo.gd("z") - cam.z);
        g.setTexture("power_shell.png");
        /*
         g.drawRect((int) vp.x, (int) vp.y, 96, 96, r.nextInt(360));
         g.removeTexture();
         g.setColor(new Color(255, 0, 0, 100));
         vg.vl.sublight((int) (vo.gd("x")), (int) (vo.gd("y")), cam, g);
         //*/
        if (vo.gi("ow") != vg.player) {
            g.setTexture("enemy_detected.png");
            g.setColor(new Color(255, 100, 0, 255));

            g.drawRect((int) vp.x, (int) vp.y, 96, 96, 0);
        }
        g.setColor(Color.WHITE);
        if (vo.gi("hp") != vo.type.gi("hp")) {

            int hp = Math.min(10, Math.max((int) (vo.gd("hp") / vo.type.gd("hp") * 10.0), 0));
            g.setTexture("icon/armor/" + hp + ".png");
            g.drawRect((int) vp.x, (int) vp.y - 35, 22, 22, 0);
        }

    }

    @Override
    public void init(VGame vg, VObject vo) {
        vo.tag("#tick");
        vo.tag("#render");
        vo.tag("#unit");
        vo.tag("#solid");

        vo.s("hp", vo.type.gi("hp"));
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
                vo.s("!ax", args[1]);
                vo.s("!ay", args[2]);
                vo.s("!atk", "no");
            }
        }
        if (args[0].equals("hit")) {
            vo.s("hp", vo.gi("hp") - Integer.valueOf(args[1]));

        }
        if (args[0].equals("attack")) {
            if (vg.player == vo.gi("ow")) {
                vo.s("!ax", args[1]);
                vo.s("!ay", args[2]);
                vo.s("!atk", "yep");
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
        if (vo.gi("hp") < 0) {
            if (r.nextInt(10) == 0) {
                vg.addSpray(1, new VPoint(vo.gi("x"), vo.gi("y"), vo.gi("z")));
            }
            vo.s("rm_time", vo.gi("rm_time") + 1);
            if (vo.gi("rm_time") >= 255) {
                vo.remove();
            }
            return;
        }
        for (VObject obj : vg.getObjects(new VPoint(vo.gi("x"), vo.gi("y"), vo.gi("z")), vo.type.gi("haborites"), "#solid")) {
            if (obj == vo) {
                continue;
            }
            if (obj.type.g("phantom").equals("yes")) {
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
        if (!vo.g("!atk").equals("yep")) {
            vo.s("!atk", "no");

            for (VObject obj : vg.getObjects(new VPoint(vo.gi("x"), vo.gi("y"), vo.gi("z")), vo.type.gi("detect_range"), "#unit")) {
                if (obj == vo) {
                    continue;
                }
                if (obj.gi("ow") == vo.gi("ow") || obj.gi("vs") > 200 || obj.gi("hp") < 0) {
                    continue;
                }

                vo.s("!ax", obj.gd("x"));
                vo.s("!ay", obj.gd("y"));
                vo.s("!atk", "yes");
                break;

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

                    if (vo.type.g("hit_world").equals("yes")) {
                        vo.s("tt", vo.gi("tt") - 1);
                        if (vo.gi("tt") < 0) {
                            vo.s("tt", 22 / vo.type.gd("speed"));
                            vg.trace(vo.type.g("voxelmodel"), new VPoint(vo.gd("x"), vo.gd("y"), vo.gd("z")), new VPoint(0, vo.gd("ay"), vo.gd("a")));
                        }
                        vg.vl.hit_world(vo.gi("x"), vo.gi("y"), vo.gi("z"));
                    }
                }

            }
        }
        int v = vo.type.gi("turrets");
        for (int i = 0; i < v; i++) {
            double a = vo.gd("a");
            vo.s("t" + i + "_ra", vo.gd("t" + i + "_a") - a);
            vo.s("t" + i + "_x", vo.gi("x") + Math.cos(a) * Math.sin(vo.gd("ay")) * vo.type.gi("turret" + i + "_w"));
            vo.s("t" + i + "_y", vo.gi("y") + Math.sin(a) * Math.sin(vo.gd("ay")) * vo.type.gi("turret" + i + "_w"));
            vo.s("t" + i + "_z", vo.gi("z") + Math.cos(vo.gd("ay")) * vo.type.gi("turret" + i + "_h"));
            double a2 = (Math.atan2(vo.gd("!ay") - vo.gd("t" + i + "_y"), vo.gd("!ax") - vo.gd("t" + i + "_x")));
            double a1 = vo.gd("t" + i + "_a");

            double ts = vo.type.gd("turret" + i + "_turnspeed");
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
            if (Math.abs(a2 - a1) <= ts) {
                a1 = a2;

                if ((vo.g("!atk").equals("yes") || vo.g("!atk").equals("yep")) && vo.gi("t" + i + "_rld") == 0) {
                    vo.s(("t" + i + "_am"), vo.gi("t" + i + "_am") - 1);
                    if (vo.gi("t" + i + "_am") > 0) {
                        vo.s("t" + i + "_rld", vo.type.gi("turret" + i + "_reload"));
                    } else {
                        vo.s("t" + i + "_rld", vo.type.gi("turret" + i + "_reloadammo"));

                        vo.s("t" + i + "_am", vo.type.gi("turret" + i + "_ammo"));
                    }
                    vo.s("vs", 0);

                    vg.newObject(vo.type.g("turret" + i + "_bullet"), "x=" + vo.g("t" + i + "_x"), "y=" + vo.gi("t" + i + "_y"), "z=" + vo.g("t" + i + "_z"), "tx=" + vo.gi("!ax"), "ty=" + vo.gi("!ay"), "pw=100", "r=" + vo.type.gi("turret" + i + "_range"), "ow=" + vo.gi("ow"), "#tick", "#render");
                }
            }
            if (vo.gi("t" + i + "_rld") > 0) {
                vo.s("t" + i + "_rld", vo.gi("t" + i + "_rld") - 1);
            }
            vo.s("t" + i + "_a", a1);

        }
        if (vo.gi("ow") == vg.player) {
            vg.vl.open_map(vo.type.gi("vision"), vo.gi("x"), vo.gi("y"), vo.gi("z"));
        }

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgame;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import vengine.CFG;
import vengine.List;
import vengine.Sprite;
import vengine.VAStarTurn;
import static vengine.VEngine.cfg;
import vengine.VFileReader;
import vengine.VGUILayer;
import vengine.VGraphicLayer;
import vengine.VGraphics;
import vengine.VPoint;
import vengine.VoxelModel;
import vengine.gui.VIconButton;
import vengine.gui.VUnitIconButton;

/**
 *
 * @author yew_mentzaki
 */
public class VGame {

    public int playerfaction = 0;
    public int player;
    public Random r = new Random();
    public Point playercam = new Point(0, 0);
    private VSpray[] vsp = new VSpray[256];
    public VLandschaft vl = new VLandschaft();
    private ArrayList<VObject> vo = new ArrayList<VObject>();
    private VGame vg = this;
    private long serial;
    private CFG cfg;
    private CFG[] types;
    private CFG[] factions;
    private ArrayList<VObject> selected[];
    private String placeBuilding = null;
    public VObject[] players = new VObject[16];

    public CFG type(String name) {
        for (CFG t : types) {
            if (t.g("gamename").equals(name)) {
                return t;
            }
        }
        return null;
    }

    public VGame(String... args) throws InterruptedException {

        this.selected = new ArrayList[10];
        for (int i = 0; i < 10; i++) {
            selected[i] = new ArrayList<VObject>();
        }
        VGraphics.getGraphicManager().removeAllLayers();
        cfg = VFileReader.readCFG(new File(vengine.VEngine.cfg.g("modhome") + "configuration.cfg"));
        for (String param : args) {
            try {
                String[] pr = param.split("=");
                cfg.s(pr[0], pr[1]);
            } catch (Exception e) {

            }
        }

        String v = cfg.g("version");
        List factions = VFileReader.readList(new File(vengine.VEngine.cfg.g("modhome") + v + "/factions.list"));
        List units = VFileReader.readList(new File(vengine.VEngine.cfg.g("modhome") + v + "/units.list"));
        types = new CFG[units.size() + 1];
        this.factions = new CFG[factions.size()];
        for (int i = 0; i < types.length - 1; i++) {
            types[i] = VFileReader.readCFG(new File(vengine.VEngine.cfg.g("modhome") + v + "/units/" + units.get(i) + ".cfg"));
        }
        CFG player = new CFG();
        player.s("gamename", "VPlayer");
        player.s("processor", "VPlayer");
        types[units.size()] = player;
        for (int i = 0; i < this.factions.length; i++) {
            this.factions[i] = VFileReader.readCFG(new File(vengine.VEngine.cfg.g("modhome") + v + "/factions/" + factions.get(i) + ".cfg"));
        }
        /*
         Globals gl = new Globals();
         gl.load(new Spawn(this));
         //*/
        new Thread() {

            @Override
            public void run() {
                Scanner s = new Scanner(System.in);
                System.out.println("HScript ready:");
                while (true) {
                    try {
                        System.out.print(">");
                        String prm[] = s.nextLine().split(" ");

                        String cmd = prm[0];
                        String prms[] = new String[prm.length - 1];
                        for (int i = 0; i < prm.length - 1; i++) {
                            prms[i] = prm[i + 1];
                        }
                        command(cmd, prms);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
        Thread.sleep(2000);
        Random r = new Random();

    }
    float selectora;
    int selectors = 96;
    int mousetimer = 0;

    public void initRender() {

        for (int i = 0; i < 16; i++) {
            players[i] = newObject("VPlayer", "name=Player " + i, "ow=" + i, "#tick");
        }
        VGraphics.getGraphicManager().addLayer(new VGraphicLayer() {

            @Override
            public void init() {
                VGraphics.getGraphicManager().addLayer(new VGraphicLayer() {

                    @Override
                    public void render(VGraphics g) {
                        renderObjects(g);
                    }
                });
                VGraphics.getGraphicManager().addLayer(new VGUILayer() {

                    @Override
                    public void render(VGraphics g) {
                        super.render(g);
                        g.setTexture("gui/bolt.png");
                        int p = (players[player].gi("power") / 20);
                        int h = (players[player].gi("rpers"));
                        int nh = (players[player].gi("pers"));
                        g.drawRect(120, 120, 30, 30, p - r.nextInt(p / 15 + 1));

                        if (h < nh) {
                            g.setColor(Color.RED);
                        }
                        if (h > nh) {
                            g.setColor(Color.GREEN);
                        }

                        g.drawRect(155, 120, 20, 28, 180 - nh);
                        g.setColor(Color.WHITE);

                        g.drawString(String.valueOf(Math.min(99999, players[player].gi("money"))), 187, 111);

                    }

                    @Override
                    public void init() {
                        add(new VIconButton("Radar", "gui/map.png", 20, 20, 100, 100) {
                        });
                        add(new VIconButton("Energy", "gui/energy.png", 105, 105, 30, 30) {
                        });
                        add(new VIconButton("Humans", "gui/humans.png", 140, 105, 30, 30) {
                        });
                        add(new VIconButton("Money", "gui/money.png", 175, 105, 90, 30) {
                        });
                        /*
                         add(new VIconButton("Buildings", "gui/button_house.png", 20, 120) {});
                         add(new VIconButton("Vehicles", "gui/button_tank.png", 20, 160) {
                         });
                        
                         //*/
                        {
                            List s = VFileReader.readList(new File(vengine.VEngine.cfg.g("modhome") + cfg.g("version") + "/factions/" + factions[playerfaction].g("sname") + "_buildings.list"));
                            int i = 120;
                            for (final String c : s) {
                                add(new VUnitIconButton("", c + ".png", 20, i) {
                                    String s = c;

                                    @Override
                                    public void clicked() {
                                        super.clicked();

                                        placeBuilding = s;

                                        mousetimer = 3;
                                    }

                                });
                                i += 64;
                            }
                        }
                        {
                            List s = VFileReader.readList(new File(vengine.VEngine.cfg.g("modhome") + cfg.g("version") + "/factions/" + factions[playerfaction].g("sname") + "_units.list"));
                            int i = 120;
                            for (final String c : s) {

                                add(new VUnitIconButton("", c + ".png", i, 20) {
                                    String s = c;

                                    @Override
                                    public void clicked() {
                                        super.clicked();

                                        mousetimer = 3;
                                        VObject[] vo = getObjects(new VPoint(playercam.x + 600, playercam.y + 600, 0), 5000, "#tick");
                                        if(players[player].gi("money")>type(s).gi("price"));
                                        for (VObject o : vo) {
                                            if (o.gi("pt") == 0 && o.type.g("building_type").equals("factory") && o.gi("!std") == 0) {

                                                try {
                                                    o.vp.call(vg, o, "buildunit", s);
                                                    players[player].s("money", players[player].gi("money") - type(s).gi("price"));

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                return;
                                            } else {

                                            }
                                        }
                                    }

                                });
                                i += 64;
                            }
                        }
                    }

                });
                for (CFG cf : types) {
                    if (cf.g("model").equals("voxel")) {
                        VoxelModel.load(cf.g("voxelmodel"));
                    }
                    if (cf.g("model").equals("sprite")) {
                        Sprite.load(cf.g("sprite"));
                    }
                }
            }

            @Override
            public void render(VGraphics g) {
                vl.render(g, playercam);
            }
        });

        Timer timer = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tick();
            }
        });
        Timer timer2 = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tick();
            }
        });
        Timer timer3 = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tick();
            }
        });
        Timer timer4 = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                tick();
            }
        });
        try {
            timer.start();
            Thread.sleep(25);
            timer2.start();
            Thread.sleep(25);
            timer3.start();
            Thread.sleep(25);
            timer4.start();
        } catch (Exception e) {
        }
        newObject("mkw", "x=0", "y=0", "ow=0", "#tick", "#render");
                
    }

    public void addSpray(int type, VPoint v) {
        if (r.nextBoolean()) {
            return;
        }
        for (int i = 0; i < vsp.length; i++) {
            if (vsp[i] == null) {
                vsp[i] = new VSpray(type, r, v);
                return;
            } else if (vsp[i].l <= 0) {
                vsp[i] = new VSpray(type, r, v);
                return;
            }
        }
    }
    ArrayList<VTrace> vt_add = new ArrayList<VTrace>();
    ArrayList<VTrace> vt = new ArrayList<VTrace>();
    
    public void renderObjects(VGraphics g) {
        int mx = Mouse.getX(), my = g.getHeight() - Mouse.getY();
        if (mousetimer > 0) {
            mousetimer--;
        }
        boolean select = false;
        boolean selected = false;
        VPoint mp = VConvert.toPoint(mx, my + 60);
        boolean canbuild = true;
        mp.x += playercam.x;
        mp.y += playercam.y;
        VPoint camera = new VPoint(playercam.x, playercam.y, 0);
        for (int i = 0; i < vt_add.size(); i++) {
            vt.add(vt_add.get(0));
            vt_add.remove(0);
        }
        for (int i = 0; i < vt.size(); i++) {
            if(vt.get(i).l<=0){
                vt.remove(i);
                i--;
            }
        }
        try {
            for (VTrace t : vt) {
            t.render(camera, g);
        }
        } catch (Exception e) {
        }
        
        for (VObject vo : getObjectsForRender("#render")) {
            if (Math.sqrt(Math.pow(mp.x - vo.gd("x"), 2) + Math.pow(mp.y - vo.gd("y"), 2)) < 128) {
                canbuild = false;
            }

            if (Mouse.isButtonDown(1) && mousetimer <= 0) {
                this.selected[0].clear();
                mousetimer = 5;
            }
            VPoint v = VConvert.to2DPoint((int) vo.gd("x") - playercam.x, (int) vo.gd("y") - playercam.y, (int) vo.gd("z"));

            if (v.x > -100 && v.y > -100 && v.x < 100 + g.getWidth() && v.y < 100 + g.getHeight()) {
                vo.vp.render(this, vo, new VPoint(playercam.x, playercam.y, 0), g);
                if (placeBuilding == null) {
                    double d = VConvert.dist(mx, my, v.x, v.y);
                    if (d < 32) {
                        select = true;
                        mx = (int) v.x;
                        my = (int) v.y;
                        if (Mouse.isButtonDown(0)) {

                            selected = true;
                            if (!(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
                                this.selected[0].clear();
                            }
                            this.selected[0].add(vo);

                            mousetimer = 5;
                        }
                    }
                }
                if (this.selected[0].contains(vo)) {
                    g.setColor(Color.white);
                    g.setTexture("unit_selected.png");

                    g.drawRect((int) v.x, (int) v.y, 64, 64, 0);
                }
            }

            if (placeBuilding == null && !selected && Mouse.isButtonDown(0) && mousetimer <= 0) {
                VPoint vpp = VConvert.toPoint(mx, my + 30);
                int x = (int) vpp.x + playercam.x;
                int y = (int) vpp.y + playercam.y;

                for (VObject voo : this.selected[0]) {

                    voo.vp.call(this, voo, "move", "" + x, "" + y);
                }
                mousetimer = 5;
            }

        }
        for (int i = 0; i < vsp.length; i++) {
            if (vsp[i] != null) {
                vsp[i].render(new VPoint(playercam.x, playercam.y, 60), vg, g);
                if (vsp[i].l <= 0) {
                    vsp[i] = null;
                }
            }

        }
        vl.render_fow(g, playercam);
        if (placeBuilding == null && select) {
            g.setColor(Color.green);
            g.setTexture("map_selector.png");
            selectora /= 1.2;
            if (selectors > 64) {
                selectors--;
            }
            g.drawRect(mx, my, selectors, selectors, selectora);
        } else {
            g.setColor(Color.yellow);
            g.setTexture("map_selector.png");
            if (selectora >= 45) {
                selectora -= 90;
            }
            selectora += 4.0f;
            if (selectors < 96) {
                selectors++;
            }
            g.drawRect(mx, my, selectors, selectors, selectora);
        }

        if (mx < 10) {
            playercam.x -= 10;
            playercam.y += 10;

        }
        if (my < 10) {
            playercam.x -= 10;
            playercam.y -= 10;
        }
        if (mx > g.getWidth() - 10) {
            playercam.x += 10;
            playercam.y -= 10;
        }
        if (my > g.getHeight() - 10) {

            playercam.x += 10;
            playercam.y += 10;
        }
        if (placeBuilding != null && mousetimer <= 0) {
            g.setColor(new Color(canbuild ? 0 : 255, canbuild ? 255 : 0, 0, 128));
            VPoint vp = VConvert.toPoint(mx, my);
            Sprite.render(placeBuilding, g, 0, vp);
            if (Mouse.isButtonDown(0) && canbuild && players[player].gi("money") >= type(placeBuilding).gi("price")&&180-players[player].gi("p")>type(placeBuilding).gi("crew")) {
                players[player].s("money", players[player].gi("money") - type(placeBuilding).gi("price"));

                newObject(placeBuilding, "x=" + (vp.x + 60 + playercam.x), "y=" + (vp.y + 60 + playercam.y), "ow=" + player, "#tick", "#render");
                placeBuilding = null;
                mousetimer = 5;
            }

        }
        if (Mouse.isButtonDown(1)) {

            placeBuilding = null;
        }
    }

    public void tick() {
        try {

            for (VObject vo : getObjects("#tick")) {
                try {
                    vo.vp.tick(this, vo);
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
        }

        vl.clear_map();
    }

    public void command(String cmd, String... params) {

        if (cmd.equals("new")) {
            newObject(params[0], params);
        }
        if (cmd.equals("print")) {
            for (String p : params) {
                System.out.print(p + " ");
            }
            System.out.println();
        }
        if (cmd.equals("list")) {
            if (params[0].equals("processors")) {
                VProcessor.list();
            }
        }
    }

    public void newObject(long serial) {

        vo.add(new VObject(serial++, this, "EmptyProcessor", ""));

    }

    public VObject newObject(String processor, String... params) {
        VObject vo = (new VObject(serial++, this, processor, params));

        this.vo.add(vo);
        return vo;
    }

    public VObject getObject(long index) {
        for (VObject vo : this.vo) {
            if (vo.isS(index)) {
                return vo;
            }
        }
        return null;
    }

    public VObject[] getObjects(VPoint t, int radius, String... tags) {
        ArrayList<VObject> vos = new ArrayList<VObject>();
        ArrayList<Integer> voi = new ArrayList<Integer>();
        for (VObject vo : this.vo) {
            if (vo.isT("#tick")) {
                int j = (int) Math.sqrt(Math.pow(t.x - vo.gd("x"), 2) + Math.pow(t.y - vo.gd("y"), 2));
                if (j < radius) {
                    voi.add(j);
                    vos.add(vo);

                }
            }
        }
        for (int i = 0; i < vos.size() - 1; i++) {
            for (int j = 0; j < vos.size() - i - 1; j++) {
                if (voi.get(j) > voi.get(j + 1)) {
                    int voi_i = voi.get(j);
                    Collections.swap(vos, j, j + 1);
                    Collections.swap(voi, j, j + 1);
                }
            }
        }
        VObject[] voz = new VObject[vos.size()];
        for (int i = 0; i < voz.length; i++) {
            voz[i] = vos.get(i);
        }
        return voz;
    }

    public VObject[] getObjects(String... tags) {
        ArrayList<VObject> vos = new ArrayList<VObject>();
        for (VObject vo : this.vo) {
            if (vo.isT(tags)) {
                vos.add(vo);
            }
        }
        VObject[] voz = new VObject[vos.size()];
        for (int i = 0; i < voz.length; i++) {
            voz[i] = vos.get(i);
        }
        return voz;
    }

    public VObject[] getObjectsForRender(String... tags) {
        ArrayList<VObject> vos = new ArrayList<VObject>();
        ArrayList<Integer> voi = new ArrayList<Integer>();
        for (VObject vo : this.vo) {
            if (vo.isT(tags)) {
                vos.add(vo);
                voi.add(vo.vp.y(vo));
            }
        }
        for (int i = 0; i < vos.size() - 1; i++) {
            for (int j = 0; j < vos.size() - i - 1; j++) {
                if (voi.get(j) > voi.get(j + 1)) {
                    int voi_i = voi.get(j);
                    Collections.swap(vos, j, j + 1);
                    Collections.swap(voi, j, j + 1);
                }
            }
        }

        VObject[] voz = new VObject[vos.size()];
        for (int i = 0; i < voz.length; i++) {
            voz[i] = vos.get(i);
        }
        return voz;
    }

    public void trace(String g, VPoint vPoint, VPoint vPoint0) {
        try {
        vt_add.add(new VTrace(g, vPoint, vPoint0));     
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

}

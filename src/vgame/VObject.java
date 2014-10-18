/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgame;

import java.awt.Point;
import java.util.ArrayList;
import vengine.CFG;

/**
 *
 * @author yew_mentzaki
 */
public class VObject {

    public Point[] way = new Point[64];
    public VProcessor vp;
    private boolean ownedbyplayer = false;
    private long serial;
    private VGame game;
    public CFG type;

    private ArrayList<String> params = new ArrayList<String>(),
            values = new ArrayList<String>(),
            tags = new ArrayList<String>(),
            events = new ArrayList<String>();

    public boolean isS(long s) {
        return s == serial;
    }

    public int y() {
        return (int) VConvert.to2DPoint((int) gd("x"), (int) gd("y"), 0).y;
    }

    public boolean isT(String... tags) {
        for (String tag_1 : tags) {
            for (String tag_2 : this.tags) {
                if (tag_1.equals(tag_2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public VObject(long serial, VGame game, String type, String... params) {
        this.game = game;
        this.type = game.type(type);
        vp = VProcessor.get(this.type.g("processor"));
        vp.init(game, this);
        for (String param : params) {
            if (param.contains("=")) {
                try {
                String s[] = param.split("=");
                s(s[0], s[1]);    
                } catch (Exception e) {
                    System.err.println("Something wrong here: " + param);
                }
                
            } else if (param.charAt(0) == '#') {
                tags.add(param);
            } else if (param.charAt(0) == '$') {
                events.add(param);
            }
        }
        vp.post_init(game, this);
    }

    public void tag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void untag(String tag) {
        tags.remove(tag);
    }

    public void server_set(String p, String v) {
        if (ownedbyplayer && !(p.charAt(0) == '!')) {
            if (params.contains(p)) {
                values.set(params.indexOf(p), v);
            } else {
                params.add(p);
                values.add(v);
            }
        }
    }

    public void s(String p, double v) {
        if (params.contains(p)) {
            values.set(params.indexOf(p), String.valueOf(v));
        } else {
            params.add(p);
            values.add(String.valueOf(v));
        }
    }

    public void s(String p, String v) {
        if (params.contains(p)) {
            values.set(params.indexOf(p), v);
        } else {
            params.add(p);
            values.add(v);
        }
    }

    public String g(String p) {
        if (params.contains(p)) {
            return values.get(params.indexOf(p));
        } else {
            return "";
        }
    }

    public double gd(String p) {
        try {
            if (params.contains(p)) {
                return Double.parseDouble(values.get(params.indexOf(p)));
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int gi(String p) {
        try {
            if (params.contains(p)) {
                return (int) Double.parseDouble(values.get(params.indexOf(p)));
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return type.g("processor")+"-"+serial;
    }
    
    public void remove() {
        game.remove(this);
    }
}

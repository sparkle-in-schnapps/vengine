/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgame;

import java.awt.Color;
import java.util.Random;
import vengine.Sprite;
import vengine.VGraphics;
import vengine.VPoint;

/**
 *
 * @author yew_mentzaki
 */
public class VSpray {

    Random r;
    int a;
    byte i;
    int l = 100;
    VPoint s;

    public VSpray(int n, Random r, VPoint s) {

        this.r = r;
        a = r.nextInt(360);
        i = (byte) n;
        this.s = s;
        if (i == 2) {
            l = 25;
        }
        if (i == 3) {
            l = 7;
        }
        if (i == 4) {
            l = 5;
        }
    }

    public void render(VPoint cm, VGame vg, VGraphics g) {
        if (l > 0) {
            l--;
        }
        if (l <= 0) {
            return;
        }

        VPoint v = new VPoint(s.x - cm.x, s.y - cm.y, s.z);
        VPoint osv = VConvert.to2DPoint(v.x, v.y, v.z);

        switch (i) {
            case 1:
                g.setTexture("eft/smoke.png");
                g.setColor(new Color(255, 255, 255, l * 2));
                g.drawRect((int) osv.x, (int) osv.y, 32, 32, a);
                s.x += (r.nextInt(6) - 2);
                s.y -= 1;
                s.z += (l) / 34;
                break;
            case 2:
                g.setTexture("eft/smoke.png");
                g.setColor(new Color(255, 255, 255, l * 8));
                g.drawRect((int) osv.x, (int) osv.y, 16, 16, a);
                s.x += (r.nextInt(6) - 2);
                s.y -= 1;
                s.z += (l) / 34;
                break;
            case 3:
                g.setColor(new Color(255, 255, 255, 255));
                Sprite.render("bullet", g, 7 - l, v);

                break;
            case 4:
                g.setColor(new Color(255, 255, 255, 255));
                Sprite.render("gauss_bullet", g, 5 - l, v);

                break;
            default:

        }
    }

}

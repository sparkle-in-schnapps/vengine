/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vp;

import vengine.VGraphics;
import vengine.VPoint;
import vgame.VGame;
import vgame.VObject;

/**
 *
 * @author yew_mentzaki
 */
public class Plugin {
    PluginInterface i;
    public Plugin (PluginInterface i){
        this.i = i;
    }
    
    public void renderScene(vgame.VGame vg, VGraphics g) {
        i.renderScene(vg, g);
    }

    public void renderUnit(VGame vg, VObject vo, VPoint cam, VGraphics g) {
        i.renderUnit(vg, vo, cam, g);
    }

    public void renderInterface(vgame.VGame vg, VGraphics g) {
        i.renderInterface(vg, g);
    }

    public void initGame(vgame.VGame vg) {
        i.initGame(vg);
    }
}

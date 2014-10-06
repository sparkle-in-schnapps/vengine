/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vexamples.abilities;

import vgame.VAbility;
import vgame.VGame;
import vgame.VObject;

/**
 *
 * @author yew_mentzaki
 */
public class Place_base extends VAbility {

    /*
    
     Not enough energy.
     Not enough free staff.
     Not enough money.
     Barracks filled.
     Reinforcements arrived.
     Energy restored. 
     Construction completed.
     Unit prepared.
     Unit captured.
     Unit lost.
     Our army is under attack.
     Our base is under attack. 
     Troops attacked.
     Unable to build here.
     Can not to put here.
     
     */
    public Place_base() {
        icon = "place_base";
        name = "Place Base";
    }

    @Override
    public boolean can(VObject vo, VGame vg) {
        return true;
    }

    @Override
    public void use(VObject vo, VGame vg) {
        if (vo.type.g("gamename").equals("mkw")) {
            vg.newObject(vo.type.g("base"), "x=" + vo.g("x"), "y=" + vo.g("y"), "ow=" + vo.g("ow"));
            vo.s("hp", "-100");
            vo.remove();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vgame;

/**
 *
 * @author yew_mentzaki
 */
public abstract class VAbility {
    public String name;
    public String icon;
    public abstract boolean can(VObject vo, VGame vg);
    public abstract void use(VObject vo, VGame vg);
}

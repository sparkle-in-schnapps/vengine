/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgame;

import java.awt.Color;
import java.util.ArrayList;
import vengine.VGraphics;
import vengine.VPoint;
import vengine.VoxelModel;

/**
 *
 * @author yew_mentzaki
 */
public class VTrace {

    private static ArrayList<String> name = new ArrayList<String>();
    private static ArrayList<VoxelModel> voxe = new ArrayList<VoxelModel>();
    VoxelModel vm;
    String unit;
    VPoint vp, vc;
    int l = 250;

    public VTrace(String unit, VPoint vp, VPoint vc) {
        this.vp = vp;
        this.vc = vc;
        this.unit = unit;

    }

    public void render(VPoint camera, VGraphics g) {
        if(l<0)return;
        l--;
        if (unit != null) {
            int i = name.indexOf(unit);
            if (i == -1) {
                vm = VoxelModel.load(unit+"_trace");
                voxe.add(vm);
            } else {
                vm = voxe.get(i);
            }
            unit = null;
        }
        g.setColor(new Color(0, 0, 0, l/2));
        VoxelModel.render(vm, g, new VPoint(vp.x - camera.x, vp.y - camera.y, vp.z - camera.z), vc);

    }
}

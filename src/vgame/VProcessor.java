/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vgame;

import java.util.ArrayList;
import vengine.VGraphics;
import vengine.VPoint;

/**
 *
 * @author yew_mentzaki
 */
public class VProcessor {
   
    private static final VProcessor emptyProcessor = new VProcessor();
    private static final ArrayList<VProcessor> processors = new ArrayList<VProcessor>();
    public static void add(VProcessor vp){
        processors.add(vp);
    }
    public static void list(){
        System.out.println("Processors");
        for (VProcessor vp : processors) {
            System.out.println("* " + vp.name);
        }
    }
    public int y(VObject vo) {
        return (int) VConvert.to2DPoint((int) vo.gd("x"), (int) vo.gd("y"), 0).y;
    }
    public static VProcessor get(String processorname){
        for (VProcessor vp : processors) {
            if(vp.name.equals(processorname)){
                return vp;
            }
        }
        return emptyProcessor;
    }
    public String name = "EmptyProcessor";
    public void load(){
        
    }
    public void init(VGame vg, VObject vo){
        
    }
    public void tick(VGame vg, VObject vo){
        
    }
    public void post_init(VGame vg, VObject vo){
        
    }
    public void render(VGame vg, VObject vo, VPoint cam, VGraphics g){
        
    }
    public void call(VGame vg, VObject vo, String... args){
        
    }
}

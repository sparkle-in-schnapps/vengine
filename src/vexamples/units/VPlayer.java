/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vexamples.units;

import vgame.VGame;
import vgame.VObject;
import vgame.VProcessor;

/**
 *
 * @author yew_mentzaki
 */
public class VPlayer extends VProcessor {

    public VPlayer() {
        name = "VPlayer";
    }

    @Override
    public void call(VGame vg, VObject vo, String... args) {
        super.call(vg, vo, args); //To change body of generated methods, choose Tools | Templates.
        if (args[0].equals("pw")) {
            int power = vo.gi("power");
            int c = Integer.parseInt(args[1]);
            if (c > 0 & power <= 3600 + c) {
                vo.s("power", power + c);
            }
            if (c < 0 & power >= -c) {
                vo.s("power", power + c);
            }

        }
    }

    @Override
    public void init(VGame vg, VObject vo) {
        super.init(vg, vo); //To change body of generated methods, choose Tools | Templates.
        vo.tag("#tick");
        vo.s("money", 10000);
    }

    @Override
    public void tick(VGame vg, VObject vo) {
        super.tick(vg, vo); //To change body of generated methods, choose Tools | Templates.
        int tm = vo.gi("tm");
        
        if(tm>0){
            tm--;
            vo.s("tm", tm);
            return;
        }else{
            
            vo.s("tm", 10);
            
        }
        int ow = vo.gi("ow");
        int personal = 0;
        
        for (VObject o : vg.getObjects("#tick")) {
            
            if (o.gi("ow") == ow && o.gi("hp") > 0) {
                
                personal += o.type.gi("crew");
            }
        }
        vo.s("rpers", personal);
        int npersonal = vo.gi("pers");
        if(personal > npersonal)npersonal++;
        if(personal < npersonal)npersonal--;
        vo.s("p", Math.max(npersonal, personal));
        vo.s("pers", npersonal);
    }

}

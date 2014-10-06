/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import org.lwjgl.input.Mouse;
import vengine.gui.VContainer;
import vengine.gui.VElement;

/**
 *
 * @author yew_mentzaki
 */
public class VGUILayer extends VGraphicLayer {

    private VContainer vc = new VContainer();

    public void add(VElement ve) {
        vc.add(ve);
    }

    public void remove(VElement ve) {
        vc.remove(ve);
    }
    int wait = 0;
    @Override
    public void render(VGraphics g) {
        vc.render(g);
        if (wait > 0)wait--;
        if (Mouse.isButtonDown(0)&&wait==0) {
            
            
            wait = (vc.isClicked(Mouse.getX(), g.getHeight() - Mouse.getY())?5:0);
        }
    }

}

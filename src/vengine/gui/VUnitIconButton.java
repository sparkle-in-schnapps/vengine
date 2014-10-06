/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vengine.gui;

import java.awt.Color;
import vengine.VGraphics;

/**
 *
 * @author yew_mentzaki
 */
public abstract class VUnitIconButton extends VElement{
    String text, icon;
    int x, y, w, h;

    public VUnitIconButton(String text, String icon, int x, int y) {
        this.text = text;
        this.icon = icon;
        this.x = x;
        this.y = y;
        this.w = 64;
        this.h = 64;
    }

    @Override
    public boolean isClicked(int x, int y) {
        
        return (this.x <= x && this.y <= y && x <= this.x+w && y <= this.y+h);
    }

    @Override
    public void render(VGraphics g) {
        
        g.setColor(new Color(255, 255, 255, 155));
        g.setTexture("icon/" + icon);
        g.drawRect(x, y, w, h);
         g.setColor(new Color(255, 255, 255, 255));
        g.setTexture("gui/button.png");
        g.drawRect(x, y, w, h);
        String s[] = text.split("\n");
        for (int i = 0; i < s.length; i++) {
        g.drawString(s[i], x, y + h + i * 20, w);
        }
    }

    
    
}

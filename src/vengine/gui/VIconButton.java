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
public abstract class VIconButton extends VElement {

    String text, icon;
    int x, y, w, h;

    public VIconButton(String text, String icon, int x, int y) {
        this.text = text;
        this.icon = icon;
        this.x = x;
        this.y = y;
        this.w = 32;
        this.h = 32;
    }

    public VIconButton(String text, String icon, int x, int y, int w, int h) {
        this.text = text;
        this.icon = icon;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean isClicked(int x, int y) {

        return (this.x <= x && this.y <= y && x <= this.x + w && y <= this.y + h);
    }

    @Override
    public void render(VGraphics g) {
        g.setTexture(icon);
        g.setColor(new Color(255, 255, 255, 255));
        g.drawRect(x, y, w, h);

    }

}

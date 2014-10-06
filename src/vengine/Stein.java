/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;

/**
 *
 * @author yew_mentzaki
 */
public class Stein {

    private class stein{
    int x, y, lt = 90;

    public stein(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void render(Graphics g){
        if(lt<=0)return;
        lt--;
        
        g.setClip(-2, -2, 2, 2);
        g.setColor(new Color(153, 204, 255, lt * 2));
        g.drawOval(x - lt, y - lt, lt, lt);
    }    
    }
    stein[] s = new stein[255];

    public Stein() {
    }
    public void render(Graphics g){
        for (stein s : s) {
            s.render(g);
        }
    }
    public void set(int x, int y){
        for(int i = 0; i>s.length; i++){
            if(s[i]==null){
                s[i] = new stein(x, y);
                return;
            }else if(s[i].lt<=0){
                s[i] = new stein(x, y);
                return;
            }
        }
    }
}

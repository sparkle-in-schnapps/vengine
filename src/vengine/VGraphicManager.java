/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vengine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author yew_mentzaki
 */
public final class VGraphicManager {
    private ArrayList<VGraphicLayer> vgl = new ArrayList<VGraphicLayer>();
    private ArrayList<VGraphicLayer> add_vgl = new ArrayList<VGraphicLayer>();
    private ArrayList<VGraphicLayer> remove_vgl = new ArrayList<VGraphicLayer>();
    private VGraphicLayer emptyLayer = new VGraphicLayer() {
        float i = 0;
        @Override
        public void render(VGraphics g) {
            g.setColor(Color.white);
            g.setTexture("vau/background.png");
            g.drawRect(0, 0, g.getWidth()+50, g.getHeight()+50);
            
            i+=5.0f;
            g.setTexture("vau/loading.png");
            g.drawRect(64, 64, 64, 64, i);
            g.drawRect(96, 64, 64, -64, -i-90);
            g.setTexture("vau/loading_word.png");
            g.drawRect(32, 96, 128, 128);
            g.drawString("Programmer: Yew_Mentzaki (http://grts3.ru/yew_mentzaki.php)", 135, 35);
            g.drawString("Music: Alex Mason (http://freemusicarchive.org/music/Alex_Mason/)", 135, 75);
        }
    };
    public void addLayer(VGraphicLayer vgl){
        add_vgl.add(vgl);
    }
    public void removeLayer(VGraphicLayer vgl){
        remove_vgl.add(vgl);
    }
    public void removeAllLayers(){
        for (VGraphicLayer vg : vgl) {
            remove_vgl.add(vg);
        }
    }
    public void render(VGraphics g){
        long d = new Date().getTime();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        for (int i = 0; i < add_vgl.size(); i++) {
            vgl.add(add_vgl.get(0));
            add_vgl.remove(0);
        }
        for (int i = 0; i < remove_vgl.size(); i++) {
            vgl.remove(remove_vgl.get(0));
            remove_vgl.remove(0);
        }
        for (VGraphicLayer vgl : this.vgl) {
            vgl.set();
            vgl.render(g);
        }
        if(vgl.isEmpty()){
            emptyLayer.set();
            emptyLayer.render(g);
        }
        long t = new Date().getTime();
        //g.drawString(String.valueOf(1000/Math.max(1, t - d)), 20, 20);
    }
}

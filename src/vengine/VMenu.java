/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import java.awt.Color;
import java.awt.Point;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import vengine.gui.VButton;
import vengine.gui.VCheckButton;
import vengine.gui.VContainer;
import vgame.VGame;
import vgame.VLandschaft;

/**
 *
 * @author yew_mentzaki
 */
public class VMenu {

    public VMenu() {
        int dx;
        VGraphics.getGraphicManager().addLayer(new VGraphicLayer() {
            VLandschaft vl = new VLandschaft();
            float a = 0;

            @Override
            public void render(VGraphics g) {

                a += 0.008f;
                float a = this.a;
                Point p = new Point(256 * 64 + (int) (640.0 * Math.cos(a)), 256 * 64 + (int) (640.0 * Math.sin(a)));
                vl.render(g, p);
                g.setTexture("vau/logo.png");
                g.setColor(Color.white);
                g.drawSprite(g.getWidth() / 2, 312, 512, 512);
            }
        });

        VGUILayer vgl = new VGUILayer() {
            boolean canStartLaunch;
            @Override
            public void render(VGraphics g) {
                super.render(g); //To change body of generated methods, choose Tools | Templates.
                
            }
            
            @Override
            public void init() {
                
                int dx = VGraphics.WIDTH / 2 - 200;
                VContainer mainmenu = new VContainer();
                mainmenu.add(new VButton("Campaign/Кампания", dx, 200, 400, 20) {
                });
                mainmenu.add(new VButton("Skirmish", dx, 230, 400, 20) {

                    @Override
                    public void clicked() {
                        VGraphics.getGraphicManager().removeAllLayers();
                        Thread t = new Thread(){

                            @Override
                            public void run() {
                                
                                VGame g;
                                try {
                                    g = new vgame.VGame("game=skirmish");
                                    g.initRender();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(VMenu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                            
                        
                        };
                        t.start();
                       
                    }
                    
                });
                mainmenu.add(new VButton("Internet skirmish", dx, 260, 400, 20) {
                });
                mainmenu.add(new VButton("Local network skirmish", dx, 290, 400, 20) {
                });
                mainmenu.add(new VCheckButton("Options", dx, 320, 400, 20) {
                });
                mainmenu.add(new VButton("Exit", dx, 350, 400, 20) {

                    @Override
                    public void clicked() {
                        VGraphics.getGraphicManager().removeAllLayers();
                    }
                    
                });
                add(mainmenu);
            }
        };

        VGraphics.getGraphicManager().addLayer(vgl);
        
    }
}

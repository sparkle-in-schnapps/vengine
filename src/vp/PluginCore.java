/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vp;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import vengine.VEngine;
import vengine.VGraphics;
import vengine.VPoint;
import vgame.VGame;
import vgame.VObject;

/**
 *
 * @author yew_mentzaki
 */
public class PluginCore {

    ArrayList<Plugin> p = new ArrayList<Plugin>();

    public void renderScene(vgame.VGame vg, VGraphics g) {
        for (Plugin i : p) {
            i.renderScene(vg, g);
        }
    }

    public void renderUnit(VGame vg, VObject vo, VPoint cam, VGraphics g) {
        for (Plugin i : p) {
            i.renderUnit(vg, vo, cam, g);
        }
    }

    public void renderInterface(vgame.VGame vg, VGraphics g) {
        for (Plugin i : p) {
            i.renderInterface(vg, g);
        }
    }

    public void initGame(vgame.VGame vg) {
        for (Plugin i : p) {
            i.initGame(vg);
        }
    }

    public void init() {
        File pluginDir = new File(VEngine.cfg.g("home") + "plugins");

        File[] jars = pluginDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".jar");
            }
        });
        Class[] pluginClasses = new Class[jars.length];

        for (int i = 0; i < jars.length; i++) {
            try {
                URL jarURL = jars[i].toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
                pluginClasses[i] = classLoader.loadClass("plugin.Plugin");
                p.add(new Plugin((PluginInterface)pluginClasses[i].newInstance()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException ex) {
                Logger.getLogger(PluginCore.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(PluginCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

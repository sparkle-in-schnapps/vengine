package vengine;

import java.io.File;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.swing.JOptionPane;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import vgame.VGame;
import vgame.VLandschaft;
import vgame.VProcessor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yew_mentzaki
 */
public class VEngine {

    /**
     * @param args the command line arguments
     */
    public static String game = "VEngine";
    public static CFG cfg;

    public static void main(String[] args) {
        System.setOut(new VOut(System.out));
        VEngineLogo vl = new VEngineLogo();

        vl.setVisible(true);
        cfg = VFileReader.readCFG(new File(new File(System.getProperty("user.home") + "/.vau/").getAbsolutePath() + "/configurations/game.cfg"));
        cfg.s("home", new File(System.getProperty("user.home") + "/.vau/").getAbsolutePath());
        cfg.s("modhome", new File(System.getProperty("user.home") + "/.vau/mods/" + cfg.g("mod") + "/").getAbsolutePath() + "/");

        for (String param : args) {
            try {
                String[] pr = param.split("=");
                cfg.s(pr[0], pr[1]);
            } catch (Exception e) {

            }
        }

        System.out.println(cfg);
        try {

            System.setProperty("java.library.path", new File(cfg.g("home") + "/native").getAbsolutePath());

            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);

            try {
                fieldSysPath.set(null, null);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(VEngine.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            } catch (IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
                System.exit(1);
            }
        } catch (NoSuchFieldException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(null, "Error!\n" + ex.toString());
            System.exit(1);
        }

        Music openingMenuMusic;
        try {
            openingMenuMusic = new Music(new File(System.getProperty("user.home") + "/.vau/music/watchword.ogg").getAbsolutePath());

            if (cfg.g("music").equals("enabled")) {
                openingMenuMusic.loop();
            }
        } catch (SlickException ex) {
            Logger.getLogger(VEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

        VGraphics.start();

        VProcessor.add(new vexamples.units.VPlayer());
        VProcessor.add(new vexamples.units.VUnit());
        VProcessor.add(new vexamples.units.VBullet());

        VMenu vm = new VMenu();
        vl.setVisible(false);

    }

}

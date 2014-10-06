/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author yew_mentzaki
 */
public class VResourceManager {

    private static String home = new File(System.getProperty("user.home") + "/.vau").getAbsolutePath();
    private static ArrayList<Texture> texture_object = new ArrayList<Texture>();
    private static ArrayList<String> texture_filename = new ArrayList<String>();

    private static InputStream getResource(String file) {
        try {
            return new FileInputStream(new File(VEngine.cfg.g("modhome") + "/resources/" + file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void bind(String texture, String format) {
        if (texture_filename.contains(texture)) {
            texture_object.get(texture_filename.indexOf(texture)).bind();
        } else {
            try {
                Texture loadedTexture = TextureLoader.getTexture(format, getResource("textures/" + texture));
                texture_object.add(loadedTexture);
                texture_filename.add(texture);
                loadedTexture.bind();
            } catch (IOException ex) {
                Logger.getLogger(VResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import com.sun.imageio.plugins.gif.GIFImageReader;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import sun.awt.image.GifImageDecoder;
import vgame.VConvert;

/**
 *
 * @author yew_mentzaki
 */
public class Sprite {

    private static ArrayList<Sprite> vm = new ArrayList<Sprite>();

    public static void render(final String name, VGraphics g, int i, VPoint loc) {

        for (Sprite vm : vm) {
            if (vm.name.equals(name)) {
                vm.render(g, i, loc);
                return;
                
            }
        }
        load(name);
               

    }
      public static void load(String name){
        Sprite v = new Sprite(name);
        vm.add(v);
    }
        
    private String name;
    private Texture textures[];

    private Texture toT(BufferedImage image) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "gif", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            return TextureLoader.getTexture("GIF", is);
        } catch (IOException ex) {
            Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void render(VGraphics g, int i, VPoint loc) {
        loc = VConvert.to2DPoint((int) loc.x, (int) loc.y, (int) loc.z);

        if (textures == null) {
            return;
        }

        textures[i].bind();
        g.fillPolygon(
                new VPoint(loc.x - textures[i].getImageWidth() / 2, loc.y - textures[i].getImageHeight() / 3 * 2, 0),
                new VPoint(loc.x - textures[i].getImageWidth() / 2, loc.y + textures[i].getImageHeight() / 3, 0),
                new VPoint(loc.x + textures[i].getImageWidth() / 2, loc.y + textures[i].getImageHeight() / 3, 0),
                new VPoint(loc.x + textures[i].getImageWidth() / 2, loc.y - textures[i].getImageHeight() / 3 * 2, 0)
        );

    }

    protected Sprite(String s) {
        name = s;
        try {
            ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
            ImageInputStream in = ImageIO.createImageInputStream(new File(VEngine.cfg.g("modhome") + "resources/sprites/" + s + ".gif"));
            reader.setInput(in);
            int count = reader.getNumImages(true);
            textures = new Texture[count];
            for (int i = 0; i < count; i++) {
                BufferedImage image = reader.read(i);
                textures[i] = toT(image);
            }
        } catch (IOException ex) {
            textures = null;
        }

    }
}

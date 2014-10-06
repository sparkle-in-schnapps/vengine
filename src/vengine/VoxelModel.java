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
public class VoxelModel {
    private static ArrayList<VoxelModel> vm = new ArrayList<VoxelModel>();
    public static void render(String name, VGraphics g, VPoint loc, VPoint t){
        for (VoxelModel vm : vm) {
            if(vm.name.equals(name)){
                vm.render(g, loc, t);
                return;
                
            }
        }
        ///*
        VoxelModel v = new VoxelModel(name);
        v.render(g, loc, t);
        vm.add(v);
        //*/
    }
     public static void render(VoxelModel v, VGraphics g, VPoint loc, VPoint t){
        
        ///*
        
        v.render(g, loc, t);
        
        //*/
    }
    public static VoxelModel load(String name){
        VoxelModel v = new VoxelModel(name);
        vm.add(v);
        return v;
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
            Logger.getLogger(VoxelModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void render(VGraphics g, VPoint loc, VPoint t) {
        if(textures==null)return;
        VPoint[] p = new VPoint[]{
            new VPoint(-textures[0].getImageWidth() / 4, textures[0].getImageWidth() / 4, 0),
            new VPoint(-textures[0].getImageWidth() / 4, -textures[0].getImageWidth() / 4, 0),
            new VPoint(textures[0].getImageWidth() / 4, -textures[0].getImageWidth() / 4, 0),
            new VPoint(textures[0].getImageWidth() / 4, textures[0].getImageWidth() / 4, 0),};
        for (int i = 0; i < textures.length; i++) {
            VPoint[] l = new VPoint[4];
            for (int j = 0; j < 4; j++) {
                l[j] = new VPoint(p[j].x * Math.cos(t.y) - (i) * Math.sin(t.y), p[j].y, p[j].x * Math.sin(t.y) + (i) * Math.cos(t.y));
                l[j] = new VPoint(l[j].x * Math.cos(t.z) - p[j].y * Math.sin(t.z), l[j].x * Math.sin(t.z) + l[j].y * Math.cos(t.z), l[j].z);
                l[j].x += loc.x;
                l[j].y += loc.y;
                l[j].z += loc.z;
                l[j] = VConvert.to2DPoint((int) l[j].x, (int) l[j].y, (int) l[j].z);
            }
            textures[i].bind();
            g.fillPolygon(l);
        }

    }

    protected VoxelModel(String s) {
        name = s;
        try {
            ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
            ImageInputStream in = ImageIO.createImageInputStream(new File(VEngine.cfg.g("modhome") + "resources/models/" + s + ".gif"));
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

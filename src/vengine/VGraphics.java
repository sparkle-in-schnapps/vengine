/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.ImageIOImageData;

/**
 *
 * @author yew_mentzaki
 */
public final class VGraphics {

    public static int WIDTH, HEIGHT;
    private Font f = new Font("New Times Roman", 1, 16);
    private TrueTypeFont ttf = new TrueTypeFont(f, false);

    public void setFont(Font f) {
        this.f = f;
        ttf = new TrueTypeFont(f, false);
    }

    public int getWidth() {
        return Display.getWidth();
    }

    public int getHeight() {
        return Display.getHeight();
    }

    public Font getFont() {
        return f;
    }
    private Color c = Color.BLACK;

    public void drawString(String s, int x, int y) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        ttf.drawString(x, y, s, new org.newdawn.slick.Color(((float) c.getRed() / 255.0f), ((float) c.getGreen() / 255.0f), ((float) c.getBlue() / 255.0f), ((float) c.getAlpha() / 255.0f)));
        if (texture != null) {
            String format[] = texture.split("\\.");
            VResourceManager.bind(texture, format[format.length - 1].toUpperCase());
        }
    }

    public void drawString(String s, int x, int y, int w) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        int ww = w / 2 - ttf.getWidth(s) / 2;
        ttf.drawString(x + ww, y, s, new org.newdawn.slick.Color(((float) c.getRed() / 255.0f), ((float) c.getGreen() / 255.0f), ((float) c.getBlue() / 255.0f), ((float) c.getAlpha() / 255.0f)));
        if (texture != null) {
            String format[] = texture.split("\\.");
            VResourceManager.bind(texture, format[format.length - 1].toUpperCase());
        }
    }

    public void setColor(Color c) {
        this.c = c;
        GL11.glColor4f(((float) c.getRed() / 255.0f), ((float) c.getGreen() / 255.0f), ((float) c.getBlue() / 255.0f), ((float) c.getAlpha() / 255.0f));
    }

    public Color getColor() {
        return c;
    }
    String texture;

    public void removeTexture() {
        setTexture("white.png");
    }

    public void setTexture(String texture) {
        this.texture = texture;
        String format[] = texture.split("\\.");
        VResourceManager.bind(texture, format[format.length - 1].toUpperCase());
    }

    public String getTexture() {
        return texture;
    }

    public void drawSprite(int x, int y, int w, int h) {
        VPoint[] points = new VPoint[]{
            new VPoint(x - w / 2, y - h / 3 * 2, 0),
            new VPoint(x - w / 2, y + h / 3, 0),
            new VPoint(x + w / 2, y + h / 3, 0),
            new VPoint(x + w / 2, y - h / 3 * 2, 0),};
        fillPolygon(points);
    }

    public void fillPolygon(VPoint... points) {
        GL11.glBegin(GL11.GL_POLYGON);

        float angle = -2.35619449f;
        float mangle = (float) ((2 * Math.PI) / points.length);
        float tx[] = new float[]{0, 0, 1, 1};
        float ty[] = new float[]{0, 1, 1, 0};
        int i = 0;
        for (VPoint vp : points) {
            if (points.length == 4) {
                GL11.glTexCoord2d(tx[i], ty[i]);
                i++;
            } else {
                GL11.glTexCoord2d(0.5 + Math.cos(angle) * 0.6, 0.5 + Math.sin(angle) * 0.6);
            }
            angle -= mangle;
            GL11.glVertex3d(vp.x, vp.y, vp.z);
        }
        GL11.glEnd();
    }

    private static final VGraphicManager vgm = new VGraphicManager();

    public static VGraphicManager getGraphicManager() {
        return vgm;
    }

    public static void setDisplayMode(int width, int height, boolean fullscreen) {

        // возвращаемся назад, если DisplayMode уже задан.
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i = 0; i < modes.length; i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the  
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    public static void start() {

        Thread renderingThread = new Thread("RenderingThread") {

            @Override
            public void run() {
                try {
                    Display.setDisplayMode(new DisplayMode(8000, 6000));
                    Display.setTitle(VEngine.cfg.g("mod"));
                   Display.setIcon(new ByteBuffer[] {
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(VEngine.cfg.g("modhome")+"/icon.png")), false, false, null),
                            
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(VEngine.cfg.g("modhome")+"/icon.png")), false, false, null)
                    });
                    Display.setFullscreen(true);
                    Display.setResizable(true);
                    Display.create();
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0, 1, 1, 0, 1, -1);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);

                    VGraphics g = new VGraphics();

                    Random r = new Random();

                    while (!Display.isCloseRequested()) {

                        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                        GL11.glEnable(GL11.GL_TEXTURE_2D); // это нужно для работания текстуры.
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glEnable(GL11.GL_BLEND);
                        WIDTH = Display.getWidth();
                        HEIGHT = Display.getHeight();
                        vgm.render(g);

                        Display.update();
                    }
                    Display.destroy();
                    System.exit(0);
                } catch (LWJGLException ex) {
                    Logger.getLogger(VGraphics.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VGraphics.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
        renderingThread.setPriority(Thread.MAX_PRIORITY);
        renderingThread.start();

    }

    public static void setInterfaceView() {

    }

    public void drawRect(int x, int y, int w, int h, double angle) {
        VPoint[] points = new VPoint[]{
            new VPoint(-w / 2, -h / 2, 0),
            new VPoint(-w / 2, +h / 2, 0),
            new VPoint(+w / 2, +h / 2, 0),
            new VPoint(+w / 2, -h / 2, 0),};
        GL11.glTranslated(x, y, 0);
        GL11.glRotated(angle, 0, 0, 1);
        fillPolygon(points);
        GL11.glRotated(angle, 0, 0, -1);
        GL11.glTranslated(-x, -y, 0);
    }

    public void drawRect(int x, int y, int w, int h) {
        VPoint[] points = new VPoint[]{
            new VPoint(x, y, 0),
            new VPoint(x, y + h, 0),
            new VPoint(x + w, y + h, 0),
            new VPoint(x + w, y, 0),};
        fillPolygon(points);
    }
    public float lineSize = 1;
    public void drawLine(double x, double y, double sx, double sy) {
        removeTexture();
         double angle = Math.atan2(y - sy, x - sx);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex2d(x - Math.cos(angle - 1.57) * lineSize, y - Math.sin(angle - 1.57) * lineSize);
        GL11.glVertex2d(sx - Math.cos(angle - 1.57) * lineSize, sy - Math.sin(angle - 1.57) * lineSize);
        GL11.glVertex2d(sx + Math.cos(angle - 1.57) * lineSize, sy + Math.sin(angle - 1.57) * lineSize);
        GL11.glVertex2d(x + Math.cos(angle - 1.57) * lineSize, y + Math.sin(angle - 1.57) * lineSize);
        GL11.glEnd();
    }

}

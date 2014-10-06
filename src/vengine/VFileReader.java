/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yew_mentzaki
 */
public class VFileReader {
    public static List readList(File list){
        try {
            Scanner s = new Scanner(list);
            List v = new List();
            while(s.hasNextLine()){
                String c = s.nextLine();
                v.add(c);
            }
            return v;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static CFG readCFG(File cfg){
        CFG r = new CFG();
        try {
            Scanner s = new Scanner(cfg);
            while(s.hasNextLine()){
                String c = s.nextLine();
                if(c.contains(":")){
                    String c2[] = c.split(":");
                    for (int i = 2; i < c2.length; i++) {
                        c2[1] = c2[1] + ":" + c2[i];
                    }
                    r.s(c2[0].replace(" ", ""), (c2[1].replace(" ", "")));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }
}

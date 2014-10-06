/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import java.util.ArrayList;
import vgame.VGame;

/**
 *
 * @author yew_mentzaki
 */
public class VScript {

    private class action {

        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> actions = new ArrayList<String>();
        int line;
    }
    VGame vg;
    String s[];

    public void sub(int line) {
        int l = line;
        while (true) {
            l++;

            try {
                
                String prm[] = s[l].split(" ");

                String cmd = prm[0];
                String prms[] = new String[prm.length - 1];
                for (int i = 0; i < prm.length - 1; i++) {
                    prms[i] = prm[i + 1];
                }
                if (cmd.equals("~~~")) {
                    return;
                }
                vg.command(cmd, prms);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void register(String script) {
        s = script.split("\n");
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replace("#*", "");
            String z[] = s[i].split(" ");
            if (z[i].equals("~~action")) {

            }
            System.out.println(s[i]);
        }
    }
}

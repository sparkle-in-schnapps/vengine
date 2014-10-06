/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import java.util.ArrayList;

/**
 *
 * @author yew_mentzaki
 */
public class CFG {

    public CFG() {
    }

    private ArrayList<String> params = new ArrayList<String>(),
            values = new ArrayList<String>();

    public void s(String p, String v) {
        if (params.contains(p)) {
            values.set(params.indexOf(p), v);
        } else {
            params.add(p);
            values.add(v);
        }
    }

    public String g(String p) {
        if (params.contains(p)) {
            return values.get(params.indexOf(p));
        } else {
            return "";
        }
    }

    public double gd(String p) {
        try {
            if (params.contains(p)) {
                return Double.parseDouble(values.get(params.indexOf(p)));
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int gi(String p) {
        try {
            if (params.contains(p)) {
                return Integer.parseInt(values.get(params.indexOf(p)));
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < params.size(); i++) {
            s = s + params.get(i) + ": " + values.get(i) + "\n";
        }
        return s;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine.gui;

import java.util.ArrayList;
import vengine.VGraphics;

/**
 *
 * @author yew_mentzaki
 */
public class VContainer extends VElement {

    private ArrayList<VElement> ve = new ArrayList<VElement>();

    public void add(VElement e) {
        ve.add(e);
    }

    @Override
    public void render(VGraphics g) {
        for (VElement e : ve) {
            e.render(g);
        }
    }

    @Override
    public boolean isClicked(int x, int y) {
        for (VElement e : ve) {
            if (e.isClicked(x, y)) {
                e.clicked();
                return true;
            }
        }
        return false;
    }

    public void remove(VElement e) {
        ve.remove(e);
    }
}

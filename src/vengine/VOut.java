/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vengine;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import javax.print.attribute.standard.DateTimeAtCompleted;

/**
 *
 * @author yew_mentzaki
 */
public class VOut extends PrintStream {

    PrintStream origin;

  
    
    public VOut(PrintStream out) {
        super(out);
        origin = out;
    }

    public void print(Object o) {
        origin.println("["+new Date().toGMTString()+"]: " + o);
    }

    public void println(Object o) {
        origin.println("["+new Date().toGMTString()+"]: " + o);
    }

   
}

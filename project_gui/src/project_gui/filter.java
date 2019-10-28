/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_gui;

import java.io.File;

/**
 *
 * @author arnav
 */
public class filter {

    filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public boolean accept(File f) 
    {
        if (f.isDirectory()) 
        {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            return extension.equals(Utils.crypt);
        }
        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }
    
}

class Utils {

    public final static String crypt = "crypt";

    /*
     * Get the extension of a file.
     */  
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}

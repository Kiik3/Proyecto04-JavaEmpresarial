
package com.propiedades;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrique Ochoa
 */
public class Propiedades {
    
    private static String CONFIGURACION = "configuracion.properties";
    
    public InputStream getResourcesInputAsStream(String configuracion){
        return Propiedades.class.getResourceAsStream(configuracion);
    }
    
    public Properties cargarPropiedades(){
        
        Properties propiedades = new Properties();
        try {
            propiedades.load(getResourcesInputAsStream(CONFIGURACION));
        } catch (IOException ex) {
            Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return propiedades;
    }
    
    public void insertarPropiedades(String key, String value, String ruta){
        Properties propiedades = new Properties();
        OutputStream out;
        
        try {
            out = new FileOutputStream(ruta);
            propiedades.setProperty("smtp", cargarPropiedades().getProperty("smtp"));
            propiedades.setProperty("port", cargarPropiedades().getProperty("port"));
            propiedades.setProperty("us", cargarPropiedades().getProperty("us"));
            propiedades.setProperty("pa", cargarPropiedades().getProperty("pa"));
            propiedades.setProperty("usBD", cargarPropiedades().getProperty("usBD"));
            propiedades.setProperty("url", cargarPropiedades().getProperty("url"));
            propiedades.setProperty("fechaPla", cargarPropiedades().getProperty("fechaPla"));
            propiedades.setProperty("pagar", cargarPropiedades().getProperty("pagar"));
            propiedades.setProperty(key, value);
            propiedades.store(out, "");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

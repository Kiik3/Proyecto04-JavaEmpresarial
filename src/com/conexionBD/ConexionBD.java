
package com.conexionBD;

import com.propiedades.Encriptador;
import com.propiedades.Propiedades;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Enrique Ochoa
 */
public class ConexionBD {
    
    //Método para abrir una nueva conexión a la BD
    public Connection iniciarConexionBD() throws ClassNotFoundException, SQLException{
        Propiedades propiedades = new Propiedades();
        Encriptador enc = new Encriptador();
        
        Class.forName("com.mysql.cj.jdbc.Driver"); 
        Connection con = DriverManager.getConnection(enc.desencriptador(propiedades.cargarPropiedades().getProperty("url")), 
                        enc.desencriptador(propiedades.cargarPropiedades().getProperty("usBD")), 
                        enc.desencriptador(propiedades.cargarPropiedades().getProperty("pa")));
        
        return con;
    }
    
    //Método para cerra la conexión a la BD
    public static void cerrarConexionDB(Connection con) throws SQLException{
        con.close();
    }
}

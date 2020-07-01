
package com.conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Enrique Ochoa
 */
public class ConexionBD {
    private static String url = "jdbc:mysql://34.68.85.103:3306/PROYECTO04_ADMINISTRACION_EMPLEADOS";
    private static String user = "java";
    private static String password = "cursoJava2020";
    
    public Connection iniciarConexionBD() throws ClassNotFoundException, SQLException{
        
        Class.forName("com.mysql.cj.jdbc.Driver"); 
        Connection con = DriverManager.getConnection(url, user, password);
        
        return con;
    }
    
    public static void cerrarConexionDB(Connection con) throws SQLException{
        con.close();
    }
}

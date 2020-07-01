
package com.login;

import com.dao.RolDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Enrique Ochoa
 */
public class Login {
    
    public String validacion(Connection con, String nombre, String contraseña) throws SQLException, Exception{
        
        String query = "select USU_NOMBRE, ROL_ID from ADM_USU_USUARIO where USU_NOMBRE = ? and USU_CONTRASENA = ?";
        PreparedStatement pre = con.prepareStatement(query);
        pre.setString(1, nombre);
        pre.setString(2, contraseña);
        ResultSet rs = pre.executeQuery();
        
        String nombreRol;
        if(rs.next()){
            RolDAO rolDAO = new RolDAO();
            rolDAO.setConexion(con);
            
            nombreRol = rolDAO.seleccionar(rs.getInt("ROL_ID")).get(0).getNombre();
            rs.close();
            pre.close();
            con.close();
            
            return nombreRol;
        }
        else{
            rs.close();
            pre.close();
            con.close();
            
            return null;
        }

    }
}

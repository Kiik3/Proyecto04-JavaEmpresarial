
package com.login;

import com.dao.RolDAO;
import com.propiedades.Encriptador;
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
        Encriptador enc = new Encriptador();
        
        String query = "select USU_NOMBRE, USU_CONTRASENA, ROL_ID from ADM_USU_USUARIO where USU_NOMBRE = ?";
        PreparedStatement pre = con.prepareStatement(query);
        pre.setString(1, nombre);
        ResultSet rs = pre.executeQuery();
        
        String nombreRol;
        if(rs.next()){
            if(contraseña.equals(enc.desencriptador(rs.getString("USU_CONTRASENA")))){
                RolDAO rolDAO = new RolDAO();
                rolDAO.setConexion(con);

                nombreRol = rolDAO.seleccionar(rs.getInt("ROL_ID")).get(0).getNombre();
                rs.close();
                pre.close();
                con.close();
            
                return nombreRol;
            }
            return null;
        }
        else{
            rs.close();
            pre.close();
            con.close();
            
            return null;
        }

    }
}

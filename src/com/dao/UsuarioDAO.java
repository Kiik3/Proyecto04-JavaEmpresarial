
package com.dao;

import com.entidades.Usuario;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrique Ochoa
 */
public class UsuarioDAO extends AbstractDAO<Usuario>{
    
    @Override
    public String nombreTabla(){
        return "ADM_USU_USUARIO";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"USU_ID","USU_NOMBRE","USU_CONTRASENA","USU_CORREO","ROL_ID"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, Usuario usuario) throws SQLException{

        pre.setString(1, usuario.getNombre());
        pre.setString(2, usuario.getContraseña());
        pre.setString(3, usuario.getCorreo());
        pre.setInt(4, usuario.getIdRol());

    }
    
    @Override
    public void mapeoActualizar(PreparedStatement pre, Usuario usuario) throws SQLException{

        pre.setString(1, usuario.getNombre());
        pre.setString(2, usuario.getContraseña());
        pre.setString(3, usuario.getCorreo());
        pre.setInt(4, usuario.getIdRol());
        pre.setInt(5, usuario.getId());

    }
    
    @Override
    public List<Usuario> mapeoSeleccionar(ResultSet rs) throws SQLException{

        List<Usuario> lista = new ArrayList();

        while(rs.next()){
            Usuario usuario = new Usuario();
            
            usuario.setId(rs.getInt("USU_ID"));
            usuario.setNombre(rs.getString("USU_NOMBRE"));
            usuario.setContraseña(rs.getString("USU_CONTRASENA"));
            usuario.setCorreo(rs.getString("USU_CORREO"));
            usuario.setIdRol(rs.getInt("ROL_ID"));
            
            lista.add(usuario);
        }

        return lista;

    }

    @Override
    public int impresion(List<Usuario> lista) throws SQLException {
        
        int i = 0;
        super.out.println("Lista de Usuarios:\n");
        super.out.println("Id\tNombre\tContraseña\tCorreo\tRol");
        for(Usuario l : lista){
            i++;
            super.out.print(l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getContraseña()+ "\t");
            super.out.print(l.getCorreo()+ "\t");
            
            RolDAO rolDAO = new RolDAO();
            rolDAO.setConexion(super.getConexion());
            try {
                if(l.getIdRol() == 0){
                    super.out.println("Ninguno");
                }
                else{
                    super.out.println(rolDAO.seleccionar(l.getIdRol()).get(0).getNombre());
                }
                
            } catch (Exception ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        
//        System.out.println("Lista de Usuarios:\n");
//        System.out.println("Id\tNombre\tContraseña\tCorreo\tRol");
//        while(rs.next()){
//            System.out.print(rs.getInt("USU_ID") + "\t");
//            System.out.print(rs.getString("USU_NOMBRE") + "\t");
//            System.out.print(rs.getString("USU_CONTRASENA") + "\t");
//            System.out.print(rs.getString("USU_CORREO") + "\t");
//            
//            RolDAO rolDAO = new RolDAO();
//            rolDAO.setConexion(super.getConexion());
//            try {
//                map = rolDAO.seleccionar(rs.getInt("ROL_ID"));
//                System.out.println(map.get("ROL_NOMBRE"));
//            } catch (Exception ex) {
//                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
            return i;
    }

//    @Override
//    protected void impresionId(Map map) throws SQLException {
//        System.out.print(map.get("ROL_ID")+"\t");
//        System.out.println(map.get("ROL_NOMBRE")+"\t");
//    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, Usuario usuario, Connection con, Socket cliente) throws Exception{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        
        RolDAO rolDAO = new RolDAO();
        boolean flag = false;
        int i;
        int leerInt;
        String leer;
        
        switch(opcion){
            case 1:
                setConexion(con);
                impresion(seleccionar());
                break;
            case 2:
                setConexion(con);
                super.out.print("Ingresa el id: ");
                leer = super.in.readLine();
                leerInt = Integer.parseInt(leer);
                impresion(seleccionar(leerInt));
                break;
            case 3:
                setConexion(con);
                super.out.print("Ingrese el nombre de usuario: ");
                usuario.setNombre(super.in.readLine());
                super.out.print("Ingrese la contrasena: ");
                usuario.setContraseña(super.in.readLine());
                super.out.print("Ingrese el correo electronico: ");
                usuario.setCorreo(super.in.readLine());
                
                rolDAO = new RolDAO();
                rolDAO.setCliente(cliente);
                rolDAO.setConexion(super.getConexion());
                rolDAO.impresion(rolDAO.seleccionar());
                
                super.out.print("Ingrese el id del rol: ");
                leer = super.in.readLine();
                leerInt = Integer.parseInt(leer);
                usuario.setIdRol(leerInt);
                
                setConexion(con);
                insertar(usuario);
                
                super.out.println("\nUsaurio agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.print("\nIngrese el id del usuario a actualizar: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    usuario.setId(leerInt);
                    super.out.print("Ingrese el nuevo nombre de usuario: ");
                    usuario.setNombre(super.in.readLine());
                    super.out.print("Ingrese la nueva contrasena: ");
                    usuario.setContraseña(super.in.readLine());
                    super.out.print("Ingrese el nuevo correo electronico: ");
                    usuario.setCorreo(super.in.readLine());

                    rolDAO = new RolDAO();
                    rolDAO.setCliente(cliente);
                    rolDAO.setConexion(super.getConexion());
                    rolDAO.impresion(rolDAO.seleccionar());

                    super.out.print("Ingrese el id del rol: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    usuario.setIdRol(leerInt);

                    setConexion(con);
                    actualizar(usuario);

                    super.out.println("\nUsaurio actualizado correctamente!");
                }
                else{
                    super.out.println("\nNo hay usuarios para actualizar!");
                    usuario.setId(0);
                    actualizar(usuario);
                }
                
                break;
            case 5:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.print("Ingresa el id: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    eliminar(leerInt);

                    super.out.println("\nUsaurio eliminado correctamente!");
                }
                else{
                    super.out.println("\nNo hay usuarios para eliminar!");
                    eliminar(0);
                }
                
                break;
            default:
                super.out.println("Ingresa una opcion valida");
                flag = true;
        }
        return flag;
    }

    @Override
    public boolean ingresoDatosGestion(int opcion, Usuario entidad, Connection con) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

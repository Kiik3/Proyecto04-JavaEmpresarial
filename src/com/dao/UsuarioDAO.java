
package com.dao;

import com.entidades.Usuario;
import com.propiedades.Encriptador;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrique Ochoa
 */
public class UsuarioDAO extends AbstractDAO<Usuario>{
    //Todos son métodos heredados, la descripción de lo que hacen se encuentra en la clase AbstractDAO
    
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
        
        //Se recorre el rs para mapear los registros obtenidos
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
        super.out.println("\rLista de Usuarios:\n");
        super.out.println("\rId\tNombre\tCorreo\tRol");
        for(Usuario l : lista){
            i++;
            super.out.print("\r" + l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getCorreo()+ "\t");
            
            //Se llama a rolDAO para mostrar en los resultados el nombre del rol al que pertenece el usuario
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
        //valida si el query está vacío
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        
        return i;
    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, Usuario usuario, Connection con, Socket cliente) throws Exception{
        Encriptador enc = new Encriptador();
        
        RolDAO rolDAO = new RolDAO();
        boolean flag = false;
        int i;
        int leerInt;
        String leer;
        
        switch(opcion){
            //Listar todos los registros
            case 1:
                setConexion(con);
                impresion(seleccionar());
                break;
            //Listar todos los registros por id
            case 2:
                setConexion(con);
                super.out.print("Ingresa el id: ");
                leer = super.in.readLine();
                leerInt = Integer.parseInt(leer);
                impresion(seleccionar(leerInt));
                break;
            //Ingresar un nuevo registro
            case 3:
                setConexion(con);
                super.out.print("Ingrese el nombre de usuario: ");
                usuario.setNombre(super.in.readLine());
                super.out.print("Ingrese la contrasena: ");
                usuario.setContraseña(enc.encriptador(super.in.readLine())); //Se ingresa encriptada la contraseña
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
            //Actualizar un registro
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
                    usuario.setContraseña(enc.encriptador(super.in.readLine())); //Se actualizada encriptada la contraseña
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
            //Eliminar un registro
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

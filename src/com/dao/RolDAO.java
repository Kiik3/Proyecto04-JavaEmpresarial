
package com.dao;

import com.entidades.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Enrique Ochoa
 */
public class RolDAO extends AbstractDAO<Rol>{
    
    @Override
    public String nombreTabla(){
        return "ADM_ROL_ROL";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"ROL_ID","ROL_NOMBRE"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, Rol rol) throws SQLException{

        pre.setString(1, rol.getNombre());

    }
    
    @Override
    public void mapeoActualizar(PreparedStatement pre, Rol rol) throws SQLException{

        pre.setString(1, rol.getNombre());
        pre.setInt(2, rol.getId());

    }
    
    @Override
    public List<Rol> mapeoSeleccionar(ResultSet rs) throws SQLException{
        
        List<Rol> lista = new ArrayList();

        while(rs.next()){ 
            Rol rol = new Rol();
            
            rol.setId(rs.getInt("ROL_ID"));
            rol.setNombre(rs.getString("ROL_NOMBRE"));
            
            lista.add(rol);
        }
        
        return lista;

    }
    
    @Override
    public int impresion(List<Rol> lista) throws SQLException{
        
        int i = 0;
        System.out.println("Lista de Roles:\n");
        System.out.println("Id\tRol");
        for(Rol l : lista){
            i++;
            System.out.print(l.getId()+ "\t");
            System.out.println(l.getNombre()+ "\t");
        }
        if(i == 0){
            System.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    @Override
    public void ingresoDatosGestion(int opcion, Rol rol, Connection con) throws Exception{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        int i;
        int id;
        
        switch(opcion){
            case 1:
                setConexion(con);
                impresion(seleccionar());
                break;
            case 2:
                setConexion(con);
                System.out.print("Ingresa el id: ");
                id = scanner.nextInt();
                impresion(seleccionar(id));
                break;
            case 3:
                setConexion(con);
                System.out.print("Ingrese el nombre del rol: ");
                rol.setNombre(scanner.next());

                insertar(rol);
                
                System.out.println("\nRol agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    System.out.println("Atención! Los cambios también se verán reflejados en los usuarios que hagan uso del rol que se actualizará");
                    System.out.print("\nIngrese el id del rol a actualizar: ");
                    rol.setId(scanner.nextInt());
                    System.out.print("Ingrese el nuevo nombre del rol: ");
                    rol.setNombre(scanner.next());

                    actualizar(rol);

                    System.out.println("\nRol actualizado correctamente!");
                }
                else{
                    rol.setId(0);
                    actualizar(rol);
                }
                
                break;
            case 5:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    System.out.print("Ingresa el id: ");
                    id = scanner.nextInt();

                    try {
                        eliminar(id);
                        System.out.println("\nRol eliminado correctamente!");
                    } catch (Exception e) {
                        System.out.println("No se pudo eliminar el rol debido a que está asignado a un usuario/s");
                    }
                }
                else{
                    id = 0;
                    eliminar(id);
                }
                
                break;
        }
    }
}

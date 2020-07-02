
package com.dao;

import com.entidades.Rol;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        super.out.println("Lista de Roles:\n");
        super.out.println("Id\tRol");
        for(Rol l : lista){
            i++;
            super.out.print(l.getId()+ "\t");
            super.out.println(l.getNombre()+ "\t");
        }
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, Rol rol, Connection con) throws Exception{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
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
                super.out.print("Ingrese el nombre del rol: ");
                rol.setNombre(super.in.readLine());

                insertar(rol);
                
                super.out.println("\nRol agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.println("Atencion! Los cambios tambien se veran reflejados en los usuarios que hagan uso del rol que se actualizara");
                    super.out.print("\nIngrese el id del rol a actualizar: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    rol.setId(leerInt);
                    super.out.print("Ingrese el nuevo nombre del rol: ");
                    rol.setNombre(super.in.readLine());

                    actualizar(rol);

                    super.out.println("\nRol actualizado correctamente!");
                }
                else{
                    super.out.println("\nNo hay roles para eliminar!");
                    rol.setId(0);
                    actualizar(rol);
                }
                
                break;
            case 5:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.print("Ingresa el id: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);

                    try {
                        eliminar(leerInt);
                        super.out.println("\nRol eliminado correctamente!");
                    } catch (Exception e) {
                        super.out.println("No se pudo eliminar el rol debido a que esta asignado a un usuario/s");
                    }
                }
                else{
                    super.out.println("\nNo hay roles para eliminar!");
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
    public boolean ingresoDatosGestion(int opcion, Rol rol, Connection con, Socket cliente) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

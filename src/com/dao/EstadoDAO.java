
package com.dao;

import com.entidades.Estado;
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
public class EstadoDAO extends AbstractDAO<Estado>{
    
    @Override
    public String nombreTabla(){
        return "ADM_EST_ESTADO";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"EST_ID","EST_NOMBRE","EST_DESCRIPCION"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, Estado estado) throws SQLException{

        pre.setString(1, estado.getNombre());
        pre.setString(2, estado.getDescripcion());

    }
    
    @Override
    public void mapeoActualizar(PreparedStatement pre, Estado estado) throws SQLException{

        pre.setString(1, estado.getNombre());
        pre.setString(2, estado.getDescripcion());
        pre.setInt(3, estado.getId());

    }
    
    @Override
    public List<Estado> mapeoSeleccionar(ResultSet rs) throws SQLException{
        
        List<Estado> lista = new ArrayList();

        while(rs.next()){ 
            Estado estado = new Estado();
            
            estado.setId(rs.getInt("EST_ID"));
            estado.setNombre(rs.getString("EST_NOMBRE"));
            estado.setDescripcion(rs.getString("EST_DESCRIPCION"));
            
            lista.add(estado);
        }
        
        return lista;

    }
    
    @Override
    public int impresion(List<Estado> lista) throws SQLException{
        
        int i = 0;
        System.out.println("Lista de Estados:\n");
        System.out.println("Id\tEstado\tDescripción");
        for(Estado l : lista){
            i++;
            System.out.print(l.getId()+ "\t");
            System.out.print(l.getNombre()+ "\t");
            System.out.println(l.getDescripcion()+ "\t");
        }
        if(i == 0){
            System.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    @Override
    public void ingresoDatosGestion(int opcion, Estado estado, Connection con) throws Exception{
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
                System.out.print("Ingrese el nombre del estado: ");
                estado.setNombre(scanner.next());
                System.out.print("Ingrese la descripción del estado: ");
                estado.setDescripcion(scanner.next());

                insertar(estado);
                
                System.out.println("\nEstado agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    System.out.println("Atención! Los cambios también se verán reflejados en los empleados que tengan asignado el estado");
                    System.out.print("\nIngrese el id del estado a actualizar: ");
                    estado.setId(scanner.nextInt());
                    System.out.print("Ingrese el nuevo nombre del estado: ");
                    estado.setNombre(scanner.next());
                    System.out.print("Ingrese la nueva descripción del estado: ");
                    estado.setDescripcion(scanner.next());

                    actualizar(estado);

                    System.out.println("\nEstado actualizado correctamente!");
                }
                else{
                    estado.setId(0);
                    actualizar(estado);
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
                        System.out.println("\nEstado eliminado correctamente!");
                    } catch (Exception e) {
                        System.out.println("No se pudo eliminar el estado debido a que está asignado a un empleado/s");
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


package com.dao;

import com.entidades.Departamento;
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
public class DepartamentoDAO extends AbstractDAO<Departamento>{
    
    @Override
    public String nombreTabla(){
        return "ADM_DEP_DEPARTAMENTO";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"DEP_ID","DEP_NOMBRE"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, Departamento departamento) throws SQLException{

        pre.setString(1, departamento.getNombre());

    }
    
    @Override
    public void mapeoActualizar(PreparedStatement pre, Departamento departamento) throws SQLException{

        pre.setString(1, departamento.getNombre());
        pre.setInt(2, departamento.getId());

    }
    
    @Override
    public List<Departamento> mapeoSeleccionar(ResultSet rs) throws SQLException{
        
        List<Departamento> lista = new ArrayList();

        while(rs.next()){ 
            Departamento departamento = new Departamento();
            
            departamento.setId(rs.getInt("DEP_ID"));
            departamento.setNombre(rs.getString("DEP_NOMBRE"));
            
            lista.add(departamento);
        }
        
        return lista;

    }
    
    @Override
    public int impresion(List<Departamento> lista) throws SQLException{
        
        int i = 0;
        System.out.println("Lista de Departamentos:\n");
        System.out.println("Id\tDepartamento");
        for(Departamento l : lista){
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
    public void ingresoDatosGestion(int opcion, Departamento departamento, Connection con) throws Exception{
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
                System.out.print("Ingrese el nombre del departamento: ");
                departamento.setNombre(scanner.next());

                insertar(departamento);
                
                System.out.println("\nDepartamento agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    System.out.println("Atención! Los cambios también se verán reflejados en los puestos pertenecientes al departamento");
                    System.out.print("\nIngrese el id del departamento a actualizar: ");
                    departamento.setId(scanner.nextInt());
                    System.out.print("Ingrese el nuevo nombre del departamento: ");
                    departamento.setNombre(scanner.next());
                    actualizar(departamento);
                    
                    System.out.println("\nDepartamento actualizado correctamente!");
                }
                else{
                    departamento.setId(0);
                    actualizar(departamento);
                }

                break;
            case 5:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    System.out.println("Atención! Los puestos pertenecientes al departamento también se eliminarán");
                    System.out.print("Ingresa el id: ");
                    id = scanner.nextInt();
                    eliminar(id);
                    
                    System.out.println("\nDepartamento eliminado correctamente!");
                }
                else{
                    id = 0;
                    eliminar(id);
                }
                
                break;
        }
    }
}

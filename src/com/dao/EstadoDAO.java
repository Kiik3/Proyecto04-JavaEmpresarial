
package com.dao;

import com.entidades.Estado;
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
        super.out.println("Lista de Estados:\n");
        super.out.println("Id\tEstado\tDescripcion");
        for(Estado l : lista){
            i++;
            super.out.print(l.getId()+ "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.println(l.getDescripcion()+ "\t");
        }
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, Estado estado, Connection con) throws Exception{
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
                super.out.print("Ingrese el nombre del estado: ");
                estado.setNombre(super.in.readLine());
                super.out.print("Ingrese la descripción del estado: ");
                estado.setDescripcion(super.in.readLine());

                insertar(estado);
                
                super.out.println("\nEstado agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.println("Atencion! Los cambios tambien se veran reflejados en los empleados que tengan asignado el estado");
                    super.out.print("\nIngrese el id del estado a actualizar: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    estado.setId(leerInt);
                    super.out.print("Ingrese el nuevo nombre del estado: ");
                    estado.setNombre(super.in.readLine());
                    super.out.print("Ingrese la nueva descripcion del estado: ");
                    estado.setDescripcion(super.in.readLine());

                    actualizar(estado);

                    super.out.println("\nEstado actualizado correctamente!");
                }
                else{
                    super.out.println("\nNo hay estados para actualizar!");
                    estado.setId(0);
                    actualizar(estado);
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
                        super.out.println("\nEstado eliminado correctamente!");
                    } catch (Exception e) {
                        super.out.println("No se pudo eliminar el estado debido a que esta asignado a un empleado/s");
                    }
                }
                else{
                    super.out.println("\nNo hay estados para eliminar!");
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
    public boolean ingresoDatosGestion(int opcion, Estado entidad, Connection con, Socket cliente) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

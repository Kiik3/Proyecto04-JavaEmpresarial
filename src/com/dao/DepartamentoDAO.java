
package com.dao;

import com.entidades.Departamento;
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
        super.out.println("Lista de Departamentos:\n");
        super.out.println("Id\tDepartamento");
        for(Departamento l : lista){
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
    public boolean ingresoDatosGestion(int opcion, Departamento departamento, Connection con) throws Exception{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        boolean flag = false;
        int i;
        int id;
        String leerId;
        
        switch(opcion){
            case 1:
                setConexion(con);
                impresion(seleccionar());
                break;
            case 2:
                setConexion(con);
                super.out.print("Ingresa el id: ");
                leerId = super.in.readLine();
                id = Integer.parseInt(leerId);
                impresion(seleccionar(id));
                break;
            case 3:
                setConexion(con);
                super.out.print("Ingrese el nombre del departamento: ");
                departamento.setNombre(super.in.readLine());

                insertar(departamento);
                
                super.out.println("\nDepartamento agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.println("Atencion! Los cambios tambien se veran reflejados en los puestos pertenecientes al departamento");
                    super.out.print("\nIngrese el id del departamento a actualizar: ");
                    leerId = super.in.readLine();
                    id = Integer.parseInt(leerId);
                    departamento.setId(id);
                    
                    super.out.print("Ingrese el nuevo nombre del departamento: ");
                    departamento.setNombre(super.in.readLine());
                    actualizar(departamento);
                    
                    super.out.println("\nDepartamento actualizado correctamente!");
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
                    super.out.println("Atencion! Los puestos pertenecientes al departamento tambien se eliminaran");
                    super.out.print("Ingresa el id: ");
                    leerId = super.in.readLine();
                    id = Integer.parseInt(leerId);
                    eliminar(id);
                    
                    super.out.println("\nDepartamento eliminado correctamente!");
                }
                else{
                    id = 0;
                    eliminar(id);
                } 
                break;
            default:
                super.out.println("Ingresa una opcion valida");
                flag = true;
        }
        return flag;
    }

    @Override
    public boolean ingresoDatosGestion(int opcion, Departamento entidad, Connection con, Socket cliente) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

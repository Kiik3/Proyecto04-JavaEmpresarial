
package com.dao;

import com.entidades.Puesto;
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
public class PuestoDAO extends AbstractDAO<Puesto>{
    
    @Override
    public String nombreTabla(){
        return "ADM_PUE_PUESTO_TRABAJO";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"PUE_ID","PUE_NOMBRE","PUE_SALARIO_MINIMO","PUE_SALARIO_MAXIMO","DEP_ID"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, Puesto puesto) throws SQLException{

        pre.setString(1, puesto.getNombre());
        pre.setDouble(2, puesto.getSalarioMinimo());
        pre.setDouble(3, puesto.getSalarioMaximo());
        pre.setInt(4, puesto.getIdDepartamento());

    }
    
    @Override
    public void mapeoActualizar(PreparedStatement pre, Puesto puesto) throws SQLException{

        pre.setString(1, puesto.getNombre());
        pre.setDouble(2, puesto.getSalarioMinimo());
        pre.setDouble(3, puesto.getSalarioMaximo());
        pre.setInt(4, puesto.getIdDepartamento());
        pre.setInt(5, puesto.getId());

    }
    
    @Override
    public List<Puesto> mapeoSeleccionar(ResultSet rs) throws SQLException{

        List<Puesto> lista = new ArrayList();

        while(rs.next()){
            Puesto puesto = new Puesto();
            
            puesto.setId(rs.getInt("PUE_ID"));
            puesto.setNombre(rs.getString("PUE_NOMBRE"));
            puesto.setSalarioMinimo(rs.getDouble("PUE_SALARIO_MINIMO"));
            puesto.setSalarioMaximo(rs.getDouble("PUE_SALARIO_MAXIMO"));
            puesto.setIdDepartamento(rs.getInt("DEP_ID"));
            
            lista.add(puesto);
        }

        return lista;

    }

    @Override
    public int impresion(List<Puesto> lista) throws SQLException {
        
        int i = 0;
        System.out.println("Lista de Puestos:\n");
        System.out.println("Id\tPuesto\tSal mínimo\tSal Máximo\tDepartamento");
        for(Puesto l : lista){
            i++;
            System.out.print(l.getId() + "\t");
            System.out.print(l.getNombre()+ "\t");
            System.out.print(l.getSalarioMinimo()+ "\t");
            System.out.print(l.getSalarioMaximo()+ "\t");
            
            DepartamentoDAO departamentoDAO = new DepartamentoDAO();
            departamentoDAO.setConexion(super.getConexion());
            try {
                if(l.getIdDepartamento() == 0){
                    System.out.println("Ninguno");
                }
                else{
                    System.out.println(departamentoDAO.seleccionar(l.getIdDepartamento()).get(0).getNombre());
                }
                
            } catch (Exception ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(i == 0){
            System.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    @Override
    public void ingresoDatosGestion(int opcion, Puesto puesto, Connection con) throws Exception{
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
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
                System.out.print("Ingrese el nombre del puesto: ");
                puesto.setNombre(scanner.next());
                System.out.print("Ingrese el salario mínimo: ");
                puesto.setSalarioMinimo(scanner.nextDouble());
                System.out.print("Ingrese el salario máximo: ");
                puesto.setSalarioMaximo(scanner.nextDouble());
                
                departamentoDAO = new DepartamentoDAO();
                departamentoDAO.setConexion(super.getConexion());
                departamentoDAO.impresion(departamentoDAO.seleccionar());
                
                System.out.print("Ingrese el id del departamento: ");
                puesto.setIdDepartamento(scanner.nextInt());
                
                setConexion(con);
                insertar(puesto);
                
                System.out.println("\nPuesto agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    System.out.print("\nIngrese el id del puesto a actualizar: ");
                    puesto.setId(scanner.nextInt());
                    System.out.print("Ingrese el nuevo nombre del puesto: ");
                    puesto.setNombre(scanner.next());
                    System.out.print("Ingrese el nuevo salario mínimo: ");
                    puesto.setSalarioMinimo(scanner.nextDouble());
                    System.out.print("Ingrese el nuevo salario máximo: ");
                    puesto.setSalarioMaximo(scanner.nextDouble());

                    departamentoDAO = new DepartamentoDAO();
                    departamentoDAO.setConexion(super.getConexion());
                    departamentoDAO.impresion(departamentoDAO.seleccionar());

                    System.out.print("Ingrese el id del departamento: ");
                    puesto.setIdDepartamento(scanner.nextInt());

                    setConexion(con);
                    actualizar(puesto);

                    System.out.println("\nPuesto actualizado correctamente!");
                }
                else{
                    puesto.setId(0);
                    actualizar(puesto);
                }
                
                break;
            case 5:   
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    System.out.print("Ingresa el id: ");
                    id = scanner.nextInt();
                    eliminar(id);

                    System.out.println("\nPuesto eliminado correctamente!");
                }
                else{
                    id = 0;
                    eliminar(id);
                }
                
                break;
        }
    }
}

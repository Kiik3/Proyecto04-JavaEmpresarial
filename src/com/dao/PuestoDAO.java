
package com.dao;

import com.entidades.Puesto;
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
public class PuestoDAO extends AbstractDAO<Puesto>{
    //Todos son métodos heredados, la descripción de lo que hacen se encuentra en la clase AbstractDAO
    
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
        
//Se recorre el rs para mapear los registros obtenidos
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
        super.out.println("\rLista de Puestos:\n");
        super.out.println("\rId\tPuesto\tSal Minimo\tSal Maximo\tDepartamento");
        for(Puesto l : lista){
            i++;
            super.out.print("\r" +l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getSalarioMinimo()+ "\t");
            super.out.print(l.getSalarioMaximo()+ "\t");
            
            //Se llama a departamentoDAO para mostrar en los resultados el nombre del departamento al que pertenece el puesto
            DepartamentoDAO departamentoDAO = new DepartamentoDAO();
            departamentoDAO.setConexion(super.getConexion());
            try {
                if(l.getIdDepartamento() == 0){
                    super.out.println("Ninguno");
                }
                else{
                    super.out.println(departamentoDAO.seleccionar(l.getIdDepartamento()).get(0).getNombre());
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
    public boolean ingresoDatosGestion(int opcion, Puesto puesto, Connection con, Socket cliente) throws Exception{
        
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        boolean flag = false;
        int i;
        int leerInt;
        double leerMinimo;
        double leerMaximo;
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
                super.out.print("Ingrese el nombre del puesto: ");
                puesto.setNombre(super.in.readLine());
                //Se valida que sea positivo el salario
                do {                    
                    super.out.print("Ingrese el salario minimo: ");
                    leer = super.in.readLine();
                    leerMinimo = Double.parseDouble(leer);
                } while (leerMinimo <= 0);
                
                puesto.setSalarioMinimo(leerMinimo);
                //Se valida que sea positivo el salario máximo y que no mayor al salrio mínimo
                do {                    
                    super.out.print("Ingrese el salario maximo: ");
                    leer = super.in.readLine();
                    leerMaximo = Double.parseDouble(leer);
                    
                } while (leerMinimo >= leerMaximo || leerMaximo <= 0);
 
                puesto.setSalarioMaximo(leerMaximo);
                
                departamentoDAO = new DepartamentoDAO();
                departamentoDAO.setCliente(cliente);
                departamentoDAO.setConexion(super.getConexion());
                departamentoDAO.impresion(departamentoDAO.seleccionar());
                
                super.out.print("Ingrese el id del departamento: ");
                leer = super.in.readLine();
                leerInt = Integer.parseInt(leer);
                puesto.setIdDepartamento(leerInt);
                
                setConexion(con);
                insertar(puesto);
                
                super.out.println("\nPuesto agregado correctamente!");
                break;
            //Actualizar un registro
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    //Actualización en cascada
                    super.out.println("Atencion! Los cambios tambien se veran reflejados en los empleados que ocupen el puesto que se actualizara");
                    super.out.print("\nIngrese el id del puesto a actualizar: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    puesto.setId(leerInt);
                    super.out.print("Ingrese el nuevo nombre del puesto: ");
                    puesto.setNombre(super.in.readLine());
                    //Se valida que sea positivo el salario
                    do {                    
                        super.out.print("Ingrese el salario minimo: ");
                        leer = super.in.readLine();
                        leerMinimo = Double.parseDouble(leer);
                    } while (leerMinimo <= 0);
                    
                    puesto.setSalarioMinimo(leerMinimo);
                    //Se valida que sea positivo el salario máximo y que no mayor al salrio mínimo
                    do {                    
                        super.out.print("Ingrese el nuevo salario maximo: ");
                        leer = super.in.readLine();
                        leerMaximo = Double.parseDouble(leer);
                    
                    } while (leerMinimo >= leerMaximo || leerMaximo <= 0);

                    puesto.setSalarioMaximo(leerMaximo);

                    departamentoDAO = new DepartamentoDAO();
                    departamentoDAO.setCliente(cliente);
                    departamentoDAO.setConexion(super.getConexion());
                    departamentoDAO.impresion(departamentoDAO.seleccionar());

                    super.out.print("Ingrese el id del departamento: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    puesto.setIdDepartamento(leerInt);

                    setConexion(con);
                    actualizar(puesto);

                    super.out.println("\nPuesto actualizado correctamente!");
                }
                else{
                    super.out.println("\nNo hay puestos para actualizar!");
                    puesto.setId(0);
                    actualizar(puesto);
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
                    
                    //No se puede eliminar si un empleado está asignado al puesto
                    try {
                        eliminar(leerInt);
                        super.out.println("\nPuesto eliminado correctamente!");
                    } catch (Exception e) {
                        super.out.println("No se pudo eliminar el puesto debido a que esta asignado a un empleado/s");
                    }
                }
                else{
                    super.out.println("\nNo hay puestos para eliminar!");
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
    public boolean ingresoDatosGestion(int opcion, Puesto entidad, Connection con) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

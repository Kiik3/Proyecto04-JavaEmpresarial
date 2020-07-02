
package com.dao;

import com.entidades.Empleado;
import com.entidades.Puesto;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Enrique Ochoa
 */
public class EmpleadoDAO extends AbstractDAO<Empleado>{
    
    @Override
    public String nombreTabla(){
        return "ADM_EMP_EMPLEADO";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"EMP_ID","EMP_NOMBRE","EMP_APELLIDO","EMP_IDENTIFICACION","EMP_FECHA_NACIMIENTO",
                            "EMP_FECHA_CONTRATACION", "EMP_TELEFONO", "EMP_CORREO", "EMP_SALARIO", "PUE_ID", "DEP_ID", "EST_ID"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, Empleado empleado) throws SQLException{

        pre.setString(1, empleado.getNombre());
        pre.setString(2, empleado.getApellido());
        pre.setString(3, empleado.getIdentificacion());
        pre.setDate(4, empleado.getFechaNacimiento());
        pre.setDate(5, empleado.getFechaContrato());
        pre.setInt(6, empleado.getTelefono());
        pre.setString(7, empleado.getCorreo());
        pre.setDouble(8, empleado.getSalario());
        pre.setInt(9, empleado.getIdPuesto());
        pre.setInt(10, empleado.getIdDepartamento());
        pre.setInt(11, empleado.getIdEstado());

    }
    
    public void ingresarEmpleado(Empleado empleado, Connection con, Socket cliente) throws IOException, Exception{
 
        String leer;
        double salario;
        DepartamentoDAO departamentoDAO;
        PuestoDAO puestoDAO;

        setConexion(con);
        super.out.print("Ingrese el nombre del empleado: ");
        empleado.setNombre(super.in.readLine());
        super.out.print("Ingrese el apellido: ");
        empleado.setApellido(super.in.readLine());
        super.out.print("Ingrese la identificacion: ");
        empleado.setIdentificacion(super.in.readLine());
        
        super.out.println("Ingrese la fecha de nacimiento: "); 
        empleado.setFechaNacimiento(ingresarFecha());
        
        super.out.println("Ingrese la fecha de contratacion: "); 
        empleado.setFechaContrato(ingresarFecha());
        
        super.out.println("Ingrese el telefono: ");
        leer = super.in.readLine();
        empleado.setTelefono(Integer.parseInt(leer));
        
        super.out.print("Ingrese el correo: ");
        empleado.setCorreo(super.in.readLine());
        

        departamentoDAO = new DepartamentoDAO();
        departamentoDAO.setCliente(cliente);
        departamentoDAO.setConexion(super.getConexion());
        departamentoDAO.impresion(departamentoDAO.seleccionar());

        super.out.print("Ingrese el id del departamento: ");
        leer = super.in.readLine();
        empleado.setIdDepartamento(Integer.parseInt(leer));
        
        super.out.println("Puestos del departamento seleccionado: ");
        puestoDAO = new PuestoDAO();
        puestoDAO.setCliente(cliente);
        puestoDAO.setConexion(super.getConexion());
        List<Puesto> lista = new ArrayList();
        lista = puestosPorDepartamento(empleado.getIdDepartamento(), super.getConexion(), puestoDAO);
//        lista = puestoDAO.seleccionar(empleado.getIdDepartamento());
        puestoDAO.impresion(lista);
        
        super.out.print("Ingrese el id del puesto: ");
        leer = super.in.readLine();
        empleado.setIdPuesto(Integer.parseInt(leer));
        
        for(Puesto l :  lista){
            if(empleado.getIdPuesto() == l.getId()){
                do {                    
                    super.out.print("Ingrese el salario (debe estar entre el rango permitido por el puesto): ");
                    leer = super.in.readLine();
                    salario = Double.parseDouble(leer);
                    empleado.setSalario(salario);
                } while (salario < l.getSalarioMinimo() || salario > l.getSalarioMaximo());
            }
            
        }
        
        empleado.setIdEstado(1);

        setConexion(con);
        insertar(empleado);

        super.out.println("\nEmpleado agregado correctamente!");
    }
    
    public Date ingresarFecha() throws IOException{
        String leer;
        GregorianCalendar gc;
        java.util.Date dateUtil;
        java.sql.Date dateSQL;
        int dia, mes, año;
        
        super.out.print("Dia: ");
        leer = super.in.readLine();
        dia = Integer.parseInt(leer);
        super.out.print("Mes: ");
        leer = super.in.readLine();
        mes = Integer.parseInt(leer);
        super.out.print("Anio (en numeros): ");
        leer = super.in.readLine();
        año = Integer.parseInt(leer);
        
        gc = new GregorianCalendar(año, mes-1, dia);
        dateUtil = gc.getTime();
        dateSQL = new java.sql.Date(dateUtil.getTime());
 
        return dateSQL;
    }
    
    public List<Puesto> puestosPorDepartamento(int id, Connection con, PuestoDAO puestoDAO) throws SQLException{
        List<Puesto> lista = new ArrayList();
        
        String query = "select * from ADM_PUE_PUESTO_TRABAJO where DEP_ID = ?";
        PreparedStatement pre = con.prepareStatement(query);
        pre.setInt(1, id);
        ResultSet rs = pre.executeQuery();
        
        lista = puestoDAO.mapeoSeleccionar(rs);
        
        rs.close();
        pre.close();
        
        return lista;
    }
//    @Override
//    public void mapeoActualizar(PreparedStatement pre, Usuario usuario) throws SQLException{
//
//        pre.setString(1, usuario.getNombre());
//        pre.setString(2, usuario.getContraseña());
//        pre.setString(3, usuario.getCorreo());
//        pre.setInt(4, usuario.getIdRol());
//        pre.setInt(5, usuario.getId());
//
//    }
//    
    @Override
    public List<Empleado> mapeoSeleccionar(ResultSet rs) throws SQLException{

        List<Empleado> lista = new ArrayList();

        while(rs.next()){
            Empleado empleado = new Empleado();
            
            empleado.setId(rs.getInt("EMP_ID"));
            empleado.setNombre(rs.getString("EMP_NOMBRE"));
            empleado.setApellido(rs.getString("EMP_APELLIDO"));
            empleado.setIdentificacion(rs.getString("EMP_IDENTIFICACION"));
            empleado.setFechaNacimiento(rs.getDate("EMP_FECHA_NACIMIENTO"));
            empleado.setFechaContrato(rs.getDate("EMP_FECHA_CONTRATACION"));
            empleado.setTelefono(rs.getInt("EMP_TELEFONO"));
            empleado.setCorreo(rs.getString("EMP_CORREO"));
            empleado.setSalario(rs.getDouble("EMP_SALARIO"));
 
            empleado.setIdDepartamento(rs.getInt("DEP_ID"));
            empleado.setIdPuesto(rs.getInt("PUE_ID"));
            empleado.setIdEstado(rs.getInt("EST_ID"));
            
            lista.add(empleado);
        }

        return lista;

    }

    public int impresionDatosPersonales(List<Empleado> lista) throws SQLException {
        
        int i = 0;
        super.out.println("Lista de Empleados:\n");
        super.out.println("Id\tNombre\tApellido\tIdentificacion\tFecha nac.\tFecha contratacion\tTelefono\tCorreo");
        for(Empleado l : lista){
            i++;
            super.out.print(l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getApellido()+ "\t");
            super.out.print(l.getIdentificacion()+ "\t");
            super.out.print(l.getFechaNacimiento()+ "\t");
            super.out.print(l.getFechaContrato()+ "\t");
            super.out.print(l.getTelefono()+ "\t");
            super.out.println(l.getCorreo()+ "\t");
            
//            RolDAO rolDAO = new RolDAO();
//            rolDAO.setConexion(super.getConexion());
//            try {
//                if(l.getIdRol() == 0){
//                    super.out.println("Ninguno");
//                }
//                else{
//                    super.out.println(rolDAO.seleccionar(l.getIdRol()).get(0).getNombre());
//                }
//                
//            } catch (Exception ex) {
//                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    public void actualizarDatosPersonales(Empleado empleado, Connection con, Socket cliente) throws Exception{
        int i;
        String leer;
        setConexion(con);
        i = impresionDatosPersonales(seleccionar());
        
        if(i != 0){
            super.out.print("Ingrese el id del empleado a actualizar: ");
            leer = super.in.readLine();
            empleado.setId(Integer.parseInt(leer));
            super.out.print("Ingrese el nuevo nombre del empleado: ");
            empleado.setNombre(super.in.readLine());
            super.out.print("Ingrese el nuevo apellido: ");
            empleado.setApellido(super.in.readLine());
            super.out.print("Ingrese la nueva identificacion: ");
            empleado.setIdentificacion(super.in.readLine());

            super.out.println("Ingrese la fecha de nacimiento: "); 
            empleado.setFechaNacimiento(ingresarFecha());

            super.out.println("Ingrese la fecha de contratacion: "); 
            empleado.setFechaContrato(ingresarFecha());

            super.out.print("Ingrese el telefono: ");
            leer = super.in.readLine();
            empleado.setTelefono(Integer.parseInt(leer));

            super.out.print("Ingrese el correo: ");
            empleado.setCorreo(super.in.readLine());
            
            setConexion(con);
            String query = "update ADM_EMP_EMPLEADO set EMP_NOMBRE = ?, EMP_APELLIDO = ?, EMP_IDENTIFICACION = ?,"
                    + " EMP_FECHA_NACIMIENTO = ?, EMP_FECHA_CONTRATACION = ?, EMP_TELEFONO = ?, EMP_CORREO = ? where EMP_ID = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setString(1, empleado.getNombre());
            pre.setString(2, empleado.getApellido());
            pre.setString(3, empleado.getIdentificacion());
            pre.setDate(4, empleado.getFechaNacimiento());
            pre.setDate(5, empleado.getFechaContrato());
            pre.setInt(6, empleado.getTelefono());
            pre.setString(7, empleado.getCorreo());
            pre.setInt(8, empleado.getId());
            
            pre.executeUpdate();

//            lista = puestoDAO.mapeoSeleccionar(rs);

            pre.close();
            
            super.out.println("\nEmpleado actualizado correctamente!");
        }
        else{
            super.out.println("\nNo hay empleados para actualizar!");
            empleado.setId(0);
            actualizar(empleado);
        }
    }
    
    public int impresionEstadoEmpleado(List<Empleado> lista) throws SQLException {
        
        int i = 0;
        super.out.println("Lista de Empleados:\n");
        super.out.println("Id\tNombre\tApellido\tEstado");
        for(Empleado l : lista){
            i++;
            super.out.print(l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getApellido()+ "\t");
            
            EstadoDAO estadoDAO = new EstadoDAO();
            estadoDAO.setConexion(super.getConexion());
            try {
                if(l.getIdEstado()== 0){
                    super.out.println("Ninguno");
                }
                else{
                    super.out.println(estadoDAO.seleccionar(l.getIdEstado()).get(0).getNombre());
                }
                
            } catch (Exception ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    public void actualizarEstadoEmpleado(Empleado empleado, Connection con, Socket cliente) throws Exception{
        int i;
        String leer;
        setConexion(con);
        EstadoDAO estadoDAO;
        i = impresionEstadoEmpleado(seleccionar());
        
        if(i != 0){
            super.out.print("Ingrese el id del empleado a actualizar: ");
            leer = super.in.readLine();
            empleado.setId(Integer.parseInt(leer));
            
            estadoDAO = new EstadoDAO();
            estadoDAO.setCliente(cliente);
            estadoDAO.setConexion(super.getConexion());
            estadoDAO.impresion(estadoDAO.seleccionar());

            super.out.print("Ingrese el id del estado: ");
            leer = super.in.readLine();
            empleado.setIdEstado(Integer.parseInt(leer));
            
            setConexion(con);
            String query = "update ADM_EMP_EMPLEADO set EST_ID = ? where EMP_ID = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setInt(1, empleado.getIdEstado());
            pre.setInt(2, empleado.getId());
            
            pre.executeUpdate();

            pre.close();
            
            super.out.println("\nEstado de empleado actualizado correctamente!");
        }
        else{
            super.out.println("\nNo hay empleados para actualizar!");
            empleado.setId(0);
            actualizar(empleado);
        }
    }
    
    
//    
//    @Override
//    public boolean ingresoDatosGestion(int opcion, Usuario usuario, Connection con, Socket cliente) throws Exception{
//        Scanner scanner = new Scanner(System.in);
//        scanner.useDelimiter("\n");
//        
//        RolDAO rolDAO = new RolDAO();
//        boolean flag = false;
//        int i;
//        int leerInt;
//        String leer;
//        
//        switch(opcion){
//            case 1:
//                setConexion(con);
//                impresion(seleccionar());
//                break;
//            case 2:
//                setConexion(con);
//                super.out.print("Ingresa el id: ");
//                leer = super.in.readLine();
//                leerInt = Integer.parseInt(leer);
//                impresion(seleccionar(leerInt));
//                break;
//            case 3:
//                setConexion(con);
//                super.out.print("Ingrese el nombre de usuario: ");
//                usuario.setNombre(super.in.readLine());
//                super.out.print("Ingrese la contrasena: ");
//                usuario.setContraseña(super.in.readLine());
//                super.out.print("Ingrese el correo electronico: ");
//                usuario.setCorreo(super.in.readLine());
//                
//                rolDAO = new RolDAO();
//                rolDAO.setCliente(cliente);
//                rolDAO.setConexion(super.getConexion());
//                rolDAO.impresion(rolDAO.seleccionar());
//                
//                super.out.print("Ingrese el id del rol: ");
//                leer = super.in.readLine();
//                leerInt = Integer.parseInt(leer);
//                usuario.setIdRol(leerInt);
//                
//                setConexion(con);
//                insertar(usuario);
//                
//                super.out.println("\nUsaurio agregado correctamente!");
//                break;
//            case 4:
//                setConexion(con);
//                i = impresion(seleccionar());
//                
//                if(i != 0){
//                    super.out.print("\nIngrese el id del usuario a actualizar: ");
//                    leer = super.in.readLine();
//                    leerInt = Integer.parseInt(leer);
//                    usuario.setId(leerInt);
//                    super.out.print("Ingrese el nuevo nombre de usuario: ");
//                    usuario.setNombre(super.in.readLine());
//                    super.out.print("Ingrese la nueva contrasena: ");
//                    usuario.setContraseña(super.in.readLine());
//                    super.out.print("Ingrese el nuevo correo electronico: ");
//                    usuario.setCorreo(super.in.readLine());
//
//                    rolDAO = new RolDAO();
//                    rolDAO.setCliente(cliente);
//                    rolDAO.setConexion(super.getConexion());
//                    rolDAO.impresion(rolDAO.seleccionar());
//
//                    super.out.print("Ingrese el id del rol: ");
//                    leer = super.in.readLine();
//                    leerInt = Integer.parseInt(leer);
//                    usuario.setIdRol(leerInt);
//
//                    setConexion(con);
//                    actualizar(usuario);
//
//                    super.out.println("\nUsaurio actualizado correctamente!");
//                }
//                else{
//                    usuario.setId(0);
//                    actualizar(usuario);
//                }
//                
//                break;
//            case 5:
//                setConexion(con);
//                i = impresion(seleccionar());
//                
//                if(i != 0){
//                    super.out.print("Ingresa el id: ");
//                    leer = super.in.readLine();
//                    leerInt = Integer.parseInt(leer);
//                    eliminar(leerInt);
//
//                    super.out.println("\nUsaurio eliminado correctamente!");
//                }
//                else{
//                    eliminar(0);
//                }
//                
//                break;
//            default:
//                super.out.println("Ingresa una opcion valida");
//                flag = true;
//        }
//        return flag;
//    }

    @Override
    public boolean ingresoDatosGestion(int opcion, Empleado entidad, Connection con) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void mapeoActualizar(PreparedStatement pre, Empleado entidad) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected int impresion(List<Empleado> lista) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean ingresoDatosGestion(int opcion, Empleado entidad, Connection con, Socket cliente) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

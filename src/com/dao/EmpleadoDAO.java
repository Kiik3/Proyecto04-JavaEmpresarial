
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
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
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
                           "EMP_FECHA_CONTRATACION", "EMP_TELEFONO", "EMP_CORREO", "EMP_SALARIO", "PUE_ID", "DEP_ID", "EST_ID", "EMP_ID_JEFE"};
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
        
        if(empleado.getIdJefe() == 0){
            pre.setNull(12, java.sql.Types.INTEGER);
        }
        else{
            pre.setInt(12, empleado.getIdJefe());
        } 

    }
    
    public void ingresarEmpleado(Empleado empleado, Connection con, Socket cliente) throws IOException, Exception{
 
        String leer;
        boolean flag = false;
        double salario;
        int idPuesto;
        int idJefe;
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
        
        do {
            super.out.print("Ingrese el id del puesto del listado anterior: ");
            leer = super.in.readLine();
            idPuesto = Integer.parseInt(leer);

            for(Puesto l :  lista){ 
                if(idPuesto == l.getId()){
                    empleado.setIdPuesto(idPuesto);
                    flag = false;
                    break;
                } 
                else{
                    flag = true;
                }
            }
        } while (flag);

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
        
        super.out.println("Escoja un jefe de la siguiente lista:");
        impresionDatosPersonales(listaJefes(super.getConexion()));
        List<Empleado> listaEmp = new ArrayList();
        //listaEmp = seleccionar();                     //Descomentar para mostrar todos los empleados
        listaEmp = listaJefes(super.getConexion());
        
        do {
            super.out.print("Ingrese el id del empleado que sera jefe del empleado que se esta ingresando o 'n' si sera jefe: ");
            leer = super.in.readLine();
            
            if("n".equalsIgnoreCase(leer)){
                empleado.setIdJefe(0);
                flag = false;
            }
            else{
                idJefe = Integer.parseInt(leer);
                for(Empleado l :  listaEmp){ 
                    if(idJefe == l.getId()){
                        empleado.setIdJefe(idJefe);
                        flag = false;
                        break;
                    } 
                    else{
                        flag = true;
                    }
                }
            }

        } while (flag);
 
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
        super.out.print("Mes (en numeros): ");
        leer = super.in.readLine();
        mes = Integer.parseInt(leer);
        super.out.print("Anio: ");
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
    
    public List<Empleado> listaJefes(Connection con) throws SQLException{
        List<Empleado> lista = new ArrayList();
        
        String query = "select * from ADM_EMP_EMPLEADO where EMP_ID_JEFE is null and EST_ID = 1";
        PreparedStatement pre = con.prepareStatement(query);
        ResultSet rs = pre.executeQuery();
        
        lista = mapeoSeleccionar(rs);
        
        rs.close();
        pre.close();
        
        return lista;
    }
    
    public List<Empleado> listaNoJefes(Connection con) throws SQLException{
        List<Empleado> lista = new ArrayList();
        
        String query = "select * from ADM_EMP_EMPLEADO where EMP_ID_JEFE is not null and EST_ID = 1";
        PreparedStatement pre = con.prepareStatement(query);
        ResultSet rs = pre.executeQuery();
        
        lista = mapeoSeleccionar(rs);
        
        rs.close();
        pre.close();
        
        return lista;
    }
    
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
            empleado.setIdJefe(rs.getInt("EMP_ID_JEFE"));
            
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
    
    public int impresionSalarioEmpleado(List<Empleado> lista) throws SQLException {
        
        int i = 0;
        super.out.println("Lista de Empleados:\n");
        super.out.println("Id\tNombre\tApellido\tSalario\tDepartamento\tPuesto");
        for(Empleado l : lista){
            i++;
            super.out.print(l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getApellido()+ "\t");
            super.out.print(l.getSalario() + "\t");
            
            DepartamentoDAO departamentoDAO = new DepartamentoDAO();
            departamentoDAO.setConexion(super.getConexion());
            try {
                if(l.getIdEstado()== 0){
                    super.out.print("Ninguno");
                }
                else{
                    super.out.print(departamentoDAO.seleccionar(l.getIdDepartamento()).get(0).getNombre() + "\t");
                }
                
                PuestoDAO puestoDAO = new PuestoDAO();
                puestoDAO.setConexion(super.getConexion());
                
                if(l.getIdPuesto()== 0){
                    super.out.println("Ninguno");
                }
                else{
                    super.out.println(puestoDAO.seleccionar(l.getIdPuesto()).get(0).getNombre());
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
    
    public void actualizarSalarioEmpleado(Empleado empleado, Connection con, Socket cliente) throws Exception{
        int i;
        String leer;
        double salario = 0;
        double salarioMin = 0;
        double salarioMax = 0;
        setConexion(con);
        PuestoDAO puestoDAO;
        i = impresionSalarioEmpleado(seleccionar());
        
        if(i != 0){
            super.out.print("Ingrese el id del empleado a actualizar: ");
            leer = super.in.readLine();
            empleado.setId(Integer.parseInt(leer));
            
            String queryS = "select ADM_PUE_PUESTO_TRABAJO.PUE_ID, PUE_SALARIO_MINIMO, PUE_SALARIO_MAXIMO from ADM_PUE_PUESTO_TRABAJO join ADM_EMP_EMPLEADO where EMP_ID = ?";
            PreparedStatement preS = con.prepareStatement(queryS);
            preS.setInt(1, empleado.getId());
            ResultSet rs = preS.executeQuery();
            if(rs.next()){
                empleado.setIdPuesto(rs.getInt("PUE_ID"));
                salarioMin = rs.getDouble("PUE_SALARIO_MINIMO");
                salarioMax = rs.getDouble("PUE_SALARIO_MAXIMO");
            }
            
            rs.close();
            preS.close();
            
            super.out.println("A continuacion se muestran los detalles del puesto, para saber el rango permitido de salario a aplicar al empleado");
            puestoDAO = new PuestoDAO();
            puestoDAO.setCliente(cliente);
            puestoDAO.setConexion(super.getConexion());
            puestoDAO.impresion(puestoDAO.seleccionar(empleado.getIdPuesto()));
            
            do {                
                super.out.print("Ingrese el salario (debe estar entre el rango permitido por el puesto): ");
                leer = super.in.readLine();
                salario = Double.parseDouble(leer);
                
            } while (salario<salarioMin || salario>salarioMax);
            empleado.setSalario(salario);
            
            setConexion(con);
            String query = "update ADM_EMP_EMPLEADO set EMP_SALARIO = ? where EMP_ID = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setDouble(1, empleado.getSalario());
            pre.setInt(2, empleado.getId());
            
            pre.executeUpdate();

            pre.close();
            
            super.out.println("\nSalario de empleado actualizado correctamente!");
        }
        else{
            super.out.println("\nNo hay empleados para actualizar!");
            empleado.setId(0);
            actualizar(empleado);
        }
    }
    
    public int impresionDepartamentoEmpleado(List<Empleado> lista) throws SQLException {
        
        int i = 0;
        super.out.println("Lista de Empleados:\n");
        super.out.println("Id\tNombre\tApellido\tDepartamento\tPuesto");
        for(Empleado l : lista){
            i++;
            super.out.print(l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getApellido()+ "\t");
            
            DepartamentoDAO departamentoDAO = new DepartamentoDAO();
            departamentoDAO.setConexion(super.getConexion());
            try {
                if(l.getIdDepartamento()== 0){
                    super.out.print("Ninguno");
                }
                else{
                    super.out.print(departamentoDAO.seleccionar(l.getIdDepartamento()).get(0).getNombre() + "\t");
                }
                
                PuestoDAO puestoDAO = new PuestoDAO();
                puestoDAO.setConexion(super.getConexion());
                
                if(l.getIdPuesto()== 0){
                    super.out.println("Ninguno");
                }
                else{
                    super.out.println(puestoDAO.seleccionar(l.getIdPuesto()).get(0).getNombre());
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
    
    public void actualizarDepartametoEmpleado(Empleado empleado, Connection con, Socket cliente) throws Exception{
        int i;
        String leer;
        boolean flag = false;
        int idPuesto;
        setConexion(con);
        DepartamentoDAO departamentoDAO;
        PuestoDAO puestoDAO;
        i = impresionDepartamentoEmpleado(seleccionar());
        
        if(i != 0){
            super.out.print("Ingrese el id del empleado a actualizar: ");
            leer = super.in.readLine();
            empleado.setId(Integer.parseInt(leer));
            
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
            puestoDAO.impresion(lista);
            
            do {
                super.out.print("Ingrese el id del puesto del listado anterior: ");
                leer = super.in.readLine();
                idPuesto = Integer.parseInt(leer);

                for(Puesto l :  lista){ 
                    if(idPuesto == l.getId()){
                        empleado.setIdPuesto(idPuesto);
                        flag = false;
                        break;
                    } 
                    else{
                        flag = true;
                    }
                }
            } while (flag);
            
            setConexion(con);
            String query = "update ADM_EMP_EMPLEADO set DEP_ID = ?, PUE_ID = ? where EMP_ID = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setInt(1, empleado.getIdDepartamento());
            pre.setInt(2, empleado.getIdPuesto());
            pre.setInt(3, empleado.getId());
            
            pre.executeUpdate();

            pre.close();
            
            super.out.println("\nDepartamento y puesto de empleado actualizado correctamente!");
        }
        else{
            super.out.println("\nNo hay empleados para actualizar!");
            empleado.setId(0);
            actualizar(empleado);
        }
    }
    
    public int impresionJefeEmpleado(List<Empleado> lista) throws SQLException, Exception {
        
        int i = 0;
        super.out.println("Lista de Empleados:\n");
        super.out.println("Id\tNombre\tApellido\tJefe");
        for(Empleado l : lista){
            i++;
            super.out.print(l.getId() + "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.print(l.getApellido()+ "\t");
            
            if(l.getIdJefe()== 0){
                super.out.println("No tiene");
            }
            else{
                super.out.print(seleccionar(l.getIdJefe()).get(0).getNombre() + " ");
                super.out.println(seleccionar(l.getIdJefe()).get(0).getApellido());
            }

        }
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    public void actualizarJefeEmpleado(Empleado empleado, Connection con, Socket cliente) throws Exception{
        int i;
        int id;
        int idJefe;
        boolean flag = false;
        String leer;
        setConexion(con);
        i = impresionJefeEmpleado(listaNoJefes(super.getConexion()));
        
        if(i != 0){
            List<Empleado> lista = new ArrayList();
            lista = listaNoJefes(super.getConexion());
            
            do {
                super.out.print("Ingrese el id del empleado a actualizar: ");
                leer = super.in.readLine();
                id = Integer.parseInt(leer);

                for(Empleado l :  lista){ 
                    if(id == l.getId()){
                        empleado.setId(id);
                        flag = false;
                        break;
                    } 
                    else{
                        flag = true;
                    }
                }
            } while (flag);
            
            super.out.println("Lista de jefes: ");
            List<Empleado> listaEmp = new ArrayList();
            listaEmp = listaJefes(super.getConexion());
            impresionDatosPersonales(listaJefes(super.getConexion()));
            do {
                super.out.print("Ingrese el id del jefe que tendra el empleado: ");
                leer = super.in.readLine();
                idJefe = Integer.parseInt(leer);
                
                for(Empleado l :  listaEmp){ 
                    if(idJefe == l.getId()){
                        empleado.setIdJefe(idJefe);
                        flag = false;
                        break;
                    } 
                    else{
                        flag = true;
                    }
                    
                }

            } while (flag);
            
            setConexion(con);
            String query = "update ADM_EMP_EMPLEADO set EMP_ID_JEFE = ? where EMP_ID = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setInt(1, empleado.getIdJefe());
            pre.setInt(2, empleado.getId());
            
            pre.executeUpdate();

            pre.close();
            
            super.out.println("\nJefe de empleado actualizado correctamente!");
        }
        else{
            super.out.println("\nNo hay empleados para actualizar!");
            empleado.setId(0);
            actualizar(empleado);
        }
    }

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

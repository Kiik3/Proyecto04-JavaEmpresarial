
package com.conexionSocket;

import com.conexionBD.ConexionBD;
import com.correo.CorreoElectronico;
import com.dao.DepartamentoDAO;
import com.dao.DescuentoLeyDAO;
import com.dao.EmpleadoDAO;
import com.dao.EstadoDAO;
import com.dao.PuestoDAO;
import com.dao.RentaDAO;
import com.dao.RolDAO;
import com.dao.UsuarioDAO;
import com.entidades.Departamento;
import com.entidades.DescuentoLey;
import com.entidades.Empleado;
import com.entidades.Estado;
import com.entidades.Puesto;
import com.entidades.Renta;
import com.entidades.Rol;
import com.entidades.Usuario;
import com.login.Login;
import com.planilla.PlanillaDAO;
import com.propiedades.Propiedades;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Enrique Ochoa
 */
public class Hilo extends Thread{
    
    private Socket cliente;
    PrintStream out;
    BufferedReader in;
    
    public void setCliente(Socket cliente){
        this.cliente = cliente;
    }
    
    @Override
    public void run(){
        Scanner scanner = new Scanner(System.in);
        boolean flagInicio = true;
        int opcion;

        try {
            out = new PrintStream(cliente.getOutputStream());
            in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            
        } catch (IOException ex) {
            Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println("---Bienvenido a la administacion de empleados---");
        out.println("Inicio de sesion. Si aun no esta registrado, consulte a su administrador para que lo ingrese al sistema.");
        
        do{
            try {
                flagInicio = false;
                ConexionBD con = new ConexionBD();

                out.println("Presione 'Y' para continuar o cualquier tecla para salir");
                
                if(in.readLine().equalsIgnoreCase("y")){
                    boolean flagValidacion = false;
                    do {
                        flagValidacion = false;

                        out.print("Ingrese su usuario: ");
                        String nombre = in.readLine();
                        out.print("Ingrese la contrasena: ");
                        String contraseña = in.readLine();

                        Login login = new Login();
                        String validacion = login.validacion(con.iniciarConexionBD(), nombre, contraseña);

                        if(!(validacion == null)){
                        out.println("\nBienvenido " + validacion + " " + nombre);
                        
                        Propiedades propiedades = new Propiedades();
                        GregorianCalendar gc = new GregorianCalendar();
                        byte fechaHoy = (byte) gc.get(GregorianCalendar.DAY_OF_MONTH);
                        byte fechaPago = Byte.parseByte(propiedades.cargarPropiedades().getProperty("fechaPla"));
                        boolean pagar = Boolean.valueOf(propiedades.cargarPropiedades().getProperty("pagar"));

                        if(validacion.equalsIgnoreCase("Administrador")){
                            
                            //autenticacion(1);

                            boolean flag = true;
                            String o;
                            do {
                                out.println("\nSeleccione una opcion: ");
                                out.println("1. Gestion de Departamentos\t 2. Gestion de Estados de empleado\t 3. Gestion de Puestos");
                                out.println("4. Gestion de Usuarios\t\t 5. Gestion de Roles\t\t\t 6. Gestion de Descuentos de ley");
                                out.println("7. Gestion de Tramos de renta\t 8. Actualizacion fecha de pago de planilla");
                                try {
                                    flag = false;
                                    o = in.readLine();
                                    opcion = Integer.parseInt(o);

                                    switch(opcion){
                                        case 1:
                                            Departamento departamento = new Departamento();
                                            DepartamentoDAO departamentoDAO = new DepartamentoDAO();
                                            menuGestion();
                                            
                                            o = in.readLine();
                                            opcion = Integer.parseInt(o);
                                            departamentoDAO.setCliente(cliente);
                                            flag = departamentoDAO.ingresoDatosGestion(opcion, departamento, con.iniciarConexionBD());
                                            break;
                                        case 2:
                                            Estado estado = new Estado();
                                            EstadoDAO estadoDAO = new EstadoDAO();
                                            menuGestion();

                                            o = in.readLine();
                                            opcion = Integer.parseInt(o);
                                            estadoDAO.setCliente(cliente);
                                            flag = estadoDAO.ingresoDatosGestion(opcion, estado, con.iniciarConexionBD());
                                            break;
                                        case 3:
                                            Puesto puesto = new Puesto();
                                            PuestoDAO puestoDAO = new PuestoDAO();
                                            menuGestion();

                                            o = in.readLine();
                                            opcion = Integer.parseInt(o);
                                            puestoDAO.setCliente(cliente);
                                            flag = puestoDAO.ingresoDatosGestion(opcion, puesto, con.iniciarConexionBD(), cliente);
                                            break;
                                        case 4:
                                            Usuario usuario = new Usuario();
                                            UsuarioDAO usuarioDAO = new UsuarioDAO();
                                            menuGestion();

                                            o = in.readLine();
                                            opcion = Integer.parseInt(o);
                                            usuarioDAO.setCliente(cliente);
                                            flag = usuarioDAO.ingresoDatosGestion(opcion, usuario, con.iniciarConexionBD(), cliente);
                                            break;
                                        case 5:
                                            Rol rol = new Rol();
                                            RolDAO rolDAO = new RolDAO();
                                            menuGestion();

                                            o = in.readLine();
                                            opcion = Integer.parseInt(o);
                                            rolDAO.setCliente(cliente);
                                            flag = rolDAO.ingresoDatosGestion(opcion, rol, con.iniciarConexionBD());
                                            break;
                                        case 6:
                                            DescuentoLey descuentoLey = new DescuentoLey();
                                            DescuentoLeyDAO descuentoLeyDAO = new DescuentoLeyDAO();
                                            menuGestion();

                                            o = in.readLine();
                                            opcion = Integer.parseInt(o);
                                            descuentoLeyDAO.setCliente(cliente);
                                            flag = descuentoLeyDAO.ingresoDatosGestion(opcion, descuentoLey, con.iniciarConexionBD());
                                            break;
                                        case 7:
                                            Renta renta = new Renta();
                                            RentaDAO rentaDAO = new RentaDAO();
                                            menuGestion();

                                            o = in.readLine();
                                            opcion = Integer.parseInt(o);
                                            rentaDAO.setCliente(cliente);
                                            flag = rentaDAO.ingresoDatosGestion(opcion, renta, con.iniciarConexionBD());
                                            break;
                                        case 8:
                                            out.println("La fecha que se paga planilla es " + fechaPago + " de cada mes");
                                            int dia;
                                            do {                                                
                                                out.print("Ingresa el nuevo dia del mes (1-30): ");
                                                dia = Integer.parseInt(in.readLine());
                                            } while (dia <= 0 || dia > 30);
                                            
                                            propiedades.insertarPropiedades("fechaPla", String.valueOf(dia), "src/com/propiedades/configuracion.properties");
                                            fechaPago = Byte.parseByte((propiedades.cargarPropiedades().getProperty("fechaPla")));
                                            out.println("\nActualizado dia de pago de planilla");
                                            break;
                                        default:
                                            out.println("Ingresa una opcion valida");
                                            flag = true;
                                    }

                                } catch (Exception e) {
                                    out.println("Ingresa una opcion valida");
                                    e.printStackTrace();
                                    flag = true;
                                }
                                flag = regresarMenu(flag);
                            } while (flag);
                        }
                        
                        else if(validacion.equalsIgnoreCase("Usuario")){
                            //autenticacion(2);
                            
                            
                            if(fechaHoy == fechaPago-1){
                                propiedades.insertarPropiedades("pagar", "true", "src/com/propiedades/configuracion.properties");
                            }
                            if(fechaHoy == fechaPago && pagar){
                                out.println("\nATENCION! ESTE DIA SE DEBE PAGAR PLANILLA");
                                out.print("Desea pagar la planilla en este momento? 'Y' si, culquier tecla, no: ");
                                
                                if("Y".equalsIgnoreCase(in.readLine())){
                                    PlanillaDAO planillaDAO = new PlanillaDAO();
                                    planillaDAO = new PlanillaDAO();
                                    planillaDAO.calcularPlanilla(con.iniciarConexionBD(), cliente, fechaHoy, fechaPago, pagar);
                                    
                                    propiedades.insertarPropiedades("pagar", "false", "src/com/propiedades/configuracion.properties");
                                }
                                else{
                                    out.println("Ok, puedes pagar la planilla ingresando en la opcion 8");
                                }    
                            }
                            
                            pagar = Boolean.valueOf(propiedades.cargarPropiedades().getProperty("pagar"));
                            boolean flag = true;
                            String o;
                            do {
                                out.println("\nSeleccione una opcion: ");
                                out.println("1. Actualizacion de datos de personales del empleado\n\r2. Actualizacion de estado de empleado (despido, etc..)");
                                out.println("3. Contratacion de empleado\n\r4. Actualizacion de departamento y puesto del empleado\n\r5. Asignacion de jefatura");
                                out.println("6. Actualizacion de salario de empleado\n\r7. Visualizacion de pagos generados\n\r8. Generacion de pagos en planilla");
                                try {
                                    flag = false;
                                    o = in.readLine();
                                    opcion = Integer.parseInt(o);

                                    Empleado empleado = new Empleado();
                                    EmpleadoDAO empleadoDAO = new EmpleadoDAO();
                                    PlanillaDAO planillaDAO = new PlanillaDAO();

                                    switch(opcion){
                                        case 1:  
                                            empleadoDAO.setCliente(cliente);
                                            empleadoDAO.actualizarDatosPersonales(empleado, con.iniciarConexionBD(), cliente);

                                            break;
                                        case 2:
                                            empleadoDAO.setCliente(cliente);
                                            empleadoDAO.actualizarEstadoEmpleado(empleado, con.iniciarConexionBD(), cliente);
                                            break;
                                        case 3:
                                            empleadoDAO.setCliente(cliente);
                                            empleadoDAO.ingresarEmpleado(empleado, con.iniciarConexionBD(), cliente);

                                            break;
                                        case 4:
                                            empleadoDAO.setCliente(cliente);
                                            empleadoDAO.actualizarDepartametoEmpleado(empleado, con.iniciarConexionBD(), cliente);
                                            
                                            break;
                                        case 5:
                                            empleadoDAO.setCliente(cliente);
                                            empleadoDAO.actualizarJefeEmpleado(empleado, con.iniciarConexionBD(), cliente);
                                            
                                            break;
                                        case 6:
                                            empleadoDAO.setCliente(cliente);
                                            empleadoDAO.actualizarSalarioEmpleado(empleado, con.iniciarConexionBD(), cliente);
                                            break;
                                        case 7:
                                            planillaDAO = new PlanillaDAO();
                                            planillaDAO.verHistorial(con.iniciarConexionBD(), cliente);
                                            
                                            break;
                                        case 8:
                                            planillaDAO = new PlanillaDAO();
                                            planillaDAO.calcularPlanilla(con.iniciarConexionBD(), cliente, fechaHoy, fechaPago, pagar);

                                            break;
                                        default:
                                            out.println("\nIngresa una opcion valida");
                                            flag = true;
                                    }

                                } catch (Exception e) {
                                    out.println("\nIngresa una opcion valida");
                                    e.printStackTrace();
                                    flag = true;
                                }
                                flag = regresarMenu(flag);
                            } while (flag);
                        }
                    }
                    else{
                        out.println("Usuario/Contrasena incorrectas, intente nuevamente\n");
                        flagValidacion = true;
                    }
                    } while (flagValidacion);

                }
                else{
                    out.println("Hasta luego!");
                    cliente.close();
                    //flagInicio = true;
                }
            
            ConexionBD.cerrarConexionDB(con.iniciarConexionBD());
            } catch (Exception e) {
                out.println("No se pudo establecer la conexion con la Base de Datos");
                e.printStackTrace();
                flagInicio = true;
            }
        }while(flagInicio);
    }
    
    public void autenticacion(int tipo) throws EmailException, IOException{
        CorreoElectronico correo = new CorreoElectronico();
        boolean flag = false;
        
        int numero = (int) (Math.random()*(999999-100000+1)+100000);
        out.print("Para terminar la autenticacion, ingresa tu correo electronico de gmail: ");
        
        do {            
            try {
                correo.mandarCorreo(in.readLine(), tipo, numero);
                out.print("Se ha enviado un codigo de autenticacion a tu correo, digitalo: ");
                flag = false;
                do {                    
                    try {
                        while(numero != Integer.parseInt(in.readLine())){
                            out.print("Codigo incorrecto, ingresalo nuevamente: ");
                        }
                        out.println("Perfecto! Ya puedes ingresar");
                        flag = false;
                    } catch (Exception e) {
                        out.print("Codigo incorrecto, ingresalo nuevamente: ");
                        flag = true;
                    }
                } while (flag);
  
            } catch (Exception e) {
                out.print("Ingresa un correo electronico valido de gmail: ");
                flag = true;
            }
        } while (flag);
        
        
    }
    
    public void menuGestion(){
        out.println("1. Listar\t 2. Filtrar por id\t 3. Ingresar");
        out.println("4. Actualizar\t 5. Eliminar");
    }
    
    public boolean regresarMenu(boolean flag) throws IOException{
        if(flag == false){
            out.println("\nPresione 'Y' para regresar al menu, o cualquier tecla para salir");
            if(in.readLine().equalsIgnoreCase("y")){
                flag = true;
            }
            else{
                out.println("Hasta luego!");
                cliente.close();
                flag = false;
            }
            
        }
        return flag;
    }
}

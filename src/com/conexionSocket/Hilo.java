
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.GregorianCalendar;
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
    
    //Ingresar un nuevo cliente
    public void setCliente(Socket cliente){
        this.cliente = cliente;
    }
    
    @Override
    public void run(){
        boolean flagInicio = true;
        int opcion;

        try {
            //Se inicializan las variables de entrada y salida
            out = new PrintStream(cliente.getOutputStream());
            in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            
        } catch (IOException ex) {
            Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        out.println("---Bienvenido a la administacion de empleados---");
        out.println("\rInicio de sesion. Si aun no esta registrado, consulte a su administrador para que lo ingrese al sistema.");
        
        do{
            try {
                flagInicio = false; //bandera
                ConexionBD con = new ConexionBD();

                out.println("\rPresione 'Y' para continuar o cualquier tecla para salir");
                
                //Si presione y continua
                if(in.readLine().equalsIgnoreCase("y")){
                    boolean flagValidacion = false; //bandera
                    do {
                        flagValidacion = false;

                        out.print("\rIngrese su usuario: ");
                        String nombre = in.readLine();
                        out.print("\rIngrese la contrasena: ");
                        String contraseña = in.readLine();
                        
                        //Se valida que el usuario ya este registrado, con su username y contraseña
                        //Si es null es porque ha ingresado mal usuario o contraseña
                        Login login = new Login();
                        String validacion = login.validacion(con.iniciarConexionBD(), nombre, contraseña);

                        if(!(validacion == null)){
                        out.println("\n\rBienvenido " + validacion + " " + nombre);
                        
                        Propiedades propiedades = new Propiedades();
                        GregorianCalendar gc = new GregorianCalendar();
                        byte fechaHoy = (byte) gc.get(GregorianCalendar.DAY_OF_MONTH); //Se obtiene la fecha de hoy
                        
                        //Se carga la fecha de pago de planilla y si ya se pagó o no
                        byte fechaPago = Byte.parseByte(propiedades.cargarFechaPla().getProperty("fechaPla"));
                        boolean pagar = Boolean.valueOf(propiedades.cargarPagarPla().getProperty("pagar"));
                        
                        //Se valida si ingresa un administrador o usuario rrhh
                        if(validacion.equalsIgnoreCase("Administrador")){
                            
                            //Se hace una autenticación con el correo electrónico
                            autenticacion(1);

                            boolean flag = true;
                            String o;
                            do {
                                out.println("\n\rSeleccione una opcion: ");
                                out.println("\r1. Gestion de Departamentos\t 2. Gestion de Estados de empleado\t 3. Gestion de Puestos");
                                out.println("\r4. Gestion de Usuarios\t\t 5. Gestion de Roles\t\t\t 6. Gestion de Descuentos de ley");
                                out.println("\r7. Gestion de Tramos de renta\t 8. Actualizacion fecha de pago de planilla");
                                try {
                                    flag = false;
                                    o = in.readLine();
                                    opcion = Integer.parseInt(o);
                                    
                                    //Acción según opción ingresada
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
                                            //Se puede actualizar la fecha en que se paga planilla cada mes
                                            out.println("\rLa fecha que se paga planilla es " + fechaPago + " de cada mes");
                                            int dia;
                                            //Se valida que ingrese una opción válida
                                            do {                                                
                                                out.print("\rIngresa el nuevo dia del mes (1-30): ");
                                                dia = Integer.parseInt(in.readLine());
                                            } while (dia <= 0 || dia > 30);
                                            
                                            propiedades.insertarPla("fechaPla", String.valueOf(dia));
                                            fechaPago = Byte.parseByte((propiedades.cargarFechaPla().getProperty("fechaPla")));
                                            out.println("\n\rActualizado dia de pago de planilla");
                                            break;
                                        default:
                                            out.println("\rIngresa una opcion valida");
                                            flag = true;
                                    }

                                } catch (Exception e) {
                                    out.println("\rIngresa una opcion valida");
                                    e.printStackTrace();
                                    flag = true;
                                }
                                flag = regresarMenu(flag);
                            } while (flag);
                        }
                        
                        else if(validacion.equalsIgnoreCase("Usuario")){
                            //Se hace una autenticación con el correo electrónico
                            autenticacion(2);
                            
                            //Se modifica el archivo de propiedades un día antes que se page planilla, para que quede activo el pago
                            if(fechaHoy == fechaPago-1){
                                propiedades.insertarPropiedades("pagar", "true");
                            }
                            //El día de pago aparece un mensaje al usuario rrhh
                            if(fechaHoy == fechaPago && pagar){
                                out.println("\n\rATENCION! ESTE DIA SE DEBE PAGAR PLANILLA");
                                out.print("\rDesea pagar la planilla en este momento? 'Y' si, culquier tecla, no: ");
                                
                                if("Y".equalsIgnoreCase(in.readLine())){
                                    PlanillaDAO planillaDAO = new PlanillaDAO();
                                    planillaDAO = new PlanillaDAO();
                                    planillaDAO.calcularPlanilla(con.iniciarConexionBD(), cliente, fechaHoy, fechaPago, pagar);
                                    
                                    //Se coloca como inactivo el pago, hasta un dia antes del siguiente mes, se vuelve a activar
                                    propiedades.insertarPropiedades("pagar", "false");
                                }
                                else{
                                    out.println("\rOk, puedes pagar la planilla ingresando en la opcion 8");
                                }    
                            }
                            
                            pagar = Boolean.valueOf(propiedades.cargarPropiedades().getProperty("pagar"));
                            boolean flag = true;
                            String o;
                            do {
                                out.println("\n\rSeleccione una opcion: ");
                                out.println("\r1. Actualizacion de datos de personales del empleado\n\r2. Actualizacion de estado de empleado (despido, etc..)");
                                out.println("\r3. Contratacion de empleado\n\r4. Actualizacion de departamento y puesto del empleado\n\r5. Asignacion de jefatura");
                                out.println("\r6. Actualizacion de salario de empleado\n\r7. Visualizacion de pagos generados\n\r8. Generacion de pagos en planilla");
                                try {
                                    flag = false;
                                    o = in.readLine();
                                    opcion = Integer.parseInt(o);

                                    Empleado empleado = new Empleado();
                                    EmpleadoDAO empleadoDAO = new EmpleadoDAO();
                                    PlanillaDAO planillaDAO = new PlanillaDAO();
                                    
                                    //Se lleva a cabo acción según opción
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
                                            out.println("\n\rIngresa una opcion valida");
                                            flag = true;
                                    }

                                } catch (Exception e) {
                                    out.println("\n\rIngresa una opcion valida");
                                    e.printStackTrace();
                                    flag = true;
                                }
                                flag = regresarMenu(flag);
                            } while (flag);
                        }
                    }
                    else{
                        out.println("\rUsuario/Contrasena incorrectas, intente nuevamente\n");
                        flagValidacion = true;
                    }
                    } while (flagValidacion);

                }
                else{
                    out.println("\rHasta luego!");
                    cliente.close();
                }
            
            ConexionBD.cerrarConexionDB(con.iniciarConexionBD());
            } catch (Exception e) {
                out.println("\rNo se pudo establecer la conexion con la Base de Datos");
                e.printStackTrace();
                flagInicio = true;
            }
        }while(flagInicio);
    }
    
    //función que manda un email con un código de verificación
    public void autenticacion(int tipo) throws EmailException, IOException{
        CorreoElectronico correo = new CorreoElectronico();
        boolean flag = false;
        
        int numero = (int) (Math.random()*(999999-100000+1)+100000); //generación de código
        out.print("\rPara terminar la autenticacion, ingresa tu correo electronico de gmail: ");
        
        do {            
            try {
                correo.mandarCorreo(in.readLine(), tipo, numero);
                out.print("\rSe ha enviado un codigo de autenticacion a tu correo, digitalo: ");
                flag = false;
                do {                    
                    try {
                        while(numero != Integer.parseInt(in.readLine())){
                            out.print("\rCodigo incorrecto, ingresalo nuevamente: ");
                        }
                        out.println("\rPerfecto! Ya puedes ingresar");
                        flag = false;
                    } catch (Exception e) {
                        out.print("\rCodigo incorrecto, ingresalo nuevamente: ");
                        flag = true;
                    }
                } while (flag);
  
            } catch (Exception e) {
                out.print("\rIngresa un correo electronico valido de gmail: ");
                flag = true;
            }
        } while (flag);
        
        
    }
    
    //función que imprime menú de gestión de tabla catálogo
    public void menuGestion(){
        out.println("\r1. Listar\t 2. Filtrar por id\t 3. Ingresar");
        out.println("\r4. Actualizar\t 5. Eliminar");
    }
    
    //función que pregunta si se quiere regresar al menú
    public boolean regresarMenu(boolean flag) throws IOException{
        if(flag == false){
            out.println("\n\rPresione 'Y' para regresar al menu, o cualquier tecla para salir");
            if(in.readLine().equalsIgnoreCase("y")){
                flag = true;
            }
            else{
                out.println("\rHasta luego!");
                cliente.close();
                flag = false;
            }
            
        }
        return flag;
    }
}

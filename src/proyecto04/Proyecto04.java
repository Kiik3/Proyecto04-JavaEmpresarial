
package proyecto04;

import com.conexionBD.ConexionBD;
import com.dao.DepartamentoDAO;
import com.dao.EstadoDAO;
import com.dao.PuestoDAO;
import com.dao.RolDAO;
import com.dao.UsuarioDAO;
import com.entidades.Departamento;
import com.entidades.Estado;
import com.entidades.Puesto;
import com.entidades.Rol;
import com.entidades.Usuario;
import com.login.Login;
import java.util.Scanner;

/**
 *
 * @author Enrique Ochoa
 */
public class Proyecto04 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean flagInicio = true;
        int opcion;
        
        System.out.println("---Bienvenido a la administación de empleados---");
        System.out.println("Inicio de sesión. Si aún no está registrado, consulte a su administrador para que lo ingrese al sistema.");
        
        do{
            try {
                flagInicio = false;
                ConexionBD con = new ConexionBD();

                System.out.println("Presione 'Y' para continuar o cualquier tecla para salir");

                if(scanner.next().equalsIgnoreCase("y")){
                    boolean flagValidacion = false;
                    do {
                        flagValidacion = false;

                        System.out.print("Ingrese su usuario: ");
                        String nombre = scanner.next();
                        System.out.print("Ingrese la contraseña: ");
                        String contraseña = scanner.next();

                        Login login = new Login();
                        String validacion = login.validacion(con.iniciarConexionBD(), nombre, contraseña);

                        if(!(validacion == null)){
                        System.out.println("\nBienvenido " + validacion + " " + nombre);

                        System.out.println("\nSeleccione una opción: ");
                        if(validacion.equalsIgnoreCase("Administracion")){

                            boolean flag = true;
                            String o;
                            do {
                                System.out.println("1. Gestión de Departamentos\t 2. Gestion de Estados de empleado\t 3. Gestion de Puestos");
                                System.out.println("4. Gestión de Usuarios\t\t 5. Gestión de Roles");                            
                                try {
                                    flag = false;
                                    o = scanner.next();
                                    opcion = Integer.parseInt(o);
                                    //System.out.println("\nSeleccione una opción: ");

                                    switch(opcion){
                                        case 1:
                                            Departamento departamento = new Departamento();
                                            DepartamentoDAO departamentoDAO = new DepartamentoDAO();
                                            menuGestion();
                                            
                                            opcion = scanner.nextInt();
                                            departamentoDAO.ingresoDatosGestion(opcion, departamento, con.iniciarConexionBD());
                                            break;
                                        case 2:
                                            Estado estado = new Estado();
                                            EstadoDAO estadoDAO = new EstadoDAO();
                                            menuGestion();

                                            opcion = scanner.nextInt();
                                            estadoDAO.ingresoDatosGestion(opcion, estado, con.iniciarConexionBD());
                                            break;
                                        case 3:
                                            Puesto puesto = new Puesto();
                                            PuestoDAO puestoDAO = new PuestoDAO();
                                            menuGestion();

                                            opcion = scanner.nextInt();
                                            puestoDAO.ingresoDatosGestion(opcion, puesto, con.iniciarConexionBD());
                                            break;
                                        case 4:
                                            Usuario usuario = new Usuario();
                                            UsuarioDAO usuarioDAO = new UsuarioDAO();
                                            menuGestion();

                                            opcion = scanner.nextInt();
                                            usuarioDAO.ingresoDatosGestion(opcion, usuario, con.iniciarConexionBD());
                                            break;
                                        case 5:
                                            Rol rol = new Rol();
                                            RolDAO rolDAO = new RolDAO();
                                            menuGestion();

                                            opcion = scanner.nextInt();
                                            rolDAO.ingresoDatosGestion(opcion, rol, con.iniciarConexionBD());
                                            break;
                                        default:
                                            System.out.println("Ingresa una opción válida");
                                            flag = true;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Ingresa una opción válida");
                                    e.printStackTrace();
                                    flag = true;
                                }
                                System.out.println("\nPresione 'Y' para regresar al menú, o cualquier tecla para salir");
                                if(scanner.next().equalsIgnoreCase("y")){
                                    flag = true;
                                }
                                else{
                                    System.out.println("Hasta luego!");
                                }
                            } while (flag);

                        }
                    }
                    else{
                        System.out.println("Usuario/Contraseña incorrectas, intente nuevamente\n");
                        flagValidacion = true;
                    }
                    } while (flagValidacion);

                }
                else{
                    System.out.println("Hasta luego!");
                    System.exit(0);
                    flagInicio = true;
                }
            
            ConexionBD.cerrarConexionDB(con.iniciarConexionBD());
            } catch (Exception e) {
                System.out.println("No se pudo establecer la conexión con la Base de Datos");
                e.printStackTrace();
                flagInicio = true;
            }
        }while(flagInicio);
    }
    
    public static void menuGestion(){
        System.out.println("1. Listar\t 2. Filtrar por id\t 3. Ingresar");
        System.out.println("4. Actualizar\t 5. Eliminar");
    }
    
}

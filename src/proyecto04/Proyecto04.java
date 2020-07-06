
package proyecto04;

import com.conexionSocket.ConexionSocket;

/**
 *
 * @author Enrique Ochoa
 */
public class Proyecto04 {

    public static void main(String[] args) {
        
        try {
            //Se abre la conexion para que puedan entrar multiples clientes
            ConexionSocket conSocket = new ConexionSocket();
            conSocket.recibirConexion();
        } catch (Exception e) {
            System.out.println("No se pudo establecer la conexion");
        }

    }
    
}

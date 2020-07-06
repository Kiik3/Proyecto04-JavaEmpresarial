
package com.conexionSocket;

import com.propiedades.Propiedades;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Enrique Ochoa
 */
public class ConexionSocket {
    
    private Socket cliente;
    private ServerSocket server;
    
    public void recibirConexion() throws IOException{
        
        //Cargamos el archivo de propiedades para traer el puerto para que se abra la conexión
        Propiedades propiedades = new Propiedades();
        server = new ServerSocket(Integer.parseInt(propiedades.cargarPropiedades().getProperty("portS")));        
        System.out.println("Esperando usuarios...");
        
        do{
            cliente = new Socket();
            cliente = server.accept(); //Se acepta un nuevo cliente
            
            //Cada nuevo cliente se ejecuta en un hilo diferente
            Hilo hilo = new Hilo();
            hilo.setCliente(cliente);
            hilo.start();
            
        }while(server.isBound());
    }
}

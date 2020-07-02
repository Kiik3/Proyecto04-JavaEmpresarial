
package com.conexionSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Enrique Ochoa
 */
public class ConexionSocket {
    
    private int port = 8888;
    private Socket cliente;
    private ServerSocket server;
    
    public void recibirConexion() throws IOException{
        
        server = new ServerSocket(port);        
        System.out.println("Esperando usuarios...");
        
        do{
            cliente = new Socket();
            cliente = server.accept();
            
            Hilo hilo = new Hilo();
            hilo.setCliente(cliente);
            hilo.start();
            
        }while(server.isBound());
    }
}

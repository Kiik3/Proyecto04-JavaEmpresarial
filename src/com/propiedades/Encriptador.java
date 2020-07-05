
package com.propiedades;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author Enrique Ochoa
 */
public class Encriptador {
    
    BasicTextEncryptor encriptadorBasico;
    final String PASS = "secret-data";
    
    //Método para encriptar
    public String encriptador(String contraseña){
        encriptadorBasico = new BasicTextEncryptor();
        encriptadorBasico.setPassword(PASS);
        
        return encriptadorBasico.encrypt(contraseña);
    }
    
    //Método para desencriptar
    public String desencriptador(String texto){
        encriptadorBasico = new BasicTextEncryptor();
        encriptadorBasico.setPassword(PASS);
        return encriptadorBasico.decrypt(texto);
    }
}

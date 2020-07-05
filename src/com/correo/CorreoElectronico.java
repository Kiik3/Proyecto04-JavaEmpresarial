
package com.correo;

import com.propiedades.Encriptador;
import com.propiedades.Propiedades;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author Enrique Ochoa
 */
public class CorreoElectronico {
    
    public void mandarCorreo(String correo, int tipo, int numero) throws EmailException{
        Propiedades propiedades = new Propiedades();
        Encriptador enc = new Encriptador(); //Instancia para usar método de desencriptación
        HtmlEmail email = new HtmlEmail();
        
        URL url;
        String imagen = "";
        try {
            //Imágen a mostrar en mensaje de correo según el tipo de correo
            switch(tipo){
                case 1:
                    url = new URL("https://images.vexels.com/media/users/3/130215/isolated/preview/91d40e8a45491022df21ebc451b332ba-s--mbolo-ejecutivo-silueta-by-vexels.png");
                    imagen = email.embed(url, "Admin-logo");
                    break;
                case 2:
                    url = new URL("https://i.pinimg.com/originals/d3/4b/64/d34b64b4103947a9e3d0d5b7c910057e.jpg");
                    imagen = email.embed(url, "User-logo");
                    break;
            }
   
        }catch (MalformedURLException ex) { 
            Logger.getLogger(CorreoElectronico.class.getName()).log(Level.SEVERE, null, ex);
        }

        email.setHostName(enc.desencriptador(propiedades.cargarPropiedades().getProperty("smtp")));
        email.setSmtpPort(Integer.parseInt(enc.desencriptador(propiedades.cargarPropiedades().getProperty("port"))));
        email.setAuthenticator(new DefaultAuthenticator(enc.desencriptador(propiedades.cargarPropiedades().getProperty("us")), 
                enc.desencriptador(propiedades.cargarPropiedades().getProperty("pa"))));
        email.setSSLOnConnect(true); 
        email.setFrom(enc.desencriptador(propiedades.cargarPropiedades().getProperty("us")));
        email.setSubject("Autenticación");
        email.addTo(correo);
        //HTML del correo a enviar
        email.setHtmlMsg("<html><head>"
                + "<style>"
                + "div{"
                + "   margin: auto;"
                + "   padding: 10px;"
                + "   width: 100%;"
                + "   text-align: center;"
                + "   box-sizing: border-box;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "   <div>"
                + "      <h1>Proyecto 04. Curso JAVA Empresarial.</h1>"
                + "      <img src=\"cid:"+imagen+"\" height=\"150px\" width=\"150px\" style=\"margin:10px; border-radius:5px;\">"
                + "      <h2>Hola,</h2>"
                + "      <h4 style=\"text-align:justify;\">Te saluda Enrique Ochoa del curso de Java Empresarial impartido por Bitlab y EchoTech. "
                + "      Este es tu código de autenticación: <b>"+numero+"</b>, ingrésalo para poder continuar.</h4>"
                + "   </div>"
                + "</body>"
                + "</html>");
        email.setTextMsg("Tu cliente de correo electrónico no soporta mensajes HTML");
        email.send();
    }
}

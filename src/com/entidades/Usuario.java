
package com.entidades;

/**
 *
 * @author Enrique Ochoa
 */
public class Usuario {
    
    private int id;
    private String nombre;
    private String contraseña;
    private String correo;
    private int idRol;

    public Usuario(int id, String nombre, String contraseña, String correo, int idRol) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.correo = correo;
        this.idRol = idRol;
    }
    
    public Usuario(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
    
    
}

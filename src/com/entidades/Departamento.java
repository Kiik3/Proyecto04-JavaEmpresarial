
package com.entidades;

/**
 *
 * @author Enrique Ochoa
 */
public class Departamento {
    
    private int id;
    private String nombre;

    public Departamento(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Departamento() {
    }

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
    
}

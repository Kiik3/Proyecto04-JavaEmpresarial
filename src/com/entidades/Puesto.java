
package com.entidades;

/**
 *
 * @author Enrique Ochoa
 */
public class Puesto {
    
    private int id;
    private String nombre;
    private double salarioMinimo;
    private double salarioMaximo;
    private int idDepartamento;

    public Puesto(int id, String nombre, double salarioMinimo, double salarioMaximo, int idDepartameto) {
        this.id = id;
        this.nombre = nombre;
        this.salarioMinimo = salarioMinimo;
        this.salarioMaximo = salarioMaximo;
        this.idDepartamento = idDepartameto;
    }

    public Puesto() {
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

    public double getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setSalarioMinimo(double salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    public double getSalarioMaximo() {
        return salarioMaximo;
    }

    public void setSalarioMaximo(double salarioMaximo) {
        this.salarioMaximo = salarioMaximo;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }
    
}

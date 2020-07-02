
package com.entidades;

import java.sql.Date;

/**
 *
 * @author Enrique Ochoa
 */
public class Empleado {
    
    private int id;
    private String nombre;
    private String apellido;
    private String identificacion;
    private Date fechaNacimiento;
    private Date fechaContrato;
    private int telefono;
    private String correo;
    private double salario;
    private int idPuesto;
    private int idDepartamento;
    private int idEstado;

    public Empleado(int id, String nombre, String apellido, String identificacion, Date fechaNacimiento, Date fechaContrato, int telefono, String correo, double salario, int idPuesto, int idDepartamento, int idEstado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.identificacion = identificacion;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaContrato = fechaContrato;
        this.telefono = telefono;
        this.correo = correo;
        this.salario = salario;
        this.idPuesto = idPuesto;
        this.idDepartamento = idDepartamento;
        this.idEstado = idEstado;
    }

    public Empleado() {
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaContrato() {
        return fechaContrato;
    }

    public void setFechaContrato(Date fechaContrato) {
        this.fechaContrato = fechaContrato;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
    
}

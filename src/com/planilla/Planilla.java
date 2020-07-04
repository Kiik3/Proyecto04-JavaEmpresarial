
package com.planilla;

import java.sql.Date;

/**
 *
 * @author Enrique Ochoa
 */
public class Planilla {
    
    private int id;
    private Date fecha;
    private double totalSalario;
    private double totalDescuento;
    private double totalPago;

    public Planilla(int id, Date fecha, double totalSalario, double totalDescuento, double totalPago) {
        this.id = id;
        this.fecha = fecha;
        this.totalSalario = totalSalario;
        this.totalDescuento = totalDescuento;
        this.totalPago = totalPago;
    }

    public Planilla() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotalSalario() {
        return totalSalario;
    }

    public void setTotalSalario(double totalSalario) {
        this.totalSalario = totalSalario;
    }

    public double getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(double totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = totalPago;
    }
    
}

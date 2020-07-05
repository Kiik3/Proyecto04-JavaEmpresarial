
package com.entidades;

/**
 *
 * @author Enrique Ochoa
 */
public class Renta {
    
    private int id;
    private String tramo;
    private double desde;
    private double hasta;
    private double porcentaje;
    private double sobreExceso;
    private double cuotaFija;

    public Renta(int id, String tramo, double desde, double hasta, double porcentaje, double sobreExceso, double cuotaFija) {
        this.id = id;
        this.tramo = tramo;
        this.desde = desde;
        this.hasta = hasta;
        this.porcentaje = porcentaje;
        this.sobreExceso = sobreExceso;
        this.cuotaFija = cuotaFija;
    }

    public Renta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTramo() {
        return tramo;
    }

    public void setTramo(String tramo) {
        this.tramo = tramo;
    }

    public double getDesde() {
        return desde;
    }

    public void setDesde(double desde) {
        this.desde = desde;
    }

    public double getHasta() {
        return hasta;
    }

    public void setHasta(double hasta) {
        this.hasta = hasta;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getSobreExceso() {
        return sobreExceso;
    }

    public void setSobreExceso(double sobreExceso) {
        this.sobreExceso = sobreExceso;
    }

    public double getCuotaFija() {
        return cuotaFija;
    }

    public void setCuotaFija(double cuotaFija) {
        this.cuotaFija = cuotaFija;
    }
    
}

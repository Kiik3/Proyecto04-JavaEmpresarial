
package com.dao;

import com.entidades.Renta;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Ochoa
 */
public class RentaDAO extends AbstractDAO<Renta>{
    
    @Override
    public String nombreTabla(){
        return "ADM_REN_RENTA";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"REN_ID","REN_TRAMO","REN_DESDE","REN_HASTA","REN_PORCENTAJE","REN_SOBRE_EXCESO","REN_CUOTA_FIJA"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, Renta renta) throws SQLException{

        pre.setString(1, renta.getTramo());
        pre.setDouble(2, renta.getDesde());
        pre.setDouble(3, renta.getHasta());
        pre.setDouble(4, renta.getPorcentaje());
        pre.setDouble(5, renta.getSobreExceso());
        pre.setDouble(6, renta.getCuotaFija());

    }
    
    @Override
    public void mapeoActualizar(PreparedStatement pre, Renta renta) throws SQLException{

        pre.setString(1, renta.getTramo());
        pre.setDouble(2, renta.getDesde());
        pre.setDouble(3, renta.getHasta());
        pre.setDouble(4, renta.getPorcentaje());
        pre.setDouble(5, renta.getSobreExceso());
        pre.setDouble(6, renta.getCuotaFija());
        pre.setDouble(7, renta.getId());

    }
    
    @Override
    public List<Renta> mapeoSeleccionar(ResultSet rs) throws SQLException{
        
        List<Renta> lista = new ArrayList();

        while(rs.next()){ 
            Renta renta = new Renta();
            
            renta.setId(rs.getInt("REN_ID"));
            renta.setTramo(rs.getString("REN_TRAMO"));
            renta.setDesde(rs.getDouble("REN_DESDE"));
            renta.setHasta(rs.getDouble("REN_HASTA"));
            renta.setPorcentaje(rs.getDouble("REN_PORCENTAJE"));
            renta.setSobreExceso(rs.getDouble("REN_SOBRE_EXCESO"));
            renta.setCuotaFija(rs.getDouble("REN_CUOTA_FIJA"));
            
            lista.add(renta);
        }
        
        return lista;

    }
    
    @Override
    public int impresion(List<Renta> lista) throws SQLException{
        
        int i = 0;
        super.out.println("Lista de Tramos:\n");
        super.out.println("Id\tTramo\tDesde\tHasta\tPorcentaje\tSobre exceso\tCuota fija");
        for(Renta l : lista){
            i++;
            super.out.print(l.getId()+ "\t");
            super.out.print(l.getTramo()+ "\t");
            super.out.print(l.getDesde()+ "\t");
            super.out.print(l.getHasta()+ "\t");
            super.out.print(l.getPorcentaje()+ "\t");
            super.out.print(l.getSobreExceso()+ "\t");
            super.out.println(l.getCuotaFija()+ "\t");
        }
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, Renta renta, Connection con) throws Exception{

        boolean flag = false;
        int i;
        int leerInt;
        String leer;
        
        switch(opcion){
            case 1:
                setConexion(con);
                impresion(seleccionar());
                break;
            case 2:
                setConexion(con);
                super.out.print("Ingresa el id: ");
                leer = super.in.readLine();
                leerInt = Integer.parseInt(leer);
                impresion(seleccionar(leerInt));
                break;
            case 3:
                setConexion(con);
                super.out.print("Ingrese el nombre del tramo: ");
                renta.setTramo(super.in.readLine());
                
                super.out.print("Ingrese el valor de inicio del tramo: ");
                renta.setDesde(Double.parseDouble(super.in.readLine()));
                
                super.out.print("Ingrese el valor final del tramo: ");
                renta.setHasta(Double.parseDouble(super.in.readLine()));
                
                super.out.print("Ingrese el porcentaje a aplicar: ");
                renta.setPorcentaje(Double.parseDouble(super.in.readLine()));
                
                super.out.print("Ingrese el sobre exceso: ");
                renta.setSobreExceso(Double.parseDouble(super.in.readLine()));
                
                super.out.print("Ingrese la cuota fija: ");
                renta.setCuotaFija(Double.parseDouble(super.in.readLine()));

                insertar(renta);
                
                super.out.println("\nTramo agregado correctamente!");
                break;
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.print("\nIngrese el id del tramo a actualizar: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    renta.setId(leerInt);
                    super.out.print("Ingrese el nuevo nombre del tramo: ");
                    renta.setTramo(super.in.readLine());
                    super.out.print("Ingrese el valor de inicio del tramo: ");
                    renta.setDesde(Double.parseDouble(super.in.readLine()));

                    super.out.print("Ingrese el valor final del tramo: ");
                    renta.setHasta(Double.parseDouble(super.in.readLine()));

                    super.out.print("Ingrese el porcentaje a aplicar: ");
                    renta.setPorcentaje(Double.parseDouble(super.in.readLine()));

                    super.out.print("Ingrese el sobre exceso: ");
                    renta.setSobreExceso(Double.parseDouble(super.in.readLine()));

                    super.out.print("Ingrese la cuota fija: ");
                    renta.setCuotaFija(Double.parseDouble(super.in.readLine()));

                    actualizar(renta);

                    super.out.println("\nTramo actualizado correctamente!");
                }
                else{
                    super.out.println("\nNo hay tramos de renta para actualizar!");
                    renta.setId(0);
                    actualizar(renta);
                }
                
                break;
            case 5:
                super.out.println("\nNo se permiten eliminar tramos de renta!");
                
                break;
            default:
                super.out.println("Ingresa una opcion valida");
                flag = true;
        }
        return flag;
    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, Renta renta, Connection con, Socket cliente) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


package com.dao;

import com.entidades.DescuentoLey;
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
public class DescuentoLeyDAO extends AbstractDAO<DescuentoLey>{
    //Todos son métodos heredados, la descripción de lo que hacen se encuentra en la clase AbstractDAO
    
    @Override
    public String nombreTabla(){
        return "ADM_DES_DESCUENTO_LEY";
    }
    
    @Override
    public String[] nombreCampos(){
        String[] campos = {"DES_ID","DES_NOMBRE","DES_PORCENTAJE"};
        return campos;
    }

    @Override
    public void mapeoInsertar(PreparedStatement pre, DescuentoLey descuentoLey) throws SQLException{

        pre.setString(1, descuentoLey.getNombre());
        pre.setDouble(2, descuentoLey.getPorcentaje());

    }
    
    @Override
    public void mapeoActualizar(PreparedStatement pre, DescuentoLey descuentoLey) throws SQLException{

        pre.setString(1, descuentoLey.getNombre());
        pre.setDouble(2, descuentoLey.getPorcentaje());
        pre.setInt(3, descuentoLey.getId());

    }
    
    @Override
    public List<DescuentoLey> mapeoSeleccionar(ResultSet rs) throws SQLException{
        
        List<DescuentoLey> lista = new ArrayList();
        
        //Se recorre el rs para mapear los registros obtenidos
        while(rs.next()){ 
            DescuentoLey descuentoLey = new DescuentoLey();
            
            descuentoLey.setId(rs.getInt("DES_ID"));
            descuentoLey.setNombre(rs.getString("DES_NOMBRE"));
            descuentoLey.setPorcentaje(rs.getDouble("DES_PORCENTAJE"));
            
            lista.add(descuentoLey);
        }
        
        return lista;

    }
    
    @Override
    public int impresion(List<DescuentoLey> lista) throws SQLException{
        
        int i = 0;
        super.out.println("\rLista de Descuentos de ley:\n");
        super.out.println("\rId\tDescuento\tPorcentaje");
        for(DescuentoLey l : lista){
            i++;
            super.out.print("\r" + l.getId()+ "\t");
            super.out.print(l.getNombre()+ "\t");
            super.out.println(l.getPorcentaje()+ "\t");
        }
        //valida si el query está vacío
        if(i == 0){
            super.out.println("No se encontraron resultados");
        }
        return i;
    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, DescuentoLey descuentoLey, Connection con) throws Exception{

        boolean flag = false;
        int i;
        int leerInt;
        String leer;
        double porcentaje;
        
        switch(opcion){
            //Listar todos los registros
            case 1:
                setConexion(con);
                impresion(seleccionar());
                break;
            //Listar todos los registros por id
            case 2:
                setConexion(con);
                super.out.print("Ingresa el id: ");
                leer = super.in.readLine();
                leerInt = Integer.parseInt(leer);
                impresion(seleccionar(leerInt));
                break;
            case 3:
            //Ingresar un nuevo registro
                setConexion(con);
                super.out.print("Ingrese el nombre del descuento de ley: ");
                descuentoLey.setNombre(super.in.readLine());
                
                do {                    
                    super.out.print("Ingrese el porcentaje: ");
                    porcentaje = Double.parseDouble(super.in.readLine());
                } while (porcentaje <= 0);
                
                descuentoLey.setPorcentaje(porcentaje);

                insertar(descuentoLey);
                
                super.out.println("\nDescuento de ley agregado correctamente!");
                break;
            //Actualizar un registro
            case 4:
                setConexion(con);
                i = impresion(seleccionar());
                
                if(i != 0){
                    super.out.print("\nIngrese el id del descuento a actualizar: ");
                    leer = super.in.readLine();
                    leerInt = Integer.parseInt(leer);
                    descuentoLey.setId(leerInt);
                    super.out.print("Ingrese el nuevo nombre del descuento: ");
                    descuentoLey.setNombre(super.in.readLine());
                    do {                    
                        super.out.print("Ingrese el porcentaje: ");
                        porcentaje = Double.parseDouble(super.in.readLine());
                    } while (porcentaje <= 0);

                    descuentoLey.setPorcentaje(porcentaje);

                    actualizar(descuentoLey);

                    super.out.println("\nDescuento de ley actualizado correctamente!");
                }
                else{
                    super.out.println("\nNo hay descuentos para actualizar!");
                    descuentoLey.setId(0);
                    actualizar(descuentoLey);
                }
                
                break;
            case 5:
                //No se permite eliminar estados porque el 1 y 2 hace referencia a isss y afp respectivamente
                super.out.println("\nNo se permiten eliminar descuentos de ley!");
                
                break;
            default:
                super.out.println("Ingresa una opcion valida");
                flag = true;
        }
        return flag;
    }
    
    @Override
    public boolean ingresoDatosGestion(int opcion, DescuentoLey descuentoLey, Connection con, Socket cliente) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

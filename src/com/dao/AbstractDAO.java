
package com.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Enrique Ochoa
 */
public abstract class AbstractDAO<T> {
    //Querys dinámicos
    private String insertQuery = "insert into TABLA (CAMPOS) values (VALORES);";
    private String updateQuery = "update TABLA set CAMPOS where CAMPO_ID = ?;";
    private String deleteQuery = "delete from TABLA where CAMPO_ID = ?";
    private String selectQuery = "select * from TABLA";
    private String selectIdQuery = "select * from TABLA where CAMPO_ID = ?";
    private Connection con;
    Socket cliente;
    protected PrintStream out; 
    protected BufferedReader in; 
    
    //Ingresar conexión
    public void setConexion(Connection con){
        this.con = con;
    }
    
    public Connection getConexion(){
        return con;
    }
    
    //Ingresar cliente conectado y asignar salida y entrada
    public void setCliente(Socket cliente) throws IOException{
        out = new PrintStream(cliente.getOutputStream());
        in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
    }
    
    //Método que inserta un nuevo registro en la tabla indicada
    public void insertar(T entidad) throws SQLException, Exception{
        
        String arreglo = "";
        for(int i=0; i<nombreCampos().length-1; i++){
            arreglo += "?,";
        }
        arreglo = arreglo.substring(0, arreglo.length()-1);
        
        String campos = Arrays.toString(nombreCampos()).replace("[", "").replace("]", "");
        byte indice = (byte) campos.indexOf(",");
        String campoId = campos.substring(0, indice);
        campos = campos.substring(indice+2);
        
        insertQuery = insertQuery.replace("TABLA", nombreTabla());
        insertQuery = insertQuery.replace("CAMPOS", campos);
        insertQuery = insertQuery.replace("VALORES", arreglo);
        
        PreparedStatement pre = con.prepareStatement(insertQuery);
        mapeoInsertar(pre, entidad); //Se llama al mapeo de los campos para sustituirse en el query
        
        pre.executeUpdate();
        pre.close();
    }
    
    //Método que actualiza un nuevo registro en la tabla indicada
    public void actualizar(T entidad) throws SQLException, Exception{
        
        String campos = Arrays.toString(nombreCampos()).replace("[", "").replace("]", "");
        byte indice = (byte) campos.indexOf(",");
        String campoId = campos.substring(0, indice);
        campos = campos.substring(indice+2);
        campos = campos.replace(",", "=?,");
        campos = campos + "=?";
  
        updateQuery = updateQuery.replace("TABLA", nombreTabla());
        updateQuery = updateQuery.replace("CAMPO_ID", campoId);
        updateQuery = updateQuery.replace("CAMPOS", campos);
        
        PreparedStatement pre = con.prepareStatement(updateQuery);
        mapeoActualizar(pre, entidad); //Se llama al mapeo de los campos para sustituirse en el query
        
        pre.executeUpdate();
        pre.close();
    }
    
    //Método que eliminar un nuevo registro de la tabla indicada
    public void eliminar(int id) throws SQLException, Exception{
        String campos = Arrays.toString(nombreCampos()).replace("[", "").replace("]", "");
        byte indice = (byte) campos.indexOf(",");
        String campoId = campos.substring(0, indice);
        
        deleteQuery = deleteQuery.replace("TABLA", nombreTabla());
        deleteQuery = deleteQuery.replace("CAMPO_ID", campoId);
        
        PreparedStatement pre = con.prepareStatement(deleteQuery);
        pre.setInt(1, id);
        pre.executeUpdate();
        pre.close();
    }
    
    //Método que selecciona todos los registros de la tabla indicada
    public List<T> seleccionar() throws SQLException, Exception{
        List<T> lista = new ArrayList();
        
        selectQuery = selectQuery.replace("TABLA", nombreTabla());
        
        PreparedStatement pre = con.prepareStatement(selectQuery);
        ResultSet rs = pre.executeQuery();
        lista = mapeoSeleccionar(rs); //Se llama al mapeo de los campos en una lista segun los registros obtenidos
        
        rs.close();
        pre.close();
        
        return lista;
    }
    
    //Método que selecciona los registros de la tabla indicada y segun el id
    public List<T> seleccionar(int id) throws SQLException, Exception{
        List<T> lista = new ArrayList();
        
        String campos = Arrays.toString(nombreCampos()).replace("[", "").replace("]", "");
        byte indice = (byte) campos.indexOf(",");
        String campoId = campos.substring(0, indice);
  
        selectIdQuery = selectIdQuery.replace("TABLA", nombreTabla());
        selectIdQuery = selectIdQuery.replace("CAMPO_ID", campoId);
 
        PreparedStatement pre = con.prepareStatement(selectIdQuery);
        pre.setInt(1, id);
        ResultSet rs = pre.executeQuery();
        
        lista = mapeoSeleccionar(rs); //Se llama al mapeo de los campos en una lista segun los registros obtenidos
        
        rs.close();
        pre.close();
        
        return lista;
    }
    //Métodos abstractos
    
    protected abstract String nombreTabla(); //Para obtener nombre de la tabla
    
    protected abstract String[] nombreCampos(); //Para obtener nombre de campos
    
    protected abstract void mapeoInsertar(PreparedStatement pre, T entidad) throws SQLException; //Mapear los valores a ingresar
    
    protected abstract void mapeoActualizar(PreparedStatement pre, T entidad) throws SQLException; //Mapear los valores a actualizar
    
    protected abstract List<T> mapeoSeleccionar(ResultSet rs) throws SQLException; //Mapear los registros obtenidos en una lista entidad
       
    protected abstract int impresion(List<T> lista) throws SQLException; //Imprimir los registros obtenidos
    
    //Permite al usuario gestionar una entidad: listar, actualizar, ingresar, eliminar
    protected abstract boolean ingresoDatosGestion(int opcion, T entidad, Connection con) throws Exception;
    
    protected abstract boolean ingresoDatosGestion(int opcion, T entidad, Connection con, Socket cliente) throws Exception;

}

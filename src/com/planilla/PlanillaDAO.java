
package com.planilla;

import com.entidades.DescuentoLey;
import com.entidades.Empleado;
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
import java.util.List;

/**
 *
 * @author Enrique Ochoa
 */
public class PlanillaDAO {
    
    PrintStream out;
    BufferedReader in;
    
    public void calcularPlanilla(Connection con, Socket cliente) throws SQLException, IOException{
        out = new PrintStream(cliente.getOutputStream());
        in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            
        List<Empleado> lista = new ArrayList();
        List<HistorialPago> listaHistorial = new ArrayList();
        List<DescuentoLey> listaDescuento = new ArrayList();
        double totalIsss = 0, totalAfp = 0, totalRenta = 0;
        double totalSalario= 0, totalDescuento = 0, totalPago = 0;
        double isss, afp, renta = 0, pago;
        double salarioRenta;

        lista = empleadosActivos(con);
        
        String query = "select * from ADM_DES_DESCUENTO_LEY";
        
        PreparedStatement pre = con.prepareStatement(query);
        ResultSet rs = pre.executeQuery();
        
        while(rs.next()){
            DescuentoLey descuentoLey = new DescuentoLey(rs.getInt("DES_ID"), rs.getString("DES_NOMBRE"), rs.getDouble("DES_PORCENTAJE"));
            listaDescuento.add(descuentoLey);
        }
        
        rs.close();
        pre.close();
        
        out.println("Id\tNombre\tApellido\tSalario\tISSS\tAFP\tRenta\tLiquido a pagar");
        if(!lista.isEmpty()){
            for(Empleado l : lista){
                out.print(l.getId() + "\t");
                out.print(l.getNombre() + "\t");
                out.print(l.getApellido()+ "\t\t");
                out.print(l.getSalario()+ "\t");

                isss = l.getSalario()*(listaDescuento.get(0).getPorcentaje()/100);
                afp = l.getSalario()*(listaDescuento.get(1).getPorcentaje()/100);
                salarioRenta = l.getSalario() - (isss + afp);

                if(salarioRenta >= 0.01 && salarioRenta <= 472){
                    renta = 0;
                }
                else if(salarioRenta >= 472.01 && salarioRenta <= 895.24){
                    renta = ((salarioRenta-472)*0.1)+17.67;
                }
                else if(salarioRenta >= 895.25 && salarioRenta <= 2038.10){
                    renta = ((salarioRenta-895.24)*0.2)+60;
                }
                else if(salarioRenta >= 2038.11){
                    renta = ((salarioRenta-2038.10)*0.3)+288.57;
                }
                pago = l.getSalario() - (isss + afp + renta);

                out.print(String.format("%.2f", isss) + "\t");
                out.print(String.format("%.2f", afp) + "\t");
                out.print(String.format("%.2f", renta) + "\t");
                out.println(String.format("%.2f", pago) + "\t");
                totalSalario = totalSalario + l.getSalario();
                totalIsss = totalIsss + isss;
                totalAfp = totalAfp + afp;
                totalRenta = totalRenta + renta;

                HistorialPago historialPago = new HistorialPago(l.getId(), l.getNombre(), l.getApellido(), l.getSalario(), isss, afp, renta, pago);
                listaHistorial.add(historialPago);
            }
            totalDescuento = totalIsss + totalAfp + totalRenta;
            totalPago = totalSalario - totalDescuento;

            out.println("TOTAL:\t\t\t\t" + String.format("%.2f", totalSalario) + "\t" + String.format("%.2f", totalIsss) + "\t" + 
                    String.format("%.2f", totalAfp) + "\t" + String.format("%.2f", totalRenta) + "\t" + String.format("%.2f", totalPago));

            Planilla planilla = new Planilla();
            planilla.setTotalSalario(totalSalario);
            planilla.setTotalDescuento(totalDescuento);
            planilla.setTotalPago(totalPago);

            pagarPlanilla(con, planilla, lista, listaHistorial);
        }
        else{
            out.println("No se encontraron empleados activos");
        }
        
    }
    
    public List<Empleado> empleadosActivos(Connection con) throws SQLException{
        List<Empleado> lista = new ArrayList();
        
        String query = "select * from ADM_EMP_EMPLEADO where EST_ID = 1";
        
        PreparedStatement pre = con.prepareStatement(query);
        ResultSet rs = pre.executeQuery();
        
        while(rs.next()){
            Empleado empleado = new Empleado();
            
            empleado.setId(rs.getInt("EMP_ID"));
            empleado.setNombre(rs.getString("EMP_NOMBRE"));
            empleado.setApellido(rs.getString("EMP_APELLIDO"));
            empleado.setIdentificacion(rs.getString("EMP_IDENTIFICACION"));
            empleado.setFechaNacimiento(rs.getDate("EMP_FECHA_NACIMIENTO"));
            empleado.setFechaContrato(rs.getDate("EMP_FECHA_CONTRATACION"));
            empleado.setTelefono(rs.getInt("EMP_TELEFONO"));
            empleado.setCorreo(rs.getString("EMP_CORREO"));
            empleado.setSalario(rs.getDouble("EMP_SALARIO"));
 
            empleado.setIdDepartamento(rs.getInt("DEP_ID"));
            empleado.setIdPuesto(rs.getInt("PUE_ID"));
            empleado.setIdEstado(rs.getInt("EST_ID"));
            empleado.setIdJefe(rs.getInt("EMP_ID_JEFE"));
            
            lista.add(empleado);
        }
        rs.close();
        pre.close();
        
        return lista;
    }
    
    public void pagarPlanilla(Connection con, Planilla planilla, List<Empleado> lista, List<HistorialPago> listaHistorial) throws SQLException{
        
        String query = "insert into ADM_PLA_PLANILLA (PLA_FECHA, PLA_TOTAL_SALARIO, PLA_TOTAL_DESCUENTO, PLA_TOTAL_PAGO)"
                        + "values (now(), ?, ?, ?)";
        
        PreparedStatement pre;
        pre = con.prepareStatement(query);
        pre.setDouble(1, planilla.getTotalSalario());
        pre.setDouble(2, planilla.getTotalDescuento());
        pre.setDouble(3, planilla.getTotalPago());
        pre.executeUpdate();
        
        pre.close();
        
        ingresarHistorial(con, listaHistorial);
    }
    
    public void ingresarHistorial(Connection con, List<HistorialPago> listaHistorial) throws SQLException{
        List<HistorialPago> lista = new ArrayList();
        
        String query = "SELECT PLA_ID FROM ADM_PLA_PLANILLA ORDER BY PLA_ID DESC LIMIT 1";
        
        PreparedStatement pre;
        pre = con.prepareStatement(query);
        ResultSet rs = pre.executeQuery();
        rs.next();
        int idPlanilla = rs.getInt("PLA_ID");
        
        rs.close();
        pre.close();
        
        query = "insert into ADM_HIS_HISTORIAL_PAGO (HIS_ID_EMPLEADO, HIS_NOMBRE_EMPLEADO, HIS_APELLIDO_EMPLEADO, "
                + "HIS_SALARIO, HIS_ISSS, HIS_AFP, HIS_RENTA, HIS_PAGO, PLA_ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        lista = listaHistorial;
        for(HistorialPago l : lista){
            pre = con.prepareStatement(query);
            pre.setInt(1, l.getIdEmpleado());
            pre.setString(2, l.getNombreEmpleado());
            pre.setString(3, l.getApellidoEmpleado());
            pre.setDouble(4, l.getSalario());
            pre.setDouble(5, l.getIsss());
            pre.setDouble(6, l.getAfp());
            pre.setDouble(7, l.getRenta());
            pre.setDouble(8, l.getPago());
            pre.setInt(9, idPlanilla);
            pre.executeUpdate();
        }
        
        pre.close();
    }
    
    public void verHistorial(Connection con, Socket cliente) throws SQLException, IOException{
        out = new PrintStream(cliente.getOutputStream());
        in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        
        List<Integer> lista = new ArrayList();
        int i = 0;
        int idPlanilla = 0;
        boolean flag = false;
        PreparedStatement pre;
        ResultSet rs;
        
        String query = "select * from ADM_PLA_PLANILLA";
        
        pre = con.prepareStatement(query);
        rs = pre.executeQuery();
        
        out.println("A continuacion se muestra el historial de planillas pagadas\n");
        out.println("Id\tFecha de pago\tTotal salario nominal\tTotal descuentos\tTOTAL PAGADO");
        
        while(rs.next()){
            lista.add(rs.getInt("PLA_ID"));
            out.print(rs.getInt("PLA_ID") + "\t");
            out.print(rs.getDate("PLA_FECHA") + "\t\t");
            out.print(String.format("%.2f", rs.getDouble("PLA_TOTAL_SALARIO")) + "\t\t");
            out.print(String.format("%.2f", rs.getDouble("PLA_TOTAL_DESCUENTO")) + "\t\t\t");
            out.println(String.format("%.2f", rs.getDouble("PLA_TOTAL_PAGO")) + "\t\t");
            i++;
        }
        
        if(i != 0){ 
            do {                
                out.print("\nIngresa el id de la planilla para ver mas detalles: ");
                try {
                    idPlanilla = Integer.parseInt(in.readLine());
                    for(Integer l : lista){
                        if(l == idPlanilla){
                            flag = false;
                            break;
                        }
                        else{
                            flag = true;
                        }
                    }
                } catch (Exception e) {
                    flag = true;
                }
                
            } while (flag);  
        }
        else{
            out.print("\nNo se encontro ninguna planilla en el historial: \n");
        }
        
        rs.close();
        pre.close();
        
        query = "select * from ADM_HIS_HISTORIAL_PAGO where PLA_ID = ?";
        
        pre = con.prepareStatement(query);
        pre.setInt(1, idPlanilla);
        rs = pre.executeQuery();
        
        out.println("Detalle de planilla seleccionada\n");
        out.println("Id\tNombre\tApellido\tSalario\tISSS\tAFP\tRenta\tLiquido a pagar");
        
            while(rs.next()){
                lista.add(rs.getInt("PLA_ID"));
                out.print(rs.getInt("HIS_ID_EMPLEADO") + "\t");
                out.print(rs.getString("HIS_NOMBRE_EMPLEADO") + "\t");
                out.print(rs.getString("HIS_APELLIDO_EMPLEADO") + "\t\t");
                out.print(rs.getDouble("HIS_SALARIO") + "\t");
                out.print(String.format("%.2f", rs.getDouble("HIS_ISSS")) + "\t");
                out.print(String.format("%.2f", rs.getDouble("HIS_AFP")) + "\t");
                out.print(String.format("%.2f", rs.getDouble("HIS_RENTA")) + "\t");
                out.println(String.format("%.2f", rs.getDouble("HIS_PAGO")) + "\t");

            }
        rs.close();
        pre.close();
        
        query = "select sum(HIS_SALARIO), sum(HIS_ISSS), sum(HIS_AFP), sum(HIS_RENTA), sum(HIS_PAGO)"
                + " from ADM_HIS_HISTORIAL_PAGO where PLA_ID = ?";
        
        pre = con.prepareStatement(query);
        pre.setInt(1, idPlanilla);
        rs = pre.executeQuery();

        rs.next();
        out.println("TOTAL:\t\t\t\t" + String.format("%.2f", rs.getDouble("SUM(HIS_SALARIO)")) + "\t" + 
                String.format("%.2f", rs.getDouble("SUM(HIS_ISSS)")) + "\t" + 
                String.format("%.2f", rs.getDouble("SUM(HIS_AFP)")) + "\t" + 
                String.format("%.2f", rs.getDouble("SUM(HIS_RENTA)")) + "\t" + 
                String.format("%.2f", rs.getDouble("SUM(HIS_PAGO)")));
        
        rs.close();
        pre.close();
    }
}

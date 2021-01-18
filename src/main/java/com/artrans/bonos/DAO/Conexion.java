/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.DAO;

import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.OrdenesDeTrabajo;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.repositorio.OrdenesDeTrabajoRepositorio;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component("conexion")
public class Conexion {
    
    @Autowired
    private OrdenesDeTrabajoRepositorio ordenesDeTrabajoRepositorio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;



//        @Scheduled(cron = "0 0 7 * * *")
        public void conectar(){
        Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             String urls = "jdbc:sqlserver://200.80.241.187\\GLOBALIS:56141;databaseName=Delfos_PROD;user=java;password=Perfil27;";
             con = DriverManager.getConnection(urls);
             System.out.println("Nos conectamos");
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select TOP 1000 CAST([TareaCodi] as nchar(10))\n"
                    + "      ,CAST([Area] as nchar(100))\n"
                    + "      ,CAST([Nombre Tarea] as nchar(100))\n"
                    + "      ,CAST([Proyecto] as nchar(100))\n"
                    + "      ,CAST([Empleados] as nchar(100))\n"
                    + "      ,CAST([Cliente] as nchar(100))\n"
                    + "      ,CAST([ArticuloVersion] as nchar(100))\n"
                    + "      ,CAST([NroSerie] as nchar(100))  FROM [Delfos_PROD].[dbo].[ViewCuboPlaneamientoJava] WHERE [Empleados] IS NOT NULL AND [FechaFinalizacionReal] IS NULL");
            
            System.out.println("a iterar");
            while (rs.next()) {
                  System.out.println(rs.getNString(1));
                    Optional<OrdenesDeTrabajo> respuesta = ordenesDeTrabajoRepositorio.findById(rs.getNString(1));
                    System.out.println("pase la respuesta");
                    if (respuesta.isPresent()) {
                        OrdenesDeTrabajo ot = respuesta.get();
                        ot.setArea(rs.getNString(2));
                        ot.setNombreTarea(rs.getNString(3));
                        ot.setProyecto(rs.getNString(4));
                        System.out.println("hasta aca ok");
                        if (rs.getNString(5) != null) {
                            System.out.println("encontre empelado");
                            ot.setEmpleados(empleados(rs.getNString(5)));
                            System.out.println("lo guardo");
                        }
                        System.out.println("no encotnre empleado sigo");
                        ot.setCliente(rs.getNString(6));
                        ot.setArticuloVerson(rs.getNString(7));
                        ot.setnSerie(rs.getNString(8));
                        ordenesDeTrabajoRepositorio.save(ot);
                    }else{
                        
                    OrdenesDeTrabajo ot = new OrdenesDeTrabajo();
                    ot.setTareaCodi(rs.getNString(1));
                    ot.setArea(rs.getNString(2));
                    ot.setNombreTarea(rs.getNString(3));
                    ot.setProyecto(rs.getNString(4));
                    if ( rs.getNString(5) != null) {
                        ot.setEmpleados(empleados(rs.getNString(5)));
                    }
                    ot.setCliente(rs.getNString(6));
                    ot.setArticuloVerson(rs.getNString(7));
                    ot.setnSerie(rs.getNString(8));
                    ordenesDeTrabajoRepositorio.save(ot);

                }
            }
            } catch (Exception e) {
                System.out.println(e);
            }
    }
    private List<Empleado> empleados(String documentos){
    
    List<Empleado> lista = new ArrayList<>();
    
    String[] doc =  documentos.split(", ");
    
        for (String string : doc) {
            
            if (string != null) {
                Empleado respuesta = empleadoRepositorio.buscarDni(string);
                if (respuesta != null) {
                    lista.add(respuesta);
                }
            }
            
        }
    return lista;
    }
    
    
    /*
               -Conexion Base de Datos Proyectos
                -Comentario: Creo nueva base de datos con solo los proyectos y sus respectivos paramentros, para que pueda se consultada por el
                    de manera rapida a la hora de generar un desvio, esta base de datos esta sincronizada con la de delfos se actulizara todos los dias a las 7 de la mañana,
   
    */
    
      //@Scheduled(cron = "0 58 15 * * *")
    public ResultSet SincrinizarBDatosProyectos(){
        
       Connection con = null;
        try {
           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           String urls = "jdbc:sqlserver://200.80.241.187\\GLOBALIS:56141;databaseName=Delfos_PROD;user=java;password=Perfil27;";
           con = DriverManager.getConnection(urls);
           System.out.println("Nos conectamos");
            Statement stm = con.createStatement();
            
            //Consulta
            ResultSet rs = stm.executeQuery("select TOP 60000 CAST([Nro Proyecto] as nchar(100))\n"
                    + "      ,CAST([ArticuloVersion] as nchar(1000))\n"
                    + "      ,CAST([Cliente] as nchar(100))\n"
                    + "      ,CAST([Area] as nchar(100))\n"
                    + "      ,CAST([FechaInicioProgramado] as DATE)\n"
                    + "      ,CAST([CodigoFabricacion] as nchar(100))\n"
                    + "      ,CAST([Nombre Tarea] as nchar(100))\n"
                    + "      ,CAST([Cantidad] as nchar(10))\n"
                    + "      ,CAST([NroSerie] as nchar(100))\n"
                    + "      ,CAST([TareaCodi] as nchar(10))  FROM [Delfos_PROD].[dbo].[ViewCuboPlaneamientoJava]\n"
                    + "WHERE [Area] = 'Deposito 1'\n"
                    + " OR [Area] = 'Deposito 2'\n"
 //                   + " AND YEAR([FechaInicioProgramado]) > 2019\n"
                    + " AND [CodigoFabricacion] LIKE '07-%'\n"
                    + " OR [CodigoFabricacion] LIKE '32-%'\n"
                    + " AND [Nombre Tarea] LIKE '%Transformador%'\n"
                    + " OR [Nombre Tarea] LIKE '%transformador%'\n");
            
            System.out.println("Conexion Inciada");
            return rs; 
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
     }
 
}
        


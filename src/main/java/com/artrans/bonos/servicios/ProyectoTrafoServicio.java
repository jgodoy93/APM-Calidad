/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.servicios;

import com.artrans.bonos.DAO.Conexion;
import com.artrans.bonos.entidades.ProyectoTrafo;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.RepositorioProyectoTrafo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author jgodoy
 */
@Service
public class ProyectoTrafoServicio {
    
    @Autowired
    private RepositorioProyectoTrafo repoTrafo;
    @Autowired
    private Conexion conexion;
    
    @Scheduled(cron = "12 00 04 * * *")
    @Transactional
    public ProyectoTrafo Guardar() throws SQLException{
    
         try{    
            ResultSet rs = conexion.SincrinizarBDatosProyectos();
            while(rs.next()){       
                String tareaCodi = rs.getNString(10);
                String notaVenta = rs.getNString(1);
                String nroSerie = rs.getNString(9);
                String descripcion = rs.getNString(2);
                String cliente = rs.getNString(3);
                String codigo = rs.getNString(6);
                String Tarea = rs.getNString(7);
                String cantidad = rs.getNString(8); 
                
                String nvAcortada = notaVenta.substring(0, 5);
                
                if((Tarea.contains("transformador") || Tarea.contains("Transformador"))){   
                         if (cantidad.contains("1")) {
                             if((codigo.substring(0,2)).equals("32")||(codigo.substring(0,2)).equals("07")){
                             
                                 Optional<ProyectoTrafo> respuesta = repoTrafo.findById(tareaCodi);
                                 
                                 if(!(respuesta.isPresent())){    
                                                
                                      ProyectoTrafo proyecto = new ProyectoTrafo();                                   
                                      proyecto.setTareaCodi(tareaCodi);
                                      proyecto.setCliente(cliente);
                                      proyecto.setDescripcion(descripcion);
                                      proyecto.setTareaItem(Tarea);
                                      proyecto.setNroSerie(nroSerie);
                                      proyecto.setSeleccion(Boolean.FALSE);
                                      proyecto.setNotadeVenta(nvAcortada);
                                      proyecto.setCodigo(codigo);
                                      System.out.println("Guardo Proyecto...");
                                      repoTrafo.save(proyecto);       
                                                         
                                    }
                               }
                          }
               }
           }
                                        
         }catch(Exception e){
            System.out.println(e);
        }
        return null;
       

    }
    
    public List<ProyectoTrafo> buscarPorNotadeVenta(String notaVenta) throws ErrorServicio{
    
        List<ProyectoTrafo> listadoProyecto = repoTrafo.buscarPorNotaVenta(notaVenta);
        
        if (listadoProyecto.isEmpty()) {
            throw new ErrorServicio("No se encuentra la nota de venta ingresada.");
            
        }
        
        return listadoProyecto;
    } 
}

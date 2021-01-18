/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.servicios;

import com.artrans.bonos.DAO.AreaDAO;
import com.artrans.bonos.entidades.Area;
import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.AreaRepositorio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AreaServicio {
    
    @Autowired
    private AreaRepositorio areaRepositorio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    
    @Autowired
    private AreaDAO areaDAO;
    
    public List<Area> verAreasActivas(){

     return areaRepositorio.areaAlta(false);
}
    @Scheduled(cron = "00 00 07 * * *")
    public void traerbd(){
    areaDAO.conectar();
    }
    //agregamos un empleado al area 
    public void agregarEmpleado(String areaid, Empleado empleado){
    //busco el area
        Optional<Area> respuesta = areaRepositorio.findById(areaid);
        //si tengo respuesta 
        if (respuesta.isPresent()) {
            Area obj = respuesta.get();
            List<Empleado> lista = obj.getResponsables();
            int a = 0;
            for (Empleado empleado1 : lista) {
               a = (empleado1.getId() == empleado.getId()) ? a+1:a;
            }
            if (a==0) {
                System.out.println("no esta asique lo agrego");
                lista.add(empleado);
                obj.setResponsables(lista);
                areaRepositorio.save(obj);
            }else{
                System.out.println("esta asique no lo agrego");
            }

           
        }
        
        
    }
    public void actualizarJefe(String areaid, Empleado jefe){
         
        Optional<Area> respuesta = areaRepositorio.findById(areaid);
        if (respuesta.isPresent()) {
            Area obj = respuesta.get();
            obj.setEncargado(jefe);
            areaRepositorio.save(obj);
        }
    }
    public void eliminarResponsable(String areaid, String empleadoid){
        //busco el area
        Optional<Area> respuesta = areaRepositorio.findById(areaid);
        //si tengo respuesta 
        if (respuesta.isPresent()) {
            System.out.println("estoy viendo si elimino la lista");
            Area obj = respuesta.get();
            List<Empleado> lista = obj.getResponsables();
            Optional<Empleado> empleadorespuesta = empleadoRepositorio.findById(empleadoid);
            System.out.println("aca supeustamnte lo elimine");
            lista.remove(lista.indexOf(empleadorespuesta.get()));
            obj.setResponsables(lista);
            areaRepositorio.save(obj);
        }
      
    
    }
    
    public List<Empleado> listarPorArea(String idArea) throws ErrorServicio{
    
        try{
            Optional<Area> respuesta = areaRepositorio.findById(idArea);
            
            if(respuesta.isPresent()){
                Area area = respuesta.get();
                
                List<Empleado> empleadosArea = areaRepositorio.buscarEmpleadosArea(idArea);
                
                if(empleadosArea.isEmpty()){
                    throw new ErrorServicio("No hay empleadoas asignados a esta Área.");
                }else{
                    return empleadosArea;
                }     
            }else{
                throw new ErrorServicio("Hubo un Error al buscar el Area asociada al desvío.");
            }
        }catch(Exception e){
                throw new ErrorServicio(e.getMessage());
        }
    
    }
    
}

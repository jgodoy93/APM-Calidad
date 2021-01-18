/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;

import com.artrans.bonos.entidades.Area;
import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.repositorio.AreaRepositorio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.servicios.AreaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
public class AreaControlador {
    
    @Autowired
    private AreaServicio areaServicio;
    @Autowired
    private AreaRepositorio areaRepositorio; 
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    
     @GetMapping("/area{id}")
    public String areaEdit(ModelMap modelo, @PathVariable String id){
        id = (id.length() == 1) ? id+" ":id;
        modelo.put("area", areaRepositorio.findById(id).get());
        modelo.put("responsables", areaRepositorio.buscarEmpleadosArea(id));
        modelo.put("jefe", areaRepositorio.buscarJefeArea(id));
        modelo.put("empleados", empleadoRepositorio.findAll());
            for (Empleado empleado : areaRepositorio.buscarEmpleadosArea(id)) {
             System.out.println("la lista de empelados son "+empleado.getApellido());
        }
    return "area/editArea.html";
    }
    
   @GetMapping("/area")
    public String area(ModelMap modelo){
        areaServicio.traerbd();
        List<Area> areas = areaServicio.verAreasActivas();
        modelo.put("areas", areas);
    return "area/adminarea.html";
    }
    @PostMapping("/agregaAreaEmpleado")
    public String agregaAreaEmpleado(ModelMap modelo, @RequestParam String idArea,@RequestParam String idEmpleado){
        //busco el empleado y lo aagrego al area luego buelvo a buscar el area con el empleado en la lista
        idArea = (idArea.length() == 1) ? idArea+" ":idArea;
        areaServicio.agregarEmpleado(idArea, empleadoRepositorio.findById(idEmpleado).get());
    
        modelo.put("area", areaRepositorio.findById(idArea).get());
        modelo.put("responsables", areaRepositorio.buscarEmpleadosArea(idArea));
        modelo.put("jefe", areaRepositorio.buscarJefeArea(idArea));
        modelo.put("empleados", empleadoRepositorio.findAll());
        
    return "area/editArea.html";  
    }
    @PostMapping("/agregaAreaJefe")
    public String agregaAreaJefe(ModelMap modelo, @RequestParam String idArea,@RequestParam String idJefe){
        //busco el jefe y lo aagrego al area luego buelvo a buscar el area con el empleado en la lista
        idArea = (idArea.length() == 1) ? idArea+" ":idArea;
        areaServicio.actualizarJefe(idArea, empleadoRepositorio.findById(idJefe).get());
        modelo.put("area", areaRepositorio.findById(idArea).get());
        modelo.put("responsables", areaRepositorio.buscarEmpleadosArea(idArea));
        modelo.put("jefe", areaRepositorio.buscarJefeArea(idArea));
        modelo.put("empleados", empleadoRepositorio.findAll());
        
    return "area/editArea.html"; 
    } 
    
    @GetMapping("/eliminaEmpleado{idE}={idA}")
    public String eliminaEmpleado(ModelMap modelo, @PathVariable String idE,@PathVariable String idA){
             System.out.println("entre aca y recibi los path"+idE+" y "+idA);
             idA = (idA.length() == 1) ? idA+" ":idA; // hace flata agreagar el espacio vacio ya que de la base de dato lo trae asi para las que tienen un digito
        areaServicio.eliminarResponsable(idA, idE);
        modelo.put("area", areaRepositorio.findById(idA).get());
        modelo.put("responsables", areaRepositorio.buscarEmpleadosArea(idA));
        modelo.put("jefe", areaRepositorio.buscarJefeArea(idA));
        modelo.put("empleados", empleadoRepositorio.findAll());
        
    return "area/editArea.html"; 
    }
}


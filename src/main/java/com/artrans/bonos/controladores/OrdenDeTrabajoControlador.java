
package com.artrans.bonos.controladores;

import com.artrans.bonos.DAO.Conexion;
import com.artrans.bonos.entidades.OrdenesDeTrabajo;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.servicios.OrdenDeTrabajoServicio;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class OrdenDeTrabajoControlador {
    
    @Autowired
    private OrdenDeTrabajoServicio ordenDeTrabajoServicio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    @Autowired
    private Conexion conexion;
  
  @GetMapping("/ordenTrabajo")  
  public String ordenesDeoT(ModelMap modelo){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
         
        //conexion.conectar();
        
  modelo.put("ots", ordenDeTrabajoServicio.buscarOT(empleadoRepositorio.buscarEmail(mail).getDni()));
      
  return "verOT.html";    
  }  
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;

import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.servicios.EmpleadoServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author chiri
 */
@Controller
@RequestMapping("/")
public class RegistroControlador {
    
    @Autowired
    private EmpleadoServicio empleadoServicio;
  
    
     @GetMapping("/registro")
    public String resgistro() {

        return "registro.html";
    }
    
    
    @PostMapping("/nempleado")
    public String nempleado( ModelMap modelo, @RequestParam String nombre, @RequestParam String email, @RequestParam String apellido, @RequestParam String dni, @RequestParam String clave, @RequestParam String clave2) {

        try {
            empleadoServicio.guardar(dni, nombre, apellido, email, clave, clave2);
        } catch (ErrorServicio ex) {
            System.out.println(ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("email", email);
            modelo.put("error", ex.getMessage());
            return "registro.html";
        }
         
        
        modelo.put("titulo", "Felicidades");
        modelo.put("mensaje", "Pronto recibiras por mail los bonos de sueldo o podras descargarlos");

        return "exito.html";
    }


     @GetMapping("/fpass")
    public String fpass() {

        return "fpass.html";
    }
    




}
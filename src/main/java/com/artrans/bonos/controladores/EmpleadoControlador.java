/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;

import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.servicios.AreaServicio;
import com.artrans.bonos.servicios.BonoServicio;
import com.artrans.bonos.servicios.EmpleadoServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class EmpleadoControlador {
    @Autowired
    private EmpleadoServicio empleadoServicio;
    
    @Autowired
    private BonoServicio bonoServicio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    @Autowired
    private AreaServicio areaServicio;
    
    //@PreAuthorize("hasAnyRole('MODULO_EMPLEADO')") 
    @GetMapping("/inicio")
    public String empleado(ModelMap modelo) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        System.out.println(name);
        modelo.put("empleado",  empleadoRepositorio.buscarEmail(name));
        return "empleado.html";

    }
        @GetMapping("/bono")
        public String bono(ModelMap modelo) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        System.out.println(name);

        try {
           
            modelo.put("bonos", bonoServicio.bonosEmpleado(name));
            modelo.put("empleado",  empleadoRepositorio.buscarEmail(name));
        
            return "bonos.html";
        } catch (ErrorServicio ex) {
            modelo.put("bonos", null);
            return "bonos.html";
        }

    }
    
    @PostMapping("/editemp")
    public String editemp(ModelMap modelo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        System.out.println(name);
        modelo.put("usuario", empleadoRepositorio.buscarEmail(name));

        return "empleadoedit.html";

    }

    
    @PostMapping("/editempleado")
    public String editempleado(ModelMap modelo, @RequestParam String id,@RequestParam String email, @RequestParam String nombre ,@RequestParam(required = false) String emaillab,
            @RequestParam String apellido, @RequestParam(required = false) String clave, @RequestParam(required = false) String clave2){
        try {
             System.out.println(clave +", "+ clave2);
            if (!clave.equals("")|| !clave2.equals("")){
                if (clave.equals(clave2)) {
                     empleadoServicio.editar(id, email, nombre, apellido, emaillab, clave2, clave2);
              modelo.put("titulo", "Felicitaciones");
              modelo.put("mensaje", "Sus datos fueron actualizados");
              return "exito.html";
                }else{
                      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                       String name = auth.getName(); //get logged in username
                      modelo.put("error", "las claves no coinciden");
                      modelo.put("usuario", empleadoRepositorio.buscarEmail(name));
                      return "empleadoedit.html";
                }
            }
            
            empleadoServicio.editar(id, email, nombre, apellido, emaillab);
            modelo.put("titulo", "Felicitaciones");
            modelo.put("mensaje", "Sus datos fueron actualizados");
            return "exito.html";
        } catch (ErrorServicio ex) {
             Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                       String name = auth.getName(); //get logged in username
                       
                      modelo.put("usuario", empleadoRepositorio.buscarEmail(name));
            modelo.put("error", ex.getMessage());
            return "empleadoedit.html";
        }}
    
    
      @GetMapping("/adminE")
    public String adminE(ModelMap modelo) {
               Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
       

        modelo.put("usuario", empleadoRepositorio.buscarEmail(name));

            modelo.put("empleados", empleadoRepositorio.findAll());
        
            return "adminempleado.html";
     }
        
    @GetMapping("/empleado{id}")
    public String editE(ModelMap modelo, @PathVariable String id){
    
        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        modelo.put("empleado", respuesta.get());
        System.out.println(respuesta.get().getNombre());
    return "empleadoE.html";
    }
    
    @PostMapping("/eempleado")
    public String eempleado(ModelMap modelo,@RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String dni,
            @RequestParam String email, @RequestParam String tipo,@RequestParam(required = false) String emaillab, @RequestParam(required = false) String habilitado){
    
      
        try {
            boolean chk = !(habilitado == null);
            empleadoServicio.editar(id, email, nombre, apellido, dni, tipo,emaillab, chk);
            return "redirect:/adminE";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("empleados", empleadoRepositorio.findAll());
        
            return "adminempleado.html";
        }
   
    }
    
    }



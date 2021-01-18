/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;

import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.servicios.EmpleadoServicio;
import com.artrans.bonos.servicios.NotificacionServicio;
import com.artrans.bonos.servicios.PasswordResetTokenService;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author chiri
 */
@Controller
@RequestMapping("/")
public class PasswordResetTokenControlador {
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio; 
    @Autowired 
    private EmpleadoServicio empleadoServicio;
    @Autowired 
    private NotificacionServicio notificacionServicio;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    
    
@PostMapping("/user/resetPassword")
public GenericResponse resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) throws ErrorServicio {
   
    Empleado empleado = empleadoRepositorio.buscarEmail(userEmail);
    if (empleado == null) {
        throw new ErrorServicio("no se encontro el email");
    }
    String token = UUID.randomUUID().toString();
    empleadoServicio.createPasswordResetTokenForUser(empleado, token);
    notificacionServicio.enviar(notificacionServicio.constructResetTokenEmail("http://bonosartrans.ddns.net",request.getLocale(), token, empleado));
  return new GenericResponse("message.resetPasswordEmail");
}

    @GetMapping("/user/changePassword{id}")
    public String editar(ModelMap modelo, @PathVariable String id) {
    
    Empleado empleado = passwordResetTokenService.buscarToken(id);
    modelo.put("empleado", empleado);
    return "rpass.html";
    }
    
    @PostMapping("/rpassw")
    public String npasword(ModelMap modelo, @RequestParam String clave, @RequestParam String clave2, @RequestParam String id){
    
        if (clave.equals(clave2)) {
            Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Empleado empleado = respuesta.get();
                String encriptada = new BCryptPasswordEncoder().encode(clave);
                empleado.setClave(encriptada);
                empleadoRepositorio.save(empleado);
                notificacionServicio.enviar("Felicidades su password cambio exitosamente", "Cambio de password", empleado.getEmail());
                modelo.put("titulo", "Felicidades");
                modelo.put("mensaje", "Su contraseña fue cambiada exitosamente");

                return "exito.html";
            } 

            modelo.put("titulo", "Algo Salio mal");
            modelo.put("error", "intente mas tarde");

            return "exito.html";

        }
        modelo.put("titulo", "Algo Salio mal");
        modelo.put("error", "Las contraseñas no coinciden");
        return "exito.html";
    }
    






    private static class GenericResponse {

  
    private String message;
    private String error;
 
    public GenericResponse(String message) {
        super();
        this.message = message;
    }
 
    public GenericResponse(String message, String error) {
        super();
        this.message = message;
        this.error = error;
    }
        }
    }
    


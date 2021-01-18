
package com.artrans.bonos.controladores;

import com.artrans.bonos.entidades.Desvio;
import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.DesvioRepositorio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.servicios.DesvioServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jgodoy
 */
@Controller
@RequestMapping("/")
public class DesvioControlador {
    
    @Autowired
    private DesvioRepositorio desvioRepositorio;
    @Autowired
    private DesvioServicio desvioServicio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;


    
    @GetMapping("/DesviosCalidad")
    public String DesviosCalidad(ModelMap model){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Empleado usuarioLog = empleadoRepositorio.buscarEmail(auth.getName());
        if(usuarioLog.getPermiso().toString().equals("SUPERVISOR")|| usuarioLog.getPermiso().toString().equals("CALIDAD") ){
            model.put("Desvios", desvioRepositorio.findAll());
            model.put("usuario", usuarioLog);
            return "DesviosCalidad.html";
        }else{
            return "redirect:/DesviosResponsable";
        }
    }
    
    @GetMapping("/DesviosResponsable")
    public String DesviosResponsable(ModelMap model){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Empleado usuarioLog = empleadoRepositorio.buscarEmail(auth.getName());
        
        if(usuarioLog != null){
            
            if(usuarioLog.getPermiso().toString().equals("ADMINAREA")){
                
                //Cambiar por una lista de desvios de Area
                List<Desvio> desviosResp = desvioRepositorio.buscarporArea(usuarioLog.getEmailLab());
                model.put("desviosResp", desviosResp);
            
            }else{
                
                if ((usuarioLog.getPermiso().toString().equals("EMPLEADO"))) {
                    
                    List<Desvio> desviosResp = desvioRepositorio.buscarPorIdResp(usuarioLog.getId());
                    model.put("desviosResp", desviosResp);
                    
                }else{
                
                    if((usuarioLog.getPermiso().toString().equals("PLANEAMIENTO"))){
                    
                        List<Desvio> desviosResp = desvioRepositorio.findAll();
                        model.put("desviosResp", desviosResp);
                    
                    }
                
                    
                
                
                }
                    
            }
        }

        model.put("usuario", usuarioLog);
        
        return "DesviosResponsable.html";
        
    }
    
    
    
    @RequestMapping(value = "/DesviosCalidad/MostarDesvios",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE} )
    public ResponseEntity<List<Desvio>> listarDesvios(){
        
        try{
        
            ResponseEntity<List<Desvio>> responseEntity;
            responseEntity = new ResponseEntity<>(desvioRepositorio.findAll(), HttpStatus.OK);
            return responseEntity;
              
        }catch(Exception e){
            return new ResponseEntity<List<Desvio>>(HttpStatus.BAD_REQUEST); 
        }
    
    
    }
    
    @GetMapping("/DesvioCalidad/OneDesvio")
    @ResponseBody
    public Optional<Desvio> desvioPorId(@RequestParam String Id, Model model){
        
        System.out.println("El id es: "+ Id);
    
        return desvioRepositorio.findById(Id);
    
    }
    
    

    

    
    
    
 
}



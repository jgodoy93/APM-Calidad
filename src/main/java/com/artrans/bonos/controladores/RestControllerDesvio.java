/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;

import com.artrans.bonos.entidades.Area;
import com.artrans.bonos.entidades.Desvio;
import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.Imagen;
import com.artrans.bonos.entidades.ProyectoTrafo;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.AreaRepositorio;
import com.artrans.bonos.repositorio.DesvioRepositorio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.repositorio.RepositorioProyectoTrafo;
import com.artrans.bonos.servicios.AreaServicio;
import com.artrans.bonos.servicios.DesvioServicio;
import com.artrans.bonos.servicios.ServicioImagen;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Invitado ARTRANS
 */
@RestController
public class RestControllerDesvio {
    
    @Autowired 
    private DesvioServicio desvioServicio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    @Autowired
    private RepositorioProyectoTrafo repoTrafo;
    @Autowired
    private DesvioRepositorio desvioRepositorio;
    @Autowired
    private ServicioImagen servicioImagen;
    @Autowired
    private AreaServicio areaServicio;
    @Autowired
    private AreaRepositorio areaRepo;
    
    //Prestar mucha atencion en como se escriben las direcciones,
    //En el controlador van con /
    //En el metodo GET,PUT,POST de jquery van sin "/"
    
    @GetMapping("/DesviosCalidad/listaEmpleados")
    public List<Empleado> listarEmpleados (){
         System.out.println("Invoque al craneo en Empleados");
        //lista.forEach((n) -> System.out.println(n.getNombre()));
        return (List<Empleado>) empleadoRepositorio.findAll();
    }
    
    @GetMapping("/DesviosCalidad/listarEmpleadosArea")
    public List<Empleado> listarEmpleadosArea (@RequestParam String idDesvio) throws ErrorServicio{ 
        
        Desvio desvio = desvioRepositorio.getOne(idDesvio);
        String idArea = desvio.getArea().getUnidadOrganizacionalCodi();
        return (List<Empleado>) areaServicio.listarPorArea(idArea);
    }
    
    
    @GetMapping("/DesviosCalidad/listarAreas")
    public List<Area> listarAreas(){
        return (List<Area>) areaServicio.verAreasActivas();
    }
    
    /*
    TODO: 
        -Cambiar EndPoints y ponerlas todas en minuscula,
    */
    
    
    //En json puedo Subir miles de Parametros 
    @PostMapping("/DesviosCalidad/guardarDesvio") 
    public String guardarDesvio(@RequestBody String jsonData) throws ErrorServicio, JsonProcessingException{
        try{
            //Mappeo Json
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData); 
            String idProyecto = jsonNode.get("idProyecto").asText();
            String idUsuarioEmisor = jsonNode.get("idEmisor").asText();            
            String procesoFab = jsonNode.get("process").asText();
            String trafoParte = jsonNode.get("trafo").asText();
            String causaPrincipal = jsonNode.get("CausaIshicawa").asText();
            String efectoCalidad = jsonNode.get("Calidad").asText();
            String planta = jsonNode.get("Planta").asText();
            String observacionCalidad = jsonNode.get("Observacion").asText(); 
            String idArea = jsonNode.get("idArea").asText();
             
        //Proyecto Que llega desde la vista
            Optional<ProyectoTrafo> respuesta = repoTrafo.findById(idProyecto);
            ProyectoTrafo pro = respuesta.get();
        //Usuario Emisor
            Optional<Empleado> respuesta1 = empleadoRepositorio.findById(idUsuarioEmisor);
            Empleado generador = respuesta1.get();
        //IdArea
            Optional<Area> respuesta3 = areaRepo.findById(idArea);
            Area areaDesvio = respuesta3.get();
                 
        //Empleado Reponsable
        //Parametros del Desvio
        Desvio desvioGuardado = desvioServicio.crearDesvio(areaDesvio,
                generador,
                pro,
                procesoFab,
                trafoParte,
                causaPrincipal,
                efectoCalidad,
                planta,
                observacionCalidad,
                null);
        //Mensaje Aprobacion
        System.out.println(desvioGuardado.getId());
        System.out.println("Desvio Guardado");
        
        String jsonResponse = objectMapper.writeValueAsString(desvioGuardado);

     
        
        return String.valueOf(jsonResponse);
        
        }catch(ErrorServicio e){
            return e.getMessage();
        }
        
    }
    
    @PostMapping("/DesviosCalidad/ModificarDesvio")
    public String modificarDesvio(@RequestBody String jsonData) throws ErrorServicio, JsonProcessingException{
        System.out.println("Entre al controlador");
        try{
            //Mappeo Json
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            String idDesvio = jsonNode.get("id").asText();
            String idProyecto = jsonNode.get("idProyecto").asText();
            String idUsuarioEmisor = jsonNode.get("idEmisor").asText();            
            String procesoFab = jsonNode.get("process").asText();
            String trafoParte = jsonNode.get("trafo").asText();
            String causaPrincipal = jsonNode.get("CausaIshicawa").asText();
            String efectoCalidad = jsonNode.get("Calidad").asText();
            String planta = jsonNode.get("Planta").asText();
            String observacionCalidad = jsonNode.get("Observacion").asText();
            String idArea = jsonNode.get("idArea").asText();
        //Proyecto Que llega desde la vista
            Optional<ProyectoTrafo> respuesta = repoTrafo.findById(idProyecto);
            ProyectoTrafo pro = respuesta.get();
        //Usuario Emisor
            Optional<Empleado> respuesta1 = empleadoRepositorio.findById(idUsuarioEmisor);
            Empleado generador = respuesta1.get();
        //IdArea
            Optional<Area> respuesta3 = areaRepo.findById(idArea);
            Area areaDesvio = respuesta3.get();    
        //Fecha de Creacion (parsear para que se guarde año/mes/dia/hora
            Date fechaEmision = new Date();        
        //Parametros del Desvio
        Desvio desvioGuardado = desvioServicio.modificarDesvio(idDesvio
                ,areaDesvio
                , generador
                , pro               
                , procesoFab
                , trafoParte
                , causaPrincipal
                , efectoCalidad
                , planta
                , observacionCalidad);
        
        //Mensaje Aprobacion

 
        return "Desvio Modificado Exitosamente.";
        
        }catch(ErrorServicio e){
            return e.getMessage();
        }
        
    }

    @GetMapping("/DesviosResponsable/Desvio{id}")
    public Desvio mostrarPorId(@PathVariable String id){
        Optional<Desvio> respuesta = desvioRepositorio.findById(id);
        return respuesta.get();      
    }
    
    @PostMapping("/Desvio{id}/cargarFotos")
    public String cargarImagenesDesvio(@PathVariable String id, @RequestParam List<MultipartFile> imagenes) throws ErrorServicio{
        
        System.out.println("desvio nro:" +id);
        System.out.println("Entre al controlador");
        try{
        List<Imagen> imagenesDesvio = new ArrayList();
        //Recorro la lista de imagenes que llega desde el controlador
        for(MultipartFile m: imagenes){
            Imagen imagen = servicioImagen.guardar(m);
            imagenesDesvio.add(imagen);
        }
        
        Optional<Desvio> respuesta = desvioRepositorio.findById(id);
        if(respuesta.isPresent()){
            Desvio desvio = respuesta.get();
            desvio.setImagenes(imagenesDesvio);
            desvioRepositorio.save(desvio);
        }
        
        return "Imagenes Cargadas Correctamente.";
        }catch(ErrorServicio e){
            throw new ErrorServicio("No se pudieron cargar las imagenes.");
        }  
    }
    
    @PostMapping("/Desvio{idDesv}/Imagen{idImg}/modificarFotos")
    public String modificarImagenesDesvio(@PathVariable String idDesv,@PathVariable String idImg, @RequestParam MultipartFile imagen) throws ErrorServicio{
        
        if(idImg.equals("0")){
            try{
        //Recorro la lista de imagenes que llega desde el controlador
                Optional<Desvio> respuesta = desvioRepositorio.findById(idDesv);
                if(respuesta.isPresent()){
                    Desvio desvio = respuesta.get();
                    desvio.guardarUnaImagen(servicioImagen.guardar(imagen));
                    desvioRepositorio.save(desvio);
                }
                return "Imagen Cargadas Correctamente.";
            }catch(ErrorServicio e){
                throw new ErrorServicio("No se pudieron cargar las imagenes.");
            }
        }else{         
            try{
                Optional<Desvio> respuesta = desvioRepositorio.findById(idDesv);
                if(respuesta.isPresent()){
                    Desvio desvio = respuesta.get();
                    List<Imagen> imagenes = desvio.getImagenes();
                    for(Imagen img : imagenes){
                        if(img.getId().equals(idImg)){
                            System.out.println("Encontre imagen");
                            servicioImagen.Modificar(imagen, idImg);
                        }
                    }
                }
                return "Imagen Modificada Correctamente.";
            }catch(ErrorServicio e){
                throw new ErrorServicio("No se pudieron cargar las imagenes.");
            } 
        }  
    }
    
    
    @GetMapping("/DesviosCalidad/listaDesvioId")
    public List<Desvio> listarIdDesvio() throws ErrorServicio{
        return (List<Desvio>) desvioServicio.mostrarTodos();   
    }

    /*
        +///////////////////////////////////////////+
        
         CONTROLES DE TRANSFERENCIA DE DESVIOS
                                                           
        +///////////////////////////////////////////+
    */
    
    @PostMapping("/DesviosCalidad/AsignarDesvio")
    public String asignar(@RequestBody String jsonData) throws ErrorServicio{
        
        try{
            
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            
            String idDesvio = jsonNode.get("idDesvio").asText();
            String idResponsable = jsonNode.get("idResp").asText();
            
            Optional<Empleado> respuesta2 = empleadoRepositorio.findById(idResponsable);
            Empleado responsable = respuesta2.get();
            
            return desvioServicio.asignarResponsable(idDesvio, responsable);
            
        
        }catch(Exception e){
            return (e.getMessage());
        }
    
    
    }
    
    
    
    @PostMapping("/DesviosCalidad/ResponderDesvio")
    public String responderDesvio(@RequestBody String jsonData) throws JsonProcessingException, ErrorServicio{
        try{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        
        String id = jsonNode.get("id").asText();
        String causa = jsonNode.get("causaRaiz").asText();
        String analisisCausa = jsonNode.get("analisis").asText();
        Integer horasRetrabajo = jsonNode.get("horas").asInt();
        String accionCorrectiva = jsonNode.get("accion").asText();

        return desvioServicio.responderDesvio(id,causa,analisisCausa,horasRetrabajo,accionCorrectiva);
        }catch(Exception e){
            return (e.getMessage());
        }
  
    }
  
    @GetMapping("/DesviosCalidad/enviarDesvio")
    public String enviarDesvio(@RequestParam String id)throws ErrorServicio{
          try{ 
          return desvioServicio.EnviarDesvio(id);
          }catch(Exception e){
              return (e.getMessage());
          }
    }
    
    @PostMapping("/DesviosResponsable/cargarDisposicionDesvio")
    public String cargarDisposicion(@RequestBody String jsonData)throws ErrorServicio, JsonProcessingException{
         try{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData); 
        
        String id = jsonNode.get("id").asText();
        String disposicion = jsonNode.get("disposicion").asText();
        
        return desvioServicio.CargarDisposicion(id, disposicion);
         }catch(Exception e){
             return(e.getMessage());
         }
    }
    
    @GetMapping("/DesviosCalidad/AprobarDesvio")
    public String Aprobar(@RequestParam String id) throws ErrorServicio{
        
        try{            
            return desvioServicio.Aprobar(id);           
        }catch(Exception e){           
             return(e.getMessage());
        }
    }
    
    @PostMapping("/DesviosCalidad/desaprobarDesvio")
    public String desaprobar(@RequestBody String jsonData) throws ErrorServicio, JsonProcessingException{
        
        //@RequestBody String jsonData
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData); 
        
        String id = jsonNode.get("id").asText();
        String observacion = jsonNode.get("observacion").asText();
        try{  
            return desvioServicio.Desaprobado(id, observacion);
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            return(e.getMessage());        
        }
    }
    
    @DeleteMapping("/DesviosCalidad/eliminarDesvio")
    public String eliminarDesvio(@RequestParam String id) throws ErrorServicio{
        
        return desvioServicio.eliminarDesvio(id);
            
    }
    
 
}

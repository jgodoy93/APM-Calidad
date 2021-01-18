/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;

import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.ProyectoTrafo;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.RepositorioProyectoTrafo;
import com.artrans.bonos.servicios.ProyectoTrafoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jgodoy
 */
@RestController
public class RestControllerProyecto {
    
    @Autowired
    private RepositorioProyectoTrafo proyectRepo;
    @Autowired
    private ProyectoTrafoServicio proyectoTrafoServicio;

    
    @GetMapping("/DesviosCalidad/buscar")
    public List<ProyectoTrafo> buscarPorNv(@RequestParam String nv) throws ErrorServicio{
        System.out.println("Invoca al craneooo!");
        List<ProyectoTrafo> lista = proyectoTrafoServicio.buscarPorNotadeVenta(nv);
        for(ProyectoTrafo p : lista){
            System.out.println(p.getTareaCodi()+"|"+p.getDescripcion());
        }
        return (List<ProyectoTrafo>) proyectoTrafoServicio.buscarPorNotadeVenta(nv);   
    }
    
    

    /*
    //Para mandar un JSON por el controlador
    
    @RequestMapping(value = "/buscar",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE} )
    public ResponseEntity<List<ProyectoTrafo>> buscar(@RequestParam String notaVenta){
        try{
            ResponseEntity<List<ProyectoTrafo>> responseEntity = 
                    new ResponseEntity<List<ProyectoTrafo>>(proyectoTrafoServicio.buscarPorNotadeVenta(notaVenta), HttpStatus.OK);
            List<ProyectoTrafo> lista = proyectoTrafoServicio.buscarPorNotadeVenta(notaVenta);
            lista.forEach((p) -> {
                System.out.println(p.getDescripcion() + "|" + p.getNroSerie());
            });
            return responseEntity; 
        }catch(Exception e){
            return new ResponseEntity<List<ProyectoTrafo>>(HttpStatus.BAD_REQUEST);
        }
    }

    */
}


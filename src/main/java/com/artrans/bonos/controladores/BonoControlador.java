/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;


import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.servicios.BonoServicio;
import com.artrans.bonos.servicios.EmpleadoServicio;
import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class BonoControlador {

    @Autowired
    private BonoServicio bonoServicio;

    @Autowired
    private EmpleadoServicio empleadoServicio;
    @PreAuthorize("hasAuthority('MODULO_BONO')")
    @GetMapping("/cargarbonos")
    public String cargabono() {
        return "subepdf.html";
    }

    @PostMapping("bonospdf")
    public String bonospdf(ModelMap modelo, @RequestParam List<MultipartFile> bonos, @RequestParam String mes, @RequestParam String anio) {
       
        if (bonos.size() == 0) {
            modelo.put("error", "NO se cargaron archivos");
            return "subepdf.html";
        }
        try {
            ArrayList<String> bonosok = new ArrayList<>();
            for (MultipartFile bono : bonos) {
                Empleado empleado = empleadoServicio.buscarPorDNI(bono.getOriginalFilename().substring(0, 8));
                if (empleado != null) {
                    empleadoServicio.guardarBono(empleado.getId(), anio, mes, bono);
                }else{
                bonosok.add(bono.getOriginalFilename());
                }
            }
            modelo.put("titulo", "Felicitaciones");
            modelo.put("mensaje", "Los bonos fueron cargados");
            if (bonosok.size() !=0) {
                 modelo.put("bonosno", bonosok);
                 modelo.put("error", "Los Siguientes bonos no se cargaron");
            }
           
            return "exito.html";

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "subepdf.html";
        }

    }
    
    @GetMapping("/bono{id}")
    public  ResponseEntity<byte[]> abrirpdf(@PathVariable String id){
    
        try {
            return bonoServicio.traer(id);
        } catch (ErrorServicio ex) {
           return ResponseEntity.status(404).body(null);
        }
    }

}

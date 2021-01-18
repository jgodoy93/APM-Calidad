package com.artrans.bonos.controladores;


import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value= "/Empleados")
public class RestControllerEmpleado {

    @Autowired
    EmpleadoRepositorio empleadoRepositorio;


    @GetMapping(value= "/{id}")
    public ResponseEntity<Empleado> getEmpleado(@PathVariable("id") String id) throws ErrorServicio {
        try {
            Optional<Empleado> empleado = empleadoRepositorio.findById(id);
            if(null == empleado.get()){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(empleado.get());
        }catch (Exception e){
            throw new ErrorServicio("Error con el id ingresado del empleado");
        }
    }




}

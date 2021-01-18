/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.servicios;

import com.artrans.bonos.entidades.OrdenesDeTrabajo;
import com.artrans.bonos.repositorio.OrdenesDeTrabajoRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chiri
 */
@Service
public class OrdenDeTrabajoServicio {
    @Autowired
    private OrdenesDeTrabajoRepositorio ordenesDeTrabajoRepositorio;
    
    @Transactional
    public List<OrdenesDeTrabajo> buscarOT(String dni){
    
      List<OrdenesDeTrabajo> ots = new ArrayList<>();
      
      ots = ordenesDeTrabajoRepositorio.buscarOTpordni(dni);
      
    return ots; 
    }
    
    
}

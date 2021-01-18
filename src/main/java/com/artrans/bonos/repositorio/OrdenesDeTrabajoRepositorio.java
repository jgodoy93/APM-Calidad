/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.repositorio;


import com.artrans.bonos.entidades.OrdenesDeTrabajo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chiri
 */
@Repository
public interface OrdenesDeTrabajoRepositorio extends JpaRepository<OrdenesDeTrabajo, String>{

      @Query("SELECT o FROM OrdenesDeTrabajo o, IN(o.empleados) e WHERE e.dni = :dni AND e.habilitado = true")
      public List<OrdenesDeTrabajo> buscarOTpordni(@Param("dni") String dni);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.repositorio;

import com.artrans.bonos.entidades.Area;
import com.artrans.bonos.entidades.Empleado;
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
public interface AreaRepositorio  extends JpaRepository<Area, String>{
    @Query("SELECT a FROM Area a WHERE a.baja = :baja")
    public List<Area> areaAlta(@Param("baja") boolean baja);
    
      @Query("SELECT e FROM Area a, IN(a.responsables)e WHERE a.unidadOrganizacionalCodi = :id")
       public List<Empleado> buscarEmpleadosArea(@Param("id") String id); 
       
      @Query("SELECT e FROM Area a, IN(a.encargado)e WHERE a.unidadOrganizacionalCodi = :id")
       public Empleado buscarJefeArea(@Param("id") String id); 
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.repositorio;

import com.artrans.bonos.entidades.ProyectoTrafo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jgodoy
 */
public interface RepositorioProyectoTrafo extends JpaRepository<ProyectoTrafo, String> {
    
    @Query("SELECT p FROM ProyectoTrafo p WHERE p.notadeVenta = :notadeVenta")
    public List<ProyectoTrafo> buscarPorNotaVenta(@Param("notadeVenta") String notadeVenta);

}

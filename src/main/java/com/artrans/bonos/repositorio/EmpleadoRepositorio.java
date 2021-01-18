/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.repositorio;

import com.artrans.bonos.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chiri
 */
@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, String> {
    
    @Query("SELECT c FROM Empleado c WHERE c.email = :email " )
    public Empleado buscarEmail(@Param("email") String email);
    
     @Query("SELECT c FROM Empleado c WHERE c.dni = :dni " )
     public Empleado buscarDni(@Param("dni") String dni);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.repositorio;


import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chiri
 */
@Repository
public interface PasswordResetTokenRepositorio extends JpaRepository<PasswordResetToken, String>  {
      @Query("SELECT e FROM PasswordResetToken t, IN(t.empleado) e WHERE t.token = :token")
      public Empleado buscarEmpleadoPorTokenl(@Param("token") String token);
    
}

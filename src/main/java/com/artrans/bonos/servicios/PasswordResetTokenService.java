/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.servicios;

import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.PasswordResetToken;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.repositorio.PasswordResetTokenRepositorio;
import java.util.Calendar;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author chiri
 */
@Service
public class PasswordResetTokenService {
    
    @Autowired
    private PasswordResetTokenRepositorio passwordResetTokenRepositorio;
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    
    @Transactional
    public String validatePasswordResetToken(String token) {
     
        Optional<PasswordResetToken> respuesta = passwordResetTokenRepositorio.findById(token);

          if (respuesta.isPresent()) {
            final  PasswordResetToken passToken = respuesta.get();
            return !isTokenFound(passToken) ? "invalidToken"
            : isTokenExpired(passToken) ? "expired"
            : null;
        }
    
 return null;
}
    
    @Transactional
    public Empleado buscarToken(String token) {
        
        System.out.println("llegue al empleado");
         Empleado empleado = passwordResetTokenRepositorio.buscarEmpleadoPorTokenl(token);
         if (empleado == null) {
             System.out.println("no se encontro empleado");
             return null;
        }
         System.out.println("encontre el empleado");
        return empleado;
    }

    
    
    
 
private boolean isTokenFound(PasswordResetToken passToken) {
    return passToken != null;
}
 
private boolean isTokenExpired(PasswordResetToken passToken) {
    final Calendar cal = Calendar.getInstance();
    return passToken.getExpiryDate().before(cal.getTime());
}
    
    public void changeUserPassword(Empleado empleado, String password) {
     
         String encriptada = new BCryptPasswordEncoder().encode(password);
        empleado.setClave(encriptada);
        empleadoRepositorio.save(empleado);
        
}
    
}

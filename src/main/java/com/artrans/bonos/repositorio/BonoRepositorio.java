
package com.artrans.bonos.repositorio;

import com.artrans.bonos.entidades.Bono;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BonoRepositorio extends JpaRepository<Bono, String> {
    
     @Query("SELECT b FROM Empleado e, IN(e.bonos) b WHERE e.email = :email AND e.habilitado = true")
      public List<Bono> buscarBonosPorMail(@Param("email") String email);
      @Query("SELECT b FROM Bono b WHERE b.nombre = :nombre")
      public Bono buscarBonosPorNombre(@Param("nombre") String nombre);
    
}

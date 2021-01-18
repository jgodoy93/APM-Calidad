
package com.artrans.bonos.repositorio;

import com.artrans.bonos.entidades.Desvio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author johnGodoy
 */
@Repository
public interface DesvioRepositorio extends JpaRepository<Desvio, String>  {
    
       @Query("SELECT d FROM Desvio d,IN(d.responsable)e WHERE e.id = :id")
       public List<Desvio> buscarPorIdResp(@Param("id") String id);
       
       @Query("SELECT d FROM Desvio d, IN(d.area)a, IN(a.encargado)e WHERE e.emailLab = :emailLab")
       public List<Desvio> buscarporArea(@Param("emailLab") String emailLab);
       
       @Query("SELECT d FROM Desvio d WHERE d.id = : id")
       public Desvio buscarPorId(@Param("id") String id);
}


package com.artrans.bonos.repositorio;

import com.artrans.bonos.entidades.Imagen;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jgodoy
 */
@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen,String> {
    
    @Query("SELECT i FROM Desvio d, IN(d.imagenes) i WHERE d.id = :id AND d.Habilitado = true")
    public List<Imagen> buscarPorIdDesvio(@Param("id") String id);
    
}

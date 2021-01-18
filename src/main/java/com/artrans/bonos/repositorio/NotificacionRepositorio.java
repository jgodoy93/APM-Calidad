package com.artrans.bonos.repositorio;

import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {

    //TERMINAR CONSULTA DE EMPLEADO
    /*
    @Query("SELECT n FROM Notificacion n,")
    public Empleado notificacionPorEmpleado(@Param("id") String id);
    */


}

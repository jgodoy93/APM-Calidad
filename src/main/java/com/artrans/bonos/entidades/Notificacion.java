package com.artrans.bonos.entidades;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Empleado empleado;

    private Boolean desvioEnviado;
    private Boolean desvioDesaprobado;
    private Boolean desvioDispInmediata;
    private Boolean desvioRespondido;


}

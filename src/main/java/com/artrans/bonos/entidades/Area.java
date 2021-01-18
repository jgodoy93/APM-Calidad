/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author chiri
 */
@Entity
public class Area {
    @Id
    private String unidadOrganizacionalCodi;
    
    private String nombre;
    private Boolean baja;
    
    @OneToOne
    private Empleado encargado;
    @ManyToMany
    private List<Empleado> responsables;

    public Area() {
    }
    
    
    public Area(String unidadOrganizacionalCodi, String String) {
        this.unidadOrganizacionalCodi = unidadOrganizacionalCodi;
        this.nombre = String;
    }

    /**
     * @return the unidadOrganizacionalCodi
     */
    public String getUnidadOrganizacionalCodi() {
        return unidadOrganizacionalCodi;
    }

    /**
     * @param unidadOrganizacionalCodi the unidadOrganizacionalCodi to set
     */
    public void setUnidadOrganizacionalCodi(String unidadOrganizacionalCodi) {
        this.unidadOrganizacionalCodi = unidadOrganizacionalCodi;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the baja
     */
    public Boolean getBaja() {
        return baja;
    }

    /**
     * @param baja the baja to set
     */
    public void setBaja(Boolean baja) {
        this.baja = baja;
    }

    /**
     * @return the encargado
     */
    public Empleado getEncargado() {
        return encargado;
    }

    /**
     * @param encargado the encargado to set
     */
    public void setEncargado(Empleado encargado) {
        this.encargado = encargado;
    }

    /**
     * @return the responsables
     */
    public List<Empleado> getResponsables() {
        return responsables;
    }

    /**
     * @param responsables the responsables to set
     */
    public void setResponsables(List<Empleado> responsables) {
        this.responsables = responsables;
    }

}
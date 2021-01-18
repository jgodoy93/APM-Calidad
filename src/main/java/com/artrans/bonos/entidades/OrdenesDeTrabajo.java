/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author chiri
 */
@Entity
public class OrdenesDeTrabajo {
    @Id
    private String tareaCodi;
    private String area;
    private String nombreTarea;
    private String proyecto;
    @OneToMany
    private List<Empleado> empleados;
    private String cliente;
    private String articuloVerson;
    private String nSerie;

    /**
     * @return the tareaCodi
     */
    public String getTareaCodi() {
        return tareaCodi;
    }

    public OrdenesDeTrabajo() {
    }

    /**
     * @param tareaCodi the tareaCodi to set
     */
    public void setTareaCodi(String tareaCodi) {
        this.tareaCodi = tareaCodi;
    }

    /**
     * @return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return the nombreTarea
     */
    public String getNombreTarea() {
        return nombreTarea;
    }

    /**
     * @param nombreTarea the nombreTarea to set
     */
    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    /**
     * @return the proyecto
     */
    public String getProyecto() {
        return proyecto;
    }

    /**
     * @param proyecto the proyecto to set
     */
    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }


    /**
     * @return the articuloVerson
     */
    public String getArticuloVerson() {
        return articuloVerson;
    }

    /**
     * @param articuloVerson the articuloVerson to set
     */
    public void setArticuloVerson(String articuloVerson) {
        this.articuloVerson = articuloVerson;
    }

    /**
     * @return the nSerie
     */
    public String getnSerie() {
        return nSerie;
    }

    /**
     * @param nSerie the nSerie to set
     */
    public void setnSerie(String nSerie) {
        this.nSerie = nSerie;
    }

    /**
     * @return the empleados
     */
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    /**
     * @param empleados the empleados to set
     */
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    /**
     * @return the cliente
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

      
}

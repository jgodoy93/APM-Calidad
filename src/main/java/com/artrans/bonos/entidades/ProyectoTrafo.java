
package com.artrans.bonos.entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author jgodoy
 */
@Entity
public class ProyectoTrafo implements Serializable{
    
    @Id
    private String TareaCodi;
    
    private String notadeVenta;
    
    private String cliente;
    
    private Boolean seleccion;
    
    private String descripcion;
    
    private String nroSerie;
    
    private String tareaItem;
    
    private String codigo;

    /**
     * @return the TareaCodi
     */
    public String getTareaCodi() {
        return TareaCodi;
    }

    /**
     * @param TareaCodi the TareaCodi to set
     */
    public void setTareaCodi(String TareaCodi) {
        this.TareaCodi = TareaCodi;
    }

    /**
     * @return the notadeVenta
     */
    public String getNotadeVenta() {
        return notadeVenta;
    }

    /**
     * @param notadeVenta the notadeVenta to set
     */
    public void setNotadeVenta(String notadeVenta) {
        this.notadeVenta = notadeVenta;
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

    /**
     * @return the seleccion
     */
    public Boolean getSeleccion() {
        return seleccion;
    }

    /**
     * @param seleccion the seleccion to set
     */
    public void setSeleccion(Boolean seleccion) {
        this.seleccion = seleccion;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the nroSerie
     */
    public String getNroSerie() {
        return nroSerie;
    }

    /**
     * @param nroSerie the nroSerie to set
     */
    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    /**
     * @return the tareaItem
     */
    public String getTareaItem() {
        return tareaItem;
    }

    /**
     * @param tareaItem the tareaItem to set
     */
    public void setTareaItem(String tareaItem) {
        this.tareaItem = tareaItem;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
            
            
    
    
    
}

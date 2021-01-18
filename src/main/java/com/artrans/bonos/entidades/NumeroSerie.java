
package com.artrans.bonos.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author jgodoy
 */
@Entity
public class NumeroSerie {
    
    @Id
    private String numero;
    
    private Boolean Seleccionado;

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the Seleccionado
     */
    public Boolean getSeleccionado() {
        return Seleccionado;
    }

    /**
     * @param Seleccionado the Seleccionado to set
     */
    public void setSeleccionado(Boolean Seleccionado) {
        this.Seleccionado = Seleccionado;
    }
    
    
    
    
}

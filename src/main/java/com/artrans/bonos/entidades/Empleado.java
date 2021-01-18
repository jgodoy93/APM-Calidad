
package com.artrans.bonos.entidades;

import com.artrans.bonos.enun.Permisos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;


@Entity
public class Empleado {
    
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean habilitado;
    private Permisos permiso;
    private String emailLab;
    
    private String clave;
    
    @OneToMany
    private List<Bono> bonos;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the legajo
     */


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
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the bonos
     */
    public List<Bono> getBonos() {
        return bonos;
    }

    /**
     * @param bonos the bonos to set
     */
    public void setBonos(ArrayList<Bono> bonos) {
        this.setBonos(bonos);
    }
    public void guardarUnBono(Bono bono){
        getBonos().add(bono);
    }
    public void borrarUnBono(Bono bono){
    
        getBonos().remove(bono);
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @param bonos the bonos to set
     */
    public void setBonos(List<Bono> bonos) {
        this.bonos = bonos;
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the habilitado
     */
    public boolean isHabilitado() {
        return getHabilitado();
    }

    /**
     * @param habilitado the habilitado to set
     */
    public void setHabilitado(boolean habilitado) {
        this.setHabilitado((Boolean) habilitado);
    }

    /**
     * @return the permiso
     */
    public Permisos getPermiso() {
        return permiso;
    }

    /**
     * @param permiso the permiso to set
     */
    public void setPermiso(Permisos permiso) {
        this.permiso = permiso;
    }

    /**
     * @return the habilitado
     */
    public Boolean getHabilitado() {
        return habilitado;
    }

    /**
     * @param habilitado the habilitado to set
     */
    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    /**
     * @return the emailLab
     */
    public String getEmailLab() {
        return emailLab;
    }

    /**
     * @param emailLab the emailLab to set
     */
    public void setEmailLab(String emailLab) {
        this.emailLab = emailLab;
    }





}

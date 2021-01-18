
package com.artrans.bonos.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;



@Entity
public class PasswordResetToken {

    private static int EXPIRATION = 60 * 24;
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String token;

    @OneToOne
    private Empleado empleado;

    private Date expiryDate;

    public PasswordResetToken(String token, Empleado empleado) {
        this.token = token;
        this.empleado = empleado;
    }
    
    
    
    

    /**
     * @return the EXPIRATION
     */
    public static int getEXPIRATION() {
        return EXPIRATION;
    }

    /**
     * @param aEXPIRATION the EXPIRATION to set
     */
    public static void setEXPIRATION(int aEXPIRATION) {
        EXPIRATION = aEXPIRATION;
    }

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
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}
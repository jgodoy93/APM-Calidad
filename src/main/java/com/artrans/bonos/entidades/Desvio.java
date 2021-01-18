
package com.artrans.bonos.entidades;


import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Desvio {
    
    //ver como generar un valor a partir del ultimo desvio
    @Id
    private String id;
    
    @OneToOne
    private ProyectoTrafo Proyecto;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDisposicion;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRespuesta;
    
    //Usuario que Genera el desvio 
    @OneToOne
    private Empleado Generador;
    
    @OneToOne
    private Area area;
    
    //Usuario que responde el desvio
    @OneToOne
    private Empleado responsable;
    
    //Contenido en imagenes del Desvio
    @OneToMany
    private List<Imagen> imagenes;
    
    private Boolean HabilitacionArea;
    
    private Boolean Habilitado;
    
    private Boolean visible;
    
    private String procesoFab;
    
    private String trafoparte;
    
    private String causaPrincipal;
    
    private String efectoscalidad;
    
    private String estado;
    
    private Integer HorasRetrabajo;
    
    private String causaRaiz;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String ObservacionCalidad;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String analisisDeCausa;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String disposicionInmediata;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String accionCorrectiva;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String observacionCorreccion;
    
    private String planta;
    
    //AgregarMetodos para Calcular Retrabajos;
    //Agregar Listado de Materiales comprometidos;

    /**
     * @return the fechaEmision
     */
    public Date getFechaEmision() {
        return fechaEmision;
    }

    /**
     * @param fechaEmision the fechaEmision to set
     */
    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    /**
     * @return the fechaRespuesta
     */
    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    /**
     * @param fechaRespuesta the fechaRespuesta to set
     */
    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    /**
     * @return the Generador
     */
    public Empleado getGenerador() {
        return Generador;
    }

    /**
     * @param Generador the Generador to set
     */
    public void setGenerador(Empleado Generador) {
        this.Generador = Generador;
    }

    /**
     * @return the responsable
     */
    public Empleado getResponsable() {
        return responsable;
    }

    /**
     * @param responsable the responsable to set
     */
    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }

    
    /**
     * @return the HorasRetrabajo
     */
    public Integer getHorasRetrabajo() {
        return HorasRetrabajo;
    }

    /**
     * @param HorasRetrabajo the HorasRetrabajo to set
     */
    public void setHorasRetrabajo(Integer HorasRetrabajo) {
        this.HorasRetrabajo = HorasRetrabajo;
    }

    /**
     * @return the analisisDeCausa
     */
    public String getAnalisisDeCausa() {
        return analisisDeCausa;
    }

    /**
     * @param analisisDeCausa the analisisDeCausa to set
     */
    public void setAnalisisDeCausa(String analisisDeCausa) {
        this.analisisDeCausa = analisisDeCausa;
    }

    /**
     * @return the disposicionInmediata
     */
    public String getDisposicionInmediata() {
        return disposicionInmediata;
    }

    /**
     * @param disposicionInmediata the disposicionInmediata to set
     */
    public void setDisposicionInmediata(String disposicionInmediata) {
        this.disposicionInmediata = disposicionInmediata;
    }

    /**
     * @return the accionCorrectiva
     */
    public String getAccionCorrectiva() {
        return accionCorrectiva;
    }

    /**
     * @param accionCorrectiva the accionCorrectiva to set
     */
    public void setAccionCorrectiva(String accionCorrectiva) {
        this.accionCorrectiva = accionCorrectiva;
    }

    /**
     * @return the observacionCorreccion
     */
    public String getObservacionCorreccion() {
        return observacionCorreccion;
    }

    /**
     * @param observacionCorreccion the observacionCorreccion to set
     */
    public void setObservacionCorreccion(String observacionCorreccion) {
        this.observacionCorreccion = observacionCorreccion;
    }

    /**
     * @return the imagenes
     */
    public List<Imagen> getImagenes() {
        return imagenes;
    }

    /**
     * @param imagenes the imagenes to set
     */
    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }
    public void guardarUnaImagen(Imagen imagen){
        getImagenes().add(imagen);
    }
    public void borrarUnaImagen(Imagen imagen){
    
        getImagenes().remove(imagen);
    }

    /**
     * @return the Habilitado
     */
    public Boolean getHabilitado() {
        return Habilitado;
    }

    /**
     * @param Habilitado the Habilitado to set
     */
    public void setHabilitado(Boolean Habilitado) {
        this.Habilitado = Habilitado;
    }


    /**
     * @return the ObservacionCalidad
     */
    public String getObservacionCalidad() {
        return ObservacionCalidad;
    }

    /**
     * @param ObservacionCalidad the ObservacionCalidad to set
     */
    public void setObservacionCalidad(String ObservacionCalidad) {
        this.ObservacionCalidad = ObservacionCalidad;
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
     * @return the Proyecto
     */
    public ProyectoTrafo getProyecto() {
        return Proyecto;
    }

    /**
     * @param Proyecto the Proyecto to set
     */
    public void setProyecto(ProyectoTrafo Proyecto) {
        this.Proyecto = Proyecto;
    }

    /**
     * @return the procesoFab
     */
    public String getProcesoFab() {
        return procesoFab;
    }

    /**
     * @param procesoFab the procesoFab to set
     */
    public void setProcesoFab(String procesoFab) {
        this.procesoFab = procesoFab;
    }

    /**
     * @return the trafoparte
     */
    public String getTrafoparte() {
        return trafoparte;
    }

    /**
     * @param trafoparte the trafoparte to set
     */
    public void setTrafoparte(String trafoparte) {
        this.trafoparte = trafoparte;
    }

    /**
     * @return the causaPrincipal
     */
    public String getCausaPrincipal() {
        return causaPrincipal;
    }

    /**
     * @param causaPrincipal the causaPrincipal to set
     */
    public void setCausaPrincipal(String causaPrincipal) {
        this.causaPrincipal = causaPrincipal;
    }

    /**
     * @return the efectoscalidad
     */
    public String getEfectoscalidad() {
        return efectoscalidad;
    }

    /**
     * @param efectoscalidad the efectoscalidad to set
     */
    public void setEfectoscalidad(String efectoscalidad) {
        this.efectoscalidad = efectoscalidad;
    }

    /**
     * @return the planta
     */
    public String getPlanta() {
        return planta;
    }

    /**
     * @param planta the planta to set
     */
    public void setPlanta(String planta) {
        this.planta = planta;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the fechaDisposicion
     */
    public Date getFechaDisposicion() {
        return fechaDisposicion;
    }

    /**
     * @param fechaDisposicion the fechaDisposicion to set
     */
    public void setFechaDisposicion(Date fechaDisposicion) {
        this.fechaDisposicion = fechaDisposicion;
    }

    /**
     * @return the causaRaiz
     */
    public String getCausaRaiz() {
        return causaRaiz;
    }

    /**
     * @param causaRaiz the causaRaiz to set
     */
    public void setCausaRaiz(String causaRaiz) {
        this.causaRaiz = causaRaiz;
    }

    /**
     * @return the area
     */
    public Area getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
     * @return the HabilitacionArea
     */
    public Boolean getHabilitacionArea() {
        return HabilitacionArea;
    }

    /**
     * @param HabilitacionArea the HabilitacionArea to set
     */
    public void setHabilitacionArea(Boolean HabilitacionArea) {
        this.HabilitacionArea = HabilitacionArea;
    }

    /**
     * @return the visible
     */
    public Boolean getVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    


    
    
}


package com.artrans.bonos.servicios;

import com.artrans.bonos.entidades.Area;
import com.artrans.bonos.entidades.Desvio;
import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.Imagen;
import com.artrans.bonos.entidades.ProyectoTrafo;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.AreaRepositorio;
import com.artrans.bonos.repositorio.DesvioRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jgodoy
 */

@Service
public class DesvioServicio {
    
    //Repositorios
    @Autowired
    private AreaServicio areaServicio;
    @Autowired
    private AreaRepositorio areaRepo;
    @Autowired
    private DesvioRepositorio desvioRepositorio;
    @Autowired
    private ServicioImagen servicioImagen;
    @Autowired
    private NotificacionServicio notiServi;
    
    //ABM
    /*
    //Alta Desvio:
        Para crear un desvio, se debe:
            -Indicar el empleado Generador: este debe ser el usuario de calidad que se encuentre loggeado
            -Ingresar un proyecto: este proyecto debe venir del resultado de la busqueda del proyecto
            -Ingresar fecha de emision: la fecha debe ser automatica una vez que se crea el desvio
            -Ingresar empleado Responsable: para ello se debe enviar por el controlador un array list con los empleados, de ahi seleccionar 1 y enviarlo al servicio
            -Responsable de Gestion (Sector): Mandar el enum por el controlador y seleccionar 1;
            -ProcesoFabricacion: Mandar el enum por el controlador y seleccionar 1;
            -TrafoPartes: Mandar el enum por el controlador y seleccionar 1;
            -CausaIshikawa: Mandar el enum por el controlador y seleccionar 1;
            -efectosCalidad: Mandar el enum por el controlador y seleccionar 1;
            -Estado: Iniciado por defecto cuando se crea el desvio;
            -Planta: Mandar el enum por el controlador y seleccionar 1;
            -Observacion Calidad: ingresa la observacion el usuario de calidad, restring la cantidad de caracteres para que no pongan pocas palabras.
            -Lista de Imagenes 
            -Habilitado: por defecto True;
    */
    
    @Transactional
    public Desvio crearDesvio(Area area,Empleado generador,ProyectoTrafo proyecto, String procesoFab, String trafoPartes, String causaPrincipal,
                                        String efectosCalidad, String planta, String observacionCalidad, List<Imagen> imagenes) throws ErrorServicio{
        
        validacionesSectorCalidad(area,generador,proyecto,procesoFab,trafoPartes,causaPrincipal,efectosCalidad,planta,observacionCalidad,imagenes);
        //Sacar empleado RESPONSABLE
        try{
        
            Desvio desvio = new Desvio();
            desvio.setId(autoGeneradorId());
            desvio.setArea(area);
            desvio.setResponsable(buscarresponsableArea(area));
            desvio.setGenerador(generador);
            desvio.setProyecto(proyecto);
            desvio.setProcesoFab(procesoFab);
            desvio.setTrafoparte(trafoPartes);
            desvio.setCausaPrincipal(causaPrincipal);
            desvio.setEfectoscalidad(efectosCalidad);
            desvio.setPlanta(planta);
            desvio.setObservacionCalidad(observacionCalidad);
            desvio.setImagenes(imagenes);
            desvio.setHabilitado(false);
            desvio.setHabilitacionArea(false);
            desvio.setVisible(false);
            desvio.setEstado("Iniciado");
                         
            desvioRepositorio.save(desvio);
            return desvio;

        }catch(Exception e){
            throw new ErrorServicio(e.getMessage());
        }
    }
    
/*
   Modificacion:
        -Hacer metodo para modificar todo lo que se ingreso en el desvio
*/
   
    @Transactional
    public Desvio modificarDesvio(String id,Area area,Empleado generador,ProyectoTrafo proyecto, String procesoFab, String trafoPartes, String causaPrincipal,
                                        String efectosCalidad, String planta, String observacionCalidad) throws ErrorServicio{
    
        Optional<Desvio> respuesta = desvioRepositorio.findById(id);
        
        validacionesSectorCalidadModificacion(area,generador,proyecto,procesoFab,trafoPartes,causaPrincipal,efectosCalidad,planta,observacionCalidad);
        
        if(respuesta.isPresent()){
            
            Desvio desvio = respuesta.get();
            desvio.setArea(area);
            desvio.setResponsable(buscarresponsableArea(area));
            desvio.setGenerador(generador);
            desvio.setProyecto(proyecto);
            desvio.setProcesoFab(procesoFab);
            desvio.setTrafoparte(trafoPartes);
            desvio.setCausaPrincipal(causaPrincipal);
            desvio.setEfectoscalidad(efectosCalidad);
            desvio.setPlanta(planta);
            desvio.setObservacionCalidad(observacionCalidad);
            desvio.setHabilitado(false);
            desvio.setHabilitacionArea(false);
            desvio.setVisible(false);
            desvio.setEstado("Modificado");
            
            desvioRepositorio.save(desvio);
            
            
        }else{
            throw new ErrorServicio("No se encontro el desvio solicitado");
        }

        return null;
    }
    
    /*
        Buscardor Responsable Area
    */
    public Empleado buscarresponsableArea(Area area) throws ErrorServicio{
        
        Optional<Area> resultado = areaRepo.findById(area.getUnidadOrganizacionalCodi());
        
        try{
            
            if(resultado.isPresent()){
            
                Empleado responsable = areaRepo.buscarJefeArea(area.getUnidadOrganizacionalCodi());
                if(responsable != null){
                    return responsable;
                }else{
                    throw new ErrorServicio("El Area no tiene un Responsable Asignado.  Comuniquese con el Administrador.");
                }    
            }           
        }catch(Exception e){
            throw new ErrorServicio("El Area no tiene un Responsable Asignado. Comuniquese con el Administrador.");
        }
        return null;
    }
    
    
    
    
    @Transactional
    public String eliminarDesvio(String id) throws ErrorServicio{
    
        Optional<Desvio> respuesta = desvioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
        
            try{
                Desvio desvio = respuesta.get();
                
                desvioRepositorio.delete(desvio);
                
                return "Desvio eliminado Correctamente.";
                
                
            }catch(Exception e){
                throw new ErrorServicio("No se pudo eliminar el desvio.");
            }       
        }else{
            return "Hubo un error al eliminar este desvio.";
        }
        
        
    }
    
    
    @Transactional
    public String EnviarDesvio(String idDesvio) throws ErrorServicio{
        //sI habilitado = true, la persona responsable puede modificarlo
        System.out.println(idDesvio);
        try{
            Optional<Desvio> respuesta = desvioRepositorio.findById(idDesvio);
            if(respuesta.isPresent()){
                Desvio desvio = respuesta.get();
            
                desvio.setFechaEmision(new Date());
                desvio.setEstado("Enviado");  
                desvio.setHabilitacionArea(true);
                desvio.setVisible(true);
                
                
                Empleado empleado = desvio.getArea().getEncargado();
                
                String subject = "APM|Calidad Notificación: Desvio Nro. "+desvio.getId()+" Generado.";
                String text = "Se generó el Desvío número "+desvio.getId()+".\n"
                + "\n"
                + " - Área: "+desvio.getArea().getNombre()+".\n"
                + "\n"
                + " - Proyecto: \n"
                + "     -Nota de Venta: "+desvio.getProyecto().getNotadeVenta()+" \n"
                + "     -Cliente: "+desvio.getProyecto().getCliente()+" \n"
                + "     -Descripción: "+desvio.getProyecto().getDescripcion()+" \n"
                + "Nota: Por favor, realizar el informe de Disposicion Inmediata, recordar que tiene un plazo de vencimiento de 24hs."
                + "Luego proceder a cargar la información solicitada en el desvìo.\n"
                + "Ante cualquier duda, comunicarse con el departamento de Calidad."
                + "\n"
                + "\n"
                + "Departamento de Calidad @Artrans.SA\n";

                notiServi.notificacionDesvioEnviado(empleado,subject,text);




                desvioRepositorio.save(desvio);
                
                return "Desvio Enviado.";
            }else{
                return "Error al enviar el desvio";
            }
           
        //MAIL SENDER NOTIFICACION RESPONSABLE;
        }catch(Exception e){
            throw new ErrorServicio("Hubo un error al enviar el desvio");
        }
    }
    
    @Transactional
    public String CargarDisposicion(String idDesvio, String disposicion) throws ErrorServicio{

        try{
            Desvio desvio = desvioRepositorio.getOne(idDesvio);
            if (disposicion == null || disposicion.isEmpty()) {
                throw new ErrorServicio("Debe Ingresar una disposicion Inmediata"); 
            }else{
                desvio.setDisposicionInmediata(disposicion);
                desvio.setFechaDisposicion(new Date()); 
                desvio.setEstado("Checkeado");
                
                
                String subject = "APM|Calidad Atención: Desvio Nro. "+desvio.getId()+" Evaluado.";
                String text = "Se cargo la Disposicion Inmediata del Desvío número "+desvio.getId()+".\n"
                + " \n"
                + " - Área: "+desvio.getArea().getNombre()+".\n"
                + " \n"
                + " - Proyecto: \n"
                + "     -Nota de Venta: "+desvio.getProyecto().getNotadeVenta()+" \n"
                + "     -Cliente: "+desvio.getProyecto().getCliente()+" \n"
                + "     -Descripción: "+desvio.getProyecto().getDescripcion()+" \n"
                + "     -Proceso Involucrado: "+desvio.getProcesoFab()+" \n"
                + "     -Trafo-Parte: "+desvio.getTrafoparte()+" \n"
                + " \n"
                + " - DISPOSICION INMEDIATA:\n"
                + "     "+disposicion+" \n"
                + " \n"        
                + "   Responsable: "+desvio.getResponsable().getNombre()+" "+desvio.getResponsable().getApellido()+".\n"
                + " \n"        
                + "Nota: Ante cualquier duda, comunicarse con el responsable de Área correspondiente al Desvio.\n"
                + " \n"
                + "Departamento de Calidad\n "
                + "@Artrans.SA\n";
                
                Empleado calidad = new Empleado();
                calidad.setEmailLab("jmaldonado@artrans.com.ar");
                Empleado planeamiento = new Empleado();
                planeamiento.setEmailLab("jquilici@artrans.com.ar");
                Empleado calidad2 = new Empleado();
                calidad2.setEmailLab("nalaniz@artran.com.ar");
                
                notiServi.notificacionDisposicion(calidad,calidad2,planeamiento,subject, text);
                desvioRepositorio.save(desvio);
                
                return "Disposición Inmediata cargada correctamente.";
            }
        }catch(ErrorServicio e){
            throw new ErrorServicio("Error al cargar disposicion.");
        }
    }
    
    @Transactional
    public String Aprobar(String idDesvio){
        
        Desvio desvio = desvioRepositorio.getOne(idDesvio);
        desvio.setEstado("Cerrado");
        desvio.setHabilitacionArea(false);

        desvioRepositorio.save(desvio);
        
        String subject = "APM|Calidad Notificación: Desvio Nro. "+desvio.getId()+" Cerrado.";
        String text = "Se cerró el Desvío número "+desvio.getId()+".\n"
                + "\n"
                + " - Área: "+desvio.getArea().getNombre()+".\n"
                + "\n"
                + " - Proyecto: \n"
                + "     -Nota de Venta: "+desvio.getProyecto().getNotadeVenta()+" \n"
                + "     -Cliente: "+desvio.getProyecto().getCliente()+" \n"
                + "     -Descripción: "+desvio.getProyecto().getDescripcion()+" \n"
                + "\n"
                + "Gracias por su Respuesta!."
                + "\n"
                + "Departamento de Calidad @Artrans.SA\n";    
        
        Empleado empleado = desvio.getArea().getEncargado();
          
        notiServi.notificacionDesvioEnviado(empleado, subject, text);
            
        //NOTIFICACION APROBADO;
        return "Desvio Aprobado!";
    }
    
    @Transactional
    public String Desaprobado(String idDesvio, String observacionCorrecion) throws ErrorServicio{
        
        Desvio desvio = desvioRepositorio.getOne(idDesvio);
        desvio.setEstado("Corregir");
        
        if(observacionCorrecion == null || observacionCorrecion.isEmpty()){        
             throw new ErrorServicio("Debe Ingresar una Observacion de Correccion");
        }else{
            desvio.setObservacionCorreccion(observacionCorrecion);
            desvio.setHabilitacionArea(true);
            desvioRepositorio.save(desvio);
            
            String subject = "APM|Calidad Notificación: Desvio Nro. "+desvio.getId()+" Para Revisión.";
            String text = "Se Realizó una observación en el Desvío número "+desvio.getId()+".\n"
                    + "\n"
                    + " - Área: "+desvio.getArea().getNombre()+".\n"
                    + "\n"
                    + " - Proyecto: \n"
                    + "     -Nota de Venta: "+desvio.getProyecto().getNotadeVenta()+" \n"
                    + "     -Cliente: "+desvio.getProyecto().getCliente()+" \n"
                    + "     -Descripción: "+desvio.getProyecto().getDescripcion()+" \n"
                    + "\n"
                    + " - OBSERVACIÓN:\n"
                    + "     "+observacionCorrecion+" \n"
                    + " \n" 
                    + "Nota: Por Favor, proceda a realizar las correcciones sugeridas y enviar el desvio nuevamente."
                    + " \n" 
                    + "Departamento de Calidad @Artrans.SA\n";    
        
            Empleado empleado = desvio.getArea().getEncargado();
          
            notiServi.notificacionDesvioEnviado(empleado, subject, text);
        }
        
        //NOTIFICACION DESAPROBADO//
        return "Desvio Desaprobado y enviado al responsable de seguimiento.";
    }
    
    
    
    //Mostrar Todos los desvios
    
    public List<Desvio> mostrarTodos(){
    
        List<Desvio> desvios = desvioRepositorio.findAll();
        
        if(!(desvios.isEmpty())){
            return desvios;
        }
        
        return null;
    }

    /*
    --------------------------------------------------------------------------------------------------
    */
        
    /*
        Responder Desvio: 
    */
    
    /*
        El desvio se genera a un Area, esta area tiene un responsable, este responsable puede Cargar el desvio o lo puede asignar a otro responsable
        para ello utilizo los boolean de habilitacion,
        El responsable contesta el desvio, calidad lo recibe, lo evalua , si esta bien lo cierra sino, lo desaprueba, lo observa y lo vuelve a enviar,
        si el desvio esta desaprobado, el responsable de Area ve que esta desaprobado, le debe llegar una notificacion a ambos que el desvio esta desaprobado
        la disposicion inmediata la debe cargar el responsable del Area, 
        El responsable de Area puede modificar la asignacion del Responsable de desvio,
        
    */
    
    @Transactional
    public String asignarResponsable(String id,Empleado responsable) throws ErrorServicio{
    
        try{
        Optional<Desvio> respuesta = desvioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            
            Desvio desvio = respuesta.get();
            
            desvio.setResponsable(responsable);
            desvio.setEstado("Asignado");
            desvio.setHabilitado(true);
            //Mail sender al responsable, notificacion del Desvio;
            
            
            String subject = "APM|Calidad Notificación: Desvio Nro. "+desvio.getId()+" Asignado.";
            String text = "Se le Asignó el Desvío número "+desvio.getId()+".\n"
                + "\n"
                + " - Área: "+desvio.getArea().getNombre()+".\n"
                + "\n"
                + " - Proyecto: \n"
                + "     -Nota de Venta: "+desvio.getProyecto().getNotadeVenta()+" \n"
                + "     -Cliente: "+desvio.getProyecto().getCliente()+" \n"
                + "     -Descripción: "+desvio.getProyecto().getDescripcion()+" \n"
                + "Nota: Por Favor, proceda a cargar la información correspondiente solicitada en este Desvío.\n"
                + "\n"
                + "Departamento de Calidad @Artrans.SA\n";
            
            if(responsable.getEmailLab().isEmpty()){
                return "El responsable asignado no tiene registrado un email laboral.";
            }else{
                notiServi.notificacionDesvioEnviado(responsable,subject,text);
                desvioRepositorio.save(desvio);
                return "Responsable Asignado Exitosamente.";
            } 
        }else{
            return "Error al Asignar el Responsable";
        }
        }catch(Exception e){
            throw new ErrorServicio("Error del servidor, intente nuevamente.");      
        }
    }
    
    
    
    @Transactional
    public String responderDesvio(String id, String causaRaiz, String analisisDeCausa, Integer HorasRetrabajo, String accionCorrectiva) throws ErrorServicio{
        
        validacionesRespuesta(causaRaiz,analisisDeCausa,HorasRetrabajo,accionCorrectiva);
        
        try{
        
        Optional<Desvio> respuesta = desvioRepositorio.findById(id);
        
            if(respuesta.isPresent()){

                Desvio desvio = respuesta.get();
                
                desvio.setCausaRaiz(causaRaiz);
                desvio.setAnalisisDeCausa(analisisDeCausa);
                desvio.setHorasRetrabajo(HorasRetrabajo);
                desvio.setAccionCorrectiva(accionCorrectiva);
                desvio.setFechaRespuesta(new Date());
                desvio.setEstado("Verificar");
                desvio.setHabilitacionArea(false);
                desvioRepositorio.save(desvio);
                
                String subject = "APM|Calidad Notificación: Desvio Nro. "+desvio.getId()+" Respondido.";
                String text = "Se le Respondio el Desvío número "+desvio.getId()+".\n"
                + "\n"
                + " - Área: "+desvio.getArea().getNombre()+".\n"
                + "\n"
                + " - Proyecto: \n"
                + "     -Nota de Venta: "+desvio.getProyecto().getNotadeVenta()+" \n"
                + "     -Cliente: "+desvio.getProyecto().getCliente()+" \n"
                + "     -Descripción: "+desvio.getProyecto().getDescripcion()+" \n"
                + "Nota: Por Favor, proceda a realizar el control correspondiente.\n"
                + "\n"
                + "Asistente Departamento de Calidad @Artrans.SA\n";
                
                Empleado calidad = new Empleado();
                calidad.setEmailLab("jmaldonado@artrans.com.ar");
                Empleado calidad2 = new Empleado();
                calidad2.setEmailLab("nalaniz@artrans.com.ar");
                
                //Cuando el desvio es respondido la notificacion es enviada a las 2 personas de Calidad
                
                notiServi.notificacionRespuesta(calidad,calidad2, subject, text);
                
                return "Informacion Cargada con exito!.";
                
            }else{
                return "No se encontro el desvio";
            }
        }catch(Exception e){
            System.out.println(e);
            throw new ErrorServicio("No se pudo cargar la informacion ingresada, intente nuevamente.");
            
        }  
    }
    
    
    
    
    
    private void  validacionesSectorCalidad(Area area,Empleado generador,ProyectoTrafo proyecto, String procesoFab, String trafoPartes, String causaPrincipal,
                                        String efectosCalidad, String planta, String observacionCalidad, List<Imagen> imagenes) throws ErrorServicio{
        
        if (area == null || !(area.getBaja()==false)) {        
            throw new ErrorServicio("El area no existe o esta dada de Baja.");
        }
        if (generador == null || !(generador.isHabilitado())) {        
            throw new ErrorServicio("El usuario no esta Habilitado");
        }
        if(proyecto == null){
           throw new ErrorServicio("Hubo un error al Cargar el proyecto");
        }
        if(procesoFab == null){
            throw new ErrorServicio("Debe seleccionar el proceso de Fabricacion relacionado con el desvio");
        }
        if(trafoPartes == null){
            throw new ErrorServicio("Debe seleccionar una trafoparte involucrada al desvio");
        }
        if(causaPrincipal == null){
            throw new ErrorServicio("Debe seleccionar la Causa Ishikawa relacionada al desvio");
        }
        if(efectosCalidad == null){
            throw new ErrorServicio("Debe seleccionar un efecto sobre la calidad de producto");
        }
        if(planta == null){
            throw new ErrorServicio("Debe seleccionar la Planta donde se produjo el desvio");
        }
        if(observacionCalidad == null || observacionCalidad.length() < 1 || observacionCalidad.isEmpty()){
            throw new ErrorServicio("Debe ingresar una observacion mas detallada");
        }
    }
    
    private void  validacionesSectorCalidadModificacion(Area area,Empleado generador,ProyectoTrafo proyecto, String procesoFab, String trafoPartes, String causaPrincipal,
                                        String efectosCalidad, String planta, String observacionCalidad) throws ErrorServicio{
    
        if (area == null || !(area.getBaja()==false)) {        
            throw new ErrorServicio("El area no existe o esta dada de Baja.");
        }
        if (generador == null || !(generador.isHabilitado())) {        
            throw new ErrorServicio("El usuario no esta Habilitado");
        }
        if(proyecto == null){
           throw new ErrorServicio("Hubo un error al Cargar el proyecto");
        }
        if(procesoFab == null){
            throw new ErrorServicio("Debe seleccionar el proceso de Fabricacion relacionado con el desvio");
        }
        if(trafoPartes == null){
            throw new ErrorServicio("Debe seleccionar una trafoparte involucrada al desvio");
        }
        if(causaPrincipal == null){
            throw new ErrorServicio("Debe seleccionar la Causa Ishikawa relacionada al desvio");
        }
        if(efectosCalidad == null){
            throw new ErrorServicio("Debe seleccionar un efecto sobre la calidad de producto");
        }
        if(planta == null){
            throw new ErrorServicio("Debe seleccionar la Planta donde se produjo el desvio");
        }
        if(observacionCalidad == null || observacionCalidad.length() < 1 || observacionCalidad.isEmpty()){
            throw new ErrorServicio("Debe ingresar una observacion mas detallada");
        }
    }
    
    
    
    
    private void validacionesRespuesta(String causaRaiz,String analisisDeCausa, Integer HorasRetrabajo,
            String accionCorrectiva) throws ErrorServicio{
        
        if(causaRaiz == null || causaRaiz.isEmpty()){
            throw new ErrorServicio("Debe seleccionar una causa Raiz, si no se encuentra en la lista, seleccione 'Otras' y detalle en analisis de Causa");
        }
    
        if (analisisDeCausa == null || analisisDeCausa.isEmpty()) {
            throw new ErrorServicio("Debe Ingresar un analisis de causa"); 
        }
        
        if (analisisDeCausa.length()< 1) {
             throw new ErrorServicio("Debe Ingrear un analsis de causa mas detallado");   
        }

        if (HorasRetrabajo == 0) {
            throw new ErrorServicio("Debe Ingresar una cantidad de Horas de Retrabajo");
        }
        
        if (accionCorrectiva == null || accionCorrectiva.isEmpty()) {
            throw new ErrorServicio("Debe Ingresar un analisis de causa"); 
        }
        
        if (accionCorrectiva.length()< 1) {
             throw new ErrorServicio("Debe Ingresar la accion correctiva de forma mas detallada");   
        }
    }
    
    
    public List<Imagen> crearAlbum(MultipartFile archivo) throws ErrorServicio{
    
        List<Imagen> AlbumDesvio = new ArrayList<>();
        
        try{
            
            AlbumDesvio.add(servicioImagen.guardar(archivo));
        
        }catch(Exception e){
            
            throw new ErrorServicio("No se pudo cargar la foto al album de fotos");
        }
    
        return AlbumDesvio;
    } 
    
    
    public List<Imagen> modificarAlbum(MultipartFile archivo, String idDesvio, String idImagen) throws ErrorServicio, IOException{
    
        List<Imagen> imagenes = servicioImagen.ImagenesDesvio(idDesvio);
        
        for(Imagen i : imagenes){
            if(i.getId().equals(idImagen)){           
                servicioImagen.Modificar(archivo, idImagen);
            }
        
        }
        
      return imagenes;  
    
    }
    
    private String autoGeneradorId(){
        
        String idAuto;
        Integer idAux = 0;
        
        List<Desvio> desvios = desvioRepositorio.findAll();
        
        if(desvios.isEmpty()){
            idAuto= "2000";
            return idAuto;
        }
        
        
        for(Desvio d: desvios){
              
            Integer idAux1 = Integer.parseInt(d.getId());
            
            if(idAux1 > idAux){
                idAux = idAux1;
            }
        }
        
        idAux++;
        
        idAuto = (idAux).toString();
        
        return idAuto; 
    }
    
    
    //@Scheduled(cron = "00 00 00 * * *")
    @Transactional
    public void RegistroVencimientos(){
    
        //Tener en cuenta los dias habiles y los fin de semanas
        
        List<Desvio> desvios = desvioRepositorio.findAll();
        Date hoy = new Date();
        /*
        El desvio posee las siguientes fechas:
        
            -Fecha emision : null, el desvio se encuentra iniciado pero no enviado (ESTADO: "Iniciado")
            -Fecha emision : una vez que el usuario de calidad presiona la tecla enviar,(ESTADO : "Enviado")
                -Si la persona de calidad modifica el desvio, cambia el estado "Enviado" y se vuelve a setear la fecha de Emision
            -Fecha RespuestaInmediata: null, (Si fecha RespuestaInmediata=null y (hoy - FechaEmision > 24hs), Desvio Vencido, enviar notificacion (ESTADO: "Vencido") 
        
        */    
        for(Desvio d : desvios){
            if(d.getFechaDisposicion() == null){
              if((hoy.getTime() - d.getFechaEmision().getTime()) > 24*3600*1000){
                    d.setEstado("Vencido");
                    //ENVIAR NOTIFICACION;
                }  
            } 
        }
    }
    
    
}

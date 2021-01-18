/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.servicios;


import com.artrans.bonos.entidades.Empleado;

import java.util.Locale;
import java.util.Optional;

import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.repositorio.NotificacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

/**
 *
 * @author chiri
 */
@Service
public class NotificacionServicio {
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;

    @Autowired
    private NotificacionRepositorio notificacionRepositorio;


    //TERMINAR LOGICA DE NEGOCIO DE NOTIFICACION

    public void activarNotificacion(String idEmpleado,Boolean estado){

        try {

            Optional respuesta = empleadoRepositorio.findById(idEmpleado);
            if(respuesta.isPresent()){

                Empleado empleado = (Empleado) respuesta.get();


            }


        }catch(Exception err){

        }



    }







    @Async
    public void enviar(String cuerpo,String titulo, String mail){
         
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("no-reply@artrans.com.ar");
        mensaje.setTo(mail);
        mensaje.setSubject(titulo);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }

    @Async
    public void enviar(SimpleMailMessage mensaje){
        mailSender.send(mensaje);
    }
    
    @Async
    public void notificacionDesvioEnviado(Empleado empleado, String subject, String text){
        
        SimpleMailMessage notificacion = new SimpleMailMessage();
        notificacion.setFrom("no-reply@artrans.com.ar");
        notificacion.setTo(empleado.getEmailLab());
        notificacion.setSubject(subject);
        notificacion.setText(text);
        mailSender.send(notificacion);

        //Notificacion automatica a Calidad, Planeamiento e Interesados





    }
    
     @Async
     public void notificacionRespuesta(Empleado calidad, Empleado calidad2, String subject, String text){
     
        SimpleMailMessage notificacion = new SimpleMailMessage();
        notificacion.setFrom("no-reply@artrans.com.ar");
        notificacion.setTo(calidad.getEmailLab());
        notificacion.setSubject(subject);
        notificacion.setText(text);
        mailSender.send(notificacion);
        notificacion.setTo(calidad2.getEmailLab());
        mailSender.send(notificacion);

         //Notificacion automatica a Calidad, Planeamiento e Interesados

         
     }
    
     
    @Async
    public void notificacionDisposicion(Empleado Calidad, Empleado calidad2, Empleado planeamiento, String subject, String text){
        System.out.println("Mando Mail disposicion inmediata");
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("no-reply@artrans.com.ar");
        mensaje.setTo(Calidad.getEmailLab());
        mensaje.setSubject(subject);
        mensaje.setText(text);
        mailSender.send(mensaje);
        mensaje.setTo(planeamiento.getEmailLab());
        mailSender.send(mensaje);
        mensaje.setTo(calidad2.getEmailLab());
        mailSender.send(mensaje);
        //Aca van los que quieran estar en copia de los mails que envia el sistema
        //El dia que se lanzen no conformidades entre sectores deberian estar aca los que las lanzaron

        //Notificacion automatica a Calidad, Planeamiento e Interesados

        
    }

    
 public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, Empleado empleado) {
    String url = contextPath + "/user/changePassword" + token;
    String message = "message.resetPassword";
    return constructEmail("Reset Password", message + " \r\n" + url, empleado);
}
 
private SimpleMailMessage constructEmail(String subject, String body, Empleado empleado) {
    SimpleMailMessage email = new SimpleMailMessage();
    email.setSubject(subject);
    email.setText(body);
    email.setTo(empleado.getEmail());
    email.setFrom("no-reply@artrans.com.ar");
    return email;
}


}


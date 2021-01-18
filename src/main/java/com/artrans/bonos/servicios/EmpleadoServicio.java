/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.servicios;


import com.artrans.bonos.entidades.Bono;
import com.artrans.bonos.entidades.Empleado;
import com.artrans.bonos.entidades.PasswordResetToken;
import com.artrans.bonos.enun.Permisos;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.BonoRepositorio;
import com.artrans.bonos.repositorio.EmpleadoRepositorio;
import com.artrans.bonos.repositorio.PasswordResetTokenRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmpleadoServicio implements UserDetailsService {

    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;
    @Autowired
    private BonoServicio bonoServicio;
    @Autowired
    private NotificacionServicio notificacionServicio;
    @Autowired
    private PasswordResetTokenRepositorio passwordResetTokenRepositorio;
    @Autowired
    private BonoRepositorio bonoRepositorio;


    @Transactional
    public Empleado guardar(String dni, String nombre, String apellido, String email, String clave, String clave2) throws ErrorServicio {

        verificar(dni, nombre, apellido, email, clave, clave2);
        
        unico(email, dni);
                  
        try {
             
            Empleado empleado = new Empleado();
            empleado.setApellido(apellido);
            empleado.setNombre(nombre);
            empleado.setDni(dni);
            empleado.setEmail(email);
            empleado.setPermiso(Permisos.EMPLEADO);
            empleado.setHabilitado(false);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            empleado.setClave(encriptada);
            empleadoRepositorio.save(empleado);
            notificacionServicio.enviar("Bienvenido al sistema de Bonos de Sueldo Automatico", "Bienvenido", email);
                
           
        } catch (Exception e) {
            throw new ErrorServicio("no se creo el empleado");
        }

        return null;
    }

    @Transactional
    public Empleado editar(String id, String email, String nombre, String apellido,String emaillab, String clave, String clave2) throws ErrorServicio {

        verificar( nombre, apellido, email, clave, clave2);

        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            empleado.setApellido(apellido);
            empleado.setNombre(nombre);
            empleado.setEmail(email);
                String encriptada = new BCryptPasswordEncoder().encode(clave);
                empleado.setClave(encriptada);
            if (!emaillab.isEmpty()) {
                empleado.setEmailLab(emaillab);
            }
            empleadoRepositorio.save(empleado);
            //notificacionServicio.enviar("Tus datos fueron actualizads", "Felicidades", email);

        } else {
            throw new ErrorServicio("no se no se encontro el empleado");
        }

        return null;
    }
     @Transactional
    public Empleado editar(String id, String email, String nombre, String apellido,String emaillab) throws ErrorServicio {

        verificar( nombre, apellido, email);

        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            empleado.setApellido(apellido);
            empleado.setNombre(nombre);
            empleado.setEmail(email);
            if (!emaillab.isEmpty()) {
                empleado.setEmailLab(emaillab);
            }
            empleadoRepositorio.save(empleado);
            //notificacionServicio.enviar("Tus datos fueron actualizads", "Felicidades", email);

        } else {
            throw new ErrorServicio("no se no se encontro el empleado");
        }

        return null;
    }
     @Transactional
    public Empleado editar(String id, String email, String nombre, String apellido,String dni, String tipo,String emaillab, Boolean habilitado) throws ErrorServicio {
          
        verificar(nombre, apellido, email);

        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            empleado.setApellido(apellido);
            empleado.setNombre(nombre);
            empleado.setEmail(email);
            empleado.setDni(dni);
            empleado.setHabilitado(habilitado);
            switch (tipo){
                case "SUPERVISOR":
                    empleado.setPermiso(Permisos.SUPERVISOR);
                    break;
                case "EMPLEADO":
                    empleado.setPermiso(Permisos.EMPLEADO);
                    break;
                case "CALIDAD":
                    empleado.setPermiso(Permisos.CALIDAD);
                    break;
                case "ADMINAREA":
                    empleado.setPermiso(Permisos.ADMINAREA);
                    break;
                case "PLANEAMIENTO":
                    empleado.setPermiso(Permisos.PLANEAMIENTO);
                    break;
                default:
                    empleado.setPermiso(Permisos.EMPLEADO);
            }
             
            if (!emaillab.isEmpty()) {
                empleado.setEmailLab(emaillab);
            }
            empleadoRepositorio.save(empleado);

        } else {
            throw new ErrorServicio("no se no se encontro el empleado");
        }

        return null;
    }
    

    @Transactional
    public Empleado guardarBono(String id, String anio, String mes, MultipartFile archivo) throws ErrorServicio {

        verificar(anio, mes, archivo);

        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Empleado empleado = respuesta.get();
            System.out.println(anio);
            System.out.println(mes);
            Bono bono = bonoRepositorio.buscarBonosPorNombre(archivo.getOriginalFilename());
            if (bono != null) {
                empleado.borrarUnBono(bono);
                empleado.guardarUnBono(bonoServicio.actualizar(bono.getId(),archivo, mes, anio));
                empleadoRepositorio.save(empleado);
            }else{
            empleado.guardarUnBono(bonoServicio.guardar(archivo, mes, anio));
            empleadoRepositorio.save(empleado);
            }
        } else {
            throw new ErrorServicio("no se no se encontro el bono");
        }

        return null;
    }


    @Transactional
    public Empleado buscarPorDNI(String dni) throws ErrorServicio {

        Empleado empleado = empleadoRepositorio.buscarDni(dni);
        if (empleado != null) {
           
            return empleado;
        }

        return null;
    }

    public void verificar( String nombre, String apellido, String email, String clave, String clave2) throws ErrorServicio {

        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("EL mail no puede estar vacio");

        }
        if (clave == null || clave.isEmpty() || !clave.equals(clave2) || clave.length() < 6) {
            throw new ErrorServicio("Clave no valida o indistintas");

        }
     
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("EL nombre no puede estar vacio");

        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("EL apellido no puede estar vacio");

        }

    }
        public void verificar( String nombre, String apellido, String email) throws ErrorServicio {

        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("EL mail no puede estar vacio");

        }

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("EL nombre no puede estar vacio");

        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("EL apellido no puede estar vacio");

        }

    }

    public void verificar(String anio, String mes, MultipartFile archivo) throws ErrorServicio {

        if (archivo == null) {
            throw new ErrorServicio("EL archivo no puede estar vacio");

        }
        if (anio == null || anio.isEmpty()) {
            throw new ErrorServicio("EL anio no puede estar vaio");

        }
        if (mes == null || mes.isEmpty()) {
            throw new ErrorServicio("EL mes no puede estar vacio");
        }
    }
    
    public void unico(String mail, String dni) throws ErrorServicio{
    
           Empleado empleado = empleadoRepositorio.buscarDni(dni);
           if (empleado != null ) {
            throw new ErrorServicio("El Empleado con ese DNI ya esta registrado");
           }
           
           empleado = empleadoRepositorio.buscarEmail(mail);
           if (empleado != null) {
            throw new ErrorServicio("El Empleado con ese E-mail ya esta registrado");
           }
       
    }
      public void verificar(String dni, String nombre, String apellido, String email, String clave, String clave2) throws ErrorServicio {

        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("EL mail no puede estar vacio");

        }
        if (clave == null || clave.isEmpty() || !clave.equals(clave2) || clave.length() < 6) {
            throw new ErrorServicio("Clave no valida o indistintas");

        }
            if (dni == null || clave.isEmpty() || !dni.equals(clave2) || dni.length() < 6) {
            throw new ErrorServicio("dni no valida o nula");

        }
     
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("EL nombre no puede estar vacio");

        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("EL apellido no puede estar vacio");

        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepositorio.buscarEmail(email);
        if (empleado != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();
            if (empleado.getPermiso().equals(Permisos.SUPERVISOR)) {
                GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_BONO");
                permisos.add(p1);
            }

            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_EMPLEADO");
            permisos.add(p2);
            
            ServletRequestAttributes attr =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuario", empleado);
            User user = new User(empleado.getEmail(), empleado.getClave(), permisos);
            return user;

        } else {
            return null;
        }
    }

    public void createPasswordResetTokenForUser(Empleado empleado, String token) {
    PasswordResetToken myToken = new PasswordResetToken(token, empleado);
    passwordResetTokenRepositorio.save(myToken);
}
    
    
}



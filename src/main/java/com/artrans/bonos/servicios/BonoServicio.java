package com.artrans.bonos.servicios;

import com.artrans.bonos.entidades.Bono;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.BonoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BonoServicio {

    @Autowired
    private BonoRepositorio bonoRepositorio;

    @Transactional
    public Bono guardar(MultipartFile archivo, String mes, String anio) throws ErrorServicio {

        verificar(archivo, mes, anio);

        try {
            Bono bono = new Bono();
            bono.setMime(archivo.getContentType());
            bono.setNombre(archivo.getOriginalFilename());
            bono.setContenido(archivo.getBytes());
            bono.setAnio(anio);
            bono.setMes(mes);
            
            return bonoRepositorio.save(bono);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Transactional
    public Bono actualizar(String id, MultipartFile archivo, String mes, String anio) throws ErrorServicio {

        verificar(archivo, mes, anio);

        Optional<Bono> respuesta = bonoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            try {
                Bono bono = respuesta.get();
                bono.setMime(archivo.getContentType());
                bono.setNombre(archivo.getOriginalFilename());
                bono.setContenido(archivo.getBytes());

                return bonoRepositorio.save(bono);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
        return null;
    }

    @Transactional
    public ResponseEntity<byte[]> traer(String id) throws ErrorServicio {

        Optional<Bono> respuesta = bonoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            try {

                Bono bono = respuesta.get();
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ bono.getNombre()+"\"")
                        .contentType(MediaType.parseMediaType(bono.getMime()))
                        .body(bono.getContenido());
            } catch (Exception e) {
                throw new ErrorServicio("no se encontro el archivo");
            }

        }
        return null;
    }

    @Transactional
    public void eliminar(String id) throws ErrorServicio {

        Optional<Bono> respuesta = bonoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            try {
                Bono bono = respuesta.get();
                bonoRepositorio.delete(bono);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }

    public List<Bono> bonosEmpleado(String email) throws ErrorServicio{
    
         List<Bono> bonos = new ArrayList<>();
         
        bonos = bonoRepositorio.buscarBonosPorMail(email);
                 
         return bonos;
    }
        
    
    
    
    public void verificar(MultipartFile archivo, String mes, String anio) throws ErrorServicio {

        if (archivo == null) {
            throw new ErrorServicio("EL archivo no puede estar vacio");

        }
        if (mes == null || mes.isEmpty()) {
            throw new ErrorServicio("EL mes no puede estar vaio");

        }
        if (anio == null || anio.isEmpty()) {
            throw new ErrorServicio("EL año no puede estar vacio");

        }

    }

}


package com.artrans.bonos.servicios;

import com.artrans.bonos.entidades.Imagen;
import com.artrans.bonos.errores.ErrorServicio;
import com.artrans.bonos.repositorio.ImagenRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jgodoy
 */
@Service
public class ServicioImagen {
    
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    
    @Transactional
    public Imagen guardar(MultipartFile archivo) throws ErrorServicio{
        
        verificar(archivo);
        
        try{
            Imagen imagen = new Imagen();
            imagen.setMime(archivo.getContentType());
            imagen.setContenido(archivo.getBytes());
            imagen.setName(archivo.getOriginalFilename());
            imagenRepositorio.save(imagen);
            return imagen;
              
        }catch(Exception e){
            throw new ErrorServicio("No se pudo Cargar el Archivo, Intente nuevamente");
        
        }
    }
    
    @Transactional
    public Imagen Modificar(MultipartFile archivo,String id) throws ErrorServicio{
        
        verificar(archivo);
        
            Optional<Imagen> respuesta = imagenRepositorio.findById(id);
            if(respuesta.isPresent()){
                try{        
                    Imagen imagen = respuesta.get();
                    imagen.setContenido(archivo.getBytes());
                    imagen.setMime(archivo.getContentType());
                    imagen.setName(archivo.getOriginalFilename());
                    imagenRepositorio.save(imagen);
                    return imagen;
                
                }catch(Exception e){
                    throw new ErrorServicio("No se pudo modificar la imagen.");
                }
            
            }
        return null;
    }
    
    @Transactional
    public void eliminar(String id) throws ErrorServicio {

        Optional<Imagen> respuesta = imagenRepositorio.findById(id);

        if (respuesta.isPresent()) {
            try {
                Imagen imagen = respuesta.get();
                imagenRepositorio.delete(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
    }
    
    
    
    public void verificar(MultipartFile archivo) throws ErrorServicio{
    
        if(archivo == null ){
            throw new ErrorServicio("No puede Cargar un archivo nulo.");
        }
    }
    
    
    
    
     public List<Imagen> ImagenesDesvio(String idDesvio) throws ErrorServicio{
    
         List<Imagen> imagenes = new ArrayList<>();
         
         imagenes = imagenRepositorio.buscarPorIdDesvio(idDesvio);
         
         return imagenes;
    }
     
    
    
}

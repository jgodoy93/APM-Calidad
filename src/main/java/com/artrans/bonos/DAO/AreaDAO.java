/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.DAO;

import com.artrans.bonos.entidades.Area;
import com.artrans.bonos.entidades.OrdenesDeTrabajo;
import com.artrans.bonos.repositorio.AreaRepositorio;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author chiri
 */
@Component("AreaDAO")
public class AreaDAO {
     @Autowired
     private AreaRepositorio areaRepositorio;
    
     public void conectar(){
        Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             String urls = "jdbc:sqlserver://200.80.241.187\\GLOBALIS:56141;databaseName=Delfos_PROD;user=java;password=Perfil27;";
             con = DriverManager.getConnection(urls);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select CAST([UnidadOrganizacionalCodi] as nchar(2))\n"
                    + "      ,CAST([Nombre] as nchar(16))\n"
                    + "      ,CAST ([FechaBaja] as nchar(1))\n"
                    + " FROM [Delfos_PROD].[dbo].[UnidadOrganizacional] ");
            while (rs.next()) {
                        Optional<Area> respuesta = areaRepositorio.findById(rs.getNString(1));
                        if (respuesta.isPresent()) {
                         Area obj = respuesta.get();
                         obj.setNombre(rs.getNString(2));
                         areaRepositorio.save(obj);
                        }else{
                        Area obj = new Area(rs.getNString(1),rs.getNString(2));
                        obj.setBaja(!(rs.getString(3) == null));
                        areaRepositorio.save(obj);
                        }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artrans.bonos.controladores;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author chiri
 */
@Controller
@RequestMapping("/")
public class PortalControlador {
    

    
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        return "login.html";
    }
    
    
        @GetMapping("/loginFallo")
        public String loginE(@RequestParam(required = false) String error, ModelMap model) {
            model.put("error", "Email o contraseña incrrecta");
        return "login.html";
    }
    

}

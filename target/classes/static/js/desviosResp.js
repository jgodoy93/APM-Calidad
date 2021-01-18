$(document).ready(function(){

    //CONSULTAR A RUBEN MASSI SI LA DISPOSICION INMEDIATA LA TIENE QUE DAR LOS ENCARGADOS DE SECTOR O EL RESPONSABLE DEL DESVIO
    
    /*
        -ARREGLAR SACAR EL CARTEL DE RESPONDER DESVIOS Y PONER EL CANDADITO DE DESVIO BLOQUEADO (VER DIFERENCIA ENTRE ADMIN AREA Y RESPONSABLE)
        -ORDENAR DESVIOS DE MAYOR A MENOR,

    */

    
    
    
    
    mostrarDesvios();
    responderDesvios();
    asignarResp();

});

function mostrarDesvios(){

    $('table #verDesvio').on('click',function(event){
        
        console.log("clickVer");
        event.preventDefault();
        var href = $(this).attr('href');

        $('#sp3').css({"display":"block"});

        $.ajax({
            type: "get",
            url: href,
        }).done(function(Desvio, status){
            //Modal ID: #DesvioVER
            $('#sp3').css({"display":"none"});
            console.log(Desvio);
                $('#ModalMostraTituloVER').text("Desvio Nro: "+Desvio.id);
                $("#nvMostrarVER").text(Desvio.proyecto.notadeVenta).css({"color":"teal"});
                
                if(!(Desvio.proyecto.nroSerie.substring(0,2) == '1 ')){
                    $('#clienteMostrar').css({"display":"inline"}); 
                    $("#nroSerieMostrar").css({"display":"inline"});
                    $('#nsDMostrarVER').text(Desvio.proyecto.nroSerie).css({"color":"teal"}); 
                    $("#cliMostrarVER").text(Desvio.proyecto.cliente).css({"color":"teal"});
                }else{
                    $('#clienteMostrar').css({"display":"none"}); 
                    $("#nroSerieMostrar").css({"display":"none"});
                }
                
                $('#desDMostrarVER').text(Desvio.proyecto.descripcion).css({"color":"teal"});
                $('#genMostrarVER').text(Desvio.generador.nombre+" "+Desvio.generador.apellido).css({"color":"teal"});
                $('#segMostrarVER').text(Desvio.responsable.nombre+" "+Desvio.responsable.apellido).css({"color":"teal"});
                $("#sectorMostarVER").text(Desvio.area.nombre).css({"color":"teal"});
                $("#plantaMostrarVER").text(Desvio.planta).css({"color":"teal"});
                $("#proDMostrarVER").text(Desvio.procesoFab).css({"color":"teal"});
                $("#tfDMostrarVER").text(Desvio.trafoparte).css({"color":"teal"});
                $("#isDMostrarVER").text(Desvio.causaPrincipal).css({"color":"teal"});
                $("#noCMostrarVER").text(Desvio.efectoscalidad).css({"color":"teal"});
                $("#obsMostrarVER").text(Desvio.observacionCalidad).css({"color":"teal"});

                Img = Desvio.imagenes;
                try{           
                    if(Img[0] == undefined){
                        $('#containerImagenes1').css({"display": "none"});
                        $('#msgImagenes').css({"display": "inline"});
                        $('#tituloImg').css({"display": "none"});
                    }else{
                        $('#containerImagenes1').css({"display": "inline"});
                        $('#myImg1VER').attr('src',`data:${Img[0].mime};base64,${Img[0].contenido}`);
                        $('#msgImagenes').css({"display": "none"});
                        $('#tituloImg').css({"display": "inline"});
                    }
                    if(Img[1] == undefined){
                        $('#containerImagenes2').css({"display": "none"});
                    }else{
                        $('#containerImagenes2').css({"display": "inline"});
                        $('#myImg2VER').attr('src',`data:${Img[1].mime};base64,${Img[1].contenido}`);
                    }
                    if(Img[2] == undefined){
                        $('#containerImagenes3').css({"display": "none"});
                    }else{
                        $('#containerImagenes3').css({"display": "inline"});
                        $('#myImg3VER').attr('src',`data:${Img[2].mime};base64,${Img[2].contenido}`);
                    }
                    if(Img[3] == undefined){
                        $('#containerImagenes4').css({"display": "none"});
                    }else{
                        $('#containerImagenes4').css({"display": "inline"});
                        $('#myImg4VER').attr('src',`data:${Img[3].mime};base64,${Img[3].contenido}`);
                    }
                }catch(Err){
                    console.log("Algunos desvios no tienen imagenes");
                }

                activarModal(document.getElementById('myModal1VER')
                ,document.getElementById('myImg1VER')
                ,document.getElementById('Img1VER')
                ,document.getElementById('closeAVER'));

                activarModal(document.getElementById('myModal2VER')
                ,document.getElementById('myImg2VER')
                ,document.getElementById('Img2VER')
                ,document.getElementById('closeBVER'));

                activarModal(document.getElementById('myModal3VER')
                ,document.getElementById('myImg3VER')
                ,document.getElementById('Img3VER')
                ,document.getElementById('closeCVER'));

                activarModal(document.getElementById('myModal4VER')
                ,document.getElementById('myImg4VER')
                ,document.getElementById('Img4VER')
                ,document.getElementById('closeDVER'));

                //Disposicion Inmediata
                if(Desvio.disposicionInmediata == null){
                    $('#DispoVER').text("Disposicion sin Cargar.") 
                }else{
                    $('#DispoVER').text(Desvio.disposicionInmediata).css({"color":"red"});
                }
                //Respuesta
                if(Desvio.fechaRespuesta == null){
                    $("#containerRespuestaVER").css("background-color", "lightgrey");
                    $("#verCausaR").css({"display":"none"});
                    $("#verHoras").css({"display":"none"});
                    $("#verAnalisis").css({"display":"none"});
                    $("#verAccion").css({"display":"none"});
                    $('#EstadoDesvio').text("Desvio sin Responder."); 
                }else{
                    $("#verCausaR").text(Desvio.causaRaiz).css({"color":"teal"});
                    $("#verHoras").text(Desvio.horasRetrabajo).css({"color":"teal"});
                    $("#verAnalisis").text(Desvio.analisisDeCausa).css({"color":"teal"});
                    $("#verAccion").text(Desvio.accionCorrectiva).css({"color":"teal"});
                }
                if(Desvio.estado == "Corregir"){
                    $("#ObservacionResp").css({"display":"block"});
                    console.log("desvio desaprobado");
                    $("#EstadoDesvioCal").text(Desvio.estado);
                    $('#ObservacionCalidad').text(Desvio.observacionCorreccion);
                }

        }).fail(function(Error){
            $('#modalAlertRes').modal();
            $('#TextModalAlertRes').text("Error del Servidor, Intente Nuevamente.");
        });
    
    })
    
        

};


function responderDesvios(){

    $('table #RespDesvio').on('click',function(event){
        console.log("click");
        event.preventDefault();
        var href = $(this).attr('href');

        console.log(href);
        $('#sp4').css({"display":"block"});

            $.ajax({
                type: "get",
                url: href,
            }).done(function(Desvio, status){
                $('#sp4').css({"display":"none"});

                console.log(Desvio);
                modalLabel = $('#ModalMostraTitulo').text("Desvio Nro: "+Desvio.id);
                nv = $("#nvMostrar").text(Desvio.proyecto.notadeVenta).css({"color":"teal"});
                
                if(!(Desvio.proyecto.nroSerie.substring(0,2) == '1 ')){
                    $('#cliMos').css({"display":"inline"}); 
                    $("#nsMos").css({"display":"inline"});
                    $('#nsDMostrar').text(Desvio.proyecto.nroSerie).css({"color":"teal"}); 
                    $("#cliMostrar").text(Desvio.proyecto.cliente).css({"color":"teal"});
                }else{
                    $('#cliMos').css({"display":"none"}); 
                    $("#nsMos").css({"display":"none"});
                }
                $('#desDMostrar').text(Desvio.proyecto.descripcion).css({"color":"teal"});
                $('#genMostrar').text(Desvio.generador.nombre+" "+Desvio.generador.apellido).css({"color":"teal"});
                $('#segMostrar').text(Desvio.responsable.nombre+" "+Desvio.responsable.apellido).css({"color":"teal"});
                $("#sectorMostar").text(Desvio.area.nombre).css({"color":"teal"});
                $("#plantaMostrar").text(Desvio.planta).css({"color":"teal"});
                $("#proDMostrar").text(Desvio.procesoFab).css({"color":"teal"});
                $("#tfDMostrar").text(Desvio.trafoparte).css({"color":"teal"});
                $("#isDMostrar").text(Desvio.causaPrincipal).css({"color":"teal"});
                $("#noCMostrar").text(Desvio.efectoscalidad).css({"color":"teal"});
                $("#obsMostrar").text(Desvio.observacionCalidad).css({"color":"teal"});

                Img = Desvio.imagenes;

               try{           
                if(Img[0] == undefined){
                    $('#contImagenes1').css({"display": "none"});
                    $('#msgImg').css({"display": "inline"});
                    $('#titImg').css({"display": "none"});
                }else{
                    $('#contImagenes1').css({"display": "inline"});
                    $('#myImg1M').attr('src',`data:${Img[0].mime};base64,${Img[0].contenido}`);
                    $('#msgImg').css({"display": "none"});
                    $('#titImg').css({"display": "inline"});
                }
                if(Img[1] == undefined){
                    $('#contImagenes2').css({"display": "none"});
                }else{
                    $('#contImagenes2').css({"display": "inline"});
                    $('#myImg2M').attr('src',`data:${Img[1].mime};base64,${Img[1].contenido}`);
                }
                if(Img[2] == undefined){
                    $('#contImagenes3').css({"display": "none"});
                }else{
                    $('#contImagenes3').css({"display": "inline"});
                    $('#myImg3M').attr('src',`data:${Img[2].mime};base64,${Img[2].contenido}`);
                }
                if(Img[3] == undefined){
                    $('#contImagenes4').css({"display": "none"});
                }else{
                    $('#contImagenes4').css({"display": "inline"});
                    $('#myImg4M').attr('src',`data:${Img[3].mime};base64,${Img[3].contenido}`);
                }
            }catch(Err){
                console.log("Algunos desvios no tienen imagenes");
            }

            activarModal(document.getElementById('myModal1M')
                ,document.getElementById('myImg1M')
                ,document.getElementById('Img1M')
                ,document.getElementById('closeAM'));

                activarModal(document.getElementById('myModal2M')
                ,document.getElementById('myImg2M')
                ,document.getElementById('Img2M')
                ,document.getElementById('closeBM'));

                activarModal(document.getElementById('myModal3M')
                ,document.getElementById('myImg3M')
                ,document.getElementById('Img3M')
                ,document.getElementById('closeCM'));

                activarModal(document.getElementById('myModal4M')
                ,document.getElementById('myImg4M')
                ,document.getElementById('Img4M')
                ,document.getElementById('closeDM'));

                
                if(Desvio.disposicionInmediata == null){
                    $('#enviarDisp').css({"display" :"inline"});
                    $('#enviarDisp').on('click',function(){
                        var disp = $("#Dispo").val();
                        enviarDisp(Desvio.id,disp);
                    })
                }else{
                    $('#enviarDisp').css({"display" :"none"});
                    $("#Dispo").css({"display" :"none"});
                    $('#dispoOculta').text(Desvio.disposicionInmediata);
                    console.log(Desvio.disposicionInmediata);
                }
                /*
                $('#enviarDisp').on('click',function(){
                    var disp = $("#Dispo").val();
                    enviarDisp(Desvio.id,disp);
                })
                */

                if(Desvio.estado == "Corregir"){
                    $('#MostrarTitulo').text("Observacion de Corrección: ")
                    $('#obsCorreccion').text(Desvio.observacionCorreccion).css({"color":"red"});

                    //Mostrar el contenido en los imputs de Respuesta
                    $('#hh1').text(Desvio.horasRetrabajo);
                    $('#Analisis1').text(Desvio.analisisDeCausa);
                    $('#Accion1').text(Desvio.accionCorrectiva);
                    $('#enviarCorrect').css({"display":"inline"});
                    $('#EnvRespuesta').css({"display":"none"});

                    $('#enviarCorrect').on('click',function(){
                        var causaRaiz = $('#causa option:selected').text();
                        var hh = $('#hh1').val();
                        var analisis = $('#Analisis1').val();
                        var accion = $('#Accion1').val();
                        console.log(causaRaiz);
                        console.log(analisis);
                        console.log(hh);
                        console.log(accion);
                        
                        if(Desvio.disposicionInmediata == null){
                            $('#modalAlertRes').modal();
                            $('#TextModalAlertRes').text("Debe enviar Primero, una disposicion Inmediata.");
                        }else{
                            responderDesvio(Desvio.id,causaRaiz,hh,analisis,accion);
                        }
                    })
                }

                $('#EnvRespuesta').on('click',function(){
                    
                    var causaRaiz = $('#causa option:selected').text();
                    var hh = $('#hh1').val();
                    var analisis = $('#Analisis1').val();
                    var accion = $('#Accion1').val();
                    console.log(causaRaiz);
                    console.log(analisis);
                    console.log(hh);
                    console.log(accion);
                    
                    if(Desvio.disposicionInmediata == null){
                        $('#modalAlertRes').modal();
                        $('#TextModalAlertRes').text("Debe enviar Primero, una disposicion Inmediata.");
                    }else{
                        responderDesvio(Desvio.id,causaRaiz,hh,analisis,accion);
                    }
                })

            }).fail(function(Error){
                $('#modalAlertRes').modal();
                $('#TextModalAlertRes').text("Error del Servidor, Intente Nuevamente.");
            });
    })
};

function enviarDisp(id,disp){

    const idesvio = id;
    const dispo = disp;

        $('#modalConfirmRes').modal();

        $('#TextModalConfirmRes').text("Desea Enviar Disposicion Inmediata?");

        $('#ConfirmRes').on('click',function(){
            url = contextPath + "DesviosResponsable/cargarDisposicionDesvio";
            data = {
                id: idesvio,
                disposicion: dispo
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(data),
                contentType: 'application/json'
            }).done(function(msg){
                $('#modalAlertRes').modal();
                $('#TextModalAlertRes').text(msg);        
            }).fail(function(Error){
                $('#modalAlertRes').modal();
                $('#TextModalAlertRes').text("Error del Servidor, Intente Nuevamente.");
            });
        })

}

function responderDesvio(idDesvio,causaR,hh,anal,acc){

    console.log('la causa raiz es: '+causaR);
    $('#modalConfirmRes').modal();

        $('#TextModalConfirmRes').text("Desea Enviar la Respuesta del Desvio?");

        $('#ConfirmRes').on('click',function(){

            url = contextPath + "DesviosCalidad/ResponderDesvio";

            data = {
                id: idDesvio,
                causaRaiz: causaR,
                analisis: anal,
                horas: hh,
                accion: acc
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(data),
                contentType: 'application/json'
            }).done(function(msg){
                $('#modalAlertRes').modal();
                $('#TextModalAlertRes').text(msg);        
            }).fail(function(Error){
                console.log(Error);
                $('#modalAlertRes').modal();
                $('#TextModalAlertRes').text("Error del Servidor, Intente Nuevamente.");
            });





        })
}

function activarModal(modal,img,modalImg,span){
    
    img.onclick = function(){
        console.log("click en la imagen");
        modal.style.display = "block";
        modalImg.src = this.src;
    }
    span.onclick = function(){
        modal.style.display = "none";
    }
}

function asignarResp(){

    $('table #Asignar').on("click",function(event){

        event.preventDefault();
        var href = $(this).attr('href');
        console.log(href);
        
        
        $('#modalEmpleado').modal();
        $('#ModiEmpleado').empty();

        url = contextPath + "DesviosCalidad/listarEmpleadosArea?idDesvio="+href;
        
        $.get(url,function(responseJson1) {
            $.each(responseJson1, function(index, Empleado){
                $("<option>").val(Empleado.id).text(Empleado.nombre+" "+Empleado.apellido).appendTo($('#ModiEmpleado'));
                $("#ModiEmpleado option[value='"+Empleado.id+"']").prop("selected", true);
            });   
        }).done(function() {
        }).fail(function(msg) {  
            $('#modalEmpleado').modal('toggle');           
            $('#modalAlertRes').modal(msg);
        });



        
        $('#ConfirmModi').on("click",function(){ 

            const idDesv = href;
            const idEmpl = $('#ModiEmpleado option:selected').val();

            jsonData = {
                idDesvio: idDesv,
                idResp: idEmpl
            };
            url = contextPath + "DesviosCalidad/AsignarDesvio";                    
            $('#row1').css({"display":"block"});
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(jsonData),
                contentType: 'application/json'
            }).done(function(msg){
                $('#row1').css({"display":"none"});
                $('#modalAlertRes').modal();
                $('#TextModalAlertRes').text(msg);  
            }).fail(function(Error){
                $('#row11').css({"display":"none"});                          
                $('#modalAlertRes').modal();
                $('#TextModalAlertRes').text("Error del Servidor, vuelva a intentarlo.");
                console.log(Error);
            });           
        });            
    })
}
var botonBuscar;
var selectProyecto;
var inputNv;
var campoNroSerie;
var campoDescripcion;
var botonGuardarDesvio;
var empleadoResponsable;
var crearDesvio;
var proyectoElegido;
var EmpleadoEmisor;

$(document).ready(function(){

    buscardorDesvios();


    
    
    botonBuscar = $('#buscarProyecto');
    selectProyecto = $('#exampleFormControlSelect2');
    empleadoResponsable = $('#inputResponsable');
    empleadoEmisor= $('#inputEmisor');
    crearDesvio = $('#crearDesvio');
    
    listarEmpleados();
    crudDesvios();
    eliminarDesvios();
    modificarDesvios();
    listarAreas();

    botonBuscar.click(function(){
        buscarProyectos();
    });
    selectProyecto.on("change", function(){
        mostrarProyectosSeleccionados();
    });
    
    crearDesvio.click(function(){
        $('#crearDesvio').text("Cargando...");       
        $('#spinner').css({"display":"block"});
        createDesvio();
              
    });

    //PreCarga de Imagenes
    $('#imput1').change(function(){
        showImageThumbnail(this,$('#myImg1'));
    });
    $('#imput2').change(function(){
        showImageThumbnail(this,$('#myImg2'));
    });
    $('#imput3').change(function(){
        showImageThumbnail(this,$('#myImg3'));
    });
    $('#imput4').change(function(){
        showImageThumbnail(this,$('#myImg4'));
    });

});

//PreCargador de Imagenes
function showImageThumbnail(fileInput,img){
    
    file = fileInput.files[0];
    console.log(file);
    reader = new FileReader();
    reader.onload = function(e){
        img.attr('src', e.target.result);
    };
    reader.readAsDataURL(file);
}
function buscarProyectos(){
    inputNv = $('#ingresoNv').val();
    url = contextPath + "DesviosCalidad/buscar?nv="+inputNv;

    $.get(url,function(responseJson) {
        selectProyecto.empty();
        $.each(responseJson, function(index, ProyectoTrafo){
            $("<option>").val(ProyectoTrafo.tareaCodi).text(ProyectoTrafo.descripcion+" | Nro. Serie"+ProyectoTrafo.nroSerie).appendTo(selectProyecto);
            $("#exampleFormControlSelect2 option[value='"+ProyectoTrafo.tareaCodi+"']").prop("selected", true);
            $('#cl1').css({"display":"inline"});
            $("#cliResult").text(ProyectoTrafo.cliente);         
        });
        
          
    }).done(function(msg) {
        $('#resBusqueda').text("Hecho!. Seleccione una Descripción").css({"color": "green"});

    }).fail(function(msg) {
        console.log(msg);
        $('#resBusqueda').text("No se encuentra en transformador ingresado").css({"color": "red"});
    });
}
//no hace falta esta funcion
function listarEmpleados(){
    
    url = contextPath + "DesviosCalidad/listaEmpleados";
        
    $.get(url,function(responseJson1) {
        $.each(responseJson1, function(index, Empleado){
            $("<option>").val(Empleado.id).text(Empleado.nombre+" "+Empleado.apellido).appendTo(empleadoResponsable);
            $("#inputResponsable option[value='"+Empleado.id+"']").prop("selected", true);
            $("<option>").val(Empleado.id).text(Empleado.nombre+" "+Empleado.apellido).appendTo(empleadoEmisor);
            $("#inputEmisor option[value='"+Empleado.id+"']").prop("selected", true);
            console.log(Empleado);
        });   
    }).done(function() {
    }).fail(function() {      
    });
    
}
function listarAreas(){
    
    url = contextPath + "DesviosCalidad/listarAreas";
        
    $.get(url,function(responseJson1) {  
        $.each(responseJson1, function(index, Area){
            $("<option>").val(Area.unidadOrganizacionalCodi).text(Area.nombre).appendTo($('#inputArea'));
            $("#inputArea option[value='"+Area.unidadOrganizacionalCodi+"']").prop("selected", true);
        });      
    }).done(function() {
    }).fail(function() {      
    });
    
}
function mostrarProyectosSeleccionados(){
    
    proyectoSel = $('#exampleFormControlSelect2 option:selected').val();
    proyectoSeleccion = $('#exampleFormControlSelect2 option:selected').text();
    $('#desText').text(proyectoSeleccion);

}
function buscardorDesvios(){
    
    $("#match").keyup(function () {
    var searchTerm = $("#match").val();
    var listItem = $('#Desvios tbody').children('tr');
    var searchSplit = searchTerm.replace(/ /g, "'):containsi('")
    
    $.extend($.expr[':'], {'containsi': function(elem, i, match, array){
        return (elem.textContent || elem.innerText || '').toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
        }
    }
    );
    
    $("#Desvios tbody tr").not(":containsi('" + searchSplit + "')").each(function(e){
        $(this).attr('visible','false');
    });

    $("#Desvios tbody tr:containsi('" + searchSplit + "')").each(function(e){
        $(this).attr('visible','true');
    });
    var jobCount = $('#Desvios tbody tr[visible="true"]').length;
    $('.counter').text(jobCount + ' Desvios Encontrados');
    if(jobCount == '0') {$('#noResults').show();}
    else {$('#noResults').hide();}
    
    });
}
//Ver que el back setee por defecto el responsable de area, luego este asignara
//un responsable de seguimiento en caso de ser necesario
function createDesvio(){
    
    url = contextPath + "DesviosCalidad/guardarDesvio";

    
 
    const Id = $('#exampleFormControlSelect2 option:selected').val();
    const Area = $("#inputArea").val();
    const usuarioEmisorId = $("#idEmisor").text();
    const sec = $("#sector option:selected").val();
    const proceso = $('#Pro option:selected').val();
    const tparte = $('#Trafo option:selected').val();
    const ishi = $('#Ishi option:selected').val();
    const cal = $('#Efect option:selected').val();
    const plant = $('#pla option:selected').val();
    const observation = $('#Obs').val();
    jsonData = {
        idArea: Area,
        idProyecto: Id,
        idEmisor: usuarioEmisorId,
        sector: sec,
        process: proceso,
        trafo: tparte,
        CausaIshicawa: ishi,
        Calidad: cal,
        Planta : plant,
        Observacion: observation
    }
    console.log(jsonData);
    
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(Desvio){
        console.log(typeof(Desvio));
            try{
                var JsonBody = JSON.parse(Desvio);
                $('#exampleModal').modal('toggle');
                $('#spinner').css({"display":"none"});
                $('#modalAlert').modal();
                $('#TextModalAlert').text('Desvio Generado, proceda al siguiente paso.');
                $('#accept').on("click",function(){
                    segundaEtapa(JsonBody);
                });
                
            }catch(Error){
                $('#modalAlert').modal();
                $('#spinner').css({"display":"none"});
                $('#crearDesvio').text("Siguiente");
                $('#TextModalAlert').text('Error:  ' +Desvio +' .'+' Vuelva a intentarlo.');
            }  
    }).fail(function(Error){
        $('#crearDesvio').text("Siguiente"); 
        $('#exampleModal').modal('toggle');
        $('#spinner').css({"display":"none"});
        $('#modalAlert').modal();
        $('#TextModalAlert').text("Error del Servidor, vuelva a intentarlo.");
        console.log(Error);
    });
    
}

var nroDesvio;
var fechaEmi;
var nvDesvio;
var clienteDesv;
var resposableDesv;
var sectorDesvio;
var plantaDesvio;
var estadoDesvio;
var fechaRespDesvio;
var modalImagenes;
var btn;

function segundaEtapa(Desvio){
    console.log(Desvio);
    $('#modalImagenes').modal();

    //En caso de que toquen otra cosa y el desvio se cierre se deben poder cargar las imagenes,
    //desde el tablero de desvios en la seccion modificar,
    modalLabel = $('#titulo2daEtapa').text("Desvio Nro: "+Desvio.id);
    $("#nv").text(Desvio.proyecto.notadeVenta).css({"color":"teal"});
    if(!(Desvio.proyecto.nroSerie.substring(0,2) == '1 ')){
        $('#2daCliente').css({"display":"inline"}); 
        $("#2dans").css({"display":"inline"});
        $('#nsD').text(Desvio.proyecto.nroSerie).css({"color":"teal"});
        $("#cliD").text(Desvio.proyecto.cliente).css({"color":"teal"}); 
    }else{
        $('#2daCliente').css({"display":"none"}); 
        $("#2dans").css({"display":"none"});
    }
    $('#desD').text(Desvio.proyecto.descripcion).css({"color":"teal"});
    $('#gen').text(Desvio.generador.nombre+" "+Desvio.generador.apellido).css({"color":"teal"});
    $('#seg').text(Desvio.responsable.nombre+" "+Desvio.responsable.apellido).css({"color":"teal"});
    $("#sec").text(Desvio.area.nombre).css({"color":"teal"});
    $("#planta2da").text(Desvio.planta).css({"color":"teal"});
    $("#proD").text(Desvio.procesoFab).css({"color":"teal"});
    $("#isD").text(Desvio.causaPrincipal).css({"color":"teal"});
    $("#tfD").text(Desvio.trafoparte).css({"color":"teal"});
    $("#noC").text(Desvio.efectoscalidad).css({"color":"teal"});
    $("#obs").text(Desvio.observacionCalidad).css({"color":"teal"}).width(600);
    $('#cargar').click(function(){
        subirImagenes(Desvio.id);
    });   

    $('#DirectSend').on("click",function(){
        console.log("Encontre boton enviarDirecto");
        enviarDesvio(Desvio.id);

    });
    //Modal Imagenes subidas
    activarModal(document.getElementById('myModal1')
                ,document.getElementById('myImg1')
                ,document.getElementById('Img1')
                ,document.getElementById('closeA'));

    activarModal(document.getElementById('myModal2')
                ,document.getElementById('myImg2')
                ,document.getElementById('Img2')
                ,document.getElementById('closeB'));

    activarModal(document.getElementById('myModal3')
                ,document.getElementById('myImg3')
                ,document.getElementById('Img3')
                ,document.getElementById('closeC'));

    activarModal(document.getElementById('myModal4')
                ,document.getElementById('myImg4')
                ,document.getElementById('Img4')
                ,document.getElementById('closeD'));


}
function subirImagenes(id){

    console.log("Entre a funcion");
    //Subir Imagenes;
    var imput1 = document.getElementById("imput1");
    var imput2 = document.getElementById("imput2");
    var imput3 = document.getElementById("imput3");
    var imput4 = document.getElementById("imput4");

    var imagenes = [];

    console.log(typeof(imagenes))
    

    imagenes.push(imput1.files[0]);
    imagenes.push(imput2.files[0]);
    imagenes.push(imput3.files[0]);
    imagenes.push(imput4.files[0]);

    console.log(imagenes.length);

    var data = new FormData();

    for(var i = 0; i < imagenes.length; i++){
        data.append('imagenes',imagenes[i]);
    }
    console.log(data);

    
    url = contextPath + `Desvio${id}/cargarFotos`;
    $.ajax({
        type:"POST",
        enctype: 'multipart/form-data',
        url: url,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
    }).done(function(msg){
        $('#modalAlert').modal();
        $('#TextModalAlert').text(msg);
    }).fail(function(){
        $('#modalAlert').modal();
        $('#TextModalAlert').text("No se ha podido cargar la imagen.");
    });
}
//cambiar los paramentros de desvio en el modal para mostrar el responsable de Area como Resp de seguimiento
function crudDesvios(){

    $('table .view').on('click',function(event){
        event.preventDefault();
        var href = $(this).attr('href');

        console.log(href);
        $('#sp1').css({"display":"block"});
        $.get(href, function(Desvio, status){
            console.log(Desvio);

            $('#ModalMostraTitulo').text("Desvio Nro: "+Desvio.id);
            $("#nvMostrar").text(Desvio.proyecto.notadeVenta).css({"color":"teal"});
            if(!(Desvio.proyecto.nroSerie.substring(0,2) == '1 ')){
                $('#mostrarCliente').css({"display":"inline"}); 
                $("#mostrarns").css({"display":"inline"});
                $('#nsDMostrar').text(Desvio.proyecto.nroSerie).css({"color":"teal"}); 
                $("#cliMostrar").text(Desvio.proyecto.cliente).css({"color":"teal"});
            }else{
                $('#mostrarCliente').css({"display":"none"}); 
                $("#mostrarns").css({"display":"none"});

            }
            
            $('#segMostrar').text(Desvio.responsable.nombre+" "+Desvio.responsable.apellido).css({"color":"teal"});
            
            $('#desDMostrar').text(Desvio.proyecto.descripcion).css({"color":"teal"});
            $('#genMostrar').text(Desvio.generador.nombre+" "+Desvio.generador.apellido).css({"color":"teal"});
           
            $("#sectorMostar").text(Desvio.area.nombre).css({"color":"teal"});
            $("#plantaMostrar").text(Desvio.planta).css({"color":"teal"});
            $("#proDMostrar").text(Desvio.procesoFab).css({"color":"teal"});
            $("#tfDMostrar").text(Desvio.trafoparte).css({"color":"teal"});
            $("#isDMostrar").text(Desvio.causaPrincipal).css({"color":"teal"});
            $("#noCMostrar").text(Desvio.efectoscalidad).css({"color":"teal"});
            $('#obsMostrar').text(Desvio.observacionCalidad).css({"color":"teal"});

            Img = Desvio.imagenes;
             
            try{           
                    if(Img[0] == undefined){
                        console.log(Img[0]);
                        $('#containerImagenes1').css({"display": "none"});
                        $('#msgImagenes').css({"display": "inline"});
                        $('#tituloImg').css({"display": "none"});

                    }else{
                        $('#containerImagenes1').css({"display": "inline"});
                        $('#myImg1M').attr('src',`data:${Img[0].mime};base64,${Img[0].contenido}`);
                        $('#msgImagenes').css({"display": "none"});
                        $('#tituloImg').css({"display": "inline"});
                    }
                    if(Img[1] == undefined){
                        $('#containerImagenes2').css({"display": "none"});
                    }else{
                        $('#containerImagenes2').css({"display": "inline"});
                        $('#myImg2M').attr('src',`data:${Img[1].mime};base64,${Img[1].contenido}`);
                    }
                    if(Img[2] == undefined){
                        
                        $('#containerImagenes3').css({"display": "none"});
                    }else{
                        $('#containerImagenes3').css({"display": "inline"});
                        $('#myImg3M').attr('src',`data:${Img[2].mime};base64,${Img[2].contenido}`);
                    }
                    if(Img[3] == undefined){
                        $('#containerImagenes4').css({"display": "none"});
                    }else{
                        $('#containerImagenes4').css({"display": "inline"});
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
                            

            //Disposicion Inmediata
            if(Desvio.disposicionInmediata == null){
                $('#DispoCal').text("Disposicion sin Cargar.").css({"color":"red"}); 
            }else{
                $('#DispoCal').text(Desvio.disposicionInmediata).css({"color":"red"});
            }
            //Respuesta
            if(Desvio.fechaRespuesta == null){
                $("#containerRespuestaCal").css("background-color", "lightgrey");
                $("#verCausaRCal").css({"display":"none"});
                $("#verHorasCal").css({"display":"none"});
                $("#verAnalisisCal").css({"display":"none"});
                $("#verAccionCal").css({"display":"none"});
                $('#EstadoDesvioCal').text("Desvio sin Responder."); 
            }else{
                $("#verCausaRCal").text(Desvio.analisisDeCausa);
                $("#verHorasCal").text(Desvio.horasRetrabajo);
                $("#verAnalisisCal").text(Desvio.analisisDeCausa);
                $("#verAccionCal").text(Desvio.accionCorrectiva);
                $('#EstadoDesvioCal').text(Desvio.estado);
            }

            $('#sp1').css({"display":"none"});

        $('#EnviarDesvio').on("click",function(){
            enviarDesvio(Desvio.id);
        });

        $("#DesaprobarDesvio").on("click",function(){
           DesaprobarDesvio(Desvio.id); 
        });

        $('#AprobarDesvio').on("click",function(){
            AprobarDesvio(Desvio.id); 
        })
        })



    })
}
function enviarDesvio(id) {

    const idesvio = id;
    console.log(idesvio);

        $('#modalConfirm').modal();

        $('#TextModalConfirm').text("Desea Enviar este Desvio?");

        $('#Confirm').on('click',function(){

            url = contextPath + "DesviosCalidad/enviarDesvio?id="+idesvio;

            $.get(url,function(responseJson) {
              
            }).done(function(msg) {
                $('#modalAlert').modal();
                $('#TextModalAlert').text(msg);
            }).fail(function() {
                $('#modalAlert').modal();
                $('#TextModalAlert').text("Hubo un Error, Intente Nuevamente.");
            });

        })
}
function AprobarDesvio(id){

    const idesvio = id;
    console.log(idesvio);

        $('#modalConfirm').modal();

        $('#TextModalConfirm').text("Desea Aprobar este Desvio?");

        $('#Confirm').on('click',function(){

            url = contextPath + "DesviosCalidad/AprobarDesvio?id="+idesvio;

            $.get(url,function(responseJson) {
              
            }).done(function(msg) {
                $('#modalAlert').modal();
                $('#TextModalAlert').text(msg);
            }).fail(function() {
                $('#modalAlert').modal();
                $('#TextModalAlert').text("Hubo un Error, Intente Nuevamente.");
            });

        })


}
function DesaprobarDesvio(id){
    console.log("Click Desapbrar");

    $("#containerObservacion").css({"display":"inline"});
    
    
    $('#enviarObservacion').on("click",function(){
        var obs = $('#ObservacionCorreccion').val();
        console.log(obs);
        
        $('#modalConfirm').modal();

        $('#TextModalConfirm').text("Desea Enviar esta Observación?");

        $('#Confirm').on('click',function(){
            url = contextPath + "DesviosCalidad/desaprobarDesvio";
            data = {
                id: id,
                observacion: obs
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(data),
                contentType: 'application/json'
            }).done(function(msg){
                $('#modalAlert').modal();
                $('#TextModalAlert').text(msg);        
            }).fail(function(Error){
                $('#modalAlert').modal();
                $('#TextModalAlert').text("Error del Servidor, Intente Nuevamente.");
            });
        })
        
    }) 
       
}
function eliminarDesvios(){
    /*
    Tener en cuenta que en la llamada siempre hay que declarar que el 
    boton pertence a una table xq sino no toma el event.PreventDefault();
    */
    $('table #deleteDesvio').on("click",function(event){
        console.log("click");
        event.preventDefault();
        var href = $(this).attr('href');
        console.log(href);
        
        $('#modalConfirm').modal();
        $('#TextModalConfirm').text("Esta seguro que desea elimnar el desvio Nro "+href+" ?");

        $('#Confirm').on('click',function(){
            url='DesviosCalidad/eliminarDesvio?id='+href;
            $.ajax({
                url: url,
                type: 'DELETE',
            }).done(function(msg){
                $('#modalAlert').modal();
                $('#TextModalAlert').text(msg); 
            }).fail(function(){
                $('#modalAlert').modal();
                $('#TextModalAlert').text("Error del Servidor, Intente Nuevamente.");
            })    
        });  
    })
}
//cambiar los paramentros de desvio en el modal para mostrar el responsable de Area como Resp de seguimiento
//modificar en vez de cambiar de responable, cambiar de Area, y modificar modal para ver el area, responable y sacar sector 
function modificarDesvios(){

    $('table #EditarDesvio').on("click",function(event){

        event.preventDefault();
        var href = $(this).attr('href');
        console.log(href);
        url = 'DesvioCalidad/OneDesvio?Id='+href;
        $('#modalModificarDesv').modal();
        $('#sp2').css({"display":"block"});
        $.get(url, function(Desvio, status){
 
            //Modificacion Proyecto
            console.log(Desvio);
            $('#ModiDesv').text('Desvio Nro: '+Desvio.id);
            $("#idProyectoMOD").text(Desvio.proyecto.tareaCodi);
            $('#nvMod').text(Desvio.proyecto.notadeVenta);
            $('#clMod').text(Desvio.proyecto.cliente);
            $('#nsMod').text(Desvio.proyecto.nroSerie);
            $('#desMod').text(Desvio.proyecto.descripcion);

            $('#sp2').css({"display":"none"});

            $('#modiPro').on("click",function(){
                $('#modalProyectoModificar').modal();
                $('#buscarProyectoMOD').on("click", function(){
                    inputNv = $('#ingresoNvMOD').val();
                    url = contextPath + "DesviosCalidad/buscar?nv="+inputNv;            
                    var selectProyecto = $('#exampleFormControlSelect2MOD');

                    $.get(url,function(responseJson) {
                        selectProyecto.empty();

                        $.each(responseJson, function(index, ProyectoTrafo){
                            $("<option>").val(ProyectoTrafo.tareaCodi).text(ProyectoTrafo.descripcion+" | Nro. Serie"+ProyectoTrafo.nroSerie).appendTo(selectProyecto);
                            $("#exampleFormControlSelect2MOD option[value='"+ProyectoTrafo.tareaCodi+"']").prop("selected", true);
                            $('#cl1MOD').css({"display":"inline"});
                            $('#cliResultMOD').text(ProyectoTrafo.cliente);        
                        });

                        selectPro = $('#exampleFormControlSelect2MOD');
                        selectPro.on("change", function(){
                            $('#desTextMOD').text($('#exampleFormControlSelect2MOD option:selected').text());       
                        });

                    }).done(function(Proyecto) {
                        $('#resBusquedaMOD').text("Hecho!. Seleccione una Descripción").css({"color": "green"});
                        $('#acceptMOD').on("click", function(){ 
                            var mostrar = $('#exampleFormControlSelect2MOD option:selected').val();
                            $('#idProyectoMOD').text(mostrar);
                            $('#nvMod').text(Proyecto[0].notadeVenta);
                            $('#clMod').text(Proyecto[0].cliente);
                            $('#nsMod').text(Proyecto[0].nroSerie);
                            $('#desMod').text($('#exampleFormControlSelect2MOD option:selected').text());                           
                        })                        
                    }).fail(function(msg) {
                        console.log(msg);
                        $('#resBusquedaMOD').text("No se encuentra en transformador ingresado").css({"color": "red"});
                    });
                });
               
            });
            
            //Modificacion de Paramentros
            $('#RespEmi').text(Desvio.generador.nombre+" "+Desvio.generador.apellido).css({"color":"gray"});
            $('#AreaMOD').text(Desvio.area.nombre);
            $('#resMOD').text(Desvio.responsable.nombre+" "+Desvio.responsable.apellido);
            var idArea = Desvio.area.unidadOrganizacionalCodi;
            //Modificar Empleado
            $('#modiResp').on("click",function(){
            $('#modalEmpleado').modal();
                url2 = contextPath + "DesviosCalidad/listarAreas";
                $.get(url2,function(responseJson1) {
                    $('#ModiEmpleado').empty();
                    $.each(responseJson1, function(index, area){
                        $("<option>").val(area.unidadOrganizacionalCodi).text(area.nombre).appendTo($('#ModiEmpleado'));
                        $("#ModiEmpleado option[value='"+area.unidadOrganizacionalCodi+"']").prop("selected", true);
                    });                       
                }).done(function() {
                    $('#ConfirmModi').on("click",function(){
                        $('#AreaMOD').empty();
                        $('#resMOD').empty();
                        $('#AreaMOD').text($('#ModiEmpleado option:selected').text());
                        idArea = $('#ModiEmpleado option:selected').val();
                    })
                }).fail(function() {      
                });
                
            })
            elejido(document.getElementById("plaMod"),Desvio.planta);
            elejido(document.getElementById("ProMod"),Desvio.procesoFab);
            elejido(document.getElementById("TrafoMod"),Desvio.trafoparte);
            elejido(document.getElementById("IshiMod"),Desvio.causaPrincipal);
            elejido(document.getElementById("EfectMod"),Desvio.efectoscalidad);
            $('#ObsMOD').val(Desvio.observacionCalidad);
            
            /*
            -CAMBIAR ISHIKAWA Y CAUSA NO CALIDAD,
            -ORDENAR DE MAYOR A MENOR
            */
            //Guardo en Variables todo lo que modifique

            $('#ModiParams').on("click",function(){

                const idDesvio = Desvio.id;
                const idPro = $('#idProyectoMOD').text();
                const userEmisor = Desvio.generador.id;
                const area = idArea;
                const sec = $('#sectorMod option:selected').val();
                const plant = $('#plaMod option:selected').val();
                const proceso = $('#ProMod option:selected').val();
                const tparte = $('#TrafoMod option:selected').val();
                const ishi = $('#IshiMod option:selected').val();
                const cal = $('#EfectMod option:selected').val();
                const observation = $('#ObsMOD').val();
                jsonData = {
                    id: idDesvio,
                    idProyecto: idPro,
                    idArea: area,
                    idEmisor: userEmisor,
                    process: proceso,
                    trafo: tparte,
                    CausaIshicawa: ishi,
                    Calidad: cal,
                    Planta : plant,
                    Observacion: observation
                }

                $('#modalConfirm').modal();
                    $('#TextModalConfirm').text('Desea Guardar los Cambios?');
                    $('#Confirm').on("click",function(){                            
                        url = contextPath + "DesviosCalidad/ModificarDesvio";                    
                        $('#spinner1').css({"display":"block"});
                        $('#msgGuardando').css({"display":"block"});
                        $.ajax({
                            type: "POST",
                            url: url,
                            data: JSON.stringify(jsonData),
                            contentType: 'application/json'
                        }).done(function(msg){
                            $('#spinner1').css({"display":"none"});
                            $('#msgGuardando').css({"display":"none"});
                            $('#modalAlert').modal();
                            $('#TextModalAlert').text(msg);  
                        }).fail(function(Error){
                            $('#spinner1').css({"display":"none"}); 
                            $('#msgGuardando').css({"display":"none"});                          
                            $('#modalAlert').modal();
                            $('#TextModalAlert').text("Error del Servidor, vuelva a intentarlo.");
                            console.log(Error);
                        });
                            
                    });
            })

           //----MODIFICACION DE IMAGENES 
           Img = Desvio.imagenes;
           $('#msgUserImg').empty();
           for(var j = 1; j<5; j++){
            $(`#imput${j}MOD`).val('');
            $(`#myImg${j}MOD`).attr('src','');
            }                     
            try{ 
                var j = 1; 
                for(var i = 0, len = Img.length; i<len; i++){
                    if(!(Img[i] == undefined)){
                        $(`#myImg${j}MOD`).attr('src',`data:${Img[i].mime};base64,${Img[i].contenido}`);  
                        j++;
                    }
                }
                if(Img.length == 0){
                    $('#msgUserImg').text("El desvio no tiene imagenes Cargadas.").css({"color":"red"}); 
                }else{
                    var a = Img.length;
                    if(a>4){
                        a=4;
                    }
                    $('#msgUserImg').text('El desvio tiene '+a+' imagenes Cargadas.').css({"color":"green"});
                }                        
            }catch(Err){
                $('#msgUserImg').text("Error al cargar las imagenes del desvío.");
                console.log("Algunos desvios no tienen imagenes");
            }
            $('#imput1MOD').change(function(){
            showImageThumbnail(this,$('#myImg1MOD'));
            });
            $('#imput2MOD').change(function(){
            showImageThumbnail(this,$('#myImg2MOD'));
            });
            $('#imput3MOD').change(function(){
            showImageThumbnail(this,$('#myImg3MOD'));
            });
            $('#imput4MOD').change(function(){
            showImageThumbnail(this,$('#myImg4MOD'));
            });

            $('#ConfirmModi1').on("click",function(){
                var input1 = document.getElementById("imput1MOD");
                if(input1.files[0] == undefined){
                    $('#modalAlert').modal();
                    $('#TextModalAlert').text("Debe cargar un archivo.");
                }else{
                    if(!(Img[0] == undefined)){
                        modficarImagenes(Desvio.id,Img[0].id,input1,$("#row1")); 
                     }else{
                        modficarImagenes(Desvio.id,0,input1,$("#row1")); 
                     }
                }         
            });
            $('#ConfirmModi2').on("click",function(){
                var input2 = document.getElementById("imput2MOD");
                if(input2.files[0] == undefined){
                    $('#modalAlert').modal();
                    $('#TextModalAlert').text("Debe cargar un archivo.");
                }else{
                    if(!(Img[1] == undefined)){
                        modficarImagenes(Desvio.id,Img[1].id,input2,$("#row2")); 
                     }else{
                        modficarImagenes(Desvio.id,0,input2,$("#row2")); 
                     }
                }
            });
            $('#ConfirmModi3').on("click",function(){
                var input3 = document.getElementById("imput3MOD");
                if(input3.files[0] == undefined){
                    $('#modalAlert').modal();
                    $('#TextModalAlert').text("Debe cargar un archivo.");
                }else{
                    if(!(Img[2] == undefined)){
                        modficarImagenes(Desvio.id,Img[2].id,input3,$("#row3")); 
                     }else{
                        modficarImagenes(Desvio.id,0,input3,$("#row3")); 
                     }
                }
            });
            $('#ConfirmModi4').on("click",function(){
                var input4 = document.getElementById("imput4MOD");
                if(input4.files[0] == undefined){
                    $('#modalAlert').modal();
                    $('#TextModalAlert').text("Debe cargar un archivo.");
                }else{
                    if(!(Img[3] == undefined)){
                        modficarImagenes(Desvio.id,Img[3].id,input4,$("#row4")); 
                     }else{
                        modficarImagenes(Desvio.id,0,input4,$("#row4")); 
                     }
                }
            });
            

            activarModal(document.getElementById('myModal1MOD')
                            ,document.getElementById('myImg1MOD')
                            ,document.getElementById('Img1MOD')
                            ,document.getElementById('closeAMOD'));

            activarModal(document.getElementById('myModal2MOD')
                            ,document.getElementById('myImg2MOD')
                            ,document.getElementById('Img2MOD')
                            ,document.getElementById('closeBMOD'));

            activarModal(document.getElementById('myModal3MOD')
                            ,document.getElementById('myImg3MOD')
                            ,document.getElementById('Img3MOD')
                            ,document.getElementById('closeCMOD'));

            activarModal(document.getElementById('myModal4MOD')
                            ,document.getElementById('myImg4MOD')
                            ,document.getElementById('Img4MOD')
                            ,document.getElementById('closeDMOD')); 
        })
    });
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
function elejido(obj,str){
    for(var i = 0, len = obj.length; i<len; i++){
        if(obj[i].value == str){
            obj[i].selected = true;
            break;
        }
    }
}
function modficarImagenes(idDesvio,idImagen,input,row){
    
    var idDesv = idDesvio;
    var idImg = idImagen;
    var inp = input;

    console.log(idImg);

        var imagen = inp.files[0];
        
        var data = new FormData();
        data.append('imagen',imagen);

        $('#modalConfirm').modal();

        switch (idImagen){
            case 0:
                $('#TextModalConfirm').text("Desea subir esta imagen al desvío?");
                break;    
            default:
                $('#TextModalConfirm').text("Desea sobreescribir esta imagen?");
                break;
        }
        $('#Confirm').on('click',function(){
            
            $("#row1")
            row.css({"display":"block"});

            url = contextPath + `Desvio${idDesv}/Imagen${idImg}/modificarFotos`;
            $.ajax({
            type:"POST",
            enctype: 'multipart/form-data',
            url: url,
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            }).done(function(msg){
                row.css({"display":"none"});
            $('#modalAlert').modal();
            $('#TextModalAlert').text(msg);
            }).fail(function(){
                row.css({"display":"none"});
            $('#modalAlert').modal();
            $('#TextModalAlert').text("No se ha podido cargar la imagen.");
            });
    })
        

}








    



    




 







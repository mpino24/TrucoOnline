package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotAuthorizedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/logros")
public class LogrosController {

    private final LogrosService logrosService;
    private final UserService userService;
    private final JugadorService jugadorService;

    @Autowired
    public LogrosController(LogrosService logrosService, UserService userService, JugadorService jugadorService){
        this.logrosService= logrosService;
        this.userService=userService;
        this.jugadorService=jugadorService;
    }


    @GetMapping
    public ResponseEntity<List<Logros>> getTodosLosLogros(){
        Boolean esAdmin = false;
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        if (currentUser.hasAuthority("ADMIN")) {
            esAdmin=true;
        }
        return new ResponseEntity<>(logrosService.findAllLogros(esAdmin,jugadorId), HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalLogros(){
     
        return new ResponseEntity<>(logrosService.findTotalLogros(), HttpStatus.OK);
    }


    @GetMapping("/misLogros")
    public ResponseEntity<List<Logros>> getMisLogros(){
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        return new ResponseEntity<>(logrosService.logrosConseguidos(jugadorId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Logros> createLogro(@RequestBody @Valid Logros logro){
        User currentUser = userService.findCurrentUser();
        if(currentUser.hasAuthority("ADMIN")){
            Logros newLogro = new Logros();
            BeanUtils.copyProperties(logro, newLogro, "id");
            return new ResponseEntity<>(logrosService.save(newLogro), HttpStatus.CREATED);
        }else{
            throw new NotAuthorizedException();
        }

    }

    @DeleteMapping("/{logroId}")
    public ResponseEntity<Void> deleteLogro(@PathVariable("logroId") Integer logroId){
        User currentUser = userService.findCurrentUser();
        if(currentUser.hasAuthority("ADMIN")){
            if(logrosService.findLogroById(logroId)==null){
                throw new ResourceNotFoundException("No se encontro el logro");
            }
            logrosService.deleteLogro(logroId);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            throw new NotAuthorizedException();
        }

    }

    @PutMapping("/{logroId}")
    public ResponseEntity<Logros> updateLogro(@RequestBody @Valid Logros logro, @PathVariable("logroId") Integer logroId){
        User currentUser = userService.findCurrentUser();
        if(currentUser.hasAuthority("ADMIN")){
            return new ResponseEntity<>(logrosService.updateLogro(logro, logroId), HttpStatus.CREATED);
        }else{
            throw new NotAuthorizedException();
        }

    }

    
}

package es.us.dp1.lx_xy_24_25.truco_beasts.carta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/carta")
@Tag(name = "Cartas", description = "La API de gestión de las cartas. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class CartaController {
    CartaService cartaService;
    
    @Autowired
    public CartaController(CartaService cartaService){
        this.cartaService = cartaService;
    }
    
    @Operation(summary = "Obtener una carta por su ID", description = "Devuelve una carta según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carta encontrada", 
                     content = { @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = Carta.class)) }),
        @ApiResponse(responseCode = "404", description = "Carta no encontrada", 
                     content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
                     content = @Content)
    })
    @GetMapping("/id/{idCarta}")
    public ResponseEntity<Carta> findCartaById(
            @Parameter(description = "ID de la carta a obtener", required = true) 
            @PathVariable("idCarta") Integer idCarta) {
        return new ResponseEntity<>(cartaService.findCartaById(idCarta), HttpStatus.OK);
    }
}

package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.EnvidoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.FlorException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/manos/{codigo}")
@Tag(name = "Mano", description = "La API de gestión de la Manos de las partidas con las acciones posibles de los jugadores. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class ManoController {

    private final ManoService manoService;

    @Autowired
    public ManoController(ManoService manoService){
        this.manoService = manoService;
    }

    @Operation(summary = "Actualizar mano", description = "Obtiene la información de la mano actual.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mano> actualizarMano(@Parameter(description = "Código de la mano") @PathVariable String codigo){
        Mano mano = manoService.getMano(codigo);
        return new ResponseEntity<>(mano, HttpStatus.OK);
    }

    @Operation(summary = "Tirar carta", description = "Permite a un jugador tirar una carta.")
    @PatchMapping("/tirarCarta/{cartaId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Carta> patchTirarCarta(
            @Parameter(description = "Código de la mano") @PathVariable String codigo,
            @Parameter(description = "ID de la carta a tirar") @PathVariable Integer cartaId) throws CartaTiradaException {
        Carta carta = manoService.tirarCarta(codigo, cartaId);
        return new ResponseEntity<>(carta, HttpStatus.OK);
    }

    @Operation(summary = "Cantar truco", description = "Permite a un jugador cantar truco.")
    @PatchMapping("/cantarTruco/{cantoTruco}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchCantarTruco(
            @Parameter(description = "Código de la mano") @PathVariable String codigo,
            @Parameter(description = "Canto de truco") @PathVariable Cantos cantoTruco) throws TrucoException {
        manoService.cantosTruco(codigo, cantoTruco);
        return new ResponseEntity<>(cantoTruco, HttpStatus.OK);
    }

    @Operation(summary = "Responder truco", description = "Permite a un jugador responder a un truco.")
    @PatchMapping("/responderTruco/{respuestasTruco}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> patchResponderTruco(
            @Parameter(description = "Código de la mano") @PathVariable String codigo,
            @Parameter(description = "Respuesta al truco") @PathVariable Cantos respuestasTruco) throws TrucoException {
        manoService.responderTruco(codigo, respuestasTruco);
        Integer puntosActualesTruco = manoService.getMano(codigo).getPuntosTruco();
        return new ResponseEntity<>(puntosActualesTruco, HttpStatus.OK);
    }

    @Operation(summary = "Cantar envido", description = "Permite a un jugador cantar envido.")
    @PatchMapping("/cantarEnvido/{cantoEnvido}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchCantarEnvido(
            @Parameter(description = "Código de la mano") @PathVariable String codigo,
            @Parameter(description = "Canto de envido") @PathVariable Cantos cantoEnvido) throws EnvidoException {
        manoService.cantosEnvido(codigo, cantoEnvido);
        return new ResponseEntity<>(cantoEnvido, HttpStatus.OK);
    }

    @Operation(summary = "Responder envido", description = "Permite a un jugador responder a un envido.")
    @PatchMapping("/responderEnvido/{cantoEnvido}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchResponderEnvido(
            @Parameter(description = "Código de la mano") @PathVariable String codigo,
            @Parameter(description = "Respuesta al envido") @PathVariable Cantos cantoEnvido) throws EnvidoException {
        manoService.responderEnvido(codigo, cantoEnvido);
        return new ResponseEntity<>(cantoEnvido, HttpStatus.OK);
    }

    @Operation(summary = "Cantar flor", description = "Permite a un jugador cantar flor.")
    @PatchMapping("/cantarFlor/{cantoFlor}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchCantarFlor(
            @Parameter(description = "Código de la mano") @PathVariable String codigo,
            @Parameter(description = "Canto de flor") @PathVariable Cantos cantoFlor) throws FlorException {
        manoService.cantosFlor(codigo, cantoFlor);
        return new ResponseEntity<>(cantoFlor, HttpStatus.OK);
    }

    @Operation(summary = "Responder flor", description = "Permite a un jugador responder a una flor.")
    @PatchMapping("/responderFlor/{cantoFlor}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchResponderFlor(
            @Parameter(description = "Código de la mano") @PathVariable String codigo,
            @Parameter(description = "Respuesta a la flor") @PathVariable Cantos cantoFlor) throws FlorException {
        manoService.responderFlor(codigo, cantoFlor);
        return new ResponseEntity<>(cantoFlor, HttpStatus.OK);
    }
}
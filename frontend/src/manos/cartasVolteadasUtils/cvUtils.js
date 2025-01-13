// src/components/CartasVolteadas/cartasVolteadasUtils/cvUtils.js

export const listaUrlCartasVacias = [
  "http://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/cartas/1carta.jpg", //0
  "http://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/cartas/2cartas.jpg", //1
  "http://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/cartas/3cartas.jpg", //2
  "http://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/cartas/mazo.png",     //3
  "http://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/cartas/SinCartas.jpg",//4
];

// Enumerations for seat distances
export const SeatDistance = {
  ONE_AWAY: 1,
  TWO_AWAY: 2,
  FOUR_AWAY: 4,
  FIVE_AWAY: 5,
};

// Function to determine if the user is a spectator
export const isSpectator = (posicionListaCartas) => typeof posicionListaCartas === 'object';

// Function to get card image based on remaining cards
export const getCardImage = (cartasRestantes) => {
  switch (cartasRestantes) {
    case 3:
      return listaUrlCartasVacias[2];
    case 2:
      return listaUrlCartasVacias[1];
    case 1:
      return listaUrlCartasVacias[0];
    default:
      return listaUrlCartasVacias[4];
  }
};

// Function to determine estiloSegunPosicionYNumCartas
export const determineCardStyle = ({
  cartasRestantes,
  pos,
  cartasDispoLength,
  eresEspectador,
  posicionListaCartas,
  esperandoRespuesta,
  jugadorTurno,
  listaEstilos,
}) => {
  let selectedStyle;

  if (cartasRestantes === 3) {
    if (eresEspectador && pos === 0 && cartasDispoLength === 2) {
      selectedStyle = listaEstilos[6];
    } else if (cartasDispoLength === 6) { // Logic for 6-player game
      if (eresEspectador && pos === 0) {
        selectedStyle = listaEstilos[13];
      } else if ((posicionListaCartas + pos) % 2 === 0) {
        if ((posicionListaCartas + SeatDistance.TWO_AWAY) % 6 === pos) {
          selectedStyle = listaEstilos[19]; // arriba derecha
        } else if ((posicionListaCartas + SeatDistance.FOUR_AWAY) % 6 === pos) {
          selectedStyle = listaEstilos[16]; // arriba izquierda
        } else {
          selectedStyle = listaEstilos[9];
        }
      } else {
        if ((posicionListaCartas + SeatDistance.ONE_AWAY) % 6 === pos) {
          selectedStyle = (esperandoRespuesta && jugadorTurno === posicionListaCartas)
            ? listaEstilos[28] // abajo derecha (con respuesta)
            : listaEstilos[22]; // abajo derecha (normal)
        } else if ((posicionListaCartas + SeatDistance.FIVE_AWAY) % 6 === pos) {
          selectedStyle = listaEstilos[25]; // abajo izquierda
        } else {
          selectedStyle = listaEstilos[8];
        }
      }
    } else if (eresEspectador && pos === 0) {
      console.log("Spectator at position 0 with cartasDispoLength:", cartasDispoLength);
      selectedStyle = listaEstilos[13];
    } else if ((posicionListaCartas + pos) % 2 === 0) {
      selectedStyle = listaEstilos[8];
    } else if (
      posicionListaCartas + 1 === pos ||
      posicionListaCartas + 1 > cartasDispoLength
    ) {
      selectedStyle = listaEstilos[7];
    } else if (
      posicionListaCartas - 1 === pos ||
      posicionListaCartas - 1 < 0
    ) {
      selectedStyle = listaEstilos[6];
    } else {
      selectedStyle = listaEstilos[7];
    }
  } else if (cartasRestantes === 2) {
    if (eresEspectador && pos === 0 && cartasDispoLength === 2) {
      selectedStyle = listaEstilos[3];
    } else if (eresEspectador && pos === 0) {
      console.log("Spectator at position 0");
      selectedStyle = listaEstilos[14];
    } else if (cartasDispoLength === 6) { // Logic for 6-player game
      if (eresEspectador && pos === 0) {
        selectedStyle = listaEstilos[20];
      } else if ((posicionListaCartas + pos) % 2 === 0) {
        if ((posicionListaCartas + SeatDistance.TWO_AWAY) % 6 === pos) {
          selectedStyle = listaEstilos[20]; // arriba derecha
        } else if ((posicionListaCartas + SeatDistance.FOUR_AWAY) % 6 === pos) {
          selectedStyle = listaEstilos[17]; // arriba izquierda
        } else {
          selectedStyle = listaEstilos[5];
        }
      } else {
        if ((posicionListaCartas + SeatDistance.ONE_AWAY) % 6 === pos) {
          selectedStyle = (esperandoRespuesta && jugadorTurno === posicionListaCartas)
            ? listaEstilos[29] // abajo derecha (con respuesta)
            : listaEstilos[23]; // abajo derecha (normal)
        } else if ((posicionListaCartas + SeatDistance.FIVE_AWAY) % 6 === pos) {
          console.log("Player at position", pos, "is abajo izquierda");
          selectedStyle = listaEstilos[26]; // abajo izquierda
        } else {
          selectedStyle = listaEstilos[5];
        }
      }
    } else if ((posicionListaCartas + pos) % 2 === 0) {
      selectedStyle = listaEstilos[5];
    } else if (
      posicionListaCartas + 1 === pos ||
      posicionListaCartas + 1 > cartasDispoLength
    ) {
      selectedStyle = listaEstilos[4];
    } else if (
      posicionListaCartas - 1 === pos ||
      posicionListaCartas - 1 < 0
    ) {
      selectedStyle = listaEstilos[3];
    } else {
      selectedStyle = listaEstilos[4];
    }
  } else { // cartasRestantes === 1 or 0
    if (eresEspectador && pos === 0 && cartasDispoLength === 2) {
      selectedStyle = listaEstilos[0];
    } else if (eresEspectador && pos === 0) {
      selectedStyle = listaEstilos[15];
    } else if (cartasDispoLength === 6) { // Logic for 6-player game
      if (eresEspectador && pos === 0) {
        selectedStyle = listaEstilos[13];
      } else if ((posicionListaCartas + pos) % 2 === 0) {
        if ((posicionListaCartas + SeatDistance.TWO_AWAY) % 6 === pos) {
          selectedStyle = listaEstilos[21]; // arriba derecha
        } else if ((posicionListaCartas + SeatDistance.FOUR_AWAY) % 6 === pos) {
          selectedStyle = listaEstilos[18]; // arriba izquierda
        } else {
          selectedStyle = listaEstilos[2];
        }
      } else {
        if ((posicionListaCartas + SeatDistance.ONE_AWAY) % 6 === pos) {
          selectedStyle = (esperandoRespuesta && jugadorTurno === posicionListaCartas)
            ? listaEstilos[30] // abajo derecha (con respuesta)
            : listaEstilos[24]; // abajo derecha (normal)
        } else if ((posicionListaCartas + SeatDistance.FIVE_AWAY) % 6 === pos) {
          console.log("Player at position", pos, "is abajo izquierda");
          selectedStyle = listaEstilos[27]; // abajo izquierda
        } else {
          selectedStyle = listaEstilos[2];
        }
      }
    } else if ((posicionListaCartas + pos) % 2 === 0) {
      selectedStyle = listaEstilos[2];
    } else if (
      posicionListaCartas + 1 === pos ||
      posicionListaCartas + 1 > cartasDispoLength
    ) {
      selectedStyle = listaEstilos[1];
    } else if (
      posicionListaCartas - 1 === pos ||
      posicionListaCartas - 1 < 0
    ) {
      selectedStyle = listaEstilos[0];
    } else {
      selectedStyle = listaEstilos[1];
    }
  }

  console.log(`determineCardStyle: pos=${pos}, cartasRestantes=${cartasRestantes}, selectedStyleIndex=${listaEstilos.indexOf(selectedStyle)}, rotate=${selectedStyle.rotate}`);
  
  return selectedStyle;
};

// Function to determine posicionMazo
export const determinePosicionMazo = ({
  pos,
  jugadorMano,
  cartasDispoLength,
  eresEspectador,
  posicionListaCartas,
  esperandoRespuesta,
  jugadorTurno,
  listaEstilos,
}) => {
  if (pos !== jugadorMano) return { width: '0px' };

  if (posicionListaCartas === jugadorMano && eresEspectador && cartasDispoLength === 2) {
    return listaEstilos[9];
  }

  if (cartasDispoLength === 6) { // Logic for 6-player game
    if (posicionListaCartas === jugadorMano) {
      return listaEstilos[12];
    }

    if ((posicionListaCartas + pos) % 2 === 0) {
      if ((posicionListaCartas + SeatDistance.TWO_AWAY) % 6 === pos) {
        return listaEstilos[33]; // arriba derecha
      } else if ((posicionListaCartas + SeatDistance.FOUR_AWAY) % 6 === pos) {
        return listaEstilos[34]; // arriba izquierda
      } else {
        return listaEstilos[11];
      }
    } else {
      if ((posicionListaCartas + SeatDistance.ONE_AWAY) % 6 === pos) {
        return (esperandoRespuesta && jugadorTurno === posicionListaCartas)
          ? listaEstilos[32] // abajo derecha (con respuesta)
          : listaEstilos[32]; // abajo derecha (normal)
      } else if ((posicionListaCartas + SeatDistance.FIVE_AWAY) % 6 === pos) {
        return listaEstilos[31]; // abajo izquierda
      } else {
        return listaEstilos[11];
      }
    }
  }

  if (posicionListaCartas === jugadorMano) {
    return listaEstilos[12];
  }

  if ((posicionListaCartas + pos) % 2 === 0) {
    return listaEstilos[11];
  }

  if (
    posicionListaCartas - 1 === pos ||
    (posicionListaCartas - 1 < 0 && jugadorMano !== 1)
  ) {
    return listaEstilos[9];
  }

  if (
    posicionListaCartas + 1 === pos ||
    posicionListaCartas + 2 > cartasDispoLength
  ) {
    return listaEstilos[10];
  }

  return listaEstilos[9];
};

// src/components/CartasVolteadas/CartasVolteadas.jsx

import React, { forwardRef } from 'react';
import "../static/css/mano/CartasVolteadas.css";
import {
  listaUrlCartasVacias,
  isSpectator,
  getCardImage,
  determineCardStyle,
  determinePosicionMazo,
} from './cartasVolteadasUtils/cvUtils.js'; // Ensure this path is correct

const CartasVolteadas = forwardRef((props, ref) => {
  const {
    cartasDispo,
    posicionListaCartas: initialPosicionListaCartas,
    jugadorMano,
    numJugadores,
    esperandoRespuesta,
    jugadorTurno,
  } = props;

  // Determine if the user is a spectator
  const eresEspectador = isSpectator(initialPosicionListaCartas);
  const posicionListaCartas = eresEspectador ? 0 : initialPosicionListaCartas;

  // Define card sizes 
  const cartaDeUna = { width: '50px', height: '75px' };
  const cartaDeDos = { width: '75px', height: '75px' };
  const cartaDeTres = { width: '100px', height: '75px' };
  const estiloMazoPropio = { width: '75px', height: '100px' };
  const estiloMazoOtro = { width: '60px', height: '85px' };

  // Define positions and rotations
  const listaEstilos = [
    { ...cartaDeUna, top: '40%', left: '20%', rotate: '90deg' },  //0
    { ...cartaDeUna, top: '40%', right: '20%', rotate: '270deg' },//1
    { ...cartaDeUna, top: '80px', left: '50%', rotate: '180deg' }, //2
    { ...cartaDeDos, top: '40%', left: '20%', rotate: '90deg' },  //3
    { ...cartaDeDos, top: '40%', right: '20%', rotate: '270deg' },//4
    { ...cartaDeDos, top: '80px', left: '50%', rotate: '180deg' }, //5
    { ...cartaDeTres, top: '40%', left: '20%', rotate: '90deg' }, //6
    { ...cartaDeTres, top: '40%', right: '20%', rotate: '270deg' },//7
    { ...cartaDeTres, top: '80px', left: '50%', rotate: '180deg' }, //8
    { ...estiloMazoOtro, top: '20%', left: '21%', rotate: '90deg' },//9
    { ...estiloMazoOtro, top: '20%', right: '21%', rotate: '270deg' },//10
    { ...estiloMazoOtro, top: '80px', left: '38%', rotate: '180deg' },//11
    { ...estiloMazoPropio, top: '81%', left: '30%', rotate: '0deg' }, //12
    { ...cartaDeTres, top: '80%', left: '50%', rotate: '0deg' }, //13
    { ...cartaDeDos, top: '80%', left: '50%', rotate: '0deg' }, //14
    { ...cartaDeUna, top: '80%', left: '50%', rotate: '0deg' }, //15
    // COSAS DE LA PARTIDA DE A 6 
    { ...cartaDeTres, top: '22%', left: '22%', rotate: '120deg' }, //16
    { ...cartaDeDos, top: '22%', left: '22%', rotate: '120deg' }, //17
    { ...cartaDeUna, top: '22%', left: '22%', rotate: '120deg' }, //18
    { ...cartaDeTres, top: '27%', left: '70%', rotate: '250deg' }, //19
    { ...cartaDeDos, top: '27%', left: '70%', rotate: '250deg' }, //20
    { ...cartaDeUna, top: '27%', left: '70%', rotate: '250deg' }, //21
    { ...cartaDeTres, top: '75%', left: '70%', rotate: '-40deg' }, //22
    { ...cartaDeDos, top: '75%', left: '70%', rotate: '-40deg' }, //23
    { ...cartaDeUna, top: '75%', left: '70%', rotate: '-40deg' }, //24
    { ...cartaDeTres, top: '70%', left: '20%', rotate: '40deg' }, //25
    { ...cartaDeDos, top: '70%', left: '20%', rotate: '40deg' }, //26
    { ...cartaDeUna, top: '70%', left: '20%', rotate: '40deg' }, //27
    { ...cartaDeTres, top: '75%', left: '88%', rotate: '-40deg' }, //28
    { ...cartaDeDos, top: '75%', left: '88%', rotate: '-40deg' }, //29
    { ...cartaDeUna, top: '75%', left: '88%', rotate: '-40deg' }, //30
    // MAZOS DE A 6
    { ...estiloMazoOtro, top: '81%', left: '28%', rotate: '40deg' }, //31
    { ...estiloMazoOtro, top: '65%', left: '79%', rotate: '-40deg' }, //32
    { ...estiloMazoOtro, top: '43%', left: '73%', rotate: '250deg' }, //33
    { ...estiloMazoOtro, top: '38%', left: '19%', rotate: '120deg' }, //34
  ];

  return (
    <>
      {cartasDispo.map((cartasDisp, pos) => {
        const cartasRestantes = cartasDisp.filter((item) => item !== null).length;
        const urlCartaVolteadas = getCardImage(cartasRestantes);

        const estiloSegunPosicionYNumCartas = determineCardStyle({
          cartasRestantes,
          pos,
          cartasDispoLength: cartasDispo.length,
          eresEspectador,
          posicionListaCartas,
          esperandoRespuesta,
          jugadorTurno,
          listaEstilos,
        });

        const posicionMazo = determinePosicionMazo({
          pos,
          jugadorMano,
          cartasDispoLength: cartasDispo.length,
          eresEspectador,
          posicionListaCartas,
          esperandoRespuesta,
          jugadorTurno,
          listaEstilos,
        });

        const urlMazo = listaUrlCartasVacias[3]; // mazo.png

        // Debugging logs
        console.log(`Card at pos ${pos}:`, estiloSegunPosicionYNumCartas);
        console.log(`Mazo at pos ${pos}:`, posicionMazo);

        return (
          <React.Fragment key={pos}>
            {/* Always render the mazo */}
            <div
              className="mazoStyle swirlStyle"
              style={{
                top: posicionMazo.top,
                left: posicionMazo.left,
                right: posicionMazo.right,
                width: posicionMazo.width,
                height: posicionMazo.height,
                // Removed rotation from container div
              }}
            >
              <div
                className="relativeContainer"
                style={{
                  transform: `rotate(${posicionMazo.rotate || '0deg'})`,
                }}
              >
                <img
                  src={urlMazo}
                  alt="Mazo"
                  className="mazoImageStyle"
                />
              </div>
            </div>

            {/* Conditionally render cartasVolteadas */}
            {(pos !== posicionListaCartas || eresEspectador) && (
              <div
                className="cardStyle swirlStyle"
                style={{
                  top: estiloSegunPosicionYNumCartas.top,
                  left: estiloSegunPosicionYNumCartas.left,
                  right: estiloSegunPosicionYNumCartas.right,
                  width: estiloSegunPosicionYNumCartas.width,
                  height: estiloSegunPosicionYNumCartas.height,
                  // Removed rotation from container div
                }}
              >
                <div
                  className="relativeContainer"
                  style={{
                    transform: `rotate(${estiloSegunPosicionYNumCartas.rotate || '0deg'})`,
                  }}
                >
                  <img
                    src={urlCartaVolteadas}
                    alt={`Quedan ${cartasRestantes}`}
                    className="imageStyle"
                  />
                  <div className="sunsetOverlay"></div>
                </div>
              </div>
            )}
          </React.Fragment>
        );
      })}
    </>
  );
});

export default CartasVolteadas;

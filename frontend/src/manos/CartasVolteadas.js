import React, { forwardRef } from 'react';
import  "../static/css/mano/CartasVolteadas.css"

const listaUrlCartasVacias = [
  "http://localhost:8080/resources/images/cartas/1carta.jpg",
  "http://localhost:8080/resources/images/cartas/2cartas.jpg",
  "http://localhost:8080/resources/images/cartas/3cartas.jpg",
  "http://localhost:8080/resources/images/cartas/mazo.png",
  "http://localhost:8080/resources/images/cartas/SinCartas.jpg",
];

const CartasVolteadas = forwardRef((props, ref) => {
  let { cartasDispo, posicionListaCartas, jugadorMano, numJugadores, esperandoRespuesta,jugadorTurno } = props;

  // Define card sizes 


  const eresEspectador= typeof posicionListaCartas === 'object'

  if(eresEspectador){
    posicionListaCartas=0
  }
  const cartaDeUna = { width: '50px', height: '75px' };
  const cartaDeDos = { width: '75px', height: '75px' };
  const cartaDeTres = { width: '100px', height: '75px' };
  const estiloMazoPropio = { width: '75px', height: '100px' };
  const estiloMazoOtro = { width: '60px', height: '85px' };

  // Define positions and rotations
  const listaEstilos = [
    {
      ...cartaDeUna,
      top: '40%',
      left: '20%',
      rotate: '90deg',
    },
    {
      ...cartaDeUna,
      top: '40%',
      right: '20%',
      rotate: '270deg',
    },
    {
      ...cartaDeUna,
      top: '80px',
      left: '50%',
      rotate: '180deg',
    },
    {
      ...cartaDeDos,
      top: '40%',
      left: '20%',
      rotate: '90deg',
    },
    {
      ...cartaDeDos,
      top: '40%',
      right: '20%',
      rotate: '270deg',
    },
    {
      ...cartaDeDos,
      top: '80px',
      left: '50%',
      rotate: '180deg',
    },
    {
      ...cartaDeTres,
      top: '40%',
      left: '20%',
      rotate: '90deg',
    },
    {
      ...cartaDeTres,
      top: '40%',
      right: '20%',
      rotate: '270deg',
    }, //7
    {
      ...cartaDeTres,
      top: '80px',
      left: '50%',
      rotate: '180deg',
    },
    {
      ...estiloMazoOtro,
      top: '20%',
      left: '21%',
      rotate: '90deg',
    },
    {
      ...estiloMazoOtro,
      top: '20%',
      right: '21%',
      rotate: '270deg',
    }, //10
    {
      ...estiloMazoOtro,
      top: '80px',
      left: '38%',
      rotate: '180deg',
    },
    {
      ...estiloMazoPropio,
      top: '81%',
      left: '30%',
    },
   { ...cartaDeTres,
    top: '80%',
    left: '50%',
  },
  { ...cartaDeDos,
    top: '80%',
    left: '50%',
  },
  { ...cartaDeUna,
    top: '80%',
    left: '50%',
  }, //15

  //COSAS DE LA PARTIDA DE A 6 
  { ...cartaDeTres,
    top: '22%',
    left: '22%',
    rotate: '120deg'
  },  //16
  
  { ...cartaDeDos,
    top: '22%',
    left: '22%',
    rotate: '120deg'

  },  
  
  { ...cartaDeUna,
    top: '22%',
    left: '22%',
    rotate: '120deg'

  },
  { ...cartaDeTres,
    top: '27%',
    left: '70%',
    rotate: '250deg'

  }, //19
  
  { ...cartaDeDos,
    top: '27%',
    left: '70%',
    rotate: '250deg'
  }, 
  
  { ...cartaDeUna,
    top: '27%',
    left: '70%',
    rotate: '250deg'
  },
  { ...cartaDeTres,
    top: '75%',
    left: '70%',
    rotate: '-40deg'
  },  //22
  
  { ...cartaDeDos,
    top: '75%',
    left: '70%',
    rotate: '-40deg'
  },  
  
  { ...cartaDeUna,
    top: '75%',
    left: '70%',
    rotate: '-40deg'
  },
  { ...cartaDeTres,
    top: '70%',
    left: '20%',
    rotate: '40deg'

  },   //25
  
  { ...cartaDeDos,
    top: '70%',
    left: '20%',
    rotate: '40deg'
  },  
  { ...cartaDeUna,
    top: '70%',
    left: '20%',
    rotate: '40deg'
  },
  ////
  { ...cartaDeTres,
    top: '75%',
    left: '88%',
    rotate: '-40deg'
  },  //28
  
  { ...cartaDeDos,
    top: '75%',
    left: '88%',
    rotate: '-40deg'
  },  
  
  { ...cartaDeUna,
    top: '75%',
    left: '88%',
    rotate: '-40deg'
  }, //30
  
  //MAZOS DE A 6
  {
    ...estiloMazoOtro,
    top: '81%',
    left: '28%',
    rotate: '40deg'
  },
  {
    ...estiloMazoOtro,
    top: '65%',
    left: '79%',
    rotate: '-40deg'
  }, //32
  {
    ...estiloMazoOtro,
    top: '43%',
    left: '73%',
    rotate: '250deg'
  },
  {
    ...estiloMazoOtro,
    top: '38%',
    left: '19%',
    rotate: '120deg'
  }, //34
 
  ];

  return (
    <>
      {cartasDispo.map((cartasDisp, pos) => {
        // Filter out null items
        const cartasRestantes = cartasDisp.filter((item) => item !== null).length;

        // Determine which card image to use
        let urlCartaVolteadas;
        let urlMazo = listaUrlCartasVacias[3];
        if (cartasRestantes === 3)
          urlCartaVolteadas = listaUrlCartasVacias[2];
        else if (cartasRestantes === 2)
          urlCartaVolteadas = listaUrlCartasVacias[1];
        else if (cartasRestantes === 1)
          urlCartaVolteadas = listaUrlCartasVacias[0];
        else
          urlCartaVolteadas = listaUrlCartasVacias[4];

        // Determine style based on position and number of cards
        let estiloSegunPosicionYNumCartas;
        if (cartasRestantes === 3) {
          if(eresEspectador && pos==0 && cartasDispo.length==2){
            estiloSegunPosicionYNumCartas = listaEstilos[6];
          }


          
          else if(cartasDispo.length==6){ //Todos los IF de la partida de a 6
            if(eresEspectador && pos==0){
              estiloSegunPosicionYNumCartas=listaEstilos[13]; 
            }
            else if((posicionListaCartas + pos) % 2 === 0){
              if((posicionListaCartas + 2) % 6 == pos){
                estiloSegunPosicionYNumCartas=listaEstilos[19]; //arriba derecha
              }
              else if((posicionListaCartas + 4) % 6 == pos ){
                estiloSegunPosicionYNumCartas=listaEstilos[16]; //arriba izquierda
              }
              else estiloSegunPosicionYNumCartas=listaEstilos[9];
            }

            else {
              if((posicionListaCartas + 1) % 6 == pos){
                if(esperandoRespuesta && jugadorTurno==posicionListaCartas){
                  estiloSegunPosicionYNumCartas=listaEstilos[28]; //abajo derecha
                }
                else{
                  estiloSegunPosicionYNumCartas=listaEstilos[22];
                }
              }
              else if((posicionListaCartas + 5) % 6 == pos){
                estiloSegunPosicionYNumCartas=listaEstilos[25]; //abajo izquierda
              }
              else estiloSegunPosicionYNumCartas=listaEstilos[8];

                }

          }

          else if(eresEspectador && pos===0 ){
            console.log("HOLAAAAAAAAAAAAAAAAAAA SOY ESPECTADOOOOOOOOOOOOOOOOOOOOOOR Y ESTA ES LA LISTAAAAAAAAAAAA     " + cartasDispo.length)
            estiloSegunPosicionYNumCartas = listaEstilos[13];
          }
          else if ((posicionListaCartas + pos) % 2 === 0)
            estiloSegunPosicionYNumCartas = listaEstilos[8];
          else if (
            posicionListaCartas + 1 === pos ||
            posicionListaCartas + 1 > cartasDispo.length
          )
            estiloSegunPosicionYNumCartas = listaEstilos[7];
          else if (posicionListaCartas - 1 === pos || posicionListaCartas - 1 < 0)
            estiloSegunPosicionYNumCartas = listaEstilos[6];
          else if (posicionListaCartas - 1 === pos || posicionListaCartas - 1 < 0)
            estiloSegunPosicionYNumCartas = listaEstilos[6];
          else
            estiloSegunPosicionYNumCartas = listaEstilos[7];
        } else if (cartasRestantes === 2) {
          if(eresEspectador && pos==0 && cartasDispo.length==2){
            estiloSegunPosicionYNumCartas = listaEstilos[3];
          }
          else if(eresEspectador && pos===0){
            console.log("HOLAAAAAAAAAAAAAAAAAAA SOY ESPECTADOOOOOOOOOOOOOOOOOOOOOOR")
            estiloSegunPosicionYNumCartas = listaEstilos[14];
          }

          else if(cartasDispo.length==6){ //Todos los IF de la partida de a 6

            if(eresEspectador && pos==0){
              estiloSegunPosicionYNumCartas=listaEstilos[13]; 
            }
            else if((posicionListaCartas + pos) % 2 === 0){
              if((posicionListaCartas + 2) % 6 == pos){
                estiloSegunPosicionYNumCartas=listaEstilos[20]; //arriba derecha
              }
              else if((posicionListaCartas + 4) % 6 == pos ){
                estiloSegunPosicionYNumCartas=listaEstilos[17]; //arriba izquierda
              }
              else estiloSegunPosicionYNumCartas=listaEstilos[5];
            }

            else {
              if((posicionListaCartas + 1) % 6 == pos){
                if(esperandoRespuesta && jugadorTurno==posicionListaCartas){
                  estiloSegunPosicionYNumCartas=listaEstilos[29]; //abajo derecha
                }
                else{
                  estiloSegunPosicionYNumCartas=listaEstilos[23];
                }
              }
              else if((posicionListaCartas + 5) % 6 == pos){
                console.log("¿Soy el jugador 3? y estoy aqui");
                console.log(posicionListaCartas);

                estiloSegunPosicionYNumCartas=listaEstilos[26]; //abajo izquierda
              }
              else estiloSegunPosicionYNumCartas=listaEstilos[5];

                }

          }
          else if ((posicionListaCartas + pos) % 2 === 0)
            estiloSegunPosicionYNumCartas = listaEstilos[5];
          else if (
            posicionListaCartas + 1 === pos ||
            posicionListaCartas + 1 > cartasDispo.length
          )
            estiloSegunPosicionYNumCartas = listaEstilos[4];
          else if (posicionListaCartas - 1 === pos || posicionListaCartas - 1 < 0)
            estiloSegunPosicionYNumCartas = listaEstilos[3];
          else
            estiloSegunPosicionYNumCartas = listaEstilos[4];
        } else {
          if(eresEspectador && pos==0 && cartasDispo.length==2){
            estiloSegunPosicionYNumCartas = listaEstilos[0];
          }
          if(eresEspectador && pos===0){
            estiloSegunPosicionYNumCartas = listaEstilos[15];
          }

          else if(cartasDispo.length==6){ //Todos los IF de la partida de a 6
            if(eresEspectador && pos==0){
              estiloSegunPosicionYNumCartas=listaEstilos[13]; 
            }
            else if((posicionListaCartas + pos) % 2 === 0){
              if((posicionListaCartas + 2) % 6 == pos){
                estiloSegunPosicionYNumCartas=listaEstilos[21]; //arriba derecha
              }
              else if((posicionListaCartas + 4) % 6 == pos ){
                estiloSegunPosicionYNumCartas=listaEstilos[18]; //arriba izquierda
              }
              else estiloSegunPosicionYNumCartas=listaEstilos[2];
            }

            else {
              if((posicionListaCartas + 1) % 6 == pos){
                if(esperandoRespuesta && jugadorTurno==posicionListaCartas){
                  estiloSegunPosicionYNumCartas=listaEstilos[30]; //abajo derecha
                }
                else{
                  estiloSegunPosicionYNumCartas=listaEstilos[24];
                }
              }
              else if((posicionListaCartas + 5) % 6 == pos){
                console.log("¿Soy el jugador 3? y estoy aqui");
                console.log(posicionListaCartas);

                estiloSegunPosicionYNumCartas=listaEstilos[27]; //abajo izquierda
              }
              else estiloSegunPosicionYNumCartas=listaEstilos[2];

                }

          }

          else if ((posicionListaCartas + pos) % 2 === 0)
            estiloSegunPosicionYNumCartas = listaEstilos[2];
          else if (
            posicionListaCartas + 1 === pos ||
            posicionListaCartas + 1 > cartasDispo.length
          )
            estiloSegunPosicionYNumCartas = listaEstilos[1];
          else if (posicionListaCartas - 1 === pos || posicionListaCartas - 1 < 0)
            estiloSegunPosicionYNumCartas = listaEstilos[0];
          else
            estiloSegunPosicionYNumCartas = listaEstilos[1];
        }

        console.log(estiloSegunPosicionYNumCartas)

        let posicionMazo = { width: '0px' };

        if (pos === jugadorMano) {
          if (posicionListaCartas === jugadorMano && eresEspectador && cartasDispo.length==2){
                        posicionMazo = listaEstilos[9];
          }

          else if (cartasDispo.length == 6) { 
            // Todos los IF para los mazos de la partida de a 6
          
            if (posicionListaCartas === jugadorMano) {
              posicionMazo = listaEstilos[12];
            }
            else if ((posicionListaCartas + pos) % 2 === 0) {
              // 2 seats away
              if ((posicionListaCartas + 2) % 6 === pos) {
                posicionMazo = listaEstilos[33]; // arriba derecha
              }
              // 4 seats away
              else if ((posicionListaCartas + 4) % 6 === pos) {
                posicionMazo = listaEstilos[34]; // arriba izquierda
              }
              else {
                posicionMazo = listaEstilos[11];
              }
            }
            else {
              // 1 seat away
              if ((posicionListaCartas + 1) % 6 === pos) {
                // Ejemplo de lógica adicional
                if (esperandoRespuesta && jugadorTurno === posicionListaCartas) {
                  posicionMazo = listaEstilos[32]; // abajo derecha (con respuesta)
                } else {
                  posicionMazo = listaEstilos[32]; // abajo derecha (normal)
                }
              }
              // 5 seats away
              else if ((posicionListaCartas + 5) % 6 === pos) {
                posicionMazo = listaEstilos[31]; // abajo izquierda
              }
              else {
                posicionMazo = listaEstilos[11];
              }
            }
          }
          

          else if (posicionListaCartas === jugadorMano)
            posicionMazo = listaEstilos[12];
          else if ((posicionListaCartas + pos) % 2 === 0)
            posicionMazo = listaEstilos[11];
          else if (
            posicionListaCartas - 1 === pos ||
            (posicionListaCartas - 1 < 0 && jugadorMano !== 1)
          )
            posicionMazo = listaEstilos[9];
          else if (
            posicionListaCartas + 1 === pos ||
            posicionListaCartas + 2 > cartasDispo.length
          )
            posicionMazo = listaEstilos[10];
          else
            posicionMazo = listaEstilos[9];
        }

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
              }}
            >
              <div
                className="fullSize"
                style={{
                  transform: `rotate(${posicionMazo.rotate || '0deg'})`,
                }}
              >
                <div className="relativeContainer">
                  <img
                    src={urlMazo}
                    alt="Mazo"
                    className="mazoImageStyle"
                  />
                </div>
              </div>
            </div>

            {/* Conditionally render cartasVolteadas */}
            {(pos !== posicionListaCartas || eresEspectador) &&(
              <div
                className="cardStyle swirlStyle"
                style={{
                  top: estiloSegunPosicionYNumCartas.top,
                  left: estiloSegunPosicionYNumCartas.left,
                  right: estiloSegunPosicionYNumCartas.right,
                  width: estiloSegunPosicionYNumCartas.width,
                  height: estiloSegunPosicionYNumCartas.height,
                }}
              >
                <div
                  className="fullSize"
                  style={{
                    transform: `rotate(${estiloSegunPosicionYNumCartas.rotate})`,
                  }}
                >
                  <div className="relativeContainer">
                    <img
                      src={urlCartaVolteadas}
                      alt={`Quedan ${cartasRestantes}`}
                      className="imageStyle"
                    />
                    <div className="sunsetOverlay"></div>
                  </div>
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
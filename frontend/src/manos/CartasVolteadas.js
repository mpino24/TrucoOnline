import React, { forwardRef, useEffect } from 'react';

const listaUrlCartasVacias = [
  "http://localhost:8080/resources/images/cartas/1carta.jpg",
  "http://localhost:8080/resources/images/cartas/2cartas.jpg",
  "http://localhost:8080/resources/images/cartas/3cartas.jpg",
  "http://localhost:8080/resources/images/cartas/mazo.png",
  "http://localhost:8080/resources/images/cartas/SinCartas.jpg",
];

const CartasVolteadas = forwardRef((props, ref) => {
  const { cartasDispo, posicionListaCartas, jugadorMano, numJugadores } = props;

  // Inject keyframes for animations once
  useEffect(() => {
    const styleSheet = document.createElement('style');
    styleSheet.type = 'text/css';
    styleSheet.innerText = `
      @keyframes holoGlow {
        0%, 100% { opacity: 1; }
        50% { opacity: 0.8; }
      }

      @keyframes slightSwirl {
        0% { transform: rotate(0deg); }
        25% { transform: rotate(2deg); }
        50% { transform: rotate(0deg); }
        75% { transform: rotate(-2deg); }
        100% { transform: rotate(0deg); }
      }

      @keyframes dropShadowGlow {
        0%, 100% {
          filter: drop-shadow(0 0 10px rgba(255, 140, 0, 0.6));
        }
        50% {
          filter: drop-shadow(0 0 20px rgba(255, 183, 76, 0.8));
        }
      }
    `;
    document.head.appendChild(styleSheet);

    // Cleanup on unmount
    return () => {
      document.head.removeChild(styleSheet);
    };
  }, []);

  // Define the sunset-like overlay style
  const sunsetOverlay = {
    position: 'absolute',
    top: 0,
    left: 0,
    width: '140%',
    height: '140%',
    background: `
      radial-gradient(
        circle at 50% 50%, 
        rgba(255, 223, 186, 0.5) 0%, 
        rgba(255, 183, 76, 0.5) 40%, 
        rgba(255, 140, 0, 0.6) 70%
      )
    `,
    backgroundSize: '400% 400%',
    animation: 'holoGlow 3s ease-in-out infinite',
    mixBlendMode: 'overlay',
    pointerEvents: 'none',
    borderRadius: '8px',
  };

  // Swirl animation style
  const swirlStyle = {
    animation: 'slightSwirl 5s ease-in-out infinite',
  };

  // Define card sizes
  const cartaDeUna = {
    width: '50px',
    height: '75px',
  };

  const cartaDeDos = {
    width: '75px',
    height: '75px',
  };

  const cartaDeTres = {
    width: '100px',
    height: '75px',
  };

  const estiloMazoPropio = {
    width: '75px',
    height: '100px',
  };

  const estiloMazoOtro = {
    width: '60px',
    height: '85px',
  };

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
      top: '70px',
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
      top: '70px',
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
      top: '70px',
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
      top: '70px',
      left: '38%',
      rotate: '180deg',
    },
    {
      ...estiloMazoPropio,
      top: '81%',
      left: '30%',
    },
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
          if ((posicionListaCartas + pos) % 2 === 0)
            estiloSegunPosicionYNumCartas = listaEstilos[8];
          else if (
            posicionListaCartas + 1 === pos ||
            posicionListaCartas + 1 > cartasDispo.length
          )
            estiloSegunPosicionYNumCartas = listaEstilos[7];
          else if (posicionListaCartas - 1 === pos || posicionListaCartas - 1 < 0)
            estiloSegunPosicionYNumCartas = listaEstilos[6];
          else
            estiloSegunPosicionYNumCartas = listaEstilos[7];
        } else if (cartasRestantes === 2) {
          if ((posicionListaCartas + pos) % 2 === 0)
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
          if ((posicionListaCartas + pos) % 2 === 0)
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

        // Logging for debugging
        console.log("Eres el jugador: " + posicionListaCartas);
        console.log("Posicion siendo recorrida: " + pos);
        console.log("");
        console.log("Resultado de la condicion: ", posicionListaCartas - 1 === pos || posicionListaCartas - 1 < 0);
        console.log("Deberia entrar al bucle de mano?: ", pos === jugadorMano);
        console.log("");

        // Initialize posicionMazo with default values
        let posicionMazo = { width: '0px' };

        if (pos === jugadorMano) {
          console.log("Efectivamente ha entrado en el bucle de la mano");
          console.log("");

          if (posicionListaCartas === jugadorMano)
            posicionMazo = listaEstilos[12];
          else if ((posicionListaCartas + pos) % 2 === 0)
            posicionMazo = listaEstilos[11];
          else if (posicionListaCartas - 1 === pos || (posicionListaCartas - 1 < 0 && jugadorMano!=1))
            posicionMazo = listaEstilos[9];
          else if (
            posicionListaCartas + 1 === pos ||
            posicionListaCartas + 2 > cartasDispo.length
          )
            posicionMazo = listaEstilos[10];
          else
            posicionMazo = listaEstilos[9];
        }

        // Combine styles for the cartasVolteadas (cards)
        const cardStyle = {
          position: 'fixed',
          margin: '5px',
          top: estiloSegunPosicionYNumCartas.top,
          left: estiloSegunPosicionYNumCartas.left,
          right: estiloSegunPosicionYNumCartas.right,
          width: estiloSegunPosicionYNumCartas.width,
          height: estiloSegunPosicionYNumCartas.height,
          transform: 'translateX(-50%)',
          ...swirlStyle,
        };

        // Combine styles for the mazo (deck)
        const mazoStyle = {
          position: 'fixed',
          margin: '5px',
          top: posicionMazo.top,
          left: posicionMazo.left,
          right: posicionMazo.right,
          width: posicionMazo.width,
          height: posicionMazo.height,
          transform: 'translateX(-50%)',
          ...swirlStyle,
        };

        // Image styles
        const imageStyle = {
          width: '140%',
          height: '140%',
          display: 'block',
          borderRadius: '8px',
        };

        const mazoImageStyle = {
          ...imageStyle,
          animation: 'dropShadowGlow 3s ease-in-out infinite',
        };

        return (
          <React.Fragment key={pos}>
            {/* Always render the mazo */}
            <div style={mazoStyle}>
              <div
                style={{
                  width: '100%',
                  height: '100%',
                  transform: `rotate(${posicionMazo.rotate || '0deg'})`,
                }}
              >
                <div
                  style={{
                    position: 'relative',
                    width: '100%',
                    height: '100%',
                    
                  }}
                >
                  <img
                    src={urlMazo}
                    alt="Mazo"
                    style={mazoImageStyle}
                  />
                </div>
              </div>
            </div>

            {/* Conditionally render cartasVolteadas */}
            {pos !== posicionListaCartas && (
              <div style={cardStyle}>
                <div
                  style={{
                    width: '100%',
                    height: '100%',
                    transform: `rotate(${estiloSegunPosicionYNumCartas.rotate})`,
                  }}
                >
                  <div
                    style={{
                      position: 'relative',
                      width: '100%',
                      height: '100%',
                    }}
                  >
                    <img
                      src={urlCartaVolteadas}
                      alt={`Quedan ${cartasRestantes}`}
                      style={imageStyle}
                    />
                    <div style={sunsetOverlay}></div>
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

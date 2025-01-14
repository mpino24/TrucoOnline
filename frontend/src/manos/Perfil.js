import useFetchState from "../util/useFetchState";
import React, { useState, forwardRef, useEffect } from 'react';
import tokenService from "../services/token.service";
import "../static/css/mano/perfil.css";
import { Client } from "@stomp/stompjs";
import { MdEmojiEmotions } from "react-icons/md";
import GestosMenu from "./GestosMenu";
import ReactDOM from "react-dom";

const jwt = tokenService.getLocalAccessToken();

// Renders a single player's profile picture and name.
const renderJugador = (persona, index) => {
  return (
    <div
      style={{
        position: 'relative',
        width: 80,
        height: 80,
        display: 'inline-block',
        textAlign: 'center'
      }}
      key={index}
    >
      <p className="swirl-glow-textFoto">{persona.userName}</p>
      <img
        style={{
          height: 80,
          width: 80,
          borderRadius: 500,
          objectFit: 'cover',
          margin: 40,
          left: '0%',
          position: 'absolute'
        }}
        src={persona.foto}
        alt={`Foto perfil jugador ${index}`}
        onError={(e) => (e.target.style.display = 'none')}
      />
    </div>
  );
};

// Fixed distribution as fallback.
const DistribuirJugadoresFijos = ({ perfil, game }) => {  
  return perfil.map((persona, index) => {
    let playerStyle = {};
    if (game.numJugadores === 2) {
      if (index === 0) {
        playerStyle = {
          position: 'absolute',
          top: '50%',
          right: '10%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      } else {
        playerStyle = {
          position: 'absolute',
          top: '50%',
          left: '10%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      }
    } else if (game.numJugadores === 4){
      if (index === 2) {
        playerStyle = {
          position: 'absolute',
          top: '7%',
          left: '58%',
          transform: 'translateX(-50%)',
          zIndex: 1000
        };
      } else if (index === 3) {
        playerStyle = {
          position: 'absolute',
          top: '57%',
          left: '10%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      } else if (index === 0) {
        playerStyle = {
          position: 'absolute',
          bottom: '7%',
          left: '58%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      } else if (index === 1) {
        playerStyle = {
          position: 'absolute',
          top: '50%',
          right: '10%',
          transform: 'translateX(-50%)',
          zIndex: 1000
        };
      }
    } else if (game.numJugadores === 6) { 
      // Use the original fixed positions for 6 players.
      let posicion = persona.posicion;
      if (posicion === 0) { 
        playerStyle = { 
          position: 'absolute', 
          bottom: '7%',
          left: '49%', 
          transform: 'translateX(-50%)', 
          zIndex: 1000 
        }; 
      } else if (posicion === 1) { 
        playerStyle = { 
          position: 'absolute', 
          bottom: '25%',
          left: '83%',
          transform: 'translateX(-50%)', 
          zIndex: 1000 
        }; 
      } else if (posicion === 2) { 
        playerStyle = { 
          position: 'absolute', 
          top: '25%',
          left: '85%',
          transform: 'translateX(-50%)', 
          zIndex: 1000 
        }; 
      } else if (posicion === 3) { 
        playerStyle = { 
          position: 'absolute', 
          top: '7%',
          left: '62%',
          transform: 'translateY(-50%)', 
          zIndex: 1000 
        }; 
      } else if (posicion === 4) { 
        playerStyle = { 
          position: 'absolute', 
          top: '25%',
          left: '5%',
          transform: 'translateY(-50%)', 
          zIndex: 1000 
        }; 
      } else if (posicion === 5) { 
        playerStyle = { 
          position: 'absolute', 
          bottom: '25%',
          left: '10%',
          transform: 'translateX(-50%)', 
          zIndex: 1000 
        }; 
      }
    }
    return (
      <div key={index} style={playerStyle}>
        {renderJugador(persona, index)}
      </div>
    );
  });
};

// Dynamic distribution uses your own position to “rotate” the profiles.
const DistribuirJugadoresDinamicos = ({ perfil, game, posicionJugador }) => {
  let posicionUsada = false;
  let posicionUsadaInferior = false;
  return perfil.map((persona, index) => {
    let posicionJug = persona.posicion;
    // Skip yourself
    if (posicionJug === posicionJugador) return null;
    let playerStyle = {};

    if (game.numJugadores === 2) {
      // For 2-player matches, the opponent goes to the right if you’re player 0, left if you’re player 1.
      if (posicionJugador === 0) {
        playerStyle = {
          position: 'absolute',
          top: '50%',
          right: '10%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      } else if (posicionJugador === 1) {
        playerStyle = {
          position: 'absolute',
          top: '50%',
          left: '10%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      }
    } else if (game.numJugadores === 4) {
      // For 4-player matches:
      // - A teammate (same parity as you) is placed at the top center.
      // - The other two players go left and right.
      if (index % 2 === posicionJugador % 2) {
        playerStyle = {
          position: 'absolute',
          top: '7%',
          left: '60%',
          transform: 'translateX(-50%)',
          zIndex: 1000
        };
      } else if (!posicionUsada) {
        posicionUsada = true;
        playerStyle = {
          position: 'absolute',
          top: '45%',
          left: '5%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      } else {
        playerStyle = {
          position: 'absolute',
          top: '42%',
          right: '10%',
          transform: 'translateY(-50%)',
          zIndex: 1000
        };
      }
    } else if (game.numJugadores === 6) {
      // For 6-player matches, here is one possible approach:
      // (Note: Adjust these values as needed for your design.)
      if (posicionJug % 2 === posicionJugador % 2) {
        // Teammates: place them near the top.
        if (posicionJug === obtenerPosicionEquipo(posicionJugador, 2, 6)) {
          playerStyle = {
            position: 'absolute',
            top: '20%',
            left: '80%',
            zIndex: 1000
          };
        } else if (posicionJug === obtenerPosicionEquipo(posicionJugador, 4, 6)) {
          playerStyle = {
            position: 'absolute',
            top: '20%',
            left: '7%',
            zIndex: 1000
          };
        }
      } else {
        // Opponents: assign positions along the lower/sides.
        if (!posicionUsadaInferior) {
          posicionUsadaInferior = true;
          playerStyle = {
            position: 'absolute',
            bottom: '16%',
            left: '87%',
            transform: 'translateX(-50%)',
            zIndex: 1000
          };
        } else if (!posicionUsada) {
          posicionUsada = true;
          playerStyle = {
            position: 'absolute',
            top: '10%',
            right: '40%',
            transform: 'translateY(-50%)',
            zIndex: 1000
          };
        } else {
          playerStyle = {
            position: 'absolute',
            top: '84%',
            left: '10%',
            transform: 'translateY(-50%)',
            zIndex: 1000
          };
        }
      }
    }
    return (
      <div key={index} style={playerStyle}>
        {renderJugador(persona, index)}
      </div>
    );
  });
};

function obtenerPosicionEquipo(posicion, incremento, longitud) { 
  return (posicion + incremento) % longitud; 
}

// Main Perfil component.
// This version uses ReactDOM.createPortal to render the overlay in document.body
// so that its stacking context is independent of PlayingModal.
const Perfil = forwardRef((props, ref) => {
  const game = props.game;
  const posicionJugador = Number(props.posicion); // Ensure numeric
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [perfil, setPerfil] = useFetchState(
    [],
    `/api/v1/partidajugador/jugadores/codigoPartida/${game.codigo}`,
    jwt,
    setMessage,
    setVisible
  );

  // --- Gestos code ---
  const [gestosVisible, setGestosVisible] = useState(false);
  const [stompClient, setStompClient] = useState(null);
  const [imagenOriginal, setImagenOriginal] = useState(null);
  const toggleGestos = () => setGestosVisible(!gestosVisible);

  useEffect(() => {
    const cliente = new Client({
      brokerURL: "ws://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/logoChico.png/ws",
      connectHeaders: {
        Authorization: `Bearer ${jwt}`
      }
    });

    cliente.onConnect = () => {
      console.log("Conectado exitosamente");
      cliente.subscribe(`/topic/partida/${props.game.id}`, (partJugador) => {
        const partJugadorParse = JSON.parse(partJugador.body);
        setPerfil(perfil.map(p =>
          p.userName === partJugadorParse.userName ? partJugadorParse : p
        ));
      });
    };

    cliente.onDisconnect = () => {
      console.log("Desconectado del servidor STOMP");
    };

    cliente.onStompError = (frame) => {
      console.error("Error de STOMP:", frame.headers["message"]);
      console.error("Detalles:", frame.body);
    };

    cliente.activate();
    setStompClient(cliente);

    return () => {
      if (cliente) cliente.deactivate();
    };
  }, [perfil, props.game.id, setPerfil]);

  const enviarGesto = (gestoUrl) => {
    if (!imagenOriginal && perfil[posicionJugador]) {
      setImagenOriginal(perfil[posicionJugador].foto);
    }
    const partJugador = perfil[posicionJugador];
    partJugador.foto = gestoUrl;
    if (stompClient && stompClient.connected && partJugador) {
      stompClient.publish({
        destination: "/app/partjugador",
        body: JSON.stringify(partJugador),
        headers: {
          Authorization: `Bearer ${jwt}`
        }
      });
    } else {
      console.error("STOMP aún no está listo o no está conectado");
    }
  };

  const gestosDisp = [
    { nombre: "1 Espada", photo: "/gestos/unoespada.png", codigo: "UNOESPADA" },
    { nombre: "1 Basto", photo: "/gestos/unobasto.png", codigo: "UNOBASTO" },
    { nombre: "7 Espada", photo: "/gestos/sieteespada.png", codigo: "SIETEESPADA" },
    { nombre: "7 Oro", photo: "/gestos/sieteoro.png", codigo: "SIETEORO" },
    { nombre: "Los 3", photo: "/gestos/tres.png", codigo: "TRES" },
    { nombre: "Los 2", photo: "/gestos/dos.png", codigo: "DOS" },
    { nombre: "1 Copa o 1 Oro", photo: "/gestos/unocopaoro.png", codigo: "UNOCOPAORO" },
    { nombre: "Son malas", photo: "/gestos/malas.png", codigo: "MALAS" },
    { nombre: "Corazón", photo: "/gestos/corazon.png", codigo: "CORAZON" },
    { nombre: "Calavera", photo: "/gestos/calavera.gif", codigo: "CALAVERA" },
    { nombre: "Tiempo", photo: "/gestos/tiempo.png", codigo: "TIEMPO" },
    { nombre: "Estrella", photo: "/gestos/estrella.png", codigo: "ESTRELLA" }
  ];

  // Build overlay content with two layers:
  // Layer 1: Profiles (non-interactive)
  // Layer 2: Gestos (interactive)
  const overlayContent = (
    <>
      {/* Layer 1: Player profiles */}
      <div
        className="perfil-profiles-layer"
        style={{
          position: 'fixed',
          top: 0,
          left: 0,
          width: '100vw',
          height: '100vh',
          pointerEvents: 'none',
          zIndex: 1500
        }}
      >
        {Number.isNaN(posicionJugador)
          ? (<DistribuirJugadoresFijos perfil={perfil} game={game} />)
          : (<DistribuirJugadoresDinamicos
                perfil={perfil}
                game={game}
                posicionJugador={posicionJugador}
              />)
        }
      </div>
      {/* Layer 2: Gestos */}
      <div
        className="perfil-gestos-layer"
        style={{
          position: 'fixed',
          top: 0,
          left: 0,
          width: '100vw',
          height: '100vh',
          pointerEvents: 'none',
          zIndex: 1600
        }}
      >
        <MdEmojiEmotions
          style={{
            fontSize: '50px',
            color: 'yellow',
            position: 'fixed',
            bottom: '5px',
            left: '5px',
            cursor: 'pointer',
            pointerEvents: 'auto'
          }}
          onClick={toggleGestos}
        />
        {perfil[posicionJugador] && gestosVisible && (
          <img
            src={perfil[posicionJugador].foto}
            alt="Foto del usuario"
            style={{
              width: '50px',
              height: '50px',
              borderRadius: '50%',
              marginLeft: '10px',
              position: 'fixed',
              bottom: '5px',
              left: '60px',
              zIndex: 1600,
              pointerEvents: 'auto'
            }}
          />
        )}
        {gestosVisible && (
          <div
            style={{
              position: 'fixed',
              bottom: '60px',
              left: '5px',
              zIndex: 1600,
              pointerEvents: 'auto'
            }}
          >
            <GestosMenu
              partJugador={perfil[posicionJugador]}
              enviarGesto={enviarGesto}
              gestosDisp={gestosDisp}
              imagenOriginal={imagenOriginal}
            />
          </div>
        )}
      </div>
    </>
  );

  return ReactDOM.createPortal(overlayContent, document.body);
});

export default Perfil;

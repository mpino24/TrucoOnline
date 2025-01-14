import React, { useState, forwardRef, useEffect, useRef } from 'react';
import { IoCloseCircle } from "react-icons/io5";
import { TbFlower, TbFlowerOff } from "react-icons/tb";
import { FaUserFriends } from "react-icons/fa";
import { IoIosSend } from "react-icons/io";
import { Client } from "@stomp/stompjs";
import { useNavigate } from 'react-router-dom';
import { CSSTransition } from 'react-transition-group';
import tokenService from "../services/token.service.js";
import EquipoView from '../game/EquipoView.js';
import LeavingGameModal from '../components/LeavingGameModal';
import ExpeledModal from './ExpeledModal';

// <<--- Import the background music for the music controls --->
import backgroundMusic from '../static/audios/musicaDeFondo.mp3';

const WaitingModal = forwardRef((props, ref) => {
  const { game } = props;
  const [connectedUsers, setConnectedUsers] = useState(0);
  const [jugadores, setJugadores] = useState([]);
  const [leavingModal, setLeavingModal] = useState(false);
  const [expeledView, setExpeledView] = useState(false);
  const [friendList, showFriendList] = useState(false);
  const [friends, setFriends] = useState([]);
  const [numDesconectados, setNumDesconectados] = useState(0);
  const [stompClient, setStompClient] = useState(null);

  // <<--- ADD: Music Controls State and Handlers --->
  const [isPlaying, setIsPlaying] = useState(false);
  const [volumeVisible, setVolumeVisible] = useState(false);
  const [volume, setVolume] = useState(50); // default volume is 50%
  const audioRef = useRef(null);

  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = volume / 100;
    }
  }, [volume]);

  const handlePlayMusic = () => {
    if (audioRef.current) {
      audioRef.current
        .play()
        .then(() => {
          setIsPlaying(true);
          setVolumeVisible(true);
        })
        .catch((error) => {
          console.error("Error playing audio:", error);
        });
    }
  };

  const handlePauseMusic = () => {
    if (audioRef.current) {
      audioRef.current.pause();
      setIsPlaying(false);
    }
  };

  const handleVolumeChange = (event) => {
    const sliderValue = event.target.value;
    setVolume(sliderValue);
    if (audioRef.current) {
      audioRef.current.volume = sliderValue / 100;
    }
  };

  // ------------------------- Original WaitingModal logic -------------------------

  const usuario = tokenService.getUser();
  const jwt = tokenService.getLocalAccessToken();
  const navigate = useNavigate();

  useEffect(() => {
    let connected = null;
    function fetchPlayers() {
      fetch(`/api/v1/partidajugador/players?partidaCode=${game.codigo}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      })
        .then(response => response.json())
        .then(data => {
          setJugadores(data);
          setConnectedUsers(data.length);
          const inTheGame = data.some(pj => pj.player.id === usuario.id);
          if (connected === null) {
            connected = inTheGame;
          }
          if (connected && !inTheGame) {
            setExpeledView(true);
          }
          setNumDesconectados(data.filter(jugador => !jugador.player.isConnected).length);
        });
    }
    fetchPlayers();
    const timer = setInterval(fetchPlayers, 2000);
    return () => clearInterval(timer);
  }, [game.codigo, usuario.id, jwt, navigate]);

  function getGameCreator() {
    if (jugadores.length > 0) {
      const currentCreator = jugadores.find(pj => pj.isCreator === true);
      return currentCreator?.player || null;
    }
    return null;
  }

  function getJugadoresEquipo(equipo) {
    return jugadores.filter(jugadorPartida => jugadorPartida.equipo === "EQUIPO" + equipo);
  }

  function startGame() {
    if (numDesconectados > 0) {
      alert(`No se puede comenzar la partida, hay jugadores desconectados. Considere expulsarlos: ${numDesconectados}`);
    } else {
      if (getJugadoresEquipo(1).length === game.numJugadores / 2 && getJugadoresEquipo(2).length === game.numJugadores / 2) {
        fetch(`/api/v1/partida/${game.codigo}/start`, {
          method: "PATCH",
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        })
          .then(response => {
            if (!response.ok) {
              return response.text().then(errorMessage => {
                throw new Error("Error al empezar la partida: " + errorMessage);
              });
            }
          })
          .catch(error => {
            alert(error.message);
          });
      } else {
        alert(`Faltan jugadores para comenzar la partida.`);
      }
    }
  }

  function getFriends() {
    fetch(`/api/v1/jugador/amigos?userId=${usuario.id}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then(response => response.text())
      .then(data => {
        const amigos = JSON.parse(data);
        if (amigos.length !== 0) {
          const jugadoresEnPartida = jugadores.map(j => j.player.id);
          const amigosFiltrados = amigos.filter(amigo => !jugadoresEnPartida.includes(amigo.id) && amigo.isConnected);
          setFriends(amigosFiltrados);
        } else {
          setFriends([]);
        }
      })
      .catch(message => alert(message));
    showFriendList(!friendList);
  }

  async function findChatId(friendId) {
    try {
      const response = await fetch(`/api/v1/chat/with/${friendId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error("Error al buscar el chat: " + errorMessage);
      }
      const data = await response.json();
      return data.id;
    } catch (error) {
      alert("Error al buscar el chat: " + error.message);
      throw error;
    }
  }

  async function sendInvitation(friendId) {
    try {
      const chatId = await findChatId(friendId);
      const cliente = new Client({
        brokerURL: "ws://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/ws",
        connectHeaders: {
          Authorization: `Bearer ${jwt}`,
        },
      });

      const connectPromise = new Promise((resolve, reject) => {
        cliente.onConnect = () => {
          console.log("Conectado exitosamente");
          cliente.subscribe(`/topic/chat/${chatId}`);
          setStompClient(cliente);
          resolve(cliente);
        };
        cliente.onDisconnect = () => {
          console.log("Desconectado del servidor STOMP");
        };
        cliente.onStompError = (frame) => {
          console.error("Error de STOMP: ", frame.headers["message"]);
          console.error("Detalles: ", frame.body);
          reject(new Error("Error de STOMP: " + frame.headers["message"]));
        };
      });

      cliente.activate();
      const connectedClient = await connectPromise;
      evtEnviarMensaje(chatId, connectedClient);
    } catch (error) {
      console.error("Error en sendInvitation: ", error.message);
    }
  }

  const evtEnviarMensaje = (chatId, connectedClient) => {
    if (connectedClient && connectedClient.connected) {
      connectedClient.publish({
        destination: "/app/mensaje",
        body: JSON.stringify({
          contenido: `Te invito a la partida {${game.codigo}}`,
          chat: { id: chatId },
          remitente: { id: usuario.id, username: usuario.username },
          username: { username: usuario.username },
        }),
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("Mensaje enviado");
      connectedClient.deactivate();
      alert("Invitación enviada");
    } else {
      console.error("STOMP aún no está listo o no está conectado");
    }
  };

  // -------------------------- END Original WaitingModal logic --------------------------

  return (
    <>
      {/* Music Controls (positioned as in the first code) */}
      <div
        style={{
          position: 'fixed',
          right: '1%',
          top: '2%',
          zIndex: 1,
        }}
      >
        <CSSTransition in={!isPlaying} timeout={300} classNames="join-modal" unmountOnExit>
          <button onClick={handlePlayMusic} className="play-music-button">
            <span className="swirl-glow-textMusica"> 🎵 </span>
          </button>
        </CSSTransition>
        <CSSTransition in={isPlaying} timeout={300} classNames="join-modal" unmountOnExit>
          <div style={{ position: 'relative' }}>
            <button onClick={handlePauseMusic} className="play-music-button">
              ⏸
            </button>
            <button
              onClick={() => setVolumeVisible(!volumeVisible)}
              className="play-music-button"
              style={{ marginTop:"77px",marginRight:"17px" ,position: "fixed" }}
            >
              X
            </button>
            <CSSTransition in={volumeVisible} timeout={300} classNames="join-modal" unmountOnExit>
              <div className="volume-slider-container">
                <label htmlFor="volume-slider">Volume:</label>
                <input
                  type="range"
                  id="volume-slider"
                  min="0"
                  max="100"
                  value={volume}
                  onChange={handleVolumeChange}
                />
                <span>{volume}%</span>
              </div>
            </CSSTransition>
          </div>
        </CSSTransition>
        {/* Hidden audio element */}
        <audio ref={audioRef} src={backgroundMusic} loop style={{ display: 'none' }} />
      </div>

      {/* Waiting Modal Background and content */}
      <div className="waiting-modal-background">
        <div className="cuadro-unionEquipos">
          <FaUserFriends
            style={{ position: 'absolute', right: '23%', width: '30px', height: '30px', cursor: 'pointer', color:"black" }}
            onClick={getFriends}
          />
          <CSSTransition in={friendList} timeout={300} classNames="join-modal" unmountOnExit>
            <div className="cuadro-creacion" style={{ position: 'absolute', right: '10px', top: '165px', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', borderRadius: '10px', padding: '10px', zIndex: 1000 }}>
              <div style={{ overflowY: 'auto', height: '200px', width: '200px' }}>
                <p style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>Invitar amigos</p>
                {friends.map((friend) => (
                  <div key={friend.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '10px' }}>
                    <img
                      style={{ height: '40px', width: '40px', borderRadius: '50%', marginRight: '10px' }}
                      src={friend.photo}
                      alt="Foto de perfil del usuario"
                    />
                    <p style={{ margin: 0, flex: 1, textAlign: 'left' }}>{friend.userName}</p>
                    <IoIosSend
                      style={{ width: '20px', height: '20px', cursor: 'pointer', marginLeft: '10px', color: 'black' }}
                      onClick={() => sendInvitation(friend.id)}
                    />
                  </div>
                ))}
              </div>
            </div>
          </CSSTransition>
          <IoCloseCircle
            style={{ width: 30, height: 30, cursor: "pointer", position: 'absolute', color: "rgb(123, 27, 0)" }}
            onClick={() => setLeavingModal(true)}
          />

          <div style={{ textAlign: 'center' }}>
            <h1>Partida {game.codigo}</h1>
            <h2>{connectedUsers}/{game.numJugadores} Jugadores conectados</h2>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', marginRight: 10 }}>
              <p style={{ margin: 0 }}>{game.puntosMaximos} puntos</p>
              {game.conFlor ? (
                <TbFlower style={{ verticalAlign: 'middle', marginLeft: 5, color:"black" }} />
              ) : (
                <TbFlowerOff style={{ marginLeft: 5, color:"black" }} />
              )}
            </div>

            <div style={{ columnCount: 2, textAlign: 'center' }}>
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center' }}>
                <h2 className='loginText'>Equipo 1</h2>
                <EquipoView
                  partida={game}
                  jugadores={getJugadoresEquipo(1)}
                  gameCreator={getGameCreator()}
                />
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center' }}>
                <h2 className='equipo2Text'>Equipo 2</h2>
                <EquipoView
                  partida={game}
                  jugadores={getJugadoresEquipo(2)}
                  gameCreator={getGameCreator()}
                />
              </div>
            </div>

            {getGameCreator()?.id === usuario.id && (
              <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: 20, width: '100%' }}>
                <button
                  onClick={startGame}
                  className="button"
                  style={{ color: 'black', width: '20%' }}
                >
                  Comenzar partida
                </button>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Modals (overlay if necessary) */}
      <LeavingGameModal modalIsOpen={leavingModal} setIsOpen={setLeavingModal} />
      <ExpeledModal modalIsOpen={expeledView} />
    </>
  );
});

export default WaitingModal;

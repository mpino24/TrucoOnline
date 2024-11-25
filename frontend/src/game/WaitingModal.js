import { useState, forwardRef, useEffect } from 'react';
import { IoCloseCircle } from "react-icons/io5";

import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import EquipoView from 'frontend/src/game/EquipoView.js'
import LeavingGameModal from '../components/LeavingGameModal';
import tokenService from "frontend/src/services/token.service.js";
import { useNavigate } from 'react-router-dom'
import ExpeledModal from './ExpeledModal';

const WaitingModal = forwardRef((props, ref) => {
    const game = props.game;

    const [connectedUsers, setConnectedUsers] = useState(0);
    const [jugadores, setJugadores] = useState([]);
    const [leavingModal, setLeavingModal] = useState(false);
    const usuario = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();
    const [connected,setConnected] = useState(null);
    const [expeledView,setExpeledView]= useState(false);
    const navigate = useNavigate();


    useEffect(() => {
        function fetchPlayers() {
            fetch(
                `/api/v1/partidajugador/players?partidaCode=` + game.codigo,
                {
                    method: "GET"
                }
            )
                .then((response) => response.json())
                .then((data) => {
                    setJugadores(data)
                    setConnectedUsers(data.length)
                    const isConnected = (jugadores.find(pj=> pj.player.id===usuario.id) ? true: false);
                    if(connected && !isConnected){
                        setExpeledView(true);
                    }
                    setConnected(isConnected);
                })
        }
        fetchPlayers();

        const intervalId = setInterval(fetchPlayers, 500);

        return () => clearInterval(intervalId)
    }, [connected, game.codigo, jugadores, navigate, usuario.id])

    function getGameCreator() {
        if (jugadores.length > 0) {
            const currentCreator = jugadores.find(pj => pj.isCreator === true)
            return currentCreator.player;
        } else {
            return null;
        }


    }

    function getJugadoresEquipo(equipo) {
        if (jugadores.length > 0) {

            return jugadores.filter(jugadorPartida => jugadorPartida.equipo === "EQUIPO" + equipo);
        } else {
            return [];
        }

    }

    function startGame(){
        if(getJugadoresEquipo(1).length===game.numJugadores/2 && getJugadoresEquipo(2).length===game.numJugadores/2 ){
            fetch(
                `/api/v1/partida/${game.codigo}/start`,
                {
                    method: "PATCH",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                      },
                }
            )
            .then((response) => {
                if (!response.ok) {
                    return response.text().then((errorMessage) => {
                        throw new Error("Error al empezar la partida: " + errorMessage);
                    });
                }
            })
            .then(() => {

            })
            .catch((error) => {
                alert(error.message);
            });

        }else{
            alert(`Faltan jugadores para comenzar la partida. Equipo 1:${getJugadoresEquipo(1).length}. Equipo 2:${getJugadoresEquipo(2).length}`)
        }

    }

    return (
        <>
            <div className='cuadro-union'>
                <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: 'absolute' }} onClick={() => setLeavingModal(true)} />
                <div style={{ textAlign: 'center' }}>
                    <h1>Partida {game.codigo}</h1>
                    <h2>{connectedUsers}/{game.numJugadores} Jugadores conectados</h2>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', marginRight: 10 }}>
                        <p style={{ margin: 0 }}>{game.puntosMaximos} puntos</p>
                        {game.conFlor && <TbFlower style={{ verticalAlign: 'middle', marginLeft: 5 }} />}
                        {!game.conFlor && <TbFlowerOff style={{ marginLeft: 5 }} />}
                    </div>
                    <div style={{ columnCount: 2, textAlign: 'center' }}>
                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            textAlign: 'center'
                        }}>
                            <h2 style={{ color: 'blue' }}>Equipo 1</h2>
                            <EquipoView
                                partida={game}
                                jugadores={getJugadoresEquipo(1)}
                                gameCreator={getGameCreator()}
                            />
                        </div>

                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            textAlign: 'center'
                        }}>
                            <h2 style={{ color: 'red' }}>Equipo 2</h2>
                            <EquipoView
                                partida={game}
                                jugadores={getJugadoresEquipo(2)}
                                gameCreator={getGameCreator()}
                            />

                        </div>

                    </div>
                    {getGameCreator() && getGameCreator().id===usuario.id && (
                        <div style={{
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            marginTop: 20,
                            width: '100%',
                        }}>
                            <button
                                onClick={()=> startGame()}
                                className="button"
                                style={{ color: 'darkgreen', width: '20%', left:0 }}
                            >
                                Comenzar partida
                            </button>
                        </div>
                    )}
                </div>
            </div>
            <LeavingGameModal
                modalIsOpen={leavingModal}
                setIsOpen={setLeavingModal} />
            <ExpeledModal
                modalIsOpen={expeledView}/>
        </>
    );




});
export default WaitingModal;
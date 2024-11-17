import { useState, forwardRef, useEffect } from 'react';
import { IoCloseCircle } from "react-icons/io5";

import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import EquipoView from 'frontend/src/game/EquipoView.js'
import LeavingGameModal from '../components/LeavingGameModal';
import tokenService from "frontend/src/services/token.service.js";

const WaitingModal = forwardRef((props, ref) => {
    const game = props.game;

    const [connectedUsers, setConnectedUsers] = useState(0);
    const [jugadores, setJugadores] = useState([]);
    const [leavingModal, setLeavingModal] = useState(false);
    const usuario = tokenService.getUser();



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

                })
        }
        fetchPlayers();

        const intervalId = setInterval(fetchPlayers, 500);

        return () => clearInterval(intervalId)
    }, [game.codigo])

    function iAmCreator() {
        if (jugadores.length > 0) {
            const currentJugadorPartida = jugadores.find(pj => pj.player.id === usuario.id)
            return currentJugadorPartida.isCreator;
        } else {
            return false;
        }


    }









    function getJugadoresEquipo(equipo) {
        if (jugadores.length > 0) {

            return jugadores.filter(jugadorPartida => jugadorPartida.equipo === "EQUIPO" + equipo);
        } else {
            return [];
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
                            />

                        </div>

                    </div>
                    {iAmCreator() && (
                        <div style={{
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            marginTop: 20,
                            width: '100%',
                        }}>
                            <button
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
        </>
    );




});
export default WaitingModal;
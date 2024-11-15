
import { useState, forwardRef, useEffect } from 'react';
import { IoCloseCircle } from "react-icons/io5";
import { useNavigate } from 'react-router-dom'
import tokenService from 'frontend/src/services/token.service.js';
import Modal from 'react-modal';
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import EquipoView from 'frontend/src/game/EquipoView.js'

const WaitingModal = forwardRef((props, ref) => {
    const game = props.game;
    const navigate = useNavigate();
    const [modalIsOpen, setIsOpen] = useState(false);
    const [connectedUsers, setConnectedUsers] = useState(0);
    const [jugadores, setJugadores] = useState([]);
    const [jugadoresE1,setJugadoresE1] = useState([]);
    const [jugadoresE2,setJugadoresE2] = useState([]);

    useEffect(() => {
        fetch(
            `/api/v1/partidajugador/players?partidaCode=`+game.codigo,
            {
                method: "GET"
            }
        )
            .then((response) => response.json())
            .then((data) => {
                setJugadores(data)
                setJugadoresE1(jugadores.filter(jugadorPartida => jugadorPartida.equipo=== "EQUIPO1"))
                setJugadoresE2(jugadores.filter(jugadorPartida => jugadorPartida.equipo=== "EQUIPO2"))
                setConnectedUsers(data.length)

            })


    })


    const customModalStyles = {
        content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            width: '400px',
            padding: '20px',
            borderRadius: '10px',
            boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
            textAlign: 'center',
            border: 'none'
        },
        overlay: {
            backgroundColor: 'rgba(0, 0, 0, 0.6)'
        }
    };




    function closeModal() {
        setIsOpen(false);
    }

    function leaveGame() {
        fetch(
            "/api/v1/partidajugador?userId=" + tokenService.getUser().id,
            {
                method: "DELETE"
            }
        )
            .then((response) => response.text())
            .then((data) => {
                setIsOpen(false);
                navigate("/home");
            })
            .catch((message) => alert(message));
    }

    function getJugadoresEquipo(equipo){
        if(jugadores.length>0){

            return jugadores.filter(jugadorPartida => jugadorPartida.equipo=== "EQUIPO"+equipo);
        }else{
            return [];
        }

    }

    return (
        <div className='cuadro-union'>
            <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: 'absolute' }} onClick={() => setIsOpen(true)} />
            <div style={{ textAlign: 'center' }}>
                <h1>Partida {game.codigo}</h1>
                <h2>{connectedUsers}/{game.numJugadores} Jugadores conectados</h2>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', marginRight: 10 }}>
                    <p style={{ margin: 0 }}>{game.puntosMaximos} puntos</p>
                    {game.conFlor && <TbFlower style={{ verticalAlign: 'middle', marginLeft: 5 }} />}
                    {!game.conFlor && <TbFlowerOff style={{ marginLeft: 5 }} />}
                </div>
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
                        jugadores={jugadoresE1}
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
                        jugadores={jugadoresE2}
                    />
                </div>
            </div>




























            <Modal
                isOpen={modalIsOpen}
                onRequestClose={closeModal}
                style={customModalStyles}
                ariaHideApp={false}
            >
                <h2 style={{ marginBottom: '20px', color: '#333' }}>¿Quieres abandonar la partida?</h2>
                <img src="https://c.tenor.com/vkvU9Fi4uOsAAAAC/tenor.gif" alt="Mono GIF" style={{ width: '100%', height: 'auto', marginBottom: '20px' }} />
                <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                    <button
                        onClick={leaveGame}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#ff4d4d',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontSize: '16px',
                            transition: 'background-color 0.3s ease',
                            marginRight: '10px'
                        }}
                        onMouseEnter={(e) => (e.target.style.backgroundColor = '#ff3333')}
                        onMouseLeave={(e) => (e.target.style.backgroundColor = '#ff4d4d')}
                    >
                        Sí, abandonar partida
                    </button>
                    <button
                        onClick={closeModal}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#4CAF50',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontSize: '16px',
                            transition: 'background-color 0.3s ease'
                        }}
                        onMouseEnter={(e) => (e.target.style.backgroundColor = '#45a049')}
                        onMouseLeave={(e) => (e.target.style.backgroundColor = '#4CAF50')}
                    >
                        No, me quedo
                    </button>
                </div>
            </Modal>
        </div>


    );




});
export default WaitingModal;
import { useEffect } from 'react';
import { useState, forwardRef } from 'react';
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import tokenService from '../services/token.service.js';
import { useNavigate } from 'react-router-dom';

const PartidaView = forwardRef((props, ref) => {
    const [game, setGame] = useState(props.game);
    const [connectedUsers, setConnectedUsers] = useState(0);
    const jwt = tokenService.getLocalAccessToken();
    const navigate = useNavigate();

    useEffect(() => {
        if (game.estado === 'WAITING') {
            fetch(
                "/api/v1/partidajugador/numjugadores?partidaId=" + game.id,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        Accept: "application/json",
                        "Content-Type": "application/json",
                      }
                }
            )
                .then((response) => response.text())
                .then((data) => {
                    setConnectedUsers(JSON.parse(data));
                })
                .catch((message) => alert(message));
        } else {
            setConnectedUsers(game.numJugadores);
        }
    }, [game.estado, game.id, game.numJugadores]);

    function handleSubmit() {
        fetch(
            "/api/v1/partidajugador/" + game.id,
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,

                    Accept: "application/json",
                    "Content-Type": "application/json",
                  }
                },

            
        )
            .then((response) => {
                response.text();
                navigate(`/partidas?partidaCode=${game.codigo}`);
            })
            .catch((message) => alert(message));
    }

    return (
        <div style={{ display: 'flex', flexDirection: props.interfaz === 'chat' ? 'column' : 'row', alignItems: props.interfaz === 'chat' ? 'flex-start' : 'center', gap: '10px',height:50 }}>
            <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', gap: '10px' }}>
                <p style={{ fontSize: 18, textDecoration: 'underline' }}>Partida {game.codigo}:</p>
                <p style={{ marginLeft: 10 }}>{game.puntosMaximos} puntos</p>
                <p>
                    {game.conFlor && <TbFlower style={{ verticalAlign: 'middle' }} />}
                    {!game.conFlor && <TbFlowerOff />}
                </p>
                {game.estado !=='FINISHED' && 
                 <p>{connectedUsers}/{game.numJugadores} jugadores</p>
                }
               
            </div>
            <div style={{ display: 'flex', flexDirection: props.interfaz === 'chat' ? 'column' : 'row', alignItems: 'center', gap: '10px', marginTop: props.interfaz === 'chat' ? '10px' : '0' }}>
                {game && connectedUsers < game.numJugadores && game.estado === 'WAITING' &&
                    <button className="button" style={{ color: 'brown' }} onClick={() => handleSubmit()}>
                        Unirse
                    </button>
                }
                {game && game.estado !== 'FINISHED' &&
                    <button className="button" style={{ color: 'darkgreen' }} onClick={() => navigate(`/partidas?partidaCode=${game.codigo}`)}>
                        Ver
                    </button>
                }
                {game && game.estado === 'FINISHED' &&
                <p>Partida finalizada</p>}
            </div>
        </div>
    );
});

export default PartidaView;

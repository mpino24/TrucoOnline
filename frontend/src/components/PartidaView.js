import { useEffect } from 'react';
import { useState, forwardRef } from 'react';
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';
import tokenService from 'frontend/src/services/token.service.js';

const user = tokenService.getUser();

const PartidaView = forwardRef((props, ref) => {
    const [game, setGame] = useState(props.game);
    const [connectedUsers, setConnectedUsers] = useState(0);

    useEffect(() => {
        if (game.estado === 'WAITING') {
            fetch(
                "/api/v1/partidajugador/numjugadores?partidaId=" + game.id,
                {
                    method: "GET"
                }
            )
                .then((response) => response.text())
                .then((data) => {
                    setConnectedUsers(JSON.parse(data))

                })
                .catch((message) => alert(message));



        } else {
            setConnectedUsers(game.numJugadores)
        }
    }, [game.estado, game.numJugadores]);

    function handleSubmit() {
        fetch(
            "/api/v1/partidajugador/" + game.id + "?userId=" + user.id,
            {
                method: "POST"
            }
        )
            .then((response) => response.text())
            .catch((message) => alert(message));

    }

    return (
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <p style={{ fontSize: 18, textDecoration: 'underline' }}>Partida {game.codigo}:</p>
            <p style={{ marginLeft: 10 }}>{game.puntosMaximos} puntos</p>
            <p>
                {game.conFlor && <TbFlower style={{ verticalAlign: 'middle' }} />}
                {!game.conFlor && <TbFlowerOff />}
            </p>
            <div style={{ display: 'flex', alignItems: 'center' }}>
            <p>{connectedUsers}/{game.numJugadores} jugadores</p>
            {
                connectedUsers < game.numJugadores &&
                    <Link
                        to={`/partidas/${game.codigo}`}
                        style={{ textDecoration: "none" }}
                    ><button class="button" style={{ margin: 10, color: 'brown' }} onClick={() => handleSubmit()} >
                            Unirse
                        </button>
                    </Link> 
            }
            </div>
            <Link
                to={`/partidas/${game.codigo}`}
                style={{ textDecoration: "none" }}
            ><button class="button" style={{ margin: 10, color: 'darkgreen' }} >
                    Ver
                </button>
            </Link>

        </div>
    )
});

export default PartidaView;
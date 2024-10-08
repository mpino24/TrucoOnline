import { useState } from 'react';
import tokenService from 'frontend/src/services/token.service.js';
import useFetchState from "frontend/src/util/useFetchState.js";
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';
import { ScrollView } from 'react-native-web';

const jwt = tokenService.getLocalAccessToken();
export default function GameList() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [games, setGames] = useFetchState(
        [],
        `/api/v1/partida`,
        jwt,
        setMessage,
        setVisible
    );

    const gamesList = games.map((game) => {
        return (
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <p>Partida {game.codigo}: {game.puntosMaximos} puntos</p>
                {game.conFlor && <TbFlower style={{ margin: 10 }} />}
                {!game.conFlor && <TbFlowerOff style={{ margin: 10 }} />}
                {
                        game.estado === 'WAITING' &&
                        
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                        <p>N/{game.numJugadores} jugadores</p>
                            <Link
                                to={`/partidas/${game.codigo}`}
                                style={{ textDecoration: "none" }}
                            ><button class="button" style={{ margin: 10, color: 'brown' }} >
                                    Unirse
                                </button>
                            </Link>

                            <Link
                                to={`/partidas/${game.codigo}`}
                                style={{ textDecoration: "none" }}
                            ><button class="button" style={{ margin: 10, color: 'darkgreen' }} >
                                    Ver
                                </button>
                            </Link>
                        </div>
                }
                {
                    game.estado === 'ACTIVE' &&
                    <>
                        <p>{game.numJugadores}/{game.numJugadores} jugadores</p>
                        <Link
                            to={`/partidas/${game.codigo}`}
                            style={{ textDecoration: "none" }}
                        ><button class="button" style={{ margin: 10, color: 'darkgreen' }} >
                                Ver
                            </button>
                        </Link>
                    </>
                }




            </div>
        )
    });

    return (
        <>
            <p>{gamesList}</p>
        </>
        
    )
}
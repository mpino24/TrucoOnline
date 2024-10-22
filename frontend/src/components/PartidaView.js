import { useState, forwardRef } from 'react';
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';


const PartidaView =forwardRef((props, ref)  => {
    const [game,setGame] = useState(props.game)


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

export default PartidaView;
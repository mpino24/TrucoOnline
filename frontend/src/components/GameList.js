import { useState } from 'react';
import tokenService from 'frontend/src/services/token.service.js';
import useFetchState from "frontend/src/util/useFetchState.js";
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";

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
                {game.conFlor && <TbFlower />}
                {!game.conFlor && <TbFlowerOff />}
                
                
                
                
                
                {
                    game.estado === 'WAITING' &&
                    <>
                    <button class="button">
                        Unirse
                    </button>
                    <button class="button" style={{ color: 'blue' }}>
                    Ver
                </button>
                    </>
                }
                {
                    game.estado === 'ACTIVE' &&
                    <button class="button" style={{ color: 'blue' }}>
                    Ver
                </button>
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
import { useState } from 'react';
import tokenService from 'frontend/src/services/token.service.js';
import useFetchState from "frontend/src/util/useFetchState.js";


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
                <p>{game.codigo}</p>

            </div>
        )
    });

    return (
        <>
            <p>{gamesList}</p>
        </>
    )
}
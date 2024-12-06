import { useEffect, useState } from 'react';
import tokenService from '../services/token.service.js';
import useFetchState from "../util/useFetchState.js";
import PartidaView from './PartidaView';

const jwt = tokenService.getLocalAccessToken();
export default function GameList() {
    const [pagina, setPagina] = useState(0);
    const [tamaño, setTamaño] = useState(4);
    const [games, setGames] = useFetchState([]);

    useEffect(() => {
        fetch(
            `/api/v1/partida/partidas/accesibles?page=${pagina}&size=${tamaño}`,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${jwt}`,
                }
            }
        )
            .then((response) => response.text())
            .then((data) => {
                if (data.content) {
                    setGames(data.content)
                }
                else {
                    setGames([])

                }
            })
            .catch((message) => alert("Error: " + message));
    }, [pagina, setGames, tamaño]);


    function handleNextPage() {
        setPagina(pagina + 1);
    }

    const gamesList = games.map((partida) => {
        return (
            <PartidaView
                game={partida}
            />
        )
    });
    return (
        <div>
            <p style={{ overflowY: true }}>{gamesList}</p>
            <button onClick={() => handleNextPage()}><p>Next Page</p></button>
        </div>

    )
}
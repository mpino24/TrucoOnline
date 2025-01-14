import { useEffect,forwardRef } from 'react';
import tokenService from '../services/token.service.js';
import useFetchState from "../util/useFetchState.js";
import PartidaView from './PartidaView';

const jwt = tokenService.getLocalAccessToken();
const GameList= forwardRef((props, ref) => {

    const [games, setGames] = useFetchState([]);


    useEffect(() => {
        fetch(
            `https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/partida/partidas/accesibles?page=${props.pagina}&size=4`,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${jwt}`,
                }
            }
        )
            .then((response) => response.json())
            .then((data) => {
                if (data.content) {
                    setGames(data.content)
                    props.setTotalPaginas(data.totalPages)
                }
                else {
                    setGames([])

                }
            })
            .catch((message) => alert("Error: " + message));
    }, [props, setGames]);




    const gamesList = games.map((partida) => {
        return (
            <PartidaView
                game={partida}
            />
        )
    });
    return (
        <div>
            <p style={{ overflowY: 'auto' }}>{gamesList}</p>
        </div>

    )
});
export default GameList;
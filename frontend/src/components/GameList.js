import { useState } from 'react';
import tokenService from 'frontend/src/services/token.service.js';
import useFetchState from "frontend/src/util/useFetchState.js";
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';
import { ScrollView } from 'react-native-web';
import PartidaView from './PartidaView';

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

    const gamesList = games.map((partida) => {
        return (
            <PartidaView
            game={partida}
            />
        )
    });
    return (
        <>
            <p>{gamesList}</p>
        </>
        
    )
}
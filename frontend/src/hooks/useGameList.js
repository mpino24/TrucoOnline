import { useState, useFetchState } from 'react';
import tokenService from 'frontend/src/services/token.service.js';
const jwt = tokenService.getLocalAccessToken();
export default function useGameList(){
const [message, setMessage] = useState(null);
const [visible, setVisible] = useState(false);
const [games, setGames] = useFetchState(
    [],
    `/api/v1/users`,
    jwt,
    setMessage,
    setVisible
);
return(
    games
);
}
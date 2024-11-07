import { useState } from 'react';
import tokenService from 'frontend/src/services/token.service.js';
import useFetchState from "frontend/src/util/useFetchState.js";
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';
import { ScrollView } from 'react-native-web';
import PartidaView from './PartidaView';
import JugadorView from './JugadorView';

const user = tokenService.getUser();
const jwt = tokenService.getLocalAccessToken();
export default function JugadorList() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [jugadores, setJugadores] = useFetchState(
        [],
        `/api/v1/jugador/amigos?userId=`+user.id,
        jwt,
        setMessage,
        setVisible
    );

    const jugadoresList = jugadores.map((player) => {
        return (
            
            <JugadorView
            jugador={player}
            />
        )
    });
    return (
        <div>
            <p style={{overflowY:true}}>{jugadoresList}</p>
        </div>
        
    )
}